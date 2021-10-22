package AnalizadorLexico;

public class TokenLexema {
    private int id;
    private int indiceTabla;

    public TokenLexema(int indiceTabla){
        this.indiceTabla = indiceTabla;
    }

    public void setID(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getIndiceTabla() {
        return indiceTabla;
    }
}
