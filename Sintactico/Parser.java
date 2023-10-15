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

//#line 23 "Parser.java"




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
    4,    4,    4,    4,    4,    6,    6,    8,    8,    7,
    7,    7,    7,    9,    9,    9,    9,    9,    9,    9,
    9,    3,    3,    5,    5,    5,    5,   10,   14,   14,
   13,   13,   13,   13,   15,   15,   15,   15,   15,   20,
   20,   19,   19,   19,   19,   19,   17,   17,   17,   17,
   17,   17,   17,   17,   22,   22,   21,   21,   21,   21,
   24,   24,   24,   24,   24,   24,   24,   18,   18,   18,
   18,   16,   16,   16,   16,   16,   16,   16,   16,   26,
   26,   25,   25,   25,   12,   12,   23,   23,   23,   27,
   27,   27,   27,   28,   28,   30,   29,   29,   29,   29,
   29,   29,   29,   11,   11,   11,   11,   11,
};
final static short yylen[] = {                            2,
    3,    2,    2,    1,    1,    2,    1,    1,    1,    5,
    4,    7,    6,    5,    4,    1,    2,    1,    1,    1,
    2,    1,    2,    5,    6,    4,    5,    3,    4,    2,
    3,    3,    2,    6,    7,    5,    6,    2,    1,    2,
    3,    3,    2,    2,    1,    1,    1,    1,    1,    2,
    1,    3,    2,    3,    2,    3,    7,    9,    6,    8,
    6,    8,    5,    7,    1,    1,    3,    2,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    7,    6,    6,
    5,    1,    1,    4,    3,    5,    4,    5,    4,    1,
    3,    4,    3,    5,    1,    3,    1,    3,    3,    1,
    3,    3,    3,    1,    1,    2,    1,    2,    1,    2,
    1,    1,    2,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
  118,    0,    0,  116,  115,  114,    0,  117,    0,    0,
    0,    5,    7,    8,    9,    0,    0,    0,    0,    0,
    3,    6,   90,    0,    0,    0,    0,    0,    0,    1,
   32,    0,    0,    0,   11,   18,   19,    0,   16,    0,
    0,    0,    0,   15,   22,    0,   20,    0,   91,    0,
   10,   17,    0,    0,    0,    0,   38,    0,   14,   23,
   21,   13,    0,    0,    0,    0,    0,    0,    0,    0,
   43,   82,    0,    0,    0,   39,   45,   46,   47,   48,
   49,   83,    0,   44,    0,   34,    0,    0,   28,    0,
   12,    0,    0,    0,    0,    0,   50,    0,    0,    0,
   41,   40,    0,   42,   35,    0,    0,   29,   56,   77,
  107,  111,  112,  109,    0,    0,   71,   72,   73,   74,
   75,   76,    0,    0,    0,    0,    0,  100,    0,  105,
   54,   52,    0,    0,    0,    0,    0,    0,   24,    0,
    0,   66,   65,    0,  110,  113,  108,    0,    0,    0,
    0,    0,    0,    0,  106,    0,    0,    0,   92,   84,
    0,   25,  103,    0,    0,    0,    0,    0,    0,  101,
  102,    0,    0,   88,   94,   86,    0,   61,    0,    0,
   80,    0,    0,    0,   57,   78,   62,    0,   58,
};
final static short yydgoto[] = {                         10,
   11,   12,   72,   14,   15,   38,   46,   39,   47,   41,
   16,   74,  142,   75,  143,   77,   78,   79,   80,   81,
  124,  144,  125,  126,   82,   83,  127,  128,  129,  130,
};
final static short yysindex[] = {                       248,
    0, -216, -209,    0,    0,    0, -156,    0, -158,    0,
  537,    0,    0,    0,    0, -150, -115,   95,   16,  645,
    0,    0,    0,    6,   94, -129,  402,  234, -121,    0,
    0, -150, -128,   34,    0,    0,    0,  662,    0,  -31,
  115, -103,  -99,    0,    0, -120,    0,   94,    0,  666,
    0,    0,  608,  571,  119,  -31,    0,  230,    0,    0,
    0,    0,  679,  -96,  137, -204,  -31,  139,  149,    0,
    0,    0, -150,   41,  625,    0,    0,    0,    0,    0,
    0,    0,   36,    0,  590,    0,  153,  309,    0,  155,
    0,  156,  -38,  159,  163,  -58,    0,   85,   -4,   85,
    0,    0,  170,    0,    0,  -15,  168,    0,    0,    0,
    0,    0,    0,    0,   85,  193,    0,    0,    0,    0,
    0,    0, -178,  171,  136,   85,   42,    0,  -68,    0,
    0,    0,  174,   32,   85,   89,  172,   70,    0,  -14,
  123,    0,    0, -220,    0,    0,    0,  193,   85,   85,
   85,  145,  106,  106,    0,  -28,  173,  142,    0,    0,
  175,    0,    0,  193,  176, -172,   42,   42,  145,    0,
    0,  178,  183,    0,    0,    0,  -34,    0,  193,  182,
    0,  185,  187,  -32,    0,    0,    0,  194,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  243,    0,    0,    0,    0,    0,    0,    0,    0,  254,
    0,    0,    0,   33,    1,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   20,    0,    0,
    0,    0,    0,    0,   46,    0,    0, -110,    0,    0,
    0,    0,    0,  -24,    0,    0,    0,  212,    0,  -40,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  133,    0,    0,    0,   59,    0,    0, -109,
    0,    0,    0,  272,  297,    0,    0,    0,  326,    0,
    0,    0,    0,    0,    0, -107,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  214,  105,    0,   76,    0,
    0,    0,    0,    0,    0,  363,  382,    0,    0, -102,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  226,  252,    0,    0,    0,    0,  413,    0,    0,    0,
  433,    0,    0,    0,  450,    0,  131,  161,  258,    0,
    0,  467,    0,    0,    0,    0,    0,    0,    0,  484,
    0,  504,  525,    0,    0,    0,    0,  554,    0,
};
final static short yygindex[] = {                         0,
  291,   58,   74,    0,  132,  253,    0,  -10,  260,  -44,
  -17,   -6,  -13,  250,   -5,    0,    0,    0,    0,    0,
  169, -113,  847,  192,    0,   15,  -59,  -25,    0,    0,
};
final static int YYTABLESIZE=998;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         90,
   95,  115,  116,   44,   59,   90,  123,   27,   54,   24,
   42,  115,  172,   90,   30,   31,  123,   26,   90,   96,
   90,  117,   27,  118,   54,   54,   55,   52,  139,  162,
   25,  117,   33,  118,  166,   73,   73,  164,  165,   31,
   42,   95,   87,  107,   95,   36,   48,   76,   76,   31,
  177,   94,   52,   96,   32,   17,  135,   73,   37,   95,
   96,   95,   18,   96,   32,  184,   99,   73,   22,  102,
   42,   95,  157,   13,  149,  103,  150,   22,   96,  102,
   96,   33,   13,  153,   13,  179,  180,   25,  154,  167,
  168,   53,   55,   13,  145,  146,  147,    1,   73,   32,
   36,  100,    2,    3,    4,    5,    6,   53,   53,    7,
  161,   36,  149,    8,  150,   19,  104,  104,  104,  104,
  104,   23,  104,   36,  115,   95,   87,  170,  171,  123,
   73,  149,  159,  150,   28,  104,   36,  104,   29,   33,
   43,   43,   34,   49,   96,   97,   73,   97,   97,   97,
  123,   30,   31,   26,   26,   56,   50,   33,   37,   27,
   45,   73,   86,  163,   97,  149,   97,  150,   57,   37,
   36,   98,   58,   98,   98,   98,   93,   60,  149,   92,
  150,   37,   97,   37,  149,  175,  150,  149,   98,  150,
   98,   95,   98,   95,   37,  117,  105,  118,  108,  109,
  104,   99,  131,   99,   99,   99,  132,  133,  140,  115,
  137,  148,  155,  156,  123,  160,  174,  110,  176,  178,
   99,  181,   99,  182,  183,  185,  188,  110,  186,   97,
  187,  117,   54,  111,  112,  113,  114,  189,  119,  120,
  121,  122,    4,  111,  112,  113,  114,  118,  119,  120,
  121,  122,   51,    2,   70,   98,   95,   95,   95,   95,
   95,   95,   95,   95,   95,   95,   68,   95,   95,   88,
   95,   95,   95,   89,   40,   96,   96,   96,   96,   96,
   96,   96,   96,   96,   96,   99,   96,   96,   33,   96,
   96,   96,   69,   33,   33,   33,   33,   33,   67,   20,
   33,   36,   63,   85,   33,   61,   36,   36,   36,   36,
   36,    0,   55,   36,   37,   53,  151,   36,    0,   37,
   37,   37,   37,   37,  173,    0,   37,    0,    0,    0,
   37,  104,  104,  104,  104,  104,   51,   53,  104,  104,
  104,    0,  104,    0,    0,  104,  104,  104,    0,  106,
    0,    0,  104,  104,  104,  104,  111,  112,  113,  114,
   97,   97,   97,   97,   97,    0,   33,   97,   97,   97,
    9,   97,    0,    0,   97,   97,   97,  111,  112,  113,
  114,   97,   97,   97,   97,    0,   98,   98,   98,   98,
   98,  110,    0,   98,   98,   98,   55,   98,    0,    0,
   98,   98,   98,   93,    0,    0,    0,   98,   98,   98,
   98,    0,  119,  120,  121,  122,   99,   99,   99,   99,
   99,   53,   85,   99,   99,   99,    0,   99,    0,    0,
   99,   99,   99,    0,    0,    0,    0,   99,   99,   99,
   99,  111,  112,  113,  114,    0,    0,    0,   64,   65,
   33,    0,   66,   89,    0,    4,    5,    6,    0,   67,
    0,    0,   68,   69,   70,    0,    0,   51,   51,   51,
   51,   51,    0,   87,   51,   51,   51,    0,   51,    0,
    0,   51,   51,   51,    0,    1,    0,   93,    0,    1,
   63,    0,    4,    5,    6,    0,    4,    5,    6,    0,
    0,    8,    0,    1,    0,    8,   85,   81,    2,    3,
    4,    5,    6,    0,    0,    7,    0,    0,    0,    8,
    0,    0,    0,    0,   59,    0,   35,   55,   55,   55,
   55,   55,    0,    0,   55,   55,   55,   89,   55,    0,
    0,   55,   55,   55,   79,    0,    0,    0,    0,    0,
    0,    0,   53,   53,   53,   53,   53,   87,    0,   53,
   53,   53,    0,   53,    1,   64,   53,   53,   53,    0,
    0,    4,    5,    6,   63,    0,    0,    0,    0,    0,
    8,   33,   33,   33,   33,   33,    0,    0,   33,   33,
   33,   81,   33,    0,   60,   33,   33,   33,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   59,    0,
    0,   84,    0,    0,    0,    0,    0,    0,   93,   93,
   93,   93,   93,    0,    0,   93,   93,   93,   79,   93,
  104,    0,   93,   93,   93,    0,    0,   85,   85,   85,
   85,   85,    0,    0,   85,   85,   85,    0,   85,   64,
    0,   85,   85,   85,    0,    0,    0,    1,    0,    0,
    0,   21,    0,    3,    4,    5,    6,    0,   89,   89,
   89,   89,   89,    8,    0,   89,   89,   89,   60,   89,
    0,    0,   89,   89,   89,    0,    0,    0,   87,   87,
   87,   87,   87,    0,    0,   87,   87,   87,    0,   87,
    0,    0,   87,   87,   87,   63,   63,   63,   63,   63,
    0,    0,   63,   63,   63,    0,   63,    0,    0,   63,
   63,   63,   81,   81,   81,   81,   81,    0,    0,   81,
   81,   81,   71,   81,    0,    0,   81,   81,   81,   59,
   59,   59,   59,   59,    0,    0,   59,   59,   59,  101,
   59,    0,    0,   59,   59,   59,    0,    0,    0,   79,
   79,   79,   79,   79,    0,    0,   79,   79,   79,   30,
   79,    0,    0,   79,   79,   79,    0,    0,    0,    0,
   64,   64,   64,   64,   64,    0,   51,   64,   64,   64,
   62,   64,    1,    0,   64,   64,   64,    2,    3,    4,
    5,    6,    0,   91,    7,    0,    0,    0,    8,   60,
   60,   60,   60,   60,    0,    0,   60,   60,   60,    0,
   60,    0,    0,   60,   60,   60,   64,   65,    0,    0,
   66,    0,    0,    4,    5,    6,    0,   67,    0,    0,
   68,   69,   70,    0,    0,   64,   65,    0,    0,   66,
    0,    0,    4,    5,    6,    0,   67,    0,    0,   68,
   69,   70,    0,   64,   65,    0,    0,   66,    0,    0,
    4,    5,    6,    0,   67,    0,    0,   68,   69,   70,
   64,   65,    0,    0,   66,    0,    0,    4,    5,    6,
    0,   67,    0,    0,   68,   69,   70,    0,    0,    0,
    1,    0,    0,    0,    0,    2,    3,    4,    5,    6,
    0,    0,    7,    0,    0,    0,    8,    1,    0,    0,
    0,    1,    0,    3,    4,    5,    6,    3,    4,    5,
    6,    0,    0,    8,    1,    0,    0,    8,    0,    0,
    3,    4,    5,    6,  134,    0,  136,    0,    0,  138,
    8,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  141,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  152,    0,    0,    0,    0,    0,    0,    0,
    0,  158,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  169,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   41,  125,  125,   46,   45,  123,   40,   16,
   28,   40,   41,   58,  125,  125,   45,  125,   59,    0,
   61,   60,  125,   62,   40,   40,   40,   38,   44,   44,
   16,   60,    0,   62,  148,   53,   54,  258,  259,   44,
   58,   41,   56,   88,   44,    0,   32,   53,   54,   44,
  164,  256,   63,   67,   59,  272,   61,   75,    0,   59,
   41,   61,  272,   44,   59,  179,   73,   85,   11,   75,
   88,  276,   41,    0,   43,   40,   45,   20,   59,   85,
   61,   46,    9,   42,   11,  258,  259,   73,   47,  149,
  150,  123,  106,   20,  273,  274,  275,  256,  116,   59,
   27,   61,  261,  262,  263,  264,  265,  123,  123,  268,
   41,   38,   43,  272,   45,  272,   41,   42,   43,   44,
   45,  272,   47,   50,   40,  125,  140,  153,  154,   45,
  148,   43,   44,   45,   40,   60,   63,   62,  123,   46,
  262,  262,  272,  272,  125,   41,  164,   43,   44,   45,
   45,  262,  262,  269,  262,   41,  123,  125,   27,  262,
   29,  179,   44,   41,   60,   43,   62,   45,  272,   38,
  125,   41,  272,   43,   44,   45,   40,   46,   43,  276,
   45,   50,   44,  125,   43,   44,   45,   43,   40,   45,
   60,   59,   62,   61,   63,   60,   44,   62,   44,   44,
  125,   41,   44,   43,   44,   45,   44,  266,   41,   40,
   41,   41,  281,   40,   45,   44,   44,  256,   44,   44,
   60,   44,   62,   41,  259,   44,  259,  256,   44,  125,
   44,  272,   40,  272,  273,  274,  275,   44,  277,  278,
  279,  280,    0,  272,  273,  274,  275,  272,  277,  278,
  279,  280,   41,    0,   41,  125,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,   41,  267,  268,   40,
  270,  271,  272,   44,   41,  256,  257,  258,  259,  260,
  261,  262,  263,  264,  265,  125,  267,  268,  256,  270,
  271,  272,   41,  261,  262,  263,  264,  265,   41,    9,
  268,  256,   50,   54,  272,   46,  261,  262,  263,  264,
  265,   -1,   41,  268,  256,  123,  125,  272,   -1,  261,
  262,  263,  264,  265,  156,   -1,  268,   -1,   -1,   -1,
  272,  256,  257,  258,  259,  260,  125,   41,  263,  264,
  265,   -1,  267,   -1,   -1,  270,  271,  272,   -1,   41,
   -1,   -1,  277,  278,  279,  280,  272,  273,  274,  275,
  256,  257,  258,  259,  260,   -1,   41,  263,  264,  265,
  123,  267,   -1,   -1,  270,  271,  272,  272,  273,  274,
  275,  277,  278,  279,  280,   -1,  256,  257,  258,  259,
  260,  256,   -1,  263,  264,  265,  125,  267,   -1,   -1,
  270,  271,  272,   41,   -1,   -1,   -1,  277,  278,  279,
  280,   -1,  277,  278,  279,  280,  256,  257,  258,  259,
  260,  125,   41,  263,  264,  265,   -1,  267,   -1,   -1,
  270,  271,  272,   -1,   -1,   -1,   -1,  277,  278,  279,
  280,  272,  273,  274,  275,   -1,   -1,   -1,  256,  257,
  125,   -1,  260,   41,   -1,  263,  264,  265,   -1,  267,
   -1,   -1,  270,  271,  272,   -1,   -1,  256,  257,  258,
  259,  260,   -1,   41,  263,  264,  265,   -1,  267,   -1,
   -1,  270,  271,  272,   -1,  256,   -1,  125,   -1,  256,
   41,   -1,  263,  264,  265,   -1,  263,  264,  265,   -1,
   -1,  272,   -1,  256,   -1,  272,  125,   41,  261,  262,
  263,  264,  265,   -1,   -1,  268,   -1,   -1,   -1,  272,
   -1,   -1,   -1,   -1,   41,   -1,  125,  256,  257,  258,
  259,  260,   -1,   -1,  263,  264,  265,  125,  267,   -1,
   -1,  270,  271,  272,   41,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,  257,  258,  259,  260,  125,   -1,  263,
  264,  265,   -1,  267,  256,   41,  270,  271,  272,   -1,
   -1,  263,  264,  265,  125,   -1,   -1,   -1,   -1,   -1,
  272,  256,  257,  258,  259,  260,   -1,   -1,  263,  264,
  265,  125,  267,   -1,   41,  270,  271,  272,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,
   -1,   41,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,
  258,  259,  260,   -1,   -1,  263,  264,  265,  125,  267,
   41,   -1,  270,  271,  272,   -1,   -1,  256,  257,  258,
  259,  260,   -1,   -1,  263,  264,  265,   -1,  267,  125,
   -1,  270,  271,  272,   -1,   -1,   -1,  256,   -1,   -1,
   -1,  125,   -1,  262,  263,  264,  265,   -1,  256,  257,
  258,  259,  260,  272,   -1,  263,  264,  265,  125,  267,
   -1,   -1,  270,  271,  272,   -1,   -1,   -1,  256,  257,
  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,  267,
   -1,   -1,  270,  271,  272,  256,  257,  258,  259,  260,
   -1,   -1,  263,  264,  265,   -1,  267,   -1,   -1,  270,
  271,  272,  256,  257,  258,  259,  260,   -1,   -1,  263,
  264,  265,  125,  267,   -1,   -1,  270,  271,  272,  256,
  257,  258,  259,  260,   -1,   -1,  263,  264,  265,  125,
  267,   -1,   -1,  270,  271,  272,   -1,   -1,   -1,  256,
  257,  258,  259,  260,   -1,   -1,  263,  264,  265,  125,
  267,   -1,   -1,  270,  271,  272,   -1,   -1,   -1,   -1,
  256,  257,  258,  259,  260,   -1,  125,  263,  264,  265,
  125,  267,  256,   -1,  270,  271,  272,  261,  262,  263,
  264,  265,   -1,  125,  268,   -1,   -1,   -1,  272,  256,
  257,  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,  256,  257,   -1,   -1,
  260,   -1,   -1,  263,  264,  265,   -1,  267,   -1,   -1,
  270,  271,  272,   -1,   -1,  256,  257,   -1,   -1,  260,
   -1,   -1,  263,  264,  265,   -1,  267,   -1,   -1,  270,
  271,  272,   -1,  256,  257,   -1,   -1,  260,   -1,   -1,
  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,  272,
  256,  257,   -1,   -1,  260,   -1,   -1,  263,  264,  265,
   -1,  267,   -1,   -1,  270,  271,  272,   -1,   -1,   -1,
  256,   -1,   -1,   -1,   -1,  261,  262,  263,  264,  265,
   -1,   -1,  268,   -1,   -1,   -1,  272,  256,   -1,   -1,
   -1,  256,   -1,  262,  263,  264,  265,  262,  263,  264,
  265,   -1,   -1,  272,  256,   -1,   -1,  272,   -1,   -1,
  262,  263,  264,  265,   98,   -1,  100,   -1,   -1,  103,
  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  115,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  126,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  135,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  151,
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
"declaracion_clase : CLASS ID '{' cuerpo_clase '}'",
"declaracion_clase : CLASS ID '{' '}'",
"declaracion_clase : CLASS ID IMPLEMENT ID '{' cuerpo_clase '}'",
"declaracion_clase : CLASS ID IMPLEMENT ID '{' '}'",
"declaracion_clase : INTERFACE ID '{' cuerpo_interfaz '}'",
"declaracion_clase : INTERFACE ID '{' '}'",
"cuerpo_clase : miembro_clase",
"cuerpo_clase : cuerpo_clase miembro_clase",
"miembro_clase : declaracion_variable",
"miembro_clase : declaracion_funcion",
"cuerpo_interfaz : declaracion_funcion_vacia",
"cuerpo_interfaz : cuerpo_interfaz declaracion_funcion_vacia",
"cuerpo_interfaz : declaracion_funcion",
"cuerpo_interfaz : cuerpo_interfaz declaracion_funcion",
"declaracion_funcion_vacia : VOID ID '(' ')' ','",
"declaracion_funcion_vacia : VOID ID '(' parametro_formal ')' ','",
"declaracion_funcion_vacia : VOID ID '(' ')'",
"declaracion_funcion_vacia : VOID ID '(' parametro_formal ')'",
"declaracion_funcion_vacia : VOID ID ','",
"declaracion_funcion_vacia : VOID ID parametro_formal ','",
"declaracion_funcion_vacia : VOID ID",
"declaracion_funcion_vacia : VOID ID parametro_formal",
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
"termino : '(' operacion ')'",
"factor : factor_comun",
"factor : factor_inmediato",
"factor_inmediato : factor_comun \"--\"",
"factor_comun : ID",
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

