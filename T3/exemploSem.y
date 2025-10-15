
%{
  import java.io.*;
%}


%token IDENT, INT, DOUBLE, BOOL, NUM, STRING
%token LITERAL, AND, VOID, MAIN, IF
%token STRUCT

%right '='
%nonassoc '>'
%left '+'
%left AND
%left '['
%left '.' // Adicionado pro acesso a campos de struct

%type <sval> IDENT
%type <ival> NUM
%type <obj> type
%type <obj> exp

%%

// Faz o parser de várias declarações intercaladas entre structs e variáveis globais
prog : { currClass = ClasseID.VarGlobal; } globalDeclList main ;

globalDeclList : globalDecl globalDeclList
               |
               ;

globalDecl : decl
           | declStruct
           ;

// Faz o parser especificamente de declarações de structs
declStructList : declStruct declStructList
               |
               ;

declStruct : STRUCT IDENT {
                            TS_entry aux = ts.pesquisa($2);
                            if (aux != null)
                            {
                              yyerror("struct >" + $2 + "< jah declarada");
                            }
                            else
                            {
                              currentStruct = new TS_entry($2, Tp_STRUCT, ClasseID.NomeStruct);
                              ts.insert(currentStruct);
                              currClass = ClasseID.CampoStruct;  // Muda contexto para declaração de campos de struct
                            }
                          }

                          '{' dList '}' ';' // Parse dos campos da struct

                          {
                            currentStruct = null;  // Sai do contexto da struct
                            currClass = ClasseID.VarGlobal;  // Volta ao contexto global para fazer o parse de outras declarações
                          }

// Faz o parser especificamente de declarações de variáveis globais e de campos de Structs
dList : decl dList
      |
      ;

decl : type  { currentType = (TS_entry)$1; } TArray Lid ';' ;

Lid : Lid  ',' id
    | id
    ;

id : IDENT   {
                TS_entry nodo;

                // Verifica o contexto atual
                if (currClass == ClasseID.CampoStruct && currentStruct != null)
                {
                  // Pesquisa e insere na tabela de campos da struct
                  nodo = currentStruct.getCampos().pesquisa($1);
                  if (nodo != null)
                    yyerror("(sem) campo >" + $1 + "< jah declarado na struct");
                  else
                    currentStruct.getCampos().insert(new TS_entry($1, currentType, currClass));
                }
                else
                {
                  // Pesquisa e insere na tabela global
                  nodo = ts.pesquisa($1);
                  if (nodo != null)
                    yyerror("(sem) variavel >" + $1 + "< jah declarada");
                  else
                    ts.insert(new TS_entry($1, currentType, currClass));
                }
             }
    ;

TArray : '[' NUM ']'  TArray { currentType = new TS_entry("?", Tp_ARRAY, currClass, $2, currentType); }
       |
       ;
             //
              // faria mais sentido reconhecer todos os tipos como ident!
              //
type : INT     { $$ = Tp_INT; }
     | DOUBLE  { $$ = Tp_DOUBLE; }
     | BOOL    { $$ = Tp_BOOL; }
     | STRING  { $$ = Tp_STRING; }
     | IDENT   { // Tratamento para os tipos definidos pelo usuário
                 TS_entry nodo = ts.pesquisa($1);
                 if (nodo == null)
                 {
                   yyerror("(sem) tipo <" + $1 + "> nao declarado");
                   $$ = Tp_ERRO;
                 }
                 else if (nodo.getTipo() != Tp_STRUCT)
                 {
                   yyerror("(sem) <" + $1 + "> nao eh um tipo struct");
                   $$ = Tp_ERRO;
                 }
                 else
                 {
                   $$ = nodo;  // Retorna a entrada da struct
                 }
               }
     ;

main :  VOID MAIN '(' ')' bloco ;

bloco : '{' listacmd '}';

listacmd : listacmd cmd
         |
         ;

