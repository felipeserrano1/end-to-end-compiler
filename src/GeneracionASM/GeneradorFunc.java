package GeneracionASM;

import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.Token;

import java.util.ArrayList;
import java.util.Stack;

public class GeneradorFunc {
    public static ArrayList<String> getInstruccion(Stack<String> pila, TablaSimbolos ts, UtilidadReg registros, int reg, boolean recursion) {
        ArrayList<String> asm = new ArrayList<>();
        String func;
        func = pila.peek();
        Token funcion = ts.getToken(ts.buscarIndice(func));
        if(funcion.getUso() == "var_func"){
            asm.add("CALL _" + pila.pop());
        }else{
            if (recursion){
                asm.add("CMP " + registros.getNombreRegistro(reg) + ", " + funcion.getReferencia());
                asm.add("JE L_rec");
            } else
                func = pila.pop(); // sacamos el paso de la funcion
            asm.add("CALL L_" + func);
        }
        return asm;
    }
}
