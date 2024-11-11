package GeneracionASM;

import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.Token;

import java.util.ArrayList;
import java.util.Stack;

public class GeneradorMult {

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
            int registro = registros.getRegistroLibreMultDiv(UtilidadReg.EAX, instrucciones, ts);
            registros.modificarRegistro(registro, true);
            instrucciones.add("MOV " + registros.getNombreRegistro(registro) + ", _" + opIzq);
            instrucciones.add("MUL _" + opDer);
            }

        // Registro y variable
        if(registros.esRegistro(opIzq) && !registros.esRegistro(opDer)) {
            if(opIzq == "EAX") {
                instrucciones.add("MUL " + opDer);
            } else {
                int registro = registros.getRegistroLibreMultDiv(UtilidadReg.EAX, instrucciones, ts);
                registros.modificarRegistro(registro, true);
                instrucciones.add("MOV EAX" + ", " + opDer);
                instrucciones.add("MUL " + opIzq);
            }
            registros.modificarRegistro(registros.getIdRegistro(opIzq), false);
        }

        // Registro y Registro
        if(registros.esRegistro(opIzq) && registros.esRegistro(opDer)) {
            if(opIzq == "EAX") {
                instrucciones.add("MUL " + opDer);
            } else {
                int registro = registros.getRegistroLibreMultDiv(UtilidadReg.EAX, instrucciones, ts);
                instrucciones.add("MOV EAX" + opIzq);
                registros.modificarRegistro(registros.getIdRegistro(opIzq), false);
                instrucciones.add("MUL " + opDer);
            }
            registros.modificarRegistro(registros.getIdRegistro(opDer), false);

        }

        // Variable y registro, es conmutativa
        if(!registros.esRegistro(opIzq) && registros.esRegistro(opDer)) {
            if(opDer == "EAX") {
                instrucciones.add("MUL _" + opIzq);
            } else {
                int registro = registros.getRegistroLibreMultDiv(UtilidadReg.EAX, instrucciones, ts);
                instrucciones.add("MOV EAX " + ", " + opDer);
                registros.modificarRegistro(registros.getIdRegistro(opDer), false);
                instrucciones.add("MUL _" + opIzq);
            }
            registros.modificarRegistro(registros.getIdRegistro(opDer), false);
        }

        pila.add("EAX");
        return instrucciones;
    }
}
