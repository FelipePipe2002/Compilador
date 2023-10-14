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
    4,    4,    6,    6,    8,    8,    7,    7,    3,    3,
    5,    5,    5,    5,   12,   13,   13,   11,   11,   11,
   11,   14,   14,   14,   14,   14,   19,   19,   18,   18,
   18,   18,   18,   16,   16,   16,   16,   16,   16,   16,
   16,   21,   21,   20,   20,   20,   20,   23,   23,   23,
   23,   23,   23,   23,   17,   17,   17,   17,   15,   15,
   15,   15,   15,   15,   15,   15,   25,   25,   24,   24,
   24,   10,   10,   22,   22,   22,   26,   26,   26,   26,
   27,   27,   29,   28,   28,   28,   28,   28,   28,   28,
    9,    9,    9,    9,    9,
};
final static short yylen[] = {                            2,
    3,    2,    2,    1,    1,    2,    1,    1,    1,    5,
    7,    5,    1,    2,    1,    1,    1,    2,    3,    2,
    6,    7,    5,    6,    2,    1,    2,    3,    3,    2,
    2,    1,    1,    1,    1,    1,    2,    1,    3,    2,
    3,    2,    3,    7,    9,    6,    8,    6,    8,    5,
    7,    1,    1,    3,    2,    2,    1,    1,    1,    1,
    1,    1,    1,    1,    7,    6,    6,    5,    1,    1,
    4,    3,    5,    4,    5,    4,    1,    3,    4,    3,
    5,    1,    3,    1,    3,    3,    1,    3,    3,    3,
    1,    1,    2,    1,    2,    1,    2,    1,    1,    2,
    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
  105,    0,    0,  103,  102,  101,    0,  104,    0,    0,
    0,    5,    7,    8,    9,    0,    0,    0,    0,    0,
    3,    6,   77,    0,    0,    0,    0,    0,    0,    1,
   19,    0,    0,    0,   15,   16,    0,   13,    0,    0,
    0,   17,    0,    0,   78,    0,   10,   14,    0,    0,
    0,   25,    0,   12,   18,    0,    0,    0,    0,    0,
    0,    0,    0,   30,   69,    0,    0,    0,   26,   32,
   33,   34,   35,   36,   70,    0,   31,    0,   21,    0,
   11,    0,    0,    0,    0,    0,   37,    0,    0,    0,
   28,   27,    0,   29,   22,   43,   64,   94,   98,   99,
   96,    0,    0,   58,   59,   60,   61,   62,   63,    0,
    0,    0,    0,    0,   87,    0,   92,   41,   39,    0,
    0,    0,    0,    0,    0,    0,   53,   52,    0,   97,
  100,   95,    0,    0,    0,    0,    0,    0,    0,   93,
    0,    0,    0,   79,   71,    0,   90,    0,    0,    0,
    0,    0,    0,   88,   89,    0,    0,   75,   81,   73,
    0,   48,    0,    0,   67,    0,    0,    0,   44,   65,
   49,    0,   45,
};
final static short yydgoto[] = {                         10,
   11,   12,   65,   14,   15,   37,   43,   38,   16,   67,
  127,   41,   68,  128,   70,   71,   72,   73,   74,  111,
  129,  112,  113,   75,   76,  114,  115,  116,  117,
};
final static short yysindex[] = {                      -109,
    0, -263, -256,    0,    0,    0, -231,    0, -160,    0,
  395,    0,    0,    0,    0, -228, -112,   44,  -49,  537,
    0,    0,    0,  -21,   51, -166,  315,  309, -152,    0,
    0, -228, -150,    9,    0,    0,  603,    0,  -35, -148,
   93,    0, -121,   51,    0,  315,    0,    0,  246,  229,
   98,    0,  -35,    0,    0,  608, -132,  124, -221,  -35,
  122,  130,    0,    0,    0, -228,  -10,  591,    0,    0,
    0,    0,    0,    0,    0,   52,    0,  571,    0,  129,
    0,  133,  -38,  138,  143,  -78,    0,   85,   32,   85,
    0,    0,  170,    0,    0,    0,    0,    0,    0,    0,
    0,   85,  193,    0,    0,    0,    0,    0,    0, -247,
  148,  136,   85,   35,    0,  -91,    0,    0,    0,  154,
   66,   85,   71,  151,   86,   92,    0,    0, -164,    0,
    0,    0,  193,   85,   85,   85,   42,  106,  106,    0,
  -28,  153,  117,    0,    0,  155,    0,  193,  156, -119,
   35,   35,   42,    0,    0,  163,  167,    0,    0,    0,
  -50,    0,  193,  169,    0,  172,  173,  -45,    0,    0,
    0,  175,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  220,    0,    0,    0,    0,    0,    0,    0,    0,  222,
    0,    0,    0,   33,    1,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   20,    0,    0,    0,    0,    0,    0,
   46,    0,    0,    0,    0,    0,  -48,    0,    0,    0,
  212,    0,  -40,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   14,    0,    0,    0,   59,
    0,    0,    0,  272,  297,    0,    0,    0,  326,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  184,  105,    0,   76,    0,    0,    0,    0,
    0,    0,  363,  382,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  186,  188,    0,    0,    0,
    0,  413,    0,    0,    0,  433,    0,    0,  450,    0,
  131,  161,  190,    0,    0,  467,    0,    0,    0,    0,
    0,    0,    0,  484,    0,  504,  525,    0,    0,    0,
    0,  554,    0,
};
final static short yygindex[] = {                         0,
  234,   58,   43,    0,   10,  192,    0,   30,  -20,    2,
   -3,    0,  198,   22,    0,    0,    0,    0,    0,  113,
 -123,   90,  162,    0,   -1,   34,   47,    0,    0,
};
final static int YYTABLESIZE=880;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         77,
   82,  102,  103,   54,   50,   77,  110,   40,   17,  150,
   27,  102,  156,    9,   25,   18,  110,   24,   77,   83,
   77,  104,   31,  105,  161,  130,  131,  132,   66,   66,
   44,  104,   20,  105,   84,   51,   36,   32,   42,  168,
   19,   82,   13,   23,   82,   23,   36,   66,   32,   80,
   90,   13,   55,   13,   85,   36,   86,   66,   24,   82,
   83,   82,   13,   83,   25,   36,   48,   89,   22,   35,
   69,   69,   82,   29,   82,   31,  138,   22,   83,   35,
   83,  139,   66,   28,  134,   48,  135,   49,   35,   92,
   32,   93,  122,  148,  149,    1,   33,   33,   35,   92,
    2,    3,    4,    5,    6,   34,  142,    7,  134,    3,
  135,    8,   66,  134,  144,  135,   91,   91,   91,   91,
   91,   45,   91,   52,  102,   82,  146,   66,  134,  110,
  135,   46,  147,   53,  134,   91,  135,   91,  163,  164,
    3,   79,   66,   82,   83,   84,    1,   84,   84,   84,
  110,    2,    3,    4,    5,    6,   26,   20,    7,  134,
  159,  135,    8,   83,   84,   87,   84,  151,  152,   88,
   23,   85,   95,   85,   85,   85,   96,  121,  134,  123,
  135,  118,  125,   24,  154,  155,  119,  120,  133,  140,
   85,  126,   85,  141,  145,  104,  158,  105,  160,  162,
   91,   86,  137,   86,   86,   86,  165,  166,  167,  102,
  124,  143,  169,  172,  110,  170,  171,   97,  173,    4,
   86,    2,   86,  105,   57,  153,   55,   97,   56,   84,
   54,  104,   50,   98,   99,  100,  101,   56,  106,  107,
  108,  109,   20,   98,   99,  100,  101,   78,  106,  107,
  108,  109,   38,  157,    0,   85,   82,   82,   82,   82,
   82,   82,   82,   82,   82,   82,    0,   82,   82,   77,
   82,   82,   82,  136,    0,   83,   83,   83,   83,   83,
   83,   83,   83,   83,   83,   86,   83,   83,   20,   83,
   83,   83,    0,   20,   20,   20,   20,   20,    0,    0,
   20,   23,    0,    0,   20,    0,   23,   23,   23,   23,
   23,    0,   42,   23,   24,   49,    0,   23,    0,   24,
   24,   24,   24,   24,    0,    0,   24,    0,    0,    0,
   24,   91,   91,   91,   91,   91,   38,   40,   91,   91,
   91,    0,   91,    0,    0,   91,   91,   91,    0,   39,
    0,    0,   91,   91,   91,   91,   98,   99,  100,  101,
   84,   84,   84,   84,   84,    0,   20,   84,   84,   84,
   64,   84,    0,    0,   84,   84,   84,   98,   99,  100,
  101,   84,   84,   84,   84,    0,   85,   85,   85,   85,
   85,   97,    0,   85,   85,   85,   42,   85,    0,    0,
   85,   85,   85,   80,    0,    0,    0,   85,   85,   85,
   85,    0,  106,  107,  108,  109,   86,   86,   86,   86,
   86,   40,   72,   86,   86,   86,    0,   86,    0,    0,
   86,   86,   86,    0,    0,    0,    0,   86,   86,   86,
   86,   98,   99,  100,  101,    0,    0,    0,   57,   58,
   20,    0,   59,   76,    0,    4,    5,    6,    0,   60,
    0,    0,   61,   62,   63,    0,    0,   38,   38,   38,
   38,   38,    0,   74,   38,   38,   38,    0,   38,    0,
    0,   38,   38,   38,   57,   58,    0,   80,   59,    0,
   50,    4,    5,    6,    0,   60,    0,    0,   61,   62,
   63,   57,   58,    0,    0,   59,   72,   68,    4,    5,
    6,    0,   60,    0,    0,   61,   62,   63,    0,   21,
    0,    0,    0,    0,   46,    0,    0,   42,   42,   42,
   42,   42,    0,    0,   42,   42,   42,   76,   42,    0,
    0,   42,   42,   42,   66,    0,    0,    0,    0,    0,
    0,    0,   40,   40,   40,   40,   40,   74,    0,   40,
   40,   40,    0,   40,    1,   51,   40,   40,   40,    0,
    1,    4,    5,    6,   50,    0,    3,    4,    5,    6,
    8,   20,   20,   20,   20,   20,    8,    0,   20,   20,
   20,   68,   20,    0,   47,   20,   20,   20,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   46,    0,
    0,   94,    0,    0,    0,    0,    0,    0,   80,   80,
   80,   80,   80,    0,    0,   80,   80,   80,   66,   80,
    0,    0,   80,   80,   80,    0,    0,   72,   72,   72,
   72,   72,    0,    0,   72,   72,   72,    0,   72,   51,
    1,   72,   72,   72,    0,    2,    3,    4,    5,    6,
    0,   30,    7,    0,    0,    0,    8,    0,   76,   76,
   76,   76,   76,    0,    0,   76,   76,   76,   47,   76,
    0,    0,   76,   76,   76,    0,    0,    0,   74,   74,
   74,   74,   74,    0,    0,   74,   74,   74,    0,   74,
    0,    0,   74,   74,   74,   50,   50,   50,   50,   50,
    0,    0,   50,   50,   50,   91,   50,    0,    0,   50,
   50,   50,   68,   68,   68,   68,   68,   47,    0,   68,
   68,   68,   81,   68,    0,    0,   68,   68,   68,   46,
   46,   46,   46,   46,    0,    0,   46,   46,   46,    0,
   46,    0,    0,   46,   46,   46,    0,    0,    0,   66,
   66,   66,   66,   66,    0,    0,   66,   66,   66,    0,
   66,    0,    0,   66,   66,   66,    0,    0,    0,    0,
   51,   51,   51,   51,   51,    0,    0,   51,   51,   51,
    0,   51,    1,    0,   51,   51,   51,    2,    3,    4,
    5,    6,    0,    0,    7,    0,    0,    0,    8,   47,
   47,   47,   47,   47,    0,    0,   47,   47,   47,    0,
   47,    0,    0,   47,   47,   47,   57,   58,    0,    0,
   59,    0,    0,    4,    5,    6,    0,   60,    0,    0,
   61,   62,   63,    0,    0,    0,   57,   58,    0,    0,
   59,    0,    0,    4,    5,    6,    0,   60,    1,    0,
   61,   62,   63,    1,    3,    4,    5,    6,    0,    3,
    4,    5,    6,    0,    8,    0,    0,    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   41,  125,   40,   46,   45,   28,  272,  133,
  123,   40,   41,  123,   16,  272,   45,   16,   59,    0,
   61,   60,   44,   62,  148,  273,  274,  275,   49,   50,
   32,   60,    0,   62,  256,   39,   27,   59,   29,  163,
  272,   41,    0,  272,   44,    0,   37,   68,   59,   53,
   61,    9,   43,   11,  276,   46,   60,   78,    0,   59,
   41,   61,   20,   44,   66,   56,   37,   66,   11,   27,
   49,   50,   59,  123,   61,   44,   42,   20,   59,   37,
   61,   47,  103,   40,   43,   56,   45,  123,   46,   68,
   59,   40,   61,  258,  259,  256,   46,   46,   56,   78,
  261,  262,  263,  264,  265,  272,   41,  268,   43,  262,
   45,  272,  133,   43,   44,   45,   41,   42,   43,   44,
   45,  272,   47,  272,   40,  125,   41,  148,   43,   45,
   45,  123,   41,   41,   43,   60,   45,   62,  258,  259,
  262,   44,  163,  276,  125,   41,  256,   43,   44,   45,
   45,  261,  262,  263,  264,  265,  269,  125,  268,   43,
   44,   45,  272,   40,   60,   44,   62,  134,  135,   40,
  125,   41,   44,   43,   44,   45,   44,   88,   43,   90,
   45,   44,   93,  125,  138,  139,   44,  266,   41,  281,
   60,  102,   62,   40,   44,   60,   44,   62,   44,   44,
  125,   41,  113,   43,   44,   45,   44,   41,  259,   40,
   41,  122,   44,  259,   45,   44,   44,  256,   44,    0,
   60,    0,   62,  272,   41,  136,   41,  256,   41,  125,
   41,  272,   40,  272,  273,  274,  275,   46,  277,  278,
  279,  280,    9,  272,  273,  274,  275,   50,  277,  278,
  279,  280,   41,  141,   -1,  125,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,   -1,  267,  268,   41,
  270,  271,  272,  112,   -1,  256,  257,  258,  259,  260,
  261,  262,  263,  264,  265,  125,  267,  268,  256,  270,
  271,  272,   -1,  261,  262,  263,  264,  265,   -1,   -1,
  268,  256,   -1,   -1,  272,   -1,  261,  262,  263,  264,
  265,   -1,   41,  268,  256,  123,   -1,  272,   -1,  261,
  262,  263,  264,  265,   -1,   -1,  268,   -1,   -1,   -1,
  272,  256,  257,  258,  259,  260,  125,   41,  263,  264,
  265,   -1,  267,   -1,   -1,  270,  271,  272,   -1,   41,
   -1,   -1,  277,  278,  279,  280,  272,  273,  274,  275,
  256,  257,  258,  259,  260,   -1,   41,  263,  264,  265,
  125,  267,   -1,   -1,  270,  271,  272,  272,  273,  274,
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
   -1,  270,  271,  272,  256,  257,   -1,  125,  260,   -1,
   41,  263,  264,  265,   -1,  267,   -1,   -1,  270,  271,
  272,  256,  257,   -1,   -1,  260,  125,   41,  263,  264,
  265,   -1,  267,   -1,   -1,  270,  271,  272,   -1,  125,
   -1,   -1,   -1,   -1,   41,   -1,   -1,  256,  257,  258,
  259,  260,   -1,   -1,  263,  264,  265,  125,  267,   -1,
   -1,  270,  271,  272,   41,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,  257,  258,  259,  260,  125,   -1,  263,
  264,  265,   -1,  267,  256,   41,  270,  271,  272,   -1,
  256,  263,  264,  265,  125,   -1,  262,  263,  264,  265,
  272,  256,  257,  258,  259,  260,  272,   -1,  263,  264,
  265,  125,  267,   -1,   41,  270,  271,  272,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,
   -1,   41,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,
  258,  259,  260,   -1,   -1,  263,  264,  265,  125,  267,
   -1,   -1,  270,  271,  272,   -1,   -1,  256,  257,  258,
  259,  260,   -1,   -1,  263,  264,  265,   -1,  267,  125,
  256,  270,  271,  272,   -1,  261,  262,  263,  264,  265,
   -1,  125,  268,   -1,   -1,   -1,  272,   -1,  256,  257,
  258,  259,  260,   -1,   -1,  263,  264,  265,  125,  267,
   -1,   -1,  270,  271,  272,   -1,   -1,   -1,  256,  257,
  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,  267,
   -1,   -1,  270,  271,  272,  256,  257,  258,  259,  260,
   -1,   -1,  263,  264,  265,  125,  267,   -1,   -1,  270,
  271,  272,  256,  257,  258,  259,  260,  125,   -1,  263,
  264,  265,  125,  267,   -1,   -1,  270,  271,  272,  256,
  257,  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,   -1,   -1,   -1,  256,
  257,  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,   -1,   -1,   -1,   -1,
  256,  257,  258,  259,  260,   -1,   -1,  263,  264,  265,
   -1,  267,  256,   -1,  270,  271,  272,  261,  262,  263,
  264,  265,   -1,   -1,  268,   -1,   -1,   -1,  272,  256,
  257,  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,  256,  257,   -1,   -1,
  260,   -1,   -1,  263,  264,  265,   -1,  267,   -1,   -1,
  270,  271,  272,   -1,   -1,   -1,  256,  257,   -1,   -1,
  260,   -1,   -1,  263,  264,  265,   -1,  267,  256,   -1,
  270,  271,  272,  256,  262,  263,  264,  265,   -1,  262,
  263,  264,  265,   -1,  272,   -1,   -1,   -1,   -1,  272,
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

