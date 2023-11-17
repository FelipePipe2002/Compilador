package Lexico;

import java.util.ArrayList;

public class Atributos {
    private Token token;
    private String tipo;
    private boolean uso;
    private String interfaz;
    private String padreClase;
    private int nivel;
    private boolean atributoDeClase;

    public Atributos(Token token){
        this.token = token;
        this.tipo = token.toString();
        this.uso = false;
        this.interfaz = "";
        this.padreClase = "";
        this.nivel = 0;
        this.atributoDeClase = false;
    }

    public Atributos(Token token, String tipo){
        this.token = token;
        this.tipo = tipo;
        this.uso = false;
        this.interfaz = "";
        this.padreClase = "";
        this.nivel = 0;
        this.atributoDeClase = false;
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

    public String getPadreClase() {
        return this.padreClase;
    }

    private void setNivelHerencia(int nivelHerenciaPadre) {
        this.nivel = nivelHerenciaPadre;
    }

    public int getNivelHerencia() {
        return this.nivel;
    }

    public void addProfundidad(int profundidadPadre) {
        this.nivel = profundidadPadre + 1;
    }

    public int getProfundidad() {
        return this.nivel;
    }

    public void setAtributoClase(boolean esAtributoDeClase){
        this.atributoDeClase = esAtributoDeClase;
    }

    public boolean getAtributoClase(){
        return this.atributoDeClase;
    }
}
