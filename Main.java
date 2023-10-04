import java.io.RandomAccessFile;
import AccionesSemanticas.ApilarCaracter;
import AccionesSemanticas.ErroresLexicos;


public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length < 1){
            System.out.println("No se agrego ningun argumento");
            System.exit(1);
        } else if(args[0].length() < 5 || !args[0].substring(args[0].length() - 4).equals(".txt")) {
            System.out.println("El argumento que se ingreso no es un archivo");
	        System.exit(3);
        } 
        RandomAccessFile reader = new RandomAccessFile(args[0], "r");
        Token token = analizadorLexico(reader);
        while (token.getTipo().getNumero() != TokenType.Fin.getNumero()){
            System.out.println(token);         
            token = analizadorLexico(reader);
        }
        reader.close();
    }

    public static Token analizadorLexico(RandomAccessFile reader) throws Exception {

        MatrizDeTransicionEstados matrizTransicion = new MatrizDeTransicionEstados();
        MatrizDeAS matrizAcciones = new MatrizDeAS();
        int caracter;
        int viejoEstado,nuevoEstado = 0;
        String buffer= "";
        Token token = new Token();
        caracter = reader.read();
        while (caracter != -1) {
                char carac = (char) caracter;
                viejoEstado = nuevoEstado;
                nuevoEstado = matrizTransicion.getProximoEstado(viejoEstado, carac);
                if (nuevoEstado == -1){
                    ErroresLexicos.MostrarErrores();
                    throw new Exception("Error a la hora de crear " + retornarCamino(viejoEstado,carac));
                }

                //si se ejecuta la AS1 poner el carac vacio
                if((viejoEstado == 0 && nuevoEstado != 0) || viejoEstado !=0){
                    buffer += carac;
                }

                matrizAcciones.ejecutarAccionSemantica(viejoEstado,carac, buffer);
                
                if (nuevoEstado == -2){
                    if(ApilarCaracter.apilar){
                        reader.seek(reader.getFilePointer() -1);
                        ApilarCaracter.apilar = false;
                        buffer = buffer.substring(0, buffer.length()-1);
                    }
                    System.out.println(buffer + "|");
                    return retornarCamino(viejoEstado, carac);
                }

                caracter = reader.read();
        }


        //retornar el token pertinente
        token = new Token(TokenType.Fin);
        return token;
    }

    public static Token retornarCamino(int estado, char caracter) throws Exception{
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

