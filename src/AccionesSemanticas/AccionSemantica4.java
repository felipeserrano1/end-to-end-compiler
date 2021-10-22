package AccionesSemanticas;

import AnalizadorLexico.TokenLexema;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica4 extends AccionSemantica {

    public AccionSemantica4(AtomicInteger indice) {
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        contenido = new StringBuilder();
        contenido.append(c);
        this.incrementarIndice();
        contenido.toString();
        tipo = String.valueOf(c);
        System.out.println(contenido);
        return new TokenLexema(-1);
    }
}