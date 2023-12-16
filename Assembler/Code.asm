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
u@main@c@f6 DD ?
d@main@b@f7 DQ ?
s@main@c@f4 DW ?
t_y@main@b DQ ?
t_z@main@b DQ ?
t_x@main@b DQ ?
h@main@a@f1 DW ?
s@main@b@f4 DW ?
@@0_15E1 DQ -0.15E1
x@main@a DQ ?
r_z@main@b DQ ?
r_y@main@b DQ ?
d@main@c@f5 DQ ?
r_x@main@b DQ ?
y@main@a DQ ?
z@main@a DQ ?
u@main@b@f6 DD ?
@0_15E1 DQ 0.15E1
a@main@b@f5 DW ?
.code
FINIT


@main@a@f1:
MOV AX, 4
MOV h@main@a@f1, AX
RET

@main@b@f6:
MOV EAX, -1
MOV u@main@b@f6, EAX
RET

@main@b@f5:
MOV AX, 3
MOV a@main@b@f5, AX
RET

@main@b@f4:
MOV AX, 2
MOV s@main@b@f4, AX
RET

@main@b@f7:
FLD @@0_15E1
FSTP d@main@b@f7
RET

MAIN:
FINIT
invoke ExitProcess, 0
END MAIN
