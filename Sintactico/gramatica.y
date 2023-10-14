%{

package Sintactico;
import Lexico.*;
import Lexico.Error;

%}

%token IF ELSE END_IF PRINT CLASS VOID LONG UINT DOUBLE WHILE DO INTERFACE IMPLEMENT RETURN TOD ID CTE_LONG CTE_UINT CTE_DOUBLE CADENA 
%start programa

%%
programa : '{' lista_declaraciones '}'
         | '{' lista_declaraciones {analizadorLex.addErroresLexicos(new Error("El programa tiene que terminar con \'}\'", analizadorLex.getLineaArchivo()));}
         | lista_declaraciones '}' {analizadorLex.addErroresLexicos(new Error("El programa tiene que arrancar con \'{\'", analizadorLex.getLineaArchivo()));}
         | lista_declaraciones {analizadorLex.addErroresLexicos(new Error("El programa tiene que estar contenido en \'{\' \'}\'", analizadorLex.getLineaArchivo()));}
         ;

lista_declaraciones : declaracion
                    | lista_declaraciones declaracion
                    ;

declaracion : declaracion_variable
            | declaracion_clase
            | declaracion_funcion
            ;

declaracion_clase   : CLASS ID '{' cuerpo_clase '}'  {System.out.println("CREACION DE CLASE");}
                    | CLASS ID IMPLEMENT ID '{' cuerpo_clase '}'  {System.out.println("CREACION DE CLASE CON HERENCIA");}
                    | INTERFACE ID '{' cuerpo_interfaz '}'  {System.out.println("CREACION DE INTERFAZ");}
                    ;

cuerpo_clase : miembro_clase
             | cuerpo_clase miembro_clase
             ;

miembro_clase : declaracion_variable
              | declaracion_funcion
              ;
                               
cuerpo_interfaz : declaracion_funcion
                | cuerpo_interfaz declaracion_funcion
                ;

