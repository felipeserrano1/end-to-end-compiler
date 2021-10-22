package AccionesSemanticas;
import AnalizadorLexico.TokenLexema;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica8 extends AccionSemantica{


    public AccionSemantica8(AtomicInteger indice) {
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        contenido.append(c);
        this.incrementarIndice();
        tipo = contenido.toString();
        System.out.println(contenido);
        return new TokenLexema(-1);
    }
}
