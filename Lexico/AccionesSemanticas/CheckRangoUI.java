package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;
import Lexico.ErrorLexico;

public class CheckRangoUI extends AccionSemantica {
    Integer rango;
    
    public CheckRangoUI(AnalizadorLexico analizar, Integer rango){
        super(analizar);
        this.rango = rango;
    }

    public boolean ejecutar(String buffer){
        Integer bufferValue = Integer.parseInt(buffer);
        
        if(bufferValue>this.rango){
            ErrorLexico error = new ErrorLexico("Constante fuera del rango de los enteros sin signo", this.getAnalizadorLexico().getLineaArchivo());
            this.getAnalizadorLexico().addErroresLexicos(error);
            return false;
            // throw new Exception("Constante fuera del rango de los enteros sin signo");
        }
        return true;
    }
}
