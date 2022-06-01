grammar Jasm;

@header {
   package com.roscopeco.jasm.antlr;
}

class
 : type_modifier* CLASS classname extends? implements? (LBRACE classbody RBRACE)?
 ;

classname
 : QNAME
 | NAME
 ;

extends
 : EXTENDS classname
 ;

implements
 : IMPLEMENTS classname (COMMA? classname)*
 ;

classbody
 : member*
 ;

member
 : field
 | method
 ;

field
 : field_modifier* membername type (EQUALS field_initializer)?
 ;

field_initializer
 : int_atom
 | float_atom
 | string_atom
 ;

method
 : method_modifier* membername method_descriptor (LBRACE stat_block RBRACE)?
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
 | AALOAD
 | AASTORE
 | ACONST_NULL
 | ALOAD
 | ANEWARRAY
 | ARETURN
 | ARRAYLENGTH
 | ASTORE
 | ATHROW
 | BALOAD
 | BASTORE
 | BIPUSH
 | CALOAD
 | CASTORE
 | CHECKCAST
 | CONSTDYNAMIC
 | D2F
 | D2I
 | D2L
 | DADD
 | DALOAD
 | DASTORE
 | DCMPG
 | DCMPG
 | DCONST
 | DDIV
 | DLOAD
 | DMUL
 | DNEG
 | DREM
 | DRETURN
 | DSTORE
 | DSUB
 | DUP
 | DUP_X1
 | DUP_X2
 | DUP2
 | DUP2_X1
 | DUP2_X2
 | F2D
 | F2I
 | F2L
 | FADD
 | FALOAD
 | FASTORE
 | FCMPG
 | FCMPG
 | FCONST
 | FDIV
 | FLOAD
 | FMUL
 | FNEG
 | FREM
 | FRETURN
 | FSUB
 | FSTORE
 | GETFIELD
 | GETSTATIC
 | GOTO
 | I2B
 | I2C
 | I2D
 | I2F
 | I2L
 | I2S
 | IADD
 | IALOAD
 | IAND
 | IASTORE
 | ICONST
 | IDIV
 | IFACMPEQ
 | IFACMPNE
 | IFICMPEQ
 | IFICMPGE
 | IFICMPGT
 | IFICMPLE
 | IFICMPLT
 | IFICMPNE
 | IFEQ
 | IFGE
 | IFGT
 | IFLE
 | IFLT
 | IFNE
 | IFNULL
 | IFNONNULL
 | IINC
 | ILOAD
 | IMUL
 | INEG
 | INSTANCEOF
 | INVOKEDYNAMIC
 | INVOKEINTERFACE
 | INVOKESPECIAL
 | INVOKESTATIC
 | INVOKEVIRTUAL
 | NEWINVOKESPECIAL
 | IOR
 | IREM
 | IRETURN
 | ISHL
 | ISHR
 | ISTORE
 | ISUB
 | IUSHR
 | IXOR
 | JSR
 | L2D
 | L2F
 | L2I
 | LADD
 | LALOAD
 | LAND
 | LASTORE
 | LCMP
 | LCONST
 | LDC
 | LDIV
 | LLOAD
 | LMUL
 | LNEG
 | LOOKUPSWITCH
 | LOR
 | LREM
 | LRETURN
 | LSHL
 | LSHR
 | LSTORE
 | LSUB
 | LUSHR
 | LXOR
 | MONITORENTER
 | MONITOREXIT
 | MULTIANEWARRAY
 | NEW
 | NEWARRAY
 | NOP
 | POP
 | POP2
 | PUTFIELD
 | PUTSTATIC
 | RETURN
 | RET
 | SALOAD
 | SASTORE
 | SIPUSH
 | SWAP
 | TABLESWITCH
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
 : stat*
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
 | insn_d2f
 | insn_d2i
 | insn_d2l
 | insn_dadd
 | insn_daload
 | insn_dastore
 | insn_dcmpg
 | insn_dcmpl
 | insn_dconst
 | insn_ddiv
 | insn_dload
 | insn_dmul
 | insn_dneg
 | insn_drem
 | insn_dreturn
 | insn_dstore
 | insn_dsub
 | insn_dup
 | insn_dup_x1
 | insn_dup_x2
 | insn_dup2
 | insn_dup2_x1
 | insn_dup2_x2
 | insn_f2d
 | insn_f2i
 | insn_f2l
 | insn_fadd
 | insn_faload
 | insn_fastore
 | insn_fcmpg
 | insn_fcmpl
 | insn_fconst
 | insn_fdiv
 | insn_fload
 | insn_fmul
 | insn_fneg
 | insn_frem
 | insn_freturn
 | insn_fsub
 | insn_fstore
 | insn_getfield
 | insn_getstatic
 | insn_goto
 | insn_i2b
 | insn_i2c
 | insn_i2d
 | insn_i2f
 | insn_i2l
 | insn_i2s
 | insn_iadd
 | insn_iaload
 | insn_iand
 | insn_iastore
 | insn_iconst
 | insn_idiv
 | insn_if_acmpeq
 | insn_if_acmpne
 | insn_if_icmpeq
 | insn_if_icmpge
 | insn_if_icmpgt
 | insn_if_icmple
 | insn_if_icmplt
 | insn_if_icmpne
 | insn_ifeq
 | insn_ifge
 | insn_ifgt
 | insn_ifle
 | insn_iflt
 | insn_ifne
 | insn_ifnull
 | insn_ifnonnull
 | insn_iinc
 | insn_iload
 | insn_imul
 | insn_ineg
 | insn_instanceof
 | insn_invokedynamic
 | insn_invokeinterface
 | insn_invokespecial
 | insn_invokestatic
 | insn_invokevirtual
 | insn_ior
 | insn_irem
 | insn_ireturn
 | insn_ishl
 | insn_ishr
 | insn_istore
 | insn_isub
 | insn_iushr
 | insn_ixor
 | insn_jsr
 | insn_l2d
 | insn_l2f
 | insn_l2i
 | insn_ladd
 | insn_laload
 | insn_land
 | insn_lastore
 | insn_lcmp
 | insn_lconst
 | insn_ldc
 | insn_ldiv
 | insn_lload
 | insn_lmul
 | insn_lneg
 | insn_lookupswitch
 | insn_lor
 | insn_lrem
 | insn_lreturn
 | insn_lshl
 | insn_lshr
 | insn_lstore
 | insn_lsub
 | insn_lushr
 | insn_lxor
 | insn_monitorenter
 | insn_monitorexit
 | insn_multianewarray
 | insn_new
 | insn_newarray
 | insn_nop
 | insn_pop
 | insn_pop2
 | insn_putfield
 | insn_putstatic
 | insn_return
 | insn_ret
 | insn_saload
 | insn_sastore
 | insn_sipush
 | insn_swap
 | insn_tableswitch
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

