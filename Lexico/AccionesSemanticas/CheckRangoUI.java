package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class CheckRangoUI implements AccionSemantica {
    Integer rango;
    
    public CheckRangoUI(Integer rango){
        this.rango = rango;
    }

    public void ejecutar() throws Exception{
        AnalizadorLexico.buffer = AnalizadorLexico.buffer.replaceAll("[^\\d]", "");
        Integer bufferValue = Integer.parseInt(AnalizadorLexico.buffer);
        
        if(bufferValue>rango){
            throw new Exception("Constante fuera del rango de los enteros sin signo");
        }
    }
}
