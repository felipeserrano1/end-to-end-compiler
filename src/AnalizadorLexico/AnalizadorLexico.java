package AnalizadorLexico;

import AccionesSemanticas.*;
import AnalizadorSintactico.ParserVal;
import utilidades.*;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class AnalizadorLexico {

    private static AnalizadorLexico instancia;

    private int linea = 1;
    AtomicInteger indice = new AtomicInteger(0);
    private ArrayList<Integer> archivo = new ArrayList<>();
    private TablaSimbolos tabla;
    private HashMap<String, Integer> tokenIdentificacion = new HashMap<>();
    private HashMap<Character, Integer> columnaMatriz = new HashMap<>();
    private TokenLexema tokenActual = null;

    private AccionSemantica1 as1 =  new AccionSemantica1(indice);
    private AccionSemantica2 as2 = new AccionSemantica2(indice);
    private AccionSemantica3 as3 = new AccionSemantica3(indice);
    private AccionSemantica4 as4 = new AccionSemantica4(indice);
    private AccionSemantica5 as5 = new AccionSemantica5(indice);
    private AccionSemantica6 as6 = new AccionSemantica6(indice);
    private AccionSemantica7 as7 = new AccionSemantica7(indice);
    private AccionSemantica8 as8 = new AccionSemantica8(indice);
    private AccionSemantica9 as9 = new AccionSemantica9(indice);
    private AccionSemantica10 as10 = new AccionSemantica10(indice);
    private AccionSemantica11 as11 = new AccionSemantica11(indice);
    private AccionSemantica12 as12 = new AccionSemantica12(indice);

    public void reset(){
        linea = 1;
        indice.set(0);
        archivo = new ArrayList<>();
        tokenActual = null;

    }

    private int[][] matrizEstados ={
            {1, 12, 2, 17, 13, 17, 17, 17, 17, 17, 17, 1, 5, 6, 10, 7, 8, 11, 9, 0, 0, 18, 1},
            {1, 1, 17, 17, 17, 17, 17, 17, 17, 17, 17, 1, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 1},
            {17, 17, 3, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17},
            {3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17},
            {17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17},
            {18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 17, 18, 18, 18, 18, 18, 18, 18},
            {17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17},
            {18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 17, 18, 18, 18, 18},
            {18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 17, 18, 18, 18, 18, 18, 18, 18, 18},
            {11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 17, 11, 11, 11, 11, 11},
            {17, 12, 17, 17, 14, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17},
            {18, 14, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18},
            {17, 14, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 15},
            {18, 16, 18, 18, 18, 18, 18, 18, 18, 16, 16, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18},
            {17, 16, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17}
    };

    private AccionSemantica[][] matrizAcciones = {
            {as1, as1, as1, as4, as1, as4, as4, as4, as4, as4, as4, as1, as1, as1, as1, as1, as1, as1, as1, as11, as11, as9, as1},
            {as2, as2, as3, as3, as3, as3, as3, as3, as3, as3, as3, as2, as3, as3, as3, as3, as3, as3, as3, as3, as3, as3, as2},
            {as7, as7, as2, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7},
            {as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2},
            {as2, as2, as6, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2},
            {as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as8, as7, as7, as8, as7, as7, as7, as7, as7, as7, as7},
            {as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as8, as7, as7, as7, as7, as7, as7, as7},
            {as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as8, as9, as9, as9, as9, as9, as9, as9},
            {as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as7, as8, as7, as7, as7, as7, as7, as7, as7},
            {as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as8, as9, as9, as9, as8, as9, as9, as9, as9},
            {as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as8, as9, as9, as9, as9, as9, as9, as9, as9},
            {as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as2, as12, as2, as2, as2, as2, as2},
            {as10, as2, as10, as10, as2, as10, as10, as10, as10, as10, as10, as10, as10, as10, as10, as10, as10, as10, as10, as10, as10, as10, as10},
            {as9, as2, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9},
            {as5, as2, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as2},
            {as9, as2, as9, as9, as9, as9, as9, as9, as9, as2, as2, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9, as9},
            {as5, as2, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5, as5}
    };


    private AnalizadorLexico(){
        this.cargarTokens();
        this.cargarMapa();
    }

    private void cargarMapa() {
        this.columnaMatriz.put('l',0);
        this.columnaMatriz.put('d',1);
        this.columnaMatriz.put('/',2);
        this.columnaMatriz.put('*',3);
        this.columnaMatriz.put('.',4);
        this.columnaMatriz.put('(',5);
        this.columnaMatriz.put(')',6);
        this.columnaMatriz.put(',',7);
        this.columnaMatriz.put(';',8);
        this.columnaMatriz.put('+',9);
        this.columnaMatriz.put('-',10);
        this.columnaMatriz.put('_',11);
        this.columnaMatriz.put('<',12);
        this.columnaMatriz.put('>',13);
        this.columnaMatriz.put('|',14);
        this.columnaMatriz.put('=',15);
        this.columnaMatriz.put(':',16);
        this.columnaMatriz.put('%',17);
        this.columnaMatriz.put('&',18);
        this.columnaMatriz.put('\t',19);
        this.columnaMatriz.put(' ', 19);
        this.columnaMatriz.put('\n',20);
        this.columnaMatriz.put('c',21);
        this.columnaMatriz.put('E',22);
    }

    private void cargarTokens() {
        this.tokenIdentificacion.put("/",47);
        this.tokenIdentificacion.put("*",42);
        this.tokenIdentificacion.put(".",46);
        this.tokenIdentificacion.put("(",40);
        this.tokenIdentificacion.put(")",41);
        this.tokenIdentificacion.put(",",44);
        this.tokenIdentificacion.put(":",58);
        this.tokenIdentificacion.put(";",59);
        this.tokenIdentificacion.put("+",43);
        this.tokenIdentificacion.put("-",45);
        this.tokenIdentificacion.put("_",95);
        this.tokenIdentificacion.put("<",60);
        this.tokenIdentificacion.put(">",62);
        this.tokenIdentificacion.put("%",37);
        this.tokenIdentificacion.put(":=",257);
        this.tokenIdentificacion.put("ID",258);
        this.tokenIdentificacion.put("CTE",259);
        this.tokenIdentificacion.put("IF",260);
        this.tokenIdentificacion.put("THEN",261);
        this.tokenIdentificacion.put("ELSE",262);
        this.tokenIdentificacion.put("ENDIF",263);
        this.tokenIdentificacion.put("PRINT",264);
        this.tokenIdentificacion.put("FUNC",265);
        this.tokenIdentificacion.put("RETURN",266);
        this.tokenIdentificacion.put("BEGIN",267);
        this.tokenIdentificacion.put("END",268);
        this.tokenIdentificacion.put("BREAK",269);
        this.tokenIdentificacion.put("ULONG",270);
        this.tokenIdentificacion.put("DOUBLE",271);
        this.tokenIdentificacion.put("WHILE",272);
        this.tokenIdentificacion.put("DO",273);
        this.tokenIdentificacion.put("CADENA", 274);
        this.tokenIdentificacion.put("TRY", 275);
        this.tokenIdentificacion.put("POST", 276);
        this.tokenIdentificacion.put("CATCH", 277);
        this.tokenIdentificacion.put("ERROR", 278);
        this.tokenIdentificacion.put("||",279);
        this.tokenIdentificacion.put("&&",280);
        this.tokenIdentificacion.put("<>",281);
        this.tokenIdentificacion.put(">=",282);
        this.tokenIdentificacion.put("<=",283);
        this.tokenIdentificacion.put("==",284);
    }

    public static AnalizadorLexico GetInstancia(){
        if (instancia == null)
            instancia = new AnalizadorLexico();
        return instancia;
    }

    public void leer(String direccion) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(direccion));
        int ascii;
        while((ascii = br.read()) != -1) {
            archivo.add(ascii);
        }
        archivo.add(36);
    }

    public TokenLexema leerToken() {
        int estado = 0;
        int estadoAnt;
        char caracter;
        char caracterTabla;
        int ascii;
        while(indice.intValue() < this.archivo.size()){
            ascii = this.archivo.get(indice.intValue());
            caracter = (char) ascii;
            caracterTabla = detectarCaracter(caracter);
            TokenLexema tokenLexema = this.matrizAcciones[estado][this.columnaMatriz.get(caracterTabla)].accion(caracter);
            estadoAnt = estado;
            estado = this.matrizEstados[estado][this.columnaMatriz.get(caracterTabla)];
            if ((ascii == 10) && (estado == 0 || estado == 3|| estado == 4)){
                this.linea++;
            }
            else if (estado == 17){
                String tipoToken = this.matrizAcciones[estadoAnt][estado].devolver();
                tokenLexema.setID(this.tokenIdentificacion.get(tipoToken));
                if (this.tokenIdentificacion.get(tipoToken) == 278)
                    ListaError.addErrores("Linea " + this.linea + ": Error Lexico, constante fuera de rango");

                return tokenLexema;
            }
            else if(ascii == 36 && estado != 3){ // $ pero fuera de comentario, fin archivo
                return null;
            }
            else if(estado == 18) { // Se encontro un error
                estado = 0;
                ListaError.addErrores("Linea " + this.linea + ": Error Lexico, no se puede agregar " + this.matrizAcciones[estadoAnt][estado].devolver());
                TokenLexema error = new TokenLexema(-1);
                error.setID(278);
                return error;
            }
        }
        if (estado == 2 || estado == 3 || estado == 4){
            ListaError.addErrores("Linea " + this.linea + ": Error Lexico, no se cerro el comentario al final del archivo ");
            return null;
        }
        return null;
    }

    private char detectarCaracter(int ascii) {
        if (ascii == 69)
            return (char)ascii;
        else if ((ascii >= 65 && ascii <= 90) || (ascii >= 97) && (ascii <= 122))
            return 'l';
        else if (ascii >= 48 && ascii <= 57)
            return 'd';
        else if (columnaMatriz.containsKey((char) ascii))
            return (char) ascii;
        else
            return 'c';
    }

    public int yylex(){
        tokenActual = this.leerToken();
        if (tokenActual != null){
            return tokenActual.getId();
        }
        else
            return 0;
    }

    public void cambiarSignoTabla(int indice){
        if(indice != -1){
            Token t = this.tabla.getToken(indice);
            t.setLexema('-' + t.getLexema());
        }
    }

    public ParserVal getyylval (){
        return new ParserVal(tokenActual.getIndiceTabla());
    }

    public int getLinea(){
        return this.linea;
    }

    public TablaSimbolos getTabla(){
        return this.tabla;
    }

    public void setTabla(TablaSimbolos tablaSimbolos) {
        this.tabla = tablaSimbolos;
        this.as1.setTabla(tablaSimbolos);
        this.as2.setTabla(tablaSimbolos);
        this.as3.setTabla(tablaSimbolos);
        this.as4.setTabla(tablaSimbolos);
        this.as5.setTabla(tablaSimbolos);
        this.as6.setTabla(tablaSimbolos);
        this.as7.setTabla(tablaSimbolos);
        this.as8.setTabla(tablaSimbolos);
        this.as9.setTabla(tablaSimbolos);
        this.as10.setTabla(tablaSimbolos);
        this.as11.setTabla(tablaSimbolos);
        this.as12.setTabla(tablaSimbolos);
    }
}