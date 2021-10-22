%{
	package AnalizadorSintactico;
	import java.util.ArrayList;
	import AnalizadorLexico.*;
	import java.math.BigDecimal;
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
bloque_try : TRY sentencia_try CATCH BEGIN sentencia_compuesta_try END {addSentencia("linea " + analizador.getLinea() + ": bloque try catch");}
	   | TRY sentencia_try error BEGIN sentencia_compuesta_try END {yyerror("Linea " + analizador.getLinea() + ": falta CATCH");}
       	   | TRY sentencia_try CATCH BEGIN sentencia_compuesta_try error {yyerror("Linea " + analizador.getLinea() + ": falta END EN TRY CATCH");}
       	   | TRY sentencia_try CATCH error sentencia_compuesta_try END {yyerror("Linea " + analizador.getLinea() + ": falta BEGIN EN TRY CATCH ");}
           ;

bloque_sentencias : sentencia_declarativa BEGIN sentencia_compuesta END ';'
                  | sentencia_declarativa BEGIN sentencia_compuesta END error {yyerror("Linea " + analizador.getLinea() + ": falta ; en bloque de sentencias");}
		  | sentencia_declarativa BEGIN sentencia_compuesta error ';' {yyerror("Linea " + analizador.getLinea() + ": falta END en bloque de sentencia");}
                  | BEGIN sentencia_compuesta END ';'
		  | sentencia_simple
                  | sentencia_declarativa error sentencia_compuesta END';'{yyerror( analizador.getLinea() + ": falta BEGIN en bloque de sentencias");}
                  | error sentencia_compuesta  END ';' {yyerror( analizador.getLinea()+ ": falta BEGIN en sentencias ejecutables");}
                  | BEGIN sentencia_compuesta  error ';' {yyerror("Linea " + analizador.getLinea() + ": falta END en sentencias ejecutables");}
                  | BEGIN sentencia_compuesta END error {yyerror("Linea " + analizador.getLinea() +": falta ; en sentencia ejecutables");}
		  ;
sentencia_declarativa:	sentencia_declarativa declaracion ';'
		      	| sentencia_declarativa declaracion error {yyerror("Linea " + analizador.getLinea() + ": falta ; en sentencia declarativa ");}
                  	| declaracion ';'
	             	| declaracion error {yyerror("Linea " + analizador.getLinea() + ": falta ; en declaracion");}
                      	;
declaracion : declaracion_var {addSentencia("linea " + analizador.getLinea() + ": declaracion de variables");}
            | declaracion_func {addSentencia("linea " + analizador.getLinea() + ": declaracion de funcion");}
	    ;
declaracion_var : tipo lista_var
		| FUNC lista_var
		| error lista_var {yyerror("Linea " + analizador.getLinea() + ": falta FUNC o tipo");}
                ;
tipo : DOUBLE
     | ULONG
     ;
lista_var : lista_var ',' ID
          | ID
          | error {yyerror("Linea " + analizador.getLinea() + ": falta ID");}
	  ;

declaracion_func : tipo FUNC ID '(' parametro ')' cuerpo_func {addSentencia("linea " + analizador.getLinea() + ": funcion");}
		 ;
parametro : tipo ID
	  ;
cuerpo_func : sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';'  END
            | sentencia_declarativa error sentencia_compuesta RETURN '(' expresion ')' ';'  END {yyerror("Linea " + analizador.getLinea() + ": falta BEGIN en funcion");}
            | sentencia_declarativa BEGIN sentencia_compuesta RETURN error expresion ')' ';'  END {yyerror("Linea " + analizador.getLinea() + ": falta ( en funcion");}
            | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion error ';'  END {yyerror("Linea " + analizador.getLinea() + ": falta ) en funcion");}
            | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';'  error {yyerror("Linea " + analizador.getLinea() + ": falta END en funcion");}
	    | BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' END
	    | error sentencia_compuesta RETURN '(' expresion ')' ';' END {yyerror("Linea " + analizador.getLinea() + ": falta BEGIN en funcion");}
	    | BEGIN sentencia_compuesta RETURN error expresion ')' ';' END {yyerror("Linea " + analizador.getLinea() + ": falta ( en funcion");}
	    | BEGIN sentencia_compuesta RETURN '(' expresion error ';' END {yyerror("Linea " + analizador.getLinea() + ": falta ) en funcion");}
	    | BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error {yyerror("Linea " + analizador.getLinea() + ": falta END en funcion");}
	    | sentencia_declarativa error RETURN '(' expresion ')' ';' END {yyerror("Linea " + analizador.getLinea() + ": falta BEGIN en funcion");}
	    | sentencia_declarativa BEGIN RETURN error expresion ')' ';' END {yyerror("Linea " + analizador.getLinea() + ": falta ( en funcion");}
	    | sentencia_declarativa BEGIN RETURN '(' expresion error ';' END {yyerror("Linea " + analizador.getLinea() + ": falta ) en funcion");}
	    | sentencia_declarativa BEGIN RETURN '(' expresion ')' ';' error {yyerror("Linea " + analizador.getLinea() + ": falta end en funcion");}
	    | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' POST ':''(' condicion ')' ';' END {addSentencia("linea " + analizador.getLinea() + ": funcion con post condicion");}
	    | sentencia_declarativa error sentencia_compuesta RETURN '(' expresion ')' ';' POST ':''(' condicion ')' ';' END {yyerror("Linea " + analizador.getLinea()+ ": falta BEGIN en funcion");}
        | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' POST ':''(' condicion ')' ';' error {yyerror("Linea " + analizador.getLinea()+ ": falta END en funcion");}
        | sentencia_declarativa BEGIN sentencia_compuesta RETURN '(' expresion ')' ';' error ':''(' condicion ')' ';' END {yyerror("Linea " + analizador.getLinea()+ ": falta POST en funcion");}
	    ;
expresion : expresion '+' termino
          | expresion '-' termino 
          | termino
   	  ;
termino : termino '*' factor 
       	| termino '/' factor 
       	| factor
       	;
factor : ID
       | '-' CTE {if(!chequeoRango($2.ival)) yyerror("Linea " + analizador.getLinea() + ": ULONG negativo"); else
               		  analizador.cambiarSignoTabla($2.ival);}
       | CTE
       | ID '(' parametro ')'
       | ERROR
       | '-' ERROR
       ;

sentencia_compuesta : sentencia_compuesta sentencia_simple
            	    | sentencia_simple
		    ;
sentencia_simple : ID ASIG expresion ';' {addSentencia("linea " + analizador.getLinea() + ": asignacion");}
                 | ID ERROR expresion ';'
               	 | seleccion ';'
               	 | seleccion error {yyerror("Linea " + analizador.getLinea() + ": falta ; en IF ");}
                 | print error  {yyerror("Linea " + analizador.getLinea() + ": falta ; en PRINT");}
                 | print ';'
                 | iteracion
                 | BREAK ';'
                 | BREAK error {yyerror("Linea " + analizador.getLinea() + ": falta ; BREAK");}
                 | bloque_try ';'
                 | bloque_try error {yyerror("Linea " + analizador.getLinea() + ": falta ; en sentencia TRY");}
		 ;

sentencia_compuesta_try : sentencia_compuesta_try sentencia_try
            	    	| sentencia_try
		    	;

sentencia_try: ID ASIG expresion ';' {addSentencia("linea " + analizador.getLinea() + ": asignacion");}
             | ID error expresion ';' {yyerror("Linea " + analizador.getLinea() + ": falta := ");}
             | ID ASIG expresion error {yyerror("Linea " + analizador.getLinea() + ": falta ; en asignacion");}
             | seleccion ';'
             | seleccion error {yyerror("Linea " + analizador.getLinea() + "falta ; en IF");}
             | print error {yyerror("Linea " + analizador.getLinea() + ": falta ; en PRINT");}
             | print ';'
             | iteracion
             | BREAK ';'
             | BREAK error {yyerror("Linea " + analizador.getLinea() + ": falta ; en BREAK");}


seleccion : IF '(' condicion ')'  THEN bloque_sentencias ELSE bloque_sentencias ENDIF {addSentencia("linea " + analizador.getLinea() + ": sentencia IF ELSE");}
          | IF error condicion ')'  THEN bloque_sentencias ELSE bloque_sentencias ENDIF {yyerror("Linea " + analizador.getLinea() + ": falta ( en IF");}
 	  | IF '(' condicion error THEN bloque_sentencias ELSE bloque_sentencias ENDIF {yyerror("Linea " + analizador.getLinea() + ": falta ) en IF");}
	  | IF '(' condicion ')'  THEN bloque_sentencias ELSE bloque_sentencias error {yyerror("Linea " + analizador.getLinea() + ": falta ENDIF en IF");}
          | IF '(' condicion ')'  THEN bloque_sentencias ENDIF {addSentencia("linea " + analizador.getLinea() + ": sentencia IF sin ELSE");}
          | IF '(' condicion ')'  error bloque_sentencias ELSE bloque_sentencias ENDIF {yyerror("Linea " + analizador.getLinea() + ": falta THEN en IF");}
          | IF error condicion ')'  THEN bloque_sentencias ENDIF {yyerror("Linea " + analizador.getLinea() + ": falta ( en IF");}
          | IF '(' condicion error  THEN bloque_sentencias ENDIF {yyerror("Linea " + analizador.getLinea() + ": falta ) en IF");}
          | IF '(' condicion ')'  error bloque_sentencias ENDIF {yyerror("Linea " + analizador.getLinea() + ": falta THEN en IF");}
          | IF '(' condicion ')'  THEN bloque_sentencias error {yyerror("Linea " + analizador.getLinea() + ": falta ENDIF en IF");}
          | error '(' condicion ')'  THEN bloque_sentencias ENDIF {yyerror("Linea " + analizador.getLinea() + ": falta IF en IF");}
	  | error '(' condicion ')'  THEN bloque_sentencias ELSE bloque_sentencias ENDIF {yyerror("Linea " + analizador.getLinea() + ": falta IF en IF");}
	  ;
condicion : expresion operadores expresion
          | expresion
	  ;
operadores : '>' 
           | 'MAYOR_IGUAL' 
           | 'MENOR_IGUAL' 
           | '<'
           | 'IGUAL' 
           | 'DISTINTO' 
           | 'AND' 
           | 'OR'
	   ;
print : PRINT '(' CADENA ')' {addSentencia("linea " + analizador.getLinea() + ": sentencia PRINT");}
      |	PRINT '(' CADENA error {yyerror("Linea " + analizador.getLinea() + ": falta ) en PRINT");}
      | PRINT '(' error ')' {yyerror("Linea " + analizador.getLinea() + ": falta CADENA en PRINT");}
      | PRINT error CADENA')' {yyerror("Linea " + analizador.getLinea() + ": falta ( en PRINT");}
      ;
iteracion : WHILE '(' condicion ')' DO bloque_sentencias {addSentencia("linea " + analizador.getLinea() + ": sentencia WHILE");}
	  | error '(' condicion ')' DO bloque_sentencias {yyerror("Linea " + analizador.getLinea() + ": falta WHILE en WHILE");}
	  | WHILE '(' condicion ')' error bloque_sentencias {yyerror("Linea " + analizador.getLinea() + ": falta DO en WHILE");}
      	  | WHILE error condicion ')' DO bloque_sentencias {yyerror("Linea " + analizador.getLinea() + ": falta ( en WHILE");}
          | WHILE '(' condicion error DO bloque_sentencias {yyerror("Linea " + analizador.getLinea() + ": falta ) en WHILE");}
          ;
          
%%

	ArrayList<String> erroresSintacticos = new ArrayList<>();
	ArrayList<Integer> tokens = new ArrayList<>();
	ArrayList<String> sentencias = new ArrayList<>();
	Analizador analizador = Analizador.GetInstancia();
	ArrayList<Integer> = new ArrayList<>();


	int yylex(){
		int token = analizador.yylex();
		if(token == 258 || token == 259 ||  token == 274)
			yylval = analizador.getyylval();
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
		Token t = analizador.getTabla().getToken(indice);
		BigDecimal valor = new BigDecimal(t.getLexema());
		if (t.getTipo() == "ulong")
			if (valor.compareTo(new BigDecimal(0)) == 1)
				return false;
		return true;
	}
