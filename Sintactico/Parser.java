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
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

//#line 27 "Parser.java"




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
   23,   23,   23,   23,   23,   23,   23,   39,   39,   38,
   38,   38,   19,   19,   34,   34,   34,   40,   40,   40,
   41,   41,   41,   37,   42,   42,   42,   42,   42,   42,
   42,   18,   18,   18,   18,
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
    1,    1,    1,    4,    5,    4,    5,    1,    3,    4,
    4,    5,    3,    1,    1,    3,    3,    1,    3,    3,
    1,    1,    4,    2,    1,    2,    1,    2,    1,    1,
    2,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  134,  133,  132,  100,    0,    0,
    0,    0,    0,    0,    5,    6,    9,   10,    0,    0,
  101,    0,    0,   59,   60,   61,   62,   63,    0,    0,
  102,  103,    0,    0,    0,   17,   49,   18,   64,   65,
    0,    3,    8,    7,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   76,    0,   77,
    0,    0,    0,    0,  124,   96,    0,  108,  129,  130,
  127,   75,   90,   91,   92,   93,   94,   95,    0,    0,
    0,    0,  122,    0,    0,  118,  121,   66,   67,    1,
    0,  135,   22,   32,    0,   29,   31,    0,   23,    0,
   11,   14,   27,   35,    0,   33,    0,   28,    0,   13,
   16,    0,    0,    0,   44,    0,    0,   82,   84,    0,
   83,    0,    0,    0,   72,    0,    0,    0,    0,  109,
    0,    0,  128,  131,  126,   74,    0,    0,    0,    0,
    0,    0,    0,    0,   19,    0,   30,  114,   21,   25,
   36,   34,   42,    0,    0,   26,    0,    0,    0,    0,
   50,  113,    0,   80,   85,   81,   78,   79,   68,   70,
    0,   73,    0,  104,  106,    0,  110,  111,    0,    0,
    0,    0,  119,  120,   12,   15,   24,   20,    0,    0,
   41,   43,   53,   58,   57,    0,   55,   54,    0,   45,
    0,  112,   69,   71,    0,    0,  105,  107,  123,   37,
   39,    0,   51,   56,   52,   46,   99,    0,   38,   40,
   97,   98,
};
final static short yydgoto[] = {                         13,
   14,   15,  194,   17,   18,   19,   48,   20,   51,   95,
  146,  105,   96,   21,  106,   22,  113,   23,   54,  159,
  196,  197,   24,   25,   26,   27,   28,   29,   59,  126,
   80,   60,  120,   81,   82,   30,   31,   32,   33,   85,
   86,   87,
};
final static short yysindex[] = {                      -116,
  -13, -259, -227, -210,    0,    0,    0,    0, -186,  -16,
    0,  300,    0,  -78,    0,    0,    0,    0,  -10,  -24,
    0,   85,  -95,    0,    0,    0,    0,    0,   79,   -9,
    0,    0,  -38,  -27,   -4,    0,    0,    0,    0,    0,
  159,    0,    0,    0,  -76,  234,  -37,    6, -112,  -32,
   74,  137,    0,  115,  119,  189,  112,    0,  -39,    0,
  -65,   60,  -67,   51,    0,    0,  177,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -136,  195,
  -23,   51,    0,  -43,   -1,    0,    0,    0,    0,    0,
   -6,    0,    0,    0,  245,    0,    0,  -33,    0,  134,
    0,    0,    0,    0, -101,    0,   48,    0,  -31,    0,
    0,   -2,  199,  -12,    0,   -5,   51,    0,    0,  212,
    0,  123,   79,  129,    0,  -25,  202,  147,   91,    0,
   84,   16,    0,    0,    0,    0,   51,   51,   51,  150,
   51,   51,  153,  205,    0,  145,    0,    0,    0,    0,
    0,    0,    0,  140,  165,    0,  -49,   90,  255,   -2,
    0,    0,  155,    0,    0,    0,    0,    0,    0,    0,
  178,    0,   18,    0,    0,  297,    0,    0,    8,   -1,
   -1,  150,    0,    0,    0,    0,    0,    0,  -15,  260,
    0,    0,    0,    0,    0,  175,    0,    0,  101,    0,
  275,    0,    0,    0,  277,  299,    0,    0,    0,    0,
    0,   -8,    0,    0,    0,    0,    0,  348,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -40,    0,    0,  338,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  345,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  111,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  307,    0,   25,   30,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  326,  337,
    0,    0,    0,  109,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   38,
   50,  350,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   15,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  377,   12,  473,    0,   11,    0,  303,    0,    0,  361,
    0,  360,   35,   76,  -57,   53,  -30,  448,    0, -148,
  253,  -36,    0,    0,    0,    0,    0,    0,    0,    0,
  241,  -19,  368,  439,  336,    0,  396,    0,  351,   29,
   62,    0,
};
final static int YYTABLESIZE=602;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        108,
   47,   62,   63,   99,  125,  108,   12,   63,  108,  156,
   61,  201,  103,   72,   48,   50,   35,   79,  172,  137,
  108,  138,   64,  150,  158,   43,   34,   39,  210,   47,
   57,  158,   73,   47,   74,  219,   73,  158,   74,   88,
  141,   47,   40,  211,   36,  142,   42,  152,  209,  101,
  220,  152,   43,   63,   89,   48,   94,   94,  205,  104,
  104,   37,   79,  201,  102,  125,  125,  125,  125,  125,
  115,  125,  115,  115,  115,  193,  155,   73,  116,   74,
  116,  116,  116,  125,  125,   38,  125,  154,  115,  115,
  117,  115,  117,  117,  117,   79,  116,  116,   49,  116,
  128,  107,  107,  168,   79,   94,  153,  157,  117,  117,
   94,  117,   46,   56,  157,  151,   46,  110,   57,  151,
  157,   97,   97,  190,   52,   47,  137,  177,  138,  147,
  198,  176,  111,  137,  147,  138,  133,  134,  135,   48,
    1,  215,  178,    2,    3,    4,    5,    6,    7,    4,
    8,    9,  121,   10,  114,   11,  108,  107,  115,  214,
    4,  107,  214,  166,   63,  180,  181,  195,  195,  114,
   97,  108,  169,  116,  149,   97,   53,  112,    1,  117,
  189,    2,    3,    4,    5,    6,    7,  170,    8,    9,
  174,   10,  137,   11,  138,   91,  185,  137,  202,  138,
  127,   56,  183,  184,  130,  175,  195,    1,  191,  195,
    2,  186,    4,    5,    6,    7,  132,    8,  123,  124,
   10,  203,   11,  192,    4,    5,    6,    7,   66,    4,
    4,  135,   66,  171,   92,  136,  204,   65,  148,  160,
  108,  173,   65,   67,   68,   69,   70,   71,  187,   75,
   76,   77,   78,   75,   76,   77,   78,   47,   45,  161,
   47,   47,   47,   47,   47,   47,  162,   47,   47,  188,
   47,   48,   47,   66,   48,   48,   48,   48,   48,   48,
  125,   48,   48,   90,   48,  115,   48,   68,   67,   68,
   69,   70,   71,  116,   75,   76,   77,   78,  200,  213,
  212,  125,  125,  125,  125,  117,  115,  115,  115,  115,
    5,    6,    7,  118,  116,  116,  116,  116,  216,   92,
  217,   67,   68,   69,   70,   71,  117,  117,  117,  117,
   67,   68,   69,   70,   71,    1,  164,    4,    2,  218,
  207,    5,    6,    7,    2,    8,    1,   89,   10,    2,
   11,    4,    5,    6,    7,  208,    8,    1,   93,   10,
    2,   11,    4,    5,    6,    7,   87,    8,    1,  145,
   10,    2,   11,   55,    5,    6,    7,   88,    8,    1,
  135,   10,    2,   11,   84,    5,    6,    7,   41,    8,
   86,  221,   10,  143,   11,    4,    5,    6,    7,    5,
    6,    7,    5,    6,    7,   92,  222,  100,   92,  109,
  199,   92,   84,  206,   84,    1,  139,    0,    2,    3,
    4,    5,    6,    7,  122,    8,    9,    0,   10,   83,
   11,    1,   84,    0,    2,    0,    4,    5,    6,    7,
    0,    8,    0,    0,   10,    1,   11,    0,    2,    0,
    0,    5,    6,    7,    0,    8,    0,   83,   10,   83,
   11,    0,    0,    0,    0,    0,    0,   84,    1,    0,
    0,    2,   16,    0,    5,    6,    7,   83,    8,    0,
    0,   10,  179,   11,   16,    0,   44,   84,   84,   84,
    0,   84,   84,   98,   98,    4,    5,    6,    7,  114,
  129,   58,  131,    0,    0,   92,    4,    5,    6,    7,
    0,    0,   83,   44,    0,    0,  144,    0,    0,    0,
  140,    0,    0,   84,    0,    0,    0,    0,  119,  119,
    0,    0,   83,   83,   83,    0,   83,   83,    0,    0,
    0,    0,   98,    0,    0,    0,    0,   98,    0,    0,
    0,    0,    0,    0,  114,  163,    1,    0,    0,    2,
    3,    4,    5,    6,    7,    0,    8,    9,   83,   10,
    0,   11,    0,    0,    0,    0,    0,  182,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  165,    0,  165,  167,    0,    0,    0,    0,
    0,  114,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   46,   41,   44,   46,  123,   46,   41,   41,
   30,  160,  125,   41,    0,   40,  276,   45,   44,   43,
   61,   45,   61,  125,   40,   14,   40,   44,   44,   40,
   40,   40,   60,   40,   62,   44,   60,   40,   62,   44,
   42,   41,   59,   59,  272,   47,  125,  105,   41,   44,
   59,  109,   41,   46,   59,   41,   46,   47,   41,   49,
   50,  272,   45,  212,   59,   41,   42,   43,   44,   45,
   41,   47,   43,   44,   45,  125,  107,   60,   41,   62,
   43,   44,   45,   59,   60,  272,   62,   40,   59,   60,
   41,   62,   43,   44,   45,   45,   59,   60,  123,   62,
   41,   49,   50,  123,   45,   95,   59,  123,   59,   60,
  100,   62,  123,  123,  123,  105,  123,   44,   40,  109,
  123,   46,   47,  154,   40,  125,   43,   44,   45,   95,
   41,   41,   59,   43,  100,   45,  273,  274,  275,  125,
  257,   41,   59,  260,  261,  262,  263,  264,  265,  262,
  267,  268,   41,  270,   44,  272,   46,  105,   44,  196,
  262,  109,  199,   41,   46,  137,  138,  157,  158,   59,
   95,   61,   44,   59,   41,  100,  272,   41,  257,   61,
   41,  260,  261,  262,  263,  264,  265,   59,  267,  268,
   44,  270,   43,  272,   45,  272,   44,   43,   44,   45,
  266,  123,  141,  142,  272,   59,  196,  257,   44,  199,
  260,   59,  262,  263,  264,  265,   40,  267,  258,  259,
  270,   44,  272,   59,  262,  263,  264,  265,  256,  262,
  262,  272,  256,  259,  272,   41,   59,  281,  272,   41,
  281,   40,  281,  271,  272,  273,  274,  275,   44,  277,
  278,  279,  280,  277,  278,  279,  280,  257,  269,  272,
  260,  261,  262,  263,  264,  265,  272,  267,  268,  125,
  270,  257,  272,  256,  260,  261,  262,  263,  264,  265,
  256,  267,  268,  125,  270,  256,  272,  272,  271,  272,
  273,  274,  275,  256,  277,  278,  279,  280,   44,  125,
   41,  277,  278,  279,  280,  256,  277,  278,  279,  280,
  263,  264,  265,  125,  277,  278,  279,  280,   44,  272,
   44,  271,  272,  273,  274,  275,  277,  278,  279,  280,
  271,  272,  273,  274,  275,  257,  125,    0,  260,   41,
   44,  263,  264,  265,    0,  267,  257,   41,  270,  260,
  272,  262,  263,  264,  265,   59,  267,  257,  125,  270,
  260,  272,  262,  263,  264,  265,   41,  267,  257,  125,
  270,  260,  272,   23,  263,  264,  265,   41,  267,  257,
  272,  270,  260,  272,   34,  263,  264,  265,   12,  267,
   41,   44,  270,   91,  272,  262,  263,  264,  265,  263,
  264,  265,  263,  264,  265,  272,   59,   47,  272,   50,
  158,  272,   62,  173,   64,  257,   81,   -1,  260,  261,
  262,  263,  264,  265,   57,  267,  268,   -1,  270,   34,
  272,  257,   82,   -1,  260,   -1,  262,  263,  264,  265,
   -1,  267,   -1,   -1,  270,  257,  272,   -1,  260,   -1,
   -1,  263,  264,  265,   -1,  267,   -1,   62,  270,   64,
  272,   -1,   -1,   -1,   -1,   -1,   -1,  117,  257,   -1,
   -1,  260,    0,   -1,  263,  264,  265,   82,  267,   -1,
   -1,  270,  132,  272,   12,   -1,   14,  137,  138,  139,
   -1,  141,  142,   46,   47,  262,  263,  264,  265,   52,
   62,   29,   64,   -1,   -1,  272,  262,  263,  264,  265,
   -1,   -1,  117,   41,   -1,   -1,  272,   -1,   -1,   -1,
   82,   -1,   -1,  173,   -1,   -1,   -1,   -1,   56,   57,
   -1,   -1,  137,  138,  139,   -1,  141,  142,   -1,   -1,
   -1,   -1,   95,   -1,   -1,   -1,   -1,  100,   -1,   -1,
   -1,   -1,   -1,   -1,  107,  117,  257,   -1,   -1,  260,
  261,  262,  263,  264,  265,   -1,  267,  268,  173,  270,
   -1,  272,   -1,   -1,   -1,   -1,   -1,  139,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  120,   -1,  122,  123,   -1,   -1,   -1,   -1,
   -1,  154,
};
}
final static short YYFINAL=13;
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
"sentencia_expresion : llamado_clase '(' operacion ')' ','",
"sentencia_expresion : llamado_clase '(' ')' ';'",
"sentencia_expresion : llamado_clase '(' operacion ')' ';'",
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
"factor : factor_comun",
"factor : factor_inmediato",
"factor : TOD '(' llamado_clase ')'",
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

