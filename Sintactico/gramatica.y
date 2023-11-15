%{

package Sintactico;
import Lexico.*;
import Errores.Error;
import java.util.ArrayList;

%}

%token IF ELSE END_IF PRINT CLASS VOID LONG UINT DOUBLE WHILE DO INTERFACE IMPLEMENT RETURN TOD ID CTE_LONG CTE_UINT CTE_DOUBLE CADENA 
%start programa

%%
programa : '{' lista_declaraciones '}' 
         | '{' lista_declaraciones {
                errores.add(new Error("El programa tiene que terminar con \'}\'", anLex.getLinea()));
         }
         | lista_declaraciones '}' {
                errores.add(new Error("El programa tiene que arrancar con \'{\'", anLex.getLinea()));
         }
         | lista_declaraciones {
                errores.add(new Error("El programa tiene que estar contenido en \'{\' \'}\'", anLex.getLinea()));
        }
         ;

lista_declaraciones : declaracion
                    | lista_declaraciones declaracion
                    ;

declaracion : declaracion_variable
            | declaracion_clase
            | declaracion_funcion
            ;

declaracion_clase   : encabezado_clase bloque_clase ',' {
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if(($2.sval != null)){
                                if(!tablaSimbolos.agregarHerencia($1.sval,$2.sval)){
                                        errores.add(new Error("No esta declarada la clase " + $2.sval.substring(0,$2.sval.lastIndexOf(":")), anLex.getLinea())); 
                                }
                        }
                    } //chequear que el padre sea una clase
                    | encabezado_clase IMPLEMENT ID bloque_clase ',' {
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if (!tablaSimbolos.agregarInterfaz($1.sval,$3.sval + ":main")){
                                errores.add(new Error("No se encuentra la interfaz " + $3.sval, anLex.getLinea())); 
                        }
                        if(($4.sval != null)){
                                if((!tablaSimbolos.agregarHerencia($1.sval,$4.sval))){
                                        errores.add(new Error("No esta declarada la clase " + $4.sval.substring(0,$4.sval.lastIndexOf(":")), anLex.getLinea())); 
                                }
                        }
                    } //chequear que el padre sea una clase y que el ID interfaz sea una interfaz
                    | encabezado_interface bloque_interfaz ',' {
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_clase bloque_clase {
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_clase IMPLEMENT ID bloque_clase {
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_interface bloque_interfaz {
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    ;

encabezado_clase : CLASS ID {
                        if(!tablaSimbolos.agregarAmbito($2.sval,ambito,$1.sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        $$.sval = $2.sval + ambito;
                        ambito += ":" + $2.sval;
                 }
                 ;

encabezado_interface : INTERFACE ID {
                        if(!tablaSimbolos.agregarAmbito($2.sval,ambito,$1.sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        ambito += ":" + $2.sval;
                     }
                     ;

bloque_clase :    '{' cuerpo_clase '}' {
                        $$.sval = null;
                }
                | '{' cuerpo_clase ID ',' '}' {
                        $$.sval = $3.sval + ":main";
                }//chequeo de ID clase
                | '(' cuerpo_clase ')' {
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
                | '{' '}'  {
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                }
                | '(' ')'  {
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
                ;

bloque_interfaz : '{' cuerpo_interfaz '}'  
                | '(' cuerpo_interfaz ')' {
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
                | '{' '}'  {
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                }
                | '(' ')'  {
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                }
                ;

cuerpo_clase : miembro_clase
             | cuerpo_clase miembro_clase
             ;

miembro_clase : declaracion_variable
              | declaracion_funcion
              ;
                               
cuerpo_interfaz : declaracion_funcion_interfaz
                | cuerpo_interfaz declaracion_funcion_interfaz
                | declaracion_funcion {
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
                | cuerpo_interfaz declaracion_funcion {
                        errores.add(new Error("Solo se permiten metodos abstractos", anLex.getLinea()));
                }
                ;

declaracion_funcion_interfaz : encabezado_funcion '(' ')' ','{
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion '(' parametro_formal ')'',' {
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion '(' ')' {
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion '(' parametro_formal ')' {
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion ','    {
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion parametro_formal ','  {
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion    {
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion parametro_formal {
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             ;

declaracion_variable : tipo lista_de_id ','
                     | tipo lista_de_id {
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                     }
                     ;

declaracion_funcion : encabezado_funcion '(' ')' bloque_sentencias_funcion ',' {
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_funcion '(' parametro_formal ')' bloque_sentencias_funcion ',' {
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_funcion '(' ')' bloque_sentencias_funcion {
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_funcion '(' parametro_formal ')' bloque_sentencias_funcion {
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    ;

encabezado_funcion : VOID ID {
                        if(!tablaSimbolos.agregarAmbito($2.sval,ambito,$1.sval)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                        ambito += ":" + $2.sval;
                   }
                   ;

parametro_formal : tipo ID {
                        if(!tablaSimbolos.agregarAmbito($2.sval,ambito,tipo)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        }
                 }
                 ;

bloque_sentencias_funcion : '{' lista_sentencias_funcion '}'  
                  | '(' lista_sentencias_funcion ')' {
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
                  | '{' '}' {
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
                  | '(' ')' {
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
                  ;

lista_sentencias_funcion : sentencia_funcion
                         | lista_sentencias_funcion sentencia_funcion
                         ;


sentencia_funcion : declaracion_funcion 
                  | sentencia
                  ;

sentencia : sentencia_expresion
          | sentencia_seleccion 
          | sentencia_iteracion 
          | sentencia_imprimir 
          | sentencia_retorno 
          ;

sentencia_retorno : RETURN ','  
                  | RETURN {
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                  }
                  ;

sentencia_imprimir : PRINT CADENA ','  
                   | PRINT CADENA {
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                   }
                   | PRINT error ',' {
                        errores.add(new Error("Cadena mal definida", anLex.getLinea()));
                   }
                   | PRINT error {
                        errores.add(new Error("Cadena mal definida", anLex.getLinea()));
                   }
                   | error CADENA ',' {
                        errores.add(new Error("No existe esa expresion para imprimir la cadena", anLex.getLinea()));
                   }
                   ;

sentencia_seleccion : IF '(' comparacion ')' cuerpo_if END_IF ','  
                    | IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF ','
                    | IF '(' comparacion ')' cuerpo_if END_IF {
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
                    | IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF {
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
                    | IF '(' ')' cuerpo_if END_IF ',' {
                        errores.add(new Error("No se declaro una condicion en el IF", anLex.getLinea()));
                    }
                    | IF '(' ')' cuerpo_if ELSE cuerpo_if END_IF ',' {
                        errores.add(new Error("No se declaro una condicion en el IF ELSE", anLex.getLinea()));
                    }
                    | IF '(' ')' cuerpo_if END_IF {
                        errores.add(new Error("No se declaro una condicion en el IF", anLex.getLinea()));
                    }
                    | IF '(' ')' cuerpo_if ELSE cuerpo_if END_IF {
                        errores.add(new Error("No se declaro una condicion en el IF ELSE", anLex.getLinea()));
                    }
                    ;

cuerpo_if : sentencia
          | bloque_sentencias
          ;

bloque_sentencias : '{' lista_sentencias '}'  
                  | '(' lista_sentencias ')' {
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
                  | '{' '}'  {
                        errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
                  }
                  | '(' ')'  {
                        errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
                  }
                  ;

lista_sentencias : sentencia
                 | lista_sentencias sentencia
                 ;

comparacion : operacion comparador operacion
            | operacion comparador {
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
            | comparador operacion {
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
            | comparador {
                errores.add(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", anLex.getLinea()));
            }
            ;

comparador : '<'
           | '>'
           | '<='
           | '>='
           | '=='
           | '!!'
           | error {
                errores.add(new Error("Comparacion mal definida", anLex.getLinea()));
           }
           ;
           
sentencia_iteracion : DO bloque_sentencias WHILE '(' comparacion ')' ','
                    | DO bloque_sentencias WHILE '(' comparacion ')'    {
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
                    | DO bloque_sentencias WHILE '(' ')' "," {
                        errores.add(new Error("No se declaro una condicion de corte", anLex.getLinea()));
                    }
                    | DO bloque_sentencias WHILE '(' ')'{
                        errores.add(new Error("No se declaro una condicion de corte y falta una \',\'", anLex.getLinea()));
                    }
                    ;

sentencia_expresion : declaracion_variable
                    | factor_inmediato
                    | asignacion 
                    | llamado_clase '(' ')' ',' 
                    | llamado_clase '(' ')' {
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
                    | llamado_clase '(' operacion ')' ','  // Chequear tipo operacion con parametro de funcion?
                    | llamado_clase '(' operacion ')' {
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
                    | TOD '(' operacion ')' ','
                    | TOD '(' operacion ')' {
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
                    ;

llamado_clase : ID {
                $$.sval = $1.sval; 
              }
              | llamado_clase '.' ID {
                $$.sval += "." + $3.sval;
              }
              ;

asignacion : llamado_clase '=' operacion ','  {
                if(!tablaSimbolos.existeVariable($1.sval,ambito)){
                        errores.add(new Error("No se declaro la variable " + $1.sval + "en el ambito reconocible", anLex.getLinea()));
                }
                polaca.add($1.sval);polaca.add("=");
           } //chequear tipos entre llamado de clase y operacion
           | llamado_clase '=' operacion {
                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
           }
           | tipo llamado_clase '=' operacion ',' {
                errores.add(new Error("No se puede declarar y asignar en la misma línea", anLex.getLinea()));
           }                                        
           ;


lista_de_id : ID {
                if(!tablaSimbolos.agregarAmbito($1.sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
            }
            | lista_de_id ';' ID {
                if(!tablaSimbolos.agregarAmbito($3.sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
            }
            ;

operacion : termino
          | operacion '+' termino {
                polaca.add("+");
          } //chequear tipos entre el operando y el termino
          | operacion '-' termino {
                polaca.add("-");
          } //chequear tipos entre el operando y el termino
          ;
          
termino : factor
        | termino '*' factor {
                polaca.add("*");
        } //chequear tipos entre el termino y el factor
        | termino '/' factor {
                polaca.add("/");
        } //chequear tipos entre el termino y el factor
        | '(' operacion ')'
        ;

factor : factor_comun
       | factor_inmediato
       ;

factor_inmediato : llamado_clase '--' {
                        polaca.add($1.sval);polaca.add("1");polaca.add("-");{polaca.add($1.sval);polaca.add("=");}
                 } //el operador inmediato es para todos los tipos? chequear
                 ;

factor_comun : llamado_clase {
                polaca.add($1.sval);
             }
             | '-' CTE_DOUBLE {
                anLex.convertirNegativo($2.sval);
                polaca.add("-" + $2.sval);
             }
             | CTE_DOUBLE {
                polaca.add($1.sval);
             }
             | '-' CTE_LONG {
                anLex.convertirNegativo($2.sval);
                polaca.add("-" + $2.sval);
             }
             | CTE_LONG {
                if(CheckRangoLong($1.sval)){
                        errores.add(new Error("LONG fuera de rango", anLex.getLinea()));}
                else {
                        polaca.add($1.sval);
                } 
             }
             | CTE_UINT {
                polaca.add($1.sval);
             }
             | '-' CTE_UINT {
                errores.add(new Error("Las variables tipo UINT no pueden ser negativas", anLex.getLinea()));
             }
             ;

tipo : DOUBLE {
        tipo = "DOUBLE";
     }
     | UINT {
        tipo = "UINT";
     }
     | LONG {
        tipo = "LONG";
     }
     | ID
     | error {
        errores.add(new Error("Tipo no reconocido", anLex.getLinea()));
     }
     ;

%%


static AnalizadorLexico anLex = null;
static Parser par = null;
static Token token = null;
static ArrayList<String>  polaca;
static ArrayList<Error> errores;
static String ambito = ":main";
static String tipo = "";
static Tabla tablaSimbolos;

public static void main(String[] args) throws Exception{
        System.out.println("Iniciando compilacion...");
        tablaSimbolos = new Tabla();
        errores = new ArrayList<Error>();
        anLex = new AnalizadorLexico(args,tablaSimbolos,errores);
        
        polaca = new ArrayList<String>();
        
        par = new Parser(false);
        par.run();

        anLex.MostrarTablaSimbolos();
        anLex.MostrarErrores();
        
        for (int i = 0; i < polaca.size(); i++) {
          System.out.println(i + " " + polaca.get(i));
        }

        System.out.println("Fin de la compilacion");
}

private int yylex(){
        try {
          token = anLex.getToken();
          int intToken = token.getTipo().getNumero();
          //System.out.println("yylex: " + token);
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