insn_d2f
 : D2F
 ;

insn_d2i
 : D2I
 ;

insn_d2l
 : D2L
 ;

insn_dadd
 : DADD
 ;

insn_daload
 : DALOAD
 ;

insn_dastore
 : DASTORE
 ;

insn_dcmpg
 : DCMPG
 ;

insn_dcmpl
 : DCMPL
 ;

insn_dconst
 : DCONST int_atom
 ;

insn_ddiv
 : DDIV
 ;

insn_dload
 : DLOAD int_atom
 ;

insn_dmul
 : DMUL
 ;

insn_dneg
 : DNEG
 ;

insn_drem
 : DREM
 ;

insn_dreturn
 : DRETURN
 ;

insn_dstore
 : DSTORE int_atom
 ;

insn_dsub
 : DSUB
 ;

insn_dup
 : DUP
 ;

insn_dup_x1
 : DUP_X1
 ;

insn_dup_x2
 : DUP_X2
 ;

insn_dup2
 : DUP2
 ;

insn_dup2_x1
 : DUP2_X1
 ;

insn_dup2_x2
 : DUP2_X2
 ;

insn_f2d
 : F2D
 ;

insn_f2i
 : F2I
 ;

insn_f2l
 : F2L
 ;

insn_fadd
 : FADD
 ;

insn_faload
 : FALOAD
 ;

insn_fastore
 : FASTORE
 ;

insn_fcmpg
 : FCMPG
 ;

insn_fcmpl
 : FCMPL
 ;

insn_fconst
 : FCONST int_atom
 ;

insn_fdiv
 : FDIV
 ;

insn_fload
 : FLOAD int_atom
 ;

insn_fmul
 : FMUL
 ;

insn_fneg
 : FNEG
 ;

insn_frem
 : FREM
 ;

insn_freturn
 : FRETURN
 ;

insn_fsub
 : FSUB
 ;

insn_fstore
 : FSTORE int_atom
 ;

insn_getfield
 : GETFIELD owner DOT membername type
 ;

insn_getstatic
 : GETSTATIC owner DOT membername type
 ;

insn_goto
 : GOTO NAME
 ;

insn_i2b
 : I2B
 ;

insn_i2c
 : I2C
 ;

insn_i2d
 : I2D
 ;

insn_i2f
 : I2F
 ;

insn_i2l
 : I2L
 ;

insn_i2s
 : I2S
 ;

insn_iadd
 : IADD
 ;

insn_iaload
 : IALOAD
 ;

insn_iand
 : IAND
 ;

insn_iastore
 : IASTORE
 ;

