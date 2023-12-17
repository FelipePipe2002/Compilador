package Lexico;

import java.util.HashMap;

import java.util.ArrayList;
import Errores.Error;

public class Tabla {
    private HashMap<String, Atributos> tabla;
    private ArrayList<Error> errores;

    public Tabla() {
        this.tabla = new HashMap<String, Atributos>();
    }

    public Tabla(ArrayList<Error> errores) {
        this.errores = errores;
        this.tabla = new HashMap<String, Atributos>();
    }

    public void agregarSimbolo(String nombre, Token valor) {
        Atributos aux = new Atributos(valor);
        tabla.put(nombre, aux);
    }

    public String getTipo(String nombre){
        return tabla.get(nombre).getTipo();
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

    public void setParametro(String nombreParamentro, String ambito){
        String nombreMetodo = ambito.substring(ambito.lastIndexOf(":") + 1,ambito.length()) + ambito.substring(0, ambito.lastIndexOf(":"));
        tabla.get(nombreParamentro + ambito).setParametro(true);
        tabla.get(nombreParamentro + ambito).setParametroDeFuncion(nombreMetodo);
        tabla.get(nombreMetodo).setParametro(true);
    }

    public String getParametro(String ambitoMetodo){
        for (String nombre : tabla.keySet()) {
            if (nombre.endsWith(ambitoMetodo)) {
                String tipo = tabla.get(nombre).getTipo();
                if (tipo.equalsIgnoreCase("UINT") || tipo.equalsIgnoreCase("LONG") || tipo.equalsIgnoreCase("DOUBLE")) {
                    return nombre;
                }
            }
        }
        return "Parametro invalido";
    }

    public boolean existeSimbolo(String nombre) {
        return tabla.containsKey(nombre);
    }

    public void eliminarSimbolo(String nombre) {
        tabla.remove(nombre);
    }

    public void convertirNegativo(String nombre){
        Token aux = obtenerSimbolo(nombre);
        agregarSimbolo("-"+nombre,aux);
    }

    public void setUso(String nombre, String ambito, boolean uso) {
        String nombreConAmbito = nombre + ambito;
        if (existeSimbolo(nombreConAmbito)) {
            tabla.get(nombreConAmbito).setUso(uso);
        }
    }

    public void checkAtributosDeClase(String nombreVariable, String ambito) {
        if (!ambito.equals(":main")) {
            String posibleClase = ambito.substring(ambito.lastIndexOf(":") + 1, ambito.length()) + ambito.substring(0, ambito.lastIndexOf(":"));
            if (tabla.get(posibleClase).getTipo() == "CLASS") {
                String[] variables = nombreVariable.split(";");
                for (String variable : variables) {
                    tabla.get(variable + ambito).setUso(true);
                }
            }
        }
    }

    private ArrayList<String> getMetodos(String ambito) {
        ArrayList<String> metodos = new ArrayList<>();

        for (String nombreMetodo : tabla.keySet()) {
            if (nombreMetodo.endsWith(ambito)) {
                if (tabla.get(nombreMetodo).getTipo() == "VOID") {
                    metodos.add(nombreMetodo.substring(0,nombreMetodo.indexOf(":")));
                }
            }
        }
        return metodos;
    }

    public void implementaMetodosInterfaz(String ambitoClase, String nombreInterfaz){
        ArrayList<String> metodosClase = new ArrayList<String>();
        metodosClase = getMetodos(ambitoClase); 
        ArrayList<String> parametrosMetodos = new ArrayList<>(); 
        parametrosMetodos = getTiposParametros(ambitoClase,metodosClase);
        ArrayList<String> metodosInterfaz = new ArrayList<String>();
        metodosInterfaz = getMetodos(nombreInterfaz); 
        ArrayList<String> parametrosMetodosInterfaz = new ArrayList<String>();
        parametrosMetodosInterfaz = getTiposParametros(nombreInterfaz,metodosInterfaz); 

        for (int i = 0; i < metodosClase.size(); i++) {
            String metodoClase = metodosClase.get(i);
            for (int j = 0; j < metodosInterfaz.size(); j++) {
                String metodoInterfaz = metodosInterfaz.get(j);
                if (metodoClase.equals(metodoInterfaz)) {
                    if (parametrosMetodos.get(i) != null && parametrosMetodosInterfaz.get(j) != null){
                        if (!parametrosMetodos.get(i).equals(parametrosMetodosInterfaz.get(j))) {
                            errores.add(new Error("El metodo " + metodoClase + " de la clase " + ambitoClase.substring(ambitoClase.lastIndexOf(":")+1, ambitoClase.length()) + " implementa el metodo con un parametro de tipo no correspondiente","ERROR"));
                            return;
                        }
                    } else if(parametrosMetodos.get(i) != null || parametrosMetodosInterfaz.get(j) != null){
                        errores.add(new Error("El metodo " + metodoClase + " de la clase " + ambitoClase.substring(ambitoClase.lastIndexOf(":")+1, ambitoClase.length()) + " implementa el metodo con un parametro no correspondiente","ERROR"));
                        return;
                    }
                }
            }
        }
        if(!metodosClase.containsAll(metodosInterfaz)){
            errores.add(new Error("No se implementaron todos los metodos de la interfaz " + nombreInterfaz.substring(nombreInterfaz.lastIndexOf(":")+1, nombreInterfaz.length()) + " en la clase " + ambitoClase.substring(ambitoClase.lastIndexOf(":")+1, ambitoClase.length()), "ERROR"));
            return;
        }
    }

    public ArrayList<String> getTiposParametros(String ambitoClase,ArrayList<String> metodos){
        ArrayList<String> params = new ArrayList<>();
        for (String metodo : metodos) {
            boolean encontro =false;
            for (String nombre : tabla.keySet()) {
                if (nombre.endsWith(ambitoClase + ":" + metodo)) { //:main:ab:metodo
                    System.out.println("Busco parametro: " + nombre);
                    if(tabla.get(nombre).isConParametro()){
                        params.add(tabla.get(nombre).getTipo());
                        encontro = true;
                    }
                } 
            }
            if(!encontro){
                params.add(null);
            }
        }
        return params;
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

    public String getAmbitoMetodoInvocado(String nombre, String ambito) {
        if (nombre.contains(".")) {
            String nombreMetodoInvocado = nombre.substring(nombre.lastIndexOf(".") + 1, nombre.length());
            nombre = nombre.substring(0,nombre.lastIndexOf("."));
            if (nombre.contains(".")) {
                return ":main:" + nombre.substring(nombre.lastIndexOf(".") + 1, nombre.length()) + ":" + nombreMetodoInvocado;
            } else {
                return ":main:" + tabla.get(nombre + ambito).getTipo() + ":" + nombreMetodoInvocado;
            }
        }
        boolean ambitoEncontrado = false;
        while (!ambitoEncontrado) {
            if (getMetodos(ambito).contains(nombre)) {
                ambitoEncontrado = true;
            } else {
                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
            }
        }
        return ambito + ":" + nombre;
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

    public String existeVariable(String nombre, String ambito){
        eliminarSimbolo(nombre);
        if(!nombre.contains(".")){
            while (ambito != ""){
                String nombreAmbito = nombre + ambito;
                if(existeSimbolo(nombreAmbito)){
                    String tipo = tabla.get(nombreAmbito).getTipo().toUpperCase();
                    if(!(tipo == "VOID" || tipo == "CLASS" || tipo == "INTERFACE" || tipo == "Long" || tipo == "Uint" || tipo == "Double" || tipo == "Cadena"))
                        return nombreAmbito;
                }
                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
            }
        } else {
            boolean encontrado = false;
            String nombreAux=nombre;
            while (ambito != "" && !encontrado){
                String nombreAmbito = nombre.substring(0,nombre.indexOf(".")) + ambito; //c.ab.x -> c:ambito
                eliminarSimbolo(nombre.substring(0,nombre.indexOf(".")));
                if(existeSimbolo(nombreAmbito)){
                    String clase = tabla.get(nombreAmbito).getTipo(); //c -> clase1
                    while(existeSimbolo(clase + ":main")){  //clase1:main -> true | false
                        String tipoClase = tabla.get(clase+":main").getTipo();
                        if(tipoClase == "CLASS"){ //si tipo:main es una clase
                            
                            nombreAux = nombreAux.substring(nombreAux.indexOf(".")+1,nombreAux.length()); //c.ab.x -> ab.x -> x

                            if(!nombreAux.contains(".")){ //busco variable de clase
                                eliminarSimbolo(nombreAux);
                                if(existeSimbolo(nombreAux + ":main:" + clase) && (tabla.get(nombreAux + ":main:" + clase).getTipo() != "VOID")){
                                    return nombre + nombreAmbito.substring(nombreAmbito.indexOf(":"),nombreAmbito.length());
                                }else{
                                    return "";
                                }
                            } else if (tabla.get(clase + ":main").getPadreClase() != ""){ //busco clase
                                clase = nombreAux.substring(0, nombreAux.indexOf(".")); //ab.x -> ab
                            } else { //c.xs.x
                                return "";
                            }
                        } else {
                            return "";
                        }
                    }

                    return "";
                }
                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
            }
        }
        return "";
    }

    public boolean existeMetodo(String nombre, String ambito, boolean conParametro){
        eliminarSimbolo(nombre);
        if(!nombre.contains(".")){
            while (ambito != ""){
                String nombreConAmbito = nombre + ambito;
                if(existeSimbolo(nombreConAmbito)){
                    String tipo = tabla.get(nombreConAmbito).getTipo();
                    tipo = tipo.toUpperCase();
                    if((tipo == "VOID") && conParametro && (tabla.get(nombreConAmbito).isConParametro() != conParametro)){
                        this.errores.add(new Error("No existe el metodo con parametro " + nombre,"ERROR"));
                    } else if((tipo == "VOID") && !conParametro && (tabla.get(nombreConAmbito).isConParametro() != conParametro)){
                        this.errores.add(new Error("No existe el metodo sin parametro " + nombre,"ERROR"));
                    }
                    return ((tipo == "VOID") && (tabla.get(nombreConAmbito).isConParametro() == conParametro));
                }
                ambito = ambito.substring(0,ambito.lastIndexOf(":"));
            }
        } else {
            boolean encontrado = false;
            while (ambito != "" && !encontrado){
                String nombreAmbito = nombre.substring(0,nombre.indexOf(".")) + ambito;
                eliminarSimbolo(nombre.substring(0,nombre.indexOf(".")));
                if(existeSimbolo(nombreAmbito)){
                    String clase = tabla.get(nombreAmbito).getTipo();
                    while(existeSimbolo(clase + ":main")){
                        String tipoClase = tabla.get(clase + ":main").getTipo();
                        if(tipoClase == "CLASS"){ 

                            nombre = nombre.substring(nombre.indexOf(".") + 1,nombre.length());
                            if(!nombre.contains(".")){ //busco variable de clase
                                eliminarSimbolo(nombre);

                                if(existeSimbolo(nombre + ":main:" + clase)){
                                    if((tabla.get(nombre + ":main:" + clase).getTipo() == "VOID") && conParametro && (tabla.get(nombre + ":main:" + clase).isConParametro() != conParametro)){
                                        this.errores.add(new Error("No existe el metodo con parametro " + nombre,"ERROR"));
                                    } else if((tabla.get(nombre + ":main:" + clase).getTipo() == "VOID") && !conParametro && (tabla.get(nombre + ":main:" + clase).isConParametro() != conParametro)){
                                        this.errores.add(new Error("No existe el metodo sin parametro " + nombre,"ERROR"));
                                    }
                                    return (tabla.get(nombre + ":main:" + clase).getTipo() == "VOID") && (tabla.get(nombre + ":main:" + clase).isConParametro() == conParametro);
                                }else{
                                    return false;
                                }
                            } else if (tabla.get(clase + ":main").getPadreClase() != ""){ //busco clase
                                clase = nombre.substring(0, nombre.indexOf("."));
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

    public boolean existeClase(String nombre){
        String nombreConAmbito = nombre + ":main";
        return ((tabla.containsKey(nombreConAmbito)) && (tabla.get(nombreConAmbito).getTipo() == "CLASS"));
    }

    public ArrayList<String> getAtributosClase(String ambito){
        ArrayList<String> atributos = new ArrayList<>();
        for (String id : tabla.keySet()) {
            if (id.endsWith(ambito)) {
                if (tabla.get(id).getTipo() != "VOID") {
                    atributos.add(id.substring(0,id.indexOf(":")));
                }
            }
        }
        return atributos;
    }

    public void addAtributosClase(String nombreClase, String instancia, String ambito){
        ArrayList<String> atributos = new ArrayList<>();
        String[] instancias = instancia.split(";");
        
        for (String variable : instancias) {
            String nombre = nombreClase + ":main";
            String ambitoClase = ":main:" + nombreClase;
            while (!nombre.equals("")) {
                atributos = getAtributosClase(ambitoClase);
                
                for (String atributoClase : atributos) {
                    Atributos atributo = tabla.get(atributoClase + ambitoClase);
                    tabla.put(variable + "." + atributoClase + ambito, atributo);
                }
                nombre = tabla.get(nombre).getPadreClase();
                
                if (!nombre.equals("")) {
                    ambitoClase = ":main:" + nombre.substring(0,nombre.indexOf(":"));
                    variable = variable + "." + nombre.substring(0,nombre.indexOf(":"));
                }
            }
        }
    }

    public ArrayList<String> getAtributosInstancia(String nombreInstancia, String ambito){
        ArrayList<String> atributos = new ArrayList<>();
        String claseHija = tabla.get(nombreInstancia.substring(0, nombreInstancia.length() - 1) + ambito).getTipo();

        for (String nombre : tabla.keySet()) {
            if (nombre.startsWith(nombreInstancia) && nombre.endsWith(ambito) && tabla.get(nombre).getTipo() != "VOID") {
                atributos.add(nombre);
                nombre = nombre.substring(0, nombre.lastIndexOf(":"));
                String[] instanciaClase = nombre.split("\\.");
                if (instanciaClase.length > 2){
                    atributos.add(instanciaClase[instanciaClase.length - 1] + ":main:" + instanciaClase[instanciaClase.length - 2]);
                } else {
                    atributos.add(instanciaClase[instanciaClase.length - 1] + ":main:" + claseHija);
                }
                atributos.add("=");
            }
        }
        return atributos;
    }

    public ArrayList<String> setAtributosInstancia(String nombreInstancia, String ambito){
        ArrayList<String> atributos = new ArrayList<>();
        String claseHija = tabla.get(nombreInstancia.substring(0, nombreInstancia.length() - 1) + ambito).getTipo();

        for (String nombre : tabla.keySet()) {
            if (nombre.startsWith(nombreInstancia) && nombre.endsWith(ambito) && tabla.get(nombre).getTipo() != "VOID") {
                String nombreAux = nombre;
                nombre = nombre.substring(0, nombre.lastIndexOf(":"));
                String[] instanciaClase = nombre.split("\\.");
                if (instanciaClase.length > 2){
                    atributos.add(instanciaClase[instanciaClase.length - 1] + ":main:" + instanciaClase[instanciaClase.length - 2]);
                } else {
                    atributos.add(instanciaClase[instanciaClase.length - 1] + ":main:" + claseHija);
                }
                atributos.add(nombreAux);
                atributos.add("=");
            }
        }
        return atributos;
    }

    public ArrayList<String> variablesNoUsadas(){
        ArrayList<String> aux = new ArrayList<>();
        tabla.forEach((k,v) -> {
            String tipo = v.getTipo();
            if(!(tipo == "VOID" || tipo == "CLASS" || tipo == "INTERFACE" || tipo == "Long" || tipo == "Uint" || tipo == "Double" || tipo == "Cadena")){
                if(!v.isUso()){
                    aux.add(k);
                }
            }
        });
        return aux;
    }

    public ArrayList<String> getSimbolos(){
        ArrayList<String> aux = new ArrayList<>();
        for (String key : tabla.keySet()) {
            aux.add(key);
        }
        return aux;
    }

    public void imprimirTabla() {
        System.out.println("Tabla de simbolos:");
        System.out.println("+----------------------------------+---------------+------------+-------+-----------+--------------+--------------+-------+");
        System.out.println("|              Nombre              |     Uso       |   Ambito   | Usado | Implement | Padre Heren. | Parametro de | Nivel |");
        System.out.println("+----------------------------------+---------------+------------+-------+-----------+--------------+--------------+-------+");
        for (String key : tabla.keySet()) {
            Atributos atributo = tabla.get(key);
            String nombre = key;
            String ambito = "";
            String uso = "";
            if(!(atributo.getTipo() == "VOID" || atributo.getTipo() == "CLASS" || atributo.getTipo() == "INTERFACE" || atributo.getTipo() == "Long" || atributo.getTipo() == "Uint" || atributo.getTipo() == "Double" || atributo.getTipo() == "Cadena")){
                if(atributo.isUso()){
                    uso = "si";
                } else {
                    uso = "no";
                }
            }
            if (atributo.getTipo().equals("Cadena") && key.length() > 25) {
                key = key.substring(0,25) + "...";
            }
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
            System.out.printf("| %-32s | %-13s | %-10s | %-5s | %-9s | %-12s | %-12s | %-5s |\n", key,atributo.getTipo(),ambito,uso,interfaz,atributo.getPadreClase(),atributo.getParametroDeFuncion(),atributo.getNivelHerencia());
        }
        System.out.println("+----------------------------------+---------------+------------+-------+-----------+--------------+--------------+-------+");
    }
}