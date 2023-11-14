@echo off
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