grammar Jasm;

@header {
   package com.roscopeco.jasm.antlr;
}

class
 : type_modifier* CLASS classname extends? implements? (LBRACE member* RBRACE)?
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
 : field_modifier* membername type
 ;

method
 : method_modifier* membername method_descriptor stat_block
 ;

method_descriptor
 : LPAREN method_arguments COMMA? RPAREN type
 ;

method_arguments
 : (method_argument (COMMA method_argument)*)?
 ;

method_argument
 : prim_type
 | ref_type
 | array_type
 ;

membername
 : NAME
 | INIT
 | CLINIT
 ;

type
 : void_type
 | prim_type
 | ref_type
 | array_type
 ;

void_type
 : TYPE_VOID
 ;

array_type
 : LSQUARE+ prim_type
 | LSQUARE+ ref_type
 ;

prim_type
 : TYPE_BYTE
 | TYPE_CHAR
 | TYPE_INT
 | TYPE_LONG
 | TYPE_FLOAT
 | TYPE_DOUBLE
 | TYPE_SHORT
 | TYPE_BOOL
 ;

ref_type
 : QNAME
 | NAME
 ;

type_modifier
 : ABSTRACT
 | ANNOTATION
 | DEPRECATED
 | ENUM
 | FINAL
 | INTERFACE
 | PUBLIC
 | RECORD
 | SYNTHETIC
 | SUPER
 ;

field_modifier
 : DEPRECATED
 | ENUM
 | FINAL
 | PRIVATE
 | PROTECTED
 | PUBLIC
 | STATIC
 | SYNTHETIC
 | TRANSIENT
 | VOLATILE
 ;

method_modifier
 : ABSTRACT
 | BRIDGE
 | DEPRECATED
 | FINAL
 | NATIVE
 | PRIVATE
 | PROTECTED
 | PUBLIC
 | STATIC
 | STRICT
 | SYNCHRONIZED
 | SYNTHETIC
 | VARARGS
 ;

stat_block
 : LBRACE stat* RBRACE
 ;

stat
 : instruction
 | OTHER {System.err.println("unknown char: " + $OTHER.text);}
 ;

instruction
 : insn_aaload
 | insn_aastore
 | insn_aconst_null
 | insn_aload
 | insn_anewarray
 | insn_areturn
 | insn_arraylength
 | insn_astore
 | insn_athrow
 | insn_baload
 | insn_bastore
 | insn_bipush
 | insn_caload
 | insn_castore
 | insn_checkcast
 | insn_daload
 | insn_dastore
 | insn_dload
 | insn_dreturn
 | insn_dstore
 | insn_dup
 | insn_faload
 | insn_fastore
 | insn_fload
 | insn_freturn
 | insn_fstore
 | insn_goto
 | insn_iaload
 | insn_iastore
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
 | insn_iload
 | insn_invokedynamic
 | insn_invokeinterface
 | insn_invokespecial
 | insn_invokestatic
 | insn_invokevirtual
 | insn_ireturn
 | insn_istore
 | insn_laload
 | insn_lastore
 | insn_ldc
 | insn_lload
 | insn_lreturn
 | insn_lstore
 | insn_new
 | insn_return
 | label
 ;

insn_aaload
 : AALOAD
 ;

insn_aastore
 : AASTORE
 ;

insn_aconst_null
 : ACONST_NULL
 ;

insn_aload
 : ALOAD int_atom
 ;

insn_anewarray
 : ANEWARRAY QNAME
 ;

insn_areturn
 : ARETURN
 ;

insn_arraylength
 : ARRAYLENGTH
 ;

insn_astore
 : ASTORE int_atom
 ;

insn_athrow
 : ATHROW
 ;

insn_baload
 : BALOAD
 ;

insn_bastore
 : BASTORE
 ;

insn_bipush
 : BIPUSH int_atom
 ;

insn_caload
 : CALOAD
 ;

insn_castore
 : CASTORE
 ;

insn_checkcast
 : CHECKCAST QNAME
 ;

insn_daload
 : DALOAD
 ;

insn_dastore
 : DASTORE
 ;

insn_dload
 : DLOAD int_atom
 ;

insn_dreturn
 : DRETURN
 ;

insn_dstore
 : DSTORE int_atom
 ;

