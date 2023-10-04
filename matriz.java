public abstract class Matriz {
    
    public int reconocedor(char c) { //reconoce que caracter es y retorna la posicion de la matriz para ese caracter
        String caracter = String.valueOf(c);
        switch(c){
            case '_':
                return 3;
            case '.':
                return 4;
            case 'd':
                return 5;
            case 'D':
                return 6;
            case '+':
                return 7;
            case '-':
                return 8;
            case 'u':
                return 9;
            case 'i':
                return 10;
            case 'l':
                return 11;
            case '{':
                return 12;
            case '}':
                return 13;
            case '(':
                return 14;
            case ')':
                return 15;
            case ',':
                return 16;
            case ';':
                return 17;
            case '=':
                return 18;
            case '>':
                return 19;
            case '<':
                return 20;
            case '!':
                return 21;
            case '%':
                return 22;
            case '*':
                return 23;
            case ' ':
                return 24;
            case '\t':
                return 25;
            case '\n':
                return 26;
            default:
                if((int)c == 13 || (int)c == 10){
                    return 26;
                } else if(caracter.matches("[a-z]")){
                    return 0;
                } else if(caracter.matches("[A-Z]")){
                    return 1;
                } else if(caracter.matches("[0-9]")){
                    return 2;
                }
                break;
        }
        return -1;
    }
}
