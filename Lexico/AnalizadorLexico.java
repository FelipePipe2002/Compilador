package Lexico;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayList;

import Errores.Error;
import Lexico.AccionesSemanticas.Comentario;

public class AnalizadorLexico{

    private RandomAccessFile reader;
    private MatrizDeTransicionEstados matrizTransicion;
    private MatrizDeAS matrizAcciones;
    private ArrayList<Error> erroresLexicos;
    private String buffer;
    private int lineaArchivo;
    private char ultimoCararterLeido;
    public Tabla tablaSimbolos;
    private TablaPalabrasReservadas tablaPalabrasReservadas;

    public AnalizadorLexico(String filename,Tabla tablasimbolos, ArrayList<Error> errores) throws Exception{
        this.reader = new RandomAccessFile(filename, "r");
        this.matrizTransicion = new MatrizDeTransicionEstados();
        this.matrizAcciones = new MatrizDeAS(this);
        this.erroresLexicos = errores;
        this.buffer = "";
        this.lineaArchivo = 1;
        this.ultimoCararterLeido = ' ';
        this.tablaSimbolos = tablasimbolos;
        tablaPalabrasReservadas = new TablaPalabrasReservadas();
    }

    public Token getToken() throws Exception {
        int caracter = -1,viejoEstado=0, nuevoEstado=0;
        this.lineaArchivo = this.getLinea();
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
            this.addErroresLexicos(new Error("Error a la hora de crear " + token,this.lineaArchivo));
        }



        //chequeo si es una palabra reservada, un identificador o una constante, si no es ninguna buscar el token en retornarToken
        if (token.getTipo() == TokenType.PalabraReservada){

            if(this.tablaPalabrasReservadas.existeSimbolo(this.buffer)){
                return this.tablaPalabrasReservadas.obtenerSimbolo(this.buffer);
            } else{
                this.erroresLexicos.add(new Error("No existe la palabra reservada: " + this.buffer,this.lineaArchivo));
                return new Token(TokenType.ErrorPalabraReservada);
            }

        } else if (token.getTipo() == TokenType.Identificador || token.getTipo() == TokenType.UInt || token.getTipo() == TokenType.Long 
        || token.getTipo() == TokenType.Double || token.getTipo() == TokenType.Cadena) {  

            if(token.getTipo() == TokenType.Long){
                this.buffer += "_l";
            } else if (token.getTipo() == TokenType.UInt){
                this.buffer += "_ui";
            } else if (token.getTipo() == TokenType.Cadena){
                this.buffer = '"' + this.buffer + '"';
            } else if (token.getTipo() == TokenType.Double){
                if (this.buffer.contains("d") || this.buffer.contains("D")) {
                    BigDecimal bufferValue = new BigDecimal(this.buffer.replaceAll("[dD]", "E"));
                    String numero = bufferValue.stripTrailingZeros().toPlainString();
                    if (!numero.contains(".")) {
                        this.buffer = "0." + numero.replaceAll("0+\\.?$", "") + "E" + numero.length();
                    } else {
                        if (numero.charAt(0) == '0'){
                            numero = numero.substring(numero.indexOf(".") + 1, numero.length());
                            int cantCeros = 1;
                            while (cantCeros < numero.length() && numero.charAt(cantCeros) == '0') {
                                cantCeros += 1;
                            }
                            this.buffer = "0." + numero.replaceFirst("^0+", "") + "E-" + cantCeros;
                        } else {
                            this.buffer = "0." + numero.replaceAll("0*$", "").replace(".", "") + "E" + numero.substring(0, numero.indexOf(".")).length();
                        }     
                    }
                } else {
                    BigDecimal bufferValue = new BigDecimal(this.buffer);
                    String numero = bufferValue.stripTrailingZeros().toPlainString();
                    if (numero.charAt(0) == '0' && !numero.equals("0")) {
                            int cantCeros = 0;
                            numero = numero.substring(2, numero.length());
                            while (numero.charAt(cantCeros) == '0') {
                                cantCeros += 1;  
                            }
                            if (cantCeros > 0)
                                this.buffer = "0." + numero.substring(cantCeros, numero.length()) + "E-" + cantCeros;
                            else
                                this.buffer = "0." + numero.substring(cantCeros, numero.length());
                    } else {
                        if (numero.contains(".")) {
                            this.buffer = "0." + numero.replaceAll("0*$", "").replace(".", "") + "E" + numero.substring(0, numero.indexOf(".")).length();
                        } else {
                            this.buffer = "0." + numero.replaceAll("0*$", "") + "E" + numero.length();
                        }
                    }
                }
            }
            if(!this.tablaSimbolos.existeSimbolo(this.buffer)){
                this.tablaSimbolos.agregarSimbolo(this.buffer,token);
            }
            token = new LexemToken(token.getTipo(),buffer);
        }

        return token;
    }

    public void sumarCaracter(){
        this.buffer += this.ultimoCararterLeido;
    }

    public boolean devolverCaracter(){
        boolean devolver = true;
        try{
            this.reader.seek(this.reader.getFilePointer() - 1);
        } catch(IOException e) {
            Error error = new Error("Error al retroceder en el archivo", this.lineaArchivo);
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

    public int getLinea(){
        return this.lineaArchivo;
    }

    public void addErroresLexicos(Error error){
        this.erroresLexicos.add(error);
    }

    public ArrayList<Error> getErroresLexicos(){
        ArrayList<Error> aux = new ArrayList<Error>();
        aux.addAll(this.erroresLexicos);
        return aux;
    }

    public void convertirNegativo(String numero){
        this.tablaSimbolos.convertirNegativo(numero);
    }

    private Token retornarToken(int estado, int c) throws Exception{
        Token token = new Token();
        if (c == -1 && this.buffer.length() == 0){
            if (Comentario.creandoComentario){
                token.setTipo(TokenType.ErrorComentario);
                Comentario.creandoComentario = false;
            } else 
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