//#line 563 ".\gramatica.y"

static final int MAXNIVELHERENCIA = 3;
static final int MAXPROFUNDIDADVOID = 1;
static AnalizadorLexico anLex = null;
static Parser par = null;
static Token token = null;
static HashMap<String, ArrayList<String>> metodosPolaca; // :main -> polacaMain | :main:asd -> polacaAsd
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
        
        metodosPolaca = new HashMap<String, ArrayList<String>>();
        polaca = new ArrayList<String>();
        metodosPolaca.put(":main",polaca);
        pila = new LinkedList<>();
        par = new Parser(false);
        par.run();

        tablaSimbolos.imprimirTabla();
        for (String variable : tablaSimbolos.variablesNoUsadas()){
            if(variable.contains(":")){
                    errores.add(new Error("La variable " + variable.substring(0,variable.indexOf(":")) + " no fue asignada dentro del ambito de " + variable.substring(variable.lastIndexOf(":") + 1, variable.length()),"WARNING"));
            }
        }
        if (errores.size()>0){
            for (Error error : errores) {
                System.out.println(error.toString());
            }
            //throw new Exception("Se han encontrado los errores anteriores");
        }
        //System.out.println(polaca.toString());
        
        
        /* for (int i = 0; i < polaca.size(); i++) {
          System.out.println(i + " " + polaca.get(i));
        } */

        for (String key : metodosPolaca.keySet()) {
            System.out.println(key + " = " + metodosPolaca.get(key).toString());
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
//#line 615 "Parser.java"
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
//#line 19 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que terminar con \'}\'", anLex.getLinea()));
         }
