import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.TablaSimbolos;
import AnalizadorSintactico.Parser;
import CodigoIntermedio.Polaca;
import GeneracionASM.GeneradorAsm;
import utilidades.ListaError;

import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws IOException {
        TablaSimbolos tablaSimbolos = new TablaSimbolos();
        AnalizadorLexico analizadorLexico = AnalizadorLexico.GetInstancia();
        analizadorLexico.setTabla(tablaSimbolos);
        Parser parser = new Parser(false);
        analizadorLexico.leer(args[0]);
        parser.run();
        analizadorLexico.reset();
        ListaError.reset();

        System.out.println(" incrementar pasada ");
        parser.incrementarPasada();
        tablaSimbolos = new TablaSimbolos();
        analizadorLexico.setTabla(tablaSimbolos);
        analizadorLexico.leer(args[0]);
        parser = new Parser(false);
        parser.run();


        System.out.println("----------- Lista de sentencias -----------");
        for (String s : parser.getSentencias())
            System.out.println(s + "\n");


        System.out.println("----------- Errores y Warnings generados -----------");
        for(String s : ListaError.getWarnings())
            System.out.println(s + '\n');
        for(String s : ListaError.getErrores())
            System.out.println(s + '\n');


        System.out.println("----------- Lista de token Polaca principal -----------");
        System.out.println(parser.getParserHelper().getPolaca());

        System.out.println("----------- Lista de token Polacas funciones -----------");
        for (Polaca p : parser.getParserHelper().getPolacaFunc().values()) {
            System.out.println("----------- funcion -----------");
            System.out.println(p);
        }

        System.out.println("----------- Tabla de simbolos -----------");
        System.out.println(analizadorLexico.getTabla());


        GeneradorAsm generadorAsm = new GeneradorAsm(tablaSimbolos);
        if (ListaError.getErrores().size() == 0) {
            try (PrintWriter out = new PrintWriter("salida.asm")) {
                out.println(GeneradorAsm.getAsmFinal(parser.getParserHelper().getPolacaFunc(), parser.getParserHelper().getPolaca()));
            }
        }
        //System.out.println(GeneradorAsm.getAsmFinal(parser.getParserHelper().getPolacaFunc(), parser.getParserHelper().getPolaca()));
        else
            System.out.println("Hay errores por lo tanto no se puede generar el codigo ASM");
    }
}
