package Lexico.AccionesSemanticas;
public class ContarSaltosLinea implements AccionSemantica {
    public static int cantSaltos;

    public ContarSaltosLinea(){
        ContarSaltosLinea.cantSaltos = 1;
    }

    public void ejecutar(){
        System.out.println(ContarSaltosLinea.cantSaltos);
        ContarSaltosLinea.cantSaltos++;
        System.out.println(ContarSaltosLinea.cantSaltos);
    }
}
