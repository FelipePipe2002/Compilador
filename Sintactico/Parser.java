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
    4,    4,    7,    7,    7,    7,    8,    8,    8,    8,
    9,    9,   11,   11,   10,   10,   10,   10,   12,   12,
   12,   12,   12,   12,   12,   12,    3,    3,    5,    5,
    5,    5,    6,   13,   16,   16,   16,   16,   17,   17,
   18,   18,   19,   19,   19,   19,   19,   24,   24,   23,
   23,   23,   23,   23,   21,   21,   21,   21,   21,   21,
   21,   21,   26,   26,   27,   27,   27,   27,   28,   28,
   25,   25,   25,   25,   30,   30,   30,   30,   30,   30,
   30,   22,   22,   22,   22,   20,   20,   20,   20,   20,
   20,   20,   20,   32,   32,   31,   31,   31,   15,   15,
   29,   29,   29,   33,   33,   33,   33,   34,   34,   36,
   35,   35,   35,   35,   35,   35,   35,   14,   14,   14,
   14,   14,
};
final static short yylen[] = {                            2,
    3,    2,    2,    1,    1,    2,    1,    1,    1,    3,
    5,    3,    3,    3,    2,    2,    3,    3,    2,    2,
    1,    2,    1,    1,    1,    2,    1,    2,    5,    6,
    4,    5,    3,    4,    2,    3,    3,    2,    6,    7,
    5,    6,    1,    2,    3,    3,    2,    2,    1,    2,
    1,    1,    1,    1,    1,    1,    1,    2,    1,    3,
    2,    3,    2,    3,    7,    9,    6,    8,    6,    8,
    5,    7,    1,    1,    3,    3,    2,    2,    1,    2,
    3,    2,    2,    1,    1,    1,    1,    1,    1,    1,
    1,    7,    6,    6,    5,    1,    1,    4,    3,    5,
    4,    5,    4,    1,    3,    4,    3,    5,    1,    3,
    1,    3,    3,    1,    3,    3,    3,    1,    1,    2,
    1,    2,    1,    2,    1,    1,    2,    1,    1,    1,
    1,    1,
};
final static short yydefred[] = {                         0,
  132,    0,    0,  130,  129,  128,    0,  131,    0,    0,
    0,    5,    7,    8,    9,    0,   43,    0,    0,    0,
    0,    3,    6,  109,    0,    0,    0,    0,   10,    0,
    0,    0,   12,    1,   37,    0,    0,   15,   23,   24,
    0,   21,   16,    0,    0,    0,    0,    0,   19,   27,
    0,   25,   20,    0,  110,   11,   13,   22,   14,    0,
    0,    0,    0,   44,    0,   17,   28,   26,   18,    0,
    0,    0,    0,    0,    0,    0,   47,   96,   51,    0,
    0,   49,   52,   53,   54,   55,   56,   57,   97,    0,
   48,    0,   39,    0,    0,   33,    0,    0,    0,    0,
    0,    0,    0,    0,   58,    0,    0,    0,   45,   50,
    0,    0,    0,   46,   40,    0,    0,   34,   64,   91,
  104,  125,  126,  123,    0,    0,   85,   86,   87,   88,
   89,   90,    0,    0,    0,    0,    0,    0,  114,    0,
  119,   62,   60,   77,   79,    0,   78,    0,    0,    0,
    0,    0,    0,  105,    0,   29,    0,    0,   73,    0,
   74,  124,  127,  122,    0,    0,    0,    0,    0,    0,
    0,  120,   75,   80,   76,    0,    0,    0,   98,    0,
  106,   30,  117,    0,    0,    0,    0,    0,    0,  115,
  116,    0,    0,  102,  108,  100,    0,   69,    0,    0,
   94,    0,    0,    0,   65,   92,   70,    0,   66,
};
final static short yydgoto[] = {                         10,
   11,   12,   78,   14,   15,   18,   29,   33,   41,   51,
   42,   52,   46,   80,   25,   62,   81,   82,   83,   84,
   85,   86,   87,   88,  134,  160,  161,  146,  135,  136,
   89,  137,  138,  139,  140,  141,
};
final static short yysindex[] = {                       -62,
    0, -257, -257,    0,    0,    0, -257,    0, -127,    0,
  688,    0,    0,    0,    0, -218,    0,  -40,   32,  -30,
  702,    0,    0,    0,   30, -207,  716,  195,    0,  462,
 -118,    5,    0,    0,    0, -193,  -21,    0,    0,    0,
  727,    0,    0,  272,  -13,   51, -164, -257,    0,    0,
 -116,    0,    0,   22,    0,    0,    0,    0,    0,  -87,
  566,   69,  -13,    0,  190,    0,    0,    0,    0, -137,
  102, -212,   -6,   74,  108,    0,    0,    0,    0, -121,
  641,    0,    0,    0,    0,    0,    0,    0,    0,    7,
    0,  583,    0,  110,  593,    0,  124,  135,  -29,  148,
  160,  658,  604,  -50,    0,  -20,    0,   29,    0,    0,
  121,   56,  -20,    0,    0,   -8,  181,    0,    0,    0,
    0,    0,    0,    0,  -20,  171,    0,    0,    0,    0,
    0,    0, -110,  263,  293,  -20,  277,   49,    0,   50,
    0,    0,    0,    0,    0,  675,    0,  624,  290,   78,
  -20,  304,  150,    0,  153,    0,   -3,  425,    0, -158,
    0,    0,    0,    0,  171,  -20,  -20,  -20,   82,  202,
  202,    0,    0,    0,    0,   37,  312,  170,    0,  317,
    0,    0,    0,  171,  318, -100,   49,   49,   82,    0,
    0,  329,  296,    0,    0,    0,  119,    0,  171,  337,
    0,  342,  343,  129,    0,    0,    0,  354,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  399,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  400,    0,    0,    0,    1,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   18,    0,    0,  -27,    0,    0,    0,    0,  134,
    0,    0,    0,  235,    0,  -16,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   35,    0,    0,   15,    0,    0,  260,
  294,    0,    0,    0,    0,    0,  404,    0,    0,    0,
    0,    0,    0,    0,    0,   25,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  370,  -39,   87,    0,   62,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  322,    0,    0,  339,    0,   46,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  372,  373,    0,
    0,    0,    0,    0,    0,    0,  356,    0,    0,  379,
    0,    0,    0,    0,  430,    0,  112,  145,  377,    0,
    0,  449,    0,    0,    0,    0,    0,    0,    0,  480,
    0,  497,  522,    0,    0,    0,    0,  547,    0,
};
final static short yygindex[] = {                         0,
  412,   41, 1019,    0,  981,   19,  389,    0,  401,  398,
   40,   34,  -52,  973,    0,  -46,  371,  -41,  859,    0,
    0,    0,    0,    0,  257, -126,  364,  336,  -56,  305,
    0,  -32,   42,   11,    0,    0,
};
final static int YYTABLESIZE=1073;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         28,
   38,  121,  121,  121,  121,  121,   49,  121,   66,   32,
  125,  126,   97,   35,   17,  133,   94,   41,   28,  125,
  121,   19,  121,  104,  133,   20,   61,   90,   90,  104,
  127,   61,  128,  103,   42,  156,   61,   77,  186,  110,
  182,   38,  117,  100,  104,   53,  111,  108,   90,  150,
  110,   23,  112,   24,  153,   36,  155,  197,   41,   90,
    9,   23,   69,  101,   37,   31,   65,  113,  158,   90,
   90,   30,  204,   35,  112,   42,  125,  192,   55,  169,
   58,  133,   27,   58,   68,  121,   32,   68,   36,  151,
  170,   63,   31,   90,  178,  171,  127,   35,  128,  184,
  185,   27,  118,  118,  118,  118,  118,   64,  118,   60,
   94,  189,   93,   90,   60,   90,  102,  105,  177,   60,
  166,  118,  167,  118,  166,   38,  167,  111,    1,  111,
  111,  111,   90,    2,    3,    4,    5,    6,   98,   36,
    7,   99,   41,   48,    8,   48,  111,  106,  111,   31,
  107,   90,  112,  115,  112,  112,  112,  199,  200,   42,
  125,  152,  162,  163,  164,  133,   90,  118,   70,   71,
   32,  112,   72,  112,    3,    4,    5,    6,  119,   73,
  190,  191,   74,   75,   76,  113,  118,  113,  113,  113,
  180,  142,  166,    1,  167,  166,  181,  167,    2,    3,
    4,    5,    6,  143,  113,    7,  113,  187,  188,    8,
  103,  111,  166,  195,  167,  149,  121,  121,  121,  121,
  121,  157,  121,  121,  121,  121,  120,  121,   26,   95,
  121,  121,  121,   96,   35,   43,  112,  121,  121,  121,
  121,  121,  121,  122,  123,  124,  133,  129,  130,  131,
  132,  121,  122,  123,  124,  131,   38,   38,   38,   38,
   38,   38,   38,   38,   38,   38,   48,   38,   38,  113,
   38,   38,   38,   41,   41,   59,   36,   41,   41,   41,
   41,   41,   41,   48,   41,   41,   31,   41,   41,   41,
   42,   42,  120,  102,   42,   42,   42,   42,   42,   42,
   63,   42,   42,  165,   42,   42,   42,   32,  121,  122,
  123,  124,   59,  129,  130,  131,  132,  118,  118,  118,
  118,  118,  112,  118,  118,  118,  118,  154,  118,  176,
  172,  118,  118,  118,   61,  166,  202,  167,  118,  118,
  118,  118,  111,  111,  111,  111,  111,  179,  111,  111,
  111,  111,  127,  111,  128,  194,  111,  111,  111,   59,
  196,  198,   99,  111,  111,  111,  111,  112,  112,  112,
  112,  112,  201,  112,  112,  112,  112,  203,  112,  107,
  205,  112,  112,  112,   63,  206,  207,  208,  112,  112,
  112,  112,  121,  122,  123,  124,  103,  209,    4,    2,
  113,  113,  113,  113,  113,  132,  113,  113,  113,  113,
   84,  113,   82,   83,  113,  113,  113,   81,   61,  101,
   21,  113,  113,  113,  113,   56,   70,   71,   44,   54,
   72,   92,  193,    4,    5,    6,  104,   73,  148,  168,
   74,   75,   76,    0,  109,    1,   99,  109,    0,  104,
    1,    0,    4,    5,    6,    0,    3,    4,    5,    6,
    0,    8,  109,  107,  104,  183,    8,  166,    0,  167,
   71,    0,    0,  121,  122,  123,  124,    0,    0,    0,
  103,    0,    0,    0,    0,    0,    0,    0,    0,   95,
   59,   59,   59,   59,   59,    0,   59,   59,   59,   59,
    0,   59,   45,  101,   59,   59,   59,    0,    0,    0,
    0,    0,    0,    0,    0,   63,   63,   63,   63,   63,
   67,   63,   63,   63,   63,    0,   63,    1,  109,   63,
   63,   63,    0,    3,    4,    5,    6,   93,    0,    0,
    0,    0,    0,    8,    0,    0,    0,    0,  120,   61,
   61,   61,   61,   61,   71,   61,   61,   61,   61,    0,
   61,    0,   72,   61,   61,   61,    0,    0,    0,  129,
  130,  131,  132,   95,    0,    0,    0,   99,   99,   99,
   99,   99,    0,   99,   99,   99,   99,   68,   99,    0,
    0,   99,   99,   99,  107,  107,  107,  107,  107,    0,
  107,  107,  107,  107,   67,  107,   91,    0,  107,  107,
  107,  103,  103,  103,  103,  103,    0,  103,  103,  103,
  103,   93,  103,  114,    0,  103,  103,  103,    0,    0,
    0,    0,    0,  116,  101,  101,  101,  101,  101,    0,
  101,  101,  101,  101,  147,  101,   72,    0,  101,  101,
  101,    0,    0,    0,    0,    0,    0,    0,    0,  109,
  109,  109,  109,  109,  175,  109,  109,  109,  109,    0,
  109,   68,    0,  109,  109,  109,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   71,   71,   71,   71,   71,
    0,   71,   71,   71,   71,    0,   71,    0,    0,   71,
   71,   71,    0,    0,   95,   95,   95,   95,   95,    0,
   95,   95,   95,   95,    0,   95,    0,    1,   95,   95,
   95,    0,    0,    0,    4,    5,    6,    0,    0,    0,
    0,    0,    0,    8,    0,   67,   67,   67,   67,   67,
    0,   67,   67,   67,   67,    0,   67,    0,    0,   67,
   67,   67,   93,   93,   93,   93,   93,    0,   93,   93,
   93,   93,    0,   93,    0,  109,   93,   93,   93,    0,
    0,    0,    0,    0,    0,    0,    0,   72,   72,   72,
   72,   72,  144,   72,   72,   72,   72,    0,   72,    0,
    0,   72,   72,   72,    0,    0,    0,    0,    0,  173,
    0,    0,   68,   68,   68,   68,   68,    0,   68,   68,
   68,   68,   22,   68,    0,    0,   68,   68,   68,    0,
    0,   70,   71,    0,    0,   72,   34,    3,    4,    5,
    6,    0,   73,    0,    0,   74,   75,   76,   70,   71,
   38,    0,   72,    0,    3,    4,    5,    6,    1,   73,
    0,   57,   74,   75,   76,    4,    5,    6,    0,   70,
   71,    0,    0,   72,    8,    0,    4,    5,    6,    0,
   73,    0,    0,   74,   75,   76,    0,    0,    0,   70,
   71,    0,    0,   72,    0,    0,    4,    5,    6,    0,
   73,    0,    0,   74,   75,   76,   70,   71,    0,    0,
   72,    0,    3,    4,    5,    6,    0,   73,    0,    0,
   74,   75,   76,   70,   71,    0,    0,   72,    0,    0,
    4,    5,    6,    0,   73,    0,    0,   74,   75,   76,
   70,   71,    0,    0,   72,    0,    0,    4,    5,    6,
    0,   73,    0,    1,   74,   75,   76,    0,    2,    3,
    4,    5,    6,    0,    0,    7,    0,    1,    0,    8,
  145,  145,    2,    3,    4,    5,    6,    0,    0,    7,
    0,    1,   16,    8,    0,    0,    0,    3,    4,    5,
    6,   16,    1,   16,  159,    0,    0,    8,    3,    4,
    5,    6,    0,   16,    0,    0,    0,    0,    8,   16,
   16,    0,   47,    0,  174,    0,  174,   40,   40,    0,
    0,   50,   50,   16,    0,    0,   16,    0,   13,    0,
    0,   40,    0,  159,   40,    0,    0,   13,    0,   13,
    0,   67,    0,    0,   67,    0,    0,   47,    0,   13,
   79,   79,  159,    0,    0,   39,   39,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  159,    0,   39,
    0,   79,   39,    0,    0,    0,    0,   47,    0,    0,
    0,    0,   79,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,   42,   43,   44,   45,  125,   47,  125,   40,
   40,   41,   65,   41,  272,   45,   63,    0,   40,   40,
   60,    3,   62,   40,   45,    7,   40,   60,   61,   46,
   60,   40,   62,   40,    0,   44,   40,  125,  165,   81,
   44,   41,   95,  256,   61,   41,   40,   80,   81,  106,
   92,   11,   46,  272,  111,   41,  113,  184,   41,   92,
  123,   21,   41,  276,  272,   41,   48,   61,  125,  102,
  103,   40,  199,   44,   46,   41,   40,   41,  272,  136,
   41,   45,  123,   44,   51,  125,   41,   54,   59,   61,
   42,   41,  123,  126,  151,   47,   60,  125,   62,  258,
  259,  123,   41,   42,   43,   44,   45,  272,   47,  123,
  157,  168,   44,  146,  123,  148,  123,   44,   41,  123,
   43,   60,   45,   62,   43,  125,   45,   41,  256,   43,
   44,   45,  165,  261,  262,  263,  264,  265,  276,  125,
  268,   40,  125,  262,  272,  262,   60,   40,   62,  125,
  272,  184,   41,   44,   43,   44,   45,  258,  259,  125,
   40,   41,  273,  274,  275,   45,  199,   44,  256,  257,
  125,   60,  260,   62,  262,  263,  264,  265,   44,  267,
  170,  171,  270,  271,  272,   41,  125,   43,   44,   45,
   41,   44,   43,  256,   45,   43,   44,   45,  261,  262,
  263,  264,  265,   44,   60,  268,   62,  166,  167,  272,
   40,  125,   43,   44,   45,  266,  256,  257,  258,  259,
  260,   41,  262,  263,  264,  265,  256,  267,  269,   40,
  270,  271,  272,   44,  262,   41,  125,  277,  278,  279,
  280,  281,  272,  273,  274,  275,   45,  277,  278,  279,
  280,  272,  273,  274,  275,  272,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  262,  267,  268,  125,
  270,  271,  272,  256,  257,   41,  262,  260,  261,  262,
  263,  264,  265,  262,  267,  268,  262,  270,  271,  272,
  256,  257,  256,  123,  260,  261,  262,  263,  264,  265,
   41,  267,  268,   41,  270,  271,  272,  262,  272,  273,
  274,  275,   41,  277,  278,  279,  280,  256,  257,  258,
  259,  260,   46,  262,  263,  264,  265,  272,  267,   40,
  281,  270,  271,  272,   41,   43,   41,   45,  277,  278,
  279,  280,  256,  257,  258,  259,  260,   44,  262,  263,
  264,  265,   60,  267,   62,   44,  270,  271,  272,  125,
   44,   44,   41,  277,  278,  279,  280,  256,  257,  258,
  259,  260,   44,  262,  263,  264,  265,  259,  267,   41,
   44,  270,  271,  272,  125,   44,   44,  259,  277,  278,
  279,  280,  272,  273,  274,  275,   41,   44,    0,    0,
  256,  257,  258,  259,  260,  272,  262,  263,  264,  265,
   41,  267,   41,   41,  270,  271,  272,   41,  125,   41,
    9,  277,  278,  279,  280,   37,  256,  257,   28,   32,
  260,   61,  176,  263,  264,  265,   73,  267,  103,  135,
  270,  271,  272,   -1,   41,  256,  125,   44,   -1,   46,
  256,   -1,  263,  264,  265,   -1,  262,  263,  264,  265,
   -1,  272,   59,  125,   61,   41,  272,   43,   -1,   45,
   41,   -1,   -1,  272,  273,  274,  275,   -1,   -1,   -1,
  125,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   41,
  256,  257,  258,  259,  260,   -1,  262,  263,  264,  265,
   -1,  267,   41,  125,  270,  271,  272,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  257,  258,  259,  260,
   41,  262,  263,  264,  265,   -1,  267,  256,  125,  270,
  271,  272,   -1,  262,  263,  264,  265,   41,   -1,   -1,
   -1,   -1,   -1,  272,   -1,   -1,   -1,   -1,  256,  256,
  257,  258,  259,  260,  125,  262,  263,  264,  265,   -1,
  267,   -1,   41,  270,  271,  272,   -1,   -1,   -1,  277,
  278,  279,  280,  125,   -1,   -1,   -1,  256,  257,  258,
  259,  260,   -1,  262,  263,  264,  265,   41,  267,   -1,
   -1,  270,  271,  272,  256,  257,  258,  259,  260,   -1,
  262,  263,  264,  265,  125,  267,   41,   -1,  270,  271,
  272,  256,  257,  258,  259,  260,   -1,  262,  263,  264,
  265,  125,  267,   41,   -1,  270,  271,  272,   -1,   -1,
   -1,   -1,   -1,   41,  256,  257,  258,  259,  260,   -1,
  262,  263,  264,  265,   41,  267,  125,   -1,  270,  271,
  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,
  257,  258,  259,  260,   41,  262,  263,  264,  265,   -1,
  267,  125,   -1,  270,  271,  272,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  257,  258,  259,  260,
   -1,  262,  263,  264,  265,   -1,  267,   -1,   -1,  270,
  271,  272,   -1,   -1,  256,  257,  258,  259,  260,   -1,
  262,  263,  264,  265,   -1,  267,   -1,  256,  270,  271,
  272,   -1,   -1,   -1,  263,  264,  265,   -1,   -1,   -1,
   -1,   -1,   -1,  272,   -1,  256,  257,  258,  259,  260,
   -1,  262,  263,  264,  265,   -1,  267,   -1,   -1,  270,
  271,  272,  256,  257,  258,  259,  260,   -1,  262,  263,
  264,  265,   -1,  267,   -1,  125,  270,  271,  272,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,
  259,  260,  125,  262,  263,  264,  265,   -1,  267,   -1,
   -1,  270,  271,  272,   -1,   -1,   -1,   -1,   -1,  125,
   -1,   -1,  256,  257,  258,  259,  260,   -1,  262,  263,
  264,  265,  125,  267,   -1,   -1,  270,  271,  272,   -1,
   -1,  256,  257,   -1,   -1,  260,  125,  262,  263,  264,
  265,   -1,  267,   -1,   -1,  270,  271,  272,  256,  257,
  125,   -1,  260,   -1,  262,  263,  264,  265,  256,  267,
   -1,  125,  270,  271,  272,  263,  264,  265,   -1,  256,
  257,   -1,   -1,  260,  272,   -1,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,   -1,   -1,   -1,  256,
  257,   -1,   -1,  260,   -1,   -1,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,  256,  257,   -1,   -1,
  260,   -1,  262,  263,  264,  265,   -1,  267,   -1,   -1,
  270,  271,  272,  256,  257,   -1,   -1,  260,   -1,   -1,
  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,
  256,  257,   -1,   -1,  260,   -1,   -1,  263,  264,  265,
   -1,  267,   -1,  256,  270,  271,  272,   -1,  261,  262,
  263,  264,  265,   -1,   -1,  268,   -1,  256,   -1,  272,
  102,  103,  261,  262,  263,  264,  265,   -1,   -1,  268,
   -1,  256,    0,  272,   -1,   -1,   -1,  262,  263,  264,
  265,    9,  256,   11,  126,   -1,   -1,  272,  262,  263,
  264,  265,   -1,   21,   -1,   -1,   -1,   -1,  272,   27,
   28,   -1,   30,   -1,  146,   -1,  148,   27,   28,   -1,
   -1,   31,   32,   41,   -1,   -1,   44,   -1,    0,   -1,
   -1,   41,   -1,  165,   44,   -1,   -1,    9,   -1,   11,
   -1,   51,   -1,   -1,   54,   -1,   -1,   65,   -1,   21,
   60,   61,  184,   -1,   -1,   27,   28,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  199,   -1,   41,
   -1,   81,   44,   -1,   -1,   -1,   -1,   95,   -1,   -1,
   -1,   -1,   92,
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
"declaracion_clase : CLASS nombre_bloque bloque_clase",
"declaracion_clase : CLASS nombre_bloque IMPLEMENT ID bloque_clase",
"declaracion_clase : INTERFACE nombre_bloque bloque_interfaz",
"bloque_clase : '{' cuerpo_clase '}'",
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
"cuerpo_interfaz : declaracion_funcion_vacia",
"cuerpo_interfaz : cuerpo_interfaz declaracion_funcion_vacia",
"cuerpo_interfaz : declaracion_funcion",
"cuerpo_interfaz : cuerpo_interfaz declaracion_funcion",
"declaracion_funcion_vacia : VOID nombre_bloque '(' ')' ','",
"declaracion_funcion_vacia : VOID nombre_bloque '(' parametro_formal ')' ','",
"declaracion_funcion_vacia : VOID nombre_bloque '(' ')'",
"declaracion_funcion_vacia : VOID nombre_bloque '(' parametro_formal ')'",
"declaracion_funcion_vacia : VOID nombre_bloque ','",
"declaracion_funcion_vacia : VOID nombre_bloque parametro_formal ','",
"declaracion_funcion_vacia : VOID nombre_bloque",
"declaracion_funcion_vacia : VOID nombre_bloque parametro_formal",
"declaracion_variable : tipo lista_de_id ','",
"declaracion_variable : tipo lista_de_id",
"declaracion_funcion : VOID nombre_bloque '(' ')' bloque_sentencias_funcion ','",
"declaracion_funcion : VOID nombre_bloque '(' parametro_formal ')' bloque_sentencias_funcion ','",
"declaracion_funcion : VOID nombre_bloque '(' ')' bloque_sentencias_funcion",
"declaracion_funcion : VOID nombre_bloque '(' parametro_formal ')' bloque_sentencias_funcion",
"nombre_bloque : ID",
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
"factor_inmediato : factor_comun \"--\"",
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

