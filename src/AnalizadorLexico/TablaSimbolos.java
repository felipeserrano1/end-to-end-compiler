package AnalizadorLexico;

import java.util.ArrayList;
import java.util.Arrays;

public class TablaSimbolos {
    private ArrayList<String> palabrasReservadas = new ArrayList<>(
            Arrays.asList("IF","THEN","ELSE","ENDIF","PRINT","FUNC","RETURN","BEGIN","END","BREAK","WHILE",
                          "DO","ULONG","DOUBLE","POST","TRY","CATCH"));

    private ArrayList<Token> tabla = new ArrayList<>();

    public TablaSimbolos(){

    }
    public boolean containsPalabaReservada(String lexema){
        return this.palabrasReservadas.contains(lexema);
    }

    public void addSimbolo(Token t){
        this.tabla.add(t);
    }

    public boolean contains(Token t) {
        return this.tabla.contains(t);
    }

    public int buscarIndice(String str) {
        for(Token t : this.tabla){
            if(t.getLexema().equals(str))
                return this.tabla.indexOf(t);
        }
        return 0;
    }

    public Token getToken(int indice){
        return this.tabla.get(indice);
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        for(Token t : this.tabla){
            st.append("lexema " + t.getLexema() + " indice " + this.buscarIndice(t.getLexema()) + "\n");
        }
        return st.toString();
    }
}
