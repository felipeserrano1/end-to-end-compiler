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
	import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;
import utilidades.ListaError;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    0,    2,    6,    3,    4,    4,    4,    4,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    8,    8,    8,    8,   11,   11,   12,   16,
   12,   12,   14,   14,   15,   15,   15,   13,   20,   17,
   18,   21,   19,   19,   19,   24,   19,   25,   19,   19,
   26,   26,   26,   26,   26,   26,   26,   26,   26,   26,
   26,   26,   26,   26,   26,   26,   26,   26,   26,   26,
   26,   26,   22,   22,   22,   27,   27,   27,   28,   29,
   28,   28,   28,   28,   28,   30,   30,   30,    9,    9,
   31,   10,   10,   10,   10,   10,   10,   10,   10,   10,
   10,   10,   10,    7,    7,   35,    5,    5,    5,    5,
    5,    5,    5,    5,    5,    5,   38,   32,   32,   32,
   32,   32,   37,   37,   39,   40,   36,   23,   23,   41,
   41,   41,   41,   41,   41,   41,   41,   42,   33,   33,
   33,   33,   45,   34,   34,   34,   43,   44,   44,
};
final static short yylen[] = {                            2,
    2,    2,    0,    3,    4,    4,    4,    4,    5,    5,
    5,    5,    4,    4,    4,    4,    4,    4,    4,    4,
    3,    1,    3,    3,    2,    2,    1,    1,    2,    0,
    3,    2,    1,    1,    3,    1,    1,    3,    0,    4,
    3,    2,    9,    8,    8,    0,   16,    0,   15,    1,
    9,   10,    9,    9,    9,    8,    7,    9,    8,    8,
    8,   15,   14,   16,   15,   15,    8,    7,    9,    8,
    8,    8,    3,    3,    1,    3,    3,    1,    1,    0,
    3,    1,    4,    1,    2,    1,    2,    1,    2,    1,
    0,    5,    4,    4,    2,    2,    2,    2,    1,    2,
    2,    2,    2,    2,    1,    0,    5,    4,    4,    2,
    2,    2,    2,    1,    2,    2,    0,    6,    5,    5,
    5,    5,    3,    1,    1,    1,    3,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    0,    5,    4,
    4,    4,    0,    4,    3,    3,    1,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,   30,    0,    0,   34,
   33,  147,    0,    1,    0,    0,    0,   22,    0,   27,
   28,    0,    0,    0,    0,   99,    0,    0,    0,    0,
    0,   90,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  101,  100,    0,    0,    3,    0,
    0,  114,  103,  102,    0,    0,    2,    0,    0,    0,
   26,   25,   37,   36,    0,    0,    0,    0,   96,   95,
   97,   98,    0,    0,    0,   82,   84,    0,    0,    0,
    0,   78,    0,   89,    0,    0,    0,    0,  145,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   21,
    0,    0,    0,    0,  116,  115,    4,  111,  110,  112,
  113,    0,    0,    0,    0,    0,    0,    0,    0,   24,
   23,   39,    0,    0,    0,    0,    0,   38,   50,  146,
  143,    0,   80,   85,    0,    0,  137,  136,  130,  133,
  135,  131,  132,  134,  127,    0,    0,    0,   18,   35,
  149,  125,    0,    0,  148,   93,   91,   94,    0,    0,
    0,  142,  141,  140,  138,   19,   20,   17,    0,    0,
  105,    0,    0,    0,   14,    0,   15,   16,   13,    0,
    0,   40,   42,   41,    0,    0,    0,    0,  144,   86,
   88,    0,    0,   81,    0,    0,    0,   76,   77,  121,
    0,   92,  122,  119,  120,  117,  139,  108,  109,  106,
    6,  104,    8,    0,    5,   12,   11,   10,    9,    0,
    0,    0,    0,    0,    0,    0,    0,   87,   83,  126,
  123,  118,  107,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   57,    0,    0,    0,    0,    0,
   68,    0,    0,    0,    0,   56,   59,   60,    0,   44,
    0,   67,    0,   70,   71,    0,   45,    0,    0,    0,
    0,    0,   58,    0,   51,    0,   69,   53,   54,    0,
    0,   43,    0,    0,    0,    0,    0,   52,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   48,
    0,    0,    0,    0,   63,   49,   62,    0,   66,   65,
   46,   64,   47,
};
final static short yydgoto[] = {                          2,
  152,   15,   16,   57,  171,  107,  172,   17,   31,   18,
   19,   20,   21,   22,   33,   41,   23,   68,  128,  182,
  124,   79,   80,  353,  346,  129,   81,   82,  194,  193,
  202,   24,   25,   26,  233,   34,  153,  232,  154,  231,
  146,  207,   27,   89,  189,
};
final static short yysindex[] = {                      -213,
 -109,    0,   90, -214,  -35,  -32,    0,  141,  -36,    0,
    0,    0,  396,    0,  -23, -212,  196,    0,   28,    0,
    0,  -58,    9,   86,  113,    0,  -30,   15, -214,   93,
  164,    0,   14, -108,  -42,   93, -150, -124, -133, -153,
  -33,   15,  115,  182,    0,    0, -173,  114,    0,  116,
  117,    0,    0,    0,  -61, -123,    0,  200,  241,  118,
    0,    0,    0,    0,  -39,   14, -103,  217,    0,    0,
    0,    0, -160, -160,  194,    0,    0, -203,  186,  -14,
  161,    0,  185,    0,  134, -109, -109, -109,    0,  198,
   67,   72, -109, -109, -109,  223,  236,  -29,   14,    0,
   23,  119,   93,   93,    0,    0,    0,    0,    0,    0,
    0,  396,  396,  396,  334,  259,   31,  120,  332,    0,
    0,    0,  138,  362,  353,  354,  237,    0,    0,    0,
    0,   98,    0,    0,   93,   93,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   93,   93,   93,    0,    0,
    0,    0, -104,  145,    0,    0,    0,    0,  149,  166,
 -158,    0,    0,    0,    0,    0,    0,    0,  159,   80,
    0,  -59,  374,  381,    0,  368,    0,    0,    0,   38,
  121,    0,    0,    0,  142,   68,  266,   69,    0,    0,
    0,  172,  400,    0,  161,  161,  186,    0,    0,    0,
 -109,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   15,    0,    0,    0,    0,    0,  397,
  -26,   93,  403,  333,  -24,   93,   89,    0,    0,    0,
    0,    0,    0,   93,   93,   93,  499,   93,  407,   93,
   93,  517,   74,   93,  518,  535,   48,  389,  590,   93,
  614,   61,  390,   93,   93,  621,  394,  406,  411,  412,
  187,  415,  626,  419,  421,  422,  191,  629,   75,  424,
  224,  230,  232, -206,    0,  235,  447,  246,  250, -149,
    0,  461,  462,  122,  249,    0,    0,    0,  261,    0,
  404,    0, -122,    0,    0,  265,    0,  268,  269,  480,
 -199,  485,    0,  512,    0,  506,    0,    0,    0,  292,
   92,    0,  519,  530,   93,  539,  524,    0,  543,  554,
   93,   -8,   93,  555,   93,   93,   -1,  544,    6,   93,
   12,   19,  547,  330,  556,   26,  557,  560,  352,    0,
  356,  562,  359, -128,    0,    0,    0,  372,    0,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   41,   83,    0,
    0,    0,  123,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  124,    0,    0,    0,    0,
    0,    0,    0,    0,  -41,    0,    0,    0,   32,    0,
  -34,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  125,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -99,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -28,  -21,   39,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  126,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  127,    0,
    0,    0,    0,    0,    0,  128,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  129,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
   36,    0,    0,    0,   17,    0,   79,  568,  386,  360,
   25,    0,    0,  577,   54,    0,    0,    0,    0,    0,
    0,  331,  100,    0,    0,    0,  251,   64,    0,    0,
    0,   22,  377,  382,    0,   24,   76,    0,    0,    0,
    0,    0,    0,  315,    0,
};
final static int YYTABLESIZE=674;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         79,
   79,   79,   78,   79,   30,   79,   75,   40,   75,   30,
   75,  165,   73,  236,   73,  241,   73,   79,   79,   74,
   79,   74,   46,   74,   75,   75,  145,   75,   38,   49,
   73,   73,  328,   73,   50,   54,   14,   74,   74,  333,
   74,   60,   35,   55,    1,  140,  335,  139,   67,  289,
   74,  140,  337,  139,   30,  133,  311,   85,  140,  338,
  139,  290,   30,   36,   56,  140,  342,  139,  312,  291,
   30,  140,  129,  139,  134,   66,  313,   30,  140,  128,
  139,  166,  103,  104,   37,  140,   62,  139,  260,  177,
  135,  129,  136,  129,   99,   86,  217,  205,  128,   37,
  128,  266,   97,  135,  206,  136,  296,  222,  226,  135,
   93,  136,   88,  255,  135,  284,  136,  135,  297,  136,
   98,  151,  135,  155,  136,  157,   36,  350,  244,   30,
  158,   94,  113,   50,   50,   50,   95,   78,  210,  351,
   96,   36,  192,  114,   70,  305,    3,   86,    4,  319,
    5,   60,   87,  306,    6,    7,  124,    8,  200,    9,
   10,   11,   12,  124,   88,   13,   10,   11,  159,  160,
  161,   72,  106,  100,  109,  111,  121,  168,  179,  219,
  301,   32,   29,   31,    7,   61,   72,   55,  212,  212,
  212,  173,  174,   50,   50,   50,   42,   63,   47,   64,
    5,  135,  147,  136,    6,  112,   65,  148,  211,   48,
  198,  199,   12,   90,   79,   75,   76,  208,  122,   45,
   37,   75,   63,   39,   64,   73,  164,   73,  135,  235,
  136,  240,   53,  132,   74,   77,  230,   79,   79,   79,
   79,   79,   79,  149,   75,   75,   75,   75,   75,   75,
   73,   73,   73,   73,   73,   73,  156,   74,   74,   74,
   74,   74,   74,  162,  137,  138,  141,  142,  143,  144,
  137,  138,  141,  142,  143,  144,  163,  137,  138,  141,
  142,  143,  144,   61,  137,  138,  141,  142,  143,  144,
  137,  138,  141,  142,  143,  144,   37,  137,  138,  141,
  142,  143,  144,  259,  137,  138,  141,  142,  143,  144,
  129,  129,  129,  129,  129,  129,  265,  128,  128,  128,
  128,  128,  128,   42,   42,    4,    4,    5,    5,  254,
  283,    6,    6,  221,  225,  209,    9,    9,   36,   12,
   12,   69,   13,   13,   42,   28,    4,   29,    5,    5,
   75,   76,    6,    6,  243,  190,  191,    9,    9,  318,
   12,   12,   32,   13,   13,   91,   92,   32,   71,  105,
   77,  108,  110,  120,  167,  178,  218,  300,   32,   29,
   31,    7,   61,   72,   55,  195,  196,  130,  131,   51,
   84,  150,  175,   44,   52,  183,   42,   42,    4,    4,
    5,    5,  184,   84,    6,    6,  201,  220,   43,    9,
    9,  203,   12,   12,  322,   13,   13,   32,   32,   42,
  327,    4,  329,    5,  331,  332,  216,    6,  204,  336,
  228,   83,    9,  169,  170,   12,  234,  101,   13,    4,
  229,    5,  238,  116,  119,    6,  250,  261,  267,  102,
    9,   58,  271,   12,  275,   28,   13,   29,  281,    5,
    7,  304,   59,    6,  272,   10,   11,  115,    9,  273,
  274,   12,  125,  276,   13,   84,  197,  278,   84,  279,
  280,    7,  285,  126,   32,   32,   10,   11,   51,   51,
   51,  286,  187,   52,   52,   52,  117,  287,    4,  288,
    5,    7,  292,  188,    6,  293,   10,   11,  118,    9,
  185,  186,   12,  294,   42,   13,    4,  295,    5,  298,
  299,   28,    6,   29,  302,    5,  176,    9,  303,    6,
   12,  223,  307,   13,    9,  308,  309,   12,  310,  248,
   13,  135,  314,  136,   84,   84,   32,   32,   51,   51,
   51,  315,  237,   52,   52,   52,  242,  253,  257,  135,
  135,  136,  136,  316,  245,  246,  247,  317,  249,  321,
  251,  252,  224,  227,  256,  258,  320,  135,  323,  136,
  263,  324,  325,   84,  268,  269,   84,  180,   42,    4,
    4,    5,    5,  326,  330,    6,    6,  340,  239,  181,
    9,    9,  334,   12,   12,  339,   13,   13,   28,   42,
   29,    4,    5,    5,  341,  343,    6,    6,  344,  345,
  348,    9,    9,  347,   12,   12,  349,   13,   13,   42,
  262,   47,  135,    5,  136,  127,  214,    6,   47,  352,
    5,  213,   48,  123,    6,   12,    0,    0,  215,   48,
    0,   42,   12,   47,  264,    5,  135,    0,  136,    6,
    0,  270,    0,  135,   48,  136,  277,   12,  135,  282,
  136,  135,    0,  136,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   45,   45,   40,   47,   41,   40,   43,   40,
   45,   41,   41,   40,   43,   40,   45,   59,   60,   41,
   62,   43,   59,   45,   59,   60,   41,   62,    5,   13,
   59,   60,   41,   62,   13,   59,    1,   59,   60,   41,
   62,   17,  257,  256,  258,   60,   41,   62,   40,  256,
   27,   60,   41,   62,   40,  259,  256,   44,   60,   41,
   62,  268,   40,  278,  277,   60,   41,   62,  268,  276,
   40,   60,   41,   62,  278,   22,  276,   40,   60,   41,
   62,   59,  256,  257,   44,   60,   59,   62,   41,   59,
   43,   60,   45,   62,   41,  256,   59,  256,   60,   59,
   62,   41,  256,   43,  263,   45,  256,   40,   40,   43,
  261,   45,  273,   40,   43,   41,   45,   43,  268,   45,
  274,   86,   43,   88,   45,   59,   44,  256,   40,   40,
   59,  256,  256,  112,  113,  114,  261,   45,   59,  268,
  274,   59,   45,  267,   59,  268,  256,  256,  258,   58,
  260,  127,  261,  276,  264,  265,  256,  267,  263,  269,
  270,  271,  272,  263,  273,  275,  270,  271,   93,   94,
   95,   59,   59,   59,   59,   59,   59,   59,   59,   59,
   59,   59,   59,   59,   59,   59,   59,   59,  172,  173,
  174,  113,  114,  172,  173,  174,  256,  256,  258,  258,
  260,   43,   42,   45,  264,  267,  265,   47,  268,  269,
  147,  148,  272,  256,  256,  258,  259,   59,  258,  256,
  256,  256,  256,  256,  258,  256,  256,  256,   43,  256,
   45,  256,  256,   40,  256,  278,  201,  279,  280,  281,
  282,  283,  284,   59,  279,  280,  281,  282,  283,  284,
  279,  280,  281,  282,  283,  284,   59,  279,  280,  281,
  282,  283,  284,   41,  279,  280,  281,  282,  283,  284,
  279,  280,  281,  282,  283,  284,   41,  279,  280,  281,
  282,  283,  284,  256,  279,  280,  281,  282,  283,  284,
  279,  280,  281,  282,  283,  284,  256,  279,  280,  281,
  282,  283,  284,  256,  279,  280,  281,  282,  283,  284,
  279,  280,  281,  282,  283,  284,  256,  279,  280,  281,
  282,  283,  284,  256,  256,  258,  258,  260,  260,  256,
  256,  264,  264,  266,  266,  256,  269,  269,  256,  272,
  272,  256,  275,  275,  256,  256,  258,  258,  260,  260,
  258,  259,  264,  264,  266,  258,  259,  269,  269,  268,
  272,  272,    3,  275,  275,   35,   36,    8,  256,  256,
  278,  256,  256,  256,  256,  256,  256,  256,  256,  256,
  256,  256,  256,  256,  256,  135,  136,   73,   74,   13,
   31,  258,   59,    8,   13,  258,  256,  256,  258,  258,
  260,  260,   41,   44,  264,  264,  262,  266,  268,  269,
  269,  263,  272,  272,  315,  275,  275,   58,   59,  256,
  321,  258,  323,  260,  325,  326,   59,  264,  263,  330,
  259,  268,  269,  103,  104,  272,   40,  256,  275,  258,
   41,  260,   40,   58,   59,  264,   40,   59,   59,  268,
  269,  256,   59,  272,  268,  256,  275,  258,  268,  260,
  265,   58,  267,  264,   59,  270,  271,  268,  269,   59,
   59,  272,  256,   59,  275,  116,  146,   59,  119,   59,
   59,  265,   59,  267,  125,  126,  270,  271,  112,  113,
  114,  268,  256,  112,  113,  114,  256,  268,  258,  268,
  260,  265,  268,  267,  264,   59,  270,  271,  268,  269,
  125,  126,  272,  268,  256,  275,  258,  268,  260,   59,
   59,  256,  264,  258,  276,  260,  268,  269,  268,  264,
  272,  266,  268,  275,  269,  268,  268,  272,   59,   41,
  275,   43,   58,   45,  185,  186,  187,  188,  172,  173,
  174,   40,  222,  172,  173,  174,  226,   41,   41,   43,
   43,   45,   45,   58,  234,  235,  236,  276,  238,   40,
  240,  241,  187,  188,  244,   41,   58,   43,   40,   45,
  250,   58,   40,  224,  254,  255,  227,  256,  256,  258,
  258,  260,  260,   40,   40,  264,  264,  268,  266,  268,
  269,  269,   59,  272,  272,   59,  275,  275,  256,  256,
  258,  258,  260,  260,   59,   59,  264,  264,   59,  268,
   59,  269,  269,  268,  272,  272,  268,  275,  275,  256,
   41,  258,   43,  260,   45,   68,  256,  264,  258,  268,
  260,  268,  269,   67,  264,  272,   -1,   -1,  268,  269,
   -1,  256,  272,  258,   41,  260,   43,   -1,   45,  264,
   -1,   41,   -1,   43,  269,   45,   41,  272,   43,   41,
   45,   43,   -1,   45,
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
"bloque_try : bloque_try_comienzo bloque_catch",
"$$1 :",
"bloque_try_comienzo : TRY sentencia_try $$1",
"bloque_catch : CATCH BEGIN sentencia_compuesta_try END",
"bloque_catch : error BEGIN sentencia_compuesta_try END",
"bloque_catch : CATCH BEGIN sentencia_compuesta_try error",
"bloque_catch : CATCH error sentencia_compuesta_try END",
"bloque_sentencias : sentencia_declarativa BEGIN sentencia_compuesta END ';'",
"bloque_sentencias : sentencia_declarativa BEGIN sentencia_compuesta END error",
"bloque_sentencias : sentencia_declarativa BEGIN sentencia_compuesta error ';'",
"bloque_sentencias : sentencia_declarativa error sentencia_compuesta END ';'",
"bloque_sentencias : sentencia_declarativa BEGIN END ';'",
"bloque_sentencias : sentencia_declarativa error END ';'",
"bloque_sentencias : sentencia_declarativa BEGIN error ';'",
"bloque_sentencias : sentencia_declarativa BEGIN END error",
"bloque_sentencias : BEGIN sentencia_compuesta END ';'",
"bloque_sentencias : error sentencia_compuesta END ';'",
"bloque_sentencias : BEGIN sentencia_compuesta error ';'",
"bloque_sentencias : BEGIN sentencia_compuesta END error",
"bloque_sentencias : BEGIN END ';'",
"bloque_sentencias : sentencia_simple",
"sentencia_declarativa : sentencia_declarativa declaracion ';'",
"sentencia_declarativa : sentencia_declarativa declaracion error",
"sentencia_declarativa : declaracion ';'",
"sentencia_declarativa : declaracion error",
"declaracion : declaracion_var",
"declaracion : func",
"declaracion_var : tipo lista_var",
"$$2 :",
"declaracion_var : FUNC $$2 lista_var",
"declaracion_var : error lista_var",
"tipo : DOUBLE",
"tipo : ULONG",
"lista_var : lista_var ',' ID",
"lista_var : ID",
"lista_var : error",
"func : declaracion_func parametro_func cuerpo_func",
"$$3 :",
"declaracion_func : tipo FUNC ID $$3",
"parametro_func : '(' parametro ')'",
"parametro : tipo ID",
"cuerpo_func : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' END",
"cuerpo_func : BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' END",
"cuerpo_func : sentencia_declarativa BEGIN RETURN '(' expresion ')' ';' END",
"$$4 :",
"cuerpo_func : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' POST ':' '(' condicion ')' ';' END $$4",
"$$5 :",
"cuerpo_func : BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' POST ':' '(' condicion ')' ';' END $$5",
"cuerpo_func : cuerpo_func_error",
"cuerpo_func_error : sentencia_declarativa error sentencia_compuesta RETURN '(' expresion ')' ';' END",
"cuerpo_func_error : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error END",
"cuerpo_func_error : sentencia_declarativa BEGIN sentencia_compuesta RETURN error expresion ')' ';' END",
"cuerpo_func_error : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion error ';' END",
"cuerpo_func_error : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error",
"cuerpo_func_error : error sentencia_compuesta RETURN '(' expresion ')' ';' END",
"cuerpo_func_error : BEGIN sentencia_compuesta '(' expresion ')' ';' END",
"cuerpo_func_error : BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error END",
"cuerpo_func_error : BEGIN sentencia_compuesta RETURN error expresion ')' ';' END",
"cuerpo_func_error : BEGIN sentencia_compuesta RETURN '(' expresion error ';' END",
"cuerpo_func_error : BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error",
"cuerpo_func_error : sentencia_declarativa error sentencia_compuesta RETURN '(' expresion ')' ';' POST ':' '(' condicion ')' ';' END",
"cuerpo_func_error : sentencia_declarativa BEGIN sentencia_compuesta '(' expresion ')' ';' POST ':' '(' condicion ')' ';' END",
"cuerpo_func_error : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' error ';' POST ':' '(' condicion ')' ';' END",
"cuerpo_func_error : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' POST ':' '(' condicion ')' ';' error",
"cuerpo_func_error : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error ':' '(' condicion ')' ';' END",
"cuerpo_func_error : sentencia_declarativa error RETURN '(' expresion ')' ';' END",
"cuerpo_func_error : sentencia_declarativa BEGIN '(' expresion ')' ';' END",
"cuerpo_func_error : sentencia_declarativa BEGIN RETURN '(' expresion ')' ';' error END",
"cuerpo_func_error : sentencia_declarativa BEGIN RETURN error expresion ')' ';' END",
"cuerpo_func_error : sentencia_declarativa BEGIN RETURN '(' expresion error ';' END",
"cuerpo_func_error : sentencia_declarativa BEGIN RETURN '(' expresion ')' ';' error",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"$$6 :",
"factor : '-' CTE $$6",
"factor : CTE",
"factor : ID '(' parametro_inv ')'",
"factor : ERROR",
"factor : '-' ERROR",
"parametro_inv : ID",
"parametro_inv : '-' CTE",
"parametro_inv : CTE",
"sentencia_compuesta : sentencia_compuesta sentencia_simple",
"sentencia_compuesta : sentencia_simple",
"$$7 :",
"sentencia_simple : ID ASIG expresion ';' $$7",
"sentencia_simple : ID ASIG error ';'",
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
"$$8 :",
"sentencia_try : ID ASIG expresion ';' $$8",
"sentencia_try : ID error expresion ';'",
"sentencia_try : ID ASIG expresion error",
"sentencia_try : seleccion ';'",
"sentencia_try : seleccion error",
"sentencia_try : print error",
"sentencia_try : print ';'",
"sentencia_try : iteracion",
"sentencia_try : BREAK ';'",
"sentencia_try : BREAK error",
"$$9 :",
"seleccion : IF cond THEN cuerpo_if ENDIF $$9",
"seleccion : IF cond error cuerpo_if ENDIF",
"seleccion : IF cond THEN cuerpo_if error",
"seleccion : error cond THEN cuerpo_if ENDIF",
"seleccion : IF error THEN cuerpo_if ENDIF",
"cuerpo_if : cuerpo_then ELSE cuerpo_else",
"cuerpo_if : cuerpo_then",
"cuerpo_then : bloque_sentencias",
"cuerpo_else : bloque_sentencias",
"cond : '(' condicion ')'",
"condicion : condicion operadores expresion",
"condicion : expresion",
"operadores : '>'",
"operadores : \"MAYOR_IGUAL\"",
"operadores : \"MENOR_IGUAL\"",
"operadores : '<'",
"operadores : \"IGUAL\"",
"operadores : \"DISTINTO\"",
"operadores : \"AND\"",
"operadores : \"OR\"",
"$$10 :",
"print : PRINT '(' CADENA ')' $$10",
"print : PRINT '(' CADENA error",
"print : PRINT '(' error ')'",
"print : PRINT error CADENA ')'",
"$$11 :",
"iteracion : inicio_while cond cuerpo_while $$11",
"iteracion : error cond cuerpo_while",
"iteracion : inicio_while error cuerpo_while",
"inicio_while : WHILE",
"cuerpo_while : DO bloque_sentencias",
"cuerpo_while : error bloque_sentencias",
};

