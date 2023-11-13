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
            System.out.println("Tabla de Simbolos: " + nombreConAmbito);
            eliminarSimbolo(nombre);
            tabla.put(nombreConAmbito, aux);
            return true;
        }
        return false;
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

    public void imprimirTabla() {
        System.out.println("Tabla de simbolos:");
        System.out.println("+----------------------+----------------------+-------+");
        System.out.println("| Nombre               | Tipo                 | Uso   |");
        System.out.println("+----------------------+----------------------+-------+");
        for (String key : tabla.keySet()) {
            System.out.printf("| %-20s | %-20s | %-5s |\n", key,tabla.get(key).getTipo(),tabla.get(key).isUso());
        }
        System.out.println("+----------------------+----------------------+-------+");
    }
    
}