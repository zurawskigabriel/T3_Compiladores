//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 3 "exemploSem.y"
  import java.io.*;
//#line 19 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IDENT=257;
public final static short INT=258;
public final static short DOUBLE=259;
public final static short BOOL=260;
public final static short NUM=261;
public final static short STRING=262;
public final static short LITERAL=263;
public final static short AND=264;
public final static short VOID=265;
public final static short MAIN=266;
public final static short IF=267;
public final static short STRUCT=268;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    4,    0,    3,    3,    6,    6,    9,    9,   10,    8,
   11,   11,   13,    7,   14,   14,   15,   12,   12,    1,
    1,    1,    1,    1,    5,   16,   17,   17,   18,   18,
    2,    2,    2,    2,    2,    2,    2,    2,    2,
};
final static short yylen[] = {                            2,
    0,    3,    2,    0,    1,    1,    2,    0,    0,    7,
    2,    0,    0,    5,    3,    1,    1,    4,    0,    1,
    1,    1,    1,    1,    5,    3,    2,    0,    2,    5,
    3,    3,    3,    1,    3,    1,    3,    4,    3,
};
final static short yydefred[] = {                         1,
    0,    0,   24,   20,   21,   22,   23,    0,   13,    0,
    0,    5,    6,    9,    0,    0,    2,    3,    0,    0,
    0,    0,    0,    0,   17,    0,   16,    0,    0,    0,
    0,   14,    0,    0,   11,    0,   18,   15,   28,   25,
   10,    0,   36,   34,    0,   26,    0,    0,   27,    0,
    0,    0,    0,    0,    0,    0,    0,   29,    0,   35,
    0,    0,    0,    0,    0,   39,    0,   38,   30,
};
final static short yydgoto[] = {                          1,
    9,   48,   10,    2,   17,   11,   12,   13,    0,   19,
   30,   21,   15,   26,   27,   40,   42,   49,
};
final static short yysindex[] = {                         0,
    0, -185,    0,    0,    0,    0,    0, -251,    0, -250,
 -185,    0,    0,    0,  -77, -232,    0,    0,  -84, -217,
 -212,    6, -162,  -37,    0,  -18,    0,   10, -162,  -71,
  -77,    0, -212,  -68,    0,    2,    0,    0,    0,    0,
    0,  -40,    0,    0,   24,    0,  -15,  -26,    0,  -15,
  -38,  -15,  -15,  -15,  -15,  -15, -181,    0,  -34,    0,
  -42,   -3,  -25,  -44,  -24,    0,  -39,    0,    0,
};
final static short yyrindex[] = {                         0,
    0, -187,    0,    0,    0,    0,    0,    0,    0,    0,
 -187,    0,    0,    0, -178,    0,    0,    0,    0,    0,
    0,    0,  -45,    0,    0,    0,    0,    0,  -45,    0,
 -178,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -30,  -11,    1,    9,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   37,   70,    0,    0,    0,  -13,    0,    0,    0,
   57,   68,    0,    0,   71,    0,    0,   34,
};
final static int YYTABLESIZE=261;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         47,
   47,   57,   60,   57,   55,   14,   67,   57,   55,   29,
   33,   57,   33,   20,   16,   29,   55,   55,   55,   57,
   57,   57,   53,   54,   47,   33,   53,   54,   33,   37,
   33,   33,   58,   22,   53,   54,   53,   54,   23,   55,
   32,   32,   57,   24,   25,   28,   56,   37,   56,   31,
   34,   31,   56,   36,   39,   31,   56,   53,   54,   32,
   41,   32,   33,   50,   56,   56,   56,   31,   68,   31,
   31,    3,    4,    5,    6,   66,    7,    4,   19,   12,
   18,   37,    8,   51,   46,   35,   59,   56,   61,   62,
   63,   64,   65,   32,    3,    4,    5,    6,   37,    7,
   69,   31,    0,   38,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   43,   43,    0,   52,
   44,   44,    0,    0,    0,   52,   45,   45,    0,   52,
    0,    0,    0,   33,    0,    0,    0,   52,   52,   52,
    0,   43,    0,    0,    0,   44,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   52,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   40,   46,   41,   46,   43,  257,   41,   46,   43,   23,
   41,   46,   43,   91,  265,   29,   43,   43,   43,   46,
   46,   46,   61,   62,   40,   44,   61,   62,   59,   41,
   61,   62,   59,  266,   61,   62,   61,   62,  123,   43,
   59,   41,   46,  261,  257,   40,   91,   59,   91,   41,
   41,   43,   91,  125,  123,   93,   91,   61,   62,   59,
   59,   61,   93,   40,   91,   91,   91,   59,   93,   61,
   62,  257,  258,  259,  260,  257,  262,  265,  257,  125,
   11,   93,  268,   47,  125,   29,   50,   91,   52,   53,
   54,   55,   56,   93,  257,  258,  259,  260,   31,  262,
   67,   93,   -1,   33,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  257,   -1,  264,
  261,  261,   -1,   -1,   -1,  264,  267,  267,   -1,  264,
   -1,   -1,   -1,  264,   -1,   -1,   -1,  264,  264,  264,
   -1,  257,   -1,   -1,   -1,  261,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  264,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=268;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'",null,"'+'","','",
