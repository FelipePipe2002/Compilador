package AccionesSemanticas;
public class CheckRangoPuntoFlotante extends CheckRango implements AccionSemantica {
    double rango;

    public CheckRangoPuntoFlotante(double rango){
        this.rango = rango;
    }

    public void ejecutar(String buffer){
        buffer = buffer.replaceAll("[dD]", "E");
        Double bufferValue = Double.parseDouble(buffer);
        
        if(bufferValue>rango){
            rangoError("Contante fuera de los rangos del punto flotante");
        }

    }
}
