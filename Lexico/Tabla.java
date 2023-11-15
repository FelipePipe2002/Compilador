package Lexico;

import java.util.HashMap;

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

    public int getNivelHerencia(String nombre) {
        return tabla.get(nombre).getNivelHerencia();
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

    public void limpiarTabla() {
        tabla.clear();
    }

    public boolean existeVariable(String nombre, String ambito){
        eliminarSimbolo(nombre);
        while (ambito != ""){
            String nombreambito = nombre + ambito;
            if(existeSimbolo(nombreambito)){
                String tipo = tabla.get(nombreambito).getTipo();
                tipo = tipo.toUpperCase();
                if (tipo == "UINT" || tipo == "LONG" || tipo == "DOUBLE")
                    return true;
            }
            ambito = ambito.substring(0,ambito.lastIndexOf(":"));
        }
        return false;
    }

    public boolean existeClase(String nombre, String ambito){
        String nombreConAmbito = nombre + ambito;
        return ((tabla.containsKey(nombreConAmbito)) && (tabla.get(nombreConAmbito).getTipo() == "CLASS"));
    }

    public void imprimirTabla() {
        System.out.println("Tabla de simbolos:");
        System.out.println("+----------------------+----------------------+-------+----------------------+----------------------+-------+");
        System.out.println("| Nombre               | Tipo                 | Uso   | Implement Interfaz   | Padre Herencia       | nivel |");
        System.out.println("+----------------------+----------------------+-------+----------------------+----------------------+-------+");
        for (String key : tabla.keySet()) {
            System.out.printf("| %-20s | %-20s | %-5s | %-20s | %-20s | %-5s |\n", key,tabla.get(key).getTipo(),tabla.get(key).isUso(),tabla.get(key).getInterfaz(),tabla.get(key).getHerencia(),tabla.get(key).getNivelHerencia());
        }
        System.out.println("+----------------------+----------------------+-------+----------------------+----------------------+-------+");
    }
}