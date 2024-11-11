%{
	package AnalizadorSintactico;
	import java.util.ArrayList;
	import AnalizadorLexico.*;
	import java.math.BigDecimal;
	import utilidades.ListaError;
%}
%token ASIG ID CTE IF THEN ELSE ENDIF PRINT FUNC RETURN BEGIN END BREAK ULONG DOUBLE WHILE DO CADENA TRY POST CATCH ERROR

%left 'OR'
%left 'AND'
%left '>' '<' 'DISTINTO' 'MAYOR_IGUAL' 'MENOR_IGUAL' 'IGUAL'
%left '+' '-'
%left '*' '/'

%%

programa : ID bloque_sentencias
         ;

bloque_try : bloque_try_comienzo bloque_catch {ph.completarPaso();}

bloque_try_comienzo : TRY sentencia_try  {addSentencia("linea " + analizador.getLinea() + ": bloque try catch");} {ph.apilarBT();}
	   ;

bloque_catch : CATCH BEGIN sentencia_compuesta_try END
	     | error BEGIN sentencia_compuesta_try END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta CATCH");}
	     | CATCH BEGIN sentencia_compuesta_try error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END EN TRY CATCH");}
	     | CATCH error sentencia_compuesta_try END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta BEGIN EN TRY CATCH ");}
	     ;

