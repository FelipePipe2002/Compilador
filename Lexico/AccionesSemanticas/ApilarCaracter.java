package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class ApilarCaracter extends AccionSemantica {

    public ApilarCaracter(AnalizadorLexico analizador){
        super(analizador);
    }

    public boolean ejecutar(String buffer){
        this.getAnalizadorLexico().sumarCaracter();
        return true;
    }
    
}
