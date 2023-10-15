package Lexico;
public enum TokenType {
    PalabraReservada(35,"PALABRA RESERVADA"),

    ParentesisIzquierdo(40,"("),
    ParentesisDerecho(41,")"),
    OperadorPor(42,"*"),
    OperadorMas(43,"+"),
    Coma(44,","),
    OperadorMenos(45,"-"),
    Punto(46,"."),
    OperadorDividido(47,"/"),
    PuntoComa(59,";"),
    Menor(60,"<"),
    Asignacion(61,"="),
    Mayor(62,">"),
    LlaveIzquierda(123,"{"),
    LlaveDerecha(125,"}"),

    IF(257,"IF"),
    ELSE(258,"ELSE"),
    ENDIF(259,"END_IF"),
    PRINT(260,"PRINT"),
    CLASS(261,"CLASS"),
    VOID(262,"VOID"),
    LONG(263,"LONG"),
    UINT(264,"UINT"),
    DOUBLE(265,"DOUBLE"),
    WHILE(266,"WHILE"),
    DO(267,"DO"),
    INTERFACE(268,"INTERFACE"),
    IMPLEMENT(269,"IMPLEMENT"),
    RETURN(270,"RETURN"),
    TOD(271,"TOD"),
    Identificador(272,"identificador"),
    Long(273,"Long"),
    UInt(274,"Uint"),
    Double(275,"Double"),
    Cadena(276,"Cadena"),
    MenorIgual(277,"<="),
    MayorIgual(278,">="),
    Igual(279,"=="),
    Distinto(280,"!!"),
    OperadorMenosInmediato(281,"--"),
    
    Error(256,"Error"),
    ErrorConstante(256,"Error Constante"),
    ErrorPuntoFlotante(256, "Error Punto Flotante"),
    ErrorUInt(256, "Error Uint"),
    ErrorDistinto(256, "Error Distinto"),
    ErrorCadena(256, "Error Cadena"),
    ErrorPalabraReservada(256, "Error Palabra Reservada"),
    ErrorComentario(256, "Error Comentario"),
    Fin(0,"Fin");

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
