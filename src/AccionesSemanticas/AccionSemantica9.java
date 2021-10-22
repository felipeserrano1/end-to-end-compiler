package AccionesSemanticas;
import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.TokenLexema;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica9 extends AccionSemantica{

    public AccionSemantica9(AtomicInteger indice) {
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        tipo = contenido.toString();
        return null;
    }
}