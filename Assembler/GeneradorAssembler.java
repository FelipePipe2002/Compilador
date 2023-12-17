package Assembler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

import Errores.Error;
import Lexico.Tabla;
import Lexico.Token;
import Lexico.TokenType;

public class GeneradorAssembler {
    private HashMap<String, ArrayList<String>> metodosPolaca;
    private Tabla tablaSimbolos;
    private Deque<String> pila; //Utiliza metodos push,pop y peek
    private String data;
    private String codigo;
    private int aux;
    private String salto;
    private ArrayList<Error> errores;
    private final String aux2bytes = "@aux_2_bytes";
    
    public GeneradorAssembler( HashMap<String, ArrayList<String>> metodosPolaca, Tabla tablaSimbolos, ArrayList<Error> errores){
        this.metodosPolaca = metodosPolaca;
        this.tablaSimbolos = tablaSimbolos;
        this.pila = new LinkedList<>();
        this.data = ".386\n" +
        ".model flat, stdcall\n" +
        "option casemap :none\n" +
        "include \\masm32\\include\\windows.inc\n" +
        "include \\masm32\\include\\kernel32.inc\n" +
        "include \\masm32\\include\\user32.inc\n" +
        "include \\masm32\\include\\masm32.inc\n" +
        "includelib \\masm32\\lib\\kernel32.lib\n" +
        "includelib \\masm32\\lib\\user32.lib\n" +
        "includelib \\masm32\\lib\\masm32.lib\n" +
        ".data\n" +
        "@newline DB 10, 0\n" +
        aux2bytes + " DW ? \n" +
        //resultados negativos en restas de enteros sin signo
        "@ERROR_RESULT_NEGATIVO_UINT DB \"ERROR: resultado negativo al restar dos enteros sin signo\", 0\n" +
        //overflow para la suma de enteros
        "@ERROR_OVERFLOW_SUMA_LONG DB \"ERROR: se produjo overflow al sumar dos enteros\", 0\n" +
        //overflow para el producto de punto flotante
        "@ERROR_OVERFLOW_MUL_DOUBLE DB \"ERROR: se produjo overflow al momento de multiplicar dos datos de punto flotante\", 0\n" +
        "@overflow_punto_flotante DQ 0.17976931348623157E309\n";
        this.codigo = ".code\n" + "FINIT\n";
        this.aux = 0;
        this.salto = "";
        this.errores = errores;
    }
    
    private String getAuxiliar(){
        this.aux += 1;
        return "@aux" + this.aux;
    }

    public void start(){
        this.codigo += "\n";
        
        for (String metodoPolaca : metodosPolaca.keySet()){
            if (!metodoPolaca.equals(":main")) {
                this.codigo += "\n";
                this.codigo += metodoPolaca.replaceAll(":", "@") + ":\n";
                cargarAssembler(metodoPolaca);
            }
        }
        this.codigo += "\n";
        this.codigo += "MAIN:\n";
        cargarAssembler(":main");
        
        for (String simbolo : tablaSimbolos.getSimbolos()) {
            String tipo = tablaSimbolos.getTipo(simbolo);
            if (tipo.equals("UINT")) {//16 bits
                this.data += simbolo.replaceAll(":","@").replaceAll("\\.","_") + " DW " + "?\n";
            } else if (tipo.equals("LONG")) {//32 bits
                this.data += simbolo.replaceAll(":","@").replaceAll("\\.","_") + " DD " + "?\n";
            } else if (tipo.equals("DOUBLE")) {//64 bits
                this.data += simbolo.replaceAll(":","@").replaceAll("\\.","_") + " DQ " + "?\n";
            } else if (tipo.equals("Double")) {//64 bits
                this.data += "@" + simbolo.replaceAll("\\.","_").replaceAll("-","@") + " DQ " + simbolo + "\n";
            }
        }
    }

