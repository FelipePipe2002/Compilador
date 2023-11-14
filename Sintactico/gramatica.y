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
         | '{' lista_declaraciones {
                analizadorLex.addErroresLexicos(new Error("El programa tiene que terminar con \'}\'", analizadorLex.getLineaArchivo()));
         }
         | lista_declaraciones '}' {
                analizadorLex.addErroresLexicos(new Error("El programa tiene que arrancar con \'{\'", analizadorLex.getLineaArchivo()));
         }
         | lista_declaraciones {
                analizadorLex.addErroresLexicos(new Error("El programa tiene que estar contenido en \'{\' \'}\'", analizadorLex.getLineaArchivo()));
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
                                        analizadorLex.addErroresLexicos(new Error("No esta declarada la clase " + $2.sval.substring(0,$2.sval.lastIndexOf(":")), analizadorLex.getLineaArchivo())); 
                                }
                        }
                    } //chequear que el padre sea una clase
                    | encabezado_clase IMPLEMENT ID bloque_clase ',' {
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if (!tablaSimbolos.agregarInterfaz($1.sval,$3.sval + ":main")){
                                analizadorLex.addErroresLexicos(new Error("No se encuentra la interfaz " + $3.sval, analizadorLex.getLineaArchivo())); 
                        }
                        if(($4.sval != null)){
                                if((!tablaSimbolos.agregarHerencia($1.sval,$4.sval))){
                                        analizadorLex.addErroresLexicos(new Error("No esta declarada la clase " + $4.sval.substring(0,$4.sval.lastIndexOf(":")), analizadorLex.getLineaArchivo())); 
                                }
                        }
                    } //chequear que el padre sea una clase y que el ID interfaz sea una interfaz
                    | encabezado_interface bloque_interfaz ',' {
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_clase bloque_clase {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_clase IMPLEMENT ID bloque_clase {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_interface bloque_interfaz {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    ;

encabezado_clase : CLASS ID {
                        if(!tablaSimbolos.agregarAmbito($2.sval,ambito,$1.sval)){
                                analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
                        }
                        $$.sval = $2.sval + ambito;
                        ambito += ":" + $2.sval;
                 }
                 ;

encabezado_interface : INTERFACE ID {
                        if(!tablaSimbolos.agregarAmbito($2.sval,ambito,$1.sval)){
                                analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
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
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                }
                | '{' '}'  {
                        analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));
                }
                | '(' ')'  {
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                }
                ;

bloque_interfaz : '{' cuerpo_interfaz '}'  
                | '(' cuerpo_interfaz ')' {
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                }
                | '{' '}'  {
                        analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));
                }
                | '(' ')'  {
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
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
                        analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));
                }
                | cuerpo_interfaz declaracion_funcion {
                        analizadorLex.addErroresLexicos(new Error("Solo se permiten metodos abstractos", analizadorLex.getLineaArchivo()));
                }
                ;

