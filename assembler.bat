@echo off
cd .\Assembler\
\masm32\bin\ml /c /Zd /coff codigo.asm
\masm32\bin\Link /SUBSYSTEM:CONSOLE codigo.obj
.\codigo.exe