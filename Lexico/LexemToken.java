package Lexico;
public class LexemToken extends Token implements Comparable<LexemToken>{
    
    String lexema;

    public LexemToken(TokenType tipo, String lexema) {
        super(tipo);
        this.lexema = lexema;
    }

    public String getLexema(){
        return lexema;
    }

    @Override
    public String toString() {
        return getTipo().getRepresentacion() + " " + lexema.toString();
    }

    @Override
    public int compareTo(LexemToken other) {
        // Compara por el atributo 'lexema' primero
        int result = this.lexema.compareTo(other.lexema);
        
        // Si los lexemas son iguales, compara por el tipo de token
        if (result == 0) {
            // Asumiendo que TokenType es una enum que es comparable
            result = this.getTipo().compareTo(other.getTipo());
        }

        return result;
    }

}
