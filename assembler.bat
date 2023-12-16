@echo off
cd .\Assembler\
\masm32\bin\ml /c /Zd /coff Code.asm
\masm32\bin\Link /SUBSYSTEM:CONSOLE Code.obj
.\Code.exe