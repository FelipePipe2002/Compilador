package Lexico;
public class MatrizDeTransicionEstados extends Matriz {
    private int[][] matrizTransicion;
    private final int MAX = 29;

    public MatrizDeTransicionEstados(){
        this.matrizTransicion = new int[][] {
    //        |  l  |  L  |  d  |  _  |  .  | "d" | "D" |  +  |  -  | "u" | "i" | "l" |  {  |  }  |  (  |  )  |  ,  |  ;  |  =  |  >  |  <  |  !  |  %  |  *  | " " | \t  | \n  | \r  | otro | fin
    /* 0 */   {  1  ,  2  ,  3  ,  1  ,  6  ,  1  ,  2  , -2  , 15  ,  1  ,  1  ,  1  , -2  , -2  , -2  , -2  , -2  , -2  , 11  , 12  , 13  , 14  , 16  , 17  ,  0  ,  0  ,  0  ,  0  , -1   , -2},
    /* 1 */   {  1  , -2  ,  1  ,  1  , -2  ,  1  , -2  , -2  , -2  ,  1  ,  1  ,  1  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2   , -2},
    /* 2 */   { -2  ,  2  , -2  ,  2  , -2  , -2  ,  2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2   , -2},
    /* 3 */   { -1  , -1  ,  3  ,  4  ,  7  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1   , -1},
    /* 4 */   { -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  ,  5  , -1  , -2  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1   , -1},
    /* 5 */   { -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -2  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1   , -1},
    /* 6 */   { -2  , -2  ,  7  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2   , -2},
    /* 7 */   { -2  , -2  ,  7  , -2  , -2  ,  8  ,  8  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2   , -2},
    /* 8 */   { -1  , -1  , -1  , -1  , -1  , -1  , -1  ,  9  ,  9  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1   , -1},
    /* 9 */   { -1  , -1  , 10  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1   , -1},
    /* 10 */  { -2  , -2  , 10  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2   , -2},
    /* 11 */  { -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2   , -2},
    /* 12 */  { -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2   , -2},
    /* 13 */  { -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2   , -2},
    /* 14 */  { -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -1  , -2  , -1  , -1  , -1  , -1  , -1  , -1  , -1   , -1},
    /* 15 */  { -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2   , -2},
    /* 16 */  { 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , 16  , -2  , 16  , 16  , 16  , 16  , 16  , 16   , -1},
    /* 17 */  { -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , 18  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2  , -2   , -2},
    /* 18 */  { 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 19  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18   , -1},
    /* 19 */  { 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 19  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  , 18  ,  0  , 18  , 18  , 18  , 18  , 18   , -1}
        };
        //-1 Error
        //-2 Final
    }

    public int getProximoEstado(int estado, int nuevoCaracter) {
        int pos;
        if (nuevoCaracter == -1){
            pos = MAX;
        } else
            pos = this.reconocedor((char)nuevoCaracter);
        /*if (nuevoCaracter == 13){
           System.out.println(estado + " | " + this.matrizTransicion[estado][pos] + "|/r|" + (int) nuevoCaracter); 
        } else if (nuevoCaracter == 10){
           System.out.println(estado + " | " + this.matrizTransicion[estado][pos] + "|/n|" + (int) nuevoCaracter); 
        } else{
            System.out.println(estado + " | " + this.matrizTransicion[estado][pos] + "|" + (char)nuevoCaracter+ "|"+ nuevoCaracter);
        }*/
        return this.matrizTransicion[estado][pos];
    }
}
