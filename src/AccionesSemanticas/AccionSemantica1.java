package AccionesSemanticas;

import AnalizadorLexico.TokenLexema;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica1 extends AccionSemantica{

    public AccionSemantica1(AtomicInteger indice){
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        contenido = new StringBuilder();
        contenido.append(c);
        this.incrementarIndice();
        return null;
    }
}
