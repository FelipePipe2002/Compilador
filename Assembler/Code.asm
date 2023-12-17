.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
include \masm32\include\masm32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
includelib \masm32\lib\masm32.lib
.data
@newline DB 10, 0
@aux_2_bytes DW ? 
@ERROR_RESULT_NEGATIVO_UINT DB "ERROR: resultado negativo al restar dos enteros sin signo", 0
@ERROR_OVERFLOW_SUMA_LONG DB "ERROR: se produjo overflow al sumar dos enteros", 0
@ERROR_OVERFLOW_MUL_DOUBLE DB "ERROR: se produjo overflow al momento de multiplicar dos datos de punto flotante", 0
@overflow_punto_flotante DQ 0.17976931348623157E309
@aux2 DB "Edad pedro correcta",0
@aux3 DB "Edad pedro incorrecta",0
@aux4 DB "Edad juan correcta",0
@aux5 DB "Edad juan incorrecta",0
juan_edad@main DW ?
pedro_mes_nac@main DW ?
mes_nac@main@persona DW ?
pedro_edad@main DW ?
juan_mes_nac@main DW ?
edad@main@persona DW ?
@aux1 DW ?
.code
FINIT


@main@persona@actualizar:
MOV CX, edad@main@persona
ADD CX, 1
MOV @aux1, CX
MOV AX, @aux1
MOV edad@main@persona, AX
RET

MAIN:
MOV AX, 20
MOV pedro_edad@main, AX
MOV AX, 40
MOV juan_edad@main, AX
MOV AX, pedro_mes_nac@main
MOV mes_nac@main@persona, AX
MOV AX, pedro_edad@main
MOV edad@main@persona, AX
CALL @main@persona@actualizar
MOV AX, mes_nac@main@persona
MOV pedro_mes_nac@main, AX
MOV AX, edad@main@persona
MOV pedro_edad@main, AX
MOV AX, juan_edad@main
MOV edad@main@persona, AX
MOV AX, juan_mes_nac@main
MOV mes_nac@main@persona, AX
CALL @main@persona@actualizar
MOV AX, edad@main@persona
MOV juan_edad@main, AX
MOV AX, mes_nac@main@persona
MOV juan_mes_nac@main, AX
MOV CX, pedro_edad@main
CMP CX, 21
JNE @main_41
invoke StdOut, addr @aux2
invoke StdOut, addr @newline 
JMP @main_44
@main_41:
invoke StdOut, addr @aux3
invoke StdOut, addr @newline 
@main_44:
MOV CX, juan_edad@main
CMP CX, 41
JNE @main_54
invoke StdOut, addr @aux4
invoke StdOut, addr @newline 
JMP @main_57
@main_54:
invoke StdOut, addr @aux5
invoke StdOut, addr @newline 
@main_57:
FINIT
invoke ExitProcess, 0
END MAIN
