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
import Lexico.Error;
import java.util.ArrayList;

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
    4,    4,    4,    4,    4,    6,    8,    7,    7,    7,
    7,    7,    9,    9,    9,    9,   10,   10,   12,   12,
   11,   11,   11,   11,   13,   13,   13,   13,   13,   13,
   13,   13,    3,    3,    5,    5,    5,    5,   14,   15,
   18,   18,   18,   18,   19,   19,   20,   20,   21,   21,
   21,   21,   21,   26,   26,   25,   25,   25,   25,   25,
   23,   23,   23,   23,   23,   23,   23,   23,   28,   28,
   29,   29,   29,   29,   30,   30,   27,   27,   27,   27,
   32,   32,   32,   32,   32,   32,   32,   24,   24,   24,
   24,   22,   22,   22,   22,   22,   22,   22,   22,   22,
   35,   35,   34,   34,   34,   17,   17,   31,   31,   31,
   36,   36,   36,   36,   37,   37,   33,   38,   38,   38,
   38,   38,   38,   38,   16,   16,   16,   16,   16,
};
final static short yylen[] = {                            2,
    3,    2,    2,    1,    1,    2,    1,    1,    1,    3,
    5,    3,    2,    4,    2,    2,    2,    3,    5,    3,
    2,    2,    3,    3,    2,    2,    1,    2,    1,    1,
    1,    2,    1,    2,    4,    5,    3,    4,    2,    3,
    1,    2,    3,    2,    5,    6,    4,    5,    2,    2,
    3,    3,    2,    2,    1,    2,    1,    1,    1,    1,
    1,    1,    1,    2,    1,    3,    2,    3,    2,    3,
    7,    9,    6,    8,    6,    8,    5,    7,    1,    1,
    3,    3,    2,    2,    1,    2,    3,    2,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    7,    6,    6,
    5,    1,    1,    1,    4,    3,    5,    4,    5,    4,
    1,    3,    4,    3,    5,    1,    3,    1,    3,    3,
    1,    3,    3,    3,    1,    1,    2,    1,    2,    1,
    2,    1,    1,    2,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
  139,    0,    0,  137,  136,  135,    0,  138,    0,    0,
    0,    5,    7,    8,    9,    0,    0,    0,    0,   16,
   49,   17,    0,    3,    6,    0,    0,    0,    0,    0,
    0,    0,    0,  116,    0,    1,    0,   21,   29,   30,
    0,   27,   22,    0,   10,   25,   33,    0,   31,    0,
   26,    0,   12,    0,    0,    0,   43,    0,    0,    0,
   18,   28,   20,   23,   34,   32,   39,    0,    0,   24,
    0,    0,    0,    0,   50,  117,   11,    0,    0,    0,
   40,    0,    0,    0,    0,    0,    0,    0,   53,  102,
   57,    0,    0,   55,   58,   59,   60,   61,   62,   63,
  103,  104,    0,   54,    0,   45,    0,   19,   35,    0,
    0,    0,    0,    0,    0,    0,    0,   64,    0,    0,
    0,   51,   56,    0,    0,    0,  127,   52,   46,   36,
   70,   97,  111,  132,  133,  130,    0,    0,   91,   92,
   93,   94,   95,   96,    0,    0,    0,    0,  126,    0,
    0,  121,  125,   68,   66,   83,   85,    0,   84,    0,
    0,    0,    0,    0,    0,  112,    0,    0,   79,    0,
   80,  131,  134,  129,    0,    0,    0,    0,    0,    0,
    0,   81,   86,   82,    0,    0,    0,  105,    0,  113,
  124,    0,    0,    0,    0,    0,    0,  122,  123,    0,
    0,  109,  115,  107,    0,   75,    0,    0,  100,    0,
    0,    0,   71,   98,   76,    0,   72,
};
final static short yydgoto[] = {                         10,
   11,   12,   90,   14,   15,   16,   29,   17,   32,   41,
   48,   42,   49,   18,   55,   92,   35,   73,   93,   94,
   95,   96,   97,   98,   99,  100,  146,  170,  171,  158,
  147,  148,  149,  102,  150,  151,  152,  153,
};
final static short yysindex[] = {                       666,
    0, -235, -233,    0,    0,    0, -201,    0,  837,    0,
  403,    0,    0,    0,    0,  -29,  -33,   51, -171,    0,
    0,    0,  734,    0,    0, -168,  -95,  280,   65, -122,
  -31,   94,  413,    0,   24,    0,  -21,    0,    0,    0,
  749,    0,    0,  373,    0,    0,    0, -120,    0,  141,
    0,    8,    0,  -15,   81, -133,    0, -131,  100,  106,
    0,    0,    0,    0,    0,    0,    0,  643,  118,    0,
  356,  598,  119,  -15,    0,    0,    0,   40,  -20,  130,
    0,  -94,  143, -225,  -11,  140,  147,    0,    0,    0,
    0,  -84,  686,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -38,    0,  616,    0,  145,    0,    0,  -18,
  146,  -28,  148,  150,  704,  633,  -71,    0,  173,    0,
   38,    0,    0,   84,  -76,  173,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  173,  247,    0,    0,
    0,    0,    0,    0, -121,  156,  164,  173,    0,  -42,
   39,    0,    0,    0,    0,    0,    0,  722,    0,  653,
  158,   44,  173,  157,   78,    0,  112,   91,    0, -180,
    0,    0,    0,    0,  247,  173,  173,  173,   88,  160,
  160,    0,    0,    0,   75,  162,  135,    0,  166,    0,
    0,  247,  167, -145,   39,   39,   88,    0,    0,  168,
  174,    0,    0,    0,  -51,    0,  247,  170,    0,  172,
  176,  -37,    0,    0,    0,  179,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  217,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  225,    0,    0,    0,    0,    0,   48,    0,
    0,   61,    0,    0,    1,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -32,
    0,    0,    0,    0,    0,    0,    0,    0,   74,  -45,
    0,    0,    0,    0,    0,    0,    0,    0,  -27,    0,
    0,    0,   18,    0,    0,    0,    0,    0,  -14,    0,
    0,  -39,    0,    0,    0,  226,    0,  -40,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   35,    0,    0,   -8,
    0,    0,  267,  303,    0,    0,    0,    0,    0,  433,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  201,    0,  104,
  131,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  336,    0,    0,  384,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  235,  236,    0,
    0,    0,    0,    0,    0,  454,    0,    0,  472,    0,
    0,    0,  492,    0,  159,  193,  252,    0,    0,  509,
    0,    0,    0,    0,    0,    0,    0,  528,    0,  545,
  563,    0,    0,    0,    0,  581,    0,
};
final static short yygindex[] = {                         0,
  285,   59, 1050,    0,   25,    0,  264,    0,    0,  286,
  284,    6,   58,   15,   -6, 1008,    0,  -46,  255,   -5,
 -100,    0,    0,    0,    0,    0,  134, -135,  243,  216,
  -83,  187,  929,    0,  932,  -49,  -22,    0,
};
final static int YYTABLESIZE=1139;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        111,
   44,  124,   46,  125,   64,  111,   31,  125,   41,   51,
   28,  137,  138,   42,  157,  157,  145,   47,   28,   72,
  111,   72,  126,  109,   72,  130,   37,  107,  116,   38,
  113,  139,   38,  140,   48,  162,   20,  169,   21,  194,
  165,   44,  167,   69,   50,   50,   62,   13,   70,   62,
  114,   40,   40,  168,   47,   47,  205,  183,   47,  183,
   15,   80,   50,  107,  179,   40,   50,   57,   40,   25,
   22,  212,   65,   14,  169,   48,   65,  192,  193,  187,
  180,   25,   58,  125,  186,  181,  176,  123,  177,   30,
   33,  169,   41,   27,  197,   91,   91,   42,  163,  123,
   34,   27,   71,   37,   71,   66,  169,   71,   45,   66,
   37,  115,  207,  208,  137,  200,   38,   91,  189,  145,
  176,   74,  177,  137,  164,   44,  195,  196,  145,   91,
  176,  191,  177,  176,  139,  177,  140,   53,   75,    3,
   76,    3,   47,   77,  128,  128,  128,  128,  128,   78,
  128,  172,  173,  174,  176,  190,  177,  198,  199,   48,
    1,   81,  106,  128,  108,  128,    3,    4,    5,    6,
  110,  118,   13,  118,  118,  118,    8,  176,  203,  177,
   68,  111,  112,  118,   67,   15,  119,  120,  129,  131,
  118,  154,  118,  155,  161,  166,  175,  185,   14,  119,
  188,  119,  119,  119,  145,  202,  176,  211,  177,  204,
  206,  209,  137,  213,  210,  214,    4,  145,  119,  215,
  119,  216,  217,  139,    2,  140,  138,  132,  128,   41,
    3,  138,  139,  120,   42,  120,  120,  120,  127,   26,
  111,   90,  127,  133,  134,  135,  136,   37,  141,  142,
  143,  144,  120,   38,  120,  118,   44,   44,   44,   44,
   44,   44,   44,   44,   44,   44,   65,   44,   44,    3,
   44,   44,   44,   47,   47,   88,   89,   47,   47,   47,
   47,   47,   47,  119,   47,   47,  116,   47,   47,   47,
   48,   48,   87,   23,   48,   48,   48,   48,   48,   48,
   59,   48,   48,   13,   48,   48,   48,   69,   13,   13,
   13,   13,   13,   44,   52,   13,   15,  120,  201,   13,
   43,   15,   15,   15,   15,   15,  105,  117,   15,   14,
  132,  160,   15,  178,   14,   14,   14,   14,   14,    0,
    0,   14,    0,   67,    0,   14,  133,  134,  135,  136,
   65,  141,  142,  143,  144,  133,  134,  135,  136,  128,
  128,  128,  128,  128,    0,  128,  128,  128,  128,  115,
  128,    0,    0,  128,  128,  128,  106,    0,    0,    0,
  128,  128,  128,  128,    0,    0,  118,  118,  118,  118,
  118,   69,  118,  118,  118,  118,    1,  118,    0,    0,
  118,  118,  118,    4,    5,    6,    0,  118,  118,  118,
  118,    0,    8,   63,  119,  119,  119,  119,  119,  132,
  119,  119,  119,  119,  114,  119,    0,   67,  119,  119,
  119,  133,  134,  135,  136,  119,  119,  119,  119,    0,
  141,  142,  143,  144,  133,  134,  135,  136,  120,  120,
  120,  120,  120,   54,  120,  120,  120,  120,    0,  120,
  106,    0,  120,  120,  120,    0,    0,    0,    0,  120,
  120,  120,  120,  116,    0,    0,  116,    0,  111,    0,
   89,   65,   65,   65,   65,   65,    0,   65,   65,   65,
   65,  116,   65,  111,  110,   65,   65,   65,    0,    0,
    0,    0,   82,   83,    0,    0,   84,    0,  114,    4,
    5,    6,  108,   85,    0,    0,   86,   87,   88,    0,
    0,    0,   69,   69,   69,   69,   69,   24,   69,   69,
   69,   69,   77,   69,    0,    1,   69,   69,   69,    0,
    0,    3,    4,    5,    6,    0,    0,    0,    0,  101,
    0,    8,    0,    0,    0,    0,    0,  116,   67,   67,
   67,   67,   67,    0,   67,   67,   67,   67,   73,   67,
    0,    0,   67,   67,   67,    0,    0,    0,  110,    0,
    0,    0,    0,    0,    0,   99,    0,    0,    0,    0,
    0,  106,  106,  106,  106,  106,  108,  106,  106,  106,
  106,    0,  106,   78,    0,  106,  106,  106,    0,    0,
    0,   82,   83,    0,    0,   84,   77,    3,    4,    5,
    6,   74,   85,    0,    0,   86,   87,   88,    1,    0,
    0,    0,    0,  101,    3,    4,    5,    6,  104,  114,
  114,  114,  114,  114,    8,  114,  114,  114,  114,    0,
  114,    0,   73,  114,  114,  114,  128,    0,    1,    0,
    0,    0,    0,    2,    3,    4,    5,    6,    1,   99,
    7,    0,    0,  159,    8,    4,    5,    6,    0,    0,
    0,    0,    0,   79,    8,    0,    0,   78,  116,  116,
  116,  116,  116,  184,  116,  116,  116,  116,    0,  116,
    0,    0,  116,  116,  116,   74,    0,    0,    0,  110,
  110,  110,  110,  110,    0,  110,  110,  110,  110,    0,
  110,    0,    0,  110,  110,  110,    0,  108,  108,  108,
  108,  108,    0,  108,  108,  108,  108,    0,  108,    0,
    0,  108,  108,  108,    0,    0,    0,   77,   77,   77,
   77,   77,    0,   77,   77,   77,   77,    0,   77,    0,
    0,   77,   77,   77,  101,  101,  101,  101,  101,    0,
  101,  101,  101,  101,    0,  101,    0,    0,  101,  101,
  101,    0,    0,   73,   73,   73,   73,   73,    9,   73,
   73,   73,   73,    0,   73,    0,    0,   73,   73,   73,
   99,   99,   99,   99,   99,    0,   99,   99,   99,   99,
  122,   99,    0,    0,   99,   99,   99,    0,   78,   78,
   78,   78,   78,    0,   78,   78,   78,   78,  156,   78,
    0,    0,   78,   78,   78,    0,   74,   74,   74,   74,
   74,    0,   74,   74,   74,   74,  182,   74,    0,    0,
   74,   74,   74,   82,   83,    0,    0,   84,   36,    3,
    4,    5,    6,    0,   85,    0,    0,   86,   87,   88,
    0,   82,   83,   61,    0,   84,    0,    3,    4,    5,
    6,    0,   85,    0,    0,   86,   87,   88,   82,   83,
    0,    0,   84,    0,    0,    4,    5,    6,    1,   85,
    0,    0,   86,   87,   88,    4,    5,    6,   82,   83,
    0,    0,   84,    0,    8,    4,    5,    6,    0,   85,
    0,    1,   86,   87,   88,    0,    2,    3,    4,    5,
    6,    0,    0,    7,    0,    0,    0,    8,    0,    0,
    0,   82,   83,    0,    0,   84,    0,    3,    4,    5,
    6,    0,   85,    0,    0,   86,   87,   88,    0,   82,
   83,    0,    0,   84,    0,    0,    4,    5,    6,    0,
   85,    0,    0,   86,   87,   88,    0,   82,   83,    0,
    0,   84,    0,    0,    4,    5,    6,    0,   85,    1,
    0,   86,   87,   88,    2,    3,    4,    5,    6,  101,
  101,    7,  103,  103,    1,    8,    0,   19,    0,    0,
    3,    4,    5,    6,    0,    0,   19,    0,   19,    0,
   60,  101,    0,  121,  103,    0,    0,    0,    0,    0,
   19,    0,    0,  101,   19,   19,  103,    0,    0,    0,
   56,    0,    0,  101,  101,    0,  103,  103,   19,   13,
    0,   19,    0,    0,    0,    0,    0,   56,   13,    0,
   13,    0,    0,    0,    0,    0,  101,    0,    0,  103,
    0,    0,   13,    0,    0,   56,   39,   39,    0,    0,
    0,    0,    0,    0,    0,    0,  101,    0,  101,  103,
   39,  103,    1,   39,    0,    0,    0,    2,    3,    4,
    5,    6,    0,  101,    7,    0,  103,    0,    8,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  101,    0,    0,  103,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  101,    0,    0,  103,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,  125,   46,  125,   46,   40,   46,   41,   41,
   40,   40,   41,   41,  115,  116,   45,    0,   40,   40,
   61,   40,   61,   44,   40,   44,   41,   74,   40,  125,
  256,   60,   41,   62,    0,  119,  272,  138,  272,  175,
  124,   41,  126,   50,   30,   31,   41,    0,   41,   44,
  276,   27,   28,  137,   30,   31,  192,  158,   41,  160,
    0,   68,   48,  110,  148,   41,   52,   44,   44,   11,
  272,  207,   48,    0,  175,   41,   52,  258,  259,  163,
   42,   23,   59,   46,   41,   47,   43,   93,   45,  123,
   40,  192,  125,  123,  178,   71,   72,  125,   61,  105,
  272,  123,  123,  272,  123,   48,  207,  123,   44,   52,
  125,  123,  258,  259,   40,   41,  125,   93,   41,   45,
   43,   41,   45,   40,   41,  125,  176,  177,   45,  105,
   43,   41,   45,   43,   60,   45,   62,   44,  272,  262,
  272,  262,  125,   44,   41,   42,   43,   44,   45,   44,
   47,  273,  274,  275,   43,   44,   45,  180,  181,  125,
  256,   44,   44,   60,  125,   62,  262,  263,  264,  265,
   41,   41,  125,   43,   44,   45,  272,   43,   44,   45,
   40,  276,   40,   44,   44,  125,   40,  272,   44,   44,
   60,   44,   62,   44,  266,  272,   41,   40,  125,   41,
   44,   43,   44,   45,   45,   44,   43,  259,   45,   44,
   44,   44,   40,   44,   41,   44,    0,   45,   60,   44,
   62,  259,   44,   60,    0,   62,  272,  256,  125,  262,
  262,  272,  272,   41,  262,   43,   44,   45,  281,  269,
  281,   41,  281,  272,  273,  274,  275,  262,  277,  278,
  279,  280,   60,  262,   62,  125,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,   41,  267,  268,  262,
  270,  271,  272,  256,  257,   41,   41,  260,  261,  262,
  263,  264,  265,  125,  267,  268,   40,  270,  271,  272,
  256,  257,   41,    9,  260,  261,  262,  263,  264,  265,
   37,  267,  268,  256,  270,  271,  272,   41,  261,  262,
  263,  264,  265,   28,   31,  268,  256,  125,  185,  272,
   41,  261,  262,  263,  264,  265,   72,   85,  268,  256,
  256,  116,  272,  147,  261,  262,  263,  264,  265,   -1,
   -1,  268,   -1,   41,   -1,  272,  272,  273,  274,  275,
  125,  277,  278,  279,  280,  272,  273,  274,  275,  256,
  257,  258,  259,  260,   -1,  262,  263,  264,  265,  123,
  267,   -1,   -1,  270,  271,  272,   41,   -1,   -1,   -1,
  277,  278,  279,  280,   -1,   -1,  256,  257,  258,  259,
  260,  125,  262,  263,  264,  265,  256,  267,   -1,   -1,
  270,  271,  272,  263,  264,  265,   -1,  277,  278,  279,
  280,   -1,  272,   41,  256,  257,  258,  259,  260,  256,
  262,  263,  264,  265,   41,  267,   -1,  125,  270,  271,
  272,  272,  273,  274,  275,  277,  278,  279,  280,   -1,
  277,  278,  279,  280,  272,  273,  274,  275,  256,  257,
  258,  259,  260,   41,  262,  263,  264,  265,   -1,  267,
  125,   -1,  270,  271,  272,   -1,   -1,   -1,   -1,  277,
  278,  279,  280,   41,   -1,   -1,   44,   -1,   46,   -1,
  125,  256,  257,  258,  259,  260,   -1,  262,  263,  264,
  265,   59,  267,   61,   41,  270,  271,  272,   -1,   -1,
   -1,   -1,  256,  257,   -1,   -1,  260,   -1,  125,  263,
  264,  265,   41,  267,   -1,   -1,  270,  271,  272,   -1,
   -1,   -1,  256,  257,  258,  259,  260,  125,  262,  263,
  264,  265,   41,  267,   -1,  256,  270,  271,  272,   -1,
   -1,  262,  263,  264,  265,   -1,   -1,   -1,   -1,   41,
   -1,  272,   -1,   -1,   -1,   -1,   -1,  125,  256,  257,
  258,  259,  260,   -1,  262,  263,  264,  265,   41,  267,
   -1,   -1,  270,  271,  272,   -1,   -1,   -1,  125,   -1,
   -1,   -1,   -1,   -1,   -1,   41,   -1,   -1,   -1,   -1,
   -1,  256,  257,  258,  259,  260,  125,  262,  263,  264,
  265,   -1,  267,   41,   -1,  270,  271,  272,   -1,   -1,
   -1,  256,  257,   -1,   -1,  260,  125,  262,  263,  264,
  265,   41,  267,   -1,   -1,  270,  271,  272,  256,   -1,
   -1,   -1,   -1,  125,  262,  263,  264,  265,   41,  256,
  257,  258,  259,  260,  272,  262,  263,  264,  265,   -1,
  267,   -1,  125,  270,  271,  272,   41,   -1,  256,   -1,
   -1,   -1,   -1,  261,  262,  263,  264,  265,  256,  125,
  268,   -1,   -1,   41,  272,  263,  264,  265,   -1,   -1,
   -1,   -1,   -1,   41,  272,   -1,   -1,  125,  256,  257,
  258,  259,  260,   41,  262,  263,  264,  265,   -1,  267,
   -1,   -1,  270,  271,  272,  125,   -1,   -1,   -1,  256,
  257,  258,  259,  260,   -1,  262,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,   -1,  256,  257,  258,
  259,  260,   -1,  262,  263,  264,  265,   -1,  267,   -1,
   -1,  270,  271,  272,   -1,   -1,   -1,  256,  257,  258,
  259,  260,   -1,  262,  263,  264,  265,   -1,  267,   -1,
   -1,  270,  271,  272,  256,  257,  258,  259,  260,   -1,
  262,  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,
  272,   -1,   -1,  256,  257,  258,  259,  260,  123,  262,
  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,
  256,  257,  258,  259,  260,   -1,  262,  263,  264,  265,
  125,  267,   -1,   -1,  270,  271,  272,   -1,  256,  257,
  258,  259,  260,   -1,  262,  263,  264,  265,  125,  267,
   -1,   -1,  270,  271,  272,   -1,  256,  257,  258,  259,
  260,   -1,  262,  263,  264,  265,  125,  267,   -1,   -1,
  270,  271,  272,  256,  257,   -1,   -1,  260,  125,  262,
  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,
   -1,  256,  257,  125,   -1,  260,   -1,  262,  263,  264,
  265,   -1,  267,   -1,   -1,  270,  271,  272,  256,  257,
   -1,   -1,  260,   -1,   -1,  263,  264,  265,  256,  267,
   -1,   -1,  270,  271,  272,  263,  264,  265,  256,  257,
   -1,   -1,  260,   -1,  272,  263,  264,  265,   -1,  267,
   -1,  256,  270,  271,  272,   -1,  261,  262,  263,  264,
  265,   -1,   -1,  268,   -1,   -1,   -1,  272,   -1,   -1,
   -1,  256,  257,   -1,   -1,  260,   -1,  262,  263,  264,
  265,   -1,  267,   -1,   -1,  270,  271,  272,   -1,  256,
  257,   -1,   -1,  260,   -1,   -1,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,   -1,  256,  257,   -1,
   -1,  260,   -1,   -1,  263,  264,  265,   -1,  267,  256,
   -1,  270,  271,  272,  261,  262,  263,  264,  265,   71,
   72,  268,   71,   72,  256,  272,   -1,    0,   -1,   -1,
  262,  263,  264,  265,   -1,   -1,    9,   -1,   11,   -1,
  272,   93,   -1,   92,   93,   -1,   -1,   -1,   -1,   -1,
   23,   -1,   -1,  105,   27,   28,  105,   -1,   -1,   -1,
   33,   -1,   -1,  115,  116,   -1,  115,  116,   41,    0,
   -1,   44,   -1,   -1,   -1,   -1,   -1,   50,    9,   -1,
   11,   -1,   -1,   -1,   -1,   -1,  138,   -1,   -1,  138,
   -1,   -1,   23,   -1,   -1,   68,   27,   28,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  158,   -1,  160,  158,
   41,  160,  256,   44,   -1,   -1,   -1,  261,  262,  263,
  264,  265,   -1,  175,  268,   -1,  175,   -1,  272,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  192,   -1,   -1,  192,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  207,   -1,   -1,  207,
};
}
final static short YYFINAL=10;
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
"declaracion_clase : encabezado_clase bloque_clase ','",
"declaracion_clase : encabezado_clase IMPLEMENT ID bloque_clase ','",
"declaracion_clase : encabezado_interface bloque_interfaz ','",
"declaracion_clase : encabezado_clase bloque_clase",
"declaracion_clase : encabezado_clase IMPLEMENT ID bloque_clase",
"declaracion_clase : encabezado_interface bloque_interfaz",
"encabezado_clase : CLASS ID",
"encabezado_interface : INTERFACE ID",
"bloque_clase : '{' cuerpo_clase '}'",
"bloque_clase : '{' cuerpo_clase ID ',' '}'",
"bloque_clase : '(' cuerpo_clase ')'",
"bloque_clase : '{' '}'",
"bloque_clase : '(' ')'",
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
"declaracion_funcion_interfaz : encabezado_funcion '(' ')'",
"declaracion_funcion_interfaz : encabezado_funcion '(' parametro_formal ')'",
"declaracion_funcion_interfaz : encabezado_funcion ','",
"declaracion_funcion_interfaz : encabezado_funcion parametro_formal ','",
"declaracion_funcion_interfaz : encabezado_funcion",
"declaracion_funcion_interfaz : encabezado_funcion parametro_formal",
"declaracion_variable : tipo lista_de_id ','",
"declaracion_variable : tipo lista_de_id",
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
"sentencia_retorno : RETURN",
"sentencia_imprimir : PRINT CADENA ','",
"sentencia_imprimir : PRINT CADENA",
"sentencia_imprimir : PRINT error ','",
"sentencia_imprimir : PRINT error",
"sentencia_imprimir : error CADENA ','",
"sentencia_seleccion : IF '(' comparacion ')' cuerpo_if END_IF ','",
"sentencia_seleccion : IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF ','",
"sentencia_seleccion : IF '(' comparacion ')' cuerpo_if END_IF",
"sentencia_seleccion : IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF",
"sentencia_seleccion : IF '(' ')' cuerpo_if END_IF ','",
"sentencia_seleccion : IF '(' ')' cuerpo_if ELSE cuerpo_if END_IF ','",
"sentencia_seleccion : IF '(' ')' cuerpo_if END_IF",
"sentencia_seleccion : IF '(' ')' cuerpo_if ELSE cuerpo_if END_IF",
"cuerpo_if : sentencia",
"cuerpo_if : bloque_sentencias",
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
"sentencia_iteracion : DO bloque_sentencias WHILE '(' comparacion ')' ','",
"sentencia_iteracion : DO bloque_sentencias WHILE '(' comparacion ')'",
"sentencia_iteracion : DO bloque_sentencias WHILE '(' ')' ','",
"sentencia_iteracion : DO bloque_sentencias WHILE '(' ')'",
"sentencia_expresion : declaracion_variable",
"sentencia_expresion : factor_inmediato",
"sentencia_expresion : asignacion",
"sentencia_expresion : llamado_clase '(' ')' ','",
"sentencia_expresion : llamado_clase '(' ')'",
"sentencia_expresion : llamado_clase '(' operacion ')' ','",
"sentencia_expresion : llamado_clase '(' operacion ')'",
"sentencia_expresion : TOD '(' operacion ')' ','",
"sentencia_expresion : TOD '(' operacion ')'",
"llamado_clase : ID",
"llamado_clase : llamado_clase '.' ID",
"asignacion : llamado_clase '=' operacion ','",
"asignacion : llamado_clase '=' operacion",
"asignacion : tipo llamado_clase '=' operacion ','",
"lista_de_id : ID",
"lista_de_id : lista_de_id ';' ID",
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
"tipo : error",
};

