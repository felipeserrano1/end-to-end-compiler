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






//#line 2 "gramatica.y"
	package AnalizadorSintactico;
	import java.util.ArrayList;
	import AnalizadorLexico.*;
	import java.math.BigDecimal;
//#line 22 "Parser.java"




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
public final static short ASIG=257;
public final static short ID=258;
public final static short CTE=259;
public final static short IF=260;
public final static short THEN=261;
public final static short ELSE=262;
public final static short ENDIF=263;
public final static short PRINT=264;
public final static short FUNC=265;
public final static short RETURN=266;
public final static short BEGIN=267;
public final static short END=268;
public final static short BREAK=269;
public final static short ULONG=270;
public final static short DOUBLE=271;
public final static short WHILE=272;
public final static short DO=273;
public final static short CADENA=274;
public final static short TRY=275;
public final static short POST=276;
public final static short CATCH=277;
public final static short ERROR=278;
public final static short OR=279;
public final static short AND=280;
public final static short DISTINTO=281;
public final static short MAYOR_IGUAL=282;
public final static short MENOR_IGUAL=283;
public final static short IGUAL=284;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    2,    2,    2,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    5,    5,    5,    5,    8,    8,
    9,    9,    9,   11,   11,   12,   12,   12,   10,   13,
   14,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   14,   14,   14,   14,   15,   15,
   15,   17,   17,   17,   18,   18,   18,   18,   18,   18,
    6,    6,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    7,    4,    4,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,   19,   19,   19,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   16,   16,   22,
   22,   22,   22,   22,   22,   22,   22,   20,   20,   20,
   20,   21,   21,   21,   21,   21,
};
final static short yylen[] = {                            2,
    2,    6,    6,    6,    6,    5,    5,    5,    4,    1,
    5,    4,    4,    4,    3,    3,    2,    2,    1,    1,
    2,    2,    2,    1,    1,    3,    1,    1,    7,    2,
    9,    9,    9,    9,    9,    8,    8,    8,    8,    8,
    8,    8,    8,    8,   15,   15,   15,   15,    3,    3,
    1,    3,    3,    1,    1,    2,    1,    4,    1,    2,
    2,    1,    4,    4,    2,    2,    2,    2,    1,    2,
    2,    2,    2,    2,    1,    4,    4,    4,    2,    2,
    2,    2,    1,    2,    2,    9,    9,    9,    9,    7,
    9,    7,    7,    7,    7,    7,    9,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    4,    4,    4,
    4,    6,    6,    6,    6,    6,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   25,
   24,    0,    0,    1,    0,    0,   10,    0,   19,   20,
    0,    0,    0,   69,    0,    0,    0,    0,   62,    0,
    0,    0,    0,    0,    0,    0,   28,   27,    0,    0,
    0,   71,   70,    0,    0,    0,    0,    0,    0,    0,
   83,   73,   72,    0,    0,    0,   18,   17,    0,    0,
   66,   65,   67,   68,    0,   57,   59,    0,    0,    0,
    0,   54,    0,   61,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   85,   84,
    0,    0,   80,   79,   81,   82,    0,    0,   16,   15,
    0,    0,   56,   60,  107,  106,  100,  103,  105,  101,
  102,  104,    0,    0,    0,    0,    0,    0,   12,   26,
   63,   64,    0,    0,    0,  111,  110,  109,  108,   13,
   14,    9,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   52,   53,    0,    0,    0,    0,    0,    0,    0,
    0,   77,   78,   76,   75,    0,    0,    0,   11,    8,
    7,    6,    0,   30,   58,    0,  113,    0,    0,    0,
    0,  115,  116,  114,  112,    3,   74,    5,    0,    2,
    0,    0,   96,    0,   92,    0,   93,    0,   94,   95,
    0,   90,    0,    0,    0,   29,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   97,   87,   88,   91,   89,
   86,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   37,   38,   39,
   40,   36,   41,    0,   42,   43,   44,    0,    0,    0,
   32,    0,   33,   34,    0,   31,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   46,   48,   47,   45,
};
final static short yydgoto[] = {                          2,
   14,   15,  165,  166,   16,   28,   17,   18,   19,   20,
   21,   30,  146,  206,   69,   70,   71,   72,   22,   23,
   24,  115,
};
final static short yysindex[] = {                      -168,
 -133,    0,   30, -219,  -31,  -30,  -95,  213,  -32,    0,
    0,  -26,  -57,    0,  -29,   96,    0,  -23,    0,    0,
 -112,  -20,  -10,    0,   76, -219,  -40,  -96,    0,   75,
  -40,  -40,  -40,  -40, -145, -165,    0,    0,   75,   76,
   90,    0,    0,  -40,  -40,  -52,   -9, -209,   23,   33,
    0,    0,    0,  231,  213,   40,    0,    0, -111,   75,
    0,    0,    0,    0,  111,    0,    0, -190,    1,   52,
   68,    0,  122,    0,  -45,   53,   81,  165,  -34,  167,
  175,  -19,   46,   44,  190,   36,  -40,  -40,    0,    0,
  -44, -156,    0,    0,    0,    0,  113,  131,    0,    0,
  217,  -50,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -40,  -40,  -40, -213,  -40,  -40,    0,    0,
    0,    0,    8,   59,  -76,    0,    0,    0,    0,    0,
    0,    0,  -12,   71, -172,   98,   22,  -57,  -57,  -57,
  170,   49,   45,  -50,   93,  312,   68,   68,  144, -133,
 -133,    0,    0, -133, -133, -133, -133, -133, -133, -133,
 -133,    0,    0,    0,    0,  241,  256,  263,    0,    0,
    0,    0,  319,    0,    0,  -13,    0,  130,  146,  171,
  -85,    0,    0,    0,    0,    0,    0,    0,   76,    0,
  209, -133,    0, -133,    0, -133,    0, -133,    0,    0,
 -133,    0,  231,  213,  237,    0,  101,  112,  116,  121,
 -104,  138,  156,  163,  181,    0,    0,    0,    0,    0,
    0,  346,  -24,  350,  188,  -22,  206,  -40,  -40,  -40,
  -40,  357,  -40,  -40,  -21,  145,  155,   12,  304,  -40,
  327,   21,  -40,  -40,  342,  352,  356,  365,  367,  449,
  371,  377,  381,  493,   31,  137,  183,  187, -138,  193,
  390,  199,  200,  202,  400,  424,  425,    0,    0,    0,
    0,    0,    0, -118,    0,    0,    0,  218,  228, -181,
    0,  440,    0,    0,  453,    0,  457,  477,  478,  482,
  -40,  -40,  -40,  485,  488,  489,  474,  480,  481,  269,
  273, -123,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   14,   35,    0,    0,    0,   47,
    0,    0,    0,    0,    0,    0,    0,    0,   48,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   58,
    0,    0,    0,    0,  -39,    0,    0,    0,   37,    0,
  -28,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -17,   -8,   39,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   61,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   62,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  182,    0,   43,  191,  351,  120,   94,    9,    0,    0,
  -61,   64,  399,    0,   82,  -33,  329,  300,   16,   27,
  189,    0,
};
final static int YYTABLESIZE=543;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         78,
   79,   55,   55,   55,   68,   55,  125,   55,   34,   36,
   85,   86,   51,   45,   51,  230,   51,  234,  244,   55,
   55,  129,   55,   49,   56,   49,   43,   49,   49,   53,
   51,   51,   50,   51,   50,   58,   50,   31,   62,   50,
  145,   49,   49,  113,   49,  114,   91,  150,   64,   90,
   50,   50,  248,   50,  113,   48,  114,   28,   32,  151,
  108,  253,  107,  113,  113,  114,  114,   92,  103,   27,
   39,  267,   28,  113,  285,  114,  135,   99,   27,   98,
  164,   94,  145,  160,   60,   27,  286,  104,   27,    1,
   81,   96,  116,   27,  287,  113,   29,  114,  100,  139,
  161,   29,  132,  172,  130,   23,   22,  170,   82,  117,
  140,  121,   76,   77,  118,   27,   21,  271,   75,    4,
   35,   74,    3,  113,    4,  114,    5,   41,   80,  272,
    6,    7,  305,    8,   74,    9,   10,   11,   12,  122,
  113,   13,  114,   37,  306,   38,  101,   29,   29,  281,
  102,  220,   59,   49,   49,   49,  162,  282,  221,   40,
   37,    4,   38,    5,   50,   50,   50,    6,  136,  137,
  200,   73,    9,   97,   98,   12,  201,  202,   13,  156,
  119,   49,   49,   49,  157,  245,  113,  113,  114,  114,
   74,   74,   50,   50,   50,  246,  149,  113,   40,  114,
   46,   51,    5,   87,   88,  123,    6,  126,  187,  187,
  187,   47,  120,   56,   12,  127,   55,   65,   66,   10,
   11,  124,  138,   42,   33,   35,   52,   51,  169,   44,
  133,  229,   57,  233,  243,   61,  128,   67,   49,   55,
   55,   55,   55,   55,   55,   63,   89,   50,  192,  193,
   51,   51,   51,   51,   51,   51,  144,  294,  295,  296,
  158,   49,   49,   49,   49,   49,   49,  247,  154,   28,
   50,   50,   50,   50,   50,   50,  252,  163,   93,  105,
  106,  109,  110,  111,  112,   25,  266,   26,   95,    5,
   27,  134,   99,    6,   98,   99,   29,   29,    9,  131,
  171,   12,   23,   22,   13,   74,   74,   29,   29,  236,
  237,  238,  239,   21,  241,  242,    4,   35,   74,  155,
   74,  250,  212,  213,  254,  255,   51,   51,   51,  167,
  168,  176,  177,  225,  227,  178,  179,  180,  181,  182,
  183,  184,  185,  159,  249,   83,  113,    4,  114,    5,
  174,   54,  175,    6,   51,   51,   51,   84,    9,  191,
    7,   12,   55,  216,   13,   10,   11,  251,   40,  113,
    4,  114,    5,  207,  217,  208,    6,  209,  218,  210,
  141,    9,  211,  219,   12,  228,  142,   13,    4,  231,
    5,  194,  195,   40,    6,    4,  240,    5,  143,    9,
  256,    6,   12,  222,  268,   13,    9,  196,  197,   12,
  257,   40,   13,    4,  258,    5,  152,  153,   25,    6,
   26,  223,    5,  259,    9,  260,    6,   12,  224,  262,
   13,    9,  198,  199,   12,  263,   40,   13,    4,  264,
    5,  147,  148,   40,    6,    4,  226,    5,  274,    9,
  269,    6,   12,  232,  270,   13,    9,  277,  278,   12,
  273,   40,   13,    4,  203,    5,  275,  276,   40,    6,
    4,  235,    5,    7,    9,  204,    6,   12,   10,   11,
   13,    9,  279,  280,   12,  283,   25,   13,   26,  261,
    5,  113,  214,  114,    6,  284,   40,  288,   46,    9,
    5,    7,   12,  215,    6,   13,   10,   11,  186,   47,
  289,   40,   12,   46,  290,    5,  291,  292,  189,    6,
   46,  293,    5,  188,   47,  297,    6,   12,  298,  299,
  190,   47,  300,  265,   12,  113,  303,  114,  301,  302,
  304,  205,  173,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   34,   41,   42,   43,   45,   45,   41,   47,   40,   40,
   44,   45,   41,   40,   43,   40,   45,   40,   40,   59,
   60,   41,   62,   41,   16,   43,   59,   45,   13,   59,
   59,   60,   41,   62,   43,   59,   45,  257,   59,   13,
  102,   59,   60,   43,   62,   45,  256,  261,   59,   59,
   59,   60,   41,   62,   43,   13,   45,   44,  278,  273,
   60,   41,   62,   43,   43,   45,   45,  277,  259,   40,
    7,   41,   59,   43,  256,   45,   41,   41,   44,   41,
   59,   59,  144,  256,   21,   40,  268,  278,   40,  258,
  256,   59,   41,   59,  276,   43,    3,   45,   59,  256,
  273,    8,   59,   59,   59,   59,   59,   59,  274,   42,
  267,   59,   31,   32,   47,   40,   59,  256,   44,   59,
   59,   28,  256,   43,  258,   45,  260,    8,  274,  268,
  264,  265,  256,  267,   41,  269,  270,  271,  272,   59,
   43,  275,   45,  256,  268,  258,  258,   54,   55,  268,
   40,  256,  265,  138,  139,  140,   59,  276,  263,  256,
  256,  258,  258,  260,  138,  139,  140,  264,   87,   88,
  256,  268,  269,   54,   55,  272,  262,  263,  275,  256,
   59,  166,  167,  168,  261,   41,   43,   43,   45,   45,
   97,   98,  166,  167,  168,   41,  115,   43,  256,   45,
  258,   13,  260,  256,  257,   41,  264,   41,  166,  167,
  168,  269,  258,  205,  272,   41,  256,  258,  259,  270,
  271,  256,  267,  256,  256,  256,  256,  256,   59,  256,
   41,  256,  256,  256,  256,  256,  256,  278,  256,  279,
  280,  281,  282,  283,  284,  256,  256,  256,  262,  263,
  279,  280,  281,  282,  283,  284,   40,  291,  292,  293,
  273,  279,  280,  281,  282,  283,  284,  256,  261,  256,
  279,  280,  281,  282,  283,  284,  256,  256,  256,  279,
  280,  281,  282,  283,  284,  256,  256,  258,  256,  260,
  256,  256,  256,  264,  256,  256,  203,  204,  269,  256,
  256,  272,  256,  256,  275,  212,  213,  214,  215,  228,
  229,  230,  231,  256,  233,  234,  256,  256,  225,  261,
  227,  240,  203,  204,  243,  244,  138,  139,  140,  139,
  140,  150,  151,  214,  215,  154,  155,  156,  157,  158,
  159,  160,  161,  273,   41,  256,   43,  258,   45,  260,
  258,  256,   41,  264,  166,  167,  168,  268,  269,   41,
  265,  272,  267,  263,  275,  270,  271,   41,  256,   43,
  258,   45,  260,  192,  263,  194,  264,  196,  263,  198,
  268,  269,  201,  263,  272,   40,  256,  275,  258,   40,
  260,  262,  263,  256,  264,  258,   40,  260,  268,  269,
   59,  264,  272,  266,  268,  275,  269,  262,  263,  272,
   59,  256,  275,  258,   59,  260,  117,  118,  256,  264,
  258,  266,  260,   59,  269,   59,  264,  272,  266,   59,
  275,  269,  262,  263,  272,   59,  256,  275,  258,   59,
  260,  113,  114,  256,  264,  258,  266,  260,   59,  269,
  268,  264,  272,  266,  268,  275,  269,  256,   59,  272,
  268,  256,  275,  258,  256,  260,  268,  268,  256,  264,
  258,  266,  260,  265,  269,  267,  264,  272,  270,  271,
  275,  269,   59,   59,  272,  268,  256,  275,  258,   41,
  260,   43,  256,   45,  264,  268,  256,   58,  258,  269,
  260,  265,  272,  267,  264,  275,  270,  271,  268,  269,
   58,  256,  272,  258,   58,  260,   40,   40,  256,  264,
  258,   40,  260,  268,  269,   41,  264,  272,   41,   41,
  268,  269,   59,   41,  272,   43,  268,   45,   59,   59,
  268,  191,  144,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=284;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'",null,"'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"ASIG","ID","CTE","IF","THEN","ELSE","ENDIF",
