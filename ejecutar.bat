@echo off
cls
cd .\Sintactico\
.\yacc.exe -J .\gramatica.y
cd ..
set "line=package Sintactico;"
set "file=.\Sintactico\ParserVal.java"

(
  echo %line%
  type %file%
) > %file%.tmp

move /y %file%.tmp %file%
echo Compilando...
javac .\Lexico\*.java
javac .\Sintactico\*.java

if errorlevel 1 (
    echo Error de compilacion. No se pudo compilar Parser.java.
) else (
    echo Compilacion exitosa. Ejecutando el programa...
    java .\Sintactico\Parser.java ./Code.txt
)

