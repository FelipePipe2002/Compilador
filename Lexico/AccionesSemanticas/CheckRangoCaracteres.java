package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;
import Errores.Error;;
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
            Error error = new Error("Exceso en la longitud de caracteres aceptable", this.getAnalizadorLexico().getLinea(),"WARNING");
            this.getAnalizadorLexico().addErroresLexicos(error);
        }
        return false;
    }
}