insn_iconst
 : ICONST ilconst_atom
 ;

ilconst_atom
 : int_atom
 | bool_atom
 ;

insn_idiv
 : IDIV
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

insn_ifnull
 : IFNULL NAME
 ;

insn_ifnonnull
 : IFNONNULL NAME
 ;

insn_iinc
 : IINC int_atom COMMA? LSQUARE int_atom RSQUARE
 ;

insn_iload
 : ILOAD int_atom
 ;

insn_imul
 : IMUL
 ;

insn_ineg
 : INEG
 ;

insn_instanceof
 : INSTANCEOF QNAME
 ;

insn_invokedynamic
 : INVOKEDYNAMIC membername method_descriptor LBRACE invokedynamic_body RBRACE
 ;

invokedynamic_body
 : method_handle (LSQUARE const_args RSQUARE)?
 ;

const_args
 : const_arg (COMMA const_arg)*
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

insn_ior
 : IOR
 ;

insn_irem
 : IREM
 ;

insn_ireturn
 : IRETURN
 ;

insn_ishl
 : ISHL
 ;

insn_ishr
 : ISHR
 ;

insn_istore
 : ISTORE int_atom
 ;

insn_isub
 : ISUB
 ;

insn_iushr
 : IUSHR
 ;

insn_ixor
 : IXOR
 ;

insn_jsr
 : JSR NAME
 ;

insn_l2d
 : L2D
 ;

insn_l2f
 : L2F
 ;

insn_l2i
 : L2I
 ;

insn_ladd
 : LADD
 ;

insn_laload
 : LALOAD
 ;

insn_land
 : LAND
 ;

insn_lastore
 : LASTORE
 ;

insn_lcmp
 : LCMP
 ;

insn_lconst
 : LCONST ilconst_atom
 ;

insn_ldc
 : LDC const_arg
 ;

insn_ldiv
 : LDIV
 ;

insn_lload
 : LLOAD int_atom
 ;

insn_lmul
 : LMUL
 ;

insn_lneg
 : LNEG
 ;

insn_lookupswitch
 : LOOKUPSWITCH NAME LBRACE switch_case+ RBRACE
 ;

switch_case
 : (int_atom COLON NAME COMMA?)
 ;

insn_lor
 : LOR
 ;

insn_lrem
 : LREM
 ;

insn_lreturn
 : LRETURN
 ;

insn_lshl
 : LSHL
 ;

insn_lshr
 : LSHR
 ;

insn_lstore
 : LSTORE int_atom
 ;

insn_lsub
 : LSUB
 ;

insn_lushr
 : LUSHR
 ;

insn_lxor
 : LXOR
 ;

insn_monitorenter
 : MONITORENTER
 ;

insn_monitorexit
 : MONITOREXIT
 ;

insn_multianewarray
 : MULTIANEWARRAY array_type COMMA? int_atom?
 ;

insn_new
 : NEW QNAME
 ;

insn_newarray
 : NEWARRAY prim_type
 ;

insn_nop
 : NOP
 ;

insn_pop
 : POP
 ;

insn_pop2
 : POP2
 ;

insn_putfield
 : PUTFIELD owner DOT membername type
 ;

insn_putstatic
 : PUTSTATIC owner DOT membername type
 ;

insn_return
 : RETURN
 ;

insn_ret
 : RET int_atom
 ;

insn_saload
 : SALOAD
 ;

insn_sastore
 : SASTORE
 ;

insn_sipush
 : SIPUSH int_atom
 ;

insn_swap
 : SWAP
 ;

insn_tableswitch
 : TABLESWITCH NAME LBRACE switch_case+ RBRACE
 ;

label
 : LABEL
 ;

owner
 : QNAME
 | NAME
 ;

int_atom    : INT;
float_atom  : FLOAT;
bool_atom   : (TRUE | FALSE);
string_atom : STRING;

