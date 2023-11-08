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
    4,    4,    4,    6,    6,    6,    6,    8,    8,    9,
    9,    7,    7,    7,    7,   10,   10,   10,   10,   10,
   10,   10,   10,    3,    3,    5,    5,    5,    5,   11,
   15,   15,   14,   14,   14,   14,   16,   16,   16,   16,
   16,   21,   21,   20,   20,   20,   20,   20,   18,   18,
   18,   18,   18,   18,   18,   18,   23,   23,   22,   22,
   22,   22,   25,   25,   25,   25,   25,   25,   25,   19,
   19,   19,   19,   17,   17,   17,   17,   17,   17,   17,
   17,   27,   27,   26,   26,   26,   13,   13,   24,   24,
   24,   28,   28,   28,   28,   29,   29,   31,   30,   30,
   30,   30,   30,   30,   30,   12,   12,   12,   12,   12,
};
final static short yylen[] = {                            2,
    3,    2,    2,    1,    1,    2,    1,    1,    1,    3,
    5,    5,    4,    3,    3,    2,    2,    1,    2,    1,
    1,    1,    2,    1,    2,    5,    6,    4,    5,    3,
    4,    2,    3,    3,    2,    6,    7,    5,    6,    2,
    1,    2,    3,    3,    2,    2,    1,    1,    1,    1,
    1,    2,    1,    3,    2,    3,    2,    3,    7,    9,
    6,    8,    6,    8,    5,    7,    1,    1,    3,    2,
    2,    1,    1,    1,    1,    1,    1,    1,    1,    7,
    6,    6,    5,    1,    1,    4,    3,    5,    4,    5,
    4,    1,    3,    4,    3,    5,    1,    3,    1,    3,
    3,    1,    3,    3,    3,    1,    1,    2,    1,    2,
    1,    2,    1,    1,    2,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
  120,    0,    0,  118,  117,  116,    0,  119,    0,    0,
    0,    5,    7,    8,    9,    0,    0,    0,    0,    0,
    3,    6,   97,    0,    0,    0,    0,   10,    0,    0,
    1,   34,    0,    0,   16,   20,   21,    0,   18,   17,
    0,    0,    0,    0,    0,   13,   24,    0,   22,   98,
   11,   14,   19,   15,    0,    0,    0,    0,   40,    0,
   12,   25,   23,    0,    0,    0,    0,    0,    0,    0,
   45,   84,    0,    0,   41,   47,   48,   49,   50,   51,
   85,    0,   46,    0,   36,    0,    0,   30,    0,    0,
    0,    0,    0,    0,   52,    0,    0,    0,   43,   42,
    0,    0,    0,   44,   37,    0,    0,   31,   58,   79,
   92,  113,  114,  111,    0,    0,   73,   74,   75,   76,
   77,   78,    0,    0,    0,    0,    0,    0,  102,    0,
  107,   56,   54,    0,    0,    0,    0,    0,   93,    0,
   26,    0,    0,   68,   67,    0,  112,  115,  110,    0,
    0,    0,    0,    0,    0,    0,  108,    0,    0,    0,
   86,    0,   94,   27,  105,    0,    0,    0,    0,    0,
    0,  103,  104,    0,    0,   90,   96,   88,    0,   63,
    0,    0,   82,    0,    0,    0,   59,   80,   64,    0,
   60,
};
final static short yydgoto[] = {                         10,
   11,   12,   72,   14,   15,   28,   48,   38,   39,   49,
   43,   16,   24,  144,   74,  145,   76,   77,   78,   79,
   80,  124,  146,  125,  126,   81,  127,  128,  129,  130,
  131,
};
final static short yysindex[] = {                       225,
    0, -220, -218,    0,    0,    0, -206,    0,  668,    0,
  619,    0,    0,    0,    0, -200,  -40,   41,  -38,  636,
    0,    0,    0,   20, -185,  -49,  337,    0,  256, -118,
    0,    0, -167,  -11,    0,    0,    0,  647,    0,    0,
  495,  -10,   70, -153, -127,    0,    0, -115,    0,    0,
    0,    0,    0,    0,  582,  545,  107,  -10,    0,  212,
    0,    0,    0, -123,  122, -219,  -10,  119,  124,    0,
    0,    0, -104,  602,    0,    0,    0,    0,    0,    0,
    0,   -2,    0,  562,    0,  125,  365,    0,  126,  132,
  -29,  133,  137, -100,    0,  148,    0,   -1,    0,    0,
  144,  -90,  148,    0,    0,   -8,  142,    0,    0,    0,
    0,    0,    0,    0,  148,  170,    0,    0,    0,    0,
    0,    0, -146,  145,  135,  148,  141,   16,    0,  -87,
    0,    0,    0,  151,   39,  148,  152,   47,    0,   87,
    0,   -5,   61,    0,    0, -197,    0,    0,    0,  170,
  148,  148,  148,  -30,   55,   55,    0,   29,  154,   91,
    0,  155,    0,    0,    0,  170,  156, -165,   16,   16,
  -30,    0,    0,  157,  161,    0,    0,    0,  -56,    0,
  170,  160,    0,  162,  164,  -50,    0,    0,    0,  167,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  222,    0,    0,    0,    0,    0,    0,    0,    0,  234,
    0,    0,    0,    1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   14,    0,    0, -108,
    0,    0,    0,  -60,    0,    0,    0,  195,    0,  -18,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   27,    0,    0, -107,    0,
    0,  246,  274,    0,    0,    0,  402,    0,    0,    0,
    0,    0,    0,    0,    0, -106,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  194,  -39,   80,    0,   54,
    0,    0,    0,    0,    0,    0,  300,    0,    0,  325,
    0, -101,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  196,  206,    0,    0,    0,    0,  355,    0,
    0,  383,    0,    0,    0,    0,  423,    0,  105,  130,
  226,    0,    0,  441,    0,    0,    0,    0,    0,    0,
    0,  466,    0,  483,  506,    0,    0,    0,    0,  527,
    0,
};
final static short yygindex[] = {                         0,
  244,   30,  896,    0,  887,  240,    0,  253,   79,  233,
  -34,   -9,    0,  -33,  228,   -7,    0,    0,    0,    0,
    0,  136, -110,    7,  171,    0,  740,  -14,    3,    0,
    0,
};
final static int YYTABLESIZE=940;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         27,
   35,  109,  109,  109,  109,  109,   46,  109,   57,   61,
  115,  116,  151,   38,  152,  123,   32,   33,   28,   44,
  109,   92,  109,   29,   86,   89,   39,   92,   27,   56,
  117,   56,  118,   94,   56,  141,   92,  101,  164,  168,
   22,   35,   92,  102,  102,   73,   73,   75,   75,   22,
   44,   17,  107,   18,   38,  179,   93,  155,  103,  136,
  166,  167,  156,   32,   73,   19,  100,   39,  115,  174,
  186,   23,   57,  123,   73,   35,  100,   44,   33,  159,
   29,  151,   26,  152,   30,  109,   34,  162,  117,  151,
  118,  152,  181,  182,  106,  106,  106,  106,  106,  123,
  106,  165,  135,  151,   50,  152,   73,  138,   86,  140,
   58,   26,   55,  106,   55,  106,   53,   55,   59,   53,
   99,  143,   99,   99,   99,   35,  147,  148,  149,  151,
  163,  152,  154,  151,  177,  152,  169,  170,   38,   99,
   73,   99,  160,   45,   60,  100,   45,  100,  100,  100,
   85,   39,   90,   32,   33,   28,   73,  172,  173,  171,
   29,   91,   95,   96,  100,  134,  100,   97,  105,  108,
  101,   73,  101,  101,  101,  109,  132,  151,  106,  152,
  133,  139,  142,  115,  137,  150,  102,  115,  123,  101,
  158,  101,  123,  157,  117,  161,  118,  176,  178,  180,
  183,  184,  185,  187,   99,  188,    1,  189,  190,   56,
  191,  120,    3,    4,    5,    6,  109,  109,  109,  109,
  109,    4,    8,  109,  109,  109,  110,  109,   25,  100,
  109,  109,  109,    2,   72,   53,   70,  109,  109,  109,
  109,  109,  111,  112,  113,  114,   71,  119,  120,  121,
  122,   87,   20,  119,  101,   88,   35,   35,   35,   35,
   35,   35,   35,   35,   35,   35,   69,   35,   35,   38,
   35,   35,   35,   51,   38,   38,   38,   38,   38,   41,
   63,   38,   39,   84,  110,   38,   57,   39,   39,   39,
   39,   39,   55,  175,   39,  153,   42,    0,   39,    0,
  111,  112,  113,  114,    0,  119,  120,  121,  122,  106,
  106,  106,  106,  106,   55,    0,  106,  106,  106,   53,
  106,    0,    0,  106,  106,  106,  111,  112,  113,  114,
  106,  106,  106,  106,    0,   99,   99,   99,   99,   99,
   87,    0,   99,   99,   99,    0,   99,    9,    0,   99,
   99,   99,    0,    0,    0,    0,   99,   99,   99,   99,
  100,  100,  100,  100,  100,   95,    0,  100,  100,  100,
   57,  100,    0,    0,  100,  100,  100,   40,    0,    0,
    0,  100,  100,  100,  100,  101,  101,  101,  101,  101,
  110,    0,  101,  101,  101,   91,  101,    0,   55,  101,
  101,  101,    0,    0,    0,  106,  101,  101,  101,  101,
    0,  119,  120,  121,  122,  111,  112,  113,  114,  111,
  112,  113,  114,   89,   87,   64,   65,    0,    0,   66,
    0,    0,    4,    5,    6,    0,   67,    0,    0,   68,
   69,   70,   97,    0,    0,   97,    0,   92,    0,   95,
   53,   53,   53,   53,   53,    0,    0,   53,   53,   53,
   97,   53,   92,   65,   53,   53,   53,    1,    0,    0,
    0,    0,    0,    0,    4,    5,    6,    0,    0,   91,
    1,   83,    0,    8,    0,    2,    3,    4,    5,    6,
    0,    0,    7,    0,    0,    0,    8,    0,    0,    0,
    0,   57,   57,   57,   57,   57,   61,   89,   57,   57,
   57,    1,   57,    0,    0,   57,   57,   57,    4,    5,
    6,    0,    0,   81,    0,    0,   97,    8,    0,   55,
   55,   55,   55,   55,    0,   54,   55,   55,   55,    0,
   55,    0,    0,   55,   55,   55,   66,   65,    0,    0,
    0,    0,    0,    0,    0,   87,   87,   87,   87,   87,
    0,    0,   87,   87,   87,   83,   87,   62,    0,   87,
   87,   87,    0,    0,    0,    0,    0,    0,    0,    0,
   95,   95,   95,   95,   95,   83,    0,   95,   95,   95,
   61,   95,    1,    0,   95,   95,   95,    0,    3,    4,
    5,    6,  104,    0,    0,    0,    0,   81,    8,    0,
   91,   91,   91,   91,   91,    0,    0,   91,   91,   91,
    1,   91,    0,    0,   91,   91,   91,    4,    5,    6,
   66,    0,    0,    0,    0,    0,    8,    0,   89,   89,
   89,   89,   89,    0,    0,   89,   89,   89,    0,   89,
    0,   62,   89,   89,   89,    0,    0,   97,   97,   97,
   97,   97,    0,    0,   97,   97,   97,    0,   97,    0,
    0,   97,   97,   97,    0,    0,    0,    0,   65,   65,
   65,   65,   65,    0,    0,   65,   65,   65,    0,   65,
    0,    0,   65,   65,   65,    0,   83,   83,   83,   83,
   83,    0,    0,   83,   83,   83,   71,   83,    0,    0,
   83,   83,   83,    0,    0,    0,    0,    0,    0,    0,
    0,   61,   61,   61,   61,   61,   99,    0,   61,   61,
   61,    0,   61,    0,    0,   61,   61,   61,   81,   81,
   81,   81,   81,   21,    0,   81,   81,   81,    0,   81,
    1,    0,   81,   81,   81,    0,    3,    4,    5,    6,
   31,   66,   66,   66,   66,   66,    8,    0,   66,   66,
   66,   52,   66,    0,    0,   66,   66,   66,    0,    0,
    0,    0,   62,   62,   62,   62,   62,    0,    0,   62,
   62,   62,    0,   62,   82,   82,   62,   62,   62,    0,
   64,   65,    0,    0,   66,    0,    0,    4,    5,    6,
    0,   67,   98,   82,   68,   69,   70,   64,   65,    0,
    0,   66,    0,   82,    4,    5,    6,    0,   67,    0,
    0,   68,   69,   70,    0,    0,    0,   64,   65,    0,
    0,   66,    0,    0,    4,    5,    6,    0,   67,    0,
    0,   68,   69,   70,    0,   82,    0,   64,   65,    0,
    0,   66,    0,    0,    4,    5,    6,    0,   67,    0,
    0,   68,   69,   70,    1,    0,    0,    0,    0,    2,
    3,    4,    5,    6,    0,    0,    7,    0,    0,   82,
    8,    1,    0,    0,    0,   13,    2,    3,    4,    5,
    6,    0,    1,    7,   13,   82,   13,    8,    3,    4,
    5,    6,   37,   37,    0,   13,   47,    0,    8,    0,
   82,   36,   36,    1,   37,    0,    0,   37,    2,    3,
    4,    5,    6,   36,   62,    7,   36,    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,   42,   43,   44,   45,  125,   47,   42,  125,
   40,   41,   43,    0,   45,   45,  125,  125,  125,   29,
   60,   40,   62,  125,   58,   60,    0,   46,   40,   40,
   60,   40,   62,   67,   40,   44,  256,   40,   44,  150,
   11,   41,   61,   46,   46,   55,   56,   55,   56,   20,
   60,  272,   87,  272,   41,  166,  276,   42,   61,   61,
  258,  259,   47,   44,   74,  272,   74,   41,   40,   41,
  181,  272,  106,   45,   84,  125,   84,   87,   59,   41,
   40,   43,  123,   45,  123,  125,  272,   41,   60,   43,
   62,   45,  258,  259,   41,   42,   43,   44,   45,   45,
   47,   41,   96,   43,  272,   45,  116,  101,  142,  103,
   41,  123,  123,   60,  123,   62,   38,  123,  272,   41,
   41,  115,   43,   44,   45,  125,  273,  274,  275,   43,
   44,   45,  126,   43,   44,   45,  151,  152,  125,   60,
  150,   62,  136,  262,  272,   41,  262,   43,   44,   45,
   44,  125,  276,  262,  262,  262,  166,  155,  156,  153,
  262,   40,   44,   40,   60,  266,   62,  272,   44,   44,
   41,  181,   43,   44,   45,   44,   44,   43,  125,   45,
   44,  272,   41,   40,   41,   41,   46,   40,   45,   60,
   40,   62,   45,  281,   60,   44,   62,   44,   44,   44,
   44,   41,  259,   44,  125,   44,  256,   44,  259,   40,
   44,  272,  262,  263,  264,  265,  256,  257,  258,  259,
  260,    0,  272,  263,  264,  265,  256,  267,  269,  125,
  270,  271,  272,    0,   41,   41,   41,  277,  278,  279,
  280,  281,  272,  273,  274,  275,   41,  277,  278,  279,
  280,   40,    9,  272,  125,   44,  256,  257,  258,  259,
  260,  261,  262,  263,  264,  265,   41,  267,  268,  256,
  270,  271,  272,   34,  261,  262,  263,  264,  265,   27,
   48,  268,  256,   56,  256,  272,   41,  261,  262,  263,
  264,  265,  123,  158,  268,  125,   41,   -1,  272,   -1,
  272,  273,  274,  275,   -1,  277,  278,  279,  280,  256,
  257,  258,  259,  260,   41,   -1,  263,  264,  265,  125,
  267,   -1,   -1,  270,  271,  272,  272,  273,  274,  275,
  277,  278,  279,  280,   -1,  256,  257,  258,  259,  260,
   41,   -1,  263,  264,  265,   -1,  267,  123,   -1,  270,
  271,  272,   -1,   -1,   -1,   -1,  277,  278,  279,  280,
  256,  257,  258,  259,  260,   41,   -1,  263,  264,  265,
  125,  267,   -1,   -1,  270,  271,  272,   41,   -1,   -1,
   -1,  277,  278,  279,  280,  256,  257,  258,  259,  260,
  256,   -1,  263,  264,  265,   41,  267,   -1,  125,  270,
  271,  272,   -1,   -1,   -1,   41,  277,  278,  279,  280,
   -1,  277,  278,  279,  280,  272,  273,  274,  275,  272,
  273,  274,  275,   41,  125,  256,  257,   -1,   -1,  260,
   -1,   -1,  263,  264,  265,   -1,  267,   -1,   -1,  270,
  271,  272,   41,   -1,   -1,   44,   -1,   46,   -1,  125,
  256,  257,  258,  259,  260,   -1,   -1,  263,  264,  265,
   59,  267,   61,   41,  270,  271,  272,  256,   -1,   -1,
   -1,   -1,   -1,   -1,  263,  264,  265,   -1,   -1,  125,
  256,   41,   -1,  272,   -1,  261,  262,  263,  264,  265,
   -1,   -1,  268,   -1,   -1,   -1,  272,   -1,   -1,   -1,
   -1,  256,  257,  258,  259,  260,   41,  125,  263,  264,
  265,  256,  267,   -1,   -1,  270,  271,  272,  263,  264,
  265,   -1,   -1,   41,   -1,   -1,  125,  272,   -1,  256,
  257,  258,  259,  260,   -1,   41,  263,  264,  265,   -1,
  267,   -1,   -1,  270,  271,  272,   41,  125,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  257,  258,  259,  260,
   -1,   -1,  263,  264,  265,  125,  267,   41,   -1,  270,
  271,  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,  259,  260,   41,   -1,  263,  264,  265,
  125,  267,  256,   -1,  270,  271,  272,   -1,  262,  263,
  264,  265,   41,   -1,   -1,   -1,   -1,  125,  272,   -1,
  256,  257,  258,  259,  260,   -1,   -1,  263,  264,  265,
  256,  267,   -1,   -1,  270,  271,  272,  263,  264,  265,
  125,   -1,   -1,   -1,   -1,   -1,  272,   -1,  256,  257,
  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,  267,
   -1,  125,  270,  271,  272,   -1,   -1,  256,  257,  258,
  259,  260,   -1,   -1,  263,  264,  265,   -1,  267,   -1,
   -1,  270,  271,  272,   -1,   -1,   -1,   -1,  256,  257,
  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,  267,
   -1,   -1,  270,  271,  272,   -1,  256,  257,  258,  259,
  260,   -1,   -1,  263,  264,  265,  125,  267,   -1,   -1,
  270,  271,  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  256,  257,  258,  259,  260,  125,   -1,  263,  264,
  265,   -1,  267,   -1,   -1,  270,  271,  272,  256,  257,
  258,  259,  260,  125,   -1,  263,  264,  265,   -1,  267,
  256,   -1,  270,  271,  272,   -1,  262,  263,  264,  265,
  125,  256,  257,  258,  259,  260,  272,   -1,  263,  264,
  265,  125,  267,   -1,   -1,  270,  271,  272,   -1,   -1,
   -1,   -1,  256,  257,  258,  259,  260,   -1,   -1,  263,
  264,  265,   -1,  267,   55,   56,  270,  271,  272,   -1,
  256,  257,   -1,   -1,  260,   -1,   -1,  263,  264,  265,
   -1,  267,   73,   74,  270,  271,  272,  256,  257,   -1,
   -1,  260,   -1,   84,  263,  264,  265,   -1,  267,   -1,
   -1,  270,  271,  272,   -1,   -1,   -1,  256,  257,   -1,
   -1,  260,   -1,   -1,  263,  264,  265,   -1,  267,   -1,
   -1,  270,  271,  272,   -1,  116,   -1,  256,  257,   -1,
   -1,  260,   -1,   -1,  263,  264,  265,   -1,  267,   -1,
   -1,  270,  271,  272,  256,   -1,   -1,   -1,   -1,  261,
  262,  263,  264,  265,   -1,   -1,  268,   -1,   -1,  150,
  272,  256,   -1,   -1,   -1,    0,  261,  262,  263,  264,
  265,   -1,  256,  268,    9,  166,   11,  272,  262,  263,
  264,  265,   26,   27,   -1,   20,   30,   -1,  272,   -1,
  181,   26,   27,  256,   38,   -1,   -1,   41,  261,  262,
  263,  264,  265,   38,   48,  268,   41,   -1,   -1,  272,
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
"declaracion_clase : CLASS ID bloque_clase",
"declaracion_clase : CLASS ID IMPLEMENT ID bloque_clase",
"declaracion_clase : INTERFACE ID '{' cuerpo_interfaz '}'",
"declaracion_clase : INTERFACE ID '{' '}'",
"bloque_clase : '{' cuerpo_clase '}'",
"bloque_clase : '(' cuerpo_clase ')'",
"bloque_clase : '{' '}'",
"bloque_clase : '(' ')'",
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