cmd :  exp ';'
      | IF '(' exp ')' cmd   {
                                TS_entry tipoExp = getTipoExp((TS_entry)$3);
                                if (tipoExp != Tp_BOOL)
                                  yyerror("(sem) expressão (if) deve ser lógica " + tipoExp.getTipoStr());
                             }
      ;


exp : exp '+' exp { $$ = validaTipo('+', (TS_entry)$1, (TS_entry)$3); }
    | exp '>' exp { $$ = validaTipo('>', (TS_entry)$1, (TS_entry)$3); }
    | exp AND exp { $$ = validaTipo(AND, (TS_entry)$1, (TS_entry)$3); }
    | NUM         { $$ = Tp_INT; }
    | '(' exp ')' { $$ = $2; }
    | IDENT       { TS_entry nodo = ts.pesquisa($1);
                    if (nodo == null)
                    {
                      yyerror("(sem) var <" + $1 + "> nao declarada");
                      $$ = Tp_ERRO;
                    }
                    else
                    {
                      $$ = nodo;  // Retorna a entrada e não apenas o tipo
                    }
                  }
     | exp '=' exp  {  $$ = validaTipo(ATRIB, (TS_entry)$1, (TS_entry)$3);  }
     | exp '[' exp ']'  {
                           TS_entry tipoIndice = getTipoExp((TS_entry)$3);
                           TS_entry tipoArray = getTipoExp((TS_entry)$1);

                           if (tipoIndice != Tp_INT)
                            yyerror("(sem) indexador não é numérico ");
                           else
                            if (tipoArray.getTipo() != Tp_ARRAY)
                              yyerror("(sem) elemento não indexado ");
                            else
                              $$ = tipoArray.getTipoBase();
                         }
     | exp '.' IDENT  { // Tratamento para acesso a campos de struct em expressoes
                        TS_entry expStruct = (TS_entry)$1;

                        // Verifica se o lado esquerdo é uma struct
                        if (expStruct == Tp_ERRO)
                        {
                          $$ = Tp_ERRO;
                        }
                        else
                        {
                          // Extrai o tipo da expressão
                          TS_entry tipoExp = getTipoExp(expStruct);

                          // Verifica se o tipo é uma struct
                          if (tipoExp == null || tipoExp.getTipo() != Tp_STRUCT)
                          {
                            yyerror("(sem) operador '.' usado em nao-struct");
                            $$ = Tp_ERRO;
                          }
                          else
                          {
                            // Busca o campo na tabela de campos da struct
                            TS_entry structDef = tipoExp;
                            TS_entry campo = structDef.getCampos().pesquisa($3);

                            if (campo == null)
                            {
                              yyerror("(sem) campo <" + $3 + "> nao existe na struct <" + structDef.getId() + ">");
                              $$ = Tp_ERRO;
                            }
                            else
                            {
                              // Retorna o campo (não apenas seu tipo) pra permitir acesso aninhado
                              $$ = campo;
                            }
                          }
                        }
                      }
    ;