null,"'.'",null,null,null,null,null,null,null,null,null,null,null,null,"';'",
null,"'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"IDENT","INT","DOUBLE","BOOL","NUM",
"STRING","LITERAL","AND","VOID","MAIN","IF","STRUCT",
};
final static String yyrule[] = {
"$accept : prog",
"$$1 :",
"prog : $$1 globalDeclList main",
"globalDeclList : globalDecl globalDeclList",
"globalDeclList :",
"globalDecl : decl",
"globalDecl : declStruct",
"declStructList : declStruct declStructList",
"declStructList :",
"$$2 :",
"declStruct : STRUCT IDENT $$2 '{' dList '}' ';'",
"dList : decl dList",
"dList :",
"$$3 :",
"decl : type $$3 TArray Lid ';'",
"Lid : Lid ',' id",
"Lid : id",
"id : IDENT",
"TArray : '[' NUM ']' TArray",
"TArray :",
"type : INT",
"type : DOUBLE",
"type : BOOL",
"type : STRING",
"type : IDENT",
"main : VOID MAIN '(' ')' bloco",
"bloco : '{' listacmd '}'",
"listacmd : listacmd cmd",
"listacmd :",
"cmd : exp ';'",
"cmd : IF '(' exp ')' cmd",
"exp : exp '+' exp",
"exp : exp '>' exp",
"exp : exp AND exp",
"exp : NUM",
"exp : '(' exp ')'",
"exp : IDENT",
"exp : exp '=' exp",
"exp : exp '[' exp ']'",
"exp : exp '.' IDENT",
};

//#line 214 "exemploSem.y"

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

//#line 424 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 26 "exemploSem.y"
{ currClass = ClasseID.VarGlobal; }
break;
case 9:
//#line 41 "exemploSem.y"
{
                            TS_entry aux = ts.pesquisa(val_peek(0).sval);
                            if (aux != null)
                            {
                              yyerror("struct >" + val_peek(0).sval + "< jah declarada");
                            }
                            else
                            {
                              currentStruct = new TS_entry(val_peek(0).sval, Tp_STRUCT, ClasseID.NomeStruct);
                              ts.insert(currentStruct);
                              currClass = ClasseID.CampoStruct;  /* Muda contexto para declaração de campos de struct*/
                            }
                          }
break;
case 10:
//#line 57 "exemploSem.y"
{
                            currentStruct = null;  /* Sai do contexto da struct*/
                            currClass = ClasseID.VarGlobal;  /* Volta ao contexto global para fazer o parse de outras declarações*/
                          }
break;
case 13:
//#line 67 "exemploSem.y"
{ currentType = (TS_entry)val_peek(0).obj; }
break;
case 17:
//#line 73 "exemploSem.y"
{
                TS_entry nodo;

                /* Verifica o contexto atual*/
                if (currClass == ClasseID.CampoStruct && currentStruct != null)
                {
                  /* Pesquisa e insere na tabela de campos da struct*/
                  nodo = currentStruct.getCampos().pesquisa(val_peek(0).sval);
                  if (nodo != null)
                    yyerror("(sem) campo >" + val_peek(0).sval + "< jah declarado na struct");
                  else
                    currentStruct.getCampos().insert(new TS_entry(val_peek(0).sval, currentType, currClass));
                }
                else
                {
                  /* Pesquisa e insere na tabela global*/
                  nodo = ts.pesquisa(val_peek(0).sval);
                  if (nodo != null)
                    yyerror("(sem) variavel >" + val_peek(0).sval + "< jah declarada");
                  else
                    ts.insert(new TS_entry(val_peek(0).sval, currentType, currClass));
                }
             }