break;
case 3:
//#line 22 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que arrancar con \'{\'", anLex.getLinea()));
         }
break;
case 4:
//#line 25 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que estar contenido en \'{\' \'}\'", anLex.getLinea()));
        }
break;
case 11:
//#line 40 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if((val_peek(1).sval != null)){
                                if(!tablaSimbolos.agregarHerencia(val_peek(2).sval,val_peek(1).sval)){
                                        errores.add(new Error("No esta declarada la clase '" + val_peek(1).sval.substring(0,val_peek(1).sval.lastIndexOf(":")), anLex.getLinea() + "'")); 
                                } else {
                                    if (tablaSimbolos.getNivelHerencia(val_peek(2).sval) >= MAXNIVELHERENCIA ) {
                                        errores.add(new Error("La clase '" + val_peek(2).sval.substring(0,val_peek(2).sval.indexOf(":")) + "' no puede heredar, debido a que la clase padre '" + tablaSimbolos.getPadreClase(tablaSimbolos.getPadreClase(val_peek(2).sval)).substring(0,val_peek(2).sval.indexOf(":")) + "' ya posee el maximo nivel de herencia permitido (" + MAXNIVELHERENCIA + ")", "ERROR"));
                                    }
                                    ArrayList<String> aux = new ArrayList<>();
                                    aux = tablaSimbolos.metodoSobreescriptos(val_peek(2).sval);
                                    if (aux.size() > 0) {
                                        errores.add(new Error("La clase " + val_peek(2).sval.substring(0,val_peek(2).sval.indexOf(":")) + " esta sobreescribiendo el/los metodo/s:" + aux.toString() + " de su clase padre", anLex.getLinea()));
                                    }
                                }
                        }
                  }
