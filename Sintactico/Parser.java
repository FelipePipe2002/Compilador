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
    0,    0,    0,    0,    1,    1,    2,    2,    2,    4,
    4,    4,    4,    4,    4,    6,    8,    7,    7,    7,
    7,    7,    9,    9,    9,    9,   10,   10,   12,   12,
   11,   11,   11,   11,   13,   13,   13,   13,   13,   13,
   13,   13,    3,    3,    5,    5,    5,    5,   14,   15,
   18,   18,   18,   18,   19,   19,   20,   20,   21,   21,
   21,   21,   21,   26,   26,   25,   25,   25,   25,   25,
   23,   23,   23,   23,   23,   23,   27,   27,   28,   28,
   29,   29,   31,   31,   31,   31,   32,   32,   30,   30,
   30,   30,   34,   34,   34,   34,   34,   34,   34,   24,
   24,   24,   35,   22,   22,   22,   22,   22,   22,   22,
   22,   22,   38,   38,   37,   37,   37,   37,   17,   17,
   33,   33,   33,   39,   39,   39,   39,   40,   40,   36,
   41,   41,   41,   41,   41,   41,   41,   16,   16,   16,
   16,   16,
};
final static short yylen[] = {                            2,
    3,    2,    2,    1,    1,    2,    1,    1,    1,    3,
    5,    3,    2,    4,    2,    2,    2,    3,    5,    3,
    2,    2,    3,    3,    2,    2,    1,    2,    1,    1,
    1,    2,    1,    2,    4,    5,    3,    4,    2,    3,
    1,    2,    3,    2,    5,    6,    4,    5,    2,    2,
    3,    3,    2,    2,    1,    2,    1,    1,    1,    1,
    1,    1,    1,    2,    1,    3,    2,    3,    2,    3,
    4,    5,    3,    4,    3,    4,    4,    3,    1,    1,
    2,    2,    3,    3,    2,    2,    1,    2,    3,    2,
    2,    1,    1,    1,    1,    1,    1,    1,    1,    7,
    6,    5,    1,    1,    1,    1,    4,    3,    5,    4,
    5,    4,    1,    3,    4,    3,    4,    5,    1,    3,
    1,    3,    3,    1,    3,    3,    3,    1,    1,    2,
    1,    2,    1,    2,    1,    1,    2,    1,    1,    1,
    1,    1,
};
final static short yydefred[] = {                         0,
  142,    0,    0,  140,  139,  138,    0,  141,    0,    0,
    0,    5,    7,    8,    9,    0,    0,    0,    0,   16,
   49,   17,    0,    3,    6,    0,    0,    0,    0,    0,
    0,    0,    0,  119,    0,    1,    0,   21,   29,   30,
    0,   27,   22,    0,   10,   25,   33,    0,   31,    0,
   26,    0,   12,    0,    0,    0,   43,    0,    0,    0,
   18,   28,   20,   23,   34,   32,   39,    0,    0,   24,
    0,    0,    0,    0,   50,  120,   11,    0,    0,    0,
   40,    0,    0,    0,  103,    0,    0,    0,   53,  104,
   57,    0,    0,   55,   58,   59,   60,   61,   62,   63,
    0,    0,  105,  106,    0,   54,    0,   45,    0,   19,
   35,    0,    0,    0,    0,    0,   64,    0,    0,    0,
   51,   56,    0,    0,   79,    0,   80,    0,    0,    0,
    0,  130,   52,   46,   36,   70,   99,  113,  135,  136,
  133,    0,   78,   93,   94,   95,   96,   97,   98,    0,
    0,    0,    0,  129,    0,    0,  124,  128,   68,   66,
    0,    0,   85,   87,    0,   86,    0,    0,    0,   75,
    0,    0,    0,    0,  114,    0,    0,  134,  137,  132,
   77,    0,    0,    0,    0,    0,    0,    0,    0,   83,
   88,   84,   81,   82,   71,    0,   76,    0,  107,    0,
  115,  117,  127,    0,    0,    0,  125,  126,  111,  118,
   72,  102,    0,  109,    0,  100,
};
final static short yydgoto[] = {                         10,
   11,   12,   90,   14,   15,   16,   29,   17,   32,   41,
   48,   42,   49,   18,   55,   92,   35,   73,   93,   94,
   95,   96,   97,   98,   99,  100,  101,  126,  171,  151,
  127,  165,  152,  153,  102,  154,  104,  155,  156,  157,
  158,
};
final static short yysindex[] = {                       -55,
    0, -209, -194,    0,    0,    0, -186,    0, -181,    0,
  701,    0,    0,    0,    0,  -21,  -16,   52, -178,    0,
    0,    0,  714,    0,    0, -151,  -95,  603,   81, -122,
   14,   83,  199,    0,    6,    0,  -15,    0,    0,    0,
  -67,    0,    0,  608,    0,    0,    0, -110,    0,  141,
    0,   15,    0,  -13,   90, -133,    0, -130,  100,  115,
    0,    0,    0,    0,    0,    0,    0,  613,  118,    0,
  630,  532,  121,  -13,    0,    0,    0,   46,  -24,  146,
    0,  -88,  152, -219,    0,  150,  175,    0,    0,    0,
    0,  -52,  648,    0,    0,    0,    0,    0,    0,    0,
  247,  -12,    0,    0,  -38,    0,  551,    0,  178,    0,
    0,  -18,  179,  -28,  183,  186,    0,   84,    0,   -1,
    0,    0,  665,  571,    0,  -34,    0,  -32,  -36,  -19,
   84,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   84,    0,    0,    0,    0,    0,    0,    0,  -91,
  214,  171,   84,    0,  -39,   -3,    0,    0,    0,    0,
   91,   84,    0,    0,  684,    0,  591,  247,  249,    0,
  -33,  254,  257,  112,    0,   69,  113,    0,    0,    0,
    0,   84,   84,   84,   27,  105,  105,  264,  135,    0,
    0,    0,    0,    0,    0,  270,    0,   75,    0,  275,
    0,    0,    0,   -3,   -3,   27,    0,    0,    0,    0,
    0,    0,  277,    0,  283,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  321,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  328,    0,    0,    0,    0,    0,   48,    0,
    0,   61,    0,    0,    1,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -27,
    0,    0,    0,    0,    0,    0,    0,    0,   74,   60,
    0,    0,    0,    0,    0,    0,    0,    0,   -8,    0,
    0,    0,   18,    0,    0,    0,    0,    0,    5,    0,
    0,   68,    0,    0,    0,  274,    0,  -40,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   35,    0,
    0,    8,    0,    0,  300,  331,    0,    0,  424,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  293,    0,  104,  131,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  351,    0,
    0,    0,  371,    0,    0,  441,    0,    0,    0,    0,
    0,    0,    0,  302,  303,    0,    0,  458,    0,    0,
    0,    0,    0,    0,    0,  475,    0,    0,    0,  494,
    0,    0,    0,  159,  399,  304,    0,    0,    0,    0,
    0,    0,    0,    0,  512,    0,
};
final static short yygindex[] = {                         0,
  342,   56,   62,    0,  967,    0,  336,    0,    0,  337,
  354,   97,   -5,   70,  -14, 1003,    0,  -43,  314,   16,
  -72,    0,    0,    0,    0,    0,    0,    0,    0,  202,
  -64,  290,  -65,  255,    0,  900,    0,  867, -142,   25,
    0,
};
final static int YYTABLESIZE=1074;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        113,
   44,  129,   46,  142,  173,  113,  130,  130,  150,  170,
  197,  142,  143,   41,   64,   72,  150,   47,   28,  111,
  113,   72,  131,   31,   28,  135,   72,  124,  125,   38,
  109,  144,   42,  145,   48,   69,  115,  128,  186,  204,
  205,   44,   66,  187,  130,   37,   66,   13,   38,   57,
  164,  164,  161,   80,   51,   70,  116,   61,   47,  162,
   15,   13,   20,  174,   58,  176,   25,    9,  109,  182,
   13,  183,   13,   14,    1,   48,  177,   21,   25,    2,
    3,    4,    5,    6,   13,   22,    7,  185,   39,   39,
    8,   33,  191,   34,  191,  193,  189,   41,   71,   50,
   50,   27,   39,  194,   71,   39,   30,   27,  122,   71,
  123,  182,  201,  183,  142,  212,   42,   50,  206,  150,
   37,   50,  122,  142,   45,   44,   53,  202,  150,   37,
   74,  188,   38,  182,  144,  183,  145,   62,   75,    3,
   62,   76,   47,   77,  131,  131,  131,  131,  131,  150,
  131,    3,  200,  203,  182,  182,  183,  183,   78,   48,
    1,   81,  131,  131,  108,  131,    3,    4,    5,    6,
  110,  121,   13,  121,  121,  121,    8,  182,  210,  183,
   68,  178,  179,  180,   67,   15,  112,  113,    1,  121,
  121,  114,  121,  117,    3,    4,    5,    6,   14,  122,
    1,  122,  122,  122,   60,    2,    3,    4,    5,    6,
  207,  208,    7,  182,  118,  183,    8,  122,  122,  119,
  122,  134,  136,  168,  169,  196,  159,  137,  131,  160,
  144,  141,  145,  172,   41,  138,  139,  140,  141,   54,
  113,  132,  132,  138,  139,  140,  141,   26,  146,  147,
  148,  149,  175,   42,  181,  121,   44,   44,   44,   44,
   44,   44,   44,   44,   44,   44,   37,   44,   44,   38,
   44,   44,   44,   47,   47,    3,    3,   47,   47,   47,
   47,   47,   47,  122,   47,   47,  124,   47,   47,   47,
   48,   48,  195,  198,   48,   48,   48,   48,   48,   48,
  199,   48,   48,   13,   48,   48,   48,  209,   13,   13,
   13,   13,   13,  211,   65,   13,   15,  215,  214,   13,
    4,   15,   15,   15,   15,   15,  216,    2,   15,   14,
  137,  141,   15,   92,   14,   14,   14,   14,   14,  142,
   69,   14,   90,   91,   89,   14,  138,  139,  140,  141,
   23,  146,  147,  148,  149,  138,  139,  140,  141,  131,
  131,  131,  131,  131,   44,  131,  131,  131,  131,  123,
  131,   67,   59,  131,  131,  131,  138,  139,  140,  141,
  131,  131,  131,  131,   52,  107,  121,  121,  121,  121,
  121,   73,  121,  121,  121,  121,    1,  121,   65,  213,
  121,  121,  121,    4,    5,    6,  184,  121,  121,  121,
  121,  108,    8,  167,  122,  122,  122,  122,  122,    0,
  122,  122,  122,  122,   69,  122,  137,    0,  122,  122,
  122,    0,    0,    0,    0,  122,  122,  122,  122,  123,
    0,  123,  123,  123,    0,    0,    0,  146,  147,  148,
  149,    0,    0,    0,    1,   67,    0,  123,  123,    0,
  123,    4,    5,    6,  119,    0,    0,  119,    0,  113,
    8,    0,    0,    0,    0,   73,    0,    0,    0,    0,
    0,  116,  119,    0,  113,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  108,    0,    0,  112,    0,
    0,    0,   82,   83,    0,    0,   84,    0,    0,    4,
    5,    6,    0,   85,    0,   74,   86,   87,   88,    0,
    0,    0,    0,  123,    0,    0,    0,    0,    0,   65,
   65,   65,   65,   65,  110,   65,   65,   65,   65,    0,
   65,    0,    0,   65,   65,   65,    0,    0,  119,    0,
    0,    0,  101,    0,    0,   69,   69,   69,   69,   69,
    0,   69,   69,   69,   69,  116,   69,    0,    0,   69,
   69,   69,  106,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  112,    0,    0,    0,   67,   67,   67,   67,
   67,  133,   67,   67,   67,   67,    0,   67,    0,   74,
   67,   67,   67,    0,    0,    0,   73,   73,   73,   73,
   73,  166,   73,   73,   73,   73,    0,   73,  110,    0,
   73,   73,   73,    0,    0,    0,  108,  108,  108,  108,
  108,  192,  108,  108,  108,  108,  101,  108,    0,    0,
  108,  108,  108,   43,    0,    0,    0,    0,   63,    0,
    0,    0,    0,   79,  123,  123,  123,  123,  123,    0,
  123,  123,  123,  123,    0,  123,    0,    0,  123,  123,
  123,    0,    0,    0,    0,  123,  123,  123,  123,  119,
  119,  119,  119,  119,    0,  119,  119,  119,  119,    0,
  119,    0,    0,  119,  119,  119,  116,  116,  116,  116,
  116,    0,  116,  116,  116,  116,    0,  116,    0,    0,
  116,  116,  116,  112,  112,  112,  112,  112,    0,  112,
  112,  112,  112,    0,  112,    0,    0,  112,  112,  112,
   74,   74,   74,   74,   74,    0,   74,   74,   74,   74,
    0,   74,    0,    0,   74,   74,   74,    0,    0,  110,
  110,  110,  110,  110,   89,  110,  110,  110,  110,    0,
  110,    0,    0,  110,  110,  110,    0,  101,  101,  101,
  101,  101,  121,  101,  101,  101,  101,    0,  101,    0,
    0,  101,  101,  101,    0,    0,    0,   82,   83,  163,
    0,   84,    0,    3,    4,    5,    6,    0,   85,    0,
    0,   86,   87,   88,    0,    0,   82,   83,  190,    0,
   84,    0,    3,    4,    5,    6,    0,   85,    0,    0,
   86,   87,   88,    0,    0,   24,   82,   83,    0,    0,
   84,    0,    0,    4,    5,    6,    0,   85,   36,    0,
   86,   87,   88,    0,    0,    0,   82,   83,    0,    0,
   84,    0,    0,    4,    5,    6,    0,   85,    1,    0,
   86,   87,   88,    1,    3,    4,    5,    6,    1,    3,
    4,    5,    6,    0,    8,    4,    5,    6,    0,    8,
    0,    0,    0,    0,    8,   82,   83,    0,    0,   84,
    0,    3,    4,    5,    6,    0,   85,    0,    0,   86,
   87,   88,    0,   82,   83,    0,    0,   84,    0,    3,
    4,    5,    6,    0,   85,    0,    0,   86,   87,   88,
   82,   83,    0,    0,   84,    0,    0,    4,    5,    6,
    0,   85,    0,    0,   86,   87,   88,  105,  105,   82,
   83,    0,    0,   84,    0,    0,    4,    5,    6,    0,
   85,    0,    0,   86,   87,   88,    1,    0,  120,  105,
    0,    2,    3,    4,    5,    6,    0,  105,    7,    1,
  103,  103,    8,  105,    2,    3,    4,    5,    6,    0,
    0,    7,    0,    0,    0,    8,    0,    0,    0,  105,
  105,    0,  103,   40,   40,    0,   47,   47,    0,    0,
  103,    0,   19,    0,    0,    0,  103,   40,    0,    0,
   40,   19,    0,   19,   65,    0,    0,    0,   65,    0,
    0,    0,  103,  103,    0,   19,    0,    0,    0,   19,
   19,  105,    0,  105,  105,   56,    0,   91,   91,    0,
    0,    0,    0,   19,    0,    0,   19,    0,    0,    0,
    0,    0,   56,    0,    0,    0,    0,    0,    0,   91,
    0,    0,    0,    0,  103,    0,  103,  103,    0,    0,
   56,    0,    0,   91,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,  125,   40,   41,   46,   46,   46,   45,   44,
   44,   40,   41,   41,  125,   40,   45,    0,   40,   44,
   61,   40,   61,   40,   40,   44,   40,   40,  101,  125,
   74,   60,   41,   62,    0,   50,  256,  102,   42,  182,
  183,   41,   48,   47,   46,   41,   52,    0,   41,   44,
  123,  124,  118,   68,   41,   41,  276,  125,   41,   61,
    0,    0,  272,  129,   59,  131,   11,  123,  112,   43,
    9,   45,   11,    0,  256,   41,  142,  272,   23,  261,
  262,  263,  264,  265,   23,  272,  268,  153,   27,   28,
  272,   40,  165,  272,  167,  168,  162,  125,  123,   30,
   31,  123,   41,  168,  123,   44,  123,  123,   93,  123,
  123,   43,   44,   45,   40,   41,  125,   48,  184,   45,
  272,   52,  107,   40,   44,  125,   44,   59,   45,  125,
   41,   41,  125,   43,   60,   45,   62,   41,  272,  262,
   44,  272,  125,   44,   41,   42,   43,   44,   45,   45,
   47,  262,   41,   41,   43,   43,   45,   45,   44,  125,
  256,   44,   59,   60,   44,   62,  262,  263,  264,  265,
  125,   41,  125,   43,   44,   45,  272,   43,   44,   45,
   40,  273,  274,  275,   44,  125,   41,  276,  256,   59,
   60,   40,   62,   44,  262,  263,  264,  265,  125,   41,
  256,   43,   44,   45,  272,  261,  262,  263,  264,  265,
  186,  187,  268,   43,   40,   45,  272,   59,   60,  272,
   62,   44,   44,  258,  259,  259,   44,  256,  125,   44,
   60,  272,   62,  266,  262,  272,  273,  274,  275,   41,
  281,  281,  281,  272,  273,  274,  275,  269,  277,  278,
  279,  280,  272,  262,   41,  125,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  262,  267,  268,  262,
  270,  271,  272,  256,  257,  262,  262,  260,  261,  262,
  263,  264,  265,  125,  267,  268,   40,  270,  271,  272,
  256,  257,   44,   40,  260,  261,  262,  263,  264,  265,
   44,  267,  268,  256,  270,  271,  272,   44,  261,  262,
  263,  264,  265,   44,   41,  268,  256,   41,   44,  272,
    0,  261,  262,  263,  264,  265,   44,    0,  268,  256,
  256,  272,  272,   41,  261,  262,  263,  264,  265,  272,
   41,  268,   41,   41,   41,  272,  272,  273,  274,  275,
    9,  277,  278,  279,  280,  272,  273,  274,  275,  256,
  257,  258,  259,  260,   28,  262,  263,  264,  265,  123,
  267,   41,   37,  270,  271,  272,  272,  273,  274,  275,
  277,  278,  279,  280,   31,   72,  256,  257,  258,  259,
  260,   41,  262,  263,  264,  265,  256,  267,  125,  198,
  270,  271,  272,  263,  264,  265,  152,  277,  278,  279,
  280,   41,  272,  124,  256,  257,  258,  259,  260,   -1,
  262,  263,  264,  265,  125,  267,  256,   -1,  270,  271,
  272,   -1,   -1,   -1,   -1,  277,  278,  279,  280,   41,
   -1,   43,   44,   45,   -1,   -1,   -1,  277,  278,  279,
  280,   -1,   -1,   -1,  256,  125,   -1,   59,   60,   -1,
   62,  263,  264,  265,   41,   -1,   -1,   44,   -1,   46,
  272,   -1,   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,
   -1,   41,   59,   -1,   61,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,   41,   -1,
   -1,   -1,  256,  257,   -1,   -1,  260,   -1,   -1,  263,
  264,  265,   -1,  267,   -1,   41,  270,  271,  272,   -1,
   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,   -1,  256,
  257,  258,  259,  260,   41,  262,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,   -1,   -1,  125,   -1,
   -1,   -1,   41,   -1,   -1,  256,  257,  258,  259,  260,
   -1,  262,  263,  264,  265,  125,  267,   -1,   -1,  270,
  271,  272,   41,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  125,   -1,   -1,   -1,  256,  257,  258,  259,
  260,   41,  262,  263,  264,  265,   -1,  267,   -1,  125,
  270,  271,  272,   -1,   -1,   -1,  256,  257,  258,  259,
  260,   41,  262,  263,  264,  265,   -1,  267,  125,   -1,
  270,  271,  272,   -1,   -1,   -1,  256,  257,  258,  259,
  260,   41,  262,  263,  264,  265,  125,  267,   -1,   -1,
  270,  271,  272,   41,   -1,   -1,   -1,   -1,   41,   -1,
   -1,   -1,   -1,   41,  256,  257,  258,  259,  260,   -1,
  262,  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,
  272,   -1,   -1,   -1,   -1,  277,  278,  279,  280,  256,
  257,  258,  259,  260,   -1,  262,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,  256,  257,  258,  259,
  260,   -1,  262,  263,  264,  265,   -1,  267,   -1,   -1,
  270,  271,  272,  256,  257,  258,  259,  260,   -1,  262,
  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,
  256,  257,  258,  259,  260,   -1,  262,  263,  264,  265,
   -1,  267,   -1,   -1,  270,  271,  272,   -1,   -1,  256,
  257,  258,  259,  260,  125,  262,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,   -1,  256,  257,  258,
  259,  260,  125,  262,  263,  264,  265,   -1,  267,   -1,
   -1,  270,  271,  272,   -1,   -1,   -1,  256,  257,  125,
   -1,  260,   -1,  262,  263,  264,  265,   -1,  267,   -1,
   -1,  270,  271,  272,   -1,   -1,  256,  257,  125,   -1,
  260,   -1,  262,  263,  264,  265,   -1,  267,   -1,   -1,
  270,  271,  272,   -1,   -1,  125,  256,  257,   -1,   -1,
  260,   -1,   -1,  263,  264,  265,   -1,  267,  125,   -1,
  270,  271,  272,   -1,   -1,   -1,  256,  257,   -1,   -1,
  260,   -1,   -1,  263,  264,  265,   -1,  267,  256,   -1,
  270,  271,  272,  256,  262,  263,  264,  265,  256,  262,
  263,  264,  265,   -1,  272,  263,  264,  265,   -1,  272,
   -1,   -1,   -1,   -1,  272,  256,  257,   -1,   -1,  260,
   -1,  262,  263,  264,  265,   -1,  267,   -1,   -1,  270,
  271,  272,   -1,  256,  257,   -1,   -1,  260,   -1,  262,
  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,
  256,  257,   -1,   -1,  260,   -1,   -1,  263,  264,  265,
   -1,  267,   -1,   -1,  270,  271,  272,   71,   72,  256,
  257,   -1,   -1,  260,   -1,   -1,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,  256,   -1,   92,   93,
   -1,  261,  262,  263,  264,  265,   -1,  101,  268,  256,
   71,   72,  272,  107,  261,  262,  263,  264,  265,   -1,
   -1,  268,   -1,   -1,   -1,  272,   -1,   -1,   -1,  123,
  124,   -1,   93,   27,   28,   -1,   30,   31,   -1,   -1,
  101,   -1,    0,   -1,   -1,   -1,  107,   41,   -1,   -1,
   44,    9,   -1,   11,   48,   -1,   -1,   -1,   52,   -1,
   -1,   -1,  123,  124,   -1,   23,   -1,   -1,   -1,   27,
   28,  165,   -1,  167,  168,   33,   -1,   71,   72,   -1,
   -1,   -1,   -1,   41,   -1,   -1,   44,   -1,   -1,   -1,
   -1,   -1,   50,   -1,   -1,   -1,   -1,   -1,   -1,   93,
   -1,   -1,   -1,   -1,  165,   -1,  167,  168,   -1,   -1,
   68,   -1,   -1,  107,
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
"sentencia_seleccion : condicion_if cuerpo_then END_IF ','",
"sentencia_seleccion : condicion_if cuerpo_then cuerpo_else END_IF ','",
"sentencia_seleccion : condicion_if cuerpo_then END_IF",
"sentencia_seleccion : condicion_if cuerpo_then cuerpo_else END_IF",
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
"sentencia_iteracion : inicio_do bloque_sentencias WHILE '(' comparacion ')'",
"sentencia_iteracion : inicio_do bloque_sentencias WHILE '(' ')'",
"inicio_do : DO",
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
"asignacion : llamado_clase '=' operacion ';'",
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

