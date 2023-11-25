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
import Assembler.*;

//#line 28 "Parser.java"




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
   15,   15,   15,   14,    5,    5,    5,    5,    5,    5,
   16,   17,   20,   20,   20,   20,   21,   21,   22,   22,
    3,    3,    3,    3,    3,   27,   27,   26,   26,   24,
   24,   24,   24,   24,   24,   28,   28,   29,   29,   30,
   30,   32,   32,   32,   32,   33,   33,   31,   31,   31,
   31,   35,   35,   35,   35,   35,   35,   35,   25,   25,
   25,   36,   23,   23,   23,   23,   23,   23,   23,   39,
   39,   38,   38,   38,   19,   19,   34,   34,   34,   40,
   40,   40,   41,   41,   41,   37,   42,   42,   42,   42,
   42,   42,   42,   18,   18,   18,   18,
};
final static short yylen[] = {                            2,
    3,    2,    2,    1,    1,    1,    2,    2,    1,    1,
    3,    5,    3,    3,    5,    3,    2,    2,    3,    4,
    3,    2,    2,    2,    3,    3,    2,    2,    1,    2,
    1,    1,    1,    2,    1,    2,    4,    5,    4,    5,
    3,    2,    3,    3,    5,    6,    4,    5,    4,    5,
    2,    2,    3,    3,    2,    2,    1,    2,    1,    1,
    1,    1,    1,    1,    1,    2,    2,    3,    3,    4,
    5,    4,    5,    3,    4,    4,    3,    1,    1,    2,
    2,    3,    3,    2,    2,    1,    2,    3,    2,    2,
    1,    1,    1,    1,    1,    1,    1,    1,    7,    7,
    6,    1,    1,    1,    1,    4,    5,    4,    5,    1,
    3,    4,    4,    5,    3,    1,    1,    3,    3,    1,
    3,    3,    1,    1,    4,    3,    1,    2,    1,    2,
    1,    1,    2,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  136,  135,  134,  102,    0,    0,
    0,    0,    0,    0,    5,    6,    9,   10,    0,    0,
  103,    0,    0,   61,   62,   63,   64,   65,    0,    0,
  104,  105,    0,    0,    0,   17,   51,   18,   66,   67,
    0,    3,    8,    7,    0,    0,    0,    0,    0,    0,
    0,  137,    0,    0,    0,    0,    0,    0,    0,    0,
   78,    0,   79,    0,    0,    0,    0,    0,   98,    0,
  110,  131,  132,  129,   77,   92,   93,   94,   95,   96,
   97,    0,    0,    0,    0,  124,    0,    0,  120,  123,
   68,   69,    1,    0,   22,   32,    0,   29,   31,    0,
   23,    0,   11,   14,   27,   35,    0,   33,    0,   28,
    0,   13,   16,    0,    0,    0,    0,    0,   52,   44,
    0,    0,   84,   86,    0,   85,    0,    0,    0,   74,
    0,    0,    0,    0,  111,    0,  126,    0,  130,  133,
  128,   76,    0,    0,    0,    0,    0,    0,    0,    0,
   19,    0,   30,  116,   21,   25,   36,   34,   42,    0,
    0,   26,    0,    0,    0,   55,   60,   59,    0,   57,
   56,    0,   47,  115,    0,   82,   87,   83,   80,   81,
   70,   72,    0,   75,    0,  106,  108,    0,  112,  113,
    0,    0,    0,    0,  121,  122,   12,   15,   24,   20,
    0,    0,   41,   43,   45,    0,   48,   53,   58,   54,
  114,   71,   73,    0,    0,  107,  109,  125,   37,   39,
    0,   46,  101,    0,   38,   40,   99,  100,
};
final static short yydgoto[] = {                         13,
   14,   15,  167,   17,   18,   19,   48,   20,   51,   97,
  152,  107,   98,   21,  108,   22,   54,   23,   57,  118,
  169,  170,   24,   25,   26,   27,   28,   29,   62,  131,
   83,   63,  125,   84,   85,   30,   31,   32,   33,   88,
   89,   90,
};
final static short yysindex[] = {                      -106,
  -28, -252, -229, -215,    0,    0,    0,    0, -211,  -18,
    0,  319,    0,  -67,    0,    0,    0,    0,  -10,  -24,
    0,   89, -186,    0,    0,    0,    0,    0,   79,   12,
    0,    0,  -38,  -27,   -4,    0,    0,    0,    0,    0,
  175,    0,    0,    0, -166,  -94,  -37,   59, -118,  -32,
   83,    0,  147,   14, -156,    0,   88,   77,  231,  122,
    0,  -39,    0, -146,   60, -136,   51,   95,    0,  109,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  189,  105,  -23,   51,    0,  -43,  134,    0,    0,
    0,    0,    0,   20,    0,    0,  196,    0,    0,  -99,
    0,  144,    0,    0,    0,    0, -114,    0,   48,    0,
  -31,    0,    0,   14,    7,  -49,  100,  142,    0,    0,
  -95,   51,    0,    0,  292,    0,  133,   79,  101,    0,
  -25,  152,  106,  384,    0,  422,    0,   51,    0,    0,
    0,    0,   51,   51,   51,  194,   51,   51,  123,  162,
    0,   85,    0,    0,    0,    0,    0,    0,    0,  150,
  -15,    0,  192,   14,  198,    0,    0,    0,  215,    0,
    0,  111,    0,    0,  473,    0,    0,    0,    0,    0,
    0,    0,  128,    0,   18,    0,    0,  140,    0,    0,
  408,  134,  134,  194,    0,    0,    0,    0,    0,    0,
   -6,   10,    0,    0,    0,  205,    0,    0,    0,    0,
    0,    0,    0,  216,  199,    0,    0,    0,    0,    0,
    5,    0,    0,  145,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -40,    0,    0,  267,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  270,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  163,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  243,    0,   25,   30,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  247,  258,    0,    0,    0,   29,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   38,   50,  273,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   15,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  307,   90,  512,    0,  493,    0,  251,    0,    0,  294,
    0,  300,  115,   78,  -75,   72,   -7,  468,    0,  -87,
  238, -107,    0,    0,    0,    0,    0,    0,    0,    0,
  173,  -17,  299,  385,  282,    0,  389,    0,  361,  204,
  190,    0,
};
final static int YYTABLESIZE=665;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        110,
   49,   65,   66,  101,  130,  110,  105,   66,  110,  162,
  156,   34,   64,   75,   50,   50,   12,   82,  184,  143,
  110,  144,   67,   35,  117,   39,  163,  165,  203,   47,
   95,  158,   76,  117,   77,  158,   76,  219,   77,   91,
   40,   49,   36,  204,  117,  115,  117,  164,  225,  117,
  221,   60,  220,  117,   92,   50,   37,   42,  214,   47,
   38,  209,   82,  226,  209,  127,  127,  127,  127,  127,
  117,  127,  117,  117,  117,  166,  206,   76,  118,   77,
  118,  118,  118,  127,  127,   56,  127,  160,  117,  117,
  119,  117,  119,  119,  119,   82,  118,  118,   49,  118,
  133,  161,  103,   43,   82,   94,  159,  116,  119,  119,
  180,  119,   46,  163,  165,  119,  116,  104,   60,  132,
  109,  109,   66,   99,   99,   49,  112,  116,   53,  116,
   43,  120,  116,  206,   59,  135,  116,  122,  137,   50,
  171,  113,   46,    4,  181,  142,  121,    4,  138,  186,
    1,  210,  202,    2,    3,    4,    5,    6,    7,  182,
    8,    9,  126,   10,  187,   11,  197,    4,    5,    6,
    7,  212,  154,  178,   99,  147,  174,   52,  109,   99,
  148,  198,  109,  216,  155,  173,  213,  114,  227,    1,
  201,  185,    2,    3,    4,    5,    6,    7,  217,    8,
    9,   59,   10,  228,   11,  199,  116,    1,  110,  200,
    2,  153,    4,    5,    6,    7,  153,    8,  128,  129,
   10,  116,   11,  110,    4,    5,    6,    7,   69,    4,
    4,  137,   69,  183,   52,  205,  143,   68,  144,  224,
  110,  207,   68,   70,   71,   72,   73,   74,  222,   78,
   79,   80,   81,   78,   79,   80,   81,   49,   45,  223,
   49,   49,   49,   49,   49,   49,    4,   49,   49,    2,
   49,   50,   49,   69,   50,   50,   50,   50,   50,   50,
  127,   50,   50,   91,   50,  117,   50,   89,   70,   71,
   72,   73,   74,  118,   78,   79,   80,   81,   90,   93,
  137,  127,  127,  127,  127,  119,  117,  117,  117,  117,
    5,    6,    7,   88,  118,  118,  118,  118,   41,   52,
  151,   70,   71,   72,   73,   74,  119,  119,  119,  119,
   70,   71,   72,   73,   74,    1,  195,  196,    2,  208,
  102,    5,    6,    7,  149,    8,  192,  193,   10,  111,
   11,    5,    6,    7,  172,  123,    1,  215,  127,    2,
   52,    4,    5,    6,    7,  145,    8,    1,    0,   10,
    2,   11,    4,    5,    6,    7,    0,    8,    1,    0,
   10,    2,   11,   58,    5,    6,    7,    0,    8,    1,
    0,   10,    2,   11,   87,    5,    6,    7,    0,    8,
    0,    0,   10,    0,   11,    4,    5,    6,    7,    5,
    6,    7,    5,    6,    7,   52,  176,    0,   52,    0,
    0,   52,   86,    0,  188,   87,  143,   87,  144,    0,
    0,    1,    0,    0,    2,    3,    4,    5,    6,    7,
    0,    8,    9,    0,   10,   87,   11,    0,  218,  134,
  143,  136,  144,   86,    0,   86,    0,    4,    5,    6,
    7,  139,  140,  141,  143,  189,  144,  150,    0,  146,
    0,    1,    0,   86,    2,    0,    4,    5,    6,    7,
  190,    8,   87,    0,   10,    0,   11,    1,    0,   55,
    2,    0,    0,    5,    6,    7,    0,    8,   87,    0,
   10,    0,   11,   87,   87,   87,  175,   87,   87,    0,
   86,   16,    0,  100,  100,  143,  211,  144,    0,    0,
   55,    0,  191,   16,    0,   44,   86,    0,    0,  194,
    0,   86,   86,   86,    0,   86,   86,    0,   96,   96,
   61,  106,  106,    0,    0,   87,    0,    0,    1,    0,
    0,    2,   44,    0,    5,    6,    7,    0,    8,    0,
    0,   10,    0,   11,  100,    0,    0,    0,    0,  100,
  124,  124,    0,   86,    0,    1,   55,    0,    2,    3,
    4,    5,    6,    7,    0,    8,    9,    0,   10,   96,
   11,    0,    0,    0,   96,    0,    0,    0,    0,  157,
    0,    0,    0,  157,    0,    0,    0,    0,  168,  168,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   55,    0,    0,
    0,    0,    0,    0,    0,    0,  177,    0,  177,  179,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  168,    0,    0,  168,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   46,   41,   44,   46,  125,   46,   41,   41,
  125,   40,   30,   41,    0,   40,  123,   45,   44,   43,
   61,   45,   61,  276,   40,   44,  114,  115,   44,   40,
  125,  107,   60,   40,   62,  111,   60,   44,   62,   44,
   59,   41,  272,   59,   40,   53,   40,   41,   44,   40,
   41,   40,   59,   40,   59,   41,  272,  125,   41,   40,
  272,  169,   45,   59,  172,   41,   42,   43,   44,   45,
   41,   47,   43,   44,   45,  125,  164,   60,   41,   62,
   43,   44,   45,   59,   60,  272,   62,   40,   59,   60,
   41,   62,   43,   44,   45,   45,   59,   60,  123,   62,
   41,  109,   44,   14,   45,  272,   59,  123,   59,   60,
  128,   62,  123,  201,  202,  272,  123,   59,   40,  266,
   49,   50,   46,   46,   47,  125,   44,  123,   40,  123,
   41,   44,  123,  221,  123,  272,  123,   61,   44,  125,
   41,   59,  123,  262,   44,   41,   59,  262,   40,   44,
  257,   41,  160,  260,  261,  262,  263,  264,  265,   59,
  267,  268,   41,  270,   59,  272,   44,  262,  263,  264,
  265,   44,  272,   41,   97,   42,  272,  272,  107,  102,
   47,   59,  111,   44,   41,   44,   59,   41,   44,  257,
   41,   40,  260,  261,  262,  263,  264,  265,   59,  267,
  268,  123,  270,   59,  272,   44,   44,  257,   46,  125,
  260,   97,  262,  263,  264,  265,  102,  267,  258,  259,
  270,   59,  272,   61,  262,  263,  264,  265,  256,  262,
  262,  272,  256,  259,  272,   44,   43,  281,   45,   41,
  281,   44,  281,  271,  272,  273,  274,  275,   44,  277,
  278,  279,  280,  277,  278,  279,  280,  257,  269,   44,
  260,  261,  262,  263,  264,  265,    0,  267,  268,    0,
  270,  257,  272,  256,  260,  261,  262,  263,  264,  265,
  256,  267,  268,   41,  270,  256,  272,   41,  271,  272,
  273,  274,  275,  256,  277,  278,  279,  280,   41,  125,
  272,  277,  278,  279,  280,  256,  277,  278,  279,  280,
  263,  264,  265,   41,  277,  278,  279,  280,   12,  272,
  125,  271,  272,  273,  274,  275,  277,  278,  279,  280,
  271,  272,  273,  274,  275,  257,  147,  148,  260,  125,
   47,  263,  264,  265,   94,  267,  143,  144,  270,   50,
  272,  263,  264,  265,  117,  125,  257,  185,   60,  260,
  272,  262,  263,  264,  265,   84,  267,  257,   -1,  270,
  260,  272,  262,  263,  264,  265,   -1,  267,  257,   -1,
  270,  260,  272,   23,  263,  264,  265,   -1,  267,  257,
   -1,  270,  260,  272,   34,  263,  264,  265,   -1,  267,
   -1,   -1,  270,   -1,  272,  262,  263,  264,  265,  263,
  264,  265,  263,  264,  265,  272,  125,   -1,  272,   -1,
   -1,  272,   34,   -1,   41,   65,   43,   67,   45,   -1,
   -1,  257,   -1,   -1,  260,  261,  262,  263,  264,  265,
   -1,  267,  268,   -1,  270,   85,  272,   -1,   41,   65,
   43,   67,   45,   65,   -1,   67,   -1,  262,  263,  264,
  265,  273,  274,  275,   43,   44,   45,  272,   -1,   85,
   -1,  257,   -1,   85,  260,   -1,  262,  263,  264,  265,
   59,  267,  122,   -1,  270,   -1,  272,  257,   -1,   22,
  260,   -1,   -1,  263,  264,  265,   -1,  267,  138,   -1,
  270,   -1,  272,  143,  144,  145,  122,  147,  148,   -1,
  122,    0,   -1,   46,   47,   43,   44,   45,   -1,   -1,
   53,   -1,  138,   12,   -1,   14,  138,   -1,   -1,  145,
   -1,  143,  144,  145,   -1,  147,  148,   -1,   46,   47,
   29,   49,   50,   -1,   -1,  185,   -1,   -1,  257,   -1,
   -1,  260,   41,   -1,  263,  264,  265,   -1,  267,   -1,
   -1,  270,   -1,  272,   97,   -1,   -1,   -1,   -1,  102,
   59,   60,   -1,  185,   -1,  257,  109,   -1,  260,  261,
  262,  263,  264,  265,   -1,  267,  268,   -1,  270,   97,
  272,   -1,   -1,   -1,  102,   -1,   -1,   -1,   -1,  107,
   -1,   -1,   -1,  111,   -1,   -1,   -1,   -1,  116,  117,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  160,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,  127,  128,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  169,   -1,   -1,  172,
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
"declaracion_funcion : encabezado_funcion parametro_formal bloque_sentencias_funcion ','",
"declaracion_funcion : encabezado_funcion '(' parametro_formal bloque_sentencias_funcion ','",
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
"factor : TOD '(' operacion ')'",
"factor_inmediato : llamado_clase \"--\" ','",
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

