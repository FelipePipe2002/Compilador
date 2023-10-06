package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class Comentario extends AccionSemantica {

    public Comentario(AnalizadorLexico analizador){
        super(analizador);
    }

    @Override
    public boolean ejecutar(String buffer) {
        buffer = buffer.substring(0, buffer.length() - 1);
        this.getAnalizadorLexico().setBuffer(buffer);
        return true;
    }
    
}
