package AccionesSemanticas;

import AnalizadorLexico.Token;
import AnalizadorLexico.TokenLexema;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica10 extends AccionSemantica {
    public AccionSemantica10(AtomicInteger indice) {
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        Long aux = Long.parseLong(contenido.toString());
        if (aux < Math.pow(2, 32) - 1 && aux >= 0) {
            Token t = new Token(contenido.toString(), "ulong");
            t.setUso("cte");
            if (!this.tabla.contains(t))
                this.tabla.addSimbolo(t);
            this.tipo = "CTE";
            return new TokenLexema(this.tabla.buscarIndice(contenido.toString()));
        } else {
            this.tipo = "ERROR";
            return new TokenLexema(-1);
        }
    }
}