//#line 455 ".\gramatica.y"


static AnalizadorLexico analizadorLex = null;
static Parser par = null;
static Token token = null;
static ArrayList<String>  polaca;
static String ambito = ":main";
static String tipo = "";
static Tabla tablaSimbolos;

public static void main(String[] args) throws Exception{
        System.out.println("Iniciando compilacion...");
        tablaSimbolos = new Tabla();
        analizadorLex = new AnalizadorLexico(args,tablaSimbolos);
        
        polaca = new ArrayList<String>();
        
        par = new Parser(false);
        par.run();

        analizadorLex.MostrarTablaSimbolos();
        analizadorLex.MostrarErrores();
        
        for (int i = 0; i < polaca.size(); i++) {
          System.out.println(i + " " + polaca.get(i));
        }

        System.out.println("Fin de la compilacion");
}

private int yylex(){
        try {
          token = analizadorLex.getToken();
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
//#line 695 "Parser.java"
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
//#line 15 ".\gramatica.y"
{
                analizadorLex.addErroresLexicos(new Error("El programa tiene que terminar con \'}\'", analizadorLex.getLineaArchivo()));
         }
break;
case 3:
//#line 18 ".\gramatica.y"
{
                analizadorLex.addErroresLexicos(new Error("El programa tiene que arrancar con \'{\'", analizadorLex.getLineaArchivo()));
         }
break;
case 4:
//#line 21 ".\gramatica.y"
{
                analizadorLex.addErroresLexicos(new Error("El programa tiene que estar contenido en \'{\' \'}\'", analizadorLex.getLineaArchivo()));
        }
break;
case 10:
//#line 35 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if((val_peek(1).sval != null)){
                                if(!tablaSimbolos.agregarHerencia(val_peek(2).sval,val_peek(1).sval)){
                                        analizadorLex.addErroresLexicos(new Error("No esta declarada la clase " + val_peek(1).sval.substring(0,val_peek(1).sval.lastIndexOf(":")), analizadorLex.getLineaArchivo())); 
                                }
                        }
                    }