//#line 618 ".\gramatica.y"

static final int MAXNIVELHERENCIA = 3;
static final int MAXPROFUNDIDADVOID = 1;
static AnalizadorLexico anLex = null;
static Parser par = null;
static Token token = null;
static HashMap<String, ArrayList<String>> metodosPolaca; // :main -> polacaMain | :main:asd -> polacaAsd
static ArrayList<String> polaca;
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
        boolean exeptions = false;
        if (errores.size()>0){
            for (Error error : errores) {
                System.out.println(error.toString());
                if(error.getTipo() == "ERROR"){
                    exeptions = true;
                }
            }
            //throw new Exception("Se han encontrado los errores anteriores");
        }
        //System.out.println(polaca.toString());

        for (String key : metodosPolaca.keySet()) {
            System.out.println(key + " = " + metodosPolaca.get(key).toString());
        }
        
        if(!exeptions){
            ArrayList<Error> erroresConversionDeTipo = new ArrayList<Error>();
            GeneradorAssembler generarAssembler = new GeneradorAssembler(metodosPolaca,tablaSimbolos,erroresConversionDeTipo);
            generarAssembler.start();
            if (erroresConversionDeTipo.size() > 0) {
                for (Error error : erroresConversionDeTipo) {
                    System.out.println(error.toString());
                }
            } else {
                generarAssembler.exportar();
            }
        } else {
            System.out.println("No se pudo generar el Assembler debido a que el codigo presenta errores");
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
//#line 644 "Parser.java"
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
//#line 19 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add("END MAIN");
         }
