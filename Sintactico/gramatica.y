%{

package Sintactico;
import Lexico.*;
import Lexico.Error;
import java.util.ArrayList;

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

declaracion_clase   : CLASS ID bloque_clase  {System.out.println("CREACION DE CLASE" + ", linea: " + analizadorLex.getLineaArchivo());}
                    | CLASS ID IMPLEMENT ID bloque_clase  {System.out.println("CREACION DE CLASE CON HERENCIA" + ", linea: " + analizadorLex.getLineaArchivo());}
                    | INTERFACE ID '{' cuerpo_interfaz '}'  {System.out.println("CREACION DE INTERFAZ" + ", linea: " + analizadorLex.getLineaArchivo());}
                    | INTERFACE ID '{' '}'  {analizadorLex.addErroresLexicos(new Error("Error interfaz vacia", analizadorLex.getLineaArchivo()));}
                    ;

bloque_clase :      '{' cuerpo_clase '}'  
                  | '(' cuerpo_clase ')' {analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
                  | '{' '}'  {analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));}
                  | '(' ')'  {analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));}
                  ;

cuerpo_clase : miembro_clase
             | cuerpo_clase miembro_clase
             ;

miembro_clase : declaracion_variable
              | declaracion_funcion
              ;
                               
cuerpo_interfaz : declaracion_funcion_vacia
                | cuerpo_interfaz declaracion_funcion_vacia
                | declaracion_funcion  {analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));}
                | cuerpo_interfaz declaracion_funcion {analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));}
                ;

declaracion_funcion_vacia : VOID ID '(' ')' ','   {System.out.println("DECLARACION DE FUNCION VACIA SIN PARAMETROS" + ", linea: " + analizadorLex.getLineaArchivo());}
                    | VOID ID '(' parametro_formal ')'','  {System.out.println("DECLARACION DE FUNCION VACIA" + ", linea: " + analizadorLex.getLineaArchivo());}
                    | VOID ID '(' ')' {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    | VOID ID '(' parametro_formal ')' {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    | VOID ID ','    {analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
                    | VOID ID parametro_formal ','  {analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
                    | VOID ID    {analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
                    | VOID ID parametro_formal {analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));}
                    ;

declaracion_variable : tipo lista_de_id ','  {System.out.println("DECLARACION DE VARIABLE" + ", linea: " + analizadorLex.getLineaArchivo());}
                     | tipo lista_de_id {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                     ;

declaracion_funcion : VOID ID '(' ')' bloque_sentencias ','   {System.out.println("DECLARACION DE FUNCION SIN PARAMETROS" + ", linea: " + analizadorLex.getLineaArchivo());}
                    | VOID ID '(' parametro_formal ')' bloque_sentencias ','  {System.out.println("DECLARACION DE FUNCION" + ", linea: " + analizadorLex.getLineaArchivo());}
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
          | sentencia_seleccion {System.out.println("SENTENCIA IF" + ", linea: " + analizadorLex.getLineaArchivo());}
          | sentencia_iteracion {System.out.println("SENTENCIA ITERACION" + ", linea: " + analizadorLex.getLineaArchivo());}
          | sentencia_imprimir {System.out.println("SENTENCIA IMPRESION DE CADENA" + ", linea: " + analizadorLex.getLineaArchivo());}
          | sentencia_retorno {System.out.println("SENTENCIA RETURN" + ", linea: " + analizadorLex.getLineaArchivo());}
          ;

sentencia_retorno : RETURN ','  
                  | RETURN      {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                  ;

sentencia_imprimir : PRINT CADENA ','  
                   | PRINT CADENA      {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                   | PRINT error ','      {analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
                   | PRINT error      {analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));}
                   | error CADENA ','      {analizadorLex.addErroresLexicos(new Error("No existe esa expresion para imprimir la cadena", analizadorLex.getLineaArchivo()));}
                   ;

sentencia_seleccion : IF '(' comparacion ')' cuerpo_if END_IF ','  
                    | IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF ','
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
           
sentencia_iteracion : DO bloque_sentencias WHILE '(' comparacion ')' ','
                    | DO bloque_sentencias WHILE '(' comparacion ')'    {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    | DO bloque_sentencias WHILE '(' ')' "," {analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte", analizadorLex.getLineaArchivo()));}
                    | DO bloque_sentencias WHILE '(' ')'{analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte y falta una \',\'", analizadorLex.getLineaArchivo()));}
                    ;

sentencia_expresion : declaracion_variable
                    | asignacion {System.out.println("ASIGNACION" + ", linea: " + analizadorLex.getLineaArchivo());}
                    | llamado_clase '(' ')' ',' {System.out.println("LLAMADO A METODO" + ", linea: " + analizadorLex.getLineaArchivo());}
                    | llamado_clase '(' ')' {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    | llamado_clase '(' operacion ')' ',' {System.out.println("LLAMADO A METODO" + ", linea: " + analizadorLex.getLineaArchivo());}
                    | llamado_clase '(' operacion ')' {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    | TOD '(' operacion ')' ','
                    | TOD '(' operacion ')' {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
                    ;

llamado_clase : ID {identClase = $1.sval + identClase; }
              | llamado_clase '.' ID {identClase += "." + $3.sval;}
              ;

asignacion : llamado_clase '=' operacion ','  {polaca.add(identClase); identClase = "";polaca.add("=");}
           | llamado_clase '=' operacion {analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));}
           | tipo llamado_clase '=' operacion ',' {analizadorLex.addErroresLexicos(new Error("No se puede declarar y asignar en la misma línea", analizadorLex.getLineaArchivo()));}                                        
           ;


lista_de_id : ID
            | lista_de_id ';' ID
            ;

operacion : termino
          | operacion '+' termino {polaca.add("+");}
          | operacion '-' termino {polaca.add("-");}
          ;
          
termino : factor
        | termino '*' factor {polaca.add("*");}
        | termino '/' factor {polaca.add("/");}
        | '(' operacion ')'
        ;

factor : factor_comun
       | factor_inmediato
       ;

factor_inmediato : factor_comun '--' {polaca.add("--");}

factor_comun : llamado_clase {polaca.add(identClase); identClase = "";}
             | '-' CTE_DOUBLE   {analizadorLex.convertirNegativo($2.sval);
polaca.add("-" + $2.sval);}
             | CTE_DOUBLE {polaca.add($1.sval);}
             | '-' CTE_LONG     {analizadorLex.convertirNegativo($2.sval);
polaca.add("-" + $2.sval);}
             | CTE_LONG {{if(CheckRangoLong($1.sval)){analizadorLex.addErroresLexicos(new Error("Long fuera de rango", analizadorLex.getLineaArchivo()));}
  else {
    polaca.add($1.sval);
  }}}
             | CTE_UINT {polaca.add($1.sval);}
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
static ArrayList<String>  polaca;
private String identClase = "";

public static void main(String[] args) throws Exception{
        System.out.println("Iniciando compilacion...");
        analizadorLex = new AnalizadorLexico(args);
        
        polaca = new ArrayList<String>();
        
        par = new Parser(false);
        par.run();

        analizadorLex.MostrarTablaSimbolos();
        analizadorLex.MostrarErrores();
        
        for (int i = 0; i < polaca.size(); i++) {
          System.out.println(i + " " + polaca.get(i));
        }

        System.out.println("Fin de la compilacion");
}

private int yylex(){
        try {
          token = analizadorLex.getToken();
          int intToken = token.getTipo().getNumero();
          System.out.println("yylex: " + token);
          yylval = new ParserVal(token.toString());
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