insn_dup
 : DUP
 ;

insn_faload
 : FALOAD
 ;

insn_fastore
 : FASTORE
 ;

insn_fload
 : FLOAD int_atom
 ;

insn_freturn
 : FRETURN
 ;

insn_fstore
 : FSTORE int_atom
 ;

insn_goto
 : GOTO NAME
 ;

insn_iaload
 : IALOAD
 ;

insn_iastore
 : IASTORE
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

insn_iload
 : ILOAD int_atom
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

insn_ireturn
 : IRETURN
 ;

insn_istore
 : ISTORE int_atom
 ;

insn_laload
 : LALOAD
 ;

insn_lastore
 : LASTORE
 ;

insn_ldc
 : LDC const_arg
 ;

insn_lload
 : LLOAD int_atom
 ;

insn_lreturn
 : LRETURN
 ;

insn_lstore
 : LSTORE int_atom
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

ABSTRACT    : 'abstract';
ANNOTATION  : 'annotation';
BRIDGE      : 'bridge';
DEPRECATED  : 'deprecated';
ENUM        : 'enum';
FINAL       : 'final';
INTERFACE   : 'interface';
NATIVE      : 'native';
PRIVATE     : 'private';
PROTECTED   : 'protected';
PUBLIC      : 'public';
RECORD      : 'record';
STATIC      : 'static';
STRICT      : 'strict';
SUPER       : 'super';
SYNCHRONIZED: 'synchronized';
SYNTHETIC   : 'synthetic';
TRANSIENT   : 'transient';
VARARGS     : 'varargs';
VOLATILE    : 'volatile';

INIT        : '<init>';
CLINIT      : '<clinit>';

AALOAD          : 'aaload';
AASTORE         : 'aastore';
ACONST_NULL     : 'aconst_null';
ALOAD           : 'aload';
ANEWARRAY       : 'anewarray';
ARETURN         : 'areturn';
ARRAYLENGTH     : 'arraylength';
ASTORE          : 'astore';
ATHROW          : 'athrow';
BALOAD          : 'baload';
BASTORE         : 'bastore';
BIPUSH          : 'bipush';
CALOAD          : 'caload';
CASTORE         : 'castore';
CHECKCAST       : 'checkcast';
DALOAD          : 'daload';
DASTORE         : 'dastore';
DLOAD           : 'dload';
DRETURN         : 'dreturn';
DSTORE          : 'dstore';
DUP             : 'dup';
FALOAD          : 'faload';
FASTORE         : 'fastore';
FLOAD           : 'fload';
FRETURN         : 'freturn';
FSTORE          : 'fstore';
GETFIELD        : 'getfield';
GETSTATIC       : 'getstatic';
GOTO            : 'goto';
IALOAD          : 'iaload';
IASTORE         : 'iastore';
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
ILOAD           : 'iload';
INVOKEDYNAMIC   : 'invokedynamic';
INVOKEINTERFACE : 'invokeinterface';
INVOKESPECIAL   : 'invokespecial';
INVOKESTATIC    : 'invokestatic';
INVOKEVIRTUAL   : 'invokevirtual';
IRETURN         : 'ireturn';
ISTORE          : 'istore';
LALOAD          : 'laload';
LASTORE         : 'lastore';
LDC             : 'ldc';
LLOAD           : 'lload';
LRETURN         : 'lreturn';
LSTORE          : 'lstore';
NEW             : 'new';
PUTFIELD        : 'putfield';
PUTSTATIC       : 'putstatic';
RETURN          : 'return';

NEWINVOKESPECIAL: 'newinvokespecial';
CONSTDYNAMIC    : 'constdynamic';

TYPE_VOID
 : 'V'
 | 'void'
 ;

TYPE_BYTE
 : 'B'
 | 'byte'
 ;

TYPE_CHAR
 : 'C'
 | 'char'
 ;

TYPE_DOUBLE
 : 'D'
 | 'double'
 ;

TYPE_FLOAT
 : 'F'
 | 'float'
 ;

TYPE_INT
 : 'I'
 | 'int'
 ;

TYPE_LONG
 : 'J'
 | 'long'
 ;

TYPE_SHORT
 : 'S'
 | 'short'
 ;

TYPE_BOOL
 : 'Z'
 | 'boolean'
 ;

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
