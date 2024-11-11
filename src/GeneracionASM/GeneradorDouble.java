package GeneracionASM;

import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.Token;

import java.util.ArrayList;
import java.util.Stack;


public class GeneradorDouble {
    public static ArrayList<String> getInstruccion(Stack<String> pila, TablaSimbolos ts, String opDer, String opIzq, String op, UtilidadReg reg) {
        ArrayList<String> asm = new ArrayList<>();
        asm.add("FINIT");
        Token tokenIzq = ts.getToken(ts.buscarIndice(opIzq));
        Token tokenDer = ts.getToken(ts.buscarIndice(opDer));
        asm.addAll(conversor(opDer, opIzq, ts, reg));
        if(op == "/") {
            if(opDer.substring(0, 1).matches("\\d"))
                asm.add("FLD _" + opDer.replace(".", "p"));
            else
                asm.add("FLD _" + opDer);
            asm.add("FLD " + "_0p0");
            asm.add("FCOM");
            asm.add("FSTSW AX");
            asm.add("SAHF");
            asm.add("JE L_div_cero");
        }

        if (tokenDer.getTipo() == "double")
            if (opDer.substring(0, 1).matches("\\d"))
                asm.add("FLD _" + opDer.replace(".", "p"));
            else
                asm.add("FLD " + GeneradorAsm.getPrefijo(opDer) + opDer);
        if (tokenIzq.getTipo() == "double")
            if (opIzq.substring(0, 1).matches("\\d"))
                asm.add("FLD _" + opIzq.replace(".", "p"));
            else
                asm.add("FLD " + GeneradorAsm.getPrefijo(opIzq) + opIzq);
        asm.add(getOperacion(op));
        asm.add("FSTP " + "@aux" + GeneradorAsm.getNumAux());
        Token aux = new Token("@aux" + GeneradorAsm.getNumAux(), "double");
        ts.addSimbolo(aux);
        pila.add("@aux" + GeneradorAsm.getNumAux());
        GeneradorAsm.IncrementarAux();
        return asm;
    }

    private static String getOperacion(String op) {
        switch (op) {
            case "+":
                return "FADD";
            case "-":
                return "FSUB";
            case "*":
                return "FMUL";
            case "/":
                return "FDIV";
            default:
                return null;
        }
    }

    public static ArrayList<String> conversor(String opDer, String opIzq, TablaSimbolos ts, UtilidadReg reg) {
        ArrayList<String> asm = new ArrayList<>();
        Token tokenIzq = ts.getToken(ts.buscarIndice(opIzq));
        Token tokenDer = ts.getToken(ts.buscarIndice(opDer));
        if ((tokenDer.getTipo() == "double" && tokenIzq.getTipo() == "ulong") || reg.esRegistro(opIzq)) {
            if (opIzq.substring(0, 1).matches("\\d")) {
                asm.add("FILD _" + opIzq);
            } else if (!reg.esRegistro(opIzq))
                asm.add("FILD " + GeneradorAsm.getPrefijo(opIzq) + opIzq);
            else {
                asm.add("MOV @aux" + GeneradorAsm.getNumAux() + ", " + opIzq);
                asm.add("FILD @aux" + GeneradorAsm.getNumAux());
                Token aux = new Token("@aux" + GeneradorAsm.getNumAux(), "ulong");
                ts.addSimbolo(aux);
                GeneradorAsm.IncrementarAux();
            }
        }
        if ((tokenIzq.getTipo() == "double" && tokenDer.getTipo() == "ulong") || reg.esRegistro(opDer)) {
            if (opIzq.substring(0, 1).matches("\\d")) {
                asm.add("FILD _" + opDer);
            } else if (!reg.esRegistro(opDer))
                asm.add("FILD "+ GeneradorAsm.getPrefijo(opDer) + opDer);
            else {
                asm.add("MOV @aux" + GeneradorAsm.getNumAux() + ", " + opDer);
                asm.add("FILD @aux" + GeneradorAsm.getNumAux());
                Token aux = new Token("@aux" + GeneradorAsm.getNumAux(), "ulong");
                ts.addSimbolo(aux);
                GeneradorAsm.IncrementarAux();
            }
        }
        return asm;
    }
}