//#line 227 ".\gramatica.y"


static AnalizadorLexico analizadorLex = null;
static Parser par = null;
static Token token = null;
static ArrayList<String>  polaca;
static String ambito = ":main";
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
//#line 672 "Parser.java"
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
{analizadorLex.addErroresLexicos(new Error("El programa tiene que terminar con \'}\'", analizadorLex.getLineaArchivo()));}
break;
case 3:
//#line 16 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El programa tiene que arrancar con \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 4:
//#line 17 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El programa tiene que estar contenido en \'{\' \'}\'", analizadorLex.getLineaArchivo()));}
break;
case 10:
//#line 29 ".\gramatica.y"
{System.out.println("CREACION DE CLASE" + ", linea: " + analizadorLex.getLineaArchivo()); ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 11:
//#line 30 ".\gramatica.y"
{System.out.println("CREACION DE CLASE CON HERENCIA" + ", linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 12:
//#line 31 ".\gramatica.y"
{System.out.println("CREACION DE INTERFAZ" + ", linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 14:
//#line 35 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 15:
//#line 36 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 16:
//#line 37 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 18:
//#line 41 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 19:
//#line 42 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 20:
//#line 43 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 27:
//#line 56 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));}
break;
case 28:
//#line 57 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));}
break;
case 29:
//#line 60 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION VACIA SIN PARAMETROS" + ", linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 30:
//#line 61 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION VACIA" + ", linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 31:
//#line 62 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 32:
//#line 63 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 33:
//#line 64 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 34:
//#line 65 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 35:
//#line 66 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 36:
//#line 67 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 37:
//#line 70 ".\gramatica.y"
{System.out.println("DECLARACION DE VARIABLE" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 38:
//#line 71 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 39:
//#line 74 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION SIN PARAMETROS" + ", linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 40:
//#line 75 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION" + ", linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 41:
//#line 76 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 42:
//#line 77 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 43:
//#line 80 ".\gramatica.y"
{ambito += ":" + val_peek(0).sval;}
break;
case 46:
//#line 87 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 47:
//#line 88 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 48:
//#line 89 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 54:
//#line 102 ".\gramatica.y"
{System.out.println("SENTENCIA IF" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 55:
//#line 103 ".\gramatica.y"
{System.out.println("SENTENCIA ITERACION" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 56:
//#line 104 ".\gramatica.y"
{System.out.println("SENTENCIA IMPRESION DE CADENA" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 57:
//#line 105 ".\gramatica.y"
{System.out.println("SENTENCIA RETURN" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 59:
//#line 109 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 61:
//#line 113 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 62:
//#line 114 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
break;
case 63:
//#line 115 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
break;
case 64:
//#line 116 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No existe esa expresion para imprimir la cadena", analizadorLex.getLineaArchivo()));}
break;
case 67:
//#line 121 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 68:
//#line 122 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 69:
//#line 123 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
break;
case 70:
//#line 124 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
break;
case 71:
//#line 125 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
break;
case 72:
//#line 126 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
break;
case 76:
//#line 134 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 77:
//#line 135 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 78:
//#line 136 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 82:
//#line 144 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 83:
//#line 145 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 84:
//#line 146 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 91:
//#line 155 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Comparacion mal definida", analizadorLex.getLineaArchivo()));}
break;
case 93:
//#line 159 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 94:
//#line 160 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte", analizadorLex.getLineaArchivo()));}
break;
case 95:
//#line 161 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte y falta una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 97:
//#line 165 ".\gramatica.y"
{System.out.println("ASIGNACION" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 98:
//#line 166 ".\gramatica.y"
{System.out.println("LLAMADO A METODO" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 99:
//#line 167 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 100:
//#line 168 ".\gramatica.y"
{System.out.println("LLAMADO A METODO" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 101:
//#line 169 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 103:
//#line 171 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 104:
//#line 174 ".\gramatica.y"
{yyval.sval = val_peek(0).sval; }
break;
case 105:
//#line 175 ".\gramatica.y"
{yyval.sval += "." + val_peek(0).sval;}
break;
case 106:
//#line 178 ".\gramatica.y"
{polaca.add(val_peek(3).sval);polaca.add("=");}
break;
case 107:
//#line 179 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 108:
//#line 180 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se puede declarar y asignar en la misma línea", analizadorLex.getLineaArchivo()));}
break;
case 109:
//#line 184 ".\gramatica.y"
{if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito)){analizadorLex.addErroresLexicos(new Error("No se puede declarar dos variables con el mismo nombre dentro del mismo ambito", analizadorLex.getLineaArchivo()));}}
break;
case 110:
//#line 185 ".\gramatica.y"
{if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito)){analizadorLex.addErroresLexicos(new Error("No se puede declarar dos variables con el mismo dentro del mismo ambito", analizadorLex.getLineaArchivo()));}}
break;
case 112:
//#line 189 ".\gramatica.y"
{polaca.add("+");}
break;
case 113:
//#line 190 ".\gramatica.y"
{polaca.add("-");}
break;
case 115:
//#line 194 ".\gramatica.y"
{polaca.add("*");}
break;
case 116:
//#line 195 ".\gramatica.y"
{polaca.add("/");}
break;
case 120:
//#line 203 ".\gramatica.y"
{polaca.add("--");}
break;
case 121:
//#line 205 ".\gramatica.y"
{polaca.add(val_peek(0).sval);}
break;
case 122:
//#line 206 ".\gramatica.y"
{analizadorLex.convertirNegativo(val_peek(0).sval);
polaca.add("-" + val_peek(0).sval);}
break;
case 123:
//#line 208 ".\gramatica.y"
{polaca.add(val_peek(0).sval);}
break;
case 124:
//#line 209 ".\gramatica.y"
{analizadorLex.convertirNegativo(val_peek(0).sval);
polaca.add("-" + val_peek(0).sval);}
break;
case 125:
//#line 211 ".\gramatica.y"
{{if(CheckRangoLong(val_peek(0).sval)){analizadorLex.addErroresLexicos(new Error("Long fuera de rango", analizadorLex.getLineaArchivo()));}
  else {
    polaca.add(val_peek(0).sval);
  }}}
break;
case 126:
//#line 215 ".\gramatica.y"
{polaca.add(val_peek(0).sval);}
break;
case 127:
//#line 216 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Las variables tipo UINT no pueden ser negativas", analizadorLex.getLineaArchivo()));}
break;
case 132:
//#line 223 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Tipo no reconocido", analizadorLex.getLineaArchivo()));}
break;
//#line 1158 "Parser.java"
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