break;
case 18:
//#line 98 "exemploSem.y"
{ currentType = new TS_entry("?", Tp_ARRAY, currClass, val_peek(2).ival, currentType); }
break;
case 20:
//#line 104 "exemploSem.y"
{ yyval.obj = Tp_INT; }
break;
case 21:
//#line 105 "exemploSem.y"
{ yyval.obj = Tp_DOUBLE; }
break;
case 22:
//#line 106 "exemploSem.y"
{ yyval.obj = Tp_BOOL; }
break;
case 23:
//#line 107 "exemploSem.y"
{ yyval.obj = Tp_STRING; }
break;
case 24:
//#line 108 "exemploSem.y"
{ /* Tratamento para os tipos definidos pelo usuário*/
                 TS_entry nodo = ts.pesquisa(val_peek(0).sval);
                 if (nodo == null)
                 {
                   yyerror("(sem) tipo <" + val_peek(0).sval + "> nao declarado");
                   yyval.obj = Tp_ERRO;
                 }
                 else if (nodo.getTipo() != Tp_STRUCT)
                 {
                   yyerror("(sem) <" + val_peek(0).sval + "> nao eh um tipo struct");
                   yyval.obj = Tp_ERRO;
                 }
                 else
                 {
                   yyval.obj = nodo;  /* Retorna a entrada da struct*/
                 }
               }
break;
case 30:
//#line 136 "exemploSem.y"
{
                                TS_entry tipoExp = getTipoExp((TS_entry)val_peek(2).obj);
                                if (tipoExp != Tp_BOOL)
                                  yyerror("(sem) expressão (if) deve ser lógica " + tipoExp.getTipoStr());
                             }
break;
case 31:
//#line 144 "exemploSem.y"
{ yyval.obj = validaTipo('+', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 32:
//#line 145 "exemploSem.y"
{ yyval.obj = validaTipo('>', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 33:
//#line 146 "exemploSem.y"
{ yyval.obj = validaTipo(AND, (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 34:
//#line 147 "exemploSem.y"
{ yyval.obj = Tp_INT; }
break;
case 35:
//#line 148 "exemploSem.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 36:
//#line 149 "exemploSem.y"
{ TS_entry nodo = ts.pesquisa(val_peek(0).sval);
                    if (nodo == null)
                    {
                      yyerror("(sem) var <" + val_peek(0).sval + "> nao declarada");
                      yyval.obj = Tp_ERRO;
                    }
                    else
                    {
                      yyval.obj = nodo;  /* Retorna a entrada e não apenas o tipo*/
                    }
                  }
break;
case 37:
//#line 160 "exemploSem.y"
{  yyval.obj = validaTipo(ATRIB, (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj);  }
break;
case 38:
//#line 161 "exemploSem.y"
{
                           TS_entry tipoIndice = getTipoExp((TS_entry)val_peek(1).obj);
                           TS_entry tipoArray = getTipoExp((TS_entry)val_peek(3).obj);

                           if (tipoIndice != Tp_INT)
                            yyerror("(sem) indexador não é numérico ");
                           else
                            if (tipoArray.getTipo() != Tp_ARRAY)
                              yyerror("(sem) elemento não indexado ");
                            else
                              yyval.obj = tipoArray.getTipoBase();
                         }
break;
case 39:
//#line 173 "exemploSem.y"
{ /* Tratamento para acesso a campos de struct em expressoes*/
                        TS_entry expStruct = (TS_entry)val_peek(2).obj;

                        /* Verifica se o lado esquerdo é uma struct*/
                        if (expStruct == Tp_ERRO)
                        {
                          yyval.obj = Tp_ERRO;
                        }
                        else
                        {
                          /* Extrai o tipo da expressão*/
                          TS_entry tipoExp = getTipoExp(expStruct);

                          /* Verifica se o tipo é uma struct*/
                          if (tipoExp == null || tipoExp.getTipo() != Tp_STRUCT)
                          {
                            yyerror("(sem) operador '.' usado em nao-struct");
                            yyval.obj = Tp_ERRO;
                          }
                          else
                          {
                            /* Busca o campo na tabela de campos da struct*/
                            TS_entry structDef = tipoExp;
                            TS_entry campo = structDef.getCampos().pesquisa(val_peek(0).sval);

                            if (campo == null)
                            {
                              yyerror("(sem) campo <" + val_peek(0).sval + "> nao existe na struct <" + structDef.getId() + ">");
                              yyval.obj = Tp_ERRO;
                            }
                            else
                            {
                              /* Retorna o campo (não apenas seu tipo) pra permitir acesso aninhado*/
                              yyval.obj = campo;
                            }
                          }
                        }
                      }
break;
//#line 772 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
