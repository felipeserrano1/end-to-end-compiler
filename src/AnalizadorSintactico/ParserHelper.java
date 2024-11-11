package AnalizadorSintactico;


import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.Token;
import CodigoIntermedio.Polaca;
import utilidades.ListaError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class ParserHelper {

    private TablaSimbolos ts;
    private Polaca polaca = new Polaca();
    private ArrayList<String> ambitos = new ArrayList<>(Arrays.asList("@Programa"));
    private AnalizadorLexico al;
    private String ultimoTipo = null;
    private boolean error = false;
    private Token ultimoToken;
    private HashMap<String, Polaca> funcionesPolacas = new HashMap<>();
    private ArrayList<String> nombresFunciones = new ArrayList<>();
    private ArrayList<String> operador = new ArrayList<>();
    private static ArrayList<Token> funciones;
    private static int pasada = 0;


    public ParserHelper() {
        this.al = AnalizadorLexico.GetInstancia();
        this.ts = al.getTabla();
        if(funciones == null){
            funciones = new ArrayList<>();
        }
    }

    public void incrementarPasada(){
        pasada++;
    }

    public Polaca getPolaca() {
        return this.polaca;
    }

    public HashMap<String, Polaca> getPolacaFunc() {
        return this.funcionesPolacas;
    }

    public void addPaso(String... paso) {
        if (this.getAmbito(this.ambitos).equals("@Programa"))
            this.polaca.addPaso(paso);
        else
            this.funcionesPolacas.get(this.getAmbito(this.ambitos)).addPaso(paso);
    }

    public String getAmbito(ArrayList<String> ambito) {
        StringBuilder sb = new StringBuilder();
        for (String s : ambito) {
            sb.append(s);
        }
        return sb.toString();
    }

    public void declaracionVar(int pos) {
        String ambito = this.getAmbito(this.ambitos);
        String id = this.ts.getToken(pos).getLexema();
        if (!this.verificarRedeclaracion(id, ambito)) {
            Token aux = ts.getToken(pos);
            aux.setLexema(aux.getLexema() + ambito);
            if (ultimoTipo == "var_func"){
                aux.setUso("var_func");
                if (pasada == 0)
                    funciones.add(aux);
            }
            else
                aux.setUso("var");
            aux.setTipo(ultimoTipo);
        }
    }

    private boolean verificarRedeclaracion(String id, String ambito) {
        boolean redeclarada = ts.containsId(id + ambito);
        if (redeclarada) {
            ListaError.addErrores("Linea " + al.getLinea() + ": Error Semantico, no se puede redeclarar la variable/funcion " + id);
            this.error = true;
        }
        return redeclarada;
    }

    public void setUltimoTipo(String tipo) {
        this.ultimoTipo = tipo;
    }

    public String buscarAmbito(int pos) {
        ArrayList<String> ambitosAux = new ArrayList<>(ambitos);
        String id = this.ts.getToken(pos).getLexema();
        while (!ambitosAux.isEmpty()) {
            if (!ts.containsId(id + getAmbito(ambitosAux)))
                ambitosAux.remove(ambitosAux.size() - 1);
            else
                return this.getAmbito(ambitosAux);
        }
        ListaError.addErrores("Linea " + al.getLinea() + ": Error Semantico, la variable " + id + " no esta declarada");
        this.error = true;
        return null;
    }

    public String buscarAmbitoFunc(int pos) {
        ArrayList<String> ambitosAux = new ArrayList<>(ambitos);
        String id = this.ts.getToken(pos).getLexema();
        Token t = new Token(id + this.getAmbito(ambitosAux));
        while (!ambitosAux.isEmpty()) {
            if (!funciones.contains(t)){
                ambitosAux.remove(ambitosAux.size() - 1);
                t = new Token(id + this.getAmbito(ambitosAux));
            }
            else
                return this.getAmbito(ambitosAux);
        }
        ListaError.addErrores("Linea " + al.getLinea() + ": Error Semantico, la funcion " + id + " no esta declarada");
        this.error = true;
        return null;
    }

    public void leerSentencia(int pos) {
        String ambito = this.buscarAmbito(pos);
        String id = this.ts.getToken(pos).getLexema();
        this.ts.remove(pos);
        if (ambito != null) {
            Token token = this.ts.getToken(this.ts.buscarIndice(id + ambito));
            if (token.getUso() == "func") {
                ListaError.addErrores("Linea " + al.getLinea() + ": Error Semantico, la variable " + token.getLexema() + " es una funcion");
                this.error = true;
            } else if (token.getUso() == "var_func")
                this.copiarVarFunc(token);
            if (!error)
                this.addPaso(token.getLexema(), ":=");
        }
    }

    private void copiarVarFunc(Token token) {
        token.setNombreParametro(ultimoToken.getNombreParametro());
        token.setTipoParametro(ultimoToken.getTipoParametro());
        token.setTipo(ultimoToken.getTipo());
        token.setNombreFunc(ultimoToken.getLexema());
    }

    public void leerConstante(int pos, boolean positivo) {
        if (!error) {
            this.ultimoToken = this.ts.getToken(pos);
            if (positivo)
                this.addPaso(this.ts.getToken(pos).getLexema());
            else this.addPaso("-", this.ts.getToken(pos).getLexema());
        }
    }

    public void leerID(int pos) {
        String ambito = this.buscarAmbito(pos);
        String id = this.ts.getToken(pos).getLexema();
        this.ultimoToken = this.ts.getToken(this.ts.buscarIndice(id + ambito));
        if(ts.getToken(ts.buscarIndice(id)).getReferencia() == 1)
            this.ts.remove(pos);
        else
            ts.getToken(ts.buscarIndice(id)).decrementarReferencia();
        if (ambito != null) {
            String nuevoId = id + ambito;
            if (!error)
                this.addPaso(nuevoId);
        }
    }

    public boolean getError() {
        return this.error;
    }

    public void declaracionFunc(int pos) {
        String ambito = this.getAmbito(this.ambitos);
        String id = this.ts.getToken(pos).getLexema();
        String nuevoId = id + ambito;
        this.verificarRedeclaracion(id, ambito);
        Token aux = ts.getToken(pos);
        aux.setLexema(aux.getLexema() + ambito);
        aux.setUso("func");
        if(pasada == 1){
            aux.setRefFunc();
        }
        aux.setTipo(ultimoTipo);
        if (!funciones.contains(aux)){
            funciones.add(aux);
        }
        this.ambitos.add("@" + id);
        this.funcionesPolacas.put(this.getAmbito(this.ambitos), new Polaca());
        this.nombresFunciones.add(nuevoId);
    }

    public void paramFuncion(int pos) {
        this.declaracionVar(pos);
        Token token = this.ts.getToken(this.ts.getLongitudTs() - 2); // accedemos a la funcion, siempre esta anteultima en la TS
        token.setTipoParametro(this.ts.getToken(pos).getTipo());
        token.setNombreParametro(this.ts.getToken(pos).getLexema());
    }

    public void retrocederAmbito() {
        this.ambitos.remove(this.ambitos.size() - 1);
    }

    public void verificarReturn() {
        if (ultimoToken != null) { //token leido del return de la funcion
            Token token = this.ts.getToken(this.ts.buscarIndice(this.nombresFunciones.remove(this.nombresFunciones.size() - 1)));
            if (ultimoToken.getTipo() != token.getTipo()) {
                ListaError.addErrores("Linea " + al.getLinea() + ": Error Semantico, el tipo retornado no coincide con el tipo de la funcion");
                this.error = true;
            }
        }
    }

    private void addPasoIncompleto() {
        if (this.getAmbito(this.ambitos).equals("@Programa"))
            this.polaca.addPasoIncompleto();
        else
            this.funcionesPolacas.get(this.getAmbito(this.ambitos)).addPasoIncompleto();
    }

    private int getPasoIncompleto() {
        int pasoIncompleto;
        if (this.getAmbito(this.ambitos).equals("@Programa"))
            pasoIncompleto = this.polaca.removePasoIncompleto();
        else
            pasoIncompleto = this.funcionesPolacas.get(this.getAmbito(this.ambitos)).removePasoIncompleto();
        return pasoIncompleto;
    }

    private void addPasoFin(int pasoIncompleto) {
        if (this.getAmbito(this.ambitos).equals("@Programa"))
            this.polaca.addPaso(pasoIncompleto, this.polaca.getLongitud()-1);
        else {
            Polaca polaca = this.funcionesPolacas.get(this.getAmbito(this.ambitos));
            polaca.addPaso(pasoIncompleto, polaca.getLongitud()-1);
        }
    }

    private void addLabel(){
        if (this.getAmbito(this.ambitos).equals("@Programa"))
            this.addPaso("L" + this.polaca.getLongitud());
        else {
            Polaca polaca = this.funcionesPolacas.get(this.getAmbito(this.ambitos));
            polaca.addPaso("L" + polaca.getLongitud());
        }
    }

    private int getLongitudIncompletos(){
        if (this.getAmbito(this.ambitos).equals("@Programa"))
            return this.polaca.getLongitudIncompletos();
        else {
            Polaca polaca = this.funcionesPolacas.get(this.getAmbito(this.ambitos));
            return polaca.getLongitudIncompletos();
        }
    }

    public void apilarBF() {
        if (!error) {
            this.addPasoIncompleto();
            this.addPaso("");
            this.addPaso("BF");
        }
    }

    public void apilarBI() {
        if (!error) {
            int pasoIncompleto = 0;
            if (this.getLongitudIncompletos() > 1){
                int aux = this.getPasoIncompleto();
                pasoIncompleto = this.getPasoIncompleto();
                this.addPasoIncompletoPos(aux);
            }else
                pasoIncompleto = this.getPasoIncompleto();
            this.addPasoIncompleto();
            this.addPaso("");
            this.addPaso("BI");
            this.addLabel();
            this.addPasoFin(pasoIncompleto);
        }
    }

    private void addPasoIncompletoPos(int aux) {
        if (this.getAmbito(this.ambitos).equals("@Programa"))
            this.polaca.addPasoIncompletoPos(aux);
        else
            this.funcionesPolacas.get(this.getAmbito(this.ambitos)).addPasoIncompletoPos(aux);
    }

    public void apilarBT() {
        if (!error) {
            this.addPasoIncompleto();
            this.addPaso("");
            this.addPaso("BT");
        }
    }

    public void completarPaso() {
        if (!error) {
            int pasoIncompleto = this.getPasoIncompleto();
            this.addLabel();
            this.addPasoFin(pasoIncompleto);
        }
    }

    public void apilarPasoWhile() {
        if (!error) {
            this.addPasoIncompleto();
            this.addLabel();
        }
    }

    public void addBreak(){
        this.addPasoIncompleto();
        this.addPaso("");
        this.addPaso("BK");
    }

    public void finWhile() {
        boolean bk = false;
        int pasoIncompletoBF = this.getPasoIncompleto();
        int pasoIncompletoBK = -1;
        if (this.getLongitudIncompletos() == 2){
            pasoIncompletoBK = this.getPasoIncompleto();
            bk = true;
        }
        if (!error) {
            this.addPaso(String.valueOf(this.getPasoIncompleto()));
            this.addPaso("BI");
            this.addLabel();
            this.addPasoFin(pasoIncompletoBF);
            if(bk)
                this.addPasoFin(pasoIncompletoBK);
        }
    }

    public void leerPrint(int pos) {
        if (!error)
            this.addPaso(this.ts.getToken(pos).getLexema(), "PRINT_CAD"); // para tp4
    }

    public void invocarFuncion(int posID, int posParam) {
        // verificamos que exista la funcion
        if (pasada == 1) {
            String ambito = this.buscarAmbitoFunc(posID);
            String id = this.ts.getToken(posID).getLexema();
            Token t = new Token(id + ambito);
            if (funciones.contains(t)) {
                Token funcion = funciones.get(funciones.indexOf(t));
                if (funcion.getUso() == "func" || funcion.getUso() == "var_func") {
                    ultimoToken = funcion;
                    if (ambito != null) {
                        String nuevoIdFunc = id + ambito;
                        // chequeamos parametros
                        String nuevoIdParam = this.ts.getToken(posParam).getLexema() + this.buscarAmbito(posParam);
                        String tipo = this.ts.getToken(this.ts.buscarIndice(nuevoIdParam)).getTipo();
                        if (tipo != funcion.getTipoParametro()) {
                            ListaError.addErrores("Linea " + al.getLinea() + ": Error Semantico, el tipo de la variable pasado por parametros es distinto al tipo de parametros declarado en la funcion");
                            this.error = true;
                        } else if (!error) { //copia valor
                            String paramReal = this.ts.getToken(this.ts.buscarIndice(nuevoIdParam)).getLexema();
                            String paramFormal = funcion.getNombreParametro();
                            this.addPaso(paramReal, paramFormal, ":=");
                            this.addPaso(nuevoIdFunc);
                            this.addPaso("FUNC");
                        }
                    }
                } else {
                    ListaError.addErrores("Linea " + al.getLinea() + ": Error Semantico, " + id + " no es una funcion, no puede ser invocada");
                    this.error = true;
                }

            }
        }
        // eliminamos nuevas entradas de invocaciones de la ts
        this.ts.remove(posParam);
        this.ts.remove(posID);
    }

    public void guardarOperador(String operador) {
        this.operador.add(operador);
    }

    public void setOperador(){
        this.addPaso(operador.remove(0));
    }
}