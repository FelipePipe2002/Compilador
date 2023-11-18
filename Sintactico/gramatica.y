%{

package Sintactico;
import Lexico.*;
import Errores.Error;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

%}

%token IF ELSE END_IF PRINT CLASS VOID LONG UINT DOUBLE WHILE DO INTERFACE IMPLEMENT RETURN TOD ID CTE_LONG CTE_UINT CTE_DOUBLE CADENA 
%start programa


%%
programa : '{' componentes_programa '}' 
         | '{' componentes_programa {
                errores.add(new Error("El programa tiene que terminar con \'}\'", anLex.getLinea()));
         }
         | componentes_programa '}' {
                errores.add(new Error("El programa tiene que arrancar con \'{\'", anLex.getLinea()));
         }
         | componentes_programa {
                errores.add(new Error("El programa tiene que estar contenido en \'{\' \'}\'", anLex.getLinea()));
        }
         ;

componentes_programa : declaracion
                     | sentencia
                     | componentes_programa sentencia
                     | componentes_programa declaracion
                     ;

declaracion : declaracion_clase
            | declaracion_funcion
            ;

declaracion_clase : encabezado_clase bloque_clase ',' {
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if(($2.sval != null)){
                                if(!tablaSimbolos.agregarHerencia($1.sval,$2.sval)){
                                        errores.add(new Error("No esta declarada la clase '" + $2.sval.substring(0,$2.sval.lastIndexOf(":")), anLex.getLinea() + "'")); 
                                } else {
                                    if (tablaSimbolos.getNivelHerencia($1.sval) >= MAXNIVELHERENCIA ) {
                                        errores.add(new Error("La clase '" + $1.sval.substring(0,$1.sval.indexOf(":")) + "' no puede heredar, debido a que la clase padre '" + tablaSimbolos.getPadreClase(tablaSimbolos.getPadreClase($1.sval)).substring(0,$1.sval.indexOf(":")) + "' ya posee el maximo nivel de herencia permitido (" + MAXNIVELHERENCIA + ")", "ERROR"));
                                    }
                                    ArrayList<String> aux = new ArrayList<>();
                                    aux = tablaSimbolos.metodoSobreescriptos($1.sval);
                                    if (aux.size() > 0) {
                                        errores.add(new Error("La clase " + $1.sval.substring(0,$1.sval.indexOf(":")) + " esta sobreescribiendo el/los metodo/s:" + aux.toString() + " de su clase padre", anLex.getLinea()));
                                    }
                                }
                        }
                  } //chequear que el padre sea una clase
                  | encabezado_clase IMPLEMENT ID bloque_clase ',' {
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                        if (!tablaSimbolos.agregarInterfaz($1.sval,$3.sval + ":main")){
                            errores.add(new Error("No se encuentra la interfaz " + $3.sval, anLex.getLinea())); 
                        } else {
                            if (!tablaSimbolos.implementaMetodosInterfaz(ambito + ":" + $1.sval.substring(0,$1.sval.indexOf(":")),ambito + ":" + $3.sval)){
                                errores.add(new Error("La clase '" + $1.sval.substring(0,$1.sval.indexOf(":")) + "' no implementa todos los metodos de la interfaz '" + $3.sval + "'", anLex.getLinea()));
                            }
                        }
                        if(($4.sval != null)){
                                if((!tablaSimbolos.agregarHerencia($1.sval,$4.sval))){
                                        errores.add(new Error("No esta declarada la clase '" + $4.sval.substring(0,$4.sval.lastIndexOf(":")), anLex.getLinea() + "'")); 
                                }
                                ArrayList<String> aux = new ArrayList<>();
                                aux = tablaSimbolos.metodoSobreescriptos($1.sval);
                                if (aux.size() > 0) {
                                    errores.add(new Error("La clase " + $1.sval.substring(0,$1.sval.indexOf(":")) + " esta sobreescribiendo el/los metodo/s:" + aux.toString() + " de su clase padre", anLex.getLinea()));
                                }
                        }
                  } //chequear que el padre sea una clase y que el ID interfaz sea una interfaz
                  | encabezado_interface bloque_interfaz ',' {
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
                  | encabezado_clase bloque_clase ';'{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
                  | encabezado_clase IMPLEMENT ID bloque_clase ';'{
                      errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                      ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                  }
                  | encabezado_interface bloque_interfaz ';'{
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

bloque_clase : '{' cuerpo_clase '}' {
                $$.sval = null;
             }
             | '{' cuerpo_clase herencia_clase '}' {
                $$.sval = $3.sval + ":main";
             }//chequeo de ID clase
             | '(' cuerpo_clase ')'{
                errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
             }
             | '{' '}' {
                errores.add(new Error("Bloque sin instrucciones", anLex.getLinea()));
             }
             | '(' ')' {
                errores.add(new Error("Los delimitadores estan mal utilizados, se esperaba una \'{\'", anLex.getLinea()));
             }
             ;

herencia_clase : ID ','

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
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion '(' parametro_formal ')'',' {
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion '(' ')' ';'{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion '(' parametro_formal ')' ';'{
                                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion parametro_formal ','  {
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion ';'{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             | encabezado_funcion parametro_formal ';'{
                                errores.add(new Error("El parametro formal tiene que estar entre \'(\' \')\'", anLex.getLinea()));
                                metodosPolaca.remove(ambito);
                                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                             }
                             ;

declaracion_variable : tipo lista_de_id ',' {
                        tablaSimbolos.checkAtributosDeClase($2.sval,ambito);
                     }
                     ;

declaracion_funcion : encabezado_funcion '(' ')' bloque_sentencias_funcion ',' {
                        metodosPolaca.get(ambito).add("RET");
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_funcion '(' parametro_formal ')' bloque_sentencias_funcion ',' {
                        metodosPolaca.get(ambito).add("RET");
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_funcion '(' ')' bloque_sentencias_funcion {
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        metodosPolaca.remove(ambito);
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    | encabezado_funcion '(' parametro_formal ')' bloque_sentencias_funcion {
                        errores.add(new Error("Se esperaba una \',\' antes o", anLex.getLinea()));
                        metodosPolaca.remove(ambito);
                        ambito = ambito.substring(0,ambito.lastIndexOf(":"));
                    }
                    ;

encabezado_funcion : VOID ID {
                        if(!tablaSimbolos.agregarAmbito($2.sval,ambito,$1.sval)){
                            errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        } else {
                            if (tablaSimbolos.addNivelVoid($2.sval,ambito)) {
                                if (tablaSimbolos.getProfundidadVoid($2.sval,ambito) > MAXPROFUNDIDADVOID) {
                                    errores.add(new Error("Se supero el nivel de anidamiento (" + MAXPROFUNDIDADVOID + ") de los metodos", anLex.getLinea()));
                                }
                            }
                        }
                        ambito += ":" + $2.sval;
                        ArrayList<String> polacas = new ArrayList<String>();
                        metodosPolaca.put(ambito,polacas);
                   }
                   ;

parametro_formal : tipo ID {
                        if(!tablaSimbolos.agregarAmbito($2.sval,ambito,tipo)){
                                errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                        } else {
                            //cambiar funcion a con parametro ambito.subString(lastIndexOf(":"),ambito.length())
                            tablaSimbolos.setParametro(ambito);
                            metodosPolaca.get(ambito).add($2.sval);
                            metodosPolaca.get(ambito).add("=");
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

sentencia_retorno : RETURN ',' { metodosPolaca.get(ambito).add("RETURN,");} 
                  | RETURN ';'{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                  }
                  ;

sentencia_imprimir : PRINT CADENA ','{
                        metodosPolaca.get(ambito).add($2.sval);
                        metodosPolaca.get(ambito).add("PRINT");
                   }
                   | PRINT CADENA ';'{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                   }
                   ;

sentencia_seleccion : condicion_if cuerpo_then END_IF ',' {
                        metodosPolaca.get(ambito).add(pila.pop(),"[" + String.valueOf(metodosPolaca.get(ambito).size() + 1) + "]");
                        metodosPolaca.get(ambito).add("L" + "[" + String.valueOf(metodosPolaca.get(ambito).size()) + "]");
                    }
                    | condicion_if cuerpo_then cuerpo_else END_IF ',' {
                        metodosPolaca.get(ambito).add(pila.pop(),"[" + String.valueOf(metodosPolaca.get(ambito).size() + 1) + "]");
                        metodosPolaca.get(ambito).add("L" + "[" + String.valueOf(metodosPolaca.get(ambito).size()) + "]");
                    }
                    | condicion_if cuerpo_then END_IF ';' {
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
                    | condicion_if cuerpo_then cuerpo_else END_IF ';' {
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
                    | condicion_if cuerpo_then ',' {
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
                    | condicion_if cuerpo_then cuerpo_else ',' {
                        errores.add(new Error("Se esperaba un END_IF y se encontro una \',\'", anLex.getLinea()));
                    }
                    ;

condicion_if: IF '(' comparacion ')' {
                pila.push(metodosPolaca.get(ambito).size());
                metodosPolaca.get(ambito).add("BF"); //bifurcacion por falso;
            }
            | IF '(' ')' {
                errores.add(new Error("Falta declarar condicion del IF ubicado", anLex.getLinea()));
            }

cuerpo_then : sentencia {
                metodosPolaca.get(ambito).add(pila.pop(), "[" + String.valueOf(metodosPolaca.get(ambito).size() + 3) + "]");
                pila.push(metodosPolaca.get(ambito).size());
                metodosPolaca.get(ambito).add("BI"); //bifurcacion incondicional;
                metodosPolaca.get(ambito).add("L[" + String.valueOf(metodosPolaca.get(ambito).size() + 1) + "]");
            }
            | bloque_sentencias {
                metodosPolaca.get(ambito).add(pila.pop(), "[" + String.valueOf(metodosPolaca.get(ambito).size() + 3) + "]");
                pila.push(metodosPolaca.get(ambito).size());
                metodosPolaca.get(ambito).add("BI"); //bifurcacion incondicional;
                metodosPolaca.get(ambito).add("L[" + String.valueOf(metodosPolaca.get(ambito).size() + 1) + "]");
            }
            ;

cuerpo_else : ELSE sentencia
            | ELSE bloque_sentencias
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

comparacion : operacion comparador operacion {
                metodosPolaca.get(ambito).add($2.sval);
            }
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

comparador : '<' {
                $$.sval = $1.sval;
           }
           | '>' {
                $$.sval = $1.sval;
           }
           | '<=' {
                $$.sval = $1.sval;
           }
           | '>=' {
                $$.sval = $1.sval;
           }
           | '==' {
                $$.sval = $1.sval;
           }
           | '!!'{
                $$.sval = $1.sval;
           }
           | error {
                errores.add(new Error("Comparacion mal definida", anLex.getLinea()));
           }
           ;
           
sentencia_iteracion : inicio_do bloque_sentencias WHILE '(' comparacion ')' ',' {
                        metodosPolaca.get(ambito).add("[" + String.valueOf(pila.pop()) + "]");
                        metodosPolaca.get(ambito).add("BF");
                    }
                    | inicio_do bloque_sentencias WHILE '(' comparacion ')' ';'{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
                    | inicio_do bloque_sentencias WHILE '(' ')' ','{
                        errores.add(new Error("No se declaro una condicion de corte en el WHILE que se ubica", anLex.getLinea()));
                    }
                    ;

inicio_do : DO {
                pila.push(metodosPolaca.get(ambito).size());
                metodosPolaca.get(ambito).add("L[" + String.valueOf(metodosPolaca.get(ambito).size()) + "]");
             }
             ;

sentencia_expresion : declaracion_variable
                    | factor_inmediato
                    | asignacion 
                    | llamado_clase '(' ')' ',' {
                        if(!tablaSimbolos.existeMetodo($1.sval,ambito,false)){
                                errores.add(new Error("No se encuentra declarado el metodo " + $1.sval + " dentro del ambito reconocible", anLex.getLinea()));
                        } else {
                            String ambitoClase = tablaSimbolos.getAmbitoMetodoInvocado($1.sval,ambito);
                            metodosPolaca.get(ambito).add("Call " + ambitoClase);
                        }
                    }
                    | llamado_clase '(' operacion ')' ','  {// Chequear tipo operacion con parametro de funcion
                        if(!tablaSimbolos.existeMetodo($1.sval,ambito,true)){
                                errores.add(new Error("No se encuentra declarado el metodo " + $1.sval + " dentro del ambito reconocible", anLex.getLinea()));
                        } else {
                            String ambitoClase = tablaSimbolos.getAmbitoMetodoInvocado($1.sval,ambito);
                            metodosPolaca.get(ambito).add("Call " + ambitoClase);
                        }
                    }
                    | llamado_clase '(' ')' ';'{
                        errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
                    }
                    | llamado_clase '(' operacion ')' ';'{
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
                        errores.add(new Error("No se declaro la variable " + $1.sval + " en el ambito reconocible", anLex.getLinea()));
                } else {

                        //tablaSimbolos.agregarSimbolo($1.sval + ambito,new Token());
                        tablaSimbolos.setUso($1.sval,ambito,true);
                }
                metodosPolaca.get(ambito).add($1.sval);metodosPolaca.get(ambito).add("=");
           } //chequear tipos entre llamado de clase y operacion
           | llamado_clase '=' operacion ';'{
                errores.add(new Error("Se esperaba una \',\'", anLex.getLinea()));
           }
           | tipo llamado_clase '=' operacion ',' {
                errores.add(new Error("No se puede declarar y asignar en la misma línea", anLex.getLinea()));
           }                                        
           ;


lista_de_id : lista_de_id ';' ID {
                if(!tablaSimbolos.agregarAmbito($3.sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
                $$.sval = $1.sval + $2.sval + $3.sval;
            }
            | ID {
                if(!tablaSimbolos.agregarAmbito($1.sval,ambito,tipo)){
                        errores.add(new Error("Identificador ya usado en este ambito", anLex.getLinea()));
                }
                $$.sval = $1.sval;
            }
            ;

operacion : termino
          | operacion '+' termino {
                metodosPolaca.get(ambito).add("+");
          } //chequear tipos entre el operando y el termino
          | operacion '-' termino {
                metodosPolaca.get(ambito).add("-");
          } //chequear tipos entre el operando y el termino
          ;
          
termino : factor
        | termino '*' factor {
                metodosPolaca.get(ambito).add("*");
        } //chequear tipos entre el termino y el factor
        | termino '/' factor {
                metodosPolaca.get(ambito).add("/");
        } //chequear tipos entre el termino y el factor
        ;

factor : factor_comun
       | factor_inmediato
       | TOD '(' llamado_clase ')' {
                if(!tablaSimbolos.existeVariable($3.sval,ambito)){
                        errores.add(new Error("No se declaro la variable " + $3.sval + " en el ambito reconocible", anLex.getLinea()));
                }
                metodosPolaca.get(ambito).add($3.sval);
                metodosPolaca.get(ambito).add("TOD");
       }
       ;

factor_inmediato : llamado_clase '--' {
                        if(!tablaSimbolos.existeVariable($1.sval,ambito)){
                                errores.add(new Error("No se declaro la variable " + $1.sval + " en el ambito reconocible", anLex.getLinea()));
                        }
                        metodosPolaca.get(ambito).add($1.sval);metodosPolaca.get(ambito).add("1");metodosPolaca.get(ambito).add("-");{metodosPolaca.get(ambito).add($1.sval);metodosPolaca.get(ambito).add("=");}
                 } //el operador inmediato es para todos los tipos? chequear
                 ;

factor_comun : llamado_clase {
                if(!tablaSimbolos.existeVariable($1.sval,ambito)){
                        errores.add(new Error("No se declaro la variable " + $1.sval + " en el ambito reconocible", anLex.getLinea()));
                }
                metodosPolaca.get(ambito).add($1.sval);
             }
             | '-' CTE_DOUBLE {
                anLex.convertirNegativo($2.sval);
                tablaSimbolos.eliminarSimbolo("-" + $2.sval);
                metodosPolaca.get(ambito).add("-" + $2.sval);
             }
             | CTE_DOUBLE {
                metodosPolaca.get(ambito).add($1.sval);
             }
             | '-' CTE_LONG {
                anLex.convertirNegativo($2.sval);
                tablaSimbolos.eliminarSimbolo($2.sval);
                metodosPolaca.get(ambito).add("-" + $2.sval);
             }
             | CTE_LONG {
                if(CheckRangoLong($1.sval)){
                        errores.add(new Error("LONG fuera de rango", anLex.getLinea()));}
                else {
                        metodosPolaca.get(ambito).add($1.sval);
                } 
             }
             | CTE_UINT {
                metodosPolaca.get(ambito).add($1.sval);
             }
             | '-' CTE_UINT {
                errores.add(new Error("Las constantes tipo UINT no pueden ser negativas", anLex.getLinea()));
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
     | ID {
        if(!tablaSimbolos.existeClase($1.sval,ambito)){
                errores.add(new Error("No se declaro la variable " + $1.sval + " en el ambito reconocible", anLex.getLinea()));
        }
        tipo = $1.sval;
        tablaSimbolos.eliminarSimbolo($1.sval);
     }
     ;

%%

static final int MAXNIVELHERENCIA = 3;
static final int MAXPROFUNDIDADVOID = 1;
static AnalizadorLexico anLex = null;
static Parser par = null;
static Token token = null;
static HashMap<String, ArrayList<String>> metodosPolaca; // :main -> polacaMain | :main:asd -> polacaAsd
static ArrayList<String>  polaca;
static ArrayList<Error> errores;
static String ambito = ":main";
static String tipo = "";
static Tabla tablaSimbolos;
static Deque<Integer> pila; //Utiliza metodos push,pop y peek

public static void main(String[] args) throws Exception{
        System.out.println("Iniciando compilacion...");
        tablaSimbolos = new Tabla();
        errores = new ArrayList<Error>();
        anLex = new AnalizadorLexico(args,tablaSimbolos,errores);
        
        metodosPolaca = new HashMap<String, ArrayList<String>>();
        polaca = new ArrayList<String>();
        metodosPolaca.put(":main",polaca);
        pila = new LinkedList<>();
        par = new Parser(false);
        par.run();

        tablaSimbolos.imprimirTabla();
        for (String variable : tablaSimbolos.variablesNoUsadas()){
            if(variable.contains(":")){
                    errores.add(new Error("La variable " + variable.substring(0,variable.indexOf(":")) + " no fue asignada dentro del ambito de " + variable.substring(variable.lastIndexOf(":") + 1, variable.length()),"WARNING"));
            }
        }
        if (errores.size()>0){
            for (Error error : errores) {
                System.out.println(error.toString());
            }
            //throw new Exception("Se han encontrado los errores anteriores");
        }
        //System.out.println(polaca.toString());
        
        
        /* for (int i = 0; i < polaca.size(); i++) {
          System.out.println(i + " " + polaca.get(i));
        } */

        for (String key : metodosPolaca.keySet()) {
            System.out.println(key + " = " + metodosPolaca.get(key).toString());
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