//#line 498 ".\gramatica.y"


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
//#line 17 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que terminar con \'}\'", anLex.getLinea()));
         }
break;
case 3:
//#line 20 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que arrancar con \'{\'", anLex.getLinea()));
         }
break;
case 4:
//#line 23 ".\gramatica.y"
{
                errores.add(new Error("El programa tiene que estar contenido en \'{\' \'}\'", anLex.getLinea()));
        }
break;
case 10:
//#line 37 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if((val_peek(1).sval != null)){
                                if(!tablaSimbolos.agregarHerencia(val_peek(2).sval,val_peek(1).sval)){
                                        errores.add(new Error("No esta declarada la clase " + val_peek(1).sval.substring(0,val_peek(1).sval.lastIndexOf(":")), anLex.getLinea())); 
                                }
                        }
                    }
break;
case 11:
//#line 45 ".\gramatica.y"
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
case 12:
//#line 56 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 13:
//#line 59 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 14:
//#line 63 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 15:
//#line 67 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 16:
//#line 73 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        yyval.sval = val_peek(0).sval + ambito;
                        ambito += ":" + val_peek(0).sval;
                 }
break;
case 17:
//#line 82 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        ambito += ":" + val_peek(0).sval;
                     }
break;
case 18:
//#line 90 ".\gramatica.y"
{
                        yyval.sval = null;
                }
