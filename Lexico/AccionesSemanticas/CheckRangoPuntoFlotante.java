package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class CheckRangoPuntoFlotante implements AccionSemantica {
    double rango;

    public CheckRangoPuntoFlotante(double rango){
        this.rango = rango;
    }

    public void ejecutar() throws Exception{
        AnalizadorLexico.buffer = AnalizadorLexico.buffer.replaceAll("[dD]", "E");
        Double bufferValue = Double.parseDouble(AnalizadorLexico.buffer);
        
        if(bufferValue>rango){
            throw new Exception("Contante fuera de los rangos del punto flotante");
        }

    }
}