//#line 236 "gramatica.y"

	ArrayList<Integer> tokens = new ArrayList<>();
	ArrayList<String> sentencias = new ArrayList<>();
	AnalizadorLexico analizador = AnalizadorLexico.GetInstancia();
	private ArrayList<Token> funciones = new ArrayList<>();
	ParserHelper ph = new ParserHelper();

	int yylex(){
		int token = analizador.yylex();
		if(token == 258 || token == 259 ||  token == 274)
			yylval = analizador.getyylval();
		tokens.add(token);
		return token;
	}

	void yyerror(String error){
		ListaError.addErrores(error);
	}

	void addSentencia(String s){
		this.sentencias.add(s);
	}

	public ArrayList<Integer> getTokens(){
		return this.tokens;
	}

	public ArrayList<String> getSentencias(){
		return this.sentencias;
	}

	public boolean chequeoRango(int indice){
		Token t = analizador.getTabla().getToken(indice);
		BigDecimal valor = new BigDecimal(t.getLexema());
		if (t.getTipo() == "ulong")
			if (valor.compareTo(new BigDecimal(0)) == 1)
				return false;
		return true;
	}

	public ParserHelper getParserHelper(){
		return ph;
	}

	public void incrementarPasada(){
		ph.incrementarPasada();
	}