"PRINT","FUNC","RETURN","BEGIN","END","BREAK","ULONG","DOUBLE","WHILE","DO",
"CADENA","TRY","POST","CATCH","ERROR","\"OR\"","\"AND\"","\"DISTINTO\"",
"\"MAYOR_IGUAL\"","\"MENOR_IGUAL\"","\"IGUAL\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID bloque_sentencias",
"bloque_try : TRY sentencia_try CATCH BEGIN sentencia_compuesta_try END",
"bloque_try : TRY sentencia_try error BEGIN sentencia_compuesta_try END",
"bloque_try : TRY sentencia_try CATCH BEGIN sentencia_compuesta_try error",
"bloque_try : TRY sentencia_try CATCH error sentencia_compuesta_try END",
"bloque_sentencias : sentencia_declarativa BEGIN sentencia_compuesta END ';'",
"bloque_sentencias : sentencia_declarativa BEGIN sentencia_compuesta END error",
"bloque_sentencias : sentencia_declarativa BEGIN sentencia_compuesta error ';'",
"bloque_sentencias : BEGIN sentencia_compuesta END ';'",
"bloque_sentencias : sentencia_simple",
"bloque_sentencias : sentencia_declarativa error sentencia_compuesta END ';'",
"bloque_sentencias : error sentencia_compuesta END ';'",
"bloque_sentencias : BEGIN sentencia_compuesta error ';'",
"bloque_sentencias : BEGIN sentencia_compuesta END error",
"sentencia_declarativa : sentencia_declarativa declaracion ';'",
"sentencia_declarativa : sentencia_declarativa declaracion error",
"sentencia_declarativa : declaracion ';'",
"sentencia_declarativa : declaracion error",
"declaracion : declaracion_var",
"declaracion : declaracion_func",
"declaracion_var : tipo lista_var",
"declaracion_var : FUNC lista_var",
"declaracion_var : error lista_var",
"tipo : DOUBLE",
"tipo : ULONG",
"lista_var : lista_var ',' ID",
"lista_var : ID",
"lista_var : error",
"declaracion_func : tipo FUNC ID '(' parametro ')' cuerpo_func",
"parametro : tipo ID",
"cuerpo_func : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' END",
"cuerpo_func : sentencia_declarativa error sentencia_compuesta RETURN '(' expresion ')' ';' END",
"cuerpo_func : sentencia_declarativa BEGIN sentencia_compuesta RETURN error expresion ')' ';' END",
"cuerpo_func : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion error ';' END",
"cuerpo_func : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error",
"cuerpo_func : BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' END",
"cuerpo_func : error sentencia_compuesta RETURN '(' expresion ')' ';' END",
"cuerpo_func : BEGIN sentencia_compuesta RETURN error expresion ')' ';' END",
"cuerpo_func : BEGIN sentencia_compuesta RETURN '(' expresion error ';' END",
"cuerpo_func : BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error",
"cuerpo_func : sentencia_declarativa error RETURN '(' expresion ')' ';' END",
"cuerpo_func : sentencia_declarativa BEGIN RETURN error expresion ')' ';' END",
"cuerpo_func : sentencia_declarativa BEGIN RETURN '(' expresion error ';' END",
"cuerpo_func : sentencia_declarativa BEGIN RETURN '(' expresion ')' ';' error",
"cuerpo_func : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' POST ':' '(' condicion ')' ';' END",
"cuerpo_func : sentencia_declarativa error sentencia_compuesta RETURN '(' expresion ')' ';' POST ':' '(' condicion ')' ';' END",
"cuerpo_func : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' POST ':' '(' condicion ')' ';' error",
"cuerpo_func : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error ':' '(' condicion ')' ';' END",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : '-' CTE",
"factor : CTE",
"factor : ID '(' parametro ')'",
"factor : ERROR",
"factor : '-' ERROR",
"sentencia_compuesta : sentencia_compuesta sentencia_simple",
"sentencia_compuesta : sentencia_simple",
"sentencia_simple : ID ASIG expresion ';'",
"sentencia_simple : ID ERROR expresion ';'",
"sentencia_simple : seleccion ';'",
"sentencia_simple : seleccion error",
"sentencia_simple : print error",
"sentencia_simple : print ';'",
"sentencia_simple : iteracion",
"sentencia_simple : BREAK ';'",
"sentencia_simple : BREAK error",
"sentencia_simple : bloque_try ';'",
"sentencia_simple : bloque_try error",
"sentencia_compuesta_try : sentencia_compuesta_try sentencia_try",
"sentencia_compuesta_try : sentencia_try",
"sentencia_try : ID ASIG expresion ';'",
"sentencia_try : ID error expresion ';'",
"sentencia_try : ID ASIG expresion error",
"sentencia_try : seleccion ';'",
"sentencia_try : seleccion error",
"sentencia_try : print error",
"sentencia_try : print ';'",
"sentencia_try : iteracion",
"sentencia_try : BREAK ';'",
"sentencia_try : BREAK error",
"seleccion : IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias ENDIF",
"seleccion : IF error condicion ')' THEN bloque_sentencias ELSE bloque_sentencias ENDIF",
"seleccion : IF '(' condicion error THEN bloque_sentencias ELSE bloque_sentencias ENDIF",
"seleccion : IF '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias error",
"seleccion : IF '(' condicion ')' THEN bloque_sentencias ENDIF",
"seleccion : IF '(' condicion ')' error bloque_sentencias ELSE bloque_sentencias ENDIF",
"seleccion : IF error condicion ')' THEN bloque_sentencias ENDIF",
"seleccion : IF '(' condicion error THEN bloque_sentencias ENDIF",
"seleccion : IF '(' condicion ')' error bloque_sentencias ENDIF",
"seleccion : IF '(' condicion ')' THEN bloque_sentencias error",
"seleccion : error '(' condicion ')' THEN bloque_sentencias ENDIF",
"seleccion : error '(' condicion ')' THEN bloque_sentencias ELSE bloque_sentencias ENDIF",
"condicion : expresion operadores expresion",
"condicion : expresion",
"operadores : '>'",
"operadores : \"MAYOR_IGUAL\"",
"operadores : \"MENOR_IGUAL\"",
"operadores : '<'",
"operadores : \"IGUAL\"",
"operadores : \"DISTINTO\"",
"operadores : \"AND\"",
"operadores : \"OR\"",
"print : PRINT '(' CADENA ')'",
"print : PRINT '(' CADENA error",
"print : PRINT '(' error ')'",
"print : PRINT error CADENA ')'",
"iteracion : WHILE '(' condicion ')' DO bloque_sentencias",
"iteracion : error '(' condicion ')' DO bloque_sentencias",
"iteracion : WHILE '(' condicion ')' error bloque_sentencias",
"iteracion : WHILE error condicion ')' DO bloque_sentencias",
"iteracion : WHILE '(' condicion error DO bloque_sentencias",
};

