package GeneracionASM;

import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.Token;

import java.util.ArrayList;
import java.util.Stack;

public class GeneradorAsig {
    public static ArrayList<String> getInstruccion(Stack<String> pila, TablaSimbolos ts, UtilidadReg registros) {
        String opIzq;
        String opDer;
        if(pila.size() > 1) {
            opDer = pila.pop();
            opIzq = pila.pop();
        } else{
            opIzq = GeneradorAsm.getPostReturn().get(GeneradorAsm.getFuncInvocada());
            if (opIzq == null)
            {
                String funcion = ts.getToken(ts.buscarIndice(GeneradorAsm.getFuncInvocada())).getNombreFunc();
                opIzq = GeneradorAsm.getPostReturn().get(funcion);
            }
            opDer = pila.pop();
        }

        ArrayList<String> instrucciones = new ArrayList<>();
        Token tokenIzq = ts.getToken(ts.buscarIndice(opIzq));
        Token tokenDer = ts.getToken(ts.buscarIndice(opDer));
        if (tokenDer.getTipo() == "ulong" && tokenIzq.getTipo() == "double") {
            instrucciones.add("JMP L_errorTipo");
        } else if (tokenDer.getTipo() == "double" && (tokenIzq.getTipo() == "ulong" || registros.esRegistro(opIzq))) {
            if (registros.esRegistro(opIzq)) {
                instrucciones.add("MOV @aux" + GeneradorAsm.getNumAux() + ", " + opIzq);
                Token t = new Token("@aux" + GeneradorAsm.getNumAux());
                t.setTipo("ulong");
                instrucciones.add("FILD @aux" + GeneradorAsm.getNumAux());
                ts.addSimbolo(t);
                GeneradorAsm.IncrementarAux();
            } else
                instrucciones.add("FILD " + GeneradorAsm.getPrefijo(opIzq) + opIzq);
            instrucciones.add("FSTP _" + opDer);
        } else if (registros.esRegistro(opIzq) && !registros.esRegistro(opDer)) {
            instrucciones.add("MOV _" + opDer + ", " + opIzq);
            registros.modificarRegistro(registros.getIdRegistro(opIzq), false); //falta referencia
        }
        // Variable y variable
        else if (!registros.esRegistro(opIzq) && !registros.esRegistro(opDer)) {
            if (tokenDer.getTipo() == "double") { //double y double
                instrucciones.add("FLD " + GeneradorAsm.getPrefijo(opIzq) + opIzq.replace(".", "p"));
                instrucciones.add("FSTP _" + opDer);
                return instrucciones;
            }
            int registro = registros.getRegistroLibre();  // ulong y ulong
            if (tokenIzq.getUso() == "func")
                instrucciones.add("MOV " + registros.getNombreRegistro(registro) + ", L_" + opIzq);
            else
                instrucciones.add("MOV " + registros.getNombreRegistro(registro) + ", _" + opIzq);
            instrucciones.add("MOV _" + opDer + ", " + registros.getNombreRegistro(registro));
            registros.modificarRegistro(registro, false);

        }
        return instrucciones;
    }
}