package Lexico;
import java.io.RandomAccessFile;

import Lexico.AccionesSemanticas.ContarSaltosLinea;

public class AnalizadorLexico{

    private static RandomAccessFile reader;
    private MatrizDeTransicionEstados matrizTransicion;
    private MatrizDeAS matrizAcciones;
    public static String buffer;

    public AnalizadorLexico(String[] args) throws Exception {
        if(args.length < 1){
            System.out.println("No se agrego ningun argumento");
            System.exit(1);
        } else if(args[0].length() < 5 || !args[0].substring(args[0].length() - 4).equals(".txt")) {
            System.out.println("El argumento que se ingreso no es un archivo");
	        System.exit(3);
        } 
        AnalizadorLexico.reader = new RandomAccessFile(args[0], "r");
        matrizTransicion = new MatrizDeTransicionEstados();
        matrizAcciones = new MatrizDeAS();
    }

    public Token getToken() throws Exception {
        int caracter,viejoEstado=0,nuevoEstado = 0;
        AnalizadorLexico.buffer= "";
        caracter = reader.read();
        while (caracter != -1) {
                char carac = (char) caracter;
                viejoEstado = nuevoEstado;
                matrizAcciones.ejecutarAccionSemantica(viejoEstado,carac);
                nuevoEstado = matrizTransicion.getProximoEstado(viejoEstado, carac);

                if (nuevoEstado == -1){
                    throw new Exception("Error a la hora de crear " + retornarCamino(viejoEstado,carac) + " en la linea " + ContarSaltosLinea.cantSaltos);
                } else if (nuevoEstado == -2){
                    System.out.println(AnalizadorLexico.buffer);
                    return retornarCamino(viejoEstado, carac);
                }
                
                caracter = reader.read();
        }
        nuevoEstado = matrizTransicion.getProximoEstado(viejoEstado, (char) caracter);
        if (AnalizadorLexico.buffer.length()>0){
            if (nuevoEstado == -2){
                System.out.println(AnalizadorLexico.buffer);
                return retornarCamino(viejoEstado, (char) caracter);
            } else{
                throw new Exception("Error a la hora de crear " + retornarCamino(viejoEstado,(char) caracter) + " en la linea " + ContarSaltosLinea.cantSaltos);
            }
        } else{
            return new Token(TokenType.Fin);
        }

        //me fijo si el ultimo buffer puede ser un token ejemplo Buffer contiene LONG pero el proximo caracter es -1, tendria que reconocerlo pero como
        //la condicion de corte del while es -1 no lo hace, entonces hago esto
    }

    public static void devolverCaracter() throws Exception{
        AnalizadorLexico.reader.seek(AnalizadorLexico.reader.getFilePointer() - 1);
    }

    public Token retornarCamino(int estado, char caracter) throws Exception{
        Token token = new Token();
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
                        throw new Exception("Caracter mal colocado: " + caracter);
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
                if(!String.valueOf(caracter).matches("[0-9]"))
                    token.setTipo(TokenType.Punto);
                break;
            case 7:
                if(String.valueOf(caracter).matches("[0-9]") || caracter == 'D' || caracter == 'd')
                    token.setTipo(TokenType.Float);
                break;
            case 8:
            case 9:
                token.setTipo(TokenType.ErrorPuntoFlotante);
                break;
            case 10:
                if(!String.valueOf(caracter).matches("[0-9]"))
                    token.setTipo(TokenType.Float);
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
                token.setTipo(TokenType.Cadena);
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
                throw new Exception("Estado no encontrado");
        }
        return token;
    }
}