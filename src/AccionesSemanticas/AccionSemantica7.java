package AccionesSemanticas;
import AnalizadorLexico.TokenLexema;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica7 extends AccionSemantica {

    public AccionSemantica7(AtomicInteger indice){
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        this.tipo = contenido.toString();
        return new TokenLexema(-1);
    }
}
