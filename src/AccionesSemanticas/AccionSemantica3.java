package AccionesSemanticas;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.Token;
import AnalizadorLexico.TokenLexema;
import utilidades.ListaError;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica3 extends AccionSemantica{

    public AccionSemantica3(AtomicInteger indice){
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        if (contenido.length() > 23) {
            ListaError.addWarning("Linea " + AnalizadorLexico.GetInstancia().getLinea() + ": Warning Se trunco el identificador " + contenido);
            contenido.delete(22, contenido.length());
        }
        if (this.tabla.containsPalabaReservada(contenido.toString())) {
            this.tipo = contenido.toString();
            return new TokenLexema(-1);
        } else {
            Token t = new Token(contenido.toString());
            if (!this.tabla.contains(t))
                this.tabla.addSimbolo(t);
            else
                this.tabla.getToken(tabla.buscarIndice(contenido.toString())).incrementarReferencia();
            this.tipo = "ID";
            return new TokenLexema(this.tabla.buscarIndice(contenido.toString()));
        }
    }
}