break;
case 2:
//#line 22 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que terminar con \'}\'", anLex.getLinea()));
         }
break;
case 3:
//#line 25 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que arrancar con \'{\'", anLex.getLinea()));
         }
break;
case 4:
//#line 28 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que estar contenido en \'{\' \'}\'", anLex.getLinea()));
        }
break;
case 11:
//#line 43 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if((val_peek(1).sval != null)){
                                if(!tablaSimbolos.agregarHerencia(val_peek(2).sval,val_peek(1).sval)){
                                        errores.add(new Error("No esta declarada la clase '" + val_peek(1).sval.substring(0,val_peek(1).sval.lastIndexOf(":")), anLex.getLinea())); 
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
//#line 60 ".\gramatica.y"
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
//#line 80 ".\gramatica.y"
{
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 14:
//#line 83 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 15:
//#line 87 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 16:
//#line 91 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 17:
//#line 97 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        yyval.sval = val_peek(0).sval + ambito;
                        ambito += ":" + val_peek(0).sval;
                 }
break;
case 18:
//#line 106 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        ambito += ":" + val_peek(0).sval;
                     }
break;
case 19:
//#line 114 ".\gramatica.y"
{
                yyval.sval = null;
             }
break;
case 20:
//#line 117 ".\gramatica.y"
{
                yyval.sval = val_peek(1).sval + ":main";
             }
