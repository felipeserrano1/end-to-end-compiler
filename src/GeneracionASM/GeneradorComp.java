package GeneracionASM;

import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.Token;

import java.util.ArrayList;
import java.util.Stack;

public class GeneradorComp {
    public static ArrayList<String> getInstruccion(Stack<String> pila, TablaSimbolos ts, UtilidadReg registros) {
        ArrayList<String> instrucciones = new ArrayList<>();
        String opDer = pila.pop();
        String opIzq = pila.pop();

        Token tokenIzq = ts.getToken(ts.buscarIndice(opIzq));
        Token tokenDer = ts.getToken(ts.buscarIndice(opDer));
        if (tokenDer.getTipo() == "double" || tokenIzq.getTipo() == "double")
           return getCompDouble(opDer, opIzq);

        // registro y registro
        if(registros.esRegistro(opIzq) && registros.esRegistro(opDer)){
            instrucciones.add("CMP " + opIzq + ", " + opDer);
        }

        //Variable y registro
        if(!registros.esRegistro(opIzq) && registros.esRegistro(opDer)){
            instrucciones.add("CMP " + GeneradorAsm.getPrefijo(opIzq) + opIzq + ", " + opDer);
        }

        // registro y variable
        if(registros.esRegistro(opIzq) && !registros.esRegistro(opDer)){
            instrucciones.add("CMP " + opIzq + ", " + GeneradorAsm.getPrefijo(opDer) + opDer);
        }

        // variable y variable
        if(!registros.esRegistro(opIzq) && !registros.esRegistro(opDer)){
            int registro = registros.getRegistroLibre();
            registros.modificarRegistro(registro, true);
            instrucciones.add("MOV " + registros.getNombreRegistro(registro) + ", " + GeneradorAsm.getPrefijo(opIzq) + opIzq);
            instrucciones.add("CMP " + registros.getNombreRegistro(registro) + ", " + GeneradorAsm.getPrefijo(opDer) + opDer);
            registros.modificarRegistro(registro, false);
        }

        return instrucciones;
    }
    // a < b
    // a b <
    public static ArrayList<String> getCompDouble(String opDer, String opIzq) {
        ArrayList<String> asm = new ArrayList<>();
        asm.add("FINIT");
        if(opDer.substring(0, 1).matches("\\d"))
            asm.add("FLD _" + opDer.replace(".", "p"));
        else
            asm.add("FLD _" + opDer);
        if(opIzq.substring(0, 1).matches("\\d"))
            asm.add("FLD _" + opIzq.replace(".", "p"));
        else
            asm.add("FLD _" + opIzq);
        asm.add("FCOM");
        asm.add("FSTSW AX");
        asm.add("SAHF");
        return asm;
    }

    public static ArrayList<String> getInstruccionSalto(String salto, String comparador, String pos) {
        ArrayList<String> instrucciones = new ArrayList<>();
        if (salto == "BF"){
            switch (comparador){
                case "<":
                    instrucciones.add("JAE " + "L" + pos);
                    break;
                case "<=":
                    instrucciones.add("JA " + "L" + pos);
                    break;
                case ">":
                    instrucciones.add("JBE " + "L" + pos);
                    break;
                case ">=":
                    instrucciones.add("JB " + "L" + pos);
                    break;
                case "==":
                    instrucciones.add("JNE " + "L" + pos);
                    break;
                case "!=":
                    instrucciones.add("JE " + "L" + pos);
                    break;
            }
        }
        else if(salto == "BT"){
            switch (comparador){
                case "<":
                    instrucciones.add("JB " + "L" + pos);
                    break;
                case "<=":
                    instrucciones.add("JBE " + "L" + pos);
                    break;
                case ">":
                    instrucciones.add("JA " + "L" + pos);
                    break;
                case ">=":
                    instrucciones.add("JAE " + "L" + pos);
                    break;
                case "==":
                    instrucciones.add("JE " + "L" + pos);
                    break;
                case "!=":
                    instrucciones.add("JNE " + "L" + pos);
                    break;

            }
        }
        return instrucciones;
    }

}

