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
    7,    9,    9,    9,    9,   10,   10,   12,   12,   11,
   11,   11,   11,   13,   13,   13,   13,   13,   13,   13,
   13,    3,    3,    5,    5,    5,    5,   14,   15,   18,
   18,   18,   18,   19,   19,   20,   20,   21,   21,   21,
   21,   21,   26,   26,   25,   25,   25,   25,   25,   23,
   23,   23,   23,   23,   23,   23,   23,   28,   28,   29,
   29,   29,   29,   30,   30,   27,   27,   27,   27,   32,
   32,   32,   32,   32,   32,   32,   24,   24,   24,   24,
   22,   22,   22,   22,   22,   22,   22,   22,   22,   35,
   35,   34,   34,   34,   17,   17,   31,   31,   31,   36,
   36,   36,   36,   37,   37,   33,   38,   38,   38,   38,
   38,   38,   38,   16,   16,   16,   16,   16,
};
final static short yylen[] = {                            2,
    3,    2,    2,    1,    1,    2,    1,    1,    1,    3,
    5,    3,    2,    4,    2,    2,    2,    3,    3,    2,
    2,    3,    3,    2,    2,    1,    2,    1,    1,    1,
    2,    1,    2,    4,    5,    3,    4,    2,    3,    1,
    2,    3,    2,    5,    6,    4,    5,    2,    2,    3,
    3,    2,    2,    1,    2,    1,    1,    1,    1,    1,
    1,    1,    2,    1,    3,    2,    3,    2,    3,    7,
    9,    6,    8,    6,    8,    5,    7,    1,    1,    3,
    3,    2,    2,    1,    2,    3,    2,    2,    1,    1,
    1,    1,    1,    1,    1,    1,    7,    6,    6,    5,
    1,    1,    1,    4,    3,    5,    4,    5,    4,    1,
    3,    4,    3,    5,    1,    3,    1,    3,    3,    1,
    3,    3,    3,    1,    1,    2,    1,    2,    1,    2,
    1,    1,    2,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
  138,    0,    0,  136,  135,  134,    0,  137,    0,    0,
    0,    5,    7,    8,    9,    0,    0,    0,    0,   16,
   48,   17,    0,    3,    6,    0,    0,    0,    0,    0,
    0,    0,    0,  115,    0,    1,    0,   20,   28,   29,
    0,   26,   21,    0,   10,   24,   32,    0,   30,    0,
   25,    0,   12,    0,    0,    0,   42,    0,    0,   18,
   27,   19,   22,   33,   31,   38,    0,    0,   23,    0,
    0,    0,    0,   49,  116,   11,    0,    0,   39,    0,
    0,    0,    0,    0,    0,    0,   52,  101,   56,    0,
    0,   54,   57,   58,   59,   60,   61,   62,  102,  103,
    0,   53,    0,   44,    0,   34,    0,    0,    0,    0,
    0,    0,    0,    0,   63,    0,    0,    0,   50,   55,
    0,    0,    0,  126,   51,   45,   35,   69,   96,  110,
  131,  132,  129,    0,    0,   90,   91,   92,   93,   94,
   95,    0,    0,    0,    0,  125,    0,    0,  120,  124,
   67,   65,   82,   84,    0,   83,    0,    0,    0,    0,
    0,    0,  111,    0,    0,   78,    0,   79,  130,  133,
  128,    0,    0,    0,    0,    0,    0,    0,   80,   85,
   81,    0,    0,    0,  104,    0,  112,  123,    0,    0,
    0,    0,    0,    0,  121,  122,    0,    0,  108,  114,
  106,    0,   74,    0,    0,   99,    0,    0,    0,   70,
   97,   75,    0,   71,
};
final static short yydgoto[] = {                         10,
   11,   12,   88,   14,   15,   16,   29,   17,   32,   41,
   48,   42,   49,   18,   55,   90,   35,   72,   91,   92,
   93,   94,   95,   96,   97,   98,  143,  167,  168,  155,
  144,  145,  146,  100,  147,  148,  149,  150,
};
final static short yysindex[] = {                       666,
    0, -257, -250,    0,    0,    0, -243,    0,  789,    0,
  403,    0,    0,    0,    0,  -29,  -21,   12, -195,    0,
    0,    0,  734,    0,    0, -168,  -95,  280,   52, -122,
  -31,   62,  413,    0,    6,    0,   -9,    0,    0,    0,
  749,    0,    0,  373,    0,    0,    0, -120,    0,  141,
    0,    8,    0,   -2,   68, -154,    0, -144,   97,    0,
    0,    0,    0,    0,    0,    0,  643,  106,    0,  356,
  598,  108,   -2,    0,    0,    0,  -24,  115,    0, -118,
  125, -232,    7,  127,  150,    0,    0,    0,    0,  -80,
  686,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -38,    0,  616,    0,  154,    0,   -4,  157,  -28,  162,
  166,  704,  633,  -58,    0,  173,    0,   34,    0,    0,
   84,  -61,  173,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  173,  247,    0,    0,    0,    0,    0,
    0,  -91,  171,  164,  173,    0,  -42,   31,    0,    0,
    0,    0,    0,    0,  722,    0,  653,  174,   46,  173,
  172,   91,    0,  135,  112,    0, -176,    0,    0,    0,
    0,  247,  173,  173,  173,   88,  160,  160,    0,    0,
    0,   75,  176,  144,    0,  178,    0,    0,  247,  179,
  -96,   31,   31,   88,    0,    0,  181,  186,    0,    0,
    0,  -44,    0,  247,  189,    0,  198,  232,   42,    0,
    0,    0,  233,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  217,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  293,    0,    0,    0,    0,    0,   48,    0,
    0,   61,    0,    0,    1,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -32,
    0,    0,    0,    0,    0,    0,    0,    0,   74,    0,
    0,    0,    0,    0,    0,    0,    0,  -27,    0,    0,
    0,   18,    0,    0,    0,    0,  -14,    0,    0,   22,
    0,    0,    0,  226,    0,  -40,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   35,    0,   -8,    0,    0,  267,
  303,    0,    0,    0,    0,    0,  433,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  273,    0,  104,  131,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  336,    0,    0,  384,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  274,  278,    0,    0,    0,    0,
    0,    0,  454,    0,    0,  472,    0,    0,    0,  492,
    0,  159,  193,  286,    0,    0,  509,    0,    0,    0,
    0,    0,    0,    0,  528,    0,  545,  563,    0,    0,
    0,    0,  581,    0,
};
final static short yygindex[] = {                         0,
  319,   43,   28,    0,  992,    0,  295,    0,    0,  306,
  309,   59,    5,   40,   14, 1032,    0,  -66,  270,   10,
  -50,    0,    0,    0,    0,    0,  161, -129,  262,  252,
  -37,  228,  913,    0,  -45,   21,   19,    0,
};
final static int YYTABLESIZE=1117;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        110,
   43,  121,   46,  122,   63,  110,  105,  122,   40,   51,
   28,  134,  135,   41,   20,   71,  142,   46,   31,  106,
  110,   21,  123,  110,  101,  101,   36,   13,   22,   38,
   28,  136,   37,  137,   47,   71,   13,   71,   13,  127,
  105,   43,  191,  111,  118,  101,  113,   13,   69,   57,
   13,   33,   65,   25,   39,   39,   65,  101,   46,  202,
   15,  154,  154,   68,   58,   25,  101,  101,   39,   50,
   50,   39,  177,   14,  209,   47,   34,  178,  159,  122,
   78,  189,  190,  162,  166,  164,  183,   50,  173,  101,
  174,   50,   40,   27,  160,   45,  165,   41,   70,   61,
  120,   30,   61,   37,  180,   53,  180,  176,   73,  101,
   36,  101,  120,   27,  134,  197,   37,   74,   70,  142,
   70,  166,  184,  134,  161,   43,  101,   75,  142,  112,
  173,  186,  174,  173,  136,  174,  137,  194,  166,    3,
   76,    3,   46,  101,  127,  127,  127,  127,  127,   79,
  127,  104,  188,  166,  173,  107,  174,  108,  101,   47,
    1,  204,  205,  127,  109,  127,    3,    4,    5,    6,
  115,  117,   13,  117,  117,  117,    8,  173,  187,  174,
   67,  169,  170,  171,   66,   15,  173,  200,  174,  116,
  117,  117,  117,  192,  193,  195,  196,  126,   14,  118,
  128,  118,  118,  118,  142,  151,  173,  158,  174,  152,
  163,  172,  134,  182,  208,  185,    4,  142,  118,  199,
  118,  201,  203,  136,  206,  137,  207,  129,  127,   40,
    3,  137,  210,  119,   41,  119,  119,  119,  124,   26,
  110,  211,  124,  130,  131,  132,  133,   36,  138,  139,
  140,  141,  119,   37,  119,  117,   43,   43,   43,   43,
   43,   43,   43,   43,   43,   43,   64,   43,   43,    3,
   43,   43,   43,   46,   46,  212,  214,   46,   46,   46,
   46,   46,   46,  118,   46,   46,  113,   46,   46,   46,
   47,   47,    2,  138,   47,   47,   47,   47,   47,   47,
  213,   47,   47,   13,   47,   47,   47,   68,   13,   13,
   13,   13,   13,   89,   87,   13,   15,  119,   88,   13,
   43,   15,   15,   15,   15,   15,   86,   23,   15,   14,
  129,   59,   15,   44,   14,   14,   14,   14,   14,   52,
  103,   14,  198,   66,  114,   14,  130,  131,  132,  133,
   64,  138,  139,  140,  141,  130,  131,  132,  133,  127,
  127,  127,  127,  127,  157,  127,  127,  127,  127,  112,
  127,  175,    0,  127,  127,  127,  105,    0,    0,    0,
  127,  127,  127,  127,    0,    0,  117,  117,  117,  117,
  117,   68,  117,  117,  117,  117,    1,  117,    0,    0,
  117,  117,  117,    4,    5,    6,    0,  117,  117,  117,
  117,    0,    8,   62,  118,  118,  118,  118,  118,  129,
  118,  118,  118,  118,  113,  118,    0,   66,  118,  118,
  118,  130,  131,  132,  133,  118,  118,  118,  118,    0,
  138,  139,  140,  141,  130,  131,  132,  133,  119,  119,
  119,  119,  119,   54,  119,  119,  119,  119,    0,  119,
  105,    0,  119,  119,  119,    0,    0,    0,    0,  119,
  119,  119,  119,  115,    0,    0,  115,    0,  110,    0,
   87,   64,   64,   64,   64,   64,    0,   64,   64,   64,
   64,  115,   64,  110,  109,   64,   64,   64,    0,    0,
    0,    0,   80,   81,    0,    0,   82,    0,  113,    4,
    5,    6,  107,   83,    0,    0,   84,   85,   86,    0,
    0,    0,   68,   68,   68,   68,   68,   24,   68,   68,
   68,   68,   76,   68,    0,    1,   68,   68,   68,    0,
    0,    3,    4,    5,    6,    0,    0,    0,    0,  100,
    0,    8,    0,    0,    0,    0,    0,  115,   66,   66,
   66,   66,   66,    0,   66,   66,   66,   66,   72,   66,
    0,    0,   66,   66,   66,    0,    0,    0,  109,    0,
    0,    0,    0,    0,    0,   98,    0,    0,    0,    0,
    0,  105,  105,  105,  105,  105,  107,  105,  105,  105,
  105,    0,  105,   77,    0,  105,  105,  105,    0,    0,
    0,   80,   81,    0,    0,   82,   76,    3,    4,    5,
    6,   73,   83,    0,    0,   84,   85,   86,    1,    0,
    0,    0,    0,  100,    3,    4,    5,    6,  102,  113,
  113,  113,  113,  113,    8,  113,  113,  113,  113,    0,
  113,    0,   72,  113,  113,  113,  125,    0,    1,    0,
    0,    0,    0,    2,    3,    4,    5,    6,    1,   98,
    7,    0,    0,  156,    8,    4,    5,    6,    0,    0,
    0,    0,    0,   77,    8,    0,    0,   77,  115,  115,
  115,  115,  115,  181,  115,  115,  115,  115,    0,  115,
    0,    0,  115,  115,  115,   73,    0,    0,    0,  109,
  109,  109,  109,  109,    0,  109,  109,  109,  109,    0,
  109,    0,    0,  109,  109,  109,    0,  107,  107,  107,
  107,  107,    0,  107,  107,  107,  107,    0,  107,    0,
    0,  107,  107,  107,    0,    0,    0,   76,   76,   76,
   76,   76,    0,   76,   76,   76,   76,    0,   76,    0,
    0,   76,   76,   76,  100,  100,  100,  100,  100,    0,
  100,  100,  100,  100,    0,  100,    0,    0,  100,  100,
  100,    0,    0,   72,   72,   72,   72,   72,    9,   72,
   72,   72,   72,    0,   72,    0,    0,   72,   72,   72,
   98,   98,   98,   98,   98,    0,   98,   98,   98,   98,
  119,   98,    0,    0,   98,   98,   98,    0,   77,   77,
   77,   77,   77,    0,   77,   77,   77,   77,  153,   77,
    0,    0,   77,   77,   77,    0,   73,   73,   73,   73,
   73,    0,   73,   73,   73,   73,  179,   73,    0,    0,
   73,   73,   73,   80,   81,    0,    0,   82,   36,    3,
    4,    5,    6,    0,   83,    0,    0,   84,   85,   86,
    0,   80,   81,   60,    0,   82,    0,    3,    4,    5,
    6,    0,   83,    0,    0,   84,   85,   86,   80,   81,
    0,    0,   82,    0,    0,    4,    5,    6,    1,   83,
    0,    0,   84,   85,   86,    4,    5,    6,   80,   81,
    0,    0,   82,    0,    8,    4,    5,    6,    0,   83,
    0,    1,   84,   85,   86,    0,    2,    3,    4,    5,
    6,    0,    0,    7,    0,    0,    0,    8,    0,    0,
    0,   80,   81,    0,    0,   82,    0,    3,    4,    5,
    6,    0,   83,    0,    0,   84,   85,   86,    0,   80,
   81,    0,    0,   82,    0,    0,    4,    5,    6,    0,
   83,    0,    0,   84,   85,   86,    0,   80,   81,    0,
    0,   82,   99,   99,    4,    5,    6,    0,   83,    1,
    0,   84,   85,   86,    2,    3,    4,    5,    6,    0,
    0,    7,    0,   99,    1,    8,    0,    0,    0,    0,
    3,    4,    5,    6,    0,   99,    0,    0,   40,   40,
    8,   47,   47,    0,   99,   99,    0,    0,    0,    0,
    0,   19,   40,    0,    0,   40,    0,    0,    0,   64,
   19,    0,   19,   64,    1,    0,    0,   99,    0,    2,
    3,    4,    5,    6,   19,    0,    7,    0,   19,   19,
    8,   89,   89,    0,   56,    0,    0,   99,    0,   99,
    0,    0,   19,    0,    0,   19,    0,    0,    0,    0,
    0,   56,   89,    0,   99,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   89,    0,    0,    0,   56,    0,
    0,   99,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   99,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,  125,   46,  125,   46,   73,   46,   41,   41,
   40,   40,   41,   41,  272,   40,   45,    0,   40,   44,
   61,  272,   61,  256,   70,   71,   41,    0,  272,  125,
   40,   60,   41,   62,    0,   40,    9,   40,   11,   44,
  107,   41,  172,  276,   90,   91,   40,    0,   41,   44,
   23,   40,   48,   11,   27,   28,   52,  103,   41,  189,
    0,  112,  113,   50,   59,   23,  112,  113,   41,   30,
   31,   44,   42,    0,  204,   41,  272,   47,  116,   46,
   67,  258,  259,  121,  135,  123,   41,   48,   43,  135,
   45,   52,  125,  123,   61,   44,  134,  125,  123,   41,
   91,  123,   44,  272,  155,   44,  157,  145,   41,  155,
  125,  157,  103,  123,   40,   41,  125,  272,  123,   45,
  123,  172,  160,   40,   41,  125,  172,  272,   45,  123,
   43,   41,   45,   43,   60,   45,   62,  175,  189,  262,
   44,  262,  125,  189,   41,   42,   43,   44,   45,   44,
   47,   44,   41,  204,   43,   41,   45,  276,  204,  125,
  256,  258,  259,   60,   40,   62,  262,  263,  264,  265,
   44,   41,  125,   43,   44,   45,  272,   43,   44,   45,
   40,  273,  274,  275,   44,  125,   43,   44,   45,   40,
   60,  272,   62,  173,  174,  177,  178,   44,  125,   41,
   44,   43,   44,   45,   45,   44,   43,  266,   45,   44,
  272,   41,   40,   40,  259,   44,    0,   45,   60,   44,
   62,   44,   44,   60,   44,   62,   41,  256,  125,  262,
  262,  272,   44,   41,  262,   43,   44,   45,  281,  269,
  281,   44,  281,  272,  273,  274,  275,  262,  277,  278,
  279,  280,   60,  262,   62,  125,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,   41,  267,  268,  262,
  270,  271,  272,  256,  257,   44,   44,  260,  261,  262,
  263,  264,  265,  125,  267,  268,   40,  270,  271,  272,
  256,  257,    0,  272,  260,  261,  262,  263,  264,  265,
  259,  267,  268,  256,  270,  271,  272,   41,  261,  262,
  263,  264,  265,   41,   41,  268,  256,  125,   41,  272,
   41,  261,  262,  263,  264,  265,   41,    9,  268,  256,
  256,   37,  272,   28,  261,  262,  263,  264,  265,   31,
   71,  268,  182,   41,   83,  272,  272,  273,  274,  275,
  125,  277,  278,  279,  280,  272,  273,  274,  275,  256,
  257,  258,  259,  260,  113,  262,  263,  264,  265,  123,
  267,  144,   -1,  270,  271,  272,   41,   -1,   -1,   -1,
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
   -1,  260,   70,   71,  263,  264,  265,   -1,  267,  256,
   -1,  270,  271,  272,  261,  262,  263,  264,  265,   -1,
   -1,  268,   -1,   91,  256,  272,   -1,   -1,   -1,   -1,
  262,  263,  264,  265,   -1,  103,   -1,   -1,   27,   28,
  272,   30,   31,   -1,  112,  113,   -1,   -1,   -1,   -1,
   -1,    0,   41,   -1,   -1,   44,   -1,   -1,   -1,   48,
    9,   -1,   11,   52,  256,   -1,   -1,  135,   -1,  261,
  262,  263,  264,  265,   23,   -1,  268,   -1,   27,   28,
  272,   70,   71,   -1,   33,   -1,   -1,  155,   -1,  157,
   -1,   -1,   41,   -1,   -1,   44,   -1,   -1,   -1,   -1,
   -1,   50,   91,   -1,  172,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  103,   -1,   -1,   -1,   67,   -1,
   -1,  189,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  204,
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
"declaracion_funcion_vacia : encabezado_funcion '(' ')' ','",
"declaracion_funcion_vacia : encabezado_funcion '(' parametro_formal ')' ','",
"declaracion_funcion_vacia : encabezado_funcion '(' ')'",
"declaracion_funcion_vacia : encabezado_funcion '(' parametro_formal ')'",
"declaracion_funcion_vacia : encabezado_funcion ','",
"declaracion_funcion_vacia : encabezado_funcion parametro_formal ','",
"declaracion_funcion_vacia : encabezado_funcion",
"declaracion_funcion_vacia : encabezado_funcion parametro_formal",
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