break;
case 12:
//#line 57 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if (!tablaSimbolos.agregarInterfaz(val_peek(4).sval,val_peek(2).sval + ":main")){
                            errores.add(new Error("No se encuentra la interfaz " + val_peek(2).sval, anLex.getLinea())); 
                        } else {
                            if (!tablaSimbolos.implementaMetodosInterfaz(ambito + ":" + val_peek(4).sval.substring(0,val_peek(4).sval.indexOf(":")),ambito + ":" + val_peek(2).sval)){
                                errores.add(new Error("La clase '" + val_peek(4).sval.substring(0,val_peek(4).sval.indexOf(":")) + "' no implementa todos los metodos de la interfaz '" + val_peek(2).sval + "'", anLex.getLinea()));
                            }
                        }
                        if((val_peek(1).sval != null)){
                                if((!tablaSimbolos.agregarHerencia(val_peek(4).sval,val_peek(1).sval))){
                                        errores.add(new Error("No esta declarada la clase '" + val_peek(1).sval.substring(0,val_peek(1).sval.lastIndexOf(":")), anLex.getLinea() + "'")); 
                                }
                                ArrayList<String> aux = new ArrayList<>();
                                aux = tablaSimbolos.metodoSobreescriptos(val_peek(4).sval);
                                if (aux.size() > 0) {
                                    errores.add(new Error("La clase " + val_peek(4).sval.substring(0,val_peek(4).sval.indexOf(":")) + " esta sobreescribiendo el/los metodo/s:" + aux.toString() + " de su clase padre", anLex.getLinea()));
                                }
                        }
                  }
