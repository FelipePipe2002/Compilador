import java.io.FileReader;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length < 1){
            System.out.println("No se agrego ningun argumento");
            System.exit(1);
        } else if(args[0].length() < 5 || !args[0].substring(args[0].length() - 4).equals(".txt")) {
            System.out.println("El argumento que se ingreso no es un archivo");
	        System.exit(3);
        }   


        FileReader reader = new FileReader(args[0]);
        Token token = analizadorLexico(reader);
        while (token.tipo != TokenType.Fin){
        }
        reader.close();
    }

    public static Token analizadorLexico(FileReader reader) throws IOException{
        MatrizDeTransicionEstados matrizTransicion = new MatrizDeTransicionEstados();
        MatrizDeAS matrizAcciones = new MatrizDeAS();
        int character, linea = 1; //porque int caracter?
        String pide = "";
        int estado = 0;
        Token token = new Token();
        while ((character = reader.read()) != -1) {
                if((char)character != ' ' || (char)character != '\t'){
                    pide += (char)character;
                    if ((char) character == '\n') //le podemos dar la responsabilidad a la accion semantica 8?
                        linea++;
                    matrizTransicion.getEstado(estado,(char)character);
                } else {
                    pide = "";
                }
        }
        //retornar el token pertinente
        return token;
    }
}

