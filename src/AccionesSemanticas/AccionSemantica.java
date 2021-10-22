package AccionesSemanticas;
import AnalizadorLexico.*;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AccionSemantica {

    protected static StringBuilder contenido;
    protected TablaSimbolos tabla;
    protected AtomicInteger indice ;
    protected static String tipo;

    public AccionSemantica(AtomicInteger indice) {
        this.indice = indice;
    }
    public void setTabla(TablaSimbolos ts) {
        this.tabla=ts;
    }
    public void setIndice(AtomicInteger i) {
        this.indice=i;
    }
    public abstract TokenLexema accion(char c);

    public void incrementarIndice(){
        this.indice.set(this.indice.intValue()+1);
    }

    public String devolver(){
        return this.tipo;
    }
}
