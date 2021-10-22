import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.TablaSimbolos;
import AnalizadorSintactico.Parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TablaSimbolos tablaSimbolos = new TablaSimbolos();
        AnalizadorLexico analizadorLexico = AnalizadorLexico.GetInstancia();
        analizadorLexico.setTabla(tablaSimbolos);
        Parser parser = new Parser(false);
        analizadorLexico.leer(args[0]);

        System.out.println("-------- Tokens leidos en AnalizadorLexico Lexico --------");
        parser.run();

        System.out.println("-------- Errores lexicos --------");
        for(String s : analizadorLexico.getErroresLexicos())
            System.out.println(s + "\n");

        System.out.println("-------- Errorres Sintacticos --------");
        for(String s : parser.getErroresSintacticos())
            System.out.println(s + "\n");

        System.out.println("-------- Tokens recibidos por analizadorLexico sintactico --------");
        System.out.println(parser.getTokens());
        System.out.println("-------- Lista de sentencias --------");
        for(String s : parser.getSentencias())
            System.out.println(s + "\n");
        System.out.println("-------- Tabla de simbolos --------");
        System.out.println(analizadorLexico.getTabla());


    }
}
