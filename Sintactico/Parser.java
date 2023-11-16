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
   40,   40,   41,   41,   37,   42,   42,   42,   42,   42,
   42,   42,   18,   18,   18,   18,
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
    3,    3,    1,    1,    2,    1,    2,    1,    2,    1,
    1,    2,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  135,  134,  133,  100,    0,    0,
    0,    0,    0,    0,    0,    5,    6,    9,   10,    0,
    0,  101,    0,    0,   59,   60,   61,   62,   63,    0,
    0,  102,  103,    0,    0,    0,   17,   49,   18,   64,
   65,    0,    0,    3,    8,    7,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   76,
    0,   77,    0,    0,    0,    0,  125,   96,  110,  130,
  131,  128,   75,   90,   91,   92,   93,   94,   95,    0,
    0,    0,    0,  124,    0,    0,  120,  123,   66,   67,
    0,    1,    0,  136,   22,   32,    0,   29,   31,    0,
   23,    0,   11,   14,   27,   35,    0,   33,    0,   28,
    0,   13,   16,    0,    0,    0,   44,    0,    0,   82,
   84,    0,   83,    0,    0,    0,   72,    0,    0,    0,
    0,  111,    0,  129,  132,  127,   74,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   19,    0,   30,  116,
   21,   25,   36,   34,   42,    0,    0,   26,    0,    0,
    0,    0,   50,  115,    0,   80,   85,   81,   78,   79,
   68,   70,    0,   73,    0,  104,  105,    0,  112,  113,
    0,    0,    0,  121,  122,  108,  109,   12,   15,   24,
   20,    0,    0,   41,   43,   53,   58,   57,    0,   55,
   54,    0,   45,    0,  114,   69,   71,    0,    0,  106,
  107,   37,   39,    0,   51,   56,   52,   46,   99,    0,
   38,   40,   97,   98,
};
final static short yydgoto[] = {                         14,
   15,   16,  197,   18,   19,   20,   50,   21,   53,   97,
  148,  107,   98,   22,  108,   23,  115,   24,   56,  161,
  199,  200,   25,   26,   27,   28,   29,   30,   61,  128,
   81,   62,  122,   82,   83,   31,   32,   33,   34,   86,
   87,   88,
};
final static short yysindex[] = {                      -103,
   26, -233, -203, -178,    0,    0,    0,    0, -176,  -13,
   83,    0,  310,    0,  -71,    0,    0,    0,    0,   -2,
    7,    0,   95, -135,    0,    0,    0,    0,    0,   82,
    9,    0,    0,  -38,  -23,   18,    0,    0,    0,    0,
    0,   40,  -50,    0,    0,    0, -127,  -90,  157,   58,
 -118,  -37,   75,   37,    0,   81,  158,  231,  130,    0,
  -19,    0, -119,   63, -121,   40,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   48,
  122,   52,   40,    0,  -43,   99,    0,    0,    0,    0,
  400,    0,   13,    0,    0,    0,  -84,    0,    0,  -77,
    0,  161,    0,    0,    0,    0, -106,    0,  -35,    0,
  -27,    0,    0,   15,  162,  -66,    0,  -63,   40,    0,
    0,  247,    0,  146,   82,   84,    0,  -28,  176,  183,
  428,    0,  391,    0,    0,    0,    0,   40,   40,   40,
   20,   40,   40,  200,  284,  179,    0,  101,    0,    0,
    0,    0,    0,    0,    0,  167,  289,    0,  195,   98,
  190,   15,    0,    0,  477,    0,    0,    0,    0,    0,
    0,    0,  307,    0,   19,    0,    0,  323,    0,    0,
   99,   99,   20,    0,    0,    0,    0,    0,    0,    0,
    0,   -8,  212,    0,    0,    0,    0,    0,  215,    0,
    0,  114,    0,  192,    0,    0,    0,  216,  229,    0,
    0,    0,    0,  404,    0,    0,    0,    0,    0,  339,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -40,    0,    0,  276,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  286,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   72,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  249,    0,  -32,   27,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  293,
  300,    0,    0,    0,    0,   78,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   39,   47,  303,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  344,   14,  493,    0,  340,    0,  280,    0,    0,  326,
    0,  328,    3,  262,  -67,   59,  -36,  435,    0, -101,
  236, -143,    0,    0,    0,    0,    0,    0,    0,    0,
  224,    2,  345,  -16,  325,    0,  426,    0,   10,   38,
   41,    0,
};
final static int YYTABLESIZE=618;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        110,
   47,   64,   65,  110,  156,  110,  105,   65,  126,  126,
  126,  126,  126,  158,  126,  174,   48,   73,  152,   13,
  110,   80,   66,  155,  127,   91,  126,  126,   45,  126,
   40,  160,   63,   57,   95,  212,   74,   49,   75,  154,
  147,   47,   36,  154,   85,   41,   52,  131,   59,  133,
  213,   85,   49,   44,  160,  216,   45,   48,  216,  208,
  204,   89,  138,   80,  139,   35,  141,  117,   37,  117,
  117,  117,  157,   85,   92,   85,   90,  114,   74,  118,
   75,  118,  118,  118,   80,  117,  117,  119,  117,  119,
  119,  119,   85,   38,  138,   39,  139,  118,  118,  149,
  118,  103,  165,  130,  149,  119,  119,   80,  119,  109,
  109,   74,  204,   75,  159,  116,  104,  110,  112,  193,
   48,   59,   42,  183,  117,   47,  170,  171,   85,   51,
  116,   58,  110,  113,   54,   48,   55,  159,  201,  118,
  142,   48,  172,    4,   93,  143,  129,   85,   85,   85,
  132,   85,   85,    1,  217,    4,    2,    3,    4,    5,
    6,    7,  137,    8,    9,  109,   10,   11,   12,  109,
  123,    4,    5,    6,    7,  181,  182,    4,    5,    6,
    7,   94,  184,  185,   85,    1,  168,  146,    2,    3,
    4,    5,    6,    7,  150,    8,    9,  101,   10,   11,
   12,  151,  162,   65,   58,  163,    1,  192,  164,    2,
    3,    4,    5,    6,    7,  175,    8,    9,  119,   10,
   11,   12,  190,  126,    4,  191,  176,    5,    6,    7,
  173,  136,   68,  203,    4,  218,   94,   67,  125,  126,
  110,  177,   67,  186,  126,  126,  126,  126,   69,   70,
   71,   72,  214,   76,   77,   78,   79,   47,  187,  219,
   47,   47,   47,   47,   47,   47,   47,   47,   47,  220,
   47,   47,   47,   48,   68,    4,   48,   48,   48,   48,
   48,   48,  117,   48,   48,    2,   48,   48,   48,   89,
   69,   70,   71,   72,  118,   76,   77,   78,   79,    5,
    6,    7,  119,  117,  117,  117,  117,   68,   94,   99,
   99,   69,   70,   71,   72,  118,  118,  118,  118,  196,
  134,  135,  136,  119,  119,  119,  119,  188,   76,   77,
   78,   79,  194,   87,   69,   70,   71,   72,    1,  215,
   88,    2,  189,   86,    5,    6,    7,  195,    8,  136,
  206,   10,   11,   12,    1,  120,   43,    2,   99,    4,
    5,    6,    7,   99,    8,  207,  210,   10,   11,   12,
    1,  166,  145,    2,  102,    4,    5,    6,    7,  111,
    8,  211,  223,   10,   11,   12,    1,   96,   96,    2,
  106,  106,    5,    6,    7,  202,    8,  224,  209,   10,
   11,   12,    1,  124,    0,    2,  140,    0,    5,    6,
    7,    0,    8,    0,    0,   10,   11,   12,    4,    5,
    6,    7,    4,    5,    6,    7,    0,    0,   94,    5,
    6,    7,   94,  138,  179,  139,   96,    0,   94,    0,
  144,   96,  138,  160,  139,    0,  153,  221,    0,  180,
  153,    1,    0,    0,    2,    0,    4,    5,    6,    7,
   84,    8,  222,    0,   10,   11,   12,   84,  178,    0,
  138,    1,  139,    0,    2,    0,    4,    5,    6,    7,
    0,    8,  100,  100,   10,   11,   12,    1,  116,   84,
    2,   84,   17,    5,    6,    7,    0,    8,  198,  198,
   10,   11,   12,    1,    0,   17,    2,   46,   84,    5,
    6,    7,    0,    8,    0,    0,   10,   11,   12,  138,
  205,  139,   60,    0,    0,    0,  159,    0,    0,    0,
    0,  100,    0,    0,    0,   46,  100,    0,  198,    0,
    0,  198,    0,  116,   84,    0,    0,    0,    0,    0,
  121,  121,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   84,   84,   84,    1,   84,   84,    2,
    3,    4,    5,    6,    7,    0,    8,    9,    0,   10,
   11,   12,    0,    0,    0,    0,    0,    0,    0,    0,
  116,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   84,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  167,    0,  167,  169,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   46,   41,   40,   46,  125,   46,   41,   42,
   43,   44,   45,   41,   47,   44,    0,   41,  125,  123,
   61,   45,   61,   59,   44,   42,   59,   60,   15,   62,
   44,   40,   31,   24,  125,   44,   60,   40,   62,  107,
  125,   41,  276,  111,   35,   59,   40,   64,   40,   66,
   59,   42,   40,  125,   40,  199,   43,   41,  202,   41,
  162,   44,   43,   45,   45,   40,   83,   41,  272,   43,
   44,   45,  109,   64,  125,   66,   59,   41,   60,   41,
   62,   43,   44,   45,   45,   59,   60,   41,   62,   43,
   44,   45,   83,  272,   43,  272,   45,   59,   60,   97,
   62,   44,  119,   41,  102,   59,   60,   45,   62,   51,
   52,   60,  214,   62,  123,   44,   59,   46,   44,  156,
  123,   40,   40,  140,   44,  125,  125,   44,  119,  123,
   59,  123,   61,   59,   40,  123,  272,  123,   41,   59,
   42,  125,   59,  262,  272,   47,  266,  138,  139,  140,
  272,  142,  143,  257,   41,  262,  260,  261,  262,  263,
  264,  265,   41,  267,  268,  107,  270,  271,  272,  111,
   41,  262,  263,  264,  265,  138,  139,  262,  263,  264,
  265,  272,  142,  143,  175,  257,   41,  272,  260,  261,
  262,  263,  264,  265,  272,  267,  268,   41,  270,  271,
  272,   41,   41,   46,  123,  272,  257,   41,  272,  260,
  261,  262,  263,  264,  265,   40,  267,  268,   61,  270,
  271,  272,   44,  256,  262,  125,   44,  263,  264,  265,
  259,  272,  256,   44,  262,   44,  272,  281,  258,  259,
  281,   59,  281,   44,  277,  278,  279,  280,  272,  273,
  274,  275,   41,  277,  278,  279,  280,  257,   59,   44,
  260,  261,  262,  263,  264,  265,  269,  267,  268,   41,
  270,  271,  272,  257,  256,    0,  260,  261,  262,  263,
  264,  265,  256,  267,  268,    0,  270,  271,  272,   41,
  272,  273,  274,  275,  256,  277,  278,  279,  280,  263,
  264,  265,  256,  277,  278,  279,  280,  256,  272,   48,
   49,  272,  273,  274,  275,  277,  278,  279,  280,  125,
  273,  274,  275,  277,  278,  279,  280,   44,  277,  278,
  279,  280,   44,   41,  272,  273,  274,  275,  257,  125,
   41,  260,   59,   41,  263,  264,  265,   59,  267,  272,
   44,  270,  271,  272,  257,  125,   13,  260,   97,  262,
  263,  264,  265,  102,  267,   59,   44,  270,  271,  272,
  257,  125,   93,  260,   49,  262,  263,  264,  265,   52,
  267,   59,   44,  270,  271,  272,  257,   48,   49,  260,
   51,   52,  263,  264,  265,  160,  267,   59,  175,  270,
  271,  272,  257,   59,   -1,  260,   82,   -1,  263,  264,
  265,   -1,  267,   -1,   -1,  270,  271,  272,  262,  263,
  264,  265,  262,  263,  264,  265,   -1,   -1,  272,  263,
  264,  265,  272,   43,   44,   45,   97,   -1,  272,   -1,
   41,  102,   43,   40,   45,   -1,  107,   44,   -1,   59,
  111,  257,   -1,   -1,  260,   -1,  262,  263,  264,  265,
   35,  267,   59,   -1,  270,  271,  272,   42,   41,   -1,
   43,  257,   45,   -1,  260,   -1,  262,  263,  264,  265,
   -1,  267,   48,   49,  270,  271,  272,  257,   54,   64,
  260,   66,    0,  263,  264,  265,   -1,  267,  159,  160,
  270,  271,  272,  257,   -1,   13,  260,   15,   83,  263,
  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,   43,
   44,   45,   30,   -1,   -1,   -1,  123,   -1,   -1,   -1,
   -1,   97,   -1,   -1,   -1,   43,  102,   -1,  199,   -1,
   -1,  202,   -1,  109,  119,   -1,   -1,   -1,   -1,   -1,
   58,   59,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  138,  139,  140,  257,  142,  143,  260,
  261,  262,  263,  264,  265,   -1,  267,  268,   -1,  270,
  271,  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  156,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  175,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  122,   -1,  124,  125,
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