break;
case 13:
//#line 77 ".\gramatica.y"
{
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 14:
//#line 80 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 15:
//#line 84 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 16:
//#line 88 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 17:
//#line 94 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        yyval.sval = val_peek(0).sval + ambito;
                        ambito += ":" + val_peek(0).sval;
                 }
break;
case 18:
//#line 103 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        ambito += ":" + val_peek(0).sval;
                     }
break;
case 19:
//#line 111 ".\gramatica.y"
{
                yyval.sval = null;
             }
break;
case 20:
//#line 114 ".\gramatica.y"
{
                yyval.sval = val_peek(1).sval + ":main";
             }
break;
case 21:
//#line 117 ".\gramatica.y"
{
                errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
             }
break;
case 22:
//#line 120 ".\gramatica.y"
{
                errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
             }
break;
case 23:
//#line 123 ".\gramatica.y"
{
                errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
             }
break;
case 26:
//#line 131 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 27:
//#line 134 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                }
break;
case 28:
//#line 137 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 35:
//#line 152 ".\gramatica.y"
{
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
break;
case 36:
//#line 155 ".\gramatica.y"
{
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
break;
case 37:
//#line 160 ".\gramatica.y"
{
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 38:
//#line 164 ".\gramatica.y"
{
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 39:
//#line 168 ".\gramatica.y"
{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 40:
//#line 173 ".\gramatica.y"
{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 41:
//#line 178 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 42:
//#line 183 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 43:
//#line 188 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 44:
//#line 195 ".\gramatica.y"
{
                        tablaSimbolos.checkAtributosDeClase(val_peek(1).sval,ambito);
                     }
break;
case 45:
//#line 200 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).add("RET");
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 46:
//#line 204 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).add("RET");
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 47:
//#line 208 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        metodosPolaca.remove(ambito);
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 48:
//#line 213 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        metodosPolaca.remove(ambito);
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 49:
//#line 220 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                            errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        } else {
                            if (tablaSimbolos.addNivelVoid(val_peek(0).sval,ambito)) {
                                if (tablaSimbolos.getProfundidadVoid(val_peek(0).sval,ambito) > MAXPROFUNDIDADVOID) {
                                    errores.add(new Error("Se supero el nivel de anidamiento (" + MAXPROFUNDIDADVOID + ") de los metodos", anLex.getLinea()));
                                }
                            }
                        }
                        ambito += ":" + val_peek(0).sval;
                        ArrayList<String> polacas = new ArrayList<String>();
                        metodosPolaca.put(ambito,polacas);
                   }
break;
case 50:
//#line 236 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        } else {
                            /*cambiar funcion a con parametro ambito.subString(lastIndexOf(":"),ambito.length())*/
                            tablaSimbolos.setParametro(ambito);
                            metodosPolaca.get(ambito).add(val_peek(0).sval);
                            metodosPolaca.get(ambito).add("=");
                        }
                 }
break;
case 52:
//#line 249 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 53:
//#line 252 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
break;
case 54:
//#line 255 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 64:
//#line 276 ".\gramatica.y"
{ System.out.println("RETURN");}
break;
case 65:
//#line 277 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                  }
break;
case 66:
//#line 282 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).add(val_peek(1).sval);
                        metodosPolaca.get(ambito).add("PRINT");
                   }
break;
case 67:
//#line 286 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                   }
break;
case 68:
//#line 291 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).add(pila.pop(),"[" + String.valueOf(metodosPolaca.get(ambito).size() + 1) + "]");
                    }