//#line 166 "gramatica.y"

	ArrayList<String> erroresSintacticos = new ArrayList<>();
	ArrayList<Integer> tokens = new ArrayList<>();
	ArrayList<String> sentencias = new ArrayList<>();
	AnalizadorLexico analizadorLexico = AnalizadorLexico.GetInstancia();


	int yylex(){
		int token = analizadorLexico.yylex();
		if(token == 258 || token == 259 ||  token == 274)
			yylval = analizadorLexico.getyylval();
		tokens.add(token);
		return token;
	}

	void yyerror(String error){
		this.erroresSintacticos.add(error);
	}

	void addSentencia(String s){
		this.sentencias.add(s);
	}

	public ArrayList<String> getErroresSintacticos(){
		return this.erroresSintacticos;
	}

	public ArrayList<Integer> getTokens(){
		return this.tokens;
	}

	public ArrayList<String> getSentencias(){
		return this.sentencias;
	}

	public boolean chequeoRango(int indice){
		Token t = analizadorLexico.getTabla().getToken(indice);
		BigDecimal valor = new BigDecimal(t.getLexema());
		if (t.getTipo() == "ulong")
			if (valor.compareTo(new BigDecimal(0)) == 1)
				return false;
		return true;
	}
//#line 568 "Parser.java"
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
//#line 20 "gramatica.y"
{addSentencia("linea " + analizadorLexico.getLinea() + ": bloque try catch");}
break;
case 3:
//#line 21 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta CATCH");}
break;
case 4:
//#line 22 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta END EN TRY CATCH");}
break;
case 5:
//#line 23 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta BEGIN EN TRY CATCH ");}
break;
case 7:
//#line 27 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ; en bloque de sentencias");}
break;
case 8:
//#line 28 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta END en bloque de sentencia");}
break;
case 11:
//#line 31 "gramatica.y"
{yyerror( analizadorLexico.getLinea() + ": falta BEGIN en bloque de sentencias");}
break;
case 12:
//#line 32 "gramatica.y"
{yyerror( analizadorLexico.getLinea()+ ": falta BEGIN en sentencias ejecutables");}
break;
case 13:
//#line 33 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta END en sentencias ejecutables");}
break;
case 14:
//#line 34 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() +": falta ; en sentencia ejecutables");}
break;
case 16:
//#line 37 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ; en sentencia declarativa ");}
break;
case 18:
//#line 39 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ; en declaracion");}
break;
case 19:
//#line 41 "gramatica.y"
{addSentencia("linea " + analizadorLexico.getLinea() + ": declaracion de variables");}
break;
case 20:
//#line 42 "gramatica.y"
{addSentencia("linea " + analizadorLexico.getLinea() + ": declaracion de funcion");}
break;
case 23:
//#line 46 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta FUNC o tipo");}
break;
case 28:
//#line 53 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ID");}
break;
case 29:
//#line 56 "gramatica.y"
{addSentencia("linea " + analizadorLexico.getLinea() + ": funcion");}
break;
case 32:
//#line 61 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta BEGIN en funcion");}
break;
case 33:
//#line 62 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ( en funcion");}
break;
case 34:
//#line 63 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ) en funcion");}
break;
case 35:
//#line 64 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta END en funcion");}
break;
case 37:
//#line 66 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta BEGIN en funcion");}
break;
case 38:
//#line 67 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ( en funcion");}
break;
case 39:
//#line 68 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ) en funcion");}
break;
case 40:
//#line 69 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta END en funcion");}
break;
case 41:
//#line 70 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta BEGIN en funcion");}
break;
case 42:
//#line 71 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ( en funcion");}
break;
case 43:
//#line 72 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ) en funcion");}
break;
case 44:
//#line 73 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta end en funcion");}
break;
case 45:
//#line 74 "gramatica.y"
{addSentencia("linea " + analizadorLexico.getLinea() + ": funcion con post condicion");}
break;
case 46:
//#line 75 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea()+ ": falta BEGIN en funcion");}
break;
case 47:
//#line 76 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea()+ ": falta END en funcion");}
break;
case 48:
//#line 77 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea()+ ": falta POST en funcion");}
break;
case 56:
//#line 88 "gramatica.y"
{if(!chequeoRango(val_peek(0).ival)) yyerror("Linea " + analizadorLexico.getLinea() + ": ULONG negativo"); else
               		  analizadorLexico.cambiarSignoTabla(val_peek(0).ival);}
