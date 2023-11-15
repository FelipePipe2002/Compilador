package Lexico.AccionesSemanticas;

import java.math.BigDecimal;

import Errores.Error;
import Lexico.AnalizadorLexico;

public class CheckRangoPuntoFlotante extends AccionSemantica {
    double rango;

    public CheckRangoPuntoFlotante(AnalizadorLexico analizar, double rango){
        super(analizar);
        this.rango = rango;
    }

    public boolean ejecutar(String buffer){
        buffer = buffer.replaceAll("[dD]", "E");
        BigDecimal bufferValue = new BigDecimal(buffer);
        
        BigDecimal doublemax = new BigDecimal("1.7976931348623157E+308");
        BigDecimal doublemin= new BigDecimal("2.2250738585072014E-308");
        BigDecimal doublezero= new BigDecimal("0.0");

        if (bufferValue.compareTo(doublemax) == 1 || (bufferValue.compareTo(doublemin) == -1 && !bufferValue.equals(doublezero))){
            Error error = new Error("Constante fuera del rango de los punto flotante", this.getAnalizadorLexico().getLinea());
            this.getAnalizadorLexico().addErroresLexicos(error);
        }
        return true;
    }
}
