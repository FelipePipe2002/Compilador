package Lexico;

public class Atributos {
    private Token token;
    private String tipo;
    private boolean uso;
    private String interfaz;
    private String padreClase;
    private int nivelHerencia;

    public Atributos(Token token){
        this.token = token;
        this.tipo = token.toString();
        this.uso = false;
        this.interfaz = " ";
        this.padreClase = " ";
        this.nivelHerencia = 0;
    }

    public Atributos(Token token, String tipo){
        this.token = token;
        this.tipo = tipo;
        this.uso = false;
        this.interfaz = " ";
        this.padreClase = " ";
        this.nivelHerencia = 0;
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

    public void addInterfaz(String interfaz) {
        this.interfaz = interfaz;
    }

    public String getInterfaz() {
        return this.interfaz;
    }

    public void addHerencia(String padre, int nivelHerenciaPadre) {
        this.padreClase = padre;
        setNivelHerencia(nivelHerenciaPadre + 1);
    }

    public String getHerencia() {
        return this.padreClase;
    }

    private void setNivelHerencia(int nivelHerenciaPadre) {
        this.nivelHerencia = nivelHerenciaPadre;
    }

    public int getNivelHerencia() {
        return this.nivelHerencia;
    }

}