package AccionesSemanticas;
public class CheckRangoLong extends CheckRango implements AccionSemantica {
    long rango;
    
    public CheckRangoLong(Long rango){
        this.rango = rango;
    }

    public void ejecutar(String buffer){
        buffer = buffer.replaceAll("[^\\d]", "");
        Long bufferValue = Long.parseLong(buffer);
            
        if(bufferValue>rango){
            rangoError("Constante fuera del rango de los enteros largos");
        }
    }
}
