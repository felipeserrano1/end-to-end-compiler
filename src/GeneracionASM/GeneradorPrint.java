package GeneracionASM;

import java.util.ArrayList;

public class GeneradorPrint {

    public static ArrayList<String> getInstruccion(String cadena) {
        ArrayList<String> asm = new ArrayList<>();
        asm.add("invoke MessageBox, NULL, addr _" + cadena.substring(1, cadena.length()-1).replace(" ", "") +
            ", addr _" + cadena.substring(1, cadena.length()-1).replace(" ", "") + ", MB_OK");
        return asm;
    }
}
