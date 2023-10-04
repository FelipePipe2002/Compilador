package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class CheckRangoUI extends CheckRango implements AccionSemantica {
    Integer rango;
    
    public CheckRangoUI(Integer rango){
        this.rango = rango;
    }

    public void ejecutar(){
        AnalizadorLexico.buffer = AnalizadorLexico.buffer.replaceAll("[^\\d]", "");
        Integer bufferValue = Integer.parseInt(AnalizadorLexico.buffer);
        
        if(bufferValue>rango){
            rangoError("Constante fuera del rango de los enteros sin signo");
        }
    }
}
