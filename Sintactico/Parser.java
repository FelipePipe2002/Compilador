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
    0,    0,    0,    0,    1,    1,    2,    2,    2,    4,
    4,    4,    6,    6,    8,    8,    7,    7,    3,    3,
    5,    5,    5,    5,   12,   13,   13,   11,   11,   11,
   11,   14,   14,   14,   14,   14,   19,   19,   18,   18,
   16,   16,   16,   16,   21,   21,   20,   23,   23,   23,
   23,   23,   23,   17,   17,   15,   15,   15,   15,   15,
   15,   15,   15,   25,   25,   24,   24,   24,   10,   10,
   22,   22,   22,   26,   26,   26,   27,   27,   29,   28,
   28,   28,   28,   28,   28,    9,    9,    9,    9,
};
final static short yylen[] = {                            2,
    3,    2,    2,    1,    1,    2,    1,    1,    1,    5,
    7,    5,    1,    2,    1,    1,    1,    2,    3,    2,
    6,    7,    5,    6,    2,    1,    2,    3,    3,    2,
    2,    1,    1,    1,    1,    1,    2,    1,    3,    2,
    7,    9,    6,    8,    1,    1,    3,    1,    1,    1,
    1,    1,    1,    7,    6,    1,    1,    4,    3,    5,
    4,    5,    4,    1,    3,    4,    3,    5,    1,    3,
    1,    3,    3,    1,    3,    3,    1,    1,    2,    1,
    2,    1,    2,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,   88,   87,   86,    0,   89,    0,    0,    0,
    5,    7,    8,    9,    0,    0,    0,    0,    0,    3,
    6,   64,    0,    0,    0,    0,    0,    0,    1,   19,
    0,    0,    0,   15,   16,    0,   13,    0,    0,    0,
   17,    0,    0,   65,    0,   10,   14,    0,    0,    0,
   25,    0,   12,   18,    0,    0,    0,    0,    0,    0,
    0,   30,   56,    0,    0,    0,   26,   32,   33,   34,
   35,   36,   57,    0,   31,    0,   21,    0,   11,    0,
    0,    0,   37,    0,    0,    0,   28,   27,    0,   29,
   22,   80,   84,   85,   82,    0,    0,    0,    0,   74,
    0,   78,   39,    0,    0,    0,    0,    0,    0,   83,
   81,    0,   48,   49,   50,   51,   52,   53,    0,    0,
    0,    0,    0,   79,    0,    0,    0,   66,   58,    0,
   46,   45,    0,    0,    0,    0,   75,   76,    0,   62,
   68,   60,    0,    0,    0,    0,   41,   54,    0,   42,
};
final static short yydgoto[] = {                          9,
   10,   11,   63,   13,   14,   36,   42,   37,   15,   65,
  131,   40,   66,   67,   68,   69,   70,   71,   72,   97,
  133,   98,  121,   73,   74,   99,  100,  101,  102,
};
final static short yysindex[] = {                      -113,
 -245, -217,    0,    0,    0, -207,    0, -157,    0,  -89,
    0,    0,    0,    0, -189, -111,   52,   16,  -56,    0,
    0,    0,   20,  107, -116, -144,   47, -105,    0,    0,
 -189, -112,   38,    0,    0,  171,    0,  -27, -109,  124,
    0, -125,  107,    0, -144,    0,    0,  -68,  387,  133,
    0,  -27,    0,    0,  195,  128,  -95,  -27,  138,  144,
    0,    0,    0, -189,   66,  419,    0,    0,    0,    0,
    0,    0,    0,   -3,    0,  403,    0,  141,    0,  -38,
  143,  -76,    0,  -38,   -9,  -38,    0,    0,  -25,    0,
    0,    0,    0,    0,    0, -143,  150,  -34,   12,    0,
  -88,    0,    0,  158,   39,  -38,   58,  157,   48,    0,
    0,  160,    0,    0,    0,    0,    0,    0,  -38,  -38,
  -38,  -38,  -38,    0,  -38,  166,   79,    0,    0,  169,
    0,    0, -211,   12,   12,   90,    0,    0,  173,    0,
    0,    0,  160,  178,  185,  -42,    0,    0,  186,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  215,
    0,    0,    0,    0,    0,    0,    0,    0,  242,    0,
    0,    0,   29,    1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   17,    0,    0,    0,    0,    0,    0,   41,
    0,    0,    0,    0,    0,    0,    0,    0,  182,    0,
  -15,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   77,    0,    0,    0,   53,    0,    0,
  212,    0,    0,    0,  229,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   69,    0,
  -39,    0,    0,    0,    0,    0,  245,  267,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  283,    0,    0,    0,  301,
    0,    0,    0,  102,  126,  211,    0,    0,    0,    0,
    0,    0,    0,  323,  353,    0,    0,    0,  370,    0,
};
final static short yygindex[] = {                         0,
  246,    5,   30,    0,   45,  210,    0,   15,   68,   -1,
  -19,    0,  207,  -44,    0,    0,    0,    0,    0,  142,
  152,  -12,    0,    0,    3,  -22,   18,    0,    0,
};
final static int YYTABLESIZE=691;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         53,
   69,   77,   77,   77,   77,   77,   96,   77,  119,    8,
  120,   26,   49,   23,   21,  108,   70,   24,   50,   96,
   77,   88,   77,   21,   64,  113,   16,  114,   20,   12,
   64,   88,   78,   43,   30,   20,   89,   12,   82,   12,
   23,   69,   32,   64,   69,   64,  143,  144,   12,   31,
   47,  106,   24,  122,   17,   34,   62,   70,  123,   69,
   70,   69,   85,   30,   18,   34,   24,  132,   29,   47,
   35,  105,   41,  107,   34,   70,  109,   70,   31,  126,
   35,  119,   22,  120,   34,   77,   54,   38,  130,   35,
  119,   27,  120,  127,   39,   48,  134,  135,  132,   35,
  119,  128,  120,    1,    2,    3,    4,    5,  136,   71,
    6,   71,   71,   71,    7,   64,   64,    2,    3,    4,
    5,  119,  141,  120,   31,   69,   86,    7,   71,  110,
   71,  111,  119,   64,  120,   69,    2,   69,   28,  137,
  138,   70,   72,   64,   72,   72,   72,    1,    2,    3,
    4,    5,   32,   20,    6,   33,    2,   25,    7,   44,
   45,   72,   51,   72,   52,   23,   73,   80,   73,   73,
   73,    1,    2,    3,    4,    5,   77,   24,    6,   64,
   81,   83,    7,   84,   91,   73,  103,   73,   56,  104,
  112,   57,  124,   71,    3,    4,    5,  125,   58,   49,
  129,   59,   60,   61,    1,    2,    3,    4,    5,  140,
   64,    6,  142,  145,    4,    7,  149,   77,   77,   77,
   77,  147,   38,   77,   77,   77,   72,   77,  148,  150,
   77,   77,   77,   92,   93,   94,   95,   77,   77,   77,
   77,    2,  115,  116,  117,  118,   92,   93,   94,   95,
   73,   47,   40,   19,   55,   76,   89,   69,   69,   69,
   69,   69,   69,   69,   69,   69,  139,   69,   69,   20,
   69,   69,   69,   70,   70,   70,   70,   70,   70,   70,
   70,   70,   48,   70,   70,   67,   70,   70,   70,   20,
   20,   20,   20,   20,  146,   46,   20,    0,    0,    0,
   20,   23,   23,   23,   23,   23,   38,   59,   23,    3,
    4,    5,   23,   24,   24,   24,   24,   24,    7,   79,
   24,    0,    0,   63,   24,   71,   71,   71,   71,    0,
    0,   71,   71,   71,    0,   71,   40,    0,   71,   71,
   71,   61,    0,    0,    0,   71,   71,   71,   71,    0,
    0,    0,    0,   20,    0,    0,    0,    0,   72,   72,
   72,   72,    0,   43,   72,   72,   72,    0,   72,   67,
    0,   72,   72,   72,    0,    0,    0,    0,   72,   72,
   72,   72,   73,   73,   73,   73,    0,    0,   73,   73,
   73,   59,   73,   55,    0,   73,   73,   73,    0,    0,
    0,    0,   73,   73,   73,   73,    0,   63,    0,    0,
   44,    0,    0,    0,    0,    0,   56,    0,    0,   57,
    0,    0,    3,    4,    5,   61,   58,   75,    0,   59,
   60,   61,    2,    3,    4,    5,    0,    0,   38,   38,
   38,   38,    7,   90,   38,   38,   38,   43,   38,    0,
    0,   38,   38,   38,    0,    0,    2,    3,    4,    5,
    0,    0,    0,    0,    0,    0,    7,    0,   40,   40,
   40,   40,    0,    0,   40,   40,   40,   55,   40,    0,
    0,   40,   40,   40,    0,   20,   20,   20,   20,    0,
    0,   20,   20,   20,   44,   20,    0,    0,   20,   20,
   20,   67,   67,   67,   67,    0,    0,   67,   67,   67,
    0,   67,    0,    0,   67,   67,   67,    0,    0,    0,
    0,    0,    0,   59,   59,   59,   59,    0,    0,   59,
   59,   59,    0,   59,    0,    0,   59,   59,   59,   63,
   63,   63,   63,   87,    0,   63,   63,   63,    0,   63,
    0,    0,   63,   63,   63,    0,    0,   61,   61,   61,
   61,    0,    0,   61,   61,   61,    0,   61,    0,    0,
   61,   61,   61,    0,    0,    0,    0,    0,    0,   43,
   43,   43,   43,    0,    0,   43,   43,   43,    0,   43,
    0,    0,   43,   43,   43,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   55,
   55,   55,   55,    0,    0,   55,   55,   55,    0,   55,
    0,    0,   55,   55,   55,    0,   44,   44,   44,   44,
    0,    0,   44,   44,   44,    0,   44,    0,    0,   44,
   44,   44,    0,   56,    0,    0,   57,    0,    0,    3,
    4,    5,    0,   58,    0,    0,   59,   60,   61,   56,
    0,    0,   57,    0,    0,    3,    4,    5,    0,   58,
    0,    0,   59,   60,   61,   56,    0,    0,   57,    0,
    0,    3,    4,    5,    0,   58,    0,    0,   59,   60,
   61,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                        125,
    0,   41,   42,   43,   44,   45,   45,   47,   43,  123,
   45,  123,   40,   15,   10,   41,    0,   15,   38,   45,
   60,   66,   62,   19,   40,   60,  272,   62,    0,    0,
   46,   76,   52,   31,   44,  125,   40,    8,   58,   10,
    0,   41,   46,   59,   44,   61,  258,  259,   19,   59,
   36,   61,    0,   42,  272,   26,  125,   41,   47,   59,
   44,   61,   64,   44,  272,   36,   64,  112,  125,   55,
   26,   84,   28,   86,   45,   59,   89,   61,   59,   41,
   36,   43,  272,   45,   55,  125,   42,   41,   41,   45,
   43,   40,   45,  106,   27,  123,  119,  120,  143,   55,
   43,   44,   45,  261,  262,  263,  264,  265,  121,   41,
  268,   43,   44,   45,  272,   48,   49,  262,  263,  264,
  265,   43,   44,   45,   59,  125,   61,  272,   60,  273,
   62,  275,   43,   66,   45,   59,  262,   61,  123,  122,
  123,  125,   41,   76,   43,   44,   45,  261,  262,  263,
  264,  265,   46,  125,  268,  272,  262,  269,  272,  272,
  123,   60,  272,   62,   41,  125,   41,   40,   43,   44,
   45,  261,  262,  263,  264,  265,   44,  125,  268,  112,
  276,   44,  272,   40,   44,   60,   44,   62,  257,  266,
   41,  260,  281,  125,  263,  264,  265,   40,  267,   40,
   44,  270,  271,  272,  261,  262,  263,  264,  265,   44,
  143,  268,   44,   41,    0,  272,  259,  257,  258,  259,
  260,   44,   41,  263,  264,  265,  125,  267,   44,   44,
  270,  271,  272,  272,  273,  274,  275,  277,  278,  279,
  280,    0,  277,  278,  279,  280,  272,  273,  274,  275,
  125,   41,   41,    8,   45,   49,  272,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  125,  267,  268,   41,
  270,  271,  272,  257,  258,  259,  260,  261,  262,  263,
  264,  265,  123,  267,  268,   41,  270,  271,  272,  261,
  262,  263,  264,  265,  143,  125,  268,   -1,   -1,   -1,
  272,  261,  262,  263,  264,  265,  125,   41,  268,  263,
  264,  265,  272,  261,  262,  263,  264,  265,  272,  125,
  268,   -1,   -1,   41,  272,  257,  258,  259,  260,   -1,
   -1,  263,  264,  265,   -1,  267,  125,   -1,  270,  271,
  272,   41,   -1,   -1,   -1,  277,  278,  279,  280,   -1,
   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,  257,  258,
  259,  260,   -1,   41,  263,  264,  265,   -1,  267,  125,
   -1,  270,  271,  272,   -1,   -1,   -1,   -1,  277,  278,
  279,  280,  257,  258,  259,  260,   -1,   -1,  263,  264,
  265,  125,  267,   41,   -1,  270,  271,  272,   -1,   -1,
   -1,   -1,  277,  278,  279,  280,   -1,  125,   -1,   -1,
   41,   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,  260,
   -1,   -1,  263,  264,  265,  125,  267,   41,   -1,  270,
  271,  272,  262,  263,  264,  265,   -1,   -1,  257,  258,
  259,  260,  272,   41,  263,  264,  265,  125,  267,   -1,
   -1,  270,  271,  272,   -1,   -1,  262,  263,  264,  265,
   -1,   -1,   -1,   -1,   -1,   -1,  272,   -1,  257,  258,
  259,  260,   -1,   -1,  263,  264,  265,  125,  267,   -1,
   -1,  270,  271,  272,   -1,  257,  258,  259,  260,   -1,
   -1,  263,  264,  265,  125,  267,   -1,   -1,  270,  271,
  272,  257,  258,  259,  260,   -1,   -1,  263,  264,  265,
   -1,  267,   -1,   -1,  270,  271,  272,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  258,  259,  260,   -1,   -1,  263,
  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,  257,
  258,  259,  260,  125,   -1,  263,  264,  265,   -1,  267,
   -1,   -1,  270,  271,  272,   -1,   -1,  257,  258,  259,
  260,   -1,   -1,  263,  264,  265,   -1,  267,   -1,   -1,
  270,  271,  272,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,  267,
   -1,   -1,  270,  271,  272,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,  267,
   -1,   -1,  270,  271,  272,   -1,  257,  258,  259,  260,
   -1,   -1,  263,  264,  265,   -1,  267,   -1,   -1,  270,
  271,  272,   -1,  257,   -1,   -1,  260,   -1,   -1,  263,
  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,  257,
   -1,   -1,  260,   -1,   -1,  263,  264,  265,   -1,  267,
   -1,   -1,  270,  271,  272,  257,   -1,   -1,  260,   -1,
   -1,  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,
  272,
};
}
final static short YYFINAL=9;
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
"programa : '{' lista_declaraciones",
"programa : lista_declaraciones '}'",
"programa : lista_declaraciones",
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
"declaracion_variable : tipo lista_de_id",
"declaracion_funcion : VOID ID '(' ')' bloque_sentencias ','",
"declaracion_funcion : VOID ID '(' parametro_formal ')' bloque_sentencias ','",
"declaracion_funcion : VOID ID '(' ')' bloque_sentencias",
"declaracion_funcion : VOID ID '(' parametro_formal ')' bloque_sentencias",
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
"sentencia_expresion : llamado_clase '(' ')'",
"sentencia_expresion : llamado_clase '(' operacion ')' ','",
"sentencia_expresion : llamado_clase '(' operacion ')'",
"sentencia_expresion : TOD '(' operacion ')' ','",
"sentencia_expresion : TOD '(' operacion ')'",
"llamado_clase : ID",
"llamado_clase : llamado_clase '.' ID",
"asignacion : lista_de_id '=' operacion ','",
"asignacion : lista_de_id '=' operacion",
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