//#line 236 ".\gramatica.y"


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
//#line 690 "Parser.java"
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
{System.out.println("CREACION DE CLASE, linea: " + analizadorLex.getLineaArchivo()); ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 11:
//#line 30 ".\gramatica.y"
{System.out.println("CREACION DE CLASE CON HERENCIA, linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 12:
//#line 31 ".\gramatica.y"
{System.out.println("CREACION DE INTERFAZ, linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 13:
//#line 32 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 14:
//#line 33 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 15:
//#line 34 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 16:
//#line 36 ".\gramatica.y"
{if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));}ambito += ":" + val_peek(0).sval;}
break;
case 17:
//#line 39 ".\gramatica.y"
{if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));}ambito += ":" + val_peek(0).sval;}
break;
case 19:
//#line 43 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 20:
//#line 44 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 21:
//#line 45 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 23:
//#line 49 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 24:
//#line 50 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 25:
//#line 51 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 32:
//#line 64 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));}
break;
case 33:
//#line 65 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));}
break;
case 34:
//#line 68 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION VACIA SIN PARAMETROS, linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 35:
//#line 69 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION VACIA, linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 36:
//#line 70 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 37:
//#line 71 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 38:
//#line 72 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 39:
//#line 73 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 40:
//#line 74 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 41:
//#line 75 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 42:
//#line 78 ".\gramatica.y"
{System.out.println("DECLARACION DE VARIABLE, linea: " + analizadorLex.getLineaArchivo());}
break;
case 43:
//#line 79 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 44:
//#line 82 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION SIN PARAMETROS, linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 45:
//#line 83 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION, linea: " + analizadorLex.getLineaArchivo());ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 46:
//#line 84 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 47:
//#line 85 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));ambito = ambito.substring(0,ambito.lastIndexOf(":"));}
break;
case 48:
//#line 88 ".\gramatica.y"
{if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));}ambito += ":" + val_peek(0).sval;}
break;
case 51:
//#line 95 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 52:
//#line 96 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 53:
//#line 97 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 59:
//#line 110 ".\gramatica.y"
{System.out.println("SENTENCIA IF, linea: " + analizadorLex.getLineaArchivo());}
break;
case 60:
//#line 111 ".\gramatica.y"
{System.out.println("SENTENCIA ITERACION, linea: " + analizadorLex.getLineaArchivo());}
break;
case 61:
//#line 112 ".\gramatica.y"
{System.out.println("SENTENCIA IMPRESION DE CADENA, linea: " + analizadorLex.getLineaArchivo());}
break;
case 62:
//#line 113 ".\gramatica.y"
{System.out.println("SENTENCIA RETURN, linea: " + analizadorLex.getLineaArchivo());}
break;
case 64:
//#line 117 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 66:
//#line 121 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 67:
//#line 122 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
break;
case 68:
//#line 123 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
break;
case 69:
//#line 124 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No existe esa expresion para imprimir la cadena", analizadorLex.getLineaArchivo()));}
break;
case 72:
//#line 129 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 73:
//#line 130 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 74:
//#line 131 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
break;
case 75:
//#line 132 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
break;
case 76:
//#line 133 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
break;
case 77:
//#line 134 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
break;
case 81:
//#line 142 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 82:
//#line 143 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 83:
//#line 144 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 87:
//#line 152 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 88:
//#line 153 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 89:
//#line 154 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 96:
//#line 163 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Comparacion mal definida", analizadorLex.getLineaArchivo()));}
break;
case 98:
//#line 167 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 99:
//#line 168 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte", analizadorLex.getLineaArchivo()));}
break;
case 100:
//#line 169 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte y falta una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 103:
//#line 174 ".\gramatica.y"
{System.out.println("ASIGNACION, linea: " + analizadorLex.getLineaArchivo());}
break;
case 104:
//#line 175 ".\gramatica.y"
{System.out.println("LLAMADO A METODO, linea: " + analizadorLex.getLineaArchivo());}
break;
case 105:
//#line 176 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 106:
//#line 177 ".\gramatica.y"
{System.out.println("LLAMADO A METODO, linea: " + analizadorLex.getLineaArchivo());}
break;
case 107:
//#line 178 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 109:
//#line 180 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 110:
//#line 183 ".\gramatica.y"
{yyval.sval = val_peek(0).sval; }
break;
case 111:
//#line 184 ".\gramatica.y"
{yyval.sval += "." + val_peek(0).sval;}
break;
case 112:
//#line 187 ".\gramatica.y"
{polaca.add(val_peek(3).sval);polaca.add("=");}
break;
case 113:
//#line 188 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 114:
//#line 189 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se puede declarar y asignar en la misma línea", analizadorLex.getLineaArchivo()));}
break;
case 115:
//#line 193 ".\gramatica.y"
{if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));}}
break;
case 116:
//#line 194 ".\gramatica.y"
{if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));}}
break;
case 118:
//#line 198 ".\gramatica.y"
{polaca.add("+");}
break;
case 119:
//#line 199 ".\gramatica.y"
{polaca.add("-");}
break;
case 121:
//#line 203 ".\gramatica.y"
{polaca.add("*");}
break;
case 122:
//#line 204 ".\gramatica.y"
{polaca.add("/");}
break;
case 126:
//#line 212 ".\gramatica.y"
{polaca.add(val_peek(1).sval);polaca.add("1");polaca.add("-");{polaca.add(val_peek(1).sval);polaca.add("=");}}
break;
case 127:
//#line 214 ".\gramatica.y"
{polaca.add(val_peek(0).sval);}
break;
case 128:
//#line 215 ".\gramatica.y"
{analizadorLex.convertirNegativo(val_peek(0).sval);
polaca.add("-" + val_peek(0).sval);}
break;
case 129:
//#line 217 ".\gramatica.y"
{polaca.add(val_peek(0).sval);}
break;
case 130:
//#line 218 ".\gramatica.y"
{analizadorLex.convertirNegativo(val_peek(0).sval);
polaca.add("-" + val_peek(0).sval);}
break;
case 131:
//#line 220 ".\gramatica.y"
{{if(CheckRangoLong(val_peek(0).sval)){analizadorLex.addErroresLexicos(new Error("Long fuera de rango", analizadorLex.getLineaArchivo()));}
  else {
    polaca.add(val_peek(0).sval);
  }}}
break;
case 132:
//#line 224 ".\gramatica.y"
{polaca.add(val_peek(0).sval);}
break;
case 133:
//#line 225 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Las variables tipo UINT no pueden ser negativas", analizadorLex.getLineaArchivo()));}
break;
case 134:
//#line 228 ".\gramatica.y"
{tipo = "Double";}
break;
case 135:
//#line 229 ".\gramatica.y"
{tipo = "Uint";}
break;
case 136:
//#line 230 ".\gramatica.y"
{tipo = "Long";}
break;
case 138:
//#line 232 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Tipo no reconocido", analizadorLex.getLineaArchivo()));}
break;
//#line 1208 "Parser.java"
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