//#line 658 "Parser.java"
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
//#line 21 "gramatica.y"
{ph.completarPaso();}
break;
case 3:
//#line 23 "gramatica.y"
{addSentencia("linea " + analizador.getLinea() + ": bloque try catch");}
break;
case 4:
//#line 23 "gramatica.y"
{ph.apilarBT();}
break;
case 6:
//#line 27 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta CATCH");}
break;
case 7:
//#line 28 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END EN TRY CATCH");}
break;
case 8:
//#line 29 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta BEGIN EN TRY CATCH ");}
break;
case 10:
//#line 33 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico,  falta ; en bloque de sentencias");}
break;
case 11:
//#line 34 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END en bloque de sentencia");}
break;
case 12:
//#line 35 "gramatica.y"
{yyerror( analizador.getLinea() + ": Error sintactico, falta BEGIN en bloque de sentencias");}
break;
case 14:
//#line 37 "gramatica.y"
{yyerror( analizador.getLinea() + ": Error sintactico, falta BEGIN en bloque de sentencias");}
break;
case 15:
//#line 38 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END en bloque de sentencia");}
break;
case 16:
//#line 39 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en bloque de sentencias");}
break;
case 18:
//#line 41 "gramatica.y"
{yyerror( analizador.getLinea()+ ": Error sintactico, falta BEGIN en sentencias ejecutables");}
break;
case 19:
//#line 42 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END en sentencias ejecutables");}
break;
case 20:
//#line 43 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() +": Error sintactico, falta ; en sentencia ejecutables");}
break;
case 24:
//#line 49 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en sentencia declarativa ");}
break;
case 26:
//#line 51 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en declaracion");}
break;
case 27:
//#line 54 "gramatica.y"
{addSentencia("linea " + analizador.getLinea() + ": declaracion de variables");}
break;
case 28:
//#line 55 "gramatica.y"
{addSentencia("linea " + analizador.getLinea() + ": declaracion de funcion");}
break;
case 30:
//#line 59 "gramatica.y"
{ph.setUltimoTipo("var_func");}
break;
case 32:
//#line 60 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta FUNC o tipo");}
break;
case 33:
//#line 63 "gramatica.y"
{ph.setUltimoTipo("double");}
break;
case 34:
//#line 64 "gramatica.y"
{ph.setUltimoTipo("ulong");}
break;
case 35:
//#line 67 "gramatica.y"
{ph.declaracionVar(val_peek(0).ival);}
break;
case 36:
//#line 68 "gramatica.y"
{ph.declaracionVar(val_peek(0).ival);}
break;
case 37:
//#line 69 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ID");}
break;
case 38:
//#line 72 "gramatica.y"
{ph.retrocederAmbito();}
break;
case 39:
//#line 75 "gramatica.y"
{addSentencia("linea " + analizador.getLinea() + ": funcion");}
break;
case 40:
//#line 75 "gramatica.y"
{ph.declaracionFunc(val_peek(1).ival);}
break;
case 42:
//#line 81 "gramatica.y"
{ph.paramFuncion(val_peek(0).ival);}
break;
case 43:
//#line 84 "gramatica.y"
{ph.verificarReturn();}
break;
case 44:
//#line 85 "gramatica.y"
{ph.verificarReturn();}
break;
case 45:
//#line 86 "gramatica.y"
{ph.verificarReturn();}
break;
case 46:
//#line 87 "gramatica.y"
{addSentencia("linea " + analizador.getLinea() + ": funcion con post condicion");}
break;
case 47:
//#line 88 "gramatica.y"
{ph.verificarReturn();}
break;
case 48:
//#line 89 "gramatica.y"
{addSentencia("linea " + analizador.getLinea() + ": funcion con post condicion");}
break;
case 49:
//#line 90 "gramatica.y"
{ph.verificarReturn();}
break;
case 51:
//#line 94 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta BEGIN en funcion");}
break;
case 52:
//#line 95 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, hay sentencias luego del RETURN en la funcion");}
break;
case 53:
//#line 96 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ( en funcion");}
break;
case 54:
//#line 97 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ) en funcion");}
break;
case 55:
//#line 98 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END en funcion");}
break;
case 56:
//#line 99 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta BEGIN en funcion");}
break;
case 57:
//#line 100 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta RETURN en la funcion");}
break;
case 58:
//#line 101 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, hay sentencias despues del RETURN en funcion");}
break;
case 59:
//#line 102 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ( en funcion");}
break;
case 60:
//#line 103 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ) en funcion");}
break;
case 61:
//#line 104 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END en funcion");}
break;
case 62:
//#line 105 "gramatica.y"
{yyerror("Linea " + analizador.getLinea()+ ": Error sintactico, falta BEGIN en funcion");}
break;
case 63:
//#line 106 "gramatica.y"
{yyerror("Linea " + analizador.getLinea()+ ": Error sintactico, falta RETURN en funcion");}
break;
case 64:
//#line 107 "gramatica.y"
{yyerror("Linea " + analizador.getLinea()+ ": Error sintactico, hay sentencias despues del RETURN en funcion");}
break;
case 65:
//#line 108 "gramatica.y"
{yyerror("Linea " + analizador.getLinea()+ ": Error sintactico, falta END en funcion");}
break;
case 66:
//#line 109 "gramatica.y"
{yyerror("Linea " + analizador.getLinea()+ ": Error sintactico, falta POST en funcion");}
break;
case 67:
//#line 110 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta BEGIN en funcion");}
break;
case 68:
//#line 111 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta RETURN en funcion");}
break;
case 69:
//#line 112 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, hay sentencias despues del RETURN en funcion");}
break;
case 70:
//#line 113 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ( en funcion");}
break;
case 71:
//#line 114 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ) en funcion");}
break;
case 72:
//#line 115 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta end en funcion");}
break;
case 73:
//#line 119 "gramatica.y"
{ph.addPaso("+");}
break;
case 74:
//#line 120 "gramatica.y"
{ph.addPaso("-");}
break;
case 76:
//#line 124 "gramatica.y"
{ph.addPaso("*");}
break;
case 77:
//#line 125 "gramatica.y"
{ph.addPaso("/");}
break;
case 79:
//#line 129 "gramatica.y"
{ph.leerID(val_peek(0).ival);}
break;
case 80:
//#line 130 "gramatica.y"
{if(!chequeoRango(val_peek(0).ival)) yyerror("Linea " + analizador.getLinea() + ": Error sintactico, ULONG negativo"); else
               		  analizador.cambiarSignoTabla(val_peek(0).ival);}
