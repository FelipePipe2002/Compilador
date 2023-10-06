import Lexico.*;

public class Main {
    public static void main(String[] args) throws Exception{
        AnalizadorLexico analizador = new AnalizadorLexico(args);
        Token token = analizador.getToken();
        while (token.getTipo().getNumero() != TokenType.Fin.getNumero()){
            System.out.println(token);       
            token = analizador.getToken();
        }
        analizador.MostrarErrores();
        analizador.MostrarTablaSimbolos();
    }
}