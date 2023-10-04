package AccionesSemanticas;
public class ContarSaltosLinea implements AccionSemantica {
    public static int cantSaltos;

    public ContarSaltosLinea(){
        ContarSaltosLinea.cantSaltos = 0;
    }

    public void ejecutar(String buffer){
        ContarSaltosLinea.cantSaltos++;
    }
}
