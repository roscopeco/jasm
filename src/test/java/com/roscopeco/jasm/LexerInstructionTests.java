/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import com.roscopeco.jasm.antlr.JasmLexer;
import lombok.NonNull;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.roscopeco.jasm.TestUtil.testCaseLexer;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertNextToken;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertTokens;

class LexerInstructionTests {
    static Stream<Arguments> singleInstructionNoOperandTests() {
        return Stream.of(
            Arguments.of("Aaload", JasmLexer.AALOAD),
            Arguments.of("Aastore", JasmLexer.AASTORE),
            Arguments.of("AconstNull", JasmLexer.ACONST_NULL),
            Arguments.of("Areturn", JasmLexer.ARETURN),
            Arguments.of("Arraylength", JasmLexer.ARRAYLENGTH),
            Arguments.of("Athrow", JasmLexer.ATHROW),
            Arguments.of("Baload", JasmLexer.BALOAD),
            Arguments.of("Bastore", JasmLexer.BASTORE),
            Arguments.of("Caload", JasmLexer.CALOAD),
            Arguments.of("Castore", JasmLexer.CASTORE),
            Arguments.of("D2i", JasmLexer.D2I),
            Arguments.of("D2f", JasmLexer.D2F),
            Arguments.of("D2l", JasmLexer.D2L),
            Arguments.of("Dadd", JasmLexer.DADD),
            Arguments.of("Daload", JasmLexer.DALOAD),
            Arguments.of("Dastore", JasmLexer.DASTORE),
            Arguments.of("Dcmpg", JasmLexer.DCMPG),
            Arguments.of("Dcmpl", JasmLexer.DCMPL),
            Arguments.of("Ddiv", JasmLexer.DDIV),
            Arguments.of("Dmul", JasmLexer.DMUL),
            Arguments.of("Dneg", JasmLexer.DNEG),
            Arguments.of("Drem", JasmLexer.DREM),
            Arguments.of("Dreturn", JasmLexer.DRETURN),
            Arguments.of("Dsub", JasmLexer.DSUB),
            Arguments.of("Dup", JasmLexer.DUP),
            Arguments.of("Dup_x1", JasmLexer.DUP_X1),
            Arguments.of("Dup_x2", JasmLexer.DUP_X2),
            Arguments.of("Dup2", JasmLexer.DUP2),
            Arguments.of("Dup2_x1", JasmLexer.DUP2_X1),
            Arguments.of("Dup2_x2", JasmLexer.DUP2_X2),
            Arguments.of("F2i", JasmLexer.F2I),
            Arguments.of("F2d", JasmLexer.F2D),
            Arguments.of("F2l", JasmLexer.F2L),
            Arguments.of("Fadd", JasmLexer.FADD),
            Arguments.of("Faload", JasmLexer.FALOAD),
            Arguments.of("Fastore", JasmLexer.FASTORE),
            Arguments.of("Fcmpg", JasmLexer.FCMPG),
            Arguments.of("Fcmpl", JasmLexer.FCMPL),
            Arguments.of("Fdiv", JasmLexer.FDIV),
            Arguments.of("Fmul", JasmLexer.FMUL),
            Arguments.of("Fneg", JasmLexer.FNEG),
            Arguments.of("Frem", JasmLexer.FREM),
            Arguments.of("Freturn", JasmLexer.FRETURN),
            Arguments.of("Fsub", JasmLexer.FSUB),
            Arguments.of("I2b", JasmLexer.I2B),
            Arguments.of("I2c", JasmLexer.I2C),
            Arguments.of("I2d", JasmLexer.I2D),
            Arguments.of("I2f", JasmLexer.I2F),
            Arguments.of("I2l", JasmLexer.I2L),
            Arguments.of("I2s", JasmLexer.I2S),
            Arguments.of("Iadd", JasmLexer.IADD),
            Arguments.of("Iaload", JasmLexer.IALOAD),
            Arguments.of("Iand", JasmLexer.IAND),
            Arguments.of("Iastore", JasmLexer.IASTORE),
            Arguments.of("Idiv", JasmLexer.IDIV),
            Arguments.of("Imul", JasmLexer.IMUL),
            Arguments.of("Ineg", JasmLexer.INEG),
            Arguments.of("Ior", JasmLexer.IOR),
            Arguments.of("Irem", JasmLexer.IREM),
            Arguments.of("Ireturn", JasmLexer.IRETURN),
            Arguments.of("Ishl", JasmLexer.ISHL),
            Arguments.of("Ishr", JasmLexer.ISHR),
            Arguments.of("Isub", JasmLexer.ISUB),
            Arguments.of("Iushr", JasmLexer.IUSHR),
            Arguments.of("Ixor", JasmLexer.IXOR),
            Arguments.of("L2d", JasmLexer.L2D),
            Arguments.of("L2f", JasmLexer.L2F),
            Arguments.of("L2i", JasmLexer.L2I),
            Arguments.of("Ladd", JasmLexer.LADD),
            Arguments.of("Laload", JasmLexer.LALOAD),
            Arguments.of("Land", JasmLexer.LAND),
            Arguments.of("Lastore", JasmLexer.LASTORE),
            Arguments.of("Lcmp", JasmLexer.LCMP),
            Arguments.of("Ldiv", JasmLexer.LDIV),
            Arguments.of("Lmul", JasmLexer.LMUL),
            Arguments.of("Lneg", JasmLexer.LNEG),
            Arguments.of("Lor", JasmLexer.LOR),
            Arguments.of("Lrem", JasmLexer.LREM),
            Arguments.of("Lreturn", JasmLexer.LRETURN),
            Arguments.of("Lshl", JasmLexer.LSHL),
            Arguments.of("Lshr", JasmLexer.LSHR),
            Arguments.of("Lsub", JasmLexer.LSUB),
            Arguments.of("Lushr", JasmLexer.LUSHR),
            Arguments.of("Monitorenter", JasmLexer.MONITORENTER),
            Arguments.of("Monitorexit", JasmLexer.MONITOREXIT),
            Arguments.of("Return", JasmLexer.RETURN)
        );
    }

