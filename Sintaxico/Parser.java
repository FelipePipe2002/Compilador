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






//#line 2 "grammer.y"

package Sintaxico;

import Lexico.AnalizadorLexico;
import Lexico.Token;

//#line 24 "Parser.java"




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
public final static short IF=257;
public final static short ELSE=258;
public final static short END_IF=259;
public final static short PRINT=260;
public final static short CLASS=261;
public final static short VOID=262;
public final static short LONG=263;
public final static short UINT=264;
public final static short DOUBLE=265;
public final static short WHILE=266;
public final static short DO=267;
public final static short INTERFACE=268;
public final static short IMPLEMENT=269;
public final static short TOD=270;
public final static short ID=271;
public final static short CTE_LONG=272;
public final static short CTE_UINT=273;
public final static short CTE_DOUBLE=274;
public final static short CADENA=275;
public final static short RETURN=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    2,    4,    4,    4,    6,
    6,    8,    8,    7,    7,    9,    5,    5,   12,   13,
   13,    3,    3,    3,    3,    3,   18,   14,   15,   15,
   21,   21,   20,   23,   23,   23,   23,   23,   23,   16,
   17,   19,   19,   19,   19,   19,   25,   25,   24,   11,
   11,   22,   22,   22,   26,   26,   26,   27,   27,   28,
   28,   28,   28,   28,   28,   10,   10,   10,   10,
};
final static short yylen[] = {                            2,
    3,    1,    2,    1,    1,    1,    5,    5,    7,    1,
    2,    1,    1,    2,    1,    3,    8,    7,    2,    1,
    2,    1,    1,    1,    1,    1,    3,    1,    6,    8,
    1,    3,    3,    1,    1,    1,    1,    1,    1,    8,
    2,    1,    1,    2,    5,    5,    3,    1,    3,    3,
    1,    3,    3,    1,    3,    3,    1,    2,    1,    1,
    1,    2,    1,    1,    2,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,   68,   67,   66,    0,
    0,    0,    0,    0,    0,    2,    4,    5,    6,   42,
    0,    0,   22,   23,   24,   25,   26,   28,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   41,    1,    3,
   48,    0,    0,    0,    0,   44,    0,    0,   64,   63,
   61,    0,    0,    0,    0,    0,   57,    0,   27,    0,
    0,    0,   20,   43,    0,    0,    0,   16,    0,    0,
    0,   47,   65,   62,    0,    0,    0,   34,   35,   36,
   37,   38,   39,    0,    0,    0,   58,    0,   69,   13,
    0,   10,   12,    0,    0,    0,    0,   21,   15,    0,
    0,    0,    0,   31,    0,    0,    0,    0,   55,   56,
    0,    7,   11,    0,   19,    0,    0,    8,   14,   46,
   45,    0,    0,   29,    0,    0,    0,    0,   32,    0,
    9,   18,    0,    0,   30,   17,   40,
};
final static short yydgoto[] = {                          2,
   15,   16,   63,   18,   64,   91,  100,   92,   20,   21,
   22,   96,   65,   23,   24,   25,   26,   27,   28,   53,
  105,   54,   84,   29,   30,   56,   57,   58,
};
final static short yysindex[] = {                       -76,
 -165,    0,   26, -205, -217, -195,    0,    0,    0,  -36,
 -188,   50,    0,   57,  -85,    0,    0,    0,    0,    0,
 -143,   32,    0,    0,    0,    0,    0,    0,   90,    9,
  -40,   95,  -98,  101, -127,   22,  -40,    0,    0,    0,
    0,    6,  106,  -40, -143,    0,  -40, -111,    0,    0,
    0, -161,  121,  -16,  106,   37,    0, -118,    0, -105,
 -155,   11,    0,    0,  -66,  -94,   78,    0,   69,  106,
   79,    0,    0,    0, -106,  -40,  -40,    0,    0,    0,
    0,    0,    0,  -40,  -40,  -40,    0,   47,    0,    0,
  -44,    0,    0,   61,  -84,  132,  -78,    0,    0, -107,
  148,  158, -127,    0, -221,   37,   37,   69,    0,    0,
 -155,    0,    0, -127,    0,   84,  172,    0,    0,    0,
    0,  -54, -106,    0,   56,   21, -127,  -40,    0,  -37,
    0,    0,   42,  174,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   70,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   30,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -41,  -29,    0,  -34,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  179,   14,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -21,   -9,  183,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  210,   67,    0,  250,  117,    0,  -48,  -31,  167,
  209,    0,  -58,    0,    0,    0,    0,    0,    0,  111,
  118,   20,    0,    0,   41,   71,   64,    0,
};
final static int YYTABLESIZE=375;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         60,
   60,   60,   60,   60,   52,   60,   59,   59,   59,   59,
   59,   54,   59,   54,   54,   54,  103,  118,   60,   52,
   60,   52,   52,   52,   61,   59,   76,   59,   77,   93,
   54,   53,   54,   53,   53,   53,  123,  124,   52,   39,
   52,   48,  113,   78,  122,   79,    1,   48,   47,   68,
   53,   94,   53,   33,   48,  126,   67,   50,   97,   93,
   48,   43,   48,   69,   45,   31,   71,   17,  133,   32,
  129,   55,   50,   51,   50,   34,  113,   55,   85,   93,
  112,   17,   36,   86,   55,   70,   35,   55,   51,   37,
   45,    3,   44,   93,    4,    5,    6,    7,    8,    9,
   38,   10,   11,  108,   12,   13,    6,    7,    8,    9,
   73,   76,   74,   77,   14,   89,   55,   55,  101,  102,
   76,   76,   77,   77,   55,   55,   55,   41,   51,    3,
   51,   98,    4,   46,    6,    7,    8,    9,   59,   10,
   62,  104,   12,   13,   66,  132,  106,  107,  109,  110,
    3,   48,   14,    4,    6,    6,    7,    8,    9,   72,
   10,   75,   87,   12,   13,   88,  136,    6,   55,  111,
   60,    3,  116,   14,    4,    5,    6,    7,    8,    9,
  131,   10,   11,  114,   12,   13,  115,  117,   98,  104,
    3,  120,   98,    4,   14,    6,    7,    8,    9,   98,
   10,  121,    3,   12,   13,    4,  127,    6,    7,    8,
    9,  128,   10,   14,  137,   12,   13,    6,    7,    8,
    9,  135,   49,   33,   40,   14,   89,  125,   95,   42,
   41,   49,   50,   51,   60,   60,   60,   60,  134,   60,
  130,   59,   59,   59,   59,    0,   54,   54,   54,   54,
   19,    0,    0,    0,   52,   52,   52,   52,    0,   80,
   81,   82,   83,    0,   19,    0,   53,   53,   53,   53,
    0,    0,   69,    7,    8,    9,    0,    3,    0,    0,
    4,   89,    6,    7,    8,    9,    0,   10,    0,    0,
   12,   13,    0,    0,    0,    0,    0,    0,    3,    0,
   14,    4,    0,    6,    7,    8,    9,    0,   10,    0,
   90,   12,   13,    0,    0,   99,    0,    6,    7,    8,
    9,   14,    0,    0,    0,    0,   89,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   90,    0,    0,    0,    0,    0,    0,    0,    0,  119,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   90,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   90,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   45,   47,   41,   42,   43,   44,
   45,   41,   47,   43,   44,   45,  123,  125,   60,   41,
   62,   43,   44,   45,  123,   60,   43,   62,   45,   61,
   60,   41,   62,   43,   44,   45,  258,  259,   60,  125,
   62,   40,   91,   60,  103,   62,  123,   46,   40,   44,
   60,   41,   62,  271,   46,  114,   37,   44,  125,   91,
   59,   21,   61,   44,   59,   40,   47,    1,  127,  275,
  125,   31,   59,   44,   61,  271,  125,   37,   42,  111,
  125,   15,  271,   47,   44,   45,  123,   47,   59,   40,
   59,  257,   61,  125,  260,  261,  262,  263,  264,  265,
   44,  267,  268,   84,  270,  271,  262,  263,  264,  265,
  272,   43,  274,   45,  280,  271,   76,   77,   41,   41,
   43,   43,   45,   45,   84,   85,   86,  271,   59,  257,
   61,   65,  260,   44,  262,  263,  264,  265,   44,  267,
   40,   75,  270,  271,  123,  125,   76,   77,   85,   86,
  257,   46,  280,  260,  262,  262,  263,  264,  265,  271,
  267,   41,  281,  270,  271,  271,  125,  262,  128,  123,
  269,  257,   41,  280,  260,  261,  262,  263,  264,  265,
  125,  267,  268,  123,  270,  271,  271,  266,  122,  123,
  257,   44,  126,  260,  280,  262,  263,  264,  265,  133,
  267,   44,  257,  270,  271,  260,  123,  262,  263,  264,
  265,   40,  267,  280,   41,  270,  271,  262,  263,  264,
  265,  259,   44,   41,   15,  280,  271,  111,   62,   21,
  271,  272,  273,  274,  276,  277,  278,  279,  128,  281,
  123,  276,  277,  278,  279,   -1,  276,  277,  278,  279,
    1,   -1,   -1,   -1,  276,  277,  278,  279,   -1,  276,
  277,  278,  279,   -1,   15,   -1,  276,  277,  278,  279,
   -1,   -1,  271,  263,  264,  265,   -1,  257,   -1,   -1,
  260,  271,  262,  263,  264,  265,   -1,  267,   -1,   -1,
  270,  271,   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,
  280,  260,   -1,  262,  263,  264,  265,   -1,  267,   -1,
   61,  270,  271,   -1,   -1,   66,   -1,  262,  263,  264,
  265,  280,   -1,   -1,   -1,   -1,  271,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   91,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  100,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  111,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  125,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IF","ELSE","END_IF","PRINT","CLASS","VOID",
