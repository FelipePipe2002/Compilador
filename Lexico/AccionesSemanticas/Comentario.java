package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class Comentario extends AccionSemantica {
    public static boolean creandoComentario;

    public Comentario(AnalizadorLexico analizador){
        super(analizador);
        creandoComentario = false;
    }

    @Override
    public boolean ejecutar(String buffer) {
        if(!creandoComentario){
        buffer = buffer.substring(0, buffer.length() - 1);
        this.getAnalizadorLexico().setBuffer(buffer);
        }
        creandoComentario = !creandoComentario;
        return true;
    }

    public boolean isCreandoComentario() {
        return creandoComentario;
    }
    
}
