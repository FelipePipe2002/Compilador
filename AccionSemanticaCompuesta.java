public class AccionSemanticaCompuesta extends AccionSemantica {
    AccionSemantica accionSemantica1;
    AccionSemantica accionSemantica2;

    public AccionSemanticaCompuesta(AccionSemantica as1, AccionSemantica as2){
        this.accionSemantica1 = as1;
        this.accionSemantica2 = as2;
    }

    public void ejecutar(){
        this.accionSemantica1.ejecutar();
        this.accionSemantica2.ejecutar();
    }
}
