package Lexico;
public enum TokenType {
    Identificador(1),
    OperadorMas(2),
    OperadorMenos(3),
    OperadorPor(4),
    OperadorDividido(5),
    OperadorMenosInmediato(6),
    Asignacion(7),
    Menor(8),
    MenorIgual(9),
    Mayor(10),
    MayorIgual(11),
    Igual(12),
    Distinto(13),
    LlaveIzquierda(14),
    LlaveDerecha(15),
    Coma(16),
    PuntoComa(17),
    Punto(18),
    IF(19),
    ELSE(20),
    ENDIF(21),
    PRINT(22),
    CLASS(23),
    VOID(24),
    LONG(25),
    UINT(26),
    DOUBLE(27),
    WHILE(28),
    DO(29),
    INTERFACE(30),
    IMPLEMENT(31),
    Cadena(32),
    ParentesisIzquierdo(33),
    ParentesisDerecho(34),
    PalabraReservada(35),
    Long(36),
    UInt(3),
    Double(36),
    Fin(-1),
    Error(50),
    ErrorConstante(51),
    ErrorPuntoFlotante(52),
    ErrorUInt(53),
    ErrorDistinto(54),
    ErrorCadena(55);

    private final int numero;

    TokenType(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }
}