//#line 515 ".\gramatica.y"

static final int MAXNIVELHERENCIA = 3;
static final int MAXPROFUNDIDADVOID = 1;
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

        tablaSimbolos.imprimirTabla();
        for (String variable : tablaSimbolos.variablesNoUsadas()){
            errores.add(new Error("La variable " + variable + " no fue asignada dentro del ambito donde fue declarada","WARNING"));
        }
        if (errores.size()>0){
            for (Error error : errores) {
                System.out.println(error.toString());
            }
            //throw new Exception("Se han encontrado los errores anteriores");
        }

        
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
//#line 608 "Parser.java"
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
                                        errores.add(new Error("No esta declarada la clase '" + val_peek(1).sval.substring(0,val_peek(1).sval.lastIndexOf(":")), anLex.getLinea() + "'")); 
                                } else {
                                    if (tablaSimbolos.getNivelHerencia(val_peek(2).sval) >= MAXNIVELHERENCIA ) {
                                        errores.add(new Error("La clase '" + val_peek(2).sval.substring(0,val_peek(2).sval.indexOf(":")) + "' no puede heredar, debido a que la clase padre '" + tablaSimbolos.getPadreClase(tablaSimbolos.getPadreClase(val_peek(2).sval)).substring(0,val_peek(2).sval.indexOf(":")) + "' ya posee el maximo nivel de herencia permitido (" + MAXNIVELHERENCIA + ")", "ERROR"));
                                    }
                                }
                        }
                  }
