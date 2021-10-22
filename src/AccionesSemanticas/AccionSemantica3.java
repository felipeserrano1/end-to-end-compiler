package AccionesSemanticas;

import AnalizadorLexico.Token;
import AnalizadorLexico.TokenLexema;

import java.util.concurrent.atomic.AtomicInteger;

public class AccionSemantica3 extends AccionSemantica{

    public AccionSemantica3(AtomicInteger indice){
        super(indice);
    }

    @Override
    public TokenLexema accion(char c) {
        if (contenido.length() > 23) {
            System.out.println("Se trunco el identificador " + contenido);
            contenido.delete(22, contenido.length());
        }
        if (this.tabla.containsPalabaReservada(contenido.toString())) {
            this.tipo = contenido.toString();
            System.out.println("palabra reservada: " + contenido);
            return new TokenLexema(-1);
        } else {
            Token t = new Token(contenido.toString());
            if (!this.tabla.contains(t))
                this.tabla.addSimbolo(t);
            this.tipo = "ID";
            System.out.println("ID: " + contenido);
            return new TokenLexema(this.tabla.buscarIndice(contenido.toString()));
        }
    }
}