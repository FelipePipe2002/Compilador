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






//#line 2 ".\gramatica.y"

package Sintactico;
import Lexico.*;

//#line 22 "Parser.java"




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
public final static short RETURN=270;
public final static short TOD=271;
public final static short ID=272;
public final static short CTE_LONG=273;
public final static short CTE_UINT=274;
public final static short CTE_DOUBLE=275;
public final static short CADENA=276;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    2,    4,    4,    4,    6,
    6,    8,    8,    7,    7,    3,    5,    5,   12,   13,
   13,   11,   11,   11,   11,   14,   14,   14,   14,   14,
   19,   19,   18,   18,   16,   16,   16,   16,   21,   21,
   20,   23,   23,   23,   23,   23,   23,   17,   17,   15,
   15,   15,   15,   15,   25,   25,   24,   24,   10,   10,
   22,   22,   22,   26,   26,   26,   27,   27,   29,   28,
   28,   28,   28,   28,   28,    9,    9,    9,    9,
};
final static short yylen[] = {                            2,
    3,    1,    2,    1,    1,    1,    5,    7,    5,    1,
    2,    1,    1,    1,    2,    3,    6,    7,    2,    1,
    2,    3,    3,    2,    2,    1,    1,    1,    1,    1,
    2,    1,    3,    2,    7,    9,    6,    8,    1,    1,
    3,    1,    1,    1,    1,    1,    1,    7,    6,    1,
    1,    4,    5,    5,    1,    3,    4,    5,    1,    3,
    1,    3,    3,    1,    3,    3,    1,    1,    2,    1,
    2,    1,    2,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   78,   77,   76,    0,   79,    0,
    2,    4,    5,    6,    0,    0,    0,    0,    1,    3,
   55,    0,    0,    0,    0,    0,    0,   16,    0,    0,
    0,   12,   13,    0,   10,    0,    0,    0,   14,    0,
    0,   56,    0,    7,   11,    0,    0,    0,   19,    0,
    9,   15,    0,    0,    0,    0,    0,    0,    0,   24,
   50,    0,    0,    0,   20,   26,   27,   28,   29,   30,
   51,    0,   25,    0,   17,    0,    8,    0,    0,    0,
   31,    0,    0,    0,   22,   21,    0,   23,   18,   70,
   74,   75,   72,    0,    0,    0,    0,   64,    0,   68,
   33,    0,    0,    0,    0,    0,    0,   73,   71,    0,
   42,   43,   44,   45,   46,   47,    0,    0,    0,    0,
    0,   69,    0,    0,    0,   57,   52,    0,   40,   39,
    0,    0,    0,    0,   65,   66,    0,   54,   58,   53,
    0,    0,    0,    0,   35,   48,    0,   36,
};
final static short yydgoto[] = {                          2,
   10,   11,   61,   13,   33,   34,   40,   35,   15,   63,
  129,   38,   64,   65,   66,   67,   68,   69,   70,   95,
  131,   96,  119,   71,   72,   97,   98,   99,  100,
};
final static short yysindex[] = {                       -96,
 -147,    0, -224, -208,    0,    0,    0, -204,    0,  -65,
    0,    0,    0,    0, -183, -100,   61,  -15,    0,    0,
    0,   -4,   73, -138, -136,  139, -119,    0, -183, -127,
   24,    0,    0,  -97,    0,  -24, -122,  110,    0, -114,
   73,    0, -136,    0,    0, -108,  113,  109,    0,  -24,
    0,    0,  -54,  120, -103,  -24,  137,  143,    0,    0,
    0, -183,   27,  -78,    0,    0,    0,    0,    0,    0,
    0,   13,    0,  129,    0,  140,    0,  133,  144,  -75,
    0,  133,   14,  133,    0,    0,   -2,    0,    0,    0,
    0,    0,    0, -171,  154,  -25,   30,    0,  -80,    0,
    0,  162,   96,  133,   37,  160,  101,    0,    0,   17,
    0,    0,    0,    0,    0,    0,  133,  133,  133,  133,
  133,    0,  133,  161,   88,    0,    0,  168,    0,    0,
 -135,   30,   30,   62,    0,    0,  172,    0,    0,    0,
   17,  170,  171,  -43,    0,    0,  173,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   32,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   51,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   33,    0,    4,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   52,    0,    0,    0,    0,    0,    0,   49,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -36,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -31,  -11,  178,    0,    0,    0,    0,    0,    0,
    0,   65,   81,    0,    0,    0,   97,    0,
};
final static short yygindex[] = {                         0,
    0,  210,   44,    0,   69,  180,    0,  -12,   20,   21,
    6,    0,  174,  -49,    0,    0,    0,    0,    0,  102,
   83,   16,    0,    0,   23,   54,   56,    0,    0,
};
final static int YYTABLESIZE=411;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         67,
   67,   67,   67,   67,   61,   67,   61,   61,   61,   62,
   51,   62,   62,   62,   86,   47,   60,  117,   67,  118,
   67,   45,   25,   61,   86,   61,    1,   44,   62,   63,
   62,   63,   63,   63,  111,   22,  112,   23,  106,   28,
   45,   48,   94,   55,   12,   37,   85,   16,   63,   55,
   63,   41,   87,   12,   29,   76,   47,   28,   30,   19,
  130,   80,   55,   17,   55,   62,   62,   18,   32,   14,
   77,  120,   29,   32,  104,   59,  121,   32,   14,  117,
  126,  118,   83,   62,   23,   29,   32,   84,   21,   34,
   59,  130,   59,   62,   60,   39,   32,  103,   46,  105,
   26,  108,  107,  109,  117,   37,  118,   27,   52,   60,
   59,   60,   59,    3,    4,    5,    6,    7,   30,  125,
    8,   49,  141,  142,    9,    4,    5,    6,    7,   62,
  117,  139,  118,   31,  134,    9,  124,   38,  117,   46,
  118,  128,    4,  117,   42,  118,   43,    4,   54,   49,
   50,   55,   75,   73,    5,    6,    7,   32,   56,   78,
   62,   57,   58,   59,    4,    5,    6,    7,   24,   88,
  132,  133,   79,   34,    9,  135,  136,   94,   54,   36,
   81,   55,   82,   89,    5,    6,    7,  101,   56,   37,
  102,   57,   58,   59,  110,    3,    4,    5,    6,    7,
  122,  123,    8,  127,  138,   49,    9,    4,    5,    6,
    7,  140,  143,  145,  146,  147,  148,    9,   41,   20,
   74,   38,   53,  144,  137,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   67,   67,   67,   67,    0,
   61,   61,   61,   61,    0,   62,   62,   62,   62,    0,
    0,  113,  114,  115,  116,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   63,   63,   63,   63,   90,
   91,   92,   93,   54,    0,   79,   55,    0,    0,    5,
    6,    7,    0,   56,    0,    0,   57,   58,   59,   32,
   32,   32,   32,    0,    0,   32,   32,   32,    0,   32,
    0,    0,   32,   32,   32,   34,   34,   34,   34,    0,
    0,   34,   34,   34,    0,   34,    0,    0,   34,   34,
   34,   37,   37,   37,   37,    0,    0,   37,   37,   37,
    0,   37,    0,    0,   37,   37,   37,   49,   49,   49,
   49,    0,    0,   49,   49,   49,    0,   49,    0,    0,
   49,   49,   49,   38,   38,   38,   38,    0,    0,   38,
   38,   38,    0,   38,    0,    0,   38,   38,   38,   54,
    0,    0,   55,    0,    0,    5,    6,    7,    0,   56,
    0,    0,   57,   58,   59,   54,    0,    0,   55,    0,
    0,    5,    6,    7,    0,   56,    0,    0,   57,   58,
   59,    5,    6,    7,   90,   91,   92,   93,    0,    0,
    9,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   41,   47,   43,   44,   45,   41,
  125,   43,   44,   45,   64,   40,  125,   43,   60,   45,
   62,   34,  123,   60,   74,   62,  123,  125,   60,   41,
   62,   43,   44,   45,   60,   15,   62,   15,   41,   44,
   53,   36,   45,   40,    1,   26,  125,  272,   60,   46,
   62,   29,   40,   10,   59,   50,   40,   44,   46,  125,
  110,   56,   59,  272,   61,   46,   47,  272,   25,    1,
  125,   42,   59,   41,   61,   44,   47,   34,   10,   43,
   44,   45,   62,   64,   62,   59,   43,   61,  272,   41,
   59,  141,   61,   74,   44,   27,   53,   82,  123,   84,
   40,  273,   87,  275,   43,   41,   45,  123,   40,   59,
   59,   61,   61,  261,  262,  263,  264,  265,   46,  104,
  268,   41,  258,  259,  272,  262,  263,  264,  265,  110,
   43,   44,   45,  272,  119,  272,   41,   41,   43,  123,
   45,   41,  262,   43,  272,   45,  123,  262,  257,  272,
   41,  260,   44,   41,  263,  264,  265,  125,  267,   40,
  141,  270,  271,  272,  262,  263,  264,  265,  269,   41,
  117,  118,  276,  125,  272,  120,  121,   45,  257,   41,
   44,  260,   40,   44,  263,  264,  265,   44,  267,  125,
  266,  270,  271,  272,   41,  261,  262,  263,  264,  265,
  281,   40,  268,   44,   44,  125,  272,  262,  263,  264,
  265,   44,   41,   44,   44,  259,   44,  272,   41,   10,
   47,  125,   43,  141,  123,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  277,  278,  279,  280,   -1,
  277,  278,  279,  280,   -1,  277,  278,  279,  280,   -1,
   -1,  277,  278,  279,  280,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  277,  278,  279,  280,  272,
  273,  274,  275,  257,   -1,  272,  260,   -1,   -1,  263,
  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,  257,
  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,  267,
   -1,   -1,  270,  271,  272,  257,  258,  259,  260,   -1,
   -1,  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,
  272,  257,  258,  259,  260,   -1,   -1,  263,  264,  265,
   -1,  267,   -1,   -1,  270,  271,  272,  257,  258,  259,
  260,   -1,   -1,  263,  264,  265,   -1,  267,   -1,   -1,
  270,  271,  272,  257,  258,  259,  260,   -1,   -1,  263,
  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,  257,
   -1,   -1,  260,   -1,   -1,  263,  264,  265,   -1,  267,
   -1,   -1,  270,  271,  272,  257,   -1,   -1,  260,   -1,
   -1,  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,
  272,  263,  264,  265,  272,  273,  274,  275,   -1,   -1,
  272,
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
"LONG","UINT","DOUBLE","WHILE","DO","INTERFACE","IMPLEMENT","RETURN","TOD","ID",
"CTE_LONG","CTE_UINT","CTE_DOUBLE","CADENA","\"<=\"","\">=\"","\"==\"","\"!!\"",
"\"--\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : '{' lista_declaraciones '}'",
"lista_declaraciones : declaracion",
"lista_declaraciones : lista_declaraciones declaracion",
"declaracion : declaracion_variable",
"declaracion : declaracion_clase",
"declaracion : declaracion_funcion",
"declaracion_clase : CLASS ID '{' cuerpo_clase '}'",
"declaracion_clase : CLASS ID IMPLEMENT ID '{' cuerpo_clase '}'",
"declaracion_clase : INTERFACE ID '{' cuerpo_interfaz '}'",
"cuerpo_clase : miembro_clase",
"cuerpo_clase : cuerpo_clase miembro_clase",
"miembro_clase : declaracion_variable",
"miembro_clase : declaracion_funcion",
"cuerpo_interfaz : declaracion_funcion",
"cuerpo_interfaz : cuerpo_interfaz declaracion_funcion",
"declaracion_variable : tipo lista_de_id ','",
"declaracion_funcion : VOID ID '(' ')' bloque_sentencias ','",
"declaracion_funcion : VOID ID '(' parametro_formal ')' bloque_sentencias ','",
"parametro_formal : tipo ID",
"lista_sentencias : sentencia",
"lista_sentencias : lista_sentencias sentencia",
"bloque_sentencias : '{' lista_sentencias '}'",
"bloque_sentencias : '(' lista_sentencias ')'",
"bloque_sentencias : '{' '}'",
"bloque_sentencias : '(' ')'",
"sentencia : sentencia_expresion",
"sentencia : sentencia_seleccion",
"sentencia : sentencia_iteracion",
"sentencia : sentencia_imprimir",
"sentencia : sentencia_retorno",
"sentencia_retorno : RETURN ','",
"sentencia_retorno : RETURN",
"sentencia_imprimir : PRINT CADENA ','",
"sentencia_imprimir : PRINT CADENA",
"sentencia_seleccion : IF '(' comparacion ')' cuerpo_if END_IF ','",
"sentencia_seleccion : IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF ','",
"sentencia_seleccion : IF '(' comparacion ')' cuerpo_if END_IF",
"sentencia_seleccion : IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF",
"cuerpo_if : sentencia",
"cuerpo_if : bloque_sentencias",
"comparacion : operacion comparador operacion",
"comparador : '<'",
"comparador : '>'",
"comparador : \"<=\"",
"comparador : \">=\"",
"comparador : \"==\"",
"comparador : \"!!\"",
"sentencia_iteracion : DO bloque_sentencias WHILE '(' comparacion ')' ','",
"sentencia_iteracion : DO bloque_sentencias WHILE '(' comparacion ')'",
"sentencia_expresion : declaracion_variable",
"sentencia_expresion : asignacion",
"sentencia_expresion : llamado_clase '(' ')' ','",
"sentencia_expresion : llamado_clase '(' operacion ')' ','",
"sentencia_expresion : TOD '(' operacion ')' ','",
"llamado_clase : ID",
"llamado_clase : llamado_clase '.' ID",
"asignacion : lista_de_id '=' operacion ','",
"asignacion : tipo lista_de_id '=' operacion ','",
"lista_de_id : llamado_clase",
"lista_de_id : lista_de_id ';' llamado_clase",
"operacion : termino",
"operacion : operacion '+' termino",
"operacion : operacion '-' termino",
"termino : factor",
"termino : termino '*' factor",
"termino : termino '/' factor",
"factor : factor_comun",
"factor : factor_inmediato",
"factor_inmediato : factor_comun \"--\"",
"factor_comun : ID",
"factor_comun : '-' CTE_DOUBLE",
"factor_comun : CTE_DOUBLE",
"factor_comun : '-' CTE_LONG",
"factor_comun : CTE_LONG",
"factor_comun : CTE_UINT",
"tipo : DOUBLE",
"tipo : UINT",
"tipo : LONG",
"tipo : ID",
};

