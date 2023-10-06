package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;
import Lexico.ErrorLexico;

public class CheckRangoCaracteres extends AccionSemantica {
    int rango;
    
    public CheckRangoCaracteres(AnalizadorLexico analizador, int rango){
        super(analizador);
        this.rango = rango;
    }

    public boolean ejecutar(String buffer){
        if (buffer.length() > this.rango){
            buffer = buffer.substring(0,20);
            this.getAnalizadorLexico().setBuffer(buffer);
            ErrorLexico error = new ErrorLexico("Exceso del rango de caracteres aceptable", this.getAnalizadorLexico().getLineaArchivo());
            this.getAnalizadorLexico().addErroresLexicos(error);
        }
        return false;
    }
}