break;
case 11:
//#line 43 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if (!tablaSimbolos.agregarInterfaz(val_peek(4).sval,val_peek(2).sval + ":main")){
                                analizadorLex.addErroresLexicos(new Error("No se encuentra la interfaz " + val_peek(2).sval, analizadorLex.getLineaArchivo())); 
                        }
                        if((val_peek(1).sval != null)){
                                if((!tablaSimbolos.agregarHerencia(val_peek(4).sval,val_peek(1).sval))){
                                        analizadorLex.addErroresLexicos(new Error("No esta declarada la clase " + val_peek(1).sval.substring(0,val_peek(1).sval.lastIndexOf(":")), analizadorLex.getLineaArchivo())); 
                                }
                        }
                    }
break;
case 12:
//#line 54 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 13:
//#line 57 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 14:
//#line 61 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 15:
//#line 65 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 16:
//#line 71 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
                        }
                        yyval.sval = val_peek(0).sval + ambito;
                        ambito += ":" + val_peek(0).sval;
                 }
break;
case 17:
//#line 80 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
                        }
                        ambito += ":" + val_peek(0).sval;
                     }
break;
case 18:
//#line 88 ".\gramatica.y"
{
                        yyval.sval = null;
                }
break;
case 19:
//#line 91 ".\gramatica.y"
{
                        yyval.sval = val_peek(2).sval + ":main";
                }
