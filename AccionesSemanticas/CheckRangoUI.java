package AccionesSemanticas;
public class CheckRangoUI extends CheckRango implements AccionSemantica {
    Integer rango;
    
    public CheckRangoUI(Integer rango){
        this.rango = rango;
    }

    public void ejecutar(String buffer){
        buffer = buffer.replaceAll("[^\\d]", "");
        Integer bufferValue = Integer.parseInt(buffer);
        
        if(bufferValue>rango){
            rangoError("Constante fuera del rango de los enteros sin signo");
        }
    }
}
