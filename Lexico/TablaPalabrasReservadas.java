package Lexico;
public class TablaPalabrasReservadas extends Tabla{

    TablaPalabrasReservadas(){
        super();
        this.agregarSimbolo("IF", new Token(TokenType.IF));
        this.agregarSimbolo("ELSE", new Token(TokenType.ELSE));
        this.agregarSimbolo("END_IF", new Token(TokenType.ENDIF));
        this.agregarSimbolo("PRINT", new Token(TokenType.PRINT));
        this.agregarSimbolo("CLASS", new Token(TokenType.CLASS));
        this.agregarSimbolo("VOID", new Token(TokenType.LONG));
        this.agregarSimbolo("LONG", new Token(TokenType.LONG));
        this.agregarSimbolo("UINT", new Token(TokenType.UINT));
        this.agregarSimbolo("DOUBLE", new Token(TokenType.DOUBLE));
        this.agregarSimbolo("WHILE", new Token(TokenType.WHILE));
        this.agregarSimbolo("DO", new Token(TokenType.DO));
        this.agregarSimbolo("INTERFACE", new Token(TokenType.INTERFACE));
        this.agregarSimbolo("IMPLEMENT", new Token(TokenType.IMPLEMENT));
    }
}