break;
case 21:
//#line 120 ".\gramatica.y"
{
                errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
             }
break;
case 22:
//#line 123 ".\gramatica.y"
{
                errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
             }
break;
case 23:
//#line 126 ".\gramatica.y"
{
                errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
             }
break;
case 26:
//#line 134 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 27:
//#line 137 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                }
break;
case 28:
//#line 140 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 35:
//#line 155 ".\gramatica.y"
{
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
break;
case 36:
//#line 158 ".\gramatica.y"
{
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
break;
case 37:
//#line 163 ".\gramatica.y"
{
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 38:
//#line 167 ".\gramatica.y"
{
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 39:
//#line 171 ".\gramatica.y"
{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 40:
//#line 176 ".\gramatica.y"
{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 41:
//#line 181 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 42:
//#line 186 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 43:
//#line 191 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 44:
//#line 198 ".\gramatica.y"
{
                        tablaSimbolos.checkAtributosDeClase(val_peek(1).sval,ambito);
                        if (val_peek(2).sval != null) {
                            tablaSimbolos.addAtributosClase(val_peek(2).sval,val_peek(1).sval,ambito);
                        }
                     }
break;
case 45:
//#line 206 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).add("RET");
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 46:
//#line 210 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).add("RET");
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 47:
//#line 214 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un (", anLex.getLinea()));
                        metodosPolaca.remove(ambito);
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 48:
//#line 219 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un )", anLex.getLinea()));
                        metodosPolaca.remove(ambito);
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 49:
//#line 224 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        metodosPolaca.remove(ambito);
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 50:
//#line 229 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        metodosPolaca.remove(ambito);
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 51:
//#line 236 ".\gramatica.y"
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
case 52:
//#line 252 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        } else {
                            /*cambiar funcion a con parametro ambito.subString(lastIndexOf(":"),ambito.length())*/
                            tablaSimbolos.setParametro(val_peek(0).sval,ambito);
                        }
                 }
break;
case 54:
//#line 263 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 55:
//#line 266 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
break;
case 56:
//#line 269 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 66:
//#line 290 ".\gramatica.y"
{ 
                        if (!ambito.equals(":main")) {
                            metodosPolaca.get(ambito).add("RET");
                        } else {
                            metodosPolaca.get(ambito).add("END MAIN");
                        }
                  }
break;
case 67:
//#line 297 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                  }
break;
case 68:
//#line 302 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).add(val_peek(1).sval);
                        metodosPolaca.get(ambito).add("PRINT");
                   }
break;
case 69:
//#line 306 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                   }
break;
case 70:
//#line 311 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).remove(metodosPolaca.get(ambito).size() - 2);
                        pila.pop();
                    }
break;
case 71:
//#line 315 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).add(pila.pop(),"[" + String.valueOf(metodosPolaca.get(ambito).size() + 1) + "]");
                        metodosPolaca.get(ambito).add("L" + "[" + String.valueOf(metodosPolaca.get(ambito).size()) + "]");
                    }
break;
case 72:
//#line 319 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 73:
//#line 322 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 74:
//#line 325 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
break;
case 75:
//#line 328 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
break;
case 76:
//#line 333 ".\gramatica.y"
{
                pila.push(metodosPolaca.get(ambito).size());
                metodosPolaca.get(ambito).add("BF"); /*bifurcacion por falso;*/
            }
break;
case 77:
//#line 337 ".\gramatica.y"
{
                errores.add(new Error("Falta declarar condicion del IF ubicado", anLex.getLinea()));
            }
break;
case 78:
//#line 341 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add(pila.pop(), "[" + String.valueOf(metodosPolaca.get(ambito).size() + 3) + "]");
                pila.push(metodosPolaca.get(ambito).size());
                metodosPolaca.get(ambito).add("BI"); /*bifurcacion incondicional;*/
                metodosPolaca.get(ambito).add("L[" + String.valueOf(metodosPolaca.get(ambito).size() + 1) + "]");
            }
