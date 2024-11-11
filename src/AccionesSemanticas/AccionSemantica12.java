package AccionesSemanticas;

import AnalizadorLexico.Token;
import AnalizadorLexico.TokenLexema;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica12 extends AccionSemantica{
    public AccionSemantica12(AtomicInteger indice) {
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        contenido.append(c);
        this.incrementarIndice();
        tipo = "CADENA";
        Token t = new Token(contenido.toString(), "cadena");
        if (!this.tabla.contains(t)){
            this.tabla.addSimbolo(t);
        }
        return new TokenLexema(this.tabla.buscarIndice(contenido.toString()));
    }

}
