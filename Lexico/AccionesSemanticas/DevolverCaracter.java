package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class DevolverCaracter extends AccionSemantica {   
    public DevolverCaracter(AnalizadorLexico analizador){
        super(analizador);
    }

    public boolean ejecutar(String buffer){
        return this.getAnalizadorLexico().devolverCaracter();
    }
    
}
