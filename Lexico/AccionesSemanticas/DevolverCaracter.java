package Lexico.AccionesSemanticas;

import Lexico.AnalizadorLexico;

public class DevolverCaracter implements AccionSemantica {   
    public DevolverCaracter(){
    }

    public void ejecutar() throws Exception{
        AnalizadorLexico.devolverCaracter();
    }

    
}
