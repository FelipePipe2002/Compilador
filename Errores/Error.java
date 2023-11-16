package Errores;


public class Error extends Exception {
    String mensaje;
    int linea;
    String tipo;

    public Error(String mensaje, String tipo){
        this.mensaje = mensaje;
        this.linea = -1;
        this.tipo = tipo;
    }

    public Error(String mensaje, int linea){
        this.mensaje = mensaje;
        this.linea = linea;
        this.tipo = "ERROR";
    }

    public Error(String mensaje, int linea, String tipo){
        this.mensaje = mensaje;
        this.linea = linea;
        this.tipo = tipo;
    }

    public String getMensaje(){
        return this.mensaje;
    }

    public String getTipo(){
        return tipo;
    }

    public int getLinea(){
        return this.linea;
    }

    @Override
    public String toString() {
        if(this.linea < 0){
            return tipo + ": " + mensaje;
        }
        return tipo + ": " + mensaje + " en la linea: " + linea;
    }
}