break;
case 79:
//#line 347 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add(pila.pop(), "[" + String.valueOf(metodosPolaca.get(ambito).size() + 3) + "]");
                pila.push(metodosPolaca.get(ambito).size());
                metodosPolaca.get(ambito).add("BI"); /*bifurcacion incondicional;*/
                metodosPolaca.get(ambito).add("L[" + String.valueOf(metodosPolaca.get(ambito).size() + 1) + "]");
            }
break;
case 83:
//#line 360 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 84:
//#line 363 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
break;
case 85:
//#line 366 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 88:
//#line 375 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add(val_peek(1).sval);
            }
break;
case 89:
//#line 378 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 90:
//#line 381 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 91:
//#line 384 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 92:
//#line 389 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 93:
//#line 392 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 94:
//#line 395 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 95:
//#line 398 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 96:
//#line 401 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 97:
//#line 404 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 98:
//#line 407 ".\gramatica.y"
{
                errores.add(new Error("Comparacion mal definida", anLex.getLinea()));
           }
break;
case 99:
//#line 412 ".\gramatica.y"
{
                        metodosPolaca.get(ambito).add("[" + String.valueOf(pila.pop()) + "]");
                        metodosPolaca.get(ambito).add("BV");
                    }
break;
case 100:
//#line 416 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 101:
//#line 419 ".\gramatica.y"
{
                        errores.add(new Error("No se declaro una condicion de corte en el WHILE que se ubica", anLex.getLinea()));
                    }