break;
case 20:
//#line 94 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                }
break;
case 21:
//#line 97 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));
                }
break;
case 22:
//#line 100 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                }
break;
case 24:
//#line 106 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                }
break;
case 25:
//#line 109 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));
                }
break;
case 26:
//#line 112 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                }
break;
case 33:
//#line 127 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));
                }
break;
case 34:
//#line 130 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));
                }
break;
case 35:
//#line 135 ".\gramatica.y"
{
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 36:
//#line 138 ".\gramatica.y"
{
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 37:
//#line 141 ".\gramatica.y"
{
                                analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 38:
//#line 145 ".\gramatica.y"
{
                                analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 39:
//#line 149 ".\gramatica.y"
{
                                analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 40:
//#line 153 ".\gramatica.y"
{
                                analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 41:
//#line 157 ".\gramatica.y"
{
                                analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 42:
//#line 161 ".\gramatica.y"
{
                                analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 44:
//#line 168 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                     }
break;
case 45:
//#line 173 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 46:
//#line 176 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 47:
//#line 179 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 48:
//#line 183 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 49:
//#line 189 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
                        }
                        ambito += ":" + val_peek(0).sval;
                   }
break;
case 50:
//#line 197 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                                analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
                        }
                 }
break;
case 52:
//#line 205 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                  }
break;
case 53:
//#line 208 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));
                  }
break;
case 54:
//#line 211 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                  }
break;
case 65:
//#line 233 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                  }
break;
case 67:
//#line 239 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                   }
break;
case 68:
//#line 242 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));
                   }
break;
case 69:
//#line 245 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));
                   }
break;
case 70:
//#line 248 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("No existe esa expresion para imprimir la cadena", analizadorLex.getLineaArchivo()));
                   }
