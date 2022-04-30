grammar Jasm;

@header {
   package com.roscopeco.jasm.antlr;
}

class
 : modifier* CLASS classname extends? implements? (LBRACE member* RBRACE)?
 ;

classname
 : QNAME
 | NAME
 ;

extends
 : EXTENDS QNAME
 ;

implements
 : IMPLEMENTS QNAME QNAME*
 ;

member
 : field
 | method
 ;

field
 : modifier* membername type
 ;

method
 : modifier* membername LPAREN type* RPAREN type stat_block
 ;

membername
 : NAME
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
 | QNAME SEMI
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
 | insn_dup
 | insn_freturn
 | insn_goto
 | insn_iconst
 | insn_ifeq
 | insn_ifge
 | insn_ifgt
 | insn_ifle
 | insn_iflt
 | insn_ifne
 | insn_if_acmpeq
 | insn_if_acmpne
 | insn_if_icmpeq
 | insn_if_icmpge
 | insn_if_icmpgt
 | insn_if_icmple
 | insn_if_icmplt
 | insn_if_icmpne
 | insn_invokeinterface
 | insn_invokespecial
 | insn_invokestatic
 | insn_invokevirtual
 | insn_ireturn
 | insn_ldc
 | insn_new
 | insn_return
 | label
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

insn_dup
 : DUP
 ;

insn_freturn
 : FRETURN
 ;

insn_goto
 : GOTO NAME
 ;

insn_iconst
 : ICONST atom
 ;

insn_ifeq
 : IFEQ NAME
 ;

insn_ifge
 : IFGE NAME
 ;

insn_ifgt
 : IFGT NAME
 ;

insn_ifle
 : IFLE NAME
 ;

insn_iflt
 : IFLT NAME
 ;

insn_ifne
 : IFNE NAME
 ;

insn_if_acmpeq
 : IFACMPEQ NAME
 ;

insn_if_acmpne
 : IFACMPNE NAME
 ;

insn_if_icmpeq
 : IFICMPEQ NAME
 ;

insn_if_icmpge
 : IFICMPGE NAME
 ;

insn_if_icmpgt
 : IFICMPGT NAME
 ;

insn_if_icmple
 : IFICMPLE NAME
 ;

insn_if_icmplt
 : IFICMPLT NAME
 ;

insn_if_icmpne
 : IFICMPNE NAME
 ;

insn_invokeinterface
 : INVOKEINTERFACE owner DOT membername method_descriptor
 ;

insn_invokespecial
 : INVOKESPECIAL owner DOT membername method_descriptor
 ;

insn_invokestatic
 : INVOKESTATIC owner DOT membername method_descriptor
 ;

insn_invokevirtual
 : INVOKEVIRTUAL owner DOT membername method_descriptor
 ;

label
 : LABEL
 ;

owner
 : QNAME
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

insn_new
 : NEW QNAME
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

int_atom    : INT;
float_atom  : FLOAT;
bool_atom   : (TRUE | FALSE);
name_atom   : NAME;
string_atom: STRING;

LPAREN  : '(';
RPAREN  : ')';
LBRACE  : '{';
RBRACE  : '}';
DOT     : '.';
MINUS   : '-';
SEMI    : ';';

CLASS       : 'class';
EXTENDS     : 'extends';
IMPLEMENTS  : 'implements';

PRIVATE     : 'private';
PROTECTED   : 'protected';
PUBLIC      : 'public';
FINAL       : 'final';
SYNCHRONIZED: 'synchronized';
STATIC      : 'static';

INIT        : '<init>';
CLINIT      : '<clinit>';

ACONST_NULL     : 'aconstnull';
ALOAD           : 'aload';
ARETURN         : 'areturn';
DUP             : 'dup';
FRETURN         : 'freturn';
GOTO            : 'goto';
ICONST          : 'iconst';
IFEQ            : 'ifeq';
IFGT            : 'ifgt';
IFLE            : 'ifle';
IFLT            : 'iflt';
IFGE            : 'ifge';
IFNE            : 'ifne';
IFACMPEQ        : 'if_acmpeq';
IFACMPNE        : 'if_acmpne';
IFICMPEQ        : 'if_icmpeq';
IFICMPGT        : 'if_icmpgt';
IFICMPLE        : 'if_icmple';
IFICMPLT        : 'if_icmplt';
IFICMPGE        : 'if_icmpge';
IFICMPNE        : 'if_icmpne';
INVOKEINTERFACE : 'invokeinterface';
INVOKESPECIAL   : 'invokespecial';
INVOKESTATIC    : 'invokestatic';
INVOKEVIRTUAL   : 'invokevirtual';
IRETURN         : 'ireturn';
LDC             : 'ldc';
NEW             : 'new';
RETURN          : 'return';

TYPE_VOID   : 'V';
TYPE_INT    : 'I';
TYPE_LONG   : 'J';
TYPE_FLOAT  : 'F';
TYPE_DOUBLE : 'D';
TYPE_BOOL   : 'Z';

TRUE    : 'true';
FALSE   : 'false';

LABEL
 : [a-zA-Z_] [a-zA-Z_0-9]* ':'
 ;

NAME
 : [a-zA-Z_] [a-zA-Z_0-9]*
 ;

QNAME
 : [a-zA-Z_] [a-zA-Z_0-9/]*
 ;

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
