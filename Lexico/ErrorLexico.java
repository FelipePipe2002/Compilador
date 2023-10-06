package Lexico;

public class ErrorLexico extends Exception {
    String mensaje;
    int linea;

    public ErrorLexico(String mensaje, int linea){
        this.mensaje = mensaje;
        this.linea = linea;
    }

    public String getMensaje(){
        return this.mensaje;
    }

    public int getLinea(){
        return this.linea;
    }
}