break;
case 73:
//#line 255 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                    }
break;
case 74:
//#line 258 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                    }
break;
case 75:
//#line 261 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));
                    }
break;
case 76:
//#line 264 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));
                    }
break;
case 77:
//#line 267 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));
                    }
break;
case 78:
//#line 270 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));
                    }
break;
case 82:
//#line 280 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                  }
break;
case 83:
//#line 283 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));
                  }
break;
case 84:
//#line 286 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                  }
break;
case 88:
//#line 296 ".\gramatica.y"
{
                analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));
            }
break;
case 89:
//#line 299 ".\gramatica.y"
{
                analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));
            }
break;
case 90:
//#line 302 ".\gramatica.y"
{
                analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));
            }
break;
case 97:
//#line 313 ".\gramatica.y"
{
                analizadorLex.addErroresLexicos(new Error("Comparacion mal definida", analizadorLex.getLineaArchivo()));
           }
break;
case 99:
//#line 319 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                    }
break;
case 100:
//#line 322 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte", analizadorLex.getLineaArchivo()));
                    }
break;
case 101:
//#line 325 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte y falta una \',\'", analizadorLex.getLineaArchivo()));
                    }
break;
case 106:
//#line 334 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                    }
break;
case 108:
//#line 338 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                    }
break;
case 110:
//#line 342 ".\gramatica.y"
{
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                    }
break;
case 111:
//#line 347 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval; 
              }
