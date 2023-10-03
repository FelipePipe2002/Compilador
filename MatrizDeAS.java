public class MatrizDeAS extends matriz {
    private AccionSemantica[][] matrizAS;

    public MatrizDeAS(){
        AccionSemantica as1 = new ApilarCaracter();
        AccionSemantica as1y2 = new AccionSemanticaCompuesta(as1,new CheckRangoCaracteres(20));
        AccionSemantica as1y3 = new AccionSemanticaCompuesta(as1,new CheckPalabraReservada());
        AccionSemantica as4 = new CheckRangoUI(223424);
        AccionSemantica as5 = new CheckRangoLong(123124);
        AccionSemantica a16y7 = new AccionSemanticaCompuesta(new AccionSemanticaCompuesta(as1, new CheckRangoPuntoFlotante(2315234)), new NormalizarPuntoFlotante());
        AccionSemantica as8 = new BorrarSaltosLinea();
        this.matrizAS = new AccionSemantica[][] {
        //        |  l  |  L  |  d  |  _  |  .  | "d" | "D" |  +  |  -  | "u" | "i" | "l" |  {  |  }  |  (  |  )  |  ,  |  ;  |  =  |  >  |  <  |  !  |  %  |  *  | " " | \t  | \n  |
        /* 0 */   { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,  as8},
        /* 1 */   { null,as1y2, null, null,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2,as1y2},
        /* 2 */   {as1y3, null,as1y3, null,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3,as1y3},
        /* 3 */   { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 4 */   { null, null, null, null, null, null, null, null, null, null, null,  as5, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 5 */   { null, null, null, null, null, null, null, null, null, null,  as4, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 6 */   {as1  ,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 7 */   {a16y7,a16y7, null,a16y7,a16y7, null, null,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7},
        /* 8 */   { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 9 */   { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 10 */  {a16y7,a16y7, null,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7,a16y7},
        /* 11 */  {  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, as1,   as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 12 */  {  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, as1,   as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 13 */  {  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, as1,   as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 14 */  { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 15 */  {  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 16 */  { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,  as8},
        /* 17 */  {  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1, null,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1,  as1},
        /* 18 */  { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        /* 19 */  { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
        };
    }
    
    public void ejecutarAccionSemantica(int estado, char nuevoCaracter){
        int pos = this.reconocedor(nuevoCaracter);
        if (this.matrizAS[estado][pos] != null) {
            this.matrizAS[estado][pos].ejecutar();
        }
    }
}