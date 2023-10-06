package Lexico;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class AnalizadorLexico{

    private RandomAccessFile reader;
    private MatrizDeTransicionEstados matrizTransicion;
    private MatrizDeAS matrizAcciones;
    private ArrayList<ErrorLexico> erroresLexicos;
    private String buffer;
    private int lineaArchivo;
    private char ultimoCararterLeido;
    private Tabla tablaSimbolos;
    private TablaPalabrasReservadas tablaPalabrasReservadas;

    public AnalizadorLexico(String[] args) throws Exception{
        if(args.length < 1){
            System.out.println("No se agrego ningun argumento");
            System.exit(1);
        } else if(args[0].length() < 5 || !args[0].substring(args[0].length() - 4).equals(".txt")) {
            System.out.println("El argumento que se ingreso no es un archivo");
	        System.exit(3);
        } 
        this.reader = new RandomAccessFile(args[0], "r");
        this.matrizTransicion = new MatrizDeTransicionEstados();
        this.matrizAcciones = new MatrizDeAS(this);
        this.erroresLexicos = new ArrayList<ErrorLexico>();
        this.buffer = "";
        this.lineaArchivo = 1;
        this.ultimoCararterLeido = ' ';
        tablaSimbolos = new Tabla();
        tablaPalabrasReservadas = new TablaPalabrasReservadas();
    }

    public Token getToken() throws Exception {
        int caracter = -1,viejoEstado=0, nuevoEstado=0;
        this.lineaArchivo = this.getLineaArchivo();
        this.buffer = "";
        while (nuevoEstado != -1 && nuevoEstado != -2) {
            caracter = reader.read();
            this.ultimoCararterLeido = (char) caracter;
            viejoEstado = nuevoEstado;
            matrizAcciones.ejecutarAccionSemantica(viejoEstado,caracter,this.buffer);
            nuevoEstado = matrizTransicion.getProximoEstado(viejoEstado, caracter);
        }
        Token token = retornarToken(viejoEstado,caracter);
        if (nuevoEstado == -1){
            this.addErroresLexicos(new ErrorLexico("Error a la hora de crear " + token,this.lineaArchivo));

        }
        //chequeo si es una palabra reservada, un identificador o una constante, si no es ninguna buscar el token en retornarToken
        if (token.getTipo() == TokenType.PalabraReservada){
            if(!this.tablaPalabrasReservadas.existeSimbolo(this.buffer)){
                this.erroresLexicos.add(new ErrorLexico("No existe la palabra reservada: " + this.buffer,this.lineaArchivo));
            }
        } else if (token.getTipo() == TokenType.Identificador || token.getTipo() == TokenType.UInt || 
                    token.getTipo() == TokenType.Long || token.getTipo() == TokenType.Double || token.getTipo() == TokenType.Cadena) {
            if(!this.tablaSimbolos.existeSimbolo(this.buffer)){
                this.tablaSimbolos.agregarSimbolo(this.buffer,token);
                token = new LexemToken(token.getTipo(),buffer);
            }
        }
        return token;
    }

    public void MostrarErrores() throws Exception{
        if (this.erroresLexicos.size()>0){
            for (ErrorLexico error : this.erroresLexicos) {
                System.out.println(error.getMensaje() + " en la linea: " + error.getLinea());
            }
            //throw new Exception("Se han encontrado los errores anteriores");
        }
    }

    public void sumarCaracter(){
        this.buffer += this.ultimoCararterLeido;
    }

    public boolean devolverCaracter(){
        boolean devolver = true;
        try{
            this.reader.seek(this.reader.getFilePointer() - 1);
        } catch(IOException e) {
            ErrorLexico error = new ErrorLexico("Error al retroceder en el archivo", this.lineaArchivo);
            this.erroresLexicos.add(error);
            devolver = false;
        }
        return devolver;
    }

    public void setBuffer(String newBuffer){
        this.buffer = newBuffer;
    }

    public String getBuffer(){
        return this.buffer;
    }

    public void sumarSaltoLinea(){
        this.lineaArchivo = this.lineaArchivo + 1;
    }

    public int getLineaArchivo(){
        return this.lineaArchivo;
    }

    public void addErroresLexicos(ErrorLexico error){
        this.erroresLexicos.add(error);
    }

    public ArrayList<ErrorLexico> getErroresLexicos(){
        ArrayList<ErrorLexico> aux = new ArrayList<ErrorLexico>();
        aux.addAll(this.erroresLexicos);
        return aux;
    }

    public void MostrarTablaSimbolos(){
        this.tablaSimbolos.imprimirTabla();
    }

    private Token retornarToken(int estado, int c) throws Exception{
        Token token = new Token();
        if (c == -1 && this.buffer.length() == 0){
            token.setTipo(TokenType.Fin);
        }else{
            char caracter = (char) c;
            switch(estado){
                case 0:
                    switch(caracter){
                        case '+':
                            token.setTipo(TokenType.OperadorMas);
                            break;
                        case '/':
                            token.setTipo(TokenType.OperadorDividido);
                            break;
                        case '{':
                            token.setTipo(TokenType.LlaveIzquierda);
                            break;
                        case '}':
                            token.setTipo(TokenType.LlaveDerecha);
                            break;
                        case '(':
                            token.setTipo(TokenType.ParentesisIzquierdo);
                            break;
                        case ')':
                            token.setTipo(TokenType.ParentesisDerecho);
                            break;
                        case ',':
                            token.setTipo(TokenType.Coma);
                            break;
                        case ';':
                            token.setTipo(TokenType.PuntoComa);
                            break;
                        default:
                            throw new Exception("No se reconoce el caracter: " + caracter);
                    }
                    break;
                case 1:
                    token.setTipo(TokenType.Identificador);
                    break;
                case 2:
                    token.setTipo(TokenType.PalabraReservada);
                    break;
                case 3:
                    token.setTipo(TokenType.ErrorConstante);
                    break;
                case 4:
                    if(caracter == 'l')
                        token.setTipo(TokenType.Long);
                    else
                        token.setTipo(TokenType.ErrorConstante);
                    break;
                case 5:
                    if(caracter == 'i')
                        token.setTipo(TokenType.UInt);
                    else
                        token.setTipo(TokenType.ErrorUInt);
                    break;
                case 6:
                    token.setTipo(TokenType.Punto);
                    break;
                case 7:
                    token.setTipo(TokenType.Double);
                    break;
                case 8:
                case 9:
                    token.setTipo(TokenType.ErrorPuntoFlotante);
                    break;
                case 10:
                    token.setTipo(TokenType.Double);
                    break;
                case 11:
                    if(caracter == '=')
                        token.setTipo(TokenType.Igual);
                    else
                        token.setTipo(TokenType.Asignacion);
                    break;
                case 12:
                    if(caracter == '=')
                        token.setTipo(TokenType.MayorIgual);
                    else
                        token.setTipo(TokenType.Mayor);
                    break;
                case 13:
                    if(caracter == '=')
                        token.setTipo(TokenType.MenorIgual);
                    else
                        token.setTipo(TokenType.Menor);
                    break;
                case 14:
                    if(caracter == '!')
                        token.setTipo(TokenType.Distinto);
                    else
                        token.setTipo(TokenType.ErrorDistinto);
                    break;
                case 15:
                    if(caracter == '-')
                        token.setTipo(TokenType.OperadorMenosInmediato);
                    else
                        token.setTipo(TokenType.OperadorMenos);
                    break;
                case 16:
                    if(caracter == '%')
                        token.setTipo(TokenType.Cadena);
                    else
                        token.setTipo(TokenType.ErrorCadena);
                    break;
                case 17:
                    if(caracter != '{')
                        token.setTipo(TokenType.OperadorPor);
                    break;
                case 18:
                    break;
                case 19:
                    break;
                default:
                    throw new Exception("Token no encontrado");
            }
        }
        return token;
    }
}