break;
case 112:
//#line 350 ".\gramatica.y"
{
                yyval.sval += "." + val_peek(0).sval;
              }
break;
case 113:
//#line 355 ".\gramatica.y"
{
                if(!tablaSimbolos.existeVariable(val_peek(3).sval,ambito)){
                        analizadorLex.addErroresLexicos(new Error("No se declaro la variable " + val_peek(3).sval + "en el ambito reconocible", analizadorLex.getLineaArchivo()));
                }
                polaca.add(val_peek(3).sval);polaca.add("=");
           }
break;
case 114:
//#line 361 ".\gramatica.y"
{
                analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
           }
break;
case 115:
//#line 364 ".\gramatica.y"
{
                analizadorLex.addErroresLexicos(new Error("No se puede declarar y asignar en la misma línea", analizadorLex.getLineaArchivo()));
           }
break;
case 116:
//#line 370 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
                }
            }
break;
case 117:
//#line 375 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
                }
            }
break;
case 119:
//#line 383 ".\gramatica.y"
{
                polaca.add("+");
          }
break;
case 120:
//#line 386 ".\gramatica.y"
{
                polaca.add("-");
          }
break;
case 122:
//#line 392 ".\gramatica.y"
{
                polaca.add("*");
        }
break;
case 123:
//#line 395 ".\gramatica.y"
{
                polaca.add("/");
        }