break;
case 102:
//#line 424 ".\gramatica.y"
{
                pila.push(metodosPolaca.get(ambito).size());
                metodosPolaca.get(ambito).add("L[" + String.valueOf(metodosPolaca.get(ambito).size()) + "]");
             }
break;
case 106:
//#line 433 ".\gramatica.y"
{
                        if(!tablaSimbolos.existeMetodo(val_peek(3).sval,ambito,false)){
                                errores.add(new Error("No se encuentra declarado el metodo " + val_peek(3).sval + " dentro del ambito reconocible", anLex.getLinea()));
                        } else {
                            String ambitoMetodo = tablaSimbolos.getAmbitoMetodoInvocado(val_peek(3).sval,ambito);
                            if (val_peek(3).sval.contains(".")) {
                                    String clase = val_peek(3).sval.substring(0,val_peek(3).sval.indexOf(".") + 1);
                                    metodosPolaca.get(ambito).addAll(tablaSimbolos.getAtributosInstancia(clase, ambito));
                            }
                            metodosPolaca.get(ambito).add("Call " + ambitoMetodo);
                        }
                    }
break;
case 107:
//#line 445 ".\gramatica.y"
{ /* Chequear tipo operacion con parametro de funcion*/
                        if(!tablaSimbolos.existeMetodo(val_peek(4).sval,ambito,true)){
                                errores.add(new Error("No se encuentra declarado el metodo " + val_peek(4).sval + " dentro del ambito reconocible", anLex.getLinea()));
                        } else {
                            String operacion = metodosPolaca.get(ambito).get(metodosPolaca.get(ambito).size() - 1);
                            metodosPolaca.get(ambito).remove(metodosPolaca.get(ambito).size() - 1);
                            String ambitoMetodo = tablaSimbolos.getAmbitoMetodoInvocado(val_peek(4).sval,ambito);
                            if (val_peek(4).sval.contains(".")) {
                                    String clase = val_peek(4).sval.substring(0,val_peek(4).sval.indexOf(".") + 1);
                                    metodosPolaca.get(ambito).addAll(tablaSimbolos.getAtributosInstancia(clase, ambito));
                            }
                            metodosPolaca.get(ambito).add(operacion);
                            metodosPolaca.get(ambito).add(tablaSimbolos.getParametro(ambitoMetodo));
                            metodosPolaca.get(ambito).add("=");
                            metodosPolaca.get(ambito).add("Call " + ambitoMetodo);
                        }
                    }