//#line 194 ".\gramatica.y"


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
          System.out.println(token);
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
//#line 619 "Parser.java"
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
//#line 14 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El programa tiene que terminar con \'}\'", analizadorLex.getLineaArchivo()));}
break;
case 3:
//#line 15 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El programa tiene que arrancar con \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 4:
//#line 16 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El programa tiene que estar contenido en \'{\' \'}\'", analizadorLex.getLineaArchivo()));}
break;
case 10:
//#line 28 ".\gramatica.y"
{System.out.println("CREACION DE CLASE" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 11:
//#line 29 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Error clase vacia", analizadorLex.getLineaArchivo()));}
break;
case 12:
//#line 30 ".\gramatica.y"
{System.out.println("CREACION DE CLASE CON HERENCIA" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 13:
//#line 31 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Error clase vacia", analizadorLex.getLineaArchivo()));}
break;
case 14:
//#line 32 ".\gramatica.y"
{System.out.println("CREACION DE INTERFAZ" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 15:
//#line 33 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Error interfaz vacia", analizadorLex.getLineaArchivo()));}
break;
case 22:
//#line 46 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));}
break;
case 23:
//#line 47 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));}
break;
case 24:
//#line 50 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION VACIA SIN PARAMETROS" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 25:
//#line 51 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION VACIA" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 26:
//#line 52 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 27:
//#line 53 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 28:
//#line 54 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
break;
case 29:
//#line 55 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
break;
case 30:
//#line 56 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
break;
case 31:
//#line 57 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
break;
case 32:
//#line 60 ".\gramatica.y"
{System.out.println("DECLARACION DE VARIABLE" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 33:
//#line 61 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 34:
//#line 64 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION SIN PARAMETROS" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 35:
//#line 65 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 36:
//#line 66 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 37:
//#line 67 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 42:
//#line 78 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 43:
//#line 79 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 44:
//#line 80 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 46:
//#line 84 ".\gramatica.y"
{System.out.println("SENTENCIA IF" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 47:
//#line 85 ".\gramatica.y"
{System.out.println("SENTENCIA ITERACION" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 48:
//#line 86 ".\gramatica.y"
{System.out.println("SENTENCIA IMPRESION DE CADENA" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 49:
//#line 87 ".\gramatica.y"
{System.out.println("SENTENCIA RETURN" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 51:
//#line 91 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 53:
//#line 95 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 54:
//#line 96 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
break;
case 55:
//#line 97 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
break;
case 56:
//#line 98 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No existe esa expresion para imprimir la cadena", analizadorLex.getLineaArchivo()));}
break;
case 59:
//#line 103 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 60:
//#line 104 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 61:
//#line 105 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
break;
case 62:
//#line 106 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
break;
case 63:
//#line 107 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
break;
case 64:
//#line 108 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
break;
case 68:
//#line 116 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 69:
//#line 117 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 70:
//#line 118 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 77:
//#line 127 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Comparacion mal definida", analizadorLex.getLineaArchivo()));}
break;
case 79:
//#line 131 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 80:
//#line 132 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte", analizadorLex.getLineaArchivo()));}
break;
case 81:
//#line 133 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte y falta una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 83:
//#line 137 ".\gramatica.y"
{System.out.println("ASIGNACION" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 84:
//#line 138 ".\gramatica.y"
{System.out.println("LLAMADO A METODO" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 85:
//#line 139 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 86:
//#line 140 ".\gramatica.y"
{System.out.println("LLAMADO A METODO" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 87:
//#line 141 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 89:
//#line 143 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 93:
//#line 151 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 94:
//#line 152 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se puede declarar y asignar en la misma línea", analizadorLex.getLineaArchivo()));}
break;
case 108:
//#line 178 ".\gramatica.y"
{analizadorLex.convertirNegativo(((LexemToken)token).getLexema());}
break;
case 110:
//#line 180 ".\gramatica.y"
{analizadorLex.convertirNegativo(((LexemToken)token).getLexema());}
break;
case 111:
//#line 181 ".\gramatica.y"
{if(CheckRangoLong(((LexemToken)token).getLexema())){analizadorLex.addErroresLexicos(new Error("Long fuera de rango", analizadorLex.getLineaArchivo()));}}
break;
case 113:
//#line 183 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Las variables tipo UINT no pueden ser negativas", analizadorLex.getLineaArchivo()));}
break;
case 118:
//#line 190 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Tipo no reconocido", analizadorLex.getLineaArchivo()));}
break;
//#line 1020 "Parser.java"
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