LPAREN  : '(';
RPAREN  : ')';
LBRACE  : '{';
RBRACE  : '}';
LSQUARE : '[';
RSQUARE : ']';
DOT     : '.';
MINUS   : '-';
SEMI    : ';';  /* not used but defining for saner com.roscopeco.jasm.errors in descriptors */
COLON   : ':';
COMMA   : ',';
EQUALS  : '=';
DQUOTE  : '"';

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
D2F             : 'd2f';
D2I             : 'd2i';
D2L             : 'd2l';
DADD            : 'dadd';
DALOAD          : 'daload';
DASTORE         : 'dastore';
DCMPG           : 'dcmpg';
DCMPL           : 'dcmpl';
DCONST          : 'dconst';
DDIV            : 'ddiv';
DLOAD           : 'dload';
DMUL            : 'dmul';
DNEG            : 'dneg';
DREM            : 'drem';
DRETURN         : 'dreturn';
DSTORE          : 'dstore';
DSUB            : 'dsub';
DUP             : 'dup';
DUP_X1          : 'dup_x1';
DUP_X2          : 'dup_x2';
DUP2            : 'dup2';
DUP2_X1         : 'dup2_x1';
DUP2_X2         : 'dup2_x2';
F2D             : 'f2d';
F2I             : 'f2i';
F2L             : 'f2l';
FADD            : 'fadd';
FALOAD          : 'faload';
FASTORE         : 'fastore';
FCMPG           : 'fcmpg';
FCMPL           : 'fcmpl';
FCONST          : 'fconst';
FDIV            : 'fdiv';
FLOAD           : 'fload';
FMUL            : 'fmul';
FNEG            : 'fneg';
FREM            : 'frem';
FRETURN         : 'freturn';
FSUB            : 'fsub';
FSTORE          : 'fstore';
GETFIELD        : 'getfield';
GETSTATIC       : 'getstatic';
GOTO            : 'goto';
I2B             : 'i2b';
I2C             : 'i2c';
I2D             : 'i2d';
I2F             : 'i2f';
I2L             : 'i2l';
I2S             : 'i2s';
IADD            : 'iadd';
IALOAD          : 'iaload';
IAND            : 'iand';
IASTORE         : 'iastore';
ICONST          : 'iconst';
IDIV            : 'idiv';
IFACMPEQ        : 'if_acmpeq';
IFACMPNE        : 'if_acmpne';
IFICMPEQ        : 'if_icmpeq';
IFICMPGT        : 'if_icmpgt';
IFICMPLE        : 'if_icmple';
IFICMPLT        : 'if_icmplt';
IFICMPGE        : 'if_icmpge';
IFICMPNE        : 'if_icmpne';
IFEQ            : 'ifeq';
IFGT            : 'ifgt';
IFLE            : 'ifle';
IFLT            : 'iflt';
IFGE            : 'ifge';
IFNE            : 'ifne';
IFNONNULL       : 'ifnonnull';
IFNULL          : 'ifnull';
IINC            : 'iinc';
ILOAD           : 'iload';
IMUL            : 'imul';
INEG            : 'ineg';
INSTANCEOF      : 'instanceof';
INVOKEDYNAMIC   : 'invokedynamic';
INVOKEINTERFACE : 'invokeinterface';
INVOKESPECIAL   : 'invokespecial';
INVOKESTATIC    : 'invokestatic';
INVOKEVIRTUAL   : 'invokevirtual';
IOR             : 'ior';
IREM            : 'irem';
IRETURN         : 'ireturn';
ISHL            : 'ishl';
ISHR            : 'ishr';
ISTORE          : 'istore';
ISUB            : 'isub';
IUSHR           : 'iushr';
IXOR            : 'ixor';
JSR             : 'jsr';
L2D             : 'l2d';
L2F             : 'l2f';
L2I             : 'l2i';
LADD            : 'ladd';
LALOAD          : 'laload';
LAND            : 'land';
LASTORE         : 'lastore';
LCMP            : 'lcmp';
LCONST          : 'lconst';
LDC             : 'ldc';
LDIV            : 'ldiv';
LLOAD           : 'lload';
LMUL            : 'lmul';
LNEG            : 'lneg';
LOOKUPSWITCH    : 'lookupswitch';
LOR             : 'lor';
LREM            : 'lrem';
LRETURN         : 'lreturn';
LSHL            : 'lshl';
LSHR            : 'lshr';
LSTORE          : 'lstore';
LSUB            : 'lsub';
LUSHR           : 'lushr';
LXOR            : 'lxor';
MONITORENTER    : 'monitorenter';
MONITOREXIT     : 'monitorexit';
MULTIANEWARRAY  : 'multianewarray';
NEW             : 'new';
NEWARRAY        : 'newarray';
NOP             : 'nop';
POP             : 'pop';
POP2            : 'pop2';
PUTFIELD        : 'putfield';
PUTSTATIC       : 'putstatic';
RETURN          : 'return';
RET             : 'ret';
SALOAD          : 'saload';
SASTORE         : 'sastore';
SIPUSH          : 'sipush';
SWAP            : 'swap';
TABLESWITCH     : 'tableswitch';

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
 : DQUOTE (~["\r\n] | '""')* DQUOTE
 ;

COMMENT
 : '//' ~[\r\n]* -> channel(HIDDEN)
 ;

BLOCK_COMMENT
 : '/*' .*? ('*/' | EOF)  -> channel(HIDDEN)
 ;

SPACE
 : [ \t\r\n] -> channel(HIDDEN)
 ;

OTHER
 : . -> channel(HIDDEN)
 ;
