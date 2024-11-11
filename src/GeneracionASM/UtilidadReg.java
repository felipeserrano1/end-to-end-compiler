package GeneracionASM;

import AnalizadorLexico.TablaSimbolos;
import AnalizadorLexico.Token;

import java.util.ArrayList;

public class UtilidadReg {
        public static final int EAX = 0;
        public static final int EBX = 1;
        public static final int ECX = 2;
        public static final int EDX = 3;

        private ArrayList<InfoReg> registros = new ArrayList<>();

        public UtilidadReg(){
                registros.add(new InfoReg());
                registros.add(new InfoReg());
                registros.add(new InfoReg());
                registros.add(new InfoReg());
        }


        public boolean esRegistro(String op) {
                return op == "EAX" || op == "EBX" || op == "ECX" || op == "EDX";
        }

        public int getRegistroLibre(){
                if(!registros.get(EBX).isOcupado()) return EBX;
                if(!registros.get(ECX).isOcupado()) return ECX;
                if(!registros.get(EAX).isOcupado()) return EAX;
                if(!registros.get(EDX).isOcupado()) return EDX;

                return -1;
        }

        public int getRegistroLibreMultDiv(int reg, ArrayList<String> asm, TablaSimbolos ts) {
                if(!registros.get(reg).isOcupado()){
                        return reg;
                }
                int nuevoReg = getRegistroLibre();
                if (nuevoReg == -1){
                        String aux = "@aux" + GeneradorAsm.getNumAux();
                        Token token = new Token("@aux" + GeneradorAsm.getNumAux(), "ulong");
                        ts.addSimbolo(token);
                        GeneradorAsm.IncrementarAux();
                        asm.add("MOV " + aux + ", " + getNombreRegistro(reg));
                        GeneradorAsm.cambiarElemento(getNombreRegistro(reg), aux);
                } else {
                        modificarRegistro(nuevoReg, true);
                        asm.add("MOV " + getNombreRegistro(nuevoReg) + ", " + getNombreRegistro(reg));
                        GeneradorAsm.cambiarElemento(getNombreRegistro(reg), getNombreRegistro(nuevoReg));
                }
                return reg;
        }


        public String getNombreRegistro(int pos){
                switch (pos) {
                        case 0:
                                return "EAX";
                        case 1:
                                return "EBX";
                        case 2:
                                return "ECX";
                        case 3:
                                return "EDX";
                }
                return "";
        }

        public int getIdRegistro(String nombre){
                switch(nombre) {
                        case "EAX":
                                return 0;
                        case "EBX":
                                return 1;
                        case "ECX":
                                return 2;
                        case "EDX":
                                return 3;
                }
                return -1;
        }

        public void modificarRegistro(int pos, boolean ocupado) {
                this.registros.get(pos).setOcupado(ocupado);
        }
}
