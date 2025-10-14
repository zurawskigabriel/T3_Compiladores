
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

%type <sval> IDENT
%type <ival> NUM
%type <obj> type
%type <obj> exp

%%

// Faz o parser de várias declarações intercaladas entre structs e variáveis globais (ALTERADO)
prog : { currClass = ClasseID.VarGlobal; } globalDeclList main ;

globalDeclList : globalDecl globalDeclList
               |
               ;

globalDecl : decl
           | declStruct
           ;

// Faz o parser especificamente de declarações de structs (ADICIONADO)
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

// Faz o parser especificamente de declarações de variáveis globais e de campos de Structs (ALTERADO)
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

// Todo resto (INALTERADO)
TArray : '[' NUM ']'  TArray { currentType = new TS_entry("?", Tp_ARRAY, currClass, $2, currentType); }
       |
       ;
             //
              // faria mais sentido reconhecer todos os tipos como ident!
              //
type : INT     { $$ = Tp_INT; }
     | DOUBLE  { $$ = Tp_DOUBLE; }
     | BOOL    { $$ = Tp_BOOL; }
     ;



main :  VOID MAIN '(' ')' bloco ;

bloco : '{' listacmd '}';

listacmd : listacmd cmd
         |
         ;

cmd :  exp ';'
      | IF '(' exp ')' cmd   {
                                if (((TS_entry)$3) != Tp_BOOL)
                                  yyerror("(sem) expressão (if) deve ser lógica "+((TS_entry)$3).getTipo());
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
                      $$ = nodo.getTipo();
                    }
                  }
     | exp '=' exp  {  $$ = validaTipo(ATRIB, (TS_entry)$1, (TS_entry)$3);  }
     | exp '[' exp ']'  {  if ((TS_entry)$3 != Tp_INT)
                            yyerror("(sem) indexador não é numérico ");
                           else
                            if (((TS_entry)$1).getTipo() != Tp_ARRAY)
                              yyerror("(sem) elemento não indexado ");
                            else
                              $$ = ((TS_entry)$1).getTipoBase();
                         }
    ;

%%

  private Yylex lexer;
  private TabSimb ts;

  public static TS_entry Tp_INT =  new TS_entry("int", null, ClasseID.TipoBase);
  public static TS_entry Tp_DOUBLE = new TS_entry("double", null,  ClasseID.TipoBase);
  public static TS_entry Tp_BOOL = new TS_entry("bool", null,  ClasseID.TipoBase);
  public static TS_entry Tp_ARRAY = new TS_entry("array", null,  ClasseID.TipoBase);
  public static TS_entry Tp_ERRO = new TS_entry("_erro_", null,  ClasseID.TipoBase);
  public static TS_entry Tp_STRUCT = new TS_entry("struct", null,  ClasseID.TipoBase); // (ADICIONADO)

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
    ts.insert(Tp_ARRAY);
    ts.insert(Tp_STRUCT); // (ADICIONADO)
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
    switch (operador)
    {
      case ATRIB:
            if ((A == Tp_INT && B == Tp_INT) || ((A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE))) || (A == B))
              return A;
            else
              yyerror("(sem) tipos incomp. para atribuicao: "+ A.getTipoStr() + " = "+B.getTipoStr());
            break;
      case '+' :
            if (A == Tp_INT && B == Tp_INT)
              return Tp_INT;
            else if ( (A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE)) || (B == Tp_DOUBLE && (A == Tp_INT || A == Tp_DOUBLE)) )
              return Tp_DOUBLE;
            else
              yyerror("(sem) tipos incomp. para soma: "+ A.getTipoStr() + " + "+B.getTipoStr());
            break;
      case '>' :
              if ((A == Tp_INT || A == Tp_DOUBLE) && (B == Tp_INT || B == Tp_DOUBLE))
                return Tp_BOOL;
              else
                yyerror("(sem) tipos incomp. para op relacional: "+ A.getTipoStr() + " > "+B.getTipoStr());
              break;
      case AND:
              if (A == Tp_BOOL && B == Tp_BOOL)
                return Tp_BOOL;
              else
                yyerror("(sem) tipos incomp. para op lógica: "+ A.getTipoStr() + " && "+B.getTipoStr());
          break;
      }
      return Tp_ERRO;
  }

