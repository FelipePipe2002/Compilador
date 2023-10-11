package Lexico;

public class Token{
    private TokenType tipo;
    
    public Token(TokenType tipo) {
        this.tipo = tipo;
    }

    public Token(){
        this.tipo = TokenType.Error;
    }

    public void setTipo(TokenType tipo){
        this.tipo = tipo;
    }

    public TokenType getTipo(){
        return this.tipo;
    }

    @Override
    public String toString() {
        return tipo.toString();
    }

}
