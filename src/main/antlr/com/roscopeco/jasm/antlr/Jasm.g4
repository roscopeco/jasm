grammar Jasm;

@header {
   package com.roscopeco.jasm.antlr;
}

class
 : modifier* CLASS classname (LBRACE member* RBRACE)?
 ;

classname
 : TYPENAME
 | NAME
 ;

member
 : field
 | method
 ;

field
 : modifier* type membername
 ;

method
 : modifier* type membername LPAREN type* RPAREN stat_block
 ;

membername
 : TYPENAME
 | NAME
 | INIT
 | CLINIT
 ;

type
 : TYPE_VOID
 | TYPE_INT
 | TYPE_LONG
 | TYPE_FLOAT
 | TYPE_DOUBLE
 | TYPE_BOOL
 | TYPENAME SEMI
 ;

modifier
 : PRIVATE
 | PROTECTED
 | PUBLIC
 | FINAL
 | SYNCHRONIZED
 | STATIC
 ;

stat_block
 : LBRACE stat* RBRACE
 ;

stat
 : instruction
 | OTHER {System.err.println("unknown char: " + $OTHER.text);}
 ;

instruction
 : insn_aconstnull
 | insn_aload
 | insn_areturn
 | insn_freturn
 | insn_iconst
 | insn_invokespecial
 | insn_ireturn
 | insn_ldc
 | insn_return
 ;

insn_aconstnull
 : ACONST_NULL
 ;

insn_aload
 : ALOAD atom
 ;

insn_areturn
 : ARETURN
 ;

insn_freturn
 : FRETURN
 ;

insn_iconst
 : ICONST atom
 ;

insn_invokespecial
 : INVOKESPECIAL owner DOT membername method_descriptor
 ;

owner
 : TYPENAME
 | NAME
 ;

method_descriptor
 : LPAREN (type (SEMI type)*)* RPAREN type
 ;

insn_ireturn
 : IRETURN
 ;

insn_ldc
 : LDC atom
 ;

insn_return
 : RETURN
 ;

atom
 : int_atom
 | float_atom
 | bool_atom
 | name_atom
 | string_atom
 ;

int_atom: INT;
float_atom: FLOAT;
bool_atom: (TRUE | FALSE);
name_atom: NAME;
string_atom: STRING;

CLASS  : 'class';
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';

PRIVATE: 'private';
PROTECTED: 'protected';
PUBLIC: 'public';
FINAL: 'final';
SYNCHRONIZED: 'synchronized';
STATIC: 'static';

INIT: '<init>';
CLINIT: '<clinit>';

ACONST_NULL: 'aconstnull';
ALOAD: 'aload';
ARETURN: 'areturn';
FRETURN: 'freturn';
ICONST: 'iconst';
INVOKESPECIAL: 'invokespecial';
IRETURN: 'ireturn';
LDC: 'ldc';
RETURN: 'return';

TYPE_VOID: 'V';
TYPE_INT: 'I';
TYPE_LONG: 'J';
TYPE_FLOAT: 'F';
TYPE_DOUBLE: 'D';
TYPE_BOOL: 'Z';

TRUE : 'true';
FALSE : 'false';

NAME
 : [a-zA-Z_] [a-zA-Z_0-9]*
 ;

TYPENAME
 : [a-zA-Z_] [a-zA-Z_0-9/]*
 ;

DOT: '.';
MINUS: '-';
SEMI : ';';

INT
 : MINUS? [0-9]+
 ;

FLOAT
 : MINUS? [0-9]+ '.' [0-9]*
 | MINUS? '.' [0-9]+
 ;

STRING
 : '"' (~["\r\n] | '""')* '"'
 ;

COMMENT
 : '//' ~[\r\n]* -> skip
 ;

SPACE
 : [ \t\r\n] -> skip
 ;

OTHER
 : .
 ;