//#line 179 ".\gramatica.y"


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
//#line 578 "Parser.java"
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
{System.out.println("CREACION DE CLASE");}
break;
case 11:
//#line 29 ".\gramatica.y"
{System.out.println("CREACION DE CLASE CON HERENCIA");}
break;
case 12:
//#line 30 ".\gramatica.y"
{System.out.println("CREACION DE INTERFAZ");}
break;
case 19:
//#line 45 ".\gramatica.y"
{System.out.println("DECLARACION DE VARIABLE");}
break;
case 20:
//#line 46 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 21:
//#line 49 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION SIN PARAMETROS");}
break;
case 22:
//#line 50 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION");}
break;
case 23:
//#line 51 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 24:
//#line 52 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 29:
//#line 63 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 30:
//#line 64 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 31:
//#line 65 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 37:
//#line 75 ".\gramatica.y"
{System.out.println("SENTENCIA RETURN");}
break;
case 38:
//#line 76 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 39:
//#line 79 ".\gramatica.y"
{System.out.println("SENTENCIA IMPRESION DE CADENA");}
break;
case 40:
//#line 80 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 41:
//#line 81 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
break;
case 42:
//#line 82 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
break;
case 43:
//#line 83 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No existe esa expresion para imprimir la cadena", analizadorLex.getLineaArchivo()));}
break;
case 44:
//#line 86 ".\gramatica.y"
{System.out.println("SENTENCIA IF");}
break;
case 45:
//#line 87 ".\gramatica.y"
{System.out.println("SENTENCIA IF ELSE");}
break;
case 46:
//#line 88 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 47:
//#line 89 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 48:
//#line 90 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
break;
case 49:
//#line 91 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
break;
case 50:
//#line 92 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
break;
case 51:
//#line 93 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
break;
case 55:
//#line 101 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 56:
//#line 102 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 57:
//#line 103 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 64:
//#line 112 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Comparacion mal definida", analizadorLex.getLineaArchivo()));}
break;
case 65:
//#line 115 ".\gramatica.y"
{System.out.println("SENTENCIA ITERACION");}
break;
case 66:
//#line 116 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 67:
//#line 117 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte", analizadorLex.getLineaArchivo()));}
break;
case 68:
//#line 118 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte y falta una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 72:
//#line 124 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 74:
//#line 126 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 76:
//#line 128 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 79:
//#line 135 ".\gramatica.y"
{System.out.println("ASIGNACION");}
break;
case 80:
//#line 136 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 81:
//#line 137 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se puede declarar y asignar en la misma línea", analizadorLex.getLineaArchivo()));}
break;
case 95:
//#line 163 ".\gramatica.y"
{analizadorLex.convertirNegativo(((LexemToken)token).getLexema());}
break;
case 97:
//#line 165 ".\gramatica.y"
{analizadorLex.convertirNegativo(((LexemToken)token).getLexema());}
break;
case 98:
//#line 166 ".\gramatica.y"
{if(CheckRangoLong(((LexemToken)token).getLexema())){analizadorLex.addErroresLexicos(new Error("Long fuera de rango", analizadorLex.getLineaArchivo()));}}
break;
case 100:
//#line 168 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Las variables tipo UINT no pueden ser negativas", analizadorLex.getLineaArchivo()));}
break;
case 105:
//#line 175 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Tipo no reconocido", analizadorLex.getLineaArchivo()));}
break;
//#line 923 "Parser.java"
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