break;
case 108:
//#line 462 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 109:
//#line 465 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 110:
//#line 470 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
              }
break;
case 111:
//#line 473 ".\gramatica.y"
{
                yyval.sval += "." + val_peek(0).sval;
              }
break;
case 112:
//#line 478 ".\gramatica.y"
{
                String direccionNombre = tablaSimbolos.existeVariable(val_peek(3).sval,ambito);
                if(direccionNombre.equals("")){
                        errores.add(new Error("No se declaro la variable " + val_peek(3).sval + " en el ambito reconocible", anLex.getLinea()));
                } else {
                        /*tablaSimbolos.agregarSimbolo($1.sval + ambito,new Token());*/
                        tablaSimbolos.setUso(val_peek(3).sval,ambito,true);
                        metodosPolaca.get(ambito).add(direccionNombre);metodosPolaca.get(ambito).add("=");
                }
           }
break;
case 113:
//#line 488 ".\gramatica.y"
{
                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
           }
break;
case 114:
//#line 491 ".\gramatica.y"
{
                errores.add(new Error("No se puede declarar y asignar en la misma línea", anLex.getLinea()));
           }
break;
case 115:
//#line 497 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
                yyval.sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval;
            }
break;
case 116:
//#line 503 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
                yyval.sval = val_peek(0).sval;
            }