break;
case 69:
//#line 294 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).add(pila.pop(),"[" + String.valueOf(metodosPolaca.get(ambito).size() + 1) + "]");
                    }
break;
case 70:
//#line 297 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 71:
//#line 300 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 72:
//#line 303 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
break;
case 73:
//#line 306 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
break;
case 74:
//#line 311 ".\gramatica.y"
{
                pila.push(metodosPolaca.get(ambito).size());
                metodosPolaca.get(ambito).add("BF"); /*bifurcacion por falso;*/
            }
break;
case 75:
//#line 315 ".\gramatica.y"
{
                errores.add(new Error("Falta declarar condicion del IF ubicado", anLex.getLinea()));
            }
break;
case 76:
//#line 319 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add(pila.pop(), "[" + String.valueOf(metodosPolaca.get(ambito).size() + 3) + "]");
                pila.push(metodosPolaca.get(ambito).size());
                metodosPolaca.get(ambito).add("BI"); /*bifurcacion incondicional;*/
            }
break;
case 77:
//#line 324 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add(pila.pop(), "[" + String.valueOf(metodosPolaca.get(ambito).size() + 3) + "]");
                pila.push(metodosPolaca.get(ambito).size());
                metodosPolaca.get(ambito).add("BI"); /*bifurcacion incondicional;*/
            }