    @Test
    void shouldLexAload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Aload.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.ALOAD);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("0");
        });
    }

    @Test
    void shouldLexAnewarray() {
        runInstructionTest("com/roscopeco/jasm/insntest/Anewarray.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.ANEWARRAY);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/String");
        });
    }

    @Test
    void shouldLexAstore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Astore.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.ASTORE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("1");
        });
    }

    @Test
    void shouldLexBipush() {
        runInstructionTest("com/roscopeco/jasm/insntest/Bipush.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.BIPUSH);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("100");
        });
    }

    @Test
    void shouldLexCheckCast() {
        runInstructionTest("com/roscopeco/jasm/insntest/Checkcast.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.CHECKCAST);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/util/ArrayList");
        });
    }

    @Test
    void shouldLexDconst() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dconst.jasm", lexer -> {
            assertNextToken(lexer)
                .hasType(JasmLexer.DCONST);

            assertNextToken(lexer)
                .hasType(JasmLexer.INT)
                .hasText("0");
        });
    }

    @Test
    void shouldLexDload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dload.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.DLOAD);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("0");
        });
    }

    @Test
    void shouldLexDstore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dstore.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.DSTORE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("0");
        });
    }

    @Test
    void shoulfLexFconst() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fconst.jasm", lexer -> {
            assertNextToken(lexer)
                .hasType(JasmLexer.FCONST);

            assertNextToken(lexer)
                .hasType(JasmLexer.INT)
                .hasText("0");
        });
    }

    @Test
    void shouldLexFload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fload.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.FLOAD);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("0");
        });
    }

    @Test
    void shouldLexFstore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fstore.jasm", lexer -> {
            assertNextToken(lexer)
                .hasType(JasmLexer.FSTORE);

            assertNextToken(lexer)
                .hasType(JasmLexer.INT)
                .hasText("0");
        });
    }

    @Test
    void shouldLexGetField() {
        runInstructionTest("com/roscopeco/jasm/insntest/GetField.jasm", lexer -> assertTokens(lexer, tokens -> {
            tokens.next().hasType(JasmLexer.GETFIELD);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("com/example/SomeClass");

            tokens.next().hasType(JasmLexer.DOT);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("someField");

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/lang/String");
        }));
    }

    @Test
    void shouldLexGetStatic() {
        runInstructionTest("com/roscopeco/jasm/insntest/GetStatic.jasm", lexer -> assertTokens(lexer, tokens -> {
            tokens.next().hasType(JasmLexer.GETSTATIC);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("com/example/SomeClass");

            tokens.next().hasType(JasmLexer.DOT);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("someField");

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/lang/String");
        }));
    }

    @Test
    void shouldLexGotoAndLabels() {
        runInstructionTest("com/roscopeco/jasm/insntest/Goto.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.LABEL)
                    .hasText("infinity:");

            assertNextToken(lexer)
                    .hasType(JasmLexer.GOTO);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("infinity");

            assertNextToken(lexer)
                    .hasType(JasmLexer.GOTO);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("unreachable");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LABEL)
                    .hasText("unreachable:");

            assertNextToken(lexer)
                    .hasType(JasmLexer.RETURN);
        });
    }

    @Test
    void shouldLexIconst() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iconst.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.ICONST);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("0");
        });
    }

    @Test
    void shouldLexIfs() {
        runInstructionTest("com/roscopeco/jasm/insntest/If.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.IFEQ);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFGE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFGT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFLE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFLT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFNE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LABEL)
                    .hasText("label:");

            assertNextToken(lexer)
                    .hasType(JasmLexer.RETURN);
        });
    }

    @Test
    void shouldLexIfAcmps() {
        runInstructionTest("com/roscopeco/jasm/insntest/IfAcmp.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.IFACMPEQ);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFACMPNE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LABEL)
                    .hasText("label:");

            assertNextToken(lexer)
                    .hasType(JasmLexer.RETURN);
        });
    }

    @Test
    void shouldLexIfIcmps() {
        runInstructionTest("com/roscopeco/jasm/insntest/IfIcmp.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.IFICMPEQ);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFICMPGE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFICMPGT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFICMPLE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFICMPLT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFICMPNE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LABEL)
                    .hasText("label:");

            assertNextToken(lexer)
                    .hasType(JasmLexer.RETURN);
        });
    }


    @Test
    void shouldLexIfNullNonNull() {
        runInstructionTest("com/roscopeco/jasm/insntest/IfNullNonNull.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.IFNULL);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.IFNONNULL);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("label");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LABEL)
                    .hasText("label:");

            assertNextToken(lexer)
                    .hasType(JasmLexer.RETURN);
        });
    }

    @Test
    void shoulfLexIinc() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iinc.jasm", lexer -> assertTokens(lexer, tokens -> {
            // First form
            tokens.next()
                .hasType(JasmLexer.IINC);

            tokens.next()
                .hasType(JasmLexer.INT)
                .hasText("3");

            tokens.next()
                .hasType(JasmLexer.LSQUARE);

            tokens.next()
                .hasType(JasmLexer.INT)
                .hasText("42");

            tokens.next()
                .hasType(JasmLexer.RSQUARE);

            // Second form
            tokens.next()
                .hasType(JasmLexer.IINC);

            tokens.next()
                .hasType(JasmLexer.INT)
                .hasText("3");

            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.LSQUARE);

            tokens.next()
                .hasType(JasmLexer.INT)
                .hasText("42");

            tokens.next()
                .hasType(JasmLexer.RSQUARE);
        }));
    }
    
    @Test
    void shouldLexIload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iload.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.ILOAD);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("0");
        });
    }

    @Test
    void shouldLexInstanceof() {
        runInstructionTest("com/roscopeco/jasm/insntest/Instanceof.jasm", lexer -> {
            assertNextToken(lexer)
                .hasType(JasmLexer.INSTANCEOF);

            assertNextToken(lexer)
                .hasType(JasmLexer.QNAME)
                .hasText("java/lang/Object");
        });
    }

    @Test
    void shouldLexInvokeDynamic() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeDynamic.jasm", lexer -> assertTokens(lexer, tokens -> {
            tokens.next()
                    .hasType(JasmLexer.INVOKEDYNAMIC);

            tokens.next()
                    .hasType(JasmLexer.NAME)
                    .hasText("dynamicMethod");

            tokens.next()
                    .hasType(JasmLexer.LPAREN);

            tokens.next()
                    .hasType(JasmLexer.TYPE_INT);

            tokens.next()
                    .hasType(JasmLexer.RPAREN);

            tokens.next()
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/Object");

            tokens.next()
                    .hasType(JasmLexer.LBRACE);

            tokens.next()
                    .hasType(JasmLexer.INVOKESTATIC);

            tokens.next()
                    .hasType(JasmLexer.QNAME)
                    .hasText("com/example/Bootstrap");

            tokens.next()
                    .hasType(JasmLexer.DOT);

            tokens.next()
                    .hasType(JasmLexer.NAME)
                    .hasText("method");

            tokens.next()
                    .hasType(JasmLexer.LPAREN);

            tokens.next()
                    .hasType(JasmLexer.TYPE_INT);

            tokens.next()
                    .hasType(JasmLexer.COMMA);

            tokens.next()
                    .hasType(JasmLexer.TYPE_FLOAT);

            tokens.next()
                    .hasType(JasmLexer.COMMA);

            tokens.next()
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/String");

            tokens.next()
                    .hasType(JasmLexer.COMMA);

            tokens.next()
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/Class");

            tokens.next()
                    .hasType(JasmLexer.COMMA);

            tokens.next()
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/invoke/MethodHandle");

            tokens.next()
                    .hasType(JasmLexer.RPAREN);

            tokens.next()
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/invoke/CallSite");

            tokens.next()
                    .hasType(JasmLexer.LSQUARE);

            tokens.next()
                    .hasType(JasmLexer.INT)
                    .hasText("10");

            tokens.next()
                    .hasType(JasmLexer.COMMA);

            tokens.next()
                    .hasType(JasmLexer.FLOAT)
                    .hasText("5.5");

            tokens.next()
                    .hasType(JasmLexer.COMMA);

            tokens.next()
                    .hasType(JasmLexer.STRING)
                    .hasText("\"Anything\"");

            tokens.next()
                    .hasType(JasmLexer.COMMA);

            tokens.next()
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/util/List");

            tokens.next()
                    .hasType(JasmLexer.COMMA);

            tokens.next()
                    .hasType(JasmLexer.CONSTDYNAMIC);

            tokens.next()
                    .hasType(JasmLexer.NAME)
                    .hasText("DYNAMIC_CONST");

            tokens.next()
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/Object");

            tokens.next()
                    .hasType(JasmLexer.LBRACE);

            tokens.next()
                    .hasType(JasmLexer.INVOKEINTERFACE);

            tokens.next()
                    .hasType(JasmLexer.QNAME)
                    .hasText("com/example/Bootstrap");

            tokens.next()
                    .hasType(JasmLexer.DOT);

            tokens.next()
                    .hasType(JasmLexer.NAME)
                    .hasText("constsite");

            tokens.next()
                    .hasType(JasmLexer.LPAREN);

            tokens.next()
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/String");

            tokens.next()
                    .hasType(JasmLexer.RPAREN);

            tokens.next()
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/invoke/CallSite");

            tokens.next()
                    .hasType(JasmLexer.LSQUARE);

            tokens.next()
                    .hasType(JasmLexer.STRING)
                    .hasText("\"Something else\"");

            tokens.next()
                    .hasType(JasmLexer.RSQUARE);

            tokens.next()
                    .hasType(JasmLexer.RBRACE);

            tokens.next()
                    .hasType(JasmLexer.RSQUARE);

            tokens.next()
                    .hasType(JasmLexer.RBRACE);
        }));
    }

    @Test
    void shouldLexInvokeInterface() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeInterface.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.INVOKEINTERFACE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/util/List");

            assertNextToken(lexer)
                    .hasType(JasmLexer.DOT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("get");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LPAREN);

            assertNextToken(lexer)
                    .hasType(JasmLexer.TYPE_INT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.RPAREN);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/Object");
        });
    }

    @Test
    void shouldLexInvokeSpecial() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeSpecial.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.INVOKESPECIAL);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/Object");

            assertNextToken(lexer)
                    .hasType(JasmLexer.DOT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INIT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.LPAREN);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/String");

            assertNextToken(lexer)
                    .hasType(JasmLexer.COMMA);

            assertNextToken(lexer)
                    .hasType(JasmLexer.TYPE_INT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.COMMA);

            assertNextToken(lexer)
                    .hasType(JasmLexer.TYPE_BOOL);

            assertNextToken(lexer)
                    .hasType(JasmLexer.RPAREN);

            assertNextToken(lexer)
                    .hasType(JasmLexer.TYPE_VOID);
        });
    }

    @Test
    void shouldLexInvokeStatic() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeStatic.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.INVOKESTATIC);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/Thread");

            assertNextToken(lexer)
                    .hasType(JasmLexer.DOT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("currentThread");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LPAREN);

            assertNextToken(lexer)
                    .hasType(JasmLexer.RPAREN);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/Thread");
        });
    }

    @Test
    void shouldLexInvokeVirtual() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeVirtual.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.INVOKEVIRTUAL);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/String");

            assertNextToken(lexer)
                    .hasType(JasmLexer.DOT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.NAME)
                    .hasText("trim");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LPAREN);

            assertNextToken(lexer)
                    .hasType(JasmLexer.RPAREN);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/lang/String");
        });
    }

    @Test
    void shouldLexIstore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Istore.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.ISTORE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("0");
        });
    }

    @Test
    void shouldLexLdc() {
        runInstructionTest("com/roscopeco/jasm/insntest/Ldc.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.LDC);

            assertNextToken(lexer)
                    .hasType(JasmLexer.TRUE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.LDC);

            assertNextToken(lexer)
                    .hasType(JasmLexer.FALSE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.LDC);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("10");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LDC);

            assertNextToken(lexer)
                    .hasType(JasmLexer.FLOAT)
                    .hasText("5.0");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LDC);

            assertNextToken(lexer)
                    .hasType(JasmLexer.STRING)
                    .hasText("\"Test string\"");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LDC);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/util/List");

            // Skip remaining tokens, the rest of these are covered by the invokedynamic lex tests
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
            lexer.nextToken();
        });
    }

    @Test
    void shouldLexLconst() {
        runInstructionTest("com/roscopeco/jasm/insntest/Lconst.jasm", lexer -> {
            assertNextToken(lexer)
                .hasType(JasmLexer.LCONST);

            assertNextToken(lexer)
                .hasType(JasmLexer.INT)
                .hasText("1");
        });
    }


    @Test
    void shouldLexLload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Lload.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.LLOAD);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("0");
        });
    }

    @Test
    void shouldLexLookupswitch() {
        runInstructionTest("com/roscopeco/jasm/insntest/Lookupswitch.jasm", lexer -> {
            assertNextToken(lexer)
                .hasType(JasmLexer.LOOKUPSWITCH);

            assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("defaultLabel");

            assertNextToken(lexer)
                .hasType(JasmLexer.LBRACE);

            assertNextToken(lexer)
                .hasType(JasmLexer.INT)
                .hasText("1");

            assertNextToken(lexer)
                .hasType(JasmLexer.COLON);

            assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("oneLabel");

            assertNextToken(lexer)
                .hasType(JasmLexer.INT)
                .hasText("100");

            assertNextToken(lexer)
                .hasType(JasmLexer.COLON);

            assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("hundredLabel");

            assertNextToken(lexer)
                .hasType(JasmLexer.COMMA);      // Optional

            assertNextToken(lexer)
                .hasType(JasmLexer.INT)
                .hasText("1000");

            assertNextToken(lexer)
                .hasType(JasmLexer.COLON);

            assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("thousandLabel");

            assertNextToken(lexer)
                .hasType(JasmLexer.RBRACE);
        });
    }

    @Test
    void shouldLexLstore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Lstore.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.LSTORE);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("0");
        });
    }

    @Test
    void shouldLexNew() {
        runInstructionTest("com/roscopeco/jasm/insntest/New.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.NEW);

            assertNextToken(lexer)
                    .hasType(JasmLexer.QNAME)
                    .hasText("java/util/ArrayList");
        });
    }

    @Test
    void shouldLexPutField() {
        runInstructionTest("com/roscopeco/jasm/insntest/PutField.jasm", lexer -> assertTokens(lexer, tokens -> {
            tokens.next().hasType(JasmLexer.PUTFIELD);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("com/example/SomeClass");

            tokens.next().hasType(JasmLexer.DOT);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("someField");

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/lang/String");
        }));
    }

    @Test
    void shouldLexPutStatic() {
        runInstructionTest("com/roscopeco/jasm/insntest/PutStatic.jasm", lexer -> assertTokens(lexer, tokens -> {
            tokens.next().hasType(JasmLexer.PUTSTATIC);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("com/example/SomeClass");

            tokens.next().hasType(JasmLexer.DOT);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("someField");

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/lang/String");
        }));
    }

    @ParameterizedTest
    @MethodSource("singleInstructionNoOperandTests")
    void singleInstructionNoOperandTests(final String testCase, final int expectedToken) {
        runInstructionTest("com/roscopeco/jasm/insntest/" + testCase + ".jasm", lexer ->
            assertNextToken(lexer)
                .hasType(expectedToken)
        );
    }

    private void runInstructionTest(
            @NonNull final String testCase,
            @NonNull final ThrowingConsumer<JasmLexer> assertions
    ) {
        final var lexer = testCaseLexer(testCase);

        assertNextToken(lexer)
                .hasType(JasmLexer.CLASS);

        assertNextToken(lexer)
                .hasType(JasmLexer.QNAME);

        assertNextToken(lexer)
                .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("insnTest");

        assertNextToken(lexer)
                .hasType(JasmLexer.LPAREN);

        assertNextToken(lexer)
                .hasType(JasmLexer.RPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.TYPE_VOID);

        assertNextToken(lexer)
                .hasType(JasmLexer.LBRACE);

        assertions.accept(lexer);

        assertNextToken(lexer)
                .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.EOF);
    }
}
