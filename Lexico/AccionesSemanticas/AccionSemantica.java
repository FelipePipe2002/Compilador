package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public abstract class AccionSemantica {
    private AnalizadorLexico analizadorLexico;
    
    public AccionSemantica(AnalizadorLexico analizador){
        this.analizadorLexico = analizador;
    }

    public void setAnalizadorLexico(AnalizadorLexico analizador){
        this.analizadorLexico = analizador;
    }
    
    public AnalizadorLexico getAnalizadorLexico(){
        return this.analizadorLexico;
    }

    public abstract boolean ejecutar(String buffer);
}