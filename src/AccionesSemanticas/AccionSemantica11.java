package AccionesSemanticas;

import AnalizadorLexico.TokenLexema;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica11 extends AccionSemantica{

    public AccionSemantica11(AtomicInteger indice){
        super(indice);
    }
    @Override
    public TokenLexema accion(char c) {
        contenido = new StringBuilder();
        this.incrementarIndice();
        return null;
    }
}
