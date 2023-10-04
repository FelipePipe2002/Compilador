package AccionesSemanticas;
import java.util.Vector;

public class ErroresLexicos {
    private static Vector<String> erroresLexicos;

    public ErroresLexicos(){
        ErroresLexicos.erroresLexicos = new Vector<String>();
    }

    public static void addError(String error){
        ErroresLexicos.erroresLexicos.add(error);
    }

    public static void MostrarErrores(){
        for (String string : erroresLexicos) {
            System.out.println(string);
        }
    }
}
