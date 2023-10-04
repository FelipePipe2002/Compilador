package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class CheckRangoPuntoFlotante extends CheckRango implements AccionSemantica {
    double rango;

    public CheckRangoPuntoFlotante(double rango){
        this.rango = rango;
    }

    public void ejecutar(){
        AnalizadorLexico.buffer = AnalizadorLexico.buffer.replaceAll("[dD]", "E");
        Double bufferValue = Double.parseDouble(AnalizadorLexico.buffer);
        
        if(bufferValue>rango){
            rangoError("Contante fuera de los rangos del punto flotante");
        }

    }
}
