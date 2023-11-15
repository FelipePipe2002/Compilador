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
import Errores.Error;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

//#line 26 "Parser.java"




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
    0,    0,    0,    0,    1,    1,    1,    1,    2,    2,
    4,    4,    4,    4,    4,    4,    6,    8,    7,    7,
    7,    7,    7,   11,    9,    9,    9,    9,   10,   10,
   13,   13,   12,   12,   12,   12,   15,   15,   15,   15,
   15,   15,   15,   14,    5,    5,    5,    5,   16,   17,
   20,   20,   20,   20,   21,   21,   22,   22,    3,    3,
    3,    3,    3,   27,   27,   26,   26,   24,   24,   24,
   24,   24,   24,   28,   28,   29,   29,   30,   30,   32,
   32,   32,   32,   33,   33,   31,   31,   31,   31,   35,
   35,   35,   35,   35,   35,   35,   25,   25,   25,   36,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   39,
   39,   38,   38,   38,   19,   19,   34,   34,   34,   40,
   40,   40,   40,   41,   41,   37,   42,   42,   42,   42,
   42,   42,   42,   18,   18,   18,   18,
};
final static short yylen[] = {                            2,
    3,    2,    2,    1,    1,    1,    2,    2,    1,    1,
    3,    5,    3,    3,    5,    3,    2,    2,    3,    4,
    3,    2,    2,    2,    3,    3,    2,    2,    1,    2,
    1,    1,    1,    2,    1,    2,    4,    5,    4,    5,
    3,    2,    3,    3,    5,    6,    4,    5,    2,    2,
    3,    3,    2,    2,    1,    2,    1,    1,    1,    1,
    1,    1,    1,    2,    2,    3,    3,    4,    5,    4,
    5,    3,    4,    4,    3,    1,    1,    2,    2,    3,
    3,    2,    2,    1,    2,    3,    2,    2,    1,    1,
    1,    1,    1,    1,    1,    1,    7,    7,    6,    1,
    1,    1,    1,    4,    4,    5,    5,    5,    5,    1,
    3,    4,    4,    5,    3,    1,    1,    3,    3,    1,
    3,    3,    3,    1,    1,    2,    1,    2,    1,    2,
    1,    1,    2,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  136,  135,  134,  100,    0,    0,
    0,    0,    0,    0,    0,    5,    6,    9,   10,    0,
    0,  101,    0,    0,   59,   60,   61,   62,   63,    0,
    0,  102,  103,    0,    0,    0,   17,   49,   18,   64,
   65,    0,    0,    3,    8,    7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   76,
    0,   77,    0,    0,    0,    0,  126,   96,  110,  131,
  132,  129,    0,   75,   90,   91,   92,   93,   94,   95,
    0,    0,    0,    0,  125,    0,    0,  120,  124,   66,
   67,    0,    1,    0,  137,   22,   32,    0,   29,   31,
    0,   23,    0,   11,   14,   27,   35,    0,   33,    0,
   28,    0,   13,   16,    0,    0,    0,   44,    0,    0,
   82,   84,    0,   83,    0,    0,    0,   72,    0,    0,
    0,    0,  111,    0,    0,  130,  133,  128,   74,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   19,    0,
   30,  116,   21,   25,   36,   34,   42,    0,    0,   26,
    0,    0,    0,    0,   50,  115,    0,   80,   85,   81,
   78,   79,   68,   70,    0,   73,    0,  104,  105,    0,
  112,  113,  123,    0,    0,    0,  121,  122,  108,  109,
   12,   15,   24,   20,    0,    0,   41,   43,   53,   58,
   57,    0,   55,   54,    0,   45,    0,  114,   69,   71,
    0,    0,  106,  107,   37,   39,    0,   51,   56,   52,
   46,   99,    0,   38,   40,   97,   98,
};
final static short yydgoto[] = {                         14,
   15,   16,  200,   18,   19,   20,   50,   21,   53,   98,
  150,  108,   99,   22,  109,   23,  116,   24,   56,  163,
  202,  203,   25,   26,   27,   28,   29,   30,   61,  129,
   82,   62,  123,   83,   84,   31,   32,   33,   34,   87,
   88,   89,
};
final static short yysindex[] = {                       -99,
   -9, -223, -196, -168,    0,    0,    0,    0, -155,   -3,
   78,    0,  370,    0,  -82,    0,    0,    0,    0,  -29,
  -21,    0,  114, -136,    0,    0,    0,    0,    0,   82,
   -2,    0,    0,  -38,  -27,    8,    0,    0,    0,    0,
    0,   63,  203,    0,    0,    0, -105,  -68,  162,   66,
 -110,  -31,   90,   37,    0,   96,   77,  324,  135,    0,
  -41,    0,  -96,  -36,  -95,   63,    0,    0,    0,    0,
    0,    0,   63,    0,    0,    0,    0,    0,    0,    0,
   48,  143,  -23,   63,    0,  -39,  174,    0,    0,    0,
    0,  102,    0,    4,    0,    0,    0,  -64,    0,    0,
  -70,    0,  166,    0,    0,    0,    0, -109,    0,   92,
    0,  -13,    0,    0,    5,  181,  -66,    0,  -44,   63,
    0,    0,  344,    0,  151,   82,   97,    0,  -32,  190,
  115,  169,    0,    3,  409,    0,    0,    0,    0,   63,
   63,   63,  180,   40,   40,  165,  167,  216,    0,  142,
    0,    0,    0,    0,    0,    0,    0,  172,  175,    0,
  252,  103,  226,    5,    0,    0,  402,    0,    0,    0,
    0,    0,    0,    0,  176,    0,   19,    0,    0,  200,
    0,    0,    0,  174,  174,  180,    0,    0,    0,    0,
    0,    0,    0,    0,  -10,  235,    0,    0,    0,    0,
    0,  307,    0,    0,  119,    0,  242,    0,    0,    0,
  246,  269,    0,    0,    0,    0,   -4,    0,    0,    0,
    0,    0,  289,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -40,    0,    0,  311,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  320,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   89,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  293,    0,   27,   39,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  302,  303,    0,    0,    0,    0,   86,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   47,   52,  309,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   17,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  338,   11,  613,    0,  345,    0,  265,    0,    0,  312,
    0,  310,   26,    2,  -35,   79,  -83,  442,    0, -101,
  207, -173,    0,    0,    0,    0,    0,    0,    0,    0,
  194,   -6,  313,   73,  295,    0,  478,    0,  416,  -75,
  196,    0,
};
final static int YYTABLESIZE=739;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        110,
   47,   64,  128,   73,  131,  110,   65,   65,   81,  111,
   49,  176,   73,   74,  106,  154,   48,   81,   52,  140,
  110,  141,   66,   13,   63,   45,  159,  160,  219,  162,
   35,  219,   75,  215,   76,  162,   75,   59,   76,  224,
   40,   47,   44,   49,  162,  140,  181,  141,  216,  100,
  100,   90,   36,   45,  225,   41,   96,   48,   73,  211,
  149,  182,  207,   81,  184,  185,   91,  127,  127,  127,
  127,  127,  156,  127,  196,   37,  156,  115,   75,  117,
   76,  117,  117,  117,   81,  127,  127,  118,  127,  118,
  118,  118,  119,   48,  119,  119,  119,  117,  117,  100,
  117,   51,   73,   38,  100,  118,  118,   81,  118,  104,
  119,  119,  161,  119,   92,  207,   39,   42,  161,  172,
   58,   59,   65,  151,  105,   47,   48,  161,  151,  110,
  110,  158,  116,  113,  110,   55,  132,  120,  134,  118,
  173,   48,  146,  204,  140,  135,  141,  116,  114,  110,
  157,    4,    4,   54,  119,  174,  143,    1,  178,  220,
    2,    3,    4,    5,    6,    7,   94,    8,    9,  130,
   10,   11,   12,  179,    1,  124,  133,    2,    3,    4,
    5,    6,    7,  139,    8,    9,  110,   10,   11,   12,
  110,  170,  167,    4,    5,    6,    7,    4,    5,    6,
    7,  152,  102,   95,   58,  165,  153,  148,  189,  180,
  191,  140,  195,  141,  186,  144,  126,  127,  197,  209,
  145,  164,  140,  190,  141,  192,  175,  166,   68,  177,
    4,  137,   68,  198,  210,   69,   70,   71,   72,   47,
  110,   67,   67,  213,   69,   70,   71,   72,    4,   77,
   78,   79,   80,   77,   78,   79,   80,   47,  214,  193,
   47,   47,   47,   47,   47,   47,  194,   47,   47,  206,
   47,   47,   47,   48,   68,  217,   48,   48,   48,   48,
   48,   48,  127,   48,   48,  221,   48,   48,   48,  222,
   69,   70,   71,   72,  117,   77,   78,   79,   80,    5,
    6,    7,  118,  127,  127,  127,  127,  119,   95,  223,
    4,   69,   70,   71,   72,  117,  117,  117,  117,    2,
  136,  137,  138,  118,  118,  118,  118,   93,  119,  119,
  119,  119,  226,   89,   69,   70,   71,   72,    1,  187,
  188,    2,   87,   88,    5,    6,    7,  227,    8,   86,
   43,   10,   11,   12,    5,    6,    7,  137,  147,    1,
  103,  112,    2,   95,    4,    5,    6,    7,  205,    8,
  212,  125,   10,   11,   12,    1,  199,  142,    2,    0,
    4,    5,    6,    7,    0,    8,    0,    0,   10,   11,
   12,    1,   97,   97,    2,  107,  107,    5,    6,    7,
    0,    8,    0,    0,   10,   11,   12,    1,    0,    0,
    2,    0,    0,    5,    6,    7,    0,    8,    0,    0,
   10,   11,   12,    4,    5,    6,    7,    4,    5,    6,
    7,  218,    0,   95,    5,    6,    7,   95,    0,   57,
    0,    0,   97,   95,  140,  208,  141,   97,  121,  183,
   86,  140,  155,  141,    0,    0,  155,   86,    0,    1,
    0,    0,    2,    3,    4,    5,    6,    7,  168,    8,
    9,    0,   10,   11,   12,    0,    0,    0,    0,   86,
    0,   86,    0,    0,    0,    0,    0,    0,   86,  101,
  101,    0,    0,    0,    0,  117,    0,    0,    0,   86,
    0,    0,    0,    0,    0,  201,  201,    0,    1,    0,
    0,    2,   85,    4,    5,    6,    7,    0,    8,   85,
    0,   10,   11,   12,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   86,    0,    0,    0,  101,
    0,   85,    0,   85,  101,    0,  201,    0,    0,  201,
   85,  117,    0,    0,    0,   86,   86,   86,    0,   86,
   86,   85,    0,    1,    0,    0,    2,    0,    4,    5,
    6,    7,    0,    8,    0,    0,   10,   11,   12,    0,
    1,    0,    0,    2,    0,    0,    5,    6,    7,    0,
    8,    0,   86,   10,   11,   12,    0,   85,    0,  117,
    1,    0,    0,    2,    0,    0,    5,    6,    7,    0,
    8,    0,   17,   10,   11,   12,    0,   85,   85,   85,
    0,   85,   85,    0,    0,   17,    1,   46,    0,    2,
    3,    4,    5,    6,    7,    0,    8,    9,    0,   10,
   11,   12,   60,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   85,   46,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  122,  122,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  169,    0,  169,  171,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   44,   40,   41,   46,   46,   46,   45,   41,
   40,   44,   40,   41,  125,  125,    0,   45,   40,   43,
   61,   45,   61,  123,   31,   15,  110,   41,  202,   40,
   40,  205,   60,   44,   62,   40,   60,   40,   62,   44,
   44,   41,  125,   40,   40,   43,   44,   45,   59,   48,
   49,   44,  276,   43,   59,   59,  125,   41,   40,   41,
  125,   59,  164,   45,  140,  141,   59,   41,   42,   43,
   44,   45,  108,   47,  158,  272,  112,   41,   60,   41,
   62,   43,   44,   45,   45,   59,   60,   41,   62,   43,
   44,   45,   41,  123,   43,   44,   45,   59,   60,   98,
   62,  123,   40,  272,  103,   59,   60,   45,   62,   44,
   59,   60,  123,   62,   42,  217,  272,   40,  123,  126,
  123,   40,   46,   98,   59,  125,  123,  123,  103,   51,
   52,   40,   44,   44,   46,  272,   64,   61,   66,   44,
   44,  125,   41,   41,   43,   73,   45,   59,   59,   61,
   59,  262,  262,   40,   59,   59,   84,  257,   44,   41,
  260,  261,  262,  263,  264,  265,  272,  267,  268,  266,
  270,  271,  272,   59,  257,   41,  272,  260,  261,  262,
  263,  264,  265,   41,  267,  268,  108,  270,  271,  272,
  112,   41,  120,  262,  263,  264,  265,  262,  263,  264,
  265,  272,   41,  272,  123,  272,   41,  272,   44,   41,
   44,   43,   41,   45,  142,   42,  258,  259,   44,   44,
   47,   41,   43,   59,   45,   59,  259,  272,  256,   40,
  262,  272,  256,   59,   59,  272,  273,  274,  275,  269,
  281,  281,  281,   44,  272,  273,  274,  275,  262,  277,
  278,  279,  280,  277,  278,  279,  280,  257,   59,   44,
  260,  261,  262,  263,  264,  265,  125,  267,  268,   44,
  270,  271,  272,  257,  256,   41,  260,  261,  262,  263,
  264,  265,  256,  267,  268,   44,  270,  271,  272,   44,
  272,  273,  274,  275,  256,  277,  278,  279,  280,  263,
  264,  265,  256,  277,  278,  279,  280,  256,  272,   41,
    0,  272,  273,  274,  275,  277,  278,  279,  280,    0,
  273,  274,  275,  277,  278,  279,  280,  125,  277,  278,
  279,  280,   44,   41,  272,  273,  274,  275,  257,  144,
  145,  260,   41,   41,  263,  264,  265,   59,  267,   41,
   13,  270,  271,  272,  263,  264,  265,  272,   94,  257,
   49,   52,  260,  272,  262,  263,  264,  265,  162,  267,
  177,   59,  270,  271,  272,  257,  125,   83,  260,   -1,
  262,  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,
  272,  257,   48,   49,  260,   51,   52,  263,  264,  265,
   -1,  267,   -1,   -1,  270,  271,  272,  257,   -1,   -1,
  260,   -1,   -1,  263,  264,  265,   -1,  267,   -1,   -1,
  270,  271,  272,  262,  263,  264,  265,  262,  263,  264,
  265,  125,   -1,  272,  263,  264,  265,  272,   -1,   24,
   -1,   -1,   98,  272,   43,   44,   45,  103,  125,   41,
   35,   43,  108,   45,   -1,   -1,  112,   42,   -1,  257,
   -1,   -1,  260,  261,  262,  263,  264,  265,  125,  267,
  268,   -1,  270,  271,  272,   -1,   -1,   -1,   -1,   64,
   -1,   66,   -1,   -1,   -1,   -1,   -1,   -1,   73,   48,
   49,   -1,   -1,   -1,   -1,   54,   -1,   -1,   -1,   84,
   -1,   -1,   -1,   -1,   -1,  161,  162,   -1,  257,   -1,
   -1,  260,   35,  262,  263,  264,  265,   -1,  267,   42,
   -1,  270,  271,  272,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  120,   -1,   -1,   -1,   98,
   -1,   64,   -1,   66,  103,   -1,  202,   -1,   -1,  205,
   73,  110,   -1,   -1,   -1,  140,  141,  142,   -1,  144,
  145,   84,   -1,  257,   -1,   -1,  260,   -1,  262,  263,
  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,   -1,
  257,   -1,   -1,  260,   -1,   -1,  263,  264,  265,   -1,
  267,   -1,  177,  270,  271,  272,   -1,  120,   -1,  158,
  257,   -1,   -1,  260,   -1,   -1,  263,  264,  265,   -1,
  267,   -1,    0,  270,  271,  272,   -1,  140,  141,  142,
   -1,  144,  145,   -1,   -1,   13,  257,   15,   -1,  260,
  261,  262,  263,  264,  265,   -1,  267,  268,   -1,  270,
  271,  272,   30,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  177,   43,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   58,   59,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  123,   -1,  125,  126,
};
}
final static short YYFINAL=14;
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
"programa : '{' componentes_programa '}'",
"programa : '{' componentes_programa",
"programa : componentes_programa '}'",
"programa : componentes_programa",
"componentes_programa : declaracion",
"componentes_programa : sentencia",
"componentes_programa : componentes_programa sentencia",
"componentes_programa : componentes_programa declaracion",
"declaracion : declaracion_clase",
"declaracion : declaracion_funcion",
"declaracion_clase : encabezado_clase bloque_clase ','",
"declaracion_clase : encabezado_clase IMPLEMENT ID bloque_clase ','",
"declaracion_clase : encabezado_interface bloque_interfaz ','",
"declaracion_clase : encabezado_clase bloque_clase ';'",
"declaracion_clase : encabezado_clase IMPLEMENT ID bloque_clase ';'",
"declaracion_clase : encabezado_interface bloque_interfaz ';'",
"encabezado_clase : CLASS ID",
"encabezado_interface : INTERFACE ID",
"bloque_clase : '{' cuerpo_clase '}'",
"bloque_clase : '{' cuerpo_clase herencia_clase '}'",
"bloque_clase : '(' cuerpo_clase ')'",
"bloque_clase : '{' '}'",
"bloque_clase : '(' ')'",
"herencia_clase : ID ','",
"bloque_interfaz : '{' cuerpo_interfaz '}'",
"bloque_interfaz : '(' cuerpo_interfaz ')'",
"bloque_interfaz : '{' '}'",
"bloque_interfaz : '(' ')'",
"cuerpo_clase : miembro_clase",
"cuerpo_clase : cuerpo_clase miembro_clase",
"miembro_clase : declaracion_variable",
"miembro_clase : declaracion_funcion",
"cuerpo_interfaz : declaracion_funcion_interfaz",
"cuerpo_interfaz : cuerpo_interfaz declaracion_funcion_interfaz",
"cuerpo_interfaz : declaracion_funcion",
"cuerpo_interfaz : cuerpo_interfaz declaracion_funcion",
"declaracion_funcion_interfaz : encabezado_funcion '(' ')' ','",
"declaracion_funcion_interfaz : encabezado_funcion '(' parametro_formal ')' ','",
"declaracion_funcion_interfaz : encabezado_funcion '(' ')' ';'",
"declaracion_funcion_interfaz : encabezado_funcion '(' parametro_formal ')' ';'",
"declaracion_funcion_interfaz : encabezado_funcion parametro_formal ','",
"declaracion_funcion_interfaz : encabezado_funcion ';'",
"declaracion_funcion_interfaz : encabezado_funcion parametro_formal ';'",
"declaracion_variable : tipo lista_de_id ','",
"declaracion_funcion : encabezado_funcion '(' ')' bloque_sentencias_funcion ','",
"declaracion_funcion : encabezado_funcion '(' parametro_formal ')' bloque_sentencias_funcion ','",
"declaracion_funcion : encabezado_funcion '(' ')' bloque_sentencias_funcion",
"declaracion_funcion : encabezado_funcion '(' parametro_formal ')' bloque_sentencias_funcion",
"encabezado_funcion : VOID ID",
"parametro_formal : tipo ID",
"bloque_sentencias_funcion : '{' lista_sentencias_funcion '}'",
"bloque_sentencias_funcion : '(' lista_sentencias_funcion ')'",
"bloque_sentencias_funcion : '{' '}'",
"bloque_sentencias_funcion : '(' ')'",
"lista_sentencias_funcion : sentencia_funcion",
"lista_sentencias_funcion : lista_sentencias_funcion sentencia_funcion",
"sentencia_funcion : declaracion_funcion",
"sentencia_funcion : sentencia",
"sentencia : sentencia_expresion",
"sentencia : sentencia_seleccion",
"sentencia : sentencia_iteracion",
"sentencia : sentencia_imprimir",
"sentencia : sentencia_retorno",
"sentencia_retorno : RETURN ','",
"sentencia_retorno : RETURN ';'",
"sentencia_imprimir : PRINT CADENA ','",
"sentencia_imprimir : PRINT CADENA ';'",
"sentencia_seleccion : condicion_if cuerpo_then END_IF ','",
"sentencia_seleccion : condicion_if cuerpo_then cuerpo_else END_IF ','",
"sentencia_seleccion : condicion_if cuerpo_then END_IF ';'",
"sentencia_seleccion : condicion_if cuerpo_then cuerpo_else END_IF ';'",
"sentencia_seleccion : condicion_if cuerpo_then ','",
"sentencia_seleccion : condicion_if cuerpo_then cuerpo_else ','",
"condicion_if : IF '(' comparacion ')'",
"condicion_if : IF '(' ')'",
"cuerpo_then : sentencia",
"cuerpo_then : bloque_sentencias",
"cuerpo_else : ELSE sentencia",
"cuerpo_else : ELSE bloque_sentencias",
"bloque_sentencias : '{' lista_sentencias '}'",
"bloque_sentencias : '(' lista_sentencias ')'",
"bloque_sentencias : '{' '}'",
"bloque_sentencias : '(' ')'",
"lista_sentencias : sentencia",
"lista_sentencias : lista_sentencias sentencia",
"comparacion : operacion comparador operacion",
"comparacion : operacion comparador",
"comparacion : comparador operacion",
"comparacion : comparador",
"comparador : '<'",
"comparador : '>'",
"comparador : \"<=\"",
"comparador : \">=\"",
"comparador : \"==\"",
"comparador : \"!!\"",
"comparador : error",
"sentencia_iteracion : inicio_do bloque_sentencias WHILE '(' comparacion ')' ','",
"sentencia_iteracion : inicio_do bloque_sentencias WHILE '(' comparacion ')' ';'",
"sentencia_iteracion : inicio_do bloque_sentencias WHILE '(' ')' ','",
"inicio_do : DO",
"sentencia_expresion : declaracion_variable",
"sentencia_expresion : factor_inmediato",
"sentencia_expresion : asignacion",
"sentencia_expresion : llamado_clase '(' ')' ','",
"sentencia_expresion : llamado_clase '(' ')' ';'",
"sentencia_expresion : llamado_clase '(' operacion ')' ','",
"sentencia_expresion : llamado_clase '(' operacion ')' ';'",
"sentencia_expresion : TOD '(' operacion ')' ','",
"sentencia_expresion : TOD '(' operacion ')' ';'",
"llamado_clase : ID",
"llamado_clase : llamado_clase '.' ID",
"asignacion : llamado_clase '=' operacion ','",
"asignacion : llamado_clase '=' operacion ';'",
"asignacion : tipo llamado_clase '=' operacion ','",
"lista_de_id : lista_de_id ';' ID",
"lista_de_id : ID",
"operacion : termino",
"operacion : operacion '+' termino",
"operacion : operacion '-' termino",
"termino : factor",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : '(' operacion ')'",
"factor : factor_comun",
"factor : factor_inmediato",
"factor_inmediato : llamado_clase \"--\"",
"factor_comun : llamado_clase",
"factor_comun : '-' CTE_DOUBLE",
"factor_comun : CTE_DOUBLE",
"factor_comun : '-' CTE_LONG",
"factor_comun : CTE_LONG",
"factor_comun : CTE_UINT",
"factor_comun : '-' CTE_UINT",
"tipo : DOUBLE",
"tipo : UINT",
"tipo : LONG",
"tipo : ID",
};

