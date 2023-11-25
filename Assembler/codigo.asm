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
@newline db 10, 0
@aux_2_bytes DW ? 
@ERROR_RESULT_NEGATIVO_UINT DB "ERROR: resultado negativo al restar dos enteros sin signo", 0
@ERROR_OVERFLOW_SUMA_LONG DB "ERROR: se produjo overflow al sumar dos enteros", 0
@ERROR_OVERFLOW_MUL_DOUBLE DB "ERROR: se produjo overflow al momento de multiplicar dos datos de punto flotante", 0
c@main DQ ?
a@main DW ?
@0_1E101 DQ 0.1E101
@0_16E101 DQ 0.16E101
@aux2 DQ ?
b@main DD ?
.code
FINIT


MAIN:
FLD @0_16E101
FMUL @0_1E101
FSTSW @aux_2_bytes
MOV AX, @aux_2_bytes
SAHF
JNO @aux1
invoke MessageBox, NULL, addr @ERROR_OVERFLOW_MUL_DOUBLE, addr @ERROR_RESULT_NEGATIVO_UINT, MB_OK
invoke ExitProcess, 0
@aux1:
FSTP @aux2
FLD @aux2
FSTP c@main
FINIT
invoke ExitProcess, 0
END MAIN
