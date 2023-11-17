package Lexico;

import java.util.HashMap;

import Sintactico.Parser;

import java.util.ArrayList;

public class Tabla {
    private HashMap<String, Atributos> tabla;

    public Tabla() {
        this.tabla = new HashMap<String, Atributos>();
    }

    public void agregarSimbolo(String nombre, Token valor) {
        Atributos aux = new Atributos(valor);
        tabla.put(nombre, aux);
    }

    public Token obtenerSimbolo(String nombre) {
        return tabla.get(nombre).getToken();
    }

    private ArrayList<String> getMetodos(String nombreClase) {
        ArrayList<String> metodos = new ArrayList<>();

        for (String nombreMetodo : tabla.keySet()) {
            if (nombreMetodo.endsWith(nombreClase)) {
                if (tabla.get(nombreMetodo).getTipo() == "VOID") {
                    metodos.add(nombreMetodo.substring(0,nombreMetodo.indexOf(":")));
                }
            }
        }
        return metodos;
    }

    public void checkAtributosDeClase(String nombreVariable, String ambito) {
        if (!ambito.equals(":main")) {
            String posibleClase = ambito.substring(ambito.lastIndexOf(":") + 1, ambito.length()) + ambito.substring(0, ambito.lastIndexOf(":"));
            if (tabla.get(posibleClase).getTipo() == "CLASS") {
                if (nombreVariable.contains(";")) {
                    String[] variables = nombreVariable.split(";");
                    for (String variable : variables) {
                        tabla.get(variable + ambito).setUso(true);
                    }
                } else {
                    tabla.get(nombreVariable + ambito).setUso(true);
                }
            }
        }
    }

    public boolean implementaMetodosInterfaz(String nombreClase, String nombreInterfaz){
        ArrayList<String> metodosClase = new ArrayList<String>();
        metodosClase = getMetodos(nombreClase);
        return metodosClase.containsAll(getMetodos(nombreInterfaz));
    }

    public boolean agregarAmbito(String nombre, String ambito,String tipo) {
        String nombreConAmbito = nombre + ambito;
        if(!existeSimbolo(nombreConAmbito)){
            Atributos aux = tabla.get(nombre);
            aux.setTipo(tipo);
            eliminarSimbolo(nombre);
            tabla.put(nombreConAmbito, aux);
            return true;
        }
        return false;
    }

    public boolean agregarInterfaz(String nombre, String nombreInterfaz) {
        if(existeSimbolo(nombreInterfaz) && tabla.get(nombreInterfaz).getTipo().equals("INTERFACE")){
            tabla.get(nombre).addInterfaz(nombreInterfaz);
            eliminarSimbolo(nombreInterfaz.substring(0,nombreInterfaz.indexOf(":")));
            return true;
        }
        return false;
    }

    public String getInterfaz(String nombre) {
        return tabla.get(nombre).getInterfaz();
    }
    
    public boolean agregarHerencia(String nombre, String nombrePadre) {
        int nivelHerenciaPadre = 0;
        if(existeSimbolo(nombrePadre) && tabla.get(nombrePadre).getTipo().equals("CLASS")){
            nivelHerenciaPadre = tabla.get(nombrePadre).getNivelHerencia();
            tabla.get(nombre).addHerencia(nombrePadre, nivelHerenciaPadre);
            eliminarSimbolo(nombrePadre.substring(0,nombrePadre.indexOf(":")));
            return true;
        }
        return false;
    }

    public String getPadreClase(String nombre) {
        return tabla.get(nombre).getPadreClase();
    }

    public int getNivelHerencia(String nombre) {
        return tabla.get(nombre).getNivelHerencia();
    }

    public boolean addNivelVoid(String nombre, String ambito) { // ambito -> :main:asd:fgh 
        String metodoPadre = ambito.substring(ambito.lastIndexOf(":")+1,ambito.length());
        metodoPadre = metodoPadre + ambito.substring(0,ambito.lastIndexOf(":"));
        String nombreConAmbito = nombre + ambito;
        if (existeSimbolo(metodoPadre) && tabla.get(metodoPadre).getTipo() == "VOID") {
            tabla.get(nombreConAmbito).addProfundidad(tabla.get(metodoPadre).getProfundidad());
            return true;
        }
        return false;
    }