"LONG","UINT","DOUBLE","WHILE","DO","INTERFACE","IMPLEMENT","TOD","ID",
"CTE_LONG","CTE_UINT","CTE_DOUBLE","CADENA","\"<=\"","\">=\"","\"==\"","\"!!\"",
"RETURN","\"--\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : '{' lista_declaraciones '}'",
"lista_declaraciones : declaracion",
"lista_declaraciones : lista_declaraciones declaracion",
"declaracion : sentencia",
"declaracion : declaracion_clase",
"declaracion : declaracion_funcion",
"declaracion_clase : CLASS ID '{' cuerpo_clase '}'",
"declaracion_clase : INTERFACE ID '{' cuerpo_interfaz '}'",
"declaracion_clase : CLASS ID IMPLEMENT ID '{' cuerpo_clase '}'",
"cuerpo_clase : miembro_clase",
"cuerpo_clase : cuerpo_clase miembro_clase",
"miembro_clase : declaracion_variable",
"miembro_clase : declaracion_funcion",
"cuerpo_interfaz : cuerpo_interfaz declaracion_funcion",
"cuerpo_interfaz : declaracion_funcion",
"declaracion_variable : tipo lista_de_id ','",
"declaracion_funcion : VOID ID '(' parametro_formal ')' '{' lista_sentencias '}'",
"declaracion_funcion : VOID ID '(' ')' '{' lista_sentencias '}'",
"parametro_formal : tipo ID",
"lista_sentencias : sentencia",
"lista_sentencias : lista_sentencias sentencia",
"sentencia : sentencia_expresion",
"sentencia : sentencia_seleccion",
"sentencia : sentencia_iteracion",
"sentencia : sentencia_retorno",
"sentencia : sentencia_imprimir",
"sentencia_imprimir : PRINT CADENA ','",
"sentencia_expresion : expresion",
"sentencia_seleccion : IF '(' comparacion ')' cuerpo_if END_IF",
"sentencia_seleccion : IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF",
"cuerpo_if : sentencia",
"cuerpo_if : '{' lista_sentencias '}'",
"comparacion : operacion comparador operacion",
"comparador : '<'",
"comparador : '>'",
"comparador : \"<=\"",
"comparador : \">=\"",
"comparador : \"==\"",
"comparador : \"!!\"",
"sentencia_iteracion : DO '{' lista_sentencias '}' WHILE '(' comparacion ')'",
"sentencia_retorno : RETURN ','",
"expresion : declaracion_variable",
"expresion : declaracion_funcion",
"expresion : asignacion ','",
"expresion : llamado_clase '(' operacion ')' ','",
"expresion : TOD '(' operacion ')' ','",
"llamado_clase : llamado_clase '.' ID",
"llamado_clase : ID",
"asignacion : lista_de_id '=' operacion",
"lista_de_id : lista_de_id ';' llamado_clase",
"lista_de_id : llamado_clase",
"operacion : operacion '+' termino",
"operacion : operacion '-' termino",
"operacion : termino",
"termino : termino '*' termino_inmediato",
"termino : termino '/' termino_inmediato",
"termino : termino_inmediato",
"termino_inmediato : factor \"--\"",
"termino_inmediato : factor",
"factor : llamado_clase",
"factor : CTE_DOUBLE",
"factor : '-' CTE_DOUBLE",
"factor : CTE_UINT",
"factor : CTE_LONG",
"factor : '-' CTE_LONG",
"tipo : DOUBLE",
"tipo : UINT",
"tipo : LONG",
"tipo : ID",
};

//#line 146 "grammer.y"

private AnalizadorLexico analizadorLexico;

 int yylex() {
	Token token;
  try {
    token = analizadorLexico.getToken();
    if (token!=null){
	    yylval = new ParserVal(token);
	    return token.getTipo().getNumero();
	}
  } catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }

	return 0;
}

private void yyerror(String string) {
	System.out.println(string);
}
//#line 394 "Parser.java"
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
