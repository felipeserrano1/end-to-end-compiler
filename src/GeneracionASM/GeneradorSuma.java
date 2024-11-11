package GeneracionASM;

import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.Token;

import java.util.ArrayList;
import java.util.Stack;

public class GeneradorSuma {

    public static ArrayList<String> getInstruccion(Stack<String> pila, TablaSimbolos ts, UtilidadReg registros, String op) {
        String opDer = pila.pop();
        String opIzq = pila.pop();

        ArrayList<String> instrucciones = new ArrayList<>();
        Token tokenIzq = ts.getToken(ts.buscarIndice(opIzq));
        Token tokenDer = ts.getToken(ts.buscarIndice(opDer));
        if (tokenDer.getTipo() == "double" || tokenIzq.getTipo() == "double")
            return GeneradorDouble.getInstruccion(pila, ts, opDer, opIzq, op, registros);

        // los 2 son variables
        if(!registros.esRegistro(opDer) && !registros.esRegistro(opIzq)) {
            int registro = registros.getRegistroLibre();
            registros.modificarRegistro(registro, true);
            instrucciones.add("MOV " + registros.getNombreRegistro(registro) + ", _" + opIzq);
            instrucciones.add("ADD " + registros.getNombreRegistro(registro) + ", _" + opDer);
            pila.add(registros.getNombreRegistro(registro));
        }

        // Registro y variable
        if(registros.esRegistro(opIzq) && !registros.esRegistro(opDer)) {
            pila.add(opIzq);
            instrucciones.add("ADD " + opIzq + ", _" + opDer);
        }

        // Registro y Registro
        if(registros.esRegistro(opIzq) && registros.esRegistro(opDer)) {
            pila.add(opIzq);
            instrucciones.add("ADD " + opIzq + ", " + opDer);
            registros.modificarRegistro(registros.getIdRegistro(opDer), false);

        }

        // Variable y registro, como la suma es conmutativa se puede hacer sobre el mismo registro
        if(!registros.esRegistro(opIzq) && registros.esRegistro(opDer)) {
            pila.add(opDer);
            instrucciones.add("ADD " + opDer + ", _" + opIzq);
        }

        return instrucciones;
    }
}
