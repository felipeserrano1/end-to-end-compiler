package AnalizadorLexico;

import GeneracionASM.GeneradorAsm;

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

    public boolean containsId(String id){
        for(Token t : this.tabla){
            if(t.getLexema().equals(id))
                return true;
        }
        return false;
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
            st.append(t + " indice " + this.buscarIndice(t.getLexema()) + "\n");
        }
        return st.toString();
    }

    public void remove(int pos) {
        this.tabla.remove(pos);
    }

    public int getLongitudTs(){
        return this.tabla.size();
    }

    public String toAsm() {
        StringBuilder asm = new StringBuilder();
        // falta el de procedimiento
        for (Token t : this.tabla) {
            if (t.getTipo() == "double")
                if (t.getUso() == "cte") {
                    if (!t.getLexema().equals("0.0"))
                        asm.append("_").append(t.getLexema().replace(".", "p")).append(" DQ ").append(t.getLexema()).append('\n');
                }else
                    asm.append(GeneradorAsm.getPrefijo(t.getLexema())).append(t.getLexema()).append(" DQ ").append(0).append('\n');
            else if (t.getTipo() == "ulong")
                if (t.getUso() == "cte")
                    asm.append("_").append(t.getLexema()).append(" DD ").append(t.getLexema()).append('\n');
                else
                    asm.append(GeneradorAsm.getPrefijo(t.getLexema())).append(t.getLexema()).append(" DD ").append(0).append('\n');

            else if (t.getTipo() == "cadena"){
                String nombre = t.getLexema().replace(" ", "");
                asm.append("_").append((nombre),1 , nombre.length()-1).append(" DB ").append("\"")
                        .append(t.getLexema(), 1 , t.getLexema().length()-1).append("\"")
                        .append(", 0").append('\n'); // Se borran % y se agrega " al principio y final
            }
            else if (t.getTipo() == "var_func"){
                asm.append("_").append(t.getLexema()).append(" DD ").append("?\n");
            }

        }
        return asm.toString();
    }
}