    public int getProfundidadVoid(String nombre, String ambito) {
        String nombreConAmbito = nombre + ambito;
        return tabla.get(nombreConAmbito).getProfundidad();
    }

    public boolean existeSimbolo(String nombre) {
        return tabla.containsKey(nombre);
    }

    public void eliminarSimbolo(String nombre) {
        tabla.remove(nombre);
    }

    public void convertirNegativo(String nombre){
        Token aux = obtenerSimbolo(nombre);
        eliminarSimbolo(nombre);
        agregarSimbolo("-"+nombre,aux);
    }

    public void setUso(String nombre, String ambito, boolean uso) {
        String nombreConAmbito = nombre + ambito;
        if (existeSimbolo(nombreConAmbito)) {
            tabla.get(nombreConAmbito).setUso(uso);
        }
    }

    private ArrayList<String> getMetodosPadres(String clasePadre) {
        ArrayList<String> metodos = new ArrayList<>();
        while (!clasePadre.equals("")) {
            clasePadre = clasePadre.substring(0, clasePadre.indexOf(":"));
            metodos.addAll(getMetodos(":main:" + clasePadre));
            clasePadre = tabla.get(clasePadre+":main").getPadreClase();
        }
        return metodos;
    }

    public ArrayList<String> metodoSobreescriptos(String claseHija) {
        ArrayList<String> metodosPadre = new ArrayList<>();
        ArrayList<String> aux = new ArrayList<>();
        metodosPadre = getMetodosPadres(tabla.get(claseHija).getPadreClase());
        for (String metodo : getMetodos(":main:" + claseHija.substring(0,claseHija.indexOf(":")))) {
            if (metodosPadre.contains(metodo)) {
                aux.add(metodo);
            }
        }
        return aux;
    }

