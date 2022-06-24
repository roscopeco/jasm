grammar JvmTypes;

@header {
   package com.roscopeco.jasm.antlr;
}

method_descriptor
 : LPAREN param* RPAREN return
 ;

param
 : type
 ;

return
 : type
 ;

type
 : prim_type
 | ref_type
 | VOID
 ;

prim_type
 : ARRAY* BOOL
 | ARRAY* BYTE
 | ARRAY* CHAR
 | ARRAY* FLOAT
 | ARRAY* DOUBLE
 | ARRAY* INT
 | ARRAY* LONG
 ;

ref_type
 : ARRAY* REF
 ;

LPAREN: '(';
RPAREN: ')';
SEMI  : ';';

ARRAY : '[';

BOOL  : 'Z';
BYTE  : 'B';
CHAR  : 'C';
DOUBLE: 'D';
FLOAT : 'F';
INT   : 'I';
LONG  : 'J';
VOID  : 'V';
REF   : 'L' ~(';')+ SEMI;
