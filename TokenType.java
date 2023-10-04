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
    If(19),
    Else(20),
    EndIf(21),
    Print(22),
    Class(23),
    Void(24),
    Long(25),
    UInt(26),
    Double(27),
    While(28),
    Do(29),
    Interface(30),
    Implement(31),
    Cadena(32),
    ParentesisIzquierdo(33),
    ParentesisDerecho(34),
    PalabraReservada(35),
    Float(36),
    Fin(-1),
    Error(50),
    ErrorConstante(51),
    ErrorPuntoFlotante(52),
    ErrorUInt(53),
    ErrorDistinto(54);

    private final int numero;

    TokenType(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }
}