bloque_sentencias : sentencia_declarativa BEGIN sentencia_compuesta END ';'
                  | sentencia_declarativa BEGIN sentencia_compuesta END error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico,  falta ; en bloque de sentencias");}
		  | sentencia_declarativa BEGIN sentencia_compuesta error ';' {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END en bloque de sentencia");}
                  | sentencia_declarativa error sentencia_compuesta END';'{yyerror( analizador.getLinea() + ": Error sintactico, falta BEGIN en bloque de sentencias");}
                  | sentencia_declarativa BEGIN END ';'
                  | sentencia_declarativa error END ';' {yyerror( analizador.getLinea() + ": Error sintactico, falta BEGIN en bloque de sentencias");}
                  | sentencia_declarativa BEGIN error ';' {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END en bloque de sentencia");}
                  | sentencia_declarativa BEGIN END error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en bloque de sentencias");}
                  | BEGIN sentencia_compuesta END ';'
                  | error sentencia_compuesta  END ';' {yyerror( analizador.getLinea()+ ": Error sintactico, falta BEGIN en sentencias ejecutables");}
                  | BEGIN sentencia_compuesta  error ';' {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END en sentencias ejecutables");}
                  | BEGIN sentencia_compuesta END error {yyerror("Linea " + analizador.getLinea() +": Error sintactico, falta ; en sentencia ejecutables");}
                  | BEGIN END ';'
		  | sentencia_simple
		  ;

sentencia_declarativa:	sentencia_declarativa declaracion ';'
		      	| sentencia_declarativa declaracion error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en sentencia declarativa ");}
                  	| declaracion ';'
	             	| declaracion error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en declaracion");}
                      	;

declaracion : declaracion_var {addSentencia("linea " + analizador.getLinea() + ": declaracion de variables");}
            | func {addSentencia("linea " + analizador.getLinea() + ": declaracion de funcion");}
	    ;

declaracion_var : tipo lista_var
		| FUNC {ph.setUltimoTipo("var_func");} lista_var
		| error lista_var {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta FUNC o tipo");}
                ;

tipo : DOUBLE {ph.setUltimoTipo("double");}
     | ULONG {ph.setUltimoTipo("ulong");}
     ;

lista_var : lista_var ',' ID {ph.declaracionVar($3.ival);}
          | ID {ph.declaracionVar($1.ival);}
          | error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ID");}
	  ;

func : declaracion_func parametro_func cuerpo_func {ph.retrocederAmbito();}
     ;

declaracion_func : tipo FUNC ID {addSentencia("linea " + analizador.getLinea() + ": funcion");} {ph.declaracionFunc($3.ival);}
		 ;

parametro_func : '(' parametro ')'
	       ;

parametro : tipo ID {ph.paramFuncion($2.ival);}
	  ;

cuerpo_func : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';'  END {ph.verificarReturn();}
            | BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' END {ph.verificarReturn();}
	    | sentencia_declarativa BEGIN RETURN '(' expresion ')' ';' END {ph.verificarReturn();}
	    | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' POST ':''(' condicion ')' ';' END {addSentencia("linea " + analizador.getLinea() + ": funcion con post condicion");}
	    														     {ph.verificarReturn();}
            | BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' POST ':''(' condicion ')' ';' END {addSentencia("linea " + analizador.getLinea() + ": funcion con post condicion");}
              	    														     {ph.verificarReturn();}
	    | cuerpo_func_error
	    ;

cuerpo_func_error : sentencia_declarativa error sentencia_compuesta RETURN '(' expresion ')' ';'  END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta BEGIN en funcion");}
                  | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, hay sentencias luego del RETURN en la funcion");}
                  | sentencia_declarativa BEGIN sentencia_compuesta RETURN error expresion ')' ';'  END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ( en funcion");}
                  | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion error ';'  END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ) en funcion");}
                  | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';'  error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END en funcion");}
                  | error sentencia_compuesta RETURN '(' expresion ')' ';' END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta BEGIN en funcion");}
                  | BEGIN sentencia_compuesta '(' expresion ')' ';' END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta RETURN en la funcion");}
                  | BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, hay sentencias despues del RETURN en funcion");}
                  | BEGIN sentencia_compuesta RETURN error expresion ')' ';' END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ( en funcion");}
                  | BEGIN sentencia_compuesta RETURN '(' expresion error ';' END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ) en funcion");}
                  | BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta END en funcion");}
                  | sentencia_declarativa error sentencia_compuesta RETURN '(' expresion ')' ';' POST ':''(' condicion ')' ';' END {yyerror("Linea " + analizador.getLinea()+ ": Error sintactico, falta BEGIN en funcion");}
                  | sentencia_declarativa BEGIN sentencia_compuesta '(' expresion ')' ';' POST ':''(' condicion ')' ';' END {yyerror("Linea " + analizador.getLinea()+ ": Error sintactico, falta RETURN en funcion");}
                  | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' error ';' POST ':''(' condicion ')' ';' END {yyerror("Linea " + analizador.getLinea()+ ": Error sintactico, hay sentencias despues del RETURN en funcion");}
                  | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' POST ':''(' condicion ')' ';' error {yyerror("Linea " + analizador.getLinea()+ ": Error sintactico, falta END en funcion");}
                  | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error ':''(' condicion ')' ';' END {yyerror("Linea " + analizador.getLinea()+ ": Error sintactico, falta POST en funcion");}
                  | sentencia_declarativa error RETURN '(' expresion ')' ';' END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta BEGIN en funcion");}
                  | sentencia_declarativa BEGIN '(' expresion ')' ';' END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta RETURN en funcion");}
                  | sentencia_declarativa BEGIN RETURN '(' expresion ')' ';' error END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, hay sentencias despues del RETURN en funcion");}
                  | sentencia_declarativa BEGIN RETURN error expresion ')' ';' END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ( en funcion");}
                  | sentencia_declarativa BEGIN RETURN '(' expresion error ';' END {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ) en funcion");}
                  | sentencia_declarativa BEGIN RETURN '(' expresion ')' ';' error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta end en funcion");}
		  ;


expresion : expresion '+' termino {ph.addPaso("+");}
          | expresion '-' termino {ph.addPaso("-");}
          | termino
   	  ;

termino : termino '*' factor {ph.addPaso("*");}
       	| termino '/' factor {ph.addPaso("/");}
       	| factor
       	;

factor : ID {ph.leerID($1.ival);}
       | '-' CTE {if(!chequeoRango($2.ival)) yyerror("Linea " + analizador.getLinea() + ": Error sintactico, ULONG negativo"); else
               		  analizador.cambiarSignoTabla($2.ival);} {ph.leerConstante($1.ival, false);}
       | CTE {ph.leerConstante($1.ival, true);}
       | ID '(' parametro_inv ')' {ph.invocarFuncion($1.ival, $3.ival);}
       | ERROR
       | '-' ERROR
       ;

parametro_inv : ID
              | '-' CTE {if(!chequeoRango($2.ival)) yyerror("Linea " + analizador.getLinea() + ": Error sintactico, ULONG negativo"); else
                               		  analizador.cambiarSignoTabla($2.ival);}
              | CTE

sentencia_compuesta : sentencia_compuesta sentencia_simple
            	    | sentencia_simple
		    ;

sentencia_simple : ID ASIG expresion ';' {addSentencia("linea " + analizador.getLinea() + ": asignacion");}
					 {ph.leerSentencia($1.ival);}
		 | ID ASIG error ';' {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta lado derecho de la asignacion ");}
                 | ID ERROR expresion ';'
               	 | seleccion ';'
               	 | seleccion error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en IF ");}
                 | print error  {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en PRINT");}
                 | print ';'
                 | iteracion
                 | BREAK ';' {ph.addBreak();};
                 | BREAK error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; BREAK");}
                 | bloque_try ';'
                 | bloque_try error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en sentencia TRY");}
		 ;

sentencia_compuesta_try : sentencia_compuesta_try sentencia_try
            	    	| sentencia_try
		    	;

sentencia_try: ID ASIG expresion ';' {addSentencia("linea " + analizador.getLinea() + ": asignacion");}
				     {ph.leerSentencia($1.ival);}
             | ID error expresion ';' {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta := ");}
             | ID ASIG expresion error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en asignacion");}
             | seleccion ';'
             | seleccion error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en IF");}
             | print error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en PRINT");}
             | print ';'
             | iteracion
             | BREAK ';' {ph.addPaso("break");}
             | BREAK error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ; en BREAK");}
	     ;

seleccion : IF cond THEN cuerpo_if ENDIF {addSentencia("linea " + analizador.getLinea() + ": sentencia IF ELSE");}
						      {ph.completarPaso();}
          | IF cond error cuerpo_if ENDIF {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta THEN en IF");}
          | IF cond THEN cuerpo_if error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ENDIF en IF");}
	  | error cond THEN cuerpo_if ENDIF {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta IF en IF");}
	  | IF error THEN cuerpo_if ENDIF {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, error en condicion en IF");}
	  ;

cuerpo_if : cuerpo_then ELSE cuerpo_else
	  | cuerpo_then
	  ;


cuerpo_then : bloque_sentencias {ph.apilarBI();}
	    ;


cuerpo_else : bloque_sentencias
	    ;

cond : '(' condicion ')' {ph.apilarBF();}
     ;

condicion : condicion operadores expresion {ph.setOperador();}
	  | expresion
	  ;

operadores : '>' {ph.guardarOperador(">");}
           | 'MAYOR_IGUAL' {ph.guardarOperador(">=");}
           | 'MENOR_IGUAL' {ph.guardarOperador("<=");}
           | '<' {ph.guardarOperador("<");}
           | 'IGUAL' {ph.guardarOperador("==");}
           | 'DISTINTO' {ph.guardarOperador("<>");}
           | 'AND' {ph.guardarOperador("&&");}
           | 'OR' {ph.guardarOperador("||");}
	   ;

print : PRINT '(' CADENA ')' {addSentencia("linea " + analizador.getLinea() + ": sentencia PRINT");} {ph.leerPrint($3.ival);}
      |	PRINT '(' CADENA error {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ) en PRINT");}
      | PRINT '(' error ')' {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta CADENA en PRINT");}
      | PRINT error CADENA')' {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta ( en PRINT");}
      ;

iteracion : inicio_while cond cuerpo_while {addSentencia("linea " + analizador.getLinea() + ": sentencia WHILE");}
							{ph.finWhile();}
          | error cond cuerpo_while {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta WHILE en WHILE");}
          | inicio_while error cuerpo_while {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, error en WHILE");}
          ;

inicio_while : WHILE {ph.apilarPasoWhile();}
	     ;

cuerpo_while : DO bloque_sentencias
	     | error bloque_sentencias {yyerror("Linea " + analizador.getLinea() + ": Error sintactico, falta DO en WHILE");}
	     ;

%%

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

