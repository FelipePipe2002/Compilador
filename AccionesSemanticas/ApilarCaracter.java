package AccionesSemanticas;
public class ApilarCaracter implements AccionSemantica {
    public static boolean apilar;
    public ApilarCaracter(){
        ApilarCaracter.apilar = false;
    }

    public void ejecutar(String buffer){
        ApilarCaracter.apilar = true;
    }

    
}
