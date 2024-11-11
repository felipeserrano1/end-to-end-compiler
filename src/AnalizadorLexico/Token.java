package AnalizadorLexico;

import java.util.Objects;

public class Token {
    private String lexema;
    private String tipo; // se usa para saber si es ULONG o DOUBLE
    private String uso;

    // usados en caso que sea funcion

    private String nombreParametro = null;
    private String tipoParametro = null;
    private static int count = 0;
    private int refFunc;

    private String nombreFunc;

    private int referencia = 1;

    public Token(String lexema){
        this.lexema = lexema;
    }

    public Token(String lexema, String tipo){
        this.lexema = lexema;
        this.tipo = tipo;
    }
    public void incrementarReferencia() {
        this.referencia++;
    }

    public int getReferencia() {
        return referencia;
    }

    public void decrementarReferencia() {
        this.referencia--;
    }

    @Override
    public String toString() {
        return "Token{" +
                "lexema='" + lexema + '\'' +
                ", tipo='" + tipo + '\'' +
                ", uso='" + uso + '\'' +
                ", nombreParametro='" + nombreParametro + '\'' +
                ", tipoParametro='" + tipoParametro + '\'' +
                '}';
    }

    public void setLexema(String lexema){
        this.lexema = lexema;
    }

    public String getLexema() {
        return this.lexema;
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getUso() {
        return this.uso;
    }

    public String getNombreFunc() {
        return nombreFunc;
    }

    public void setNombreFunc(String nombreFunc) {
        this.nombreFunc = nombreFunc;
    }

    public void setUso(String uso){
        this.uso = uso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(lexema, token.lexema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexema);
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreParametro() {
        return nombreParametro;
    }

    public void setNombreParametro(String nombreParametro) {
        this.nombreParametro = nombreParametro;
    }

    public String getTipoParametro() {
        return tipoParametro;
    }

    public void setTipoParametro(String tipoParametro) {
        this.tipoParametro = tipoParametro;
    }

    public void setRefFunc(){
        this.refFunc = count;
        count++;
    }

    public int getRefFunc() {
        return refFunc;
    }

}