break;
case 81:
//#line 336 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 82:
//#line 339 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
break;
case 83:
//#line 342 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 86:
//#line 351 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add(val_peek(1).sval);
            }
break;
case 87:
//#line 354 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 88:
//#line 357 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 89:
//#line 360 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 90:
//#line 365 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 91:
//#line 368 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 92:
//#line 371 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 93:
//#line 374 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 94:
//#line 377 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 95:
//#line 380 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 96:
//#line 383 ".\gramatica.y"
{
                errores.add(new Error("Comparacion mal definida", anLex.getLinea()));
           }
break;
case 97:
//#line 388 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).add("[" + String.valueOf(pila.pop()) + "]");
                        metodosPolaca.get(ambito).add("BF");
                    }
break;
case 98:
//#line 392 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 99:
//#line 395 ".\gramatica.y"
{
                        errores.add(new Error("No se declaro una condicion de corte en el WHILE que se ubica", anLex.getLinea()));
                    }
break;
case 100:
//#line 400 ".\gramatica.y"
{
                pila.push(metodosPolaca.get(ambito).size());
             }
break;
case 104:
//#line 408 ".\gramatica.y"
{
                        if(!tablaSimbolos.existeMetodo(val_peek(3).sval,ambito,false)){
                                errores.add(new Error("No se encuentra declarado el metodo " + val_peek(3).sval + " dentro del ambito reconocible", anLex.getLinea()));
                        } else {
                            String ambitoClase = tablaSimbolos.getAmbitoMetodoInvocado(val_peek(3).sval,ambito);
                            metodosPolaca.get(ambito).add("Call " + ambitoClase);
                        }
                    }
break;
case 105:
//#line 416 ".\gramatica.y"
{/* Chequear tipo operacion con parametro de funcion*/
                        if(!tablaSimbolos.existeMetodo(val_peek(4).sval,ambito,true)){
                                errores.add(new Error("No se encuentra declarado el metodo " + val_peek(4).sval + " dentro del ambito reconocible", anLex.getLinea()));
                        } else {
                            String ambitoClase = tablaSimbolos.getAmbitoMetodoInvocado(val_peek(4).sval,ambito);
                            metodosPolaca.get(ambito).add("Call " + ambitoClase);
                        }
                    }
break;
case 106:
//#line 424 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 107:
//#line 427 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 108:
//#line 432 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
              }
break;
case 109:
//#line 435 ".\gramatica.y"
{
                yyval.sval += "." + val_peek(0).sval;
              }
break;
case 110:
//#line 440 ".\gramatica.y"
{
                if(!tablaSimbolos.existeVariable(val_peek(3).sval,ambito)){
                        errores.add(new Error("No se declaro la variable " + val_peek(3).sval + " en el ambito reconocible", anLex.getLinea()));
                } else {

                        /*tablaSimbolos.agregarSimbolo($1.sval + ambito,new Token());*/
                        tablaSimbolos.setUso(val_peek(3).sval,ambito,true);
                }
                metodosPolaca.get(ambito).add(val_peek(3).sval);metodosPolaca.get(ambito).add("=");
           }
break;
case 111:
//#line 450 ".\gramatica.y"
{
                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
           }