break;
case 63:
//#line 99 "gramatica.y"
{addSentencia("linea " + analizadorLexico.getLinea() + ": asignacion");}
break;
case 66:
//#line 102 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ; en IF ");}
break;
case 67:
//#line 103 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ; en PRINT");}
break;
case 71:
//#line 107 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ; BREAK");}
break;
case 73:
//#line 109 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ; en sentencia TRY");}
break;
case 76:
//#line 116 "gramatica.y"
{addSentencia("linea " + analizadorLexico.getLinea() + ": asignacion");}
break;
case 77:
//#line 117 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta := ");}
break;
case 78:
//#line 118 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ; en asignacion");}
break;
case 80:
//#line 120 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + "falta ; en IF");}
break;
case 81:
//#line 121 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ; en PRINT");}
break;
case 85:
//#line 125 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ; en BREAK");}
break;
case 86:
//#line 128 "gramatica.y"
{addSentencia("linea " + analizadorLexico.getLinea() + ": sentencia IF ELSE");}
break;
case 87:
//#line 129 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ( en IF");}
break;
case 88:
//#line 130 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ) en IF");}
break;
case 89:
//#line 131 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ENDIF en IF");}
break;
case 90:
//#line 132 "gramatica.y"
{addSentencia("linea " + analizadorLexico.getLinea() + ": sentencia IF sin ELSE");}
break;
case 91:
//#line 133 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta THEN en IF");}
break;
case 92:
//#line 134 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ( en IF");}
break;
case 93:
//#line 135 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ) en IF");}
break;
case 94:
//#line 136 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta THEN en IF");}
break;
case 95:
//#line 137 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ENDIF en IF");}
break;
case 96:
//#line 138 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta IF en IF");}
break;
case 97:
//#line 139 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta IF en IF");}
break;
case 108:
//#line 153 "gramatica.y"
{addSentencia("linea " + analizadorLexico.getLinea() + ": sentencia PRINT");}
break;
case 109:
//#line 154 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ) en PRINT");}
break;
case 110:
//#line 155 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta CADENA en PRINT");}
break;
case 111:
//#line 156 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ( en PRINT");}
break;
case 112:
//#line 158 "gramatica.y"
{addSentencia("linea " + analizadorLexico.getLinea() + ": sentencia WHILE");}
break;
case 113:
//#line 159 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta WHILE en WHILE");}
break;
case 114:
//#line 160 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta DO en WHILE");}
break;
case 115:
//#line 161 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ( en WHILE");}
break;
case 116:
//#line 162 "gramatica.y"
{yyerror("Linea " + analizadorLexico.getLinea() + ": falta ) en WHILE");}
break;
//#line 982 "Parser.java"
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