//#line 496 ".\gramatica.y"


static AnalizadorLexico anLex = null;
static Parser par = null;
static Token token = null;
static ArrayList<String>  polaca;
static ArrayList<Error> errores;
static String ambito = ":main";
static String tipo = "";
static Tabla tablaSimbolos;
static Deque<Integer> pila; //Utiliza metodos push,pop y peek

public static void main(String[] args) throws Exception{
        System.out.println("Iniciando compilacion...");
        tablaSimbolos = new Tabla();
        errores = new ArrayList<Error>();
        anLex = new AnalizadorLexico(args,tablaSimbolos,errores);
        
        polaca = new ArrayList<String>();
        pila = new LinkedList<>();
        par = new Parser(false);
        par.run();

        anLex.MostrarTablaSimbolos();
        anLex.MostrarErrores();
        
        for (int i = 0; i < polaca.size(); i++) {
          System.out.println(i + " " + polaca.get(i));
        }

        System.out.println("Fin de la compilacion");
}

private int yylex(){
        try {
          token = anLex.getToken();
          int intToken = token.getTipo().getNumero();
          //System.out.println("yylex: " + token);
          yylval = new ParserVal(token.toString());
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
//#line 623 "Parser.java"
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
//#line 18 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que terminar con \'}\'", anLex.getLinea()));
         }
break;
case 3:
//#line 21 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que arrancar con \'{\'", anLex.getLinea()));
         }
