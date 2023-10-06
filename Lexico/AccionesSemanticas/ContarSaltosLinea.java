package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class ContarSaltosLinea extends AccionSemantica {

    public ContarSaltosLinea(AnalizadorLexico analizador){
        super(analizador);
    }

    public boolean ejecutar(String buffer){
        this.getAnalizadorLexico().sumarSaltoLinea();
        return true;
    }
}
