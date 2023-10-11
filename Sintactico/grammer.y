%{

package Sintacico;

import Lexico.*;

%}

%token IF ELSE END_IF PRINT CLASS VOID LONG UINT DOUBLE WHILE DO INTERFACE IMPLEMENT RETURN TOD ID CTE_LONG CTE_UINT CTE_DOUBLE CADENA

// Precedencia 
%left '+' '-'
%left '*' '/'

%%

programa    :   '{' lista_declaraciones '}'
            ;

lista_declaraciones :   declaracion
                    |   lista_declaraciones declaracion
                    ;

declaracion :   sentencia
            |   declaracion_clase
            |   declaracion_funcion
            ;

declaracion_clase   :   CLASS ID '{' cuerpo_clase '}'
                    |   CLASS ID IMPLEMENT ID '{' cuerpo_clase '}'
                    |   INTERFACE ID '{' cuerpo_interfaz '}' 
                    ;

cuerpo_clase    : miembro_clase
                | cuerpo_clase miembro_clase
                ;

miembro_clase   :   declaracion_variable
                |   declaracion_funcion
                ;
                               
cuerpo_interfaz : cuerpo_interfaz declaracion_funcion
                | declaracion_funcion
                ;

declaracion_variable:   tipo lista_de_id ','
                    ;

declaracion_funcion :   VOID ID '(' parametro_formal ')' '{' lista_sentencias '}' ','
                    |   VOID ID '(' ')' '{' lista_sentencias '}' ','
                    ;

parametro_formal    :   tipo ID 
                    ;

lista_sentencias    :   sentencia
                    |   lista_sentencias sentencia
                    ;

sentencia   :   sentencia_expresion
            |   sentencia_seleccion
            |   sentencia_iteracion
            |   sentencia_imprimir
            |   sentencia_retorno
            ;

sentencia_imprimir  :    PRINT CADENA ','
                    ;

sentencia_seleccion :   IF '(' comparacion ')' cuerpo_if END_IF ','
                    |   IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF ','
                    ;

cuerpo_if   :   sentencia 
            |   '{' lista_sentencias '}' 
            ;

comparacion : operacion comparador operacion;

comparador  :   '<'
            |   '>'
            |   '<='
            |   '>='
            |   '=='
            |   '!!'
            ;
           
sentencia_iteracion :   DO '{' lista_sentencias '}' WHILE '(' comparacion ')'
                    ;

sentencia_retorno   :   RETURN ','
                    ;

sentencia_expresion   :   declaracion_variable
            |   asignacion
            |   llamado_clase '(' operacion ')' ','
            |   TOD '(' operacion ')' ','
            ;

llamado_clase   :   llamado_clase '.' ID
                |   ID
                ;

asignacion  :   lista_de_id '=' operacion ','
            ;

lista_de_id :   lista_de_id ';' llamado_clase
            |   llamado_clase
            ;

operacion   :   operacion '+' termino
            |   operacion '-' termino
            |   termino
            ;

          
termino :   termino '*' termino_inmediato
        |   termino '/' termino_inmediato
        |   termino_inmediato
        ;

termino_inmediato   :   factor '--' 
                    |   factor
                    ;

factor  :    llamado_clase
        |    CTE_DOUBLE
        |    '-' CTE_DOUBLE
        |    CTE_UINT
        |    CTE_LONG
        |    '-' CTE_LONG
        ;

tipo    :   DOUBLE 
        |   UINT 
        |   LONG 
        |   ID
        ;

%%

private AnalizadorLexico analizadorLexico;

 int yylex() {
	Token token=analizadorLexico.getToken();

	if (token!=null){
	    yylval = new ParserVal(token);
	    return token.getTipo().getNumero();
	}

	return 0;
}

private void yyerror(String string) {
	System.out.println(string);
}