//#line 163 ".\gramatica.y"


static AnalizadorLexico analizadorLex = null;
static Parser par = null;
static Token token = null;

public static void main(String[] args) throws Exception{
        System.out.println("Iniciando compilacion...");
        analizadorLex = new AnalizadorLexico(args);

        par = new Parser(false);
        par.run();

        analizadorLex.MostrarTablaSimbolos();
        analizadorLex.MostrarErrores();

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
//#line 512 "Parser.java"
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
case 2:
//#line 13 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("El programa tiene que terminar con \'}\'", analizadorLex.getLineaArchivo()));}
break;
case 3:
//#line 14 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("El programa tiene que arrancar con \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 4:
//#line 15 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("El programa tiene que estar contenido en \'{\' \'}\'", analizadorLex.getLineaArchivo()));}
break;
case 10:
//#line 27 ".\gramatica.y"
{System.out.println("CREACION DE CLASE");}
break;
case 11:
//#line 28 ".\gramatica.y"
{System.out.println("CREACION DE CLASE CON HERENCIA");}
break;
case 12:
//#line 29 ".\gramatica.y"
{System.out.println("CREACION DE INTERFAZ");}
break;
case 19:
//#line 44 ".\gramatica.y"
{System.out.println("DECLARACION DE VARIABLE");}
break;
case 20:
//#line 45 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 21:
//#line 48 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION SIN PARAMETROS");}
break;
case 22:
//#line 49 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION");}
break;
case 23:
//#line 50 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 24:
//#line 51 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 29:
//#line 62 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 30:
//#line 63 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 31:
//#line 64 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 37:
//#line 74 ".\gramatica.y"
{System.out.println("SENTENCIA RETURN");}
break;
case 38:
//#line 75 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 39:
//#line 78 ".\gramatica.y"
{System.out.println("SENTENCIA IMPRESION DE CADENA");}
break;
case 40:
//#line 79 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 41:
//#line 82 ".\gramatica.y"
{System.out.println("SENTENCIA IF");}
break;
case 42:
//#line 83 ".\gramatica.y"
{System.out.println("SENTENCIA IF ELSE");}
break;
case 43:
//#line 84 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 44:
//#line 85 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 54:
//#line 102 ".\gramatica.y"
{System.out.println("SENTENCIA ITERACION");}
break;
case 55:
//#line 103 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 59:
//#line 109 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 61:
//#line 111 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 63:
//#line 113 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 66:
//#line 120 ".\gramatica.y"
{System.out.println("ASIGNACION");}
break;
case 67:
//#line 121 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new ErrorLexico("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 68:
//#line 122 ".\gramatica.y"
{ErrorLexico error = new ErrorLexico("No se puede declarar y asignar en la misma línea", analizadorLex.getLineaArchivo());
                                                        analizadorLex.addErroresLexicos(error);}
break;
case 81:
//#line 147 ".\gramatica.y"
{analizadorLex.convertirNegativo(((LexemToken)token).getLexema());}
break;
case 83:
//#line 149 ".\gramatica.y"
{analizadorLex.convertirNegativo(((LexemToken)token).getLexema());}
break;
case 84:
//#line 150 ".\gramatica.y"
{if(CheckRangoLong(((LexemToken)token).getLexema())){
                                ErrorLexico error = new ErrorLexico("Long fuera de rango", analizadorLex.getLineaArchivo());
                                analizadorLex.addErroresLexicos(error);}}
break;
//#line 800 "Parser.java"
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
