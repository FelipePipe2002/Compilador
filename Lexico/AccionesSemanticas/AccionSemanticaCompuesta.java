package Lexico.AccionesSemanticas;
public class AccionSemanticaCompuesta extends AccionSemantica {
    AccionSemantica accionSemantica1;
    AccionSemantica accionSemantica2;

    public AccionSemanticaCompuesta(AccionSemantica as1, AccionSemantica as2){
        super(as1.getAnalizadorLexico());
        this.accionSemantica1 = as1;
        this.accionSemantica2 = as2;
    }

    public boolean ejecutar(String buffer){
        boolean ejecutarAS1 = this.accionSemantica1.ejecutar(buffer);
        if (ejecutarAS1){
            return this.accionSemantica2.ejecutar(buffer);
        }
        return ejecutarAS1;
    }
}
