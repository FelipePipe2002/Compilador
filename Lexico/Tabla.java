package Lexico;

import java.util.HashMap;

public class Tabla {
    private HashMap<String, Token> tabla;

    public Tabla() {
        tabla = new HashMap<String, Token>();
    }

    public void agregarSimbolo(String nombre, Token valor) {
        tabla.put(nombre, valor);
    }

    public Token obtenerSimbolo(String nombre) {
        return tabla.get(nombre);
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
        System.out.println("+----------------------+----------------------+");
        System.out.println("| Nombre               | Valor                |");
        System.out.println("+----------------------+----------------------+");
        for (String key : tabla.keySet()) {
            System.out.printf("| %-20s | %-20s |\n", key, tabla.get(key));
        }
        System.out.println("+----------------------+----------------------+");
    }
    
}