break;
case 118:
//#line 512 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add("+");
          }
break;
case 119:
//#line 515 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add("-");
          }
break;
case 121:
//#line 521 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add("*");
        }
break;
case 122:
//#line 524 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add("/");
        }
break;
case 125:
//#line 531 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add("TOD");
       }
break;
case 126:
//#line 536 ".\gramatica.y"
{
                        String direccionNombre = tablaSimbolos.existeVariable(val_peek(2).sval,ambito);
                        if(direccionNombre.equals("")){
                                errores.add(new Error("No se declaro la variable " + val_peek(2).sval + " en el ambito reconocible", anLex.getLinea()));
                        } else {
                            metodosPolaca.get(ambito).add(direccionNombre);
                            String tipo = tablaSimbolos.getTipo(direccionNombre);
                            if (tipo.equals("UINT")) {
                                metodosPolaca.get(ambito).add("1_ui");
                            } else if (tipo.equals("LONG")) {
                                metodosPolaca.get(ambito).add("1_l");
                            } else {
                                metodosPolaca.get(ambito).add("1.0");
                            }
                            metodosPolaca.get(ambito).add("-");
                            metodosPolaca.get(ambito).add(direccionNombre);
                            metodosPolaca.get(ambito).add("=");
                        }
                 }
break;
case 127:
//#line 557 ".\gramatica.y"
{
                String direccionNombre = tablaSimbolos.existeVariable(val_peek(0).sval,ambito);
                if(direccionNombre.equals("")){
                        errores.add(new Error("No se declaro la variable " + val_peek(0).sval + " en el ambito reconocible", anLex.getLinea()));
                } else {
                    metodosPolaca.get(ambito).add(direccionNombre);
                }
             }
break;
case 128:
//#line 565 ".\gramatica.y"
{
                anLex.convertirNegativo(val_peek(0).sval);
                tablaSimbolos.eliminarSimbolo("-" + val_peek(0).sval);
                metodosPolaca.get(ambito).add("-" + val_peek(0).sval);
             }
break;
case 129:
//#line 570 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add(val_peek(0).sval);
             }
break;
case 130:
//#line 573 ".\gramatica.y"
{
                anLex.convertirNegativo(val_peek(0).sval);
                tablaSimbolos.eliminarSimbolo(val_peek(0).sval);
                metodosPolaca.get(ambito).add("-" + val_peek(0).sval);
             }
break;
case 131:
//#line 578 ".\gramatica.y"
{
                if(CheckRangoLong(val_peek(0).sval)){
                        errores.add(new Error("LONG fuera de rango", anLex.getLinea()));}
                else {
                        metodosPolaca.get(ambito).add(val_peek(0).sval);
                } 
             }
break;
case 132:
//#line 585 ".\gramatica.y"
{
                metodosPolaca.get(ambito).add(val_peek(0).sval);
             }
break;
case 133:
//#line 588 ".\gramatica.y"
{
                errores.add(new Error("Las constantes tipo UINT no pueden ser negativas", anLex.getLinea()));
             }
break;
case 134:
//#line 593 ".\gramatica.y"
{
        tipo = "DOUBLE";
        yyval.sval = null;
     }
break;
case 135:
//#line 597 ".\gramatica.y"
{
        tipo = "UINT";
        yyval.sval = null;
     }
break;
case 136:
//#line 601 ".\gramatica.y"
{
        tipo = "LONG";
        yyval.sval = null;
     }
break;
case 137:
//#line 605 ".\gramatica.y"
{
        if(!tablaSimbolos.existeClase(val_peek(0).sval)){
                errores.add(new Error("No se declaro la clase " + val_peek(0).sval + " en el ambito reconocible", anLex.getLinea()));
                yyval.sval = null;
        } else {
                yyval.sval = val_peek(0).sval;
        }
        tipo = val_peek(0).sval;
        tablaSimbolos.eliminarSimbolo(val_peek(0).sval);
     }
break;
//#line 1572 "Parser.java"
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
