package GeneracionASM;

import AnalizadorLexico.TablaSimbolos;
import CodigoIntermedio.Polaca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class GeneradorAsm {
    private static Stack<String> pila = new Stack();
    private static TablaSimbolos ts;
    private static UtilidadReg registros = new UtilidadReg();
    private static int numAux = 1;
    private static String invocador = "";
    private static String comparador;
    private static HashMap<String, String> PostFunc = new HashMap<>();
    private static HashMap<String, String> postReturn = new HashMap<>();
    private static String funcInvocada = "";

    public GeneradorAsm(TablaSimbolos ts){
        this.ts = ts;
    }

    public static ArrayList<String> generarAssemblerFunc(HashMap<String, Polaca> funcionesPolacas){
        ArrayList<String> asm = new ArrayList<>();
        for(Map.Entry<String, Polaca> p : funcionesPolacas.entrySet()){
            String[] s = p.getKey().split("@");
            invertirArr(s);
            StringBuilder sb = new StringBuilder();
            for(String str : s){
                sb.append(str + "@");
            }
            PostFunc.put(sb.substring(0,sb.length()-2), null);
            invocador = sb.substring(0,sb.length()-2);
            asm.add("L_" + sb.substring(0,sb.length()-2)+ ":");
            asm.addAll(generarAssembler(p.getValue(), true));
            asm.add("ret");
            invocador = "";
            postReturn.put(sb.substring(0,sb.length()-2), pila.pop());
        }
        return asm;
    }

    private static void invertirArr(String[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            String temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }
    public static ArrayList<String> generarAssembler(Polaca p, boolean func) {
        funcInvocada = "";
        ArrayList<String> asm = new ArrayList<>();
        for(String paso : p.getPolaca()){
            if (paso.charAt(0) == 'L')
                asm.add(paso + ':');
            else switch (paso) {
                case "FUNC":{
                    int reg = registros.getRegistroLibre();
                    funcInvocada = pila.peek();
                    if(func){
                        asm.add("MOV " + registros.getNombreRegistro(reg) + ", " + ts.getToken(ts.buscarIndice(invocador)).getRefFunc());
                        asm.addAll(GeneradorFunc.getInstruccion(pila, ts, registros, reg, true));
                    } else
                        asm.addAll(GeneradorFunc.getInstruccion(pila, ts, registros, reg, false));
                    break;
                }
                case "+": asm.addAll(GeneradorSuma.getInstruccion(pila, ts, registros, "+"));
                    break;
                case "-": asm.addAll(GeneradorResta.getInstruccion(pila, ts, registros, "-"));
                   break;
                case "*": asm.addAll(GeneradorMult.getInstruccion(pila, ts, registros, "*"));
                    break;
                case "/": asm.addAll(GeneradorDiv.getInstruccion(pila, ts, registros, "/"));
                    break;
                case ":=": asm.addAll(GeneradorAsig.getInstruccion(pila, ts, registros));
                    break;
                case "<":
                case "<=":
                case ">":
                case ">=":
                case "==":
                case "!=": {
                    asm.addAll(GeneradorComp.getInstruccion(pila, ts, registros));
                    comparador = paso;
                    if(func)
                        PostFunc.put(invocador, paso);
                    break;
                }
                case "BI": asm.add("JMP " + "L" + pila.pop());
                    break;
                case "BF": asm.addAll(GeneradorComp.getInstruccionSalto("BF", comparador, pila.pop()));
                    break;
                case "BT": asm.addAll(GeneradorComp.getInstruccionSalto("BT", PostFunc.get(funcInvocada), pila.pop()));
                    break;
                case "BK": asm.add("JMP " + "L" + pila.pop());
                    break;
                case "PRINT_CAD": asm.addAll(GeneradorPrint.getInstruccion(pila.pop()));
                    break;
                default:
                    pila.push(paso);
           }
        }
        return asm;
    }

    public static void IncrementarAux() {
        numAux++;
    }

    public static int getNumAux() {
        return numAux;
    }

    public static void cambiarElemento(String regAnt, String elemento){
        int pos = pila.search(regAnt);
        pila.set(pos, elemento);
    }

    public static String getPrefijo(String op){
        if(op.charAt(0) == '@')
            return "";
        else
            return "_";
    }

    public static HashMap<String, String> getPostReturn() {
        return postReturn;
    }

    public static String getFuncInvocada() {
        return funcInvocada;
    }

    public static String getAsmFinal(HashMap<String, Polaca> funcionesPolacas, Polaca p){
        StringBuilder asmPrograma = new StringBuilder();
        StringBuilder asmFunciones = new StringBuilder();

        for(String s : generarAssemblerFunc(funcionesPolacas))
            asmFunciones.append(s + '\n');

        for(String s : generarAssembler(p, false))
            asmPrograma.append(s + '\n');

        return ".386\n" +
                ".model flat, stdcall\n" +
                "option casemap :none\n" +
                "include \\masm32\\include\\windows.inc\n" +
                "include \\masm32\\include\\user32.inc\n" +
                "include \\masm32\\include\\kernel32.inc\n" +
                "includelib \\masm32\\lib\\kernel32.lib\n" +
                "includelib \\masm32\\lib\\user32.lib\n\n" +
                ".data\n" +
                ts.toAsm() +
                "_0p0 DQ 0.0 \n" +
                "@resta_neg DB 'Error: Resultado de resta menor a cero.', 0" + "\n" +
                "@recursion DB 'Error: Recursiones recursion mutua en procedimientos', 0" + "\n" +
                "@div_cero DB 'Error: Division por cero no permitida', 0" + "\n" +
                "@error_asig DB 'Error: No se puede asignar double a ulong', 0" + "\n\n" +
                ".code\n" +
                asmFunciones + '\n' +
                "start:" + '\n' +
                asmPrograma +
                "JMP L_final" + '\n' +
                "L_errorTipo:" + '\n' +
                "invoke MessageBox, NULL, addr @error_asig, addr @error_asig, MB_OK" + '\n' +
                "JMP L_final" + '\n' +
                "L_resta_neg:" + '\n' +
                "invoke MessageBox, NULL, addr @resta_neg, addr @resta_neg, MB_OK" + '\n' +
                "JMP L_final" + '\n' +
                "L_rec:" + '\n' +
                "invoke MessageBox, NULL, addr @recursion, addr @recursion, MB_OK" + '\n' +
                "JMP L_final" + '\n' +
                "L_div_cero:" + '\n' +
                "invoke MessageBox, NULL, addr @div_cero, addr @div_cero, MB_OK" + '\n' +
                "JMP L_final" + '\n' +
                "L_final:" + '\n' +
                "invoke ExitProcess, 0" + '\n' +
                "end start";
    }


}
