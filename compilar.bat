@echo off
echo Compilando Parser.java...

javac .\Sintactico\Parser.java

if errorlevel 1 (
    echo Error de compilacion. No se pudo compilar Parser.java.
) else (
    echo Compilacion exitosa. Ejecutando el programa...
    java .\Sintactico\Parser.java ./Code.txt
)

