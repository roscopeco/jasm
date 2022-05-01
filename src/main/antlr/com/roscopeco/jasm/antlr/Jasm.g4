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
 : modifier* membername LPAREN argument_type* RPAREN type stat_block
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
 | ref_type
 | prim_array_type
 | ref_array_type
 ;

argument_type
 : TYPE_VOID
 | TYPE_INT
 | TYPE_LONG
 | TYPE_FLOAT
 | TYPE_DOUBLE
 | TYPE_BOOL
 | ref_type
 | prim_array_type SEMI
 | ref_array_type
 ;

ref_type
 : QNAME SEMI
 ;

prim_array_type
 : LSQUARE+ TYPE_INT
 | LSQUARE+ TYPE_LONG
 | LSQUARE+ TYPE_FLOAT
 | LSQUARE+ TYPE_DOUBLE
 | LSQUARE+ TYPE_BOOL
 ;

ref_array_type
 : LSQUARE+ QNAME SEMI
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
 : insn_aconst_null
 | insn_aload
 | insn_areturn
 | insn_athrow
 | insn_checkcast
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
 | insn_ifnull
 | insn_ifnonnull
 | insn_invokedynamic
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

insn_aconst_null
 : ACONST_NULL
 ;

insn_aload
 : ALOAD atom
 ;

insn_areturn
 : ARETURN
 ;

insn_athrow
 : ATHROW
 ;

insn_checkcast
 : CHECKCAST QNAME
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

insn_ifnull
 : IFNULL NAME
 ;

insn_ifnonnull
 : IFNONNULL NAME
 ;

insn_invokedynamic
 : INVOKEDYNAMIC membername method_descriptor LBRACE method_handle (LSQUARE const_arg (COMMA const_arg)* RSQUARE)? RBRACE
 ;

method_handle
 : handle_tag bootstrap_spec
 ;

bootstrap_spec
 : owner DOT membername method_descriptor
 ;

handle_tag
 : INVOKEINTERFACE
 | INVOKESPECIAL
 | INVOKESTATIC
 | INVOKEVIRTUAL
 | NEWINVOKESPECIAL
 | GETFIELD
 | GETSTATIC
 | PUTFIELD
 | PUTSTATIC
 ;

const_arg
 : int_atom
 | float_atom
 | string_atom
 | bool_atom
 | QNAME
 | method_descriptor
 | method_handle
 | constdynamic
 ;

constdynamic
 : CONSTDYNAMIC membername type LBRACE method_handle (LSQUARE const_arg (COMMA const_arg)* RSQUARE)? RBRACE
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
 : LDC const_arg
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
LSQUARE : '[';
RSQUARE : ']';
DOT     : '.';
MINUS   : '-';
SEMI    : ';';
COMMA   : ',';

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

ACONST_NULL     : 'aconst_null';
ALOAD           : 'aload';
ARETURN         : 'areturn';
ATHROW          : 'athrow';
CHECKCAST       : 'checkcast';
DUP             : 'dup';
FRETURN         : 'freturn';
GETFIELD        : 'getfield';
GETSTATIC       : 'getstatic';
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
IFNULL          : 'ifnull';
IFNONNULL       : 'ifnonnull';
INVOKEDYNAMIC   : 'invokedynamic';
INVOKEINTERFACE : 'invokeinterface';
INVOKESPECIAL   : 'invokespecial';
INVOKESTATIC    : 'invokestatic';
INVOKEVIRTUAL   : 'invokevirtual';
IRETURN         : 'ireturn';
LDC             : 'ldc';
NEW             : 'new';
PUTFIELD        : 'putfield';
PUTSTATIC       : 'putstatic';
RETURN          : 'return';

NEWINVOKESPECIAL: 'newinvokespecial';
CONSTDYNAMIC    : 'constdynamic';

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
 : [a-zA-Z_$] [a-zA-Z_$0-9]*
 ;

QNAME
 : [a-zA-Z_$] [a-zA-Z_$0-9/]*
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
 : . -> channel(HIDDEN)
 ;