break;
case 81:
//#line 131 "gramatica.y"
{ph.leerConstante(val_peek(2).ival, false);}
break;
case 82:
//#line 132 "gramatica.y"
{ph.leerConstante(val_peek(0).ival, true);}
break;
case 83:
//#line 133 "gramatica.y"
{ph.invocarFuncion(val_peek(3).ival, val_peek(1).ival);}
break;
case 87:
//#line 139 "gramatica.y"
{if(!chequeoRango(val_peek(0).ival)) yyerror("Linea " + analizador.getLinea() + ": Error sintactico, ULONG negativo"); else
                               		  analizador.cambiarSignoTabla(val_peek(0).ival);}
break;
case 91:
//#line 147 "gramatica.y"
{addSentencia("linea " + analizador.getLinea() + ": asignacion");}
break;
case 92:
//#line 148 "gramatica.y"
{ph.leerSentencia(val_peek(4).ival);}
break;
case 93:
//#line 149 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta lado derecho de la asignacion ");}
break;
case 96:
//#line 152 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en IF ");}
break;
case 97:
//#line 153 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en PRINT");}
break;
case 100:
//#line 156 "gramatica.y"
{ph.addBreak();}
break;
case 101:
//#line 157 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; BREAK");}
break;
case 103:
//#line 159 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en sentencia TRY");}
break;
case 106:
//#line 166 "gramatica.y"
{addSentencia("linea " + analizador.getLinea() + ": asignacion");}
break;
case 107:
//#line 167 "gramatica.y"
{ph.leerSentencia(val_peek(4).ival);}
break;
case 108:
//#line 168 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta := ");}
break;
case 109:
//#line 169 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en asignacion");}
break;
case 111:
//#line 171 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en IF");}
break;
case 112:
//#line 172 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en PRINT");}
break;
case 115:
//#line 175 "gramatica.y"
{ph.addPaso("break");}
break;
case 116:
//#line 176 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en BREAK");}
break;
case 117:
//#line 179 "gramatica.y"
{addSentencia("linea " + analizador.getLinea() + ": sentencia IF ELSE");}
break;
case 118:
//#line 180 "gramatica.y"
{ph.completarPaso();}
break;
case 119:
//#line 181 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta THEN en IF");}
break;
case 120:
//#line 182 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ENDIF en IF");}
break;
case 121:
//#line 183 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta IF en IF");}
break;
case 122:
//#line 184 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, error en condicion en IF");}
break;
case 125:
//#line 192 "gramatica.y"
{ph.apilarBI();}
break;
case 127:
//#line 199 "gramatica.y"
{ph.apilarBF();}
break;
case 128:
//#line 202 "gramatica.y"
{ph.setOperador();}
break;
case 130:
//#line 206 "gramatica.y"
{ph.guardarOperador(">");}
break;
case 131:
//#line 207 "gramatica.y"
{ph.guardarOperador(">=");}
break;
case 132:
//#line 208 "gramatica.y"
{ph.guardarOperador("<=");}
break;
case 133:
//#line 209 "gramatica.y"
{ph.guardarOperador("<");}
break;
case 134:
//#line 210 "gramatica.y"
{ph.guardarOperador("==");}
break;
case 135:
//#line 211 "gramatica.y"
{ph.guardarOperador("<>");}
break;
case 136:
//#line 212 "gramatica.y"
{ph.guardarOperador("&&");}
break;
case 137:
//#line 213 "gramatica.y"
{ph.guardarOperador("||");}
break;
case 138:
//#line 216 "gramatica.y"
{addSentencia("linea " + analizador.getLinea() + ": sentencia PRINT");}
break;
case 139:
//#line 216 "gramatica.y"
{ph.leerPrint(val_peek(2).ival);}
break;
case 140:
//#line 217 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ) en PRINT");}
break;
case 141:
//#line 218 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta CADENA en PRINT");}
break;
case 142:
//#line 219 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ( en PRINT");}
break;
case 143:
//#line 222 "gramatica.y"
{addSentencia("linea " + analizador.getLinea() + ": sentencia WHILE");}
break;
case 144:
//#line 223 "gramatica.y"
{ph.finWhile();}
break;
case 145:
//#line 224 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta WHILE en WHILE");}
break;
case 146:
//#line 225 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, error en WHILE");}
break;
case 147:
//#line 228 "gramatica.y"
{ph.apilarPasoWhile();}
break;
case 149:
//#line 232 "gramatica.y"
{yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta DO en WHILE");}
break;
//#line 1261 "Parser.java"
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
