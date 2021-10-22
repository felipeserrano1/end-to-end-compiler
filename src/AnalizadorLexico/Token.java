package AnalizadorLexico;

import java.util.Objects;

public class Token {
    private String lexema;
    private String tipo = null; // se usa para saber si es ULONG o DOUBLE
    private String uso;

    public Token(String lexema){
        this.lexema = lexema;
    }

    public Token(String lexema, String tipo){
        this.lexema = lexema;
        this.tipo = tipo;
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
}