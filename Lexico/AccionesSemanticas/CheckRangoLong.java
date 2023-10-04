package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class CheckRangoLong extends CheckRango implements AccionSemantica {
    long rango;
    
    public CheckRangoLong(Long rango){
        this.rango = rango;
    }

    public void ejecutar(){
        AnalizadorLexico.buffer = AnalizadorLexico.buffer.replaceAll("[^\\d]", "");
        Long bufferValue = Long.parseLong(AnalizadorLexico.buffer);
            
        if(bufferValue>rango){
            rangoError("Constante fuera del rango de los enteros largos");
        }
    }
}