break;
case 127:
//#line 405 ".\gramatica.y"
{
                        polaca.add(val_peek(1).sval);polaca.add("1");polaca.add("-");{polaca.add(val_peek(1).sval);polaca.add("=");}
                 }
break;
case 128:
//#line 410 ".\gramatica.y"
{
                polaca.add(val_peek(0).sval);
             }
break;
case 129:
//#line 413 ".\gramatica.y"
{
                analizadorLex.convertirNegativo(val_peek(0).sval);
                polaca.add("-" + val_peek(0).sval);
             }
break;
case 130:
//#line 417 ".\gramatica.y"
{
                polaca.add(val_peek(0).sval);
             }
break;
case 131:
//#line 420 ".\gramatica.y"
{
                analizadorLex.convertirNegativo(val_peek(0).sval);
                polaca.add("-" + val_peek(0).sval);
             }
break;
case 132:
//#line 424 ".\gramatica.y"
{
                if(CheckRangoLong(val_peek(0).sval)){
                        analizadorLex.addErroresLexicos(new Error("LONG fuera de rango", analizadorLex.getLineaArchivo()));}
                else {
                        polaca.add(val_peek(0).sval);
                } 
             }
break;
case 133:
//#line 431 ".\gramatica.y"
{
                polaca.add(val_peek(0).sval);
             }
break;
case 134:
//#line 434 ".\gramatica.y"
{
                analizadorLex.addErroresLexicos(new Error("Las variables tipo UINT no pueden ser negativas", analizadorLex.getLineaArchivo()));
             }
break;
case 135:
//#line 439 ".\gramatica.y"
{
        tipo = "DOUBLE";
     }
break;
case 136:
//#line 442 ".\gramatica.y"
{
        tipo = "UINT";
     }
break;
case 137:
//#line 445 ".\gramatica.y"
{
        tipo = "LONG";
     }
break;
case 139:
//#line 449 ".\gramatica.y"
{
        analizadorLex.addErroresLexicos(new Error("Tipo no reconocido", analizadorLex.getLineaArchivo()));
     }
break;
//#line 1409 "Parser.java"
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
