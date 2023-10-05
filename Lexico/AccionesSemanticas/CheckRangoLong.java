package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class CheckRangoLong implements AccionSemantica {
    long rango;
    
    public CheckRangoLong(Long rango){
        this.rango = rango;
    }

    public void ejecutar() throws Exception{
        AnalizadorLexico.buffer = AnalizadorLexico.buffer.replaceAll("[^\\d]", "");
        Long bufferValue = Long.parseLong(AnalizadorLexico.buffer);
            
        if(bufferValue>rango){
            throw new Exception("Constante fuera del rango de los enteros largos");
        }
    }
}