break;
case 112:
//#line 453 ".\gramatica.y"
{
                errores.add(new Error("No se puede declarar y asignar en la misma línea", anLex.getLinea()));
           }
break;
case 113:
//#line 459 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
                yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval;
            }
break;
case 114:
//#line 465 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
                yyval.sval = val_peek(0).sval;
            }
break;
case 116:
//#line 474 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add("+");
          }
break;
case 117:
//#line 477 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add("-");
          }
break;
case 119:
//#line 483 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add("*");
        }
break;
case 120:
//#line 486 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add("/");
        }
break;
case 123:
//#line 493 ".\gramatica.y"
{
                if(!tablaSimbolos.existeVariable(val_peek(1).sval,ambito)){
                        errores.add(new Error("No se declaro la variable " + val_peek(1).sval + " en el ambito reconocible", anLex.getLinea()));
                }
                metodosPolaca.get(ambito).add(val_peek(1).sval);
                metodosPolaca.get(ambito).add("TOD");
       }
break;
case 124:
//#line 502 ".\gramatica.y"
{
                        if(!tablaSimbolos.existeVariable(val_peek(1).sval,ambito)){
                                errores.add(new Error("No se declaro la variable " + val_peek(1).sval + " en el ambito reconocible", anLex.getLinea()));
                        }
                        metodosPolaca.get(ambito).add(val_peek(1).sval);metodosPolaca.get(ambito).add("1");metodosPolaca.get(ambito).add("-");{metodosPolaca.get(ambito).add(val_peek(1).sval);metodosPolaca.get(ambito).add("=");}
                 }
break;
case 125:
//#line 510 ".\gramatica.y"
{
                if(!tablaSimbolos.existeVariable(val_peek(0).sval,ambito)){
                        errores.add(new Error("No se declaro la variable " + val_peek(0).sval + " en el ambito reconocible", anLex.getLinea()));
                }
                metodosPolaca.get(ambito).add(val_peek(0).sval);
             }
break;
case 126:
//#line 516 ".\gramatica.y"
{
                anLex.convertirNegativo(val_peek(0).sval);
                tablaSimbolos.eliminarSimbolo("-" + val_peek(0).sval);
                metodosPolaca.get(ambito).add("-" + val_peek(0).sval);
             }
break;
case 127:
//#line 521 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add(val_peek(0).sval);
             }
break;
case 128:
//#line 524 ".\gramatica.y"
{
                anLex.convertirNegativo(val_peek(0).sval);
                tablaSimbolos.eliminarSimbolo(val_peek(0).sval);
                metodosPolaca.get(ambito).add("-" + val_peek(0).sval);
             }
break;
case 129:
//#line 529 ".\gramatica.y"
{
                if(CheckRangoLong(val_peek(0).sval)){
                        errores.add(new Error("LONG fuera de rango", anLex.getLinea()));}
                else {
                        metodosPolaca.get(ambito).add(val_peek(0).sval);
                } 
             }
break;
case 130:
//#line 536 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add(val_peek(0).sval);
             }
break;
case 131:
//#line 539 ".\gramatica.y"
{
                errores.add(new Error("Las constantes tipo UINT no pueden ser negativas", anLex.getLinea()));
             }
break;
case 132:
//#line 544 ".\gramatica.y"
{
        tipo = "DOUBLE";
     }
break;
case 133:
//#line 547 ".\gramatica.y"
{
        tipo = "UINT";
     }
break;
case 134:
//#line 550 ".\gramatica.y"
{
        tipo = "LONG";
     }
break;
case 135:
//#line 553 ".\gramatica.y"
{
        if(!tablaSimbolos.existeClase(val_peek(0).sval,ambito)){
                errores.add(new Error("No se declaro la variable " + val_peek(0).sval + " en el ambito reconocible", anLex.getLinea()));
        }
        tipo = val_peek(0).sval;
        tablaSimbolos.eliminarSimbolo(val_peek(0).sval);
     }
break;
//#line 1479 "Parser.java"
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
