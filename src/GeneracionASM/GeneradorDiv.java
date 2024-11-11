package GeneracionASM;

import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.Token;

import java.util.ArrayList;
import java.util.Stack;

public class GeneradorDiv {
    public static ArrayList<String> getInstruccion(Stack<String> pila, TablaSimbolos ts, UtilidadReg registros, String op) {
        String opDer = pila.pop();
        String opIzq = pila.pop();
        ArrayList<String> instrucciones = new ArrayList<>();

        int registro = registros.getRegistroLibre();
        registros.modificarRegistro(registro, true);
        instrucciones.add("MOV " + registros.getNombreRegistro(registro) + ", 0" );
        instrucciones.add("CMP " + GeneradorAsm.getPrefijo(opDer) + opDer + ", " + registros.getNombreRegistro(registro));
        instrucciones.add("JE L_div_cero");
        registros.modificarRegistro(registro, false);

        Token tokenIzq = ts.getToken(ts.buscarIndice(opIzq));
        Token tokenDer = ts.getToken(ts.buscarIndice(opDer));

        // los 2 son variables
        if (tokenDer.getTipo() == "double" || tokenIzq.getTipo() == "double") {
            return GeneradorDouble.getInstruccion(pila, ts, opDer, opIzq, op, registros);
        }
        if (!registros.esRegistro(opDer) && !registros.esRegistro(opIzq)) {
            registro = registros.getRegistroLibreMultDiv(UtilidadReg.EAX, instrucciones,ts);
            registros.modificarRegistro(registro, true);
            instrucciones.add("MOV EAX, _" + opIzq);

            registro = registros.getRegistroLibreMultDiv(UtilidadReg.EDX, instrucciones, ts);
            registros.modificarRegistro(registro, true);
            instrucciones.add("MOV EDX, 0");
            instrucciones.add("DIV " + GeneradorAsm.getPrefijo(opDer) + opDer);
        }

        // Registro y variable
        if (registros.esRegistro(opIzq) && !registros.esRegistro(opDer)) {
            if (opIzq != "EAX") {
                registro = registros.getRegistroLibreMultDiv(UtilidadReg.EAX, instrucciones, ts);
                registros.modificarRegistro(registro,  true); //falta referencia
                instrucciones.add("MOV EAX, _" + opIzq);
            }
            registro = registros.getRegistroLibreMultDiv(UtilidadReg.EDX, instrucciones, ts);
            registros.modificarRegistro(registro, true); //falta referencia
            instrucciones.add("MOV EDX, 0");
            instrucciones.add("DIV " + GeneradorAsm.getPrefijo(opDer) + opDer);

        }

        // Registro y Registro
        if (registros.esRegistro(opIzq) && registros.esRegistro(opDer)) {
            if (opIzq != "EAX") {
                registro = registros.getRegistroLibreMultDiv(UtilidadReg.EAX, instrucciones, ts);
                registros.modificarRegistro(registro, true); //falta referencia
                instrucciones.add("MOV EAX, _" + opIzq);
            }
            registro = registros.getRegistroLibreMultDiv(UtilidadReg.EDX, instrucciones, ts);
            registros.modificarRegistro(registro, true); //falta referencia
            instrucciones.add("DIV " + GeneradorAsm.getPrefijo(opDer) + opDer);
            registros.modificarRegistro(registros.getIdRegistro(opDer), false); // queda con el resto, que se hace aca
        }

        // Variable y registro
        if (!registros.esRegistro(opIzq) && registros.esRegistro(opDer)) {
            registro = registros.getRegistroLibreMultDiv(UtilidadReg.EAX, instrucciones, ts);
            registros.modificarRegistro(registro, true); //falta referencia
            instrucciones.add("MOV EAX, _" + opIzq);

            registro = registros.getRegistroLibreMultDiv(UtilidadReg.EDX, instrucciones, ts);
            registros.modificarRegistro(registro, true); //falta referencia
            instrucciones.add("MOV EDX, 0");

            instrucciones.add("DIV " + GeneradorAsm.getPrefijo(opDer) + opDer);
            registros.modificarRegistro(registros.getIdRegistro(opDer), false);
        }

        pila.add("EAX");
        return instrucciones;
    }
}