break;
case 4:
//#line 24 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que estar contenido en \'{\' \'}\'", anLex.getLinea()));
        }
break;
case 11:
//#line 39 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if((val_peek(1).sval != null)){
                                if(!tablaSimbolos.agregarHerencia(val_peek(2).sval,val_peek(1).sval)){
                                        errores.add(new Error("No esta declarada la clase " + val_peek(1).sval.substring(0,val_peek(1).sval.lastIndexOf(":")), anLex.getLinea())); 
                                }
                        }
                  }
break;
case 12:
//#line 47 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if (!tablaSimbolos.agregarInterfaz(val_peek(4).sval,val_peek(2).sval + ":main")){
                                errores.add(new Error("No se encuentra la interfaz " + val_peek(2).sval, anLex.getLinea())); 
                        }
                        if((val_peek(1).sval != null)){
                                if((!tablaSimbolos.agregarHerencia(val_peek(4).sval,val_peek(1).sval))){
                                        errores.add(new Error("No esta declarada la clase " + val_peek(1).sval.substring(0,val_peek(1).sval.lastIndexOf(":")), anLex.getLinea())); 
                                }
                        }
                  }
break;
case 13:
//#line 58 ".\gramatica.y"
{
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 14:
//#line 61 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 15:
//#line 65 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 16:
//#line 69 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 17:
//#line 75 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        yyval.sval = val_peek(0).sval + ambito;
                        ambito += ":" + val_peek(0).sval;
                 }
