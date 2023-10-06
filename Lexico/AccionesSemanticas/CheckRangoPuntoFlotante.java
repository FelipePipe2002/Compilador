package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;
import Lexico.ErrorLexico;

public class CheckRangoPuntoFlotante extends AccionSemantica {
    double rango;

    public CheckRangoPuntoFlotante(AnalizadorLexico analizar, double rango){
        super(analizar);
        this.rango = rango;
    }

    public boolean ejecutar(String buffer){
        buffer = buffer.replaceAll("[dD]", "E");
        Double bufferValue = Double.parseDouble(buffer);
        
        if(bufferValue>this.rango){
            ErrorLexico error = new ErrorLexico("Constante fuera del rango de los punto flotante", this.getAnalizadorLexico().getLineaArchivo());
            this.getAnalizadorLexico().addErroresLexicos(error);
            return false;
            // throw new Exception("Contante fuera de los rangos del punto flotante");
        }
        return true;
    }
}
