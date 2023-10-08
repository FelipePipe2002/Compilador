package Lexico;
public enum TokenType {
    Identificador(1,"identificador"),
    OperadorMas(2,"+"),
    OperadorMenos(3,"-"),
    OperadorPor(4,"*"),
    OperadorDividido(5,"/"),
    OperadorMenosInmediato(6,"--"),
    Asignacion(7,"="),
    Menor(8,"<"),
    MenorIgual(9,"<="),
    Mayor(10,">"),
    MayorIgual(11,">="),
    Igual(12,"=="),
    Distinto(13,"!!"),
    LlaveIzquierda(14,"{"),
    LlaveDerecha(1,"}"),
    Coma(16,","),
    PuntoComa(17,";"),
    Punto(18,"."),
    IF(19,"IF"),
    ELSE(20,"ELSE"),
    ENDIF(21,"END_IF"),
    PRINT(22,"PRINT"),
    CLASS(23,"CLASS"),
    VOID(24,"VOID"),
    LONG(25,"LONG"),
    UINT(26,"UINT"),
    DOUBLE(27,"DOUBLE"),
    WHILE(28,"WHILE"),
    DO(29,"DO"),
    INTERFACE(30,"INTERFACE"),
    IMPLEMENT(31,"IMPLEMENT"),
    Cadena(32,"Cadena"),
    ParentesisIzquierdo(33,"("),
    ParentesisDerecho(34,")"),
    PalabraReservada(35,"PALABRA RESERVADA"),
    Long(36,"Long"),
    UInt(3,"Uint"),
    Double(36,"Double"),
    Fin(-1,"Fin"),
    Error(50,"Error"),
    ErrorConstante(51,"Error Constante"),
    ErrorPuntoFlotante(52, "Error Punto Flotante"),
    ErrorUInt(53, "Error Uint"),
    ErrorDistinto(54, "Error Distinto"),
    ErrorCadena(55, "Error Cadena");

    private final int numero;
    private final String representacion;

    TokenType(int numero,String representacion) {
        this.numero = numero;
        this.representacion = representacion;
    }

    public int getNumero() {
        return this.numero;
    }

    public String getRepresentacion() {
        return this.representacion;
    }

    @Override
    public String toString() {
        return this.representacion;
    }
}
