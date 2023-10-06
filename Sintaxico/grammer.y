program : '{' declaration_list '}'

declaration_list : declaration
              | declaration_list declaration

declaration : statement
          | class_declaration
          | fun_declaration

class_declaration : CLASS ID '{' class_body '}'

class_body : class_member
           | class_body class_member

class_member : attribute_declaration
             | method_declaration

attribute_declaration : TYPE ID ','

method_declaration : TYPE ID '(' param_list ')' '{' method_body '}'

param_list : param
           | param_list ',' param

param : TYPE ID

method_body : method_member
            | method_body method_member

method_member : attribute_declaration
              | function_declaration

function_declaration : TYPE ID '(' param_list ')' compound_stmt

compound_stmt : '{' stmt_list '}'

stmt_list : stmt
        | stmt_list stmt

stmt : expr_stmt
    | compound_stmt
    | selection_stmt
    | iteration_stmt
    | return_stmt
    | print_stmt

print_stmt : PRINT string

expr_stmt : expr ';'

selection_stmt : IF '(' expr ')' stmt
            | IF '(' expr ')' stmt ELSE stmt

iteration_stmt : DO stmt WHILE '(' expr ')' ';'

return_stmt : RETURN expr ';'

expr : ID '=' expr
    | ID '(' args ')'
    | expr '+' expr
    | expr '-' expr
    | expr '*' expr
    | expr '/' expr
    | expr '--'
    | ID
    | NUM

args : expr
    | args ',' expr



statement : var_declaration , 

var_declaration :  TYPE ID ','
                | TYPE ID ';' var_declaration

fun_declaration : TYPE ID '(' param ')' compound_stmt

param : TYPE ID

compound_stmt : '{' stmt_list '}'

stmt_list : stmt
        | stmt_list stmt

stmt : expr_stmt
    | compound_stmt
    | selection_stmt
    | iteration_stmt
    | return_stmt
    | print_stmt

print_stmt : PRINT string

expr_stmt : expr ','

selection_stmt : IF '(' expr ')' stmt
            | IF '(' expr ')' stmt ELSE stmt

iteration_stmt : DO stmt WHILE '(' expr ')' ','

return_stmt : RETURN expr ';'

expr : ID '=' expr
    | ID '(' args ')'
    | expr '+' expr
    | expr '-' expr
    | expr '*' expr
    | expr '/' expr
    | expr '--'
    | ID
    | NUM

args : expr
    | args ',' expr
