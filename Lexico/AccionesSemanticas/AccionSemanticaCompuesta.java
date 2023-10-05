package Lexico.AccionesSemanticas;
public class AccionSemanticaCompuesta implements AccionSemantica {
    AccionSemantica accionSemantica1;
    AccionSemantica accionSemantica2;

    public AccionSemanticaCompuesta(AccionSemantica as1, AccionSemantica as2){
        this.accionSemantica1 = as1;
        this.accionSemantica2 = as2;
    }

    public void ejecutar() throws Exception {
        this.accionSemantica1.ejecutar();
        this.accionSemantica2.ejecutar();
    }
}
