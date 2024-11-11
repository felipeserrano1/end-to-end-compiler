package AccionesSemanticas;

import AnalizadorLexico.Token;
import AnalizadorLexico.TokenLexema;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica5 extends AccionSemantica{

    public AccionSemantica5(AtomicInteger indice){
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        BigDecimal aux = new BigDecimal(contenido.toString());
        BigDecimal minimo = new BigDecimal("2.2250738585072014E-308");
        BigDecimal maximo = new BigDecimal("1.7976931348623157E+308");
        if ((aux.compareTo(minimo) == 1 && aux.compareTo(maximo) == -1) ||
                (aux.compareTo(maximo) == 1 && aux.compareTo(minimo) == -1) ||
                aux.compareTo(new BigDecimal(0.0)) == 0){
            Token t = new Token(contenido.toString(), "double");
            t.setUso("cte");
            if (!this.tabla.contains(t))
                this.tabla.addSimbolo(t);
            this.tipo = "CTE";
            return new TokenLexema(this.tabla.buscarIndice(contenido.toString()));
        } else {
            this.tipo = "ERROR";
            return new TokenLexema(this.tabla.buscarIndice(contenido.toString()));
        }
    }
}