//#line 154 ".\gramatica.y"


static AnalizadorLexico analizadorLex = null;
static Parser par = null;
static Token token = null;

public static void main(String[] args) throws Exception{
        System.out.println("Iniciando compilacion...");
        analizadorLex = new AnalizadorLexico(args);

        par = new Parser(false);
        par.run();

        analizadorLex.MostrarErrores();
        analizadorLex.MostrarTablaSimbolos();

        System.out.println("Fin de la compilacion");
}

private int yylex(){
        try {
          token = analizadorLex.getToken();
          int intToken = token.getTipo().getNumero();
          System.out.println("El token obtenido es: " + token);
          System.out.println("El valor del token obtenido es: " + intToken);
          yylval = new ParserVal(token);
          return intToken;
        } catch (Exception e) {
          System.out.println("Error a la hora encontrar un token");
          return 0;
        }
}

private void yyerror(String s) {
        System.out.println(s);
}

private boolean CheckRangoLong(String numero){
        numero = numero.substring(0,numero.length()-2);
        Long bufferValue = Long.parseLong(numero);
        Long rango = 2147483647l;
        if(bufferValue>rango)
            return true;
        else
            return false;
}
//#line 444 "Parser.java"
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
case 7:
//#line 24 ".\gramatica.y"
{System.out.println("CREACION DE CLASE");}
break;
case 8:
//#line 25 ".\gramatica.y"
{System.out.println("CREACION DE CLASE CON HERENCIA");}
break;
case 9:
//#line 26 ".\gramatica.y"
{System.out.println("CREACION DE INTERFAZ");}
break;
case 16:
//#line 41 ".\gramatica.y"
{System.out.println("DECLARACION DE VARIABLE");}
break;
case 17:
//#line 45 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION SIN PARAMETROS");}
break;
case 18:
//#line 46 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION");}
break;
case 23:
//#line 57 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 24:
//#line 58 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 25:
//#line 59 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 31:
//#line 69 ".\gramatica.y"
{System.out.println("SENTENCIA RETURN");}
break;
case 32:
//#line 70 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 33:
//#line 73 ".\gramatica.y"
{System.out.println("SENTENCIA IMPRESION DE CADENA");}
break;
case 34:
//#line 74 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 35:
//#line 77 ".\gramatica.y"
{System.out.println("SENTENCIA IF");}
break;
case 36:
//#line 78 ".\gramatica.y"
{System.out.println("SENTENCIA IF ELSE");}
break;
case 37:
//#line 79 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 38:
//#line 80 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 48:
//#line 97 ".\gramatica.y"
{System.out.println("SENTENCIA ITERACION");}
break;
case 49:
//#line 98 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 57:
//#line 112 ".\gramatica.y"
{System.out.println("ASIGNACION");}
break;
case 58:
//#line 113 ".\gramatica.y"
{ErrorLexico error = new ErrorLexico("No se puede declarar y asignar en la misma línea", analizadorLex.getLineaArchivo());
                                                        analizadorLex.addErroresLexicos(error);}
break;
case 71:
//#line 138 ".\gramatica.y"
{analizadorLex.convertirNegativo(((LexemToken)token).getLexema());}
break;
case 73:
//#line 140 ".\gramatica.y"
{analizadorLex.convertirNegativo(((LexemToken)token).getLexema());}
break;
case 74:
//#line 141 ".\gramatica.y"
{if(CheckRangoLong(((LexemToken)token).getLexema())){
                                ErrorLexico error = new ErrorLexico("Long fuera de rango", analizadorLex.getLineaArchivo());
                                analizadorLex.addErroresLexicos(error);}}
break;
//#line 692 "Parser.java"
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
