package Lexico;

public class Atributos {
    private Token token;
    private String tipo;
    private boolean uso;

    public Atributos(Token token){
        this.token = token;
        this.tipo = token.toString();
        this.uso = false;
    }

    public Atributos(Token token, String tipo){
        this.token = token;
        this.tipo = tipo;
        this.uso = false;
    }

    public Token getToken() {
        return token;
    }
    public void setToken(Token token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isUso() {
        return uso;
    }
    public void setUso(boolean uso) {
        this.uso = uso;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return token + " | " + tipo + " | " + uso;
    }
}
