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
        System.out.println("Cadena: " + contenido);
        Token t = new Token(contenido.toString());
        if (!this.tabla.contains(t)){
            this.tabla.addSimbolo(new Token(contenido.toString()));
        }
        return new TokenLexema(this.tabla.buscarIndice(contenido.toString()));
    }

}
