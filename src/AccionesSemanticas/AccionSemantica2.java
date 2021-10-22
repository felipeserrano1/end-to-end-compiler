package AccionesSemanticas;

import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.TokenLexema;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica2 extends AccionSemantica{

    public AccionSemantica2(AtomicInteger indice){
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        contenido.append(c);
        incrementarIndice();
        return null;
    }
}
