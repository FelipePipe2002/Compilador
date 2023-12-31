package Lexico.AccionesSemanticas;

import Errores.Error;
import Lexico.AnalizadorLexico;

public class CheckRangoLong extends AccionSemantica {
    long rango;
    
    public CheckRangoLong(AnalizadorLexico analizador, Long rango){
        super(analizador);
        this.rango = rango;
    }

    public boolean ejecutar(String buffer){
        Long bufferValue = Long.parseLong(buffer);
        
        if(bufferValue>this.rango){
            Error error = new Error("Constante fuera del rango de los enteros largos", this.getAnalizadorLexico().getLinea());
            this.getAnalizadorLexico().addErroresLexicos(error);
            return false;
            // throw new Exception("Constante fuera del rango de los enteros largos");
        }
        return true;
    }
}