declaracion_funcion_interfaz : encabezado_funcion '(' ')' ','{
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion '(' parametro_formal ')'',' {
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion '(' ')' {
                                analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion '(' parametro_formal ')' {
                                analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion ','    {
                                analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion parametro_formal ','  {
                                analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion    {
                                analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion parametro_formal {
                                analizadorLex.addErroresLexicos(new Error("El parametro formal tiene que estar entre \'(\' \')\'", analizadorLex.getLineaArchivo()));
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             ;

declaracion_variable : tipo lista_de_id ','
                     | tipo lista_de_id {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                     }
                     ;

declaracion_funcion : encabezado_funcion '(' ')' bloque_sentencias_funcion ',' {
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_funcion '(' parametro_formal ')' bloque_sentencias_funcion ',' {
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_funcion '(' ')' bloque_sentencias_funcion {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_funcion '(' parametro_formal ')' bloque_sentencias_funcion {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\' antes o", analizadorLex.getLineaArchivo()));
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    ;

encabezado_funcion : VOID ID {
                        if(!tablaSimbolos.agregarAmbito($2.sval,ambito,$1.sval)){
                                analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
                        }
                        ambito += ":" + $2.sval;
                   }
                   ;

parametro_formal : tipo ID {
                        if(!tablaSimbolos.agregarAmbito($2.sval,ambito,tipo)){
                                analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
                        }
                 }
                 ;

bloque_sentencias_funcion : '{' lista_sentencias_funcion '}'  
                  | '(' lista_sentencias_funcion ')' {
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                  }
                  | '{' '}' {
                        analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));
                  }
                  | '(' ')' {
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
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
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                  }
                  ;

sentencia_imprimir : PRINT CADENA ','  
                   | PRINT CADENA {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                   }
                   | PRINT error ',' {
                        analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));
                   }
                   | PRINT error {
                        analizadorLex.addErroresLexicos(new Error("Cadena mal definida", analizadorLex.getLineaArchivo()));
                   }
                   | error CADENA ',' {
                        analizadorLex.addErroresLexicos(new Error("No existe esa expresion para imprimir la cadena", analizadorLex.getLineaArchivo()));
                   }
                   ;

sentencia_seleccion : IF '(' comparacion ')' cuerpo_if END_IF ','  
                    | IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF ','
                    | IF '(' comparacion ')' cuerpo_if END_IF {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                    }
                    | IF '(' comparacion ')' cuerpo_if ELSE cuerpo_if END_IF {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                    }
                    | IF '(' ')' cuerpo_if END_IF ',' {
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));
                    }
                    | IF '(' ')' cuerpo_if ELSE cuerpo_if END_IF ',' {
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));
                    }
                    | IF '(' ')' cuerpo_if END_IF {
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF", analizadorLex.getLineaArchivo()));
                    }
                    | IF '(' ')' cuerpo_if ELSE cuerpo_if END_IF {
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion en el IF ELSE", analizadorLex.getLineaArchivo()));
                    }
                    ;

cuerpo_if : sentencia
          | bloque_sentencias
          ;

bloque_sentencias : '{' lista_sentencias '}'  
                  | '(' lista_sentencias ')' {
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                  }
                  | '{' '}'  {
                        analizadorLex.addErroresLexicos(new Error("Bloque sin instrucciones", analizadorLex.getLineaArchivo()));
                  }
                  | '(' ')'  {
                        analizadorLex.addErroresLexicos(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", analizadorLex.getLineaArchivo()));
                  }
                  ;

lista_sentencias : sentencia
                 | lista_sentencias sentencia
                 ;

comparacion : operacion comparador operacion
            | operacion comparador {
                analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));
            }
            | comparador operacion {
                analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));
            }
            | comparador {
                analizadorLex.addErroresLexicos(new Error("La comparacion tiene que estar compuesta por: Lado1, Comparador, Lado2", analizadorLex.getLineaArchivo()));
            }
            ;

comparador : '<'
           | '>'
           | '<='
           | '>='
           | '=='
           | '!!'
           | error {
                analizadorLex.addErroresLexicos(new Error("Comparacion mal definida", analizadorLex.getLineaArchivo()));
           }
           ;
           
sentencia_iteracion : DO bloque_sentencias WHILE '(' comparacion ')' ','
                    | DO bloque_sentencias WHILE '(' comparacion ')'    {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                    }
                    | DO bloque_sentencias WHILE '(' ')' "," {
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte", analizadorLex.getLineaArchivo()));
                    }
                    | DO bloque_sentencias WHILE '(' ')'{
                        analizadorLex.addErroresLexicos(new Error("No se declaro una condicion de corte y falta una \',\'", analizadorLex.getLineaArchivo()));
                    }
                    ;

sentencia_expresion : declaracion_variable
                    | factor_inmediato
                    | asignacion 
                    | llamado_clase '(' ')' ',' 
                    | llamado_clase '(' ')' {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                    }
                    | llamado_clase '(' operacion ')' ','  // Chequear tipo operacion con parametro de funcion?
                    | llamado_clase '(' operacion ')' {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
                    }
                    | TOD '(' operacion ')' ','
                    | TOD '(' operacion ')' {
                        analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
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
                        analizadorLex.addErroresLexicos(new Error("No se declaro la variable " + $1.sval + "en el ambito reconocible", analizadorLex.getLineaArchivo()));
                }
                polaca.add($1.sval);polaca.add("=");
           } //chequear tipos entre llamado de clase y operacion
           | llamado_clase '=' operacion {
                analizadorLex.addErroresLexicos(new Error("Se esperaba una \',\'", analizadorLex.getLineaArchivo()));
           }
           | tipo llamado_clase '=' operacion ',' {
                analizadorLex.addErroresLexicos(new Error("No se puede declarar y asignar en la misma línea", analizadorLex.getLineaArchivo()));
           }                                        
           ;


lista_de_id : ID {
                if(!tablaSimbolos.agregarAmbito($1.sval,ambito,tipo)){
                        analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
                }
            }
            | lista_de_id ';' ID {
                if(!tablaSimbolos.agregarAmbito($3.sval,ambito,tipo)){
                        analizadorLex.addErroresLexicos(new Error("Identificador ya usado en este ambito", analizadorLex.getLineaArchivo()));
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
                analizadorLex.convertirNegativo($2.sval);
                polaca.add("-" + $2.sval);
             }
             | CTE_DOUBLE {
                polaca.add($1.sval);
             }
             | '-' CTE_LONG {
                analizadorLex.convertirNegativo($2.sval);
                polaca.add("-" + $2.sval);
             }
             | CTE_LONG {
                if(CheckRangoLong($1.sval)){
                        analizadorLex.addErroresLexicos(new Error("LONG fuera de rango", analizadorLex.getLineaArchivo()));}
                else {
                        polaca.add($1.sval);
                } 
             }
             | CTE_UINT {
                polaca.add($1.sval);
             }
             | '-' CTE_UINT {
                analizadorLex.addErroresLexicos(new Error("Las variables tipo UINT no pueden ser negativas", analizadorLex.getLineaArchivo()));
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
        analizadorLex.addErroresLexicos(new Error("Tipo no reconocido", analizadorLex.getLineaArchivo()));
     }
     ;

%%


static AnalizadorLexico analizadorLex = null;
static Parser par = null;
static Token token = null;
static ArrayList<String>  polaca;
static String ambito = ":main";
static String tipo = "";
static Tabla tablaSimbolos;

public static void main(String[] args) throws Exception{
        System.out.println("Iniciando compilacion...");
        tablaSimbolos = new Tabla();
        analizadorLex = new AnalizadorLexico(args,tablaSimbolos);
        
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