break;
case 12:
//#line 51 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if (!tablaSimbolos.agregarInterfaz(val_peek(4).sval,val_peek(2).sval + ":main")){
                            errores.add(new Error("No se encuentra la interfaz " + val_peek(2).sval, anLex.getLinea())); 
                        } else {
                            if (!tablaSimbolos.implementaMetodosInterfaz(ambito + ":" + val_peek(4).sval.substring(0,val_peek(4).sval.lastIndexOf(":")),ambito + ":" + val_peek(2).sval)){
                                errores.add(new Error("La clase '" + val_peek(4).sval.substring(0,val_peek(4).sval.indexOf(":")) + "' no implementa todos los metodos de la interfaz '" + val_peek(2).sval + "'", anLex.getLinea()));
                            }
                        }
                        if((val_peek(1).sval != null)){
                                if((!tablaSimbolos.agregarHerencia(val_peek(4).sval,val_peek(1).sval))){
                                        errores.add(new Error("No esta declarada la clase '" + val_peek(1).sval.substring(0,val_peek(1).sval.lastIndexOf(":")), anLex.getLinea() + "'")); 
                                }
                        }
                  }
break;
case 13:
//#line 66 ".\gramatica.y"
{
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 14:
//#line 69 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 15:
//#line 73 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 16:
//#line 77 ".\gramatica.y"
{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
break;
case 17:
//#line 83 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        yyval.sval = val_peek(0).sval + ambito;
                        ambito += ":" + val_peek(0).sval;
                 }
break;
case 18:
//#line 92 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        ambito += ":" + val_peek(0).sval;
                     }
