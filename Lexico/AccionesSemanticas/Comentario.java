package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class Comentario implements AccionSemantica {

    @Override
    public void ejecutar() {
        AnalizadorLexico.buffer = AnalizadorLexico.buffer.substring(0, AnalizadorLexico.buffer.length() - 1);
    }
    
}