    public void cargarAssembler(String nombreMetodoPolaca) {
        for (String token : metodosPolaca.get(nombreMetodoPolaca)) {
            /*--------------------------------------OPERADORES--------------------------------------*/
            if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) { 
                
                String operando2 = pila.pop();
                String tipoOp2 = tablaSimbolos.getTipo(operando2);
                
                String operando1 = pila.pop();
                String tipoOp1 = tablaSimbolos.getTipo(operando1);

                String tipoDeOperandos = comprobarTipos(operando1, tipoOp1, operando2, tipoOp2);
                
                operando1 = operando1.replaceAll(":","@").replaceAll("\\.","_");
                operando2 = operando2.replaceAll(":","@").replaceAll("\\.","_");
                
                if (tipoDeOperandos.equals("UINT")) {
                    if (Character.isDigit(operando1.charAt(0)) || (operando1.charAt(0) == '-' && Character.isDigit(operando1.charAt(1)))) {
                        operando1 = operando1.substring(0,operando1.length()-3);
                    }
                    if (Character.isDigit(operando2.charAt(0)) || (operando2.charAt(0) == '-' && Character.isDigit(operando2.charAt(1)))) {
                        operando2 = operando2.substring(0,operando2.length()-3);
                    }
                    switch (token) {
                        case "+":
                            sumarUint(operando1, operando2);
                            break;
                        case "-":
                            restarUint(operando1, operando2); //controlar resultados negativos en restas de enteros sin signo (SF)
                            break;
                        case "*":
                            multiplicarUint(operando1, operando2);
                            break;
                        case "/":
                            dividirUint(operando1, operando2);
                            break;
                    }
                } else if (tipoDeOperandos.equals("LONG")) {
                    if (Character.isDigit(operando1.charAt(0)) || (operando1.charAt(0) == '-' && Character.isDigit(operando1.charAt(1)))) {
                        operando1 = operando1.substring(0,operando1.length()-2);
                    }
                    if (Character.isDigit(operando2.charAt(0)) || (operando2.charAt(0) == '-' && Character.isDigit(operando2.charAt(1)))) {
                        operando2 = operando2.substring(0,operando2.length()-2);
                    }
                    switch (token) {
                        case "+":
                            sumarLong(operando1, operando2);  //Controlar overflow para la suma de enteros (OF)
                            break;
                        case "-":
                            restarLong(operando1, operando2);
                            break;
                        case "*":
                            multiplicarLong(operando1, operando2);
                            break;
                        case "/":
                            dividirLong(operando1, operando2);
                            break;
                    }
                } else if (tipoDeOperandos.equals("DOUBLE")) {
                    if (Character.isDigit(operando1.charAt(0)) || (operando1.charAt(0) == '-' && Character.isDigit(operando1.charAt(1)))){
                        operando1 = "@" + operando1.replaceAll("\\.","_").replaceAll("-","@");
                    }
                    if (Character.isDigit(operando2.charAt(0)) || (operando2.charAt(0) == '-' && Character.isDigit(operando2.charAt(1)))){
                        operando2 = "@" + operando2.replaceAll("\\.","_").replaceAll("-","@");
                    }
                    switch (token) {
                        case "+":
                            sumarDouble(operando1, operando2);
                            break;
                        case "-":
                            restarDouble(operando1, operando2);
                            break;
                        case "*":
                            multiplicarDouble(operando1, operando2); //Controlar overflow para el producto de punto flotante
                            break;
                        case "/":
                            dividirDouble(operando1, operando2);
                            break;
                    }
                }
            /*--------------------------------------COMPARADORES--------------------------------------*/
            } else if (token.equals("<") || token.equals(">") || token.equals("<=") || token.equals(">=") || token.equals("==") || token.equals("!!")) {
                String operando2 = pila.pop();
                String tipoOp2 = tablaSimbolos.getTipo(operando2);
                String operando1 = pila.pop();
                String tipoOp1 = tablaSimbolos.getTipo(operando1);
                
                String tipoDeOperandos = comprobarTipos(operando1, tipoOp1, operando2, tipoOp2);
                
                operando1 = operando1.replaceAll(":","@").replaceAll("\\.","_");
                operando2 = operando2.replaceAll(":","@").replaceAll("\\.","_");
                if (tipoDeOperandos.equals("UINT")) {
                    if (Character.isDigit(operando1.charAt(0)) || (operando1.charAt(0) == '-' && Character.isDigit(operando1.charAt(1)))) {
                        operando1 = operando1.substring(0,operando1.length()-3);
                    }
                    if (Character.isDigit(operando2.charAt(0)) || (operando2.charAt(0) == '-' && Character.isDigit(operando2.charAt(1)))) {
                        operando2 = operando2.substring(0,operando2.length()-3);
                    }
                    this.codigo += "MOV CX, " + operando1 + "\n";
                    this.codigo += "CMP CX, " + operando2 + "\n"; //afecta flags
                    switch (token) {
                        case "<":
                            this.salto += "JB"; //JB menor -> JAE mayorIgual
                            break;
                        case ">":
                            this.salto += "JA"; //JA mayor -> JBE menorIgual
                            break;
                        case "<=":
                            this.salto += "JBE"; //JBE menorIgual -> JA mayor
                            break;
                        case ">=":
                            this.salto += "JAE"; //JAE mayorIgual -> JB menor
                            break;
                        case "==":
                            this.salto += "JE"; //JE igual -> JNE distinto
                            break;
                        case "!!":
                            this.salto += "JNE"; //JNE distinto -> JE igual
                            break;
                    }
                } else if (tipoDeOperandos.equals("LONG")) {
                    if (Character.isDigit(operando1.charAt(0)) || (operando1.charAt(0) == '-' && Character.isDigit(operando1.charAt(1)))) {
                        operando1 = operando1.substring(0,operando1.length()-2);
                    }
                    if (Character.isDigit(operando2.charAt(0)) || (operando2.charAt(0) == '-' && Character.isDigit(operando2.charAt(1)))) {
                        operando2 = operando2.substring(0,operando2.length()-2);
                    }
                    this.codigo += "MOV ECX, " + operando1 + "\n";
                    this.codigo += "CMP ECX, " + operando2 + "\n"; //afecta flags
                    switch (token) {
                        case "<":
                            this.salto += "JL"; //JL menor -> JGE mayorIgual
                            break;
                        case ">":
                            this.salto += "JG"; //JG mayor-> JLE menorIgual
                            break;
                        case "<=":
                            this.salto += "JLE"; //JLE menorIgual -> JG mayor
                            break;
                        case ">=":
                            this.salto += "JGE"; //JGE mayorIgual -> JL menor
                            break;
                        case "==":
                            this.salto += "JE"; //JE igual -> JNE distinto
                            break;
                        case "!!":
                            this.salto += "JNE"; //JNE distinto -> JE igual
                            break;
                    }
                } else if (tipoDeOperandos.equals("DOUBLE")) {
                    if (Character.isDigit(operando1.charAt(0)) || (operando1.charAt(0) == '-' && Character.isDigit(operando1.charAt(1)))){
                        operando1 = "@" + operando1.replaceAll("\\.","_").replaceAll("-","@");
                    }
                    if (Character.isDigit(operando2.charAt(0)) || (operando2.charAt(0) == '-' && Character.isDigit(operando2.charAt(1)))){
                        operando2 = "@" + operando2.replaceAll("\\.","_").replaceAll("-","@");
                    }
                    this.codigo += "FLD " + operando1 + "\n";
                    this.codigo += "FCOM " + operando2 + "\n";
                    this.codigo += "FSTSW " + this.aux2bytes + "\n";
                    this.codigo += "MOV AX, " + this.aux2bytes + "\n";
                    this.codigo += "SAHF\n";
                    switch (token) {
                        case "<":
                            this.salto += "JB"; //JB menor -> JAE mayorIgual
                            break;
                        case ">":
                            this.salto += "JA"; //JA mayor -> JBE menorIgual
                            break;
                        case "<=":
                            this.salto += "JBE"; //JBE menorIgual -> JA mayor
                            break;
                        case ">=":
                            this.salto += "JAE"; //JAE mayorIgual -> JB menor
                            break;
                        case "==":
                            this.salto += "JE"; //JE igual -> JNE distinto
                            break;
                        case "!!":
                            this.salto += "JNE"; //JNE distinto -> JE igual
                            break;
                    }
                }
            /*--------------------------------------ASIGNACION--------------------------------------*/
            } else if (token.equals("=")) {
                String operando2 = pila.pop();
                String tipoOp2 = tablaSimbolos.getTipo(operando2);
                String operando1 = pila.pop();
                String tipoOp1 = tablaSimbolos.getTipo(operando1);
                
                String tipoDeOperandos = comprobarTipos(operando1, tipoOp1, operando2, tipoOp2);
                
                operando1 = operando1.replaceAll(":","@").replaceAll("\\.","_");
                operando2 = operando2.replaceAll(":","@").replaceAll("\\.","_");
                
                if (tipoDeOperandos.equals("UINT")) {
                    if (Character.isDigit(operando1.charAt(0)) || (operando1.charAt(0) == '-' && Character.isDigit(operando1.charAt(1)))) {
                        operando1 = operando1.substring(0,operando1.length()-3);
                    } 
                    if (Character.isDigit(operando2.charAt(0)) || (operando2.charAt(0) == '-' && Character.isDigit(operando2.charAt(1)))) {
                        operando2 = operando2.substring(0,operando2.length()-3);
                    }
                    this.codigo += "MOV AX, " + operando1 + "\n";
                    this.codigo += "MOV " + operando2 + ", AX\n";
                } else if (tipoDeOperandos.equals("LONG")) {
                    if (Character.isDigit(operando1.charAt(0)) || (operando1.charAt(0) == '-' && Character.isDigit(operando1.charAt(1)))) {
                        operando1 = operando1.substring(0,operando1.length()-2);
                    }
                    if (Character.isDigit(operando2.charAt(0)) || (operando2.charAt(0) == '-' && Character.isDigit(operando2.charAt(1)))) {
                        operando2 = operando2.substring(0,operando2.length()-2);
                    }
                    this.codigo += "MOV EAX, " + operando1 + "\n";
                    this.codigo += "MOV " + operando2 + ", EAX\n";
                } else if (tipoDeOperandos.equals("DOUBLE")) {

                    if (Character.isDigit(operando1.charAt(0)) || (operando1.charAt(0) == '-' && Character.isDigit(operando1.charAt(1)))){
                        operando1 = "@" + operando1.replaceAll("\\.","_").replaceAll("-","@");
                    }
                    if (Character.isDigit(operando2.charAt(0)) || (operando2.charAt(0) == '-' && Character.isDigit(operando2.charAt(1)))){
                        operando2 = "@" + operando2.replaceAll("\\.","_").replaceAll("-","@");
                    }
                    this.codigo += "FLD " + operando1 + "\n";
                    this.codigo += "FSTP " + operando2 + "\n";
                }
            /*--------------------------------------CONVERSION EXPLICITA--------------------------------------*/
            } else if (token.equals("TOD")) {
                String operando = pila.pop();
                String tipoOp = tablaSimbolos.getTipo(operando).toUpperCase();
                if (tipoOp.equals("UINT") || tipoOp.equals("LONG")) {
                    if (Character.isDigit(operando.charAt(0))){
                        String aux1 = getAuxiliar();
                        if (tipoOp.equals("UINT")){
                            operando = operando.substring(0,operando.length()-3);
                            tablaSimbolos.agregarSimbolo(aux1, new Token(TokenType.UINT)); //guardo auxiliar en tabla de simbolos
                        } else {
                            operando = operando.substring(0,operando.length()-2);
                            tablaSimbolos.agregarSimbolo(aux1, new Token(TokenType.LONG)); //guardo auxiliar en tabla de simbolos
                        }
                        this.codigo += "MOV AX, " + operando.replaceAll(":","@").replaceAll("\\.","_") + "\n";
                        this.codigo += "MOV " + aux1 + ", AX\n";
                        this.codigo += "FILD " + aux1 + "\n";
                    } else {
                        this.codigo += "FILD " + operando.replaceAll(":","@").replaceAll("\\.","_") + "\n";
                    }
                    String aux2 = getAuxiliar();
                    tablaSimbolos.agregarSimbolo(aux2, new Token(TokenType.DOUBLE)); //guardo auxiliar en tabla de simbolos
                    this.codigo += "FST " + aux2 + "\n";
                    pila.push(aux2);
                } else if (tipoOp.equals("DOUBLE")) {
                    errores.add(new Error("La conversion de " + operando + " no es necesaria debido a que ya es un double", "WARNING"));
                    pila.push(operando);
                } else {
                    errores.add(new Error("El operando " + operando + " no es un tipo compatible para realizar una conversion", "ERROR"));
                    pila.push(operando);
                }
            /*---------------------------------------MOSTRAR POR PANTALLA---------------------------------------*/
            } else if (token.equals("PRINT")) {
                String operando = pila.pop();
                imprimirPantalla(operando);
            /*----------------------------------------RETORNO DE FUNCION----------------------------------------*/
            } else if (token.equals("RET")) {
                this.codigo += token + "\n";
            /*----------------------------------SALTO BIFURCACION POR VERDADERO----------------------------------*/
            } else if (token.equals("BV")) {
                this.codigo += this.salto + " " + pila.pop() + "\n";
                this.salto = "";
            /*------------------------------------SALTO BIFURCACION POR FALSO------------------------------------*/
            } else if (token.equals("BF")) {
                this.salto = NegarSalto(this.salto);
                this.codigo += this.salto + " " + pila.pop() + "\n";
                this.salto = "";
            /*-----------------------------------------SALTO INCONDICIONAL----------------------------------------*/
            } else if (token.equals("BI")) {
                // salto incondicional -> JMP label
                this.codigo += "JMP " + pila.pop() + "\n";
            /*---------------------------------------ASIGANCION DE ETIQUETA---------------------------------------*/
            } else if (token.startsWith("L[")) {
                this.codigo += nombreMetodoPolaca.replaceAll(":", "@") + "_" + token.substring(2,token.lastIndexOf("]")) + ":\n";
            /*----------------------------------------UBICACION DE ETIQUETA----------------------------------------*/
            } else if (token.startsWith("[")) {
                pila.push(nombreMetodoPolaca.replaceAll(":", "@") + "_" + token.substring(1,token.lastIndexOf("]")));
            /*------------------------------------------LLAMADO A FUNCION------------------------------------------*/
            } else if (token.startsWith("Call :main:")) {
                this.codigo += "CALL " + token.substring(5, token.length()).replaceAll(":", "@") + "\n";
            /*-------------------------------------------FIN DE PROGRAMA-------------------------------------------*/
            } else if (token.equals("END MAIN")) {
                this.codigo += "FINIT\n" + "invoke ExitProcess, 0\n" +
                token + "\n";
            } else {
                pila.push(token);
            }
        }
    }

    public String comprobarTipos(String operando1, String tipoOperando1, String operando2, String tipoOperando2) {
        tipoOperando1 = tipoOperando1.toUpperCase();
        tipoOperando2 = tipoOperando2.toUpperCase();

        if (tipoOperando1.equals(tipoOperando2)) {
            return tipoOperando1;
        } else {
            errores.add(new Error("No se puede realizar la operacion debido a que " + operando2 + " es de tipo " + tipoOperando2 + " y " + operando1 + " es de tipo " + tipoOperando1,"ERROR"));
            return tipoOperando1;
        }
    }


    /*---Generadores de codigo--------------------------------------------------------------------------------------------------------------------------------------*/

    /*---------------------------------------------------------------------SUMA---------------------------------------------------------------------*/
    public void sumarUint(String op1, String op2) { //bien
        this.codigo += "MOV CX, " + op1 + "\n";
        this.codigo += "ADD CX, " + op2 + "\n";

        // genero auxiliar y lo guardo en la tabla de simbolos con su respectivo tipo
        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.UINT)); //guardo auxiliar en tabla de simbolos
        
        this.codigo += "MOV " + auxiliar + ", CX\n";
        pila.push(auxiliar);
    }

    public void sumarLong(String op1, String op2) { //bien
        this.codigo += "MOV ECX, " + op1 + "\n";
        this.codigo += "ADD ECX, " + op2 + "\n";
        
        // overflow Long
        String etiquetaSalto = getAuxiliar();
        this.codigo += "JNO " + etiquetaSalto + "\n"; //Controlar overflow para la suma de enteros (OF)
        // this.codigo += "invoke StdOut, addr @ERROR_OVERFLOW_SUMA_LONG \n";
        this.codigo += "invoke MessageBox, NULL, addr @ERROR_OVERFLOW_SUMA_LONG, addr @ERROR_OVERFLOW_SUMA_LONG, MB_OK\n";
        this.codigo += "invoke ExitProcess, 0\n";
        this.codigo += etiquetaSalto + ":\n";
        
        // genero auxiliar y lo guardo en la tabla de simbolos con su respectivo tipo
        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.LONG)); //guardo auxiliar en tabla de simbolos

        this.codigo += "MOV " + auxiliar + ", ECX\n";
        pila.push(auxiliar);
    }

    public void sumarDouble(String op1, String op2) { //bien
        this.codigo += "FLD " + op1 + "\n"; // introduce una copia de memoria en ST
        this.codigo += "FADD "+ op2 + "\n";
        // genero auxiliar y lo guardo en la tabla de simbolos con su respectivo tipo
        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.DOUBLE)); //guardo auxiliar en tabla de simbolos

        this.codigo += "FSTP " + auxiliar + "\n"; // extrae una copia de ST en memoria
        pila.push(auxiliar);
    }

    /*---------------------------------------------------------------------RESTA---------------------------------------------------------------------*/
    public void restarUint(String op1, String op2) { //bien
        this.codigo += "MOV CX, " + op1 + "\n";
        this.codigo += "SUB CX, " + op2 + "\n";
        
        // resultado negativo entre enteros sin signo
        String etiquetaSalto = getAuxiliar();
        this.codigo += "JNS " + etiquetaSalto + "\n"; //controlar resultados negativos en restas de enteros sin signo ( JS )
        // this.codigo += "invoke StdOut, addr @ERROR_RESULT_NEGATIVO_UINT\n";
        this.codigo += "invoke MessageBox, NULL, addr @ERROR_RESULT_NEGATIVO_UINT, addr @ERROR_RESULT_NEGATIVO_UINT, MB_OK\n";
        this.codigo += "invoke ExitProcess, 0\n";
        this.codigo += etiquetaSalto + ":\n";
        
        // genero auxiliar y lo guardo en la tabla de simbolos con su respectivo tipo
        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.UINT)); //guardo auxiliar en tabla de simbolos

        this.codigo += "MOV " + auxiliar + ", CX\n";
        pila.push(auxiliar);
    }

    public void restarLong(String op1, String op2) { //bien
        this.codigo += "MOV ECX, " + op1 + "\n";
        this.codigo += "SUB ECX, " + op2 + "\n";
        // genero auxiliar y lo guardo en la tabla de simbolos con su respectivo tipo
        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.LONG)); //guardo auxiliar en tabla de simbolos

        this.codigo += "MOV " + auxiliar + ", ECX\n";
        pila.push(auxiliar);
    }

    public void restarDouble(String op1, String op2) { //bien
        this.codigo += "FLD " + op1 + "\n"; // introduce una copia de memoria en ST
        this.codigo += "FSUB "+ op2 + "\n";
        // genero auxiliar y lo guardo en la tabla de simbolos con su respectivo tipo
        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.DOUBLE)); //guardo auxiliar en tabla de simbolos

        this.codigo += "FSTP " + auxiliar + "\n"; // extrae una copia de ST en memoria
        pila.push(auxiliar);
    }

    /*---------------------------------------------------------------------MULTIPLICACION---------------------------------------------------------------------*/
    public void multiplicarUint(String op1, String op2) { //bien
        // MUL AX para UINT -> resultado queda en DX:AX
        this.codigo += "MOV AX, " + op1 +"\n";
        this.codigo += "MOV BX, " + op2 +"\n";
        this.codigo += "MUL BX\n";

        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.UINT));

        this.codigo += "MOV " + auxiliar + ", AX\n";
        pila.push(auxiliar);
    }

    public void multiplicarLong(String op1, String op2) { //bie
        // IMUL EAX para LONG -> resultado queda en EDX:EAX
        this.codigo += "MOV EAX, " + op1 +"\n";
        this.codigo += "IMUL EAX, " + op2 + "\n";   

        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.LONG));
        
        this.codigo += "MOV " + auxiliar + ", EAX\n";
        pila.push(auxiliar);
    }

    public void multiplicarDouble(String op1, String op2) { //bien
        this.codigo += "FLD " + op1 + "\n"; // introduce una copia de memoria en ST
        this.codigo += "FLD " + op2 + "\n"; // introduce una copia de memoria en ST
        this.codigo += "FMUL\n";
        
        this.codigo += "FLD @overflow_punto_flotante\n";
        this.codigo += "FCOM\n";
        
        this.codigo += "FSTSW " + aux2bytes + "\n";
        this.codigo += "MOV AX, " + aux2bytes + "\n";
        this.codigo += "SAHF\n"; // Establece los flags de acuerdo al resultado de la comparación

        String etiquetaSalto = getAuxiliar();
        this.codigo += "JA " + etiquetaSalto + "\n"; // Salta por mayor
        this.codigo += "invoke MessageBox, NULL, addr @ERROR_OVERFLOW_MUL_DOUBLE, addr @ERROR_RESULT_NEGATIVO_UINT, MB_OK\n";
        this.codigo += "invoke ExitProcess, 0\n";
        this.codigo += etiquetaSalto + ":\n";

        // genero auxiliar y lo guardo en la tabla de simbolos con su respectivo tipo
        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.DOUBLE)); //guardo auxiliar en tabla de simbolos
        
        this.codigo += "FSTP " + auxiliar + "\n"; // extrae una copia de ST en memoria
        pila.push(auxiliar);
    }

    /*---------------------------------------------------------------------DIVISION---------------------------------------------------------------------*/
    public void dividirUint(String op1, String op2) { //bien
        // mover primero el dividendo a el registro DX:AX o EDX:ADX segun corresponda
        // para los UINT mover 0 a DX
        // luego utilizar DIV con el divisor
        // El cociente quedara en AX y el resto en DX
        this.codigo += "MOV AX, " + op1 + "\n";
        this.codigo += "MOV DX, 0\n";
        this.codigo += "MOV BX, " + op2 + "\n";
        this.codigo += "DIV BX\n";

        // genero auxiliar y lo guardo en la tabla de simbolos con su respectivo tipo
        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.UINT)); //guardo auxiliar en tabla de simbolos
        
        this.codigo += "MOV " + auxiliar + ", AX\n";
        pila.push(auxiliar);
    }

    // para LONG usar CDQ que extiende el signo de EAX en EDX:AX
    // IDIV para enteros o enteros largos
    public void dividirLong(String op1, String op2) { //bien
        this.codigo += "MOV EAX, " + op1 + "\n";
        this.codigo += "CDQ\n";
        this.codigo += "MOV EBX, " + op2 + "\n";
        this.codigo += "IDIV EBX\n";

        // genero auxiliar y lo guardo en la tabla de simbolos con su respectivo tipo
        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.LONG)); //guardo auxiliar en tabla de simbolos
        
        this.codigo += "MOV " + auxiliar + ", EAX\n";
        pila.push(auxiliar);
    }

    public void dividirDouble(String op1, String op2) { //bien
        this.codigo += "FLD " + op1 + "\n"; // introduce una copia de memoria en ST
        this.codigo += "FDIV "+ op2 +"\n";

        // genero auxiliar y lo guardo en la tabla de simbolos con su respectivo tipo
        String auxiliar = getAuxiliar();
        tablaSimbolos.agregarSimbolo(auxiliar, new Token(TokenType.DOUBLE)); //guardo auxiliar en tabla de simbolos
        this.codigo += "FSTP " + auxiliar + "\n"; // extrae una copia de ST en memoria
        pila.push(auxiliar);
    }

    /*--------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public void imprimirPantalla(String op){
        String auxiliarCadena = getAuxiliar();
        this.codigo += "invoke StdOut, addr " + auxiliarCadena + "\n";
        this.codigo += "invoke StdOut, addr @newline \n";
        this.data += auxiliarCadena + " DB " + op + ",0\n";
    }

    /*--------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    private String NegarSalto(String salto){
        switch (salto) {
            case "JB":
                return "JAE";
            case "JA":
                return "JBE";
            case "JBE":
                return "JA";
            case "JAE":
                return "JB";
            case "JL":
                return "JGE";
            case "JG":
                return "JLE";
            case "JLE":
                return "JG";
            case "JGE":
                return "JL";
            case "JE":
                return "JNE";
            case "JNE":
                return "JE";
            default:
                return "JMP";
        }
    }
    
    public void exportar(String filename){
        try (FileWriter fileWriter = new FileWriter("./Assembler/" + filename + ".asm")) {
            fileWriter.write(this.data + this.codigo);
            System.out.println("Se genero el codigo assembler");
        } catch (IOException e) {
            System.err.println("Error a la hora de generar el codigo" + e.getMessage());
        }
    }
}