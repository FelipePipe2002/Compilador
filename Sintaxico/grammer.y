%{
    import package
}%

%token IF ELSE END_IF PRINT CLASS VOID LONG UINT DOUBLE WHILE DO INTERFACE IMPLEMENT TOD

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
                    |   INTERFACE ID '{' cuerpo_interfaz '}' 
                    |   CLASS ID IMPLEMENT ID '{' cuerpo_clase '}'
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

declaracion_variable:   TIPO lista_de_id ','
                    ;

declaracion_funcion :   VOID ID '(' parametro_formal ')' '{' lista_sentencias '}' ','
                    ;

parametro_formal    :   TIPO ID 
                    |   ''
                    ;

lista_sentencias    :   sentencia
                    |   lista_sentencias sentencia
                    ;

sentencia   :   sentencia_expresion
            |   sentencia_seleccion
            |   sentencia_iteracion
            |   sentencia_retorno
            |   sentencia_imprimir
            |   ''
            ;

sentencia_imprimir  :    PRINT cadena ','
                    ;

sentencia_expresion :   expresion ','
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
           
sentencia_iteracion :   DO lista_sentencias WHILE '(' comparacion ')' ','
                    ;

sentencia_retorno   :   RETURN ','
                    ;

expresion   :   declaracion_variable
            |   asignacion
            |   llamado_clase '(' operacion ')'
            ;

llamado_clase   :   llamado_clase '.' ID;
                |   ID
                ;

asignacion  :   lista_de_id '=' operacion 
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

termino_inmediato   :   factor '-''-' 
                    |   factor
                    ;

factor  :    llamado_clase
        |    CTE
        ;

TIPO    :   DOUBLE 
        |   UINT 
        |   LONG 
        |   ID
        ;

%%

