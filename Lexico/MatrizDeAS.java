package Lexico;

import java.io.RandomAccessFile;
import Lexico.AccionesSemanticas.*;

public class MatrizDeAS extends Matriz {
    private AccionSemantica[][] matrizAS;

    // as1: devuelve el ultimo caracter al archivo              | char
    // as2: limita el tamño del identificador a 20              | buffer
    // as3: chequea que exista la palabra clave                 | buffer
    // as4: chequea el rango de los enteros sin signo           | buffer
    // as5: cheque el rango de los enteros largos               | buffer
    // as6: Normaliza y chequea rango de punto flotante         | buffer
    // as7: Cuenta saltos de linea                              | buffer

    public MatrizDeAS(){
        AccionSemantica as1 = new ApilarCaracter();
        AccionSemantica as1y2 = new AccionSemanticaCompuesta(as1,new CheckRangoCaracteres(20));
        AccionSemantica as1y3 = new AccionSemanticaCompuesta(as1,new CheckPalabraReservada());
        AccionSemantica as4 = new CheckRangoUI(65535);
        AccionSemantica as5 = new CheckRangoLong(2147483648l);
        AccionSemantica as1y6 = new AccionSemanticaCompuesta(as1,new CheckRangoPuntoFlotante(1.7976931348623157E308));
        AccionSemantica as7 = new ContarSaltosLinea();
        this.matrizAS = new AccionSemantica[][] {
            
        //        |  l  |  L  |  d  |  _  |  .  | "d" | "D" |  +  |  -  | "u" | "i" | "l" |  {  |  }  |  (  |  )  |  ,  |  ;  |  =  |  >  |  <  |  !  |  %  |  *  | " " | \t  | \n  |
        /* 0 */   { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,  as7},
        /* 1 */   { null,as1y2, null, null,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2},
        /* 2 */   {as1y3, null,as1y3, null,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3},
        /* 3 */   { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 4 */   { null, null, null, null, null, null, null, null, null, null, null,  as5, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 5 */   { null, null, null, null, null, null, null, null, null, null,  as4, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 6 */   {as1  ,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 7 */   {as1y6,as1y6, null,as1y6,as1y6, null, null,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6},
        /* 8 */   { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 9 */   { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 10 */  {as1y6,as1y6, null,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6,as1y6},
        /* 11 */  {  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, as1,   as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 12 */  {  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, as1,   as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 13 */  {  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, as1,   as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 14 */  { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 15 */  {  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 16 */  { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,  as7},
        /* 17 */  {  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 18 */  { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 19 */  { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
        
        };
    }
    
    public void ejecutarAccionSemantica(int estado, char nuevoCaracter) throws Exception{
        int pos = this.reconocedor(nuevoCaracter);
        if (this.matrizAS[estado][pos] != null) {
            this.matrizAS[estado][pos].ejecutar();
        }
    }
}