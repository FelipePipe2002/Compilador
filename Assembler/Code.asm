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
c@main DQ ?
@0_1E20 DQ 0.1E20
@0_1E3 DQ 0.1E3
@aux2 DQ ?
.code
FINIT


MAIN:
FLD @0_1E3
FLD @0_1E20
FMUL
FLD @overflow_punto_flotante
FCOM
FSTSW @aux_2_bytes
MOV AX, @aux_2_bytes
SAHF
JA @aux1
invoke MessageBox, NULL, addr @ERROR_OVERFLOW_MUL_DOUBLE, addr @ERROR_RESULT_NEGATIVO_UINT, MB_OK
invoke ExitProcess, 0
@aux1:
FSTP @aux2
FLD @aux2
FSTP c@main
FINIT
invoke ExitProcess, 0
END MAIN
