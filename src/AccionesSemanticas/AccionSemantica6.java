package AccionesSemanticas;

import AnalizadorLexico.TokenLexema;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica6 extends AccionSemantica {

    public AccionSemantica6(AtomicInteger indice){
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        contenido.append(c);
        incrementarIndice();
        return null;
    }
}