break;
case 18:
//#line 84 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        ambito += ":" + val_peek(0).sval;
                     }
break;
case 19:
//#line 92 ".\gramatica.y"
{
                yyval.sval = null;
             }
break;
case 20:
//#line 95 ".\gramatica.y"
{
                yyval.sval = val_peek(1).sval + ":main";
             }
break;
case 21:
//#line 98 ".\gramatica.y"
{
                errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
             }
break;
case 22:
//#line 101 ".\gramatica.y"
{
                errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
             }
break;
case 23:
//#line 104 ".\gramatica.y"
{
                errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
             }
break;
case 26:
//#line 112 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 27:
//#line 115 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                }
break;
case 28:
//#line 118 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 35:
//#line 133 ".\gramatica.y"
{
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
break;
case 36:
//#line 136 ".\gramatica.y"
{
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
break;
case 37:
//#line 141 ".\gramatica.y"
{
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 38:
//#line 144 ".\gramatica.y"
{
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 39:
//#line 147 ".\gramatica.y"
{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 40:
//#line 151 ".\gramatica.y"
{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 41:
//#line 155 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 42:
//#line 159 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 43:
//#line 163 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 45:
//#line 172 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 46:
//#line 175 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 47:
//#line 178 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 48:
//#line 182 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 49:
//#line 188 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        ambito += ":" + val_peek(0).sval;
                   }
break;
case 50:
//#line 196 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                 }
break;
case 52:
//#line 204 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 53:
//#line 207 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
break;
case 54:
//#line 210 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 65:
//#line 232 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                  }
break;
case 67:
//#line 238 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                   }
break;
case 68:
//#line 243 ".\gramatica.y"
{
                        polaca.add(pila.pop(),"[" + String.valueOf(polaca.size() + 1) + "]");
                    }
break;
case 69:
//#line 246 ".\gramatica.y"
{
                        polaca.add(pila.pop(),"[" + String.valueOf(polaca.size() + 1) + "]");
                    }
break;
case 70:
//#line 249 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 71:
//#line 252 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 72:
//#line 255 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
break;
case 73:
//#line 258 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
break;
case 74:
//#line 263 ".\gramatica.y"
{
                pila.push(polaca.size());
                polaca.add("BF"); /*bifurcacion por falso;*/
            }
break;
case 75:
//#line 267 ".\gramatica.y"
{
                errores.add(new Error("Falta declarar condicion del IF ubicado", anLex.getLinea()));
            }
break;
case 76:
//#line 271 ".\gramatica.y"
{
                polaca.add(pila.pop(), "[" + String.valueOf(polaca.size() + 3) + "]");
                pila.push(polaca.size());
                polaca.add("BI"); /*bifurcacion incondicional;*/
            }
break;
case 77:
//#line 276 ".\gramatica.y"
{
                polaca.add(pila.pop(), "[" + String.valueOf(polaca.size() + 3) + "]");
                pila.push(polaca.size());
                polaca.add("BI"); /*bifurcacion incondicional;*/
            }
break;
case 81:
//#line 288 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 82:
//#line 291 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
break;
case 83:
//#line 294 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 86:
//#line 303 ".\gramatica.y"
{
                polaca.add(val_peek(1).sval);
            }
break;
case 87:
//#line 306 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 88:
//#line 309 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 89:
//#line 312 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 90:
//#line 317 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 91:
//#line 320 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 92:
//#line 323 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 93:
//#line 326 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 94:
//#line 329 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 95:
//#line 332 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 96:
//#line 335 ".\gramatica.y"
{
                errores.add(new Error("Comparacion mal definida", anLex.getLinea()));
           }
break;
case 97:
//#line 340 ".\gramatica.y"
{
                        polaca.add("[" + String.valueOf(pila.pop()) + "]");
                        polaca.add("BF");
                    }
break;
case 98:
//#line 344 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 99:
//#line 347 ".\gramatica.y"
{
                        errores.add(new Error("No se declaro una condicion de corte en el WHILE que se ubica", anLex.getLinea()));
                    }
break;
case 100:
//#line 352 ".\gramatica.y"
{
                pila.push(polaca.size());
             }
break;
case 104:
//#line 360 ".\gramatica.y"
{
                        if(!tablaSimbolos.existeMetodo(val_peek(3).sval,ambito)){
                                errores.add(new Error("No se declaro el Metodo " + val_peek(3).sval + " en el ambito reconocible", anLex.getLinea()));
                        }
                        polaca.add("Call a " + val_peek(3).sval);
                    }
break;
case 105:
//#line 366 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 107:
//#line 370 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 109:
//#line 374 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 110:
//#line 379 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval; 
              }
break;
case 111:
//#line 382 ".\gramatica.y"
{
                yyval.sval += "." + val_peek(0).sval;
              }
break;
case 112:
//#line 387 ".\gramatica.y"
{
                if(!tablaSimbolos.existeVariable(val_peek(3).sval,ambito)){
                        errores.add(new Error("No se declaro la variable " + val_peek(3).sval + " en el ambito reconocible", anLex.getLinea()));
                }
                polaca.add(val_peek(3).sval);polaca.add("=");
           }
break;
case 113:
//#line 393 ".\gramatica.y"
{
                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
           }
break;
case 114:
//#line 396 ".\gramatica.y"
{
                errores.add(new Error("No se puede declarar y asignar en la misma línea", anLex.getLinea()));
           }
break;
case 115:
//#line 402 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
            }
break;
case 116:
//#line 407 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
            }
break;
case 118:
//#line 415 ".\gramatica.y"
{
                polaca.add("+");
          }
break;
case 119:
//#line 418 ".\gramatica.y"
{
                polaca.add("-");
          }
break;
case 121:
//#line 424 ".\gramatica.y"
{
                polaca.add("*");
        }
break;
case 122:
//#line 427 ".\gramatica.y"
{
                polaca.add("/");
        }
break;
case 126:
//#line 437 ".\gramatica.y"
{
                        if(!tablaSimbolos.existeVariable(val_peek(1).sval,ambito)){
                                errores.add(new Error("No se declaro la variable " + val_peek(1).sval + " en el ambito reconocible", anLex.getLinea()));
                        }
                        polaca.add(val_peek(1).sval);polaca.add("1");polaca.add("-");{polaca.add(val_peek(1).sval);polaca.add("=");}
                 }
break;
case 127:
//#line 445 ".\gramatica.y"
{
                if(!tablaSimbolos.existeVariable(val_peek(0).sval,ambito)){
                        errores.add(new Error("No se declaro la variable " + val_peek(0).sval + " en el ambito reconocible", anLex.getLinea()));
                }
                polaca.add(val_peek(0).sval);
             }
break;
case 128:
//#line 451 ".\gramatica.y"
{
                anLex.convertirNegativo(val_peek(0).sval);
                polaca.add("-" + val_peek(0).sval);
             }
break;
case 129:
//#line 455 ".\gramatica.y"
{
                polaca.add(val_peek(0).sval);
             }
break;
case 130:
//#line 458 ".\gramatica.y"
{
                anLex.convertirNegativo(val_peek(0).sval);
                polaca.add("-" + val_peek(0).sval);
             }
break;
case 131:
//#line 462 ".\gramatica.y"
{
                if(CheckRangoLong(val_peek(0).sval)){
                        errores.add(new Error("LONG fuera de rango", anLex.getLinea()));}
                else {
                        polaca.add(val_peek(0).sval);
                } 
             }
break;
case 132:
//#line 469 ".\gramatica.y"
{
                polaca.add(val_peek(0).sval);
             }
break;
case 133:
//#line 472 ".\gramatica.y"
{
                errores.add(new Error("Las variables tipo UINT no pueden ser negativas", anLex.getLinea()));
             }
break;
case 134:
//#line 477 ".\gramatica.y"
{
        tipo = "DOUBLE";
     }
break;
case 135:
//#line 480 ".\gramatica.y"
{
        tipo = "UINT";
     }
break;
case 136:
//#line 483 ".\gramatica.y"
{
        tipo = "LONG";
     }
break;
case 137:
//#line 486 ".\gramatica.y"
{
        if(!tablaSimbolos.existeClase(val_peek(0).sval,ambito)){
                errores.add(new Error("No se declaro la variable " + val_peek(0).sval + " en el ambito reconocible", anLex.getLinea()));
        }
        tipo = val_peek(0).sval;
        tablaSimbolos.eliminarSimbolo(val_peek(0).sval);
     }
break;
//#line 1403 "Parser.java"
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
