package Lexico.AccionesSemanticas;

import java.io.RandomAccessFile;
import Lexico.AnalizadorLexico;;

public class ApilarCaracter implements AccionSemantica {   
    public ApilarCaracter(){
    }

    public void ejecutar(){
        try {
            AnalizadorLexico.devolverCaracter();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
}