break;
case 19:
//#line 93 ".\gramatica.y"
{
                        yyval.sval = val_peek(2).sval + ":main";
                }
break;
case 20:
//#line 96 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 21:
//#line 99 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                }
break;
case 22:
//#line 102 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 24:
//#line 108 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 25:
//#line 111 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                }
break;
case 26:
//#line 114 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
break;
case 33:
//#line 129 ".\gramatica.y"
{
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
break;
case 34:
//#line 132 ".\gramatica.y"
{
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
break;
case 35:
//#line 137 ".\gramatica.y"
{
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 36:
//#line 140 ".\gramatica.y"
{
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 37:
//#line 143 ".\gramatica.y"
{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 38:
//#line 147 ".\gramatica.y"
{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 39:
//#line 151 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 40:
//#line 155 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 41:
//#line 159 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 42:
//#line 163 ".\gramatica.y"
{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
break;
case 44:
//#line 170 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                     }
break;
case 45:
//#line 175 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 46:
//#line 178 ".\gramatica.y"
{
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 47:
//#line 181 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 48:
//#line 185 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
break;
case 49:
//#line 191 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,val_peek(1).sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        ambito += ":" + val_peek(0).sval;
                   }
break;
case 50:
//#line 199 ".\gramatica.y"
{
                        if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                 }
break;
case 52:
//#line 207 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 53:
//#line 210 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
break;
case 54:
//#line 213 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 65:
//#line 235 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                  }
break;
case 67:
//#line 241 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                   }
break;
case 68:
//#line 244 ".\gramatica.y"
{
                        errores.add(new Error("Cadena mal definida", anLex.getLinea()));
                   }
break;
case 69:
//#line 247 ".\gramatica.y"
{
                        errores.add(new Error("Cadena mal definida", anLex.getLinea()));
                   }
break;
case 70:
//#line 250 ".\gramatica.y"
{
                        errores.add(new Error("No existe esa expresion para imprimir la cadena", anLex.getLinea()));
                   }
break;
case 71:
//#line 255 ".\gramatica.y"
{
                        polaca.add(pila.pop(),"[" + String.valueOf(polaca.size() + 1) + "]");
                    }
break;
case 72:
//#line 258 ".\gramatica.y"
{
                        polaca.add(pila.pop(),"[" + String.valueOf(polaca.size() + 1) + "]");
                    }
break;
case 73:
//#line 261 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 74:
//#line 264 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 75:
//#line 267 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
break;
case 76:
//#line 270 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
break;
case 77:
//#line 275 ".\gramatica.y"
{
                pila.push(polaca.size());
                polaca.add("BF"); /*bifurcacion por falso;*/
            }
break;
case 78:
//#line 279 ".\gramatica.y"
{
                errores.add(new Error("Falta declarar condicion del IF ubicado", anLex.getLinea()));
            }
break;
case 79:
//#line 283 ".\gramatica.y"
{
                polaca.add(pila.pop(), "[" + String.valueOf(polaca.size() + 3) + "]");
                pila.push(polaca.size());
                polaca.add("BI"); /*bifurcacion incondicional;*/
            }
break;
case 80:
//#line 288 ".\gramatica.y"
{
                polaca.add(pila.pop(), "[" + String.valueOf(polaca.size() + 3) + "]");
                pila.push(polaca.size());
                polaca.add("BI"); /*bifurcacion incondicional;*/
            }
break;
case 84:
//#line 300 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 85:
//#line 303 ".\gramatica.y"
{
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
break;
case 86:
//#line 306 ".\gramatica.y"
{
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
break;
case 89:
//#line 315 ".\gramatica.y"
{
                polaca.add(val_peek(1).sval);
            }
break;
case 90:
//#line 318 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 91:
//#line 321 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 92:
//#line 324 ".\gramatica.y"
{
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
break;
case 93:
//#line 329 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 94:
//#line 332 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 95:
//#line 335 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 96:
//#line 338 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 97:
//#line 341 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 98:
//#line 344 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval;
           }
break;
case 99:
//#line 347 ".\gramatica.y"
{
                errores.add(new Error("Comparacion mal definida", anLex.getLinea()));
           }
break;
case 100:
//#line 352 ".\gramatica.y"
{
                        polaca.add("[" + String.valueOf(pila.pop()) + "]");
                        polaca.add("BF");
                    }
break;
case 101:
//#line 356 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 102:
//#line 359 ".\gramatica.y"
{
                        errores.add(new Error("No se declaro una condicion de corte en el WHILE que se ubica", anLex.getLinea()));
                    }
break;
case 103:
//#line 364 ".\gramatica.y"
{
                pila.push(polaca.size());

             }
break;
case 108:
//#line 374 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 110:
//#line 378 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 112:
//#line 382 ".\gramatica.y"
{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
break;
case 113:
//#line 387 ".\gramatica.y"
{
                yyval.sval = val_peek(0).sval; 
              }
break;
case 114:
//#line 390 ".\gramatica.y"
{
                yyval.sval += "." + val_peek(0).sval;
              }
break;
case 115:
//#line 395 ".\gramatica.y"
{
                if(!tablaSimbolos.existeVariable(val_peek(3).sval,ambito)){
                        errores.add(new Error("No se declaro la variable " + val_peek(3).sval + "en el ambito reconocible", anLex.getLinea()));
                }
                polaca.add(val_peek(3).sval);polaca.add("=");
           }
break;
case 116:
//#line 401 ".\gramatica.y"
{
                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
           }
break;
case 117:
//#line 404 ".\gramatica.y"
{
                errores.add(new Error("Se esperaba una \',\' y se encontro un \';\'", anLex.getLinea()));
           }
break;
case 118:
//#line 407 ".\gramatica.y"
{
                errores.add(new Error("No se puede declarar y asignar en la misma línea", anLex.getLinea()));
           }
break;
case 119:
//#line 413 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
            }
break;
case 120:
//#line 418 ".\gramatica.y"
{
                if(!tablaSimbolos.agregarAmbito(val_peek(0).sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
            }
break;
case 122:
//#line 426 ".\gramatica.y"
{
                polaca.add("+");
          }
break;
case 123:
//#line 429 ".\gramatica.y"
{
                polaca.add("-");
          }
break;
case 125:
//#line 435 ".\gramatica.y"
{
                polaca.add("*");
        }
break;
case 126:
//#line 438 ".\gramatica.y"
{
                polaca.add("/");
        }
break;
case 130:
//#line 448 ".\gramatica.y"
{
                        polaca.add(val_peek(1).sval);polaca.add("1");polaca.add("-");{polaca.add(val_peek(1).sval);polaca.add("=");}
                 }
break;
case 131:
//#line 453 ".\gramatica.y"
{
                polaca.add(val_peek(0).sval);
             }
break;
case 132:
//#line 456 ".\gramatica.y"
{
                anLex.convertirNegativo(val_peek(0).sval);
                polaca.add("-" + val_peek(0).sval);
             }
break;
case 133:
//#line 460 ".\gramatica.y"
{
                polaca.add(val_peek(0).sval);
             }
break;
case 134:
//#line 463 ".\gramatica.y"
{
                anLex.convertirNegativo(val_peek(0).sval);
                polaca.add("-" + val_peek(0).sval);
             }
break;
case 135:
//#line 467 ".\gramatica.y"
{
                if(CheckRangoLong(val_peek(0).sval)){
                        errores.add(new Error("LONG fuera de rango", anLex.getLinea()));}
                else {
                        polaca.add(val_peek(0).sval);
                } 
             }
break;
case 136:
//#line 474 ".\gramatica.y"
{
                polaca.add(val_peek(0).sval);
             }
break;
case 137:
//#line 477 ".\gramatica.y"
{
                errores.add(new Error("Las variables tipo UINT no pueden ser negativas", anLex.getLinea()));
             }
break;
case 138:
//#line 482 ".\gramatica.y"
{
        tipo = "DOUBLE";
     }
break;
case 139:
//#line 485 ".\gramatica.y"
{
        tipo = "UINT";
     }
break;
case 140:
//#line 488 ".\gramatica.y"
{
        tipo = "Long";
     }
break;
case 142:
//#line 492 ".\gramatica.y"
{
        errores.add(new Error("Tipo no reconocido", anLex.getLinea()));
     }
break;
//#line 1494 "Parser.java"
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
