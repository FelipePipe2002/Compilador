package Lexico;
public class LexemToken extends Token{
    
    String lexema;

    public LexemToken(TokenType tipo, String lexema) {
        super(tipo);
        this.lexema = lexema;
    }

    @Override
    public String toString() {
        return getTipo().getRepresentacion() + " " + lexema.toString();
    }
}