//#line 203 ".\gramatica.y"


static AnalizadorLexico analizadorLex = null;
static Parser par = null;
static Token token = null;
static ArrayList<String>  polaca;
private String identClase = "";

public static void main(String[] args) throws Exception{
        System.out.println("Iniciando compilacion...");
        analizadorLex = new AnalizadorLexico(args);
        
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
          System.out.println("yylex: " + token);
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
{System.out.println("CREACION DE CLASE" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 11:
//#line 30 ".\gramatica.y"
{System.out.println("CREACION DE CLASE CON HERENCIA" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 12:
//#line 31 ".\gramatica.y"
{System.out.println("CREACION DE INTERFAZ" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 13:
//#line 32 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Error interfaz vacia", analizadorLex.getLineaArchivo()));}
break;
case 15:
//#line 36 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 16:
//#line 37 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 17:
//#line 38 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 24:
//#line 51 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));}
break;
case 25:
//#line 52 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));}
break;
case 26:
//#line 55 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION VACIA SIN PARAMETROS" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 27:
//#line 56 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION VACIA" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 28:
//#line 57 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 29:
//#line 58 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 30:
//#line 59 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
break;
case 31:
//#line 60 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
break;
case 32:
//#line 61 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
break;
case 33:
//#line 62 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
break;
case 34:
//#line 65 ".\gramatica.y"
{System.out.println("DECLARACION DE VARIABLE" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 35:
//#line 66 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 36:
//#line 69 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION SIN PARAMETROS" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 37:
//#line 70 ".\gramatica.y"
{System.out.println("DECLARACION DE FUNCION" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 38:
//#line 71 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 39:
//#line 72 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 44:
//#line 83 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 45:
//#line 84 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
break;
case 46:
//#line 85 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
break;
case 48:
//#line 89 ".\gramatica.y"
{System.out.println("SENTENCIA IF" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 49:
//#line 90 ".\gramatica.y"
{System.out.println("SENTENCIA ITERACION" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 50:
//#line 91 ".\gramatica.y"
{System.out.println("SENTENCIA IMPRESION DE CADENA" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 51:
//#line 92 ".\gramatica.y"
{System.out.println("SENTENCIA RETURN" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 53:
//#line 96 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 55:
//#line 100 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 56:
//#line 101 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
break;
case 57:
//#line 102 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
break;
case 58:
//#line 103 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No existe esa expresion para imprimir la cadena", analizadorLex.getLineaArchivo()));}
break;
case 61:
//#line 108 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 62:
//#line 109 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 63:
//#line 110 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
break;
case 64:
//#line 111 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
break;
case 65:
//#line 112 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
break;
case 66:
//#line 113 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
break;
case 70:
//#line 121 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 71:
//#line 122 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 72:
//#line 123 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
break;
case 79:
//#line 132 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Comparacion mal definida", analizadorLex.getLineaArchivo()));}
break;
case 81:
//#line 136 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 82:
//#line 137 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte", analizadorLex.getLineaArchivo()));}
break;
case 83:
//#line 138 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte y falta una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 85:
//#line 142 ".\gramatica.y"
{System.out.println("ASIGNACION" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 86:
//#line 143 ".\gramatica.y"
{System.out.println("LLAMADO A METODO" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 87:
//#line 144 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 88:
//#line 145 ".\gramatica.y"
{System.out.println("LLAMADO A METODO" + ", linea: " + analizadorLex.getLineaArchivo());}
break;
case 89:
//#line 146 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 91:
//#line 148 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 92:
//#line 151 ".\gramatica.y"
{identClase = val_peek(0).sval + identClase;}
break;
case 93:
//#line 152 ".\gramatica.y"
{identClase += "." + val_peek(0).sval;}
break;
case 94:
//#line 154 ".\gramatica.y"
{polaca.add(identClase); identClase = "";polaca.add("=");}
break;
case 95:
//#line 155 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
break;
case 96:
//#line 156 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("No se puede declarar y asignar en la misma línea", analizadorLex.getLineaArchivo()));}
break;
case 100:
//#line 165 ".\gramatica.y"
{polaca.add("+");}
break;
case 101:
//#line 166 ".\gramatica.y"
{polaca.add("-");}
break;
case 103:
//#line 170 ".\gramatica.y"
{polaca.add("*");}
break;
case 104:
//#line 171 ".\gramatica.y"
{polaca.add("/");}
break;
case 108:
//#line 179 ".\gramatica.y"
{polaca.add("--");}
break;
case 110:
//#line 182 ".\gramatica.y"
{analizadorLex.convertirNegativo(val_peek(0).sval);
polaca.add("-" + val_peek(0).sval);}
break;
case 111:
//#line 184 ".\gramatica.y"
{polaca.add(val_peek(0).sval);}
break;
case 112:
//#line 185 ".\gramatica.y"
{analizadorLex.convertirNegativo(val_peek(0).sval);
polaca.add("-" + val_peek(0).sval);}
break;
case 113:
//#line 187 ".\gramatica.y"
{{if(CheckRangoLong(val_peek(0).sval)){analizadorLex.addErroresLexicos(new Error("Long fuera de rango", analizadorLex.getLineaArchivo()));}
  else {
    polaca.add(val_peek(0).sval);
  }}}
break;
case 114:
//#line 191 ".\gramatica.y"
{polaca.add(val_peek(0).sval);}
break;
case 115:
//#line 192 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Las variables tipo UINT no pueden ser negativas", analizadorLex.getLineaArchivo()));}
break;
case 120:
//#line 199 ".\gramatica.y"
{analizadorLex.addErroresLexicos(new Error("Tipo no reconocido", analizadorLex.getLineaArchivo()));}
break;
//#line 1073 "Parser.java"
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
