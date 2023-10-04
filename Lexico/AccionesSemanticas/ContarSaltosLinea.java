package Lexico.AccionesSemanticas;
public class ContarSaltosLinea implements AccionSemantica {
    public static int cantSaltos;

    public ContarSaltosLinea(){
        ContarSaltosLinea.cantSaltos = 0;
    }

    public void ejecutar(){
        ContarSaltosLinea.cantSaltos++;
    }
}
