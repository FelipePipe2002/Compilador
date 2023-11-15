package Lexico.AccionesSemanticas;

import Errores.Error;
import Lexico.AnalizadorLexico;

public class CheckRangoUI extends AccionSemantica {
    Integer rango;
    
    public CheckRangoUI(AnalizadorLexico analizar, Integer rango){
        super(analizar);
        this.rango = rango;
    }

    public boolean ejecutar(String buffer){
        Integer bufferValue = Integer.parseInt(buffer);
        
        if(bufferValue>this.rango){
            Error error = new Error("Constante fuera del rango de los enteros sin signo", this.getAnalizadorLexico().getLinea());
            this.getAnalizadorLexico().addErroresLexicos(error);
            return false;
            // throw new Exception("Constante fuera del rango de los enteros sin signo");
        }
        return true;
    }
}
