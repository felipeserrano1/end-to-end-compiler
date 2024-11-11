package utilidades;

import java.util.ArrayList;

public class ListaError {

    private static ArrayList<String> errores = new ArrayList<>();
    private static ArrayList<String> warnings = new ArrayList<>();

    public static void reset()
    {
        errores = new ArrayList<>();
        warnings = new ArrayList<>();
    }

    public static void addErrores(String error){
        errores.add(error);
    }

    public static void addWarning(String warning){
        warnings.add(warning);
    }

    public static ArrayList<String> getWarnings() {
        return warnings;
    }

    public static ArrayList<String> getErrores() {
        return errores;
    }
}