    public boolean existeVariable(String nombre, String ambito){
        eliminarSimbolo(nombre);
        if(!nombre.contains(".")){
            while (ambito != ""){
                String nombreambito = nombre + ambito;
                if(existeSimbolo(nombreambito)){
                    String tipo = tabla.get(nombreambito).getTipo().toUpperCase();
                    if (tipo == "UINT" || tipo == "LONG" || tipo == "DOUBLE")
                        return true;
                }
                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
            }
        } else {
            boolean encontrado = false;
            while (ambito != "" && !encontrado){
                String nombreambito = nombre.substring(0,nombre.indexOf(".")) + ambito; //c.ab.x -> c:ambito
                eliminarSimbolo(nombre.substring(0,nombre.indexOf(".")));
                if(existeSimbolo(nombreambito)){
                    String clase = tabla.get(nombreambito).getTipo(); //c -> clase1
                    while(existeSimbolo(clase + ":main")){  //clase1:main -> true | false
                        String tipoClase = tabla.get(clase+":main").getTipo();
                        if(tipoClase == "CLASS"){ //si tipo:main es una clase
                            
                            nombre = nombre.substring(nombre.indexOf(".")+1,nombre.length()); //c.ab.x -> ab.x -> x

                            if(!nombre.contains(".")){ //busco variable de clase
                                eliminarSimbolo(nombre);
                                if(existeSimbolo(nombre + ":main:" + clase)){
                                    if(tabla.get(nombre + ":main:" + clase).getTipo() != "VOID")
                                        return true;
                                    else 
                                        return false;
                                }else{
                                    while(tabla.get(clase + ":main").getPadreClase() != ""){
                                        clase = tabla.get(clase + ":main").getPadreClase();
                                        if(existeSimbolo(nombre + ":main:" + clase)){
                                            return true;
                                        } 
                                    }
                                    return false;
                                }
                            } else if (tabla.get(clase + ":main").getPadreClase() != ""){ //busco clase
                                clase = nombre.substring(0, nombre.indexOf(".")); //ab.x -> ab
                            } else { //c.xs.x
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }

                    return false;
                }
                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
            }
        }
        return false;
    }

    public boolean existeMetodo(String nombre, String ambito){
        eliminarSimbolo(nombre);
        if(!nombre.contains(".")){
            while (ambito != ""){
                String nombreConAmbito = nombre + ambito;
                if(existeSimbolo(nombreConAmbito)){
                    String tipo = tabla.get(nombreConAmbito).getTipo();
                    tipo = tipo.toUpperCase();
                    if (tipo == "VOID")
                        return true;
                }
                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
            }
        } else {
            boolean encontrado = false;
            while (ambito != "" && !encontrado){
                String nombreambito = nombre.substring(0,nombre.indexOf(".")) + ambito;
                eliminarSimbolo(nombre.substring(0,nombre.indexOf(".")));
                if(existeSimbolo(nombreambito)){
                    String tipo = tabla.get(nombreambito).getTipo();
                    while(existeSimbolo(tipo + ":main")){
                        String tipoClase = tabla.get(tipo+":main").getTipo();
                        if(tipoClase == "CLASS"){
                            
                            nombre = nombre.substring(nombre.indexOf(".")+1,nombre.length());

                            if(!nombre.contains(".")){ //busco variable de clase
                                eliminarSimbolo(nombre);
                                if(existeSimbolo(nombre + ":main:" + tipo)){
                                    if(tabla.get(nombre + ":main:" + tipo).getTipo() == "VOID")
                                        return true;
                                    else 
                                        return false;
                                }else{
                                    return false;
                                }
                            } else if (tabla.get(tipo + ":main").getPadreClase() != ""){ //busco clase
                                tipo = nombre.substring(0, nombre.indexOf("."));
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }

                    return false;
                }
                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
            }
        }
        return false;
    }

    public boolean existeClase(String nombre, String ambito){
        String nombreConAmbito = nombre + ambito;
        return ((tabla.containsKey(nombreConAmbito)) && (tabla.get(nombreConAmbito).getTipo() == "CLASS"));
    }

    public ArrayList<String> variablesNoUsadas(){
        ArrayList<String> aux = new ArrayList<>();
        tabla.forEach((k,v) -> {
            String tipo = v.getTipo();
            if(tipo == "LONG" || tipo == "UINT" || tipo == "DOUBLE"){
                if(!v.isUso()){
                    aux.add(k);
                }
            }
        });
        return aux;
    }

    public void imprimirTabla() {
        System.out.println("Tabla de simbolos:");
        System.out.println("+-----------------+---------------+------------+-------+-----------+--------------+-------+");
        System.out.println("|     Nombre      |     Tipo      |   Ambito   |  Uso  | Implement | Padre Heren. | Nivel |");
        System.out.println("+-----------------+---------------+------------+-------+-----------+--------------+-------+");
        for (String key : tabla.keySet()) {
            Atributos atributo = tabla.get(key);
            String nombre = key;
            String ambito = "";
            if (key.contains(":")) {
                nombre = key.substring(0, key.indexOf(":"));
                ambito = key.substring(key.lastIndexOf(":") + 1, key.length());
            }
            String interfaz = atributo.getInterfaz();
            if (interfaz.contains(":")) {
                interfaz = interfaz.substring(0, interfaz.indexOf(":"));
            }
            String padreClase = atributo.getPadreClase();
            if (padreClase.contains(":")) {
                padreClase = padreClase.substring(0, padreClase.indexOf(":"));
            }
            System.out.printf("| %-15s | %-13s | %-10s | %-5s | %-9s | %-12s | %-5s |\n", key,atributo.getTipo(),ambito,atributo.isUso(),interfaz,padreClase,atributo.getNivelHerencia());
        }
        System.out.println("+-----------------+---------------+------------+-------+-----------+--------------+-------+");
    }
}