break;
case 19:
//#line 100 ".\gramatica.y"
{
                yyval.sval = null;
             }
break;
case 20:
//#line 103 ".\gramatica.y"
{
                yyval.sval = val_peek(1).sval + ":main";
             }
break;
case 21:
//#line 106 ".\gramatica.y"
{
                errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
             }
break;
case 22:
//#line 109 ".\gramatica.y"
{
                errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
             }
break;
case 23:
//#line 112 ".\gramatica.y"
{
                errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
             }
break;
case 26:
//#line 120 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 27:
//#line 123 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                }
break;
case 28:
//#line 126 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 35:
//#line 141 ".\gramatica.y"
{
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
break;
case 36:
//#line 144 ".\gramatica.y"
{
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
break;
case 37:
//#line 149 ".\gramatica.y"
{
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 38:
//#line 152 ".\gramatica.y"
{
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 39:
//#line 155 ".\gramatica.y"
{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 40:
//#line 159 ".\gramatica.y"
{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 41:
//#line 163 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 42:
//#line 167 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 43:
//#line 171 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 45:
//#line 180 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 46:
//#line 183 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 47:
//#line 186 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 48:
//#line 190 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 49:
//#line 196 ".\gramatica.y"
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
                   }
break;
case 50:
//#line 210 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                 }
break;
case 52:
//#line 218 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 53:
//#line 221 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
break;
case 54:
//#line 224 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 65:
//#line 246 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                  }
break;
case 67:
//#line 252 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                   }
break;
case 68:
//#line 257 ".\gramatica.y"
{
                        polaca.add(pila.pop(),"[" + String.valueOf(polaca.size() + 1) + "]");
                    }
break;
case 69:
//#line 260 ".\gramatica.y"
{
                        polaca.add(pila.pop(),"[" + String.valueOf(polaca.size() + 1) + "]");
                    }
break;
case 70:
//#line 263 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 71:
//#line 266 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 72:
//#line 269 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
break;
case 73:
//#line 272 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
break;
case 74:
//#line 277 ".\gramatica.y"
{
                pila.push(polaca.size());
                polaca.add("BF"); /*bifurcacion por falso;*/
            }
break;
case 75:
//#line 281 ".\gramatica.y"
{
                errores.add(new Error("Falta declarar condicion del IF ubicado", anLex.getLinea()));
            }
break;
case 76:
//#line 285 ".\gramatica.y"
{
                polaca.add(pila.pop(), "[" + String.valueOf(polaca.size() + 3) + "]");
                pila.push(polaca.size());
                polaca.add("BI"); /*bifurcacion incondicional;*/
            }
break;
case 77:
//#line 290 ".\gramatica.y"
{
                polaca.add(pila.pop(), "[" + String.valueOf(polaca.size() + 3) + "]");
                pila.push(polaca.size());
                polaca.add("BI"); /*bifurcacion incondicional;*/
            }
break;
case 81:
//#line 302 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 82:
//#line 305 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
break;
case 83:
//#line 308 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 86:
//#line 317 ".\gramatica.y"
{
                polaca.add(val_peek(1).sval);
            }
break;
case 87:
//#line 320 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 88:
//#line 323 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 89:
//#line 326 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 90:
//#line 331 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 91:
//#line 334 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 92:
//#line 337 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 93:
//#line 340 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 94:
//#line 343 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 95:
//#line 346 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 96:
//#line 349 ".\gramatica.y"
{
                errores.add(new Error("Comparacion mal definida", anLex.getLinea()));
           }
break;
case 97:
//#line 354 ".\gramatica.y"
{
                        polaca.add("[" + String.valueOf(pila.pop()) + "]");
                        polaca.add("BF");
                    }
break;
case 98:
//#line 358 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 99:
//#line 361 ".\gramatica.y"
{
                        errores.add(new Error("No se declaro una condicion de corte en el WHILE que se ubica", anLex.getLinea()));
                    }
break;
case 100:
//#line 366 ".\gramatica.y"
{
                pila.push(polaca.size());
             }
break;
case 104:
//#line 374 ".\gramatica.y"
{
                        if(!tablaSimbolos.existeMetodo(val_peek(3).sval,ambito)){
                                errores.add(new Error("No se declaro el Metodo " + val_peek(3).sval + " en el ambito reconocible", anLex.getLinea()));
                        }
                        polaca.add("Call a " + val_peek(3).sval);
                    }
break;
case 105:
//#line 380 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 107:
//#line 384 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 108:
//#line 387 ".\gramatica.y"
{
                        
                    }
break;
case 109:
//#line 390 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 110:
//#line 395 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
              }
break;
case 111:
//#line 398 ".\gramatica.y"
{
                yyval.sval += "." + val_peek(0).sval;
              }
break;
case 112:
//#line 403 ".\gramatica.y"
{
                if(!tablaSimbolos.existeVariable(val_peek(3).sval,ambito)){
                        errores.add(new Error("No se declaro la variable " + val_peek(3).sval + " en el ambito reconocible", anLex.getLinea()));
                } else {
                        tablaSimbolos.setUso(val_peek(3).sval,ambito,true);
                }
                polaca.add(val_peek(3).sval);polaca.add("=");
           }
break;
case 113:
//#line 411 ".\gramatica.y"
{
                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
           }
break;
case 114:
//#line 414 ".\gramatica.y"
{
                errores.add(new Error("No se puede declarar y asignar en la misma línea", anLex.getLinea()));
           }
break;
case 115:
//#line 420 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
            }
break;
case 116:
//#line 425 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
            }
break;
case 118:
//#line 433 ".\gramatica.y"
{
                polaca.add("+");
          }
break;
case 119:
//#line 436 ".\gramatica.y"
{
                polaca.add("-");
          }
break;
case 121:
//#line 442 ".\gramatica.y"
{
                polaca.add("*");
        }
break;
case 122:
//#line 445 ".\gramatica.y"
{
                polaca.add("/");
        }
break;
case 125:
//#line 454 ".\gramatica.y"
{
                        if(!tablaSimbolos.existeVariable(val_peek(1).sval,ambito)){
                                errores.add(new Error("No se declaro la variable " + val_peek(1).sval + " en el ambito reconocible", anLex.getLinea()));
                        }
                        polaca.add(val_peek(1).sval);polaca.add("1");polaca.add("-");{polaca.add(val_peek(1).sval);polaca.add("=");}
                 }
break;
case 126:
//#line 462 ".\gramatica.y"
{
                if(!tablaSimbolos.existeVariable(val_peek(0).sval,ambito)){
                        errores.add(new Error("No se declaro la variable " + val_peek(0).sval + " en el ambito reconocible", anLex.getLinea()));
                }
                polaca.add(val_peek(0).sval);
             }
break;
case 127:
//#line 468 ".\gramatica.y"
{
                anLex.convertirNegativo(val_peek(0).sval);
                tablaSimbolos.eliminarSimbolo("-" + val_peek(0).sval);
                polaca.add("-" + val_peek(0).sval);
             }
break;
case 128:
//#line 473 ".\gramatica.y"
{
                polaca.add(val_peek(0).sval);
             }
break;
case 129:
//#line 476 ".\gramatica.y"
{
                anLex.convertirNegativo(val_peek(0).sval);
                tablaSimbolos.eliminarSimbolo(val_peek(0).sval);
                polaca.add("-" + val_peek(0).sval);
             }
break;
case 130:
//#line 481 ".\gramatica.y"
{
                if(CheckRangoLong(val_peek(0).sval)){
                        errores.add(new Error("LONG fuera de rango", anLex.getLinea()));}
                else {
                        polaca.add(val_peek(0).sval);
                } 
             }
break;
case 131:
//#line 488 ".\gramatica.y"
{
                polaca.add(val_peek(0).sval);
             }
break;
case 132:
//#line 491 ".\gramatica.y"
{
                errores.add(new Error("Las constantes tipo UINT no pueden ser negativas", anLex.getLinea()));
             }
break;
case 133:
//#line 496 ".\gramatica.y"
{
        tipo = "DOUBLE";
     }
break;
case 134:
//#line 499 ".\gramatica.y"
{
        tipo = "UINT";
     }
break;
case 135:
//#line 502 ".\gramatica.y"
{
        tipo = "LONG";
     }
break;
case 136:
//#line 505 ".\gramatica.y"
{
        if(!tablaSimbolos.existeClase(val_peek(0).sval,ambito)){
                errores.add(new Error("No se declaro la variable " + val_peek(0).sval + " en el ambito reconocible", anLex.getLinea()));
        }
        tipo = val_peek(0).sval;
        tablaSimbolos.eliminarSimbolo(val_peek(0).sval);
     }
break;
//#line 1412 "Parser.java"
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
