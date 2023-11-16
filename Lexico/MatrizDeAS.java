package Lexico;

import Lexico.AccionesSemanticas.*;

public class MatrizDeAS extends Matriz {
    private AccionSemantica[][] matrizAS;
    private final int MAXCARACTERES = 20;
    private final int MAXRANGOINT = 65535;
    private final long MAXRANGOLONG = 2147483648l;
    private final double MAXRANGOPUNTOFLOTANTE = 1.7976931348623157E308;

    // as1: devuelve el ultimo caracter al archivo
    // as2: limita el tamaño del identificador a 20
    // as3: chequea que exista la palabra clave
    // as4: chequea el rango de los enteros sin signo
    // as5: cheque el rango de los enteros largos
    // as6: Normaliza y chequea rango de punto flotante
    // as7: Cuenta saltos de linea
    
    public MatrizDeAS(AnalizadorLexico analizadorLexico){   
        AccionSemantica as0 = new ApilarCaracter(analizadorLexico);
        AccionSemantica as1 = new DevolverCaracter(analizadorLexico);
        AccionSemantica as2 = new CheckRangoCaracteres(analizadorLexico, MAXCARACTERES);
        AccionSemantica as1y2 = new AccionSemanticaCompuesta(as1,as2);
        AccionSemantica as4 = new CheckRangoUI(analizadorLexico, MAXRANGOINT);
        AccionSemantica as5 = new CheckRangoLong(analizadorLexico, MAXRANGOLONG);
        AccionSemantica as6 = new CheckRangoPuntoFlotante(analizadorLexico, MAXRANGOPUNTOFLOTANTE);
        AccionSemantica as1y6 = new AccionSemanticaCompuesta(as1,as6);
        AccionSemantica as7 = new ContarSaltosLinea(analizadorLexico);
        AccionSemantica as8 = new Comentario(analizadorLexico);
        this.matrizAS = new AccionSemantica[][] {
            
        //        |  l  |  L  |  d  |  _  |  .  | "d" | "D" |  +  |  -  | "u" | "i" | "l" |  {  |  }  |  (  |  )  |  ,  |  ;  |  =  |  >  |  <  |  !  |  %  |  *  |  /  | " " | \t  | \n  | \r | otro | fin
        /* 0 */   { as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , null, as0 , as0 , null, null,  as7, null, null, null},
        /* 1 */   { as0 ,as1y2, as0 , as0 ,as1y2, as0 ,as1y2,as1y2,as1y2, as0 , as0 , as0 ,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2, as2 ,as1y2, as2 },
        /* 2 */   { as1 , as0 , as1 , as0 , as1 , as1 , as0 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , null, as1 , null},
        /* 3 */   { as1 , as1 , as0 , null, as0 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 },
        /* 4 */   { as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , null, as1 , as5 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 },
        /* 5 */   { as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as4 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 },
        /* 6 */   { as1 , as1 , as0 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1,  as1 , null, as1 , null},
        /* 7 */   {as1y6,as1y6, as0 ,as1y6,as1y6, as0 , as0 ,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6, null,as1y6, as6 },
        /* 8 */   { as1 , as1 , as1 , as1 , as1 , as1 , as1 , as0 , as0 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 },
        /* 9 */   { as1 , as1 , as0 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 },
        /* 10 */  {as1y6,as1y6, as0 ,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6, null,as1y6, as6 },
        /* 11 */  { as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as0 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , null, as1 , null},
        /* 12 */  { as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as0 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , null, as1 , null},
        /* 13 */  { as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as0 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , null, as1 , null},
        /* 14 */  { as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as0 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 },
        /* 15 */  { as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as0 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , null, as1 , null},
        /* 16 */  { as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , as0 , null, as0 , as0 , as0 , as0 , as7 , null, as0 , as0 },
        /* 17 */  { as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as8 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , as1 , null, as1 , null},
        /* 18 */  { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, as7 , null, null, as1 },
        /* 19 */  { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, as8 , null, null, null, as7 , null, null, as1 }
        
        };
    }
    
    public void ejecutarAccionSemantica(int estado, int nuevoCaracter, String buffer){
        int pos;
        if (nuevoCaracter == -1){ // fin de archivo
            pos = 30;
        } else
            pos = this.reconocedor((char)nuevoCaracter);

        if (this.matrizAS[estado][pos] != null) {
            this.matrizAS[estado][pos].ejecutar(buffer);
        }
    }
}