%%

  private Yylex lexer;
  private TabSimb ts;

  public static TS_entry Tp_INT =  new TS_entry("int", null, ClasseID.TipoBase);
  public static TS_entry Tp_DOUBLE = new TS_entry("double", null,  ClasseID.TipoBase);
  public static TS_entry Tp_BOOL = new TS_entry("bool", null,  ClasseID.TipoBase);
  public static TS_entry Tp_STRING = new TS_entry("string", null,  ClasseID.TipoBase); // Adicionado pro tipo string, pois o código base não suportava strings
  public static TS_entry Tp_ARRAY = new TS_entry("array", null,  ClasseID.TipoBase);
  public static TS_entry Tp_ERRO = new TS_entry("_erro_", null,  ClasseID.TipoBase);
  public static TS_entry Tp_STRUCT = new TS_entry("struct", null,  ClasseID.TipoBase);

  public static final int ARRAY = 1500;
  public static final int ATRIB = 1600;

  private String currEscopo;
  private ClasseID currClass;
  private TS_entry currentType;
  private TS_entry currentStruct;  // Guarda a struct sendo declarada

  public static void main(String args[]) throws IOException
  {
    System.out.println("\n\nVerificador semantico simples\n");

    Parser yyparser;
    if (args.length > 0)
    {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }
    else
    {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("Programa de entrada:\n");
      yyparser = new Parser(new InputStreamReader(System.in));
    }

    yyparser.yyparse();
    yyparser.listarTS();
    System.out.print("\n\nFeito!\n");
  }

  public Parser(Reader r)
  {
    lexer = new Yylex(r, this);
    ts = new TabSimb();

    //
    // não me parece que necessitem estar na TS
    // já que criei todas como public static...
    //
    ts.insert(Tp_ERRO);
    ts.insert(Tp_INT);
    ts.insert(Tp_DOUBLE);
    ts.insert(Tp_BOOL);
    ts.insert(Tp_STRING); // Adicionado pro tipo string, pois o código base não suportava strings
    ts.insert(Tp_ARRAY);
    ts.insert(Tp_STRUCT);
  }

  private int yylex()
  {
    int yyl_return = -1;
    try
    {
      yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e)
    {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }

  public void yyerror(String error)
  {
    //System.err.println("Erro (linha: "+ lexer.getLine() + ")\tMensagem: "+error);
    System.err.printf("Erro (linha: %2d) \tMensagem: %s\n", lexer.getLine(), error);
  }

  public void setDebug(boolean debug)
  {
    yydebug = debug;
  }

  public void listarTS()
  {
    ts.listar();
  }

  public TS_entry validaTipo(int operador, TS_entry A, TS_entry B)
  {
    // Extrai os tipos das expressões
    TS_entry tipoA = getTipoExp(A);
    TS_entry tipoB = getTipoExp(B);

    switch (operador)
    {
      case ATRIB:
            if ((tipoA == Tp_INT && tipoB == Tp_INT) || ((tipoA == Tp_DOUBLE && (tipoB == Tp_INT || tipoB == Tp_DOUBLE))) || (tipoA == tipoB))
              return tipoA;
            else
              yyerror("(sem) tipos incomp. para atribuicao: "+ tipoA.getTipoStr() + " = "+tipoB.getTipoStr());
            break;
      case '+' :
            if (tipoA == Tp_INT && tipoB == Tp_INT)
              return Tp_INT;
            else if ( (tipoA == Tp_DOUBLE && (tipoB == Tp_INT || tipoB == Tp_DOUBLE)) || (tipoB == Tp_DOUBLE && (tipoA == Tp_INT || tipoA == Tp_DOUBLE)) )
              return Tp_DOUBLE;
            else
              yyerror("(sem) tipos incomp. para soma: "+ tipoA.getTipoStr() + " + "+tipoB.getTipoStr());
            break;
      case '>' :
              if ((tipoA == Tp_INT || tipoA == Tp_DOUBLE) && (tipoB == Tp_INT || tipoB == Tp_DOUBLE))
                return Tp_BOOL;
              else
                yyerror("(sem) tipos incomp. para op relacional: "+ tipoA.getTipoStr() + " > "+tipoB.getTipoStr());
              break;
      case AND:
              if (tipoA == Tp_BOOL && tipoB == Tp_BOOL)
                return Tp_BOOL;
              else
                yyerror("(sem) tipos incomp. para op lógica: "+ tipoA.getTipoStr() + " && "+tipoB.getTipoStr());
          break;
      }
      return Tp_ERRO;
  }

  // Função auxiliar para extrair o tipo de uma expressão
  // Se a expressão é um tipo base (Tp_INT, etc) retorna ela mesma
  // Se é uma variável/campo retorna seu tipo
  public TS_entry getTipoExp(TS_entry exp)
  {
    if (exp == Tp_INT || exp == Tp_DOUBLE || exp == Tp_BOOL || exp == Tp_STRING || exp == Tp_ERRO)
      return exp;
    else if (exp.getTipo() == null)
      return exp;  // É um tipo (como uma definição de struct)
    else
      return exp.getTipo();
  }

