package CodigoIntermedio;

import java.util.ArrayList;
import java.util.Arrays;

public class Polaca {

    ArrayList<String> listaPasos = new ArrayList<>();
    ArrayList<Integer> incompletos = new ArrayList<>();

    public Polaca(){

    }


    public void addPaso(String... aux){
        this.listaPasos.addAll(Arrays.asList(aux));
    }

    public ArrayList<String> getPolaca(){
        return this.listaPasos;
    }


    public int getLongitud(){
        return this.listaPasos.size();
    }

    public void addPasoIncompleto(){
        this.incompletos.add(this.listaPasos.size());
    }

    public int removePasoIncompleto(){
        return this.incompletos.remove(this.incompletos.size()-1);
    }

    public void addPaso(int pos, int salto){
        this.listaPasos.set(pos,String.valueOf(salto));
    }

    public int getLongitudIncompletos() {return this.incompletos.size();}
    @Override
    public String toString() {
        StringBuilder salida = new StringBuilder();
        int indice = 0;
        for(String s : this.listaPasos){
            salida.append(indice);
            salida.append("  "  + s);
            salida.append('\n');
            indice++;
        }
        return salida.toString();
    }

    public void addPasoIncompletoPos(int aux) {
        this.incompletos.add(aux);
    }
}