declaracion_variable : tipo lista_de_id ','  {System.out.println("DECLARACION DE VARIABLE");}
                     | tipo lista_de_id {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                     ;

declaracion_funcion : VOID ID '(' ')' bloque_sentencias ','   {System.out.println("DECLARACION DE FUNCION SIN PARAMETROS");}
                    | VOID ID '(' parametro_formal ')' bloque_sentencias ','  {System.out.println("DECLARACION DE FUNCION");}
                    | VOID ID '(' ')' bloque_sentencias   {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    | VOID ID '(' parametro_formal ')' bloque_sentencias  {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    ;

parametro_formal : tipo ID 
                 ;

lista_sentencias : sentencia
                 | lista_sentencias sentencia
                 ;

bloque_sentencias : '{' lista_sentencias '}'
                  | '(' lista_sentencias ')' {analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
                  | '{' '}'  {analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
                  | '(' ')'  {analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
                  ;

sentencia : sentencia_expresion
          | sentencia_seleccion
          | sentencia_iteracion
          | sentencia_imprimir
          | sentencia_retorno
          ;

sentencia_retorno : RETURN ','  {System.out.println("SENTENCIA RETURN");}
                  | RETURN      {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                  ;

sentencia_imprimir : PRINT CADENA ','  {System.out.println("SENTENCIA IMPRESION DE CADENA");}
                   | PRINT CADENA      {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                   | PRINT error ','      {analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
                   | PRINT error      {analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
                   | error CADENA ','      {analizadorLex.addErroresLexicos(new Error("No existe esa expresion para imprimir la cadena", analizadorLex.getLineaArchivo()));}
                   ;

sentencia_seleccion : IF '(' comparacion ')' cuerpo_if END_IF ','  {System.out.println("SENTENCIA IF");}
                    | IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF ','  {System.out.println("SENTENCIA IF ELSE");}
                    | IF '(' comparacion ')' cuerpo_if END_IF      {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    | IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF      {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    | IF '(' ')' cuerpo_if END_IF ','  {analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
                    | IF '(' ')' cuerpo_if ELSE cuerpo_if END_IF ','  {analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
                    | IF '(' ')' cuerpo_if END_IF      {analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));}
                    | IF '(' ')' cuerpo_if ELSE cuerpo_if END_IF      {analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));}
                    ;

cuerpo_if : sentencia 
          | bloque_sentencias
          ;

comparacion : operacion comparador operacion
            | operacion comparador {analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
            | comparador operacion {analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
            | comparador {analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));}
            ;

comparador : '<'
           | '>'
           | '<='
           | '>='
           | '=='
           | '!!'
           | error {analizadorLex.addErroresLexicos(new Error("Comparacion mal definida", analizadorLex.getLineaArchivo()));}
           ;
           
sentencia_iteracion : DO bloque_sentencias WHILE '(' comparacion ')' ','  {System.out.println("SENTENCIA ITERACION");}
                    | DO bloque_sentencias WHILE '(' comparacion ')'    {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    | DO bloque_sentencias WHILE '(' ')' "," {analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte", analizadorLex.getLineaArchivo()));}
                    | DO bloque_sentencias WHILE '(' ')'{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte y falta una \',\'", analizadorLex.getLineaArchivo()));}
                    ;

sentencia_expresion : declaracion_variable
                    | asignacion
                    | llamado_clase '(' ')' ','
                    | llamado_clase '(' ')' {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    | llamado_clase '(' operacion ')' ','
                    | llamado_clase '(' operacion ')' {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    | TOD '(' operacion ')' ','
                    | TOD '(' operacion ')' {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    ;

llamado_clase : ID
              | llamado_clase '.' ID
              ;

asignacion : lista_de_id '=' operacion ','  {System.out.println("ASIGNACION");}
           | lista_de_id '=' operacion {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
           | tipo lista_de_id '=' operacion ',' {analizadorLex.addErroresLexicos(new Error("No se puede declarar y asignar en la misma línea", analizadorLex.getLineaArchivo()));}
                                                        
           ;

lista_de_id : llamado_clase
            | lista_de_id ';' llamado_clase
            ;

operacion : termino
          | operacion '+' termino
          | operacion '-' termino
          ;
          
termino : factor
        | termino '*' factor
        | termino '/' factor
        | '(' operacion ')'
        ;

factor : factor_comun
       | factor_inmediato
       ;

factor_inmediato : factor_comun '--'

factor_comun : ID
             | '-' CTE_DOUBLE   {analizadorLex.convertirNegativo(((LexemToken)token).getLexema());}
             | CTE_DOUBLE 
             | '-' CTE_LONG     {analizadorLex.convertirNegativo(((LexemToken)token).getLexema());}
             | CTE_LONG {if(CheckRangoLong(((LexemToken)token).getLexema())){analizadorLex.addErroresLexicos(new Error("Long fuera de rango", analizadorLex.getLineaArchivo()));}}
             | CTE_UINT
             | '-' CTE_UINT {analizadorLex.addErroresLexicos(new Error("Las variables tipo UINT no pueden ser negativas", analizadorLex.getLineaArchivo()));}
             ;

tipo : DOUBLE 
     | UINT 
     | LONG
     | ID
     | error {analizadorLex.addErroresLexicos(new Error("Tipo no reconocido", analizadorLex.getLineaArchivo()));}
     ;

%%


static AnalizadorLexico analizadorLex = null;
static Parser par = null;
static Token token = null;

public static void main(String[] args) throws Exception{
        System.out.println("Iniciando compilacion...");
        analizadorLex = new AnalizadorLexico(args);

        par = new Parser(false);
        par.run();

        analizadorLex.MostrarTablaSimbolos();
        analizadorLex.MostrarErrores();

        System.out.println("Fin de la compilacion");
}

private int yylex(){
        try {
          token = analizadorLex.getToken();
          int intToken = token.getTipo().getNumero();
          System.out.println("El token obtenido es: " + token);
          System.out.println("El valor del token obtenido es: " + intToken);
          yylval = new ParserVal(token);
          return intToken;
        } catch (Exception e) {
          System.out.println("Error a la hora encontrar un token");
          return 0;
        }
}

private void yyerror(String s) {
        System.out.println(s);
}

private boolean CheckRangoLong(String numero){
        numero = numero.substring(0,numero.length()-2);
        Long bufferValue = Long.parseLong(numero);
        Long rango = 2147483647l;
        if(bufferValue>rango)
            return true;
        else
            return false;
}