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

import java.util.List;

import static com.roscopeco.jasm.TestUtil.testCaseLexer;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertNextToken;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertTokens;

class LexerInstructionTests {
    @Test
    void shouldLexAaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Aaload.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.AALOAD));
    }

    @Test
    void shouldLexAastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Aastore.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.AASTORE));
    }

    @Test
    void shouldLexAconstNull() {
        runInstructionTest("com/roscopeco/jasm/insntest/AconstNull.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.ACONST_NULL));
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
    void shouldLexAreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Areturn.jasm", REF_RETURN_TYPE_TOKEN, lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.ARETURN));
    }

    @Test
    void shouldLexArraylength() {
        runInstructionTest("com/roscopeco/jasm/insntest/Arraylength.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.ARRAYLENGTH));
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
    void shouldLexAthrow() {
        runInstructionTest("com/roscopeco/jasm/insntest/Athrow.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.ATHROW));
    }

    @Test
    void shouldLexBaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Baload.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.BALOAD));
    }

    @Test
    void shouldLexBastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Bastore.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.BASTORE));
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
    void shouldLexCaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Caload.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.CALOAD));
    }

    @Test
    void shouldLexCastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Castore.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.CASTORE));
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
    void shouldLexD2f() {
        runInstructionTest("com/roscopeco/jasm/insntest/D2f.jasm", lexer -> assertNextToken(lexer)
            .hasType(JasmLexer.D2F));
    }

    @Test
    void shouldLexD2i() {
        runInstructionTest("com/roscopeco/jasm/insntest/D2i.jasm", lexer -> assertNextToken(lexer)
            .hasType(JasmLexer.D2I));
    }

    @Test
    void shouldLexD2l() {
        runInstructionTest("com/roscopeco/jasm/insntest/D2l.jasm", lexer -> assertNextToken(lexer)
            .hasType(JasmLexer.D2L));
    }

    @Test
    void shouldLexDadd() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dadd.jasm", lexer -> assertNextToken(lexer)
            .hasType(JasmLexer.DADD));
    }

    @Test
    void shouldLexDaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Daload.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.DALOAD));
    }

    @Test
    void shouldLexDastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dastore.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.DASTORE));
    }

    @Test
    void shouldLexDcmpg() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dcmpg.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.DCMPG));
    }

    @Test
    void shouldLexDcmpl() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dcmpl.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.DCMPL));
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
    void shouldLexDdiv() {
        runInstructionTest("com/roscopeco/jasm/insntest/Ddiv.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.DDIV));
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
    void shouldLexDmul() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dmul.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.DMUL));
    }

    @Test
    void shouldLexDneg() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dneg.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.DNEG));
    }

    @Test
    void shouldLexDrem() {
        runInstructionTest("com/roscopeco/jasm/insntest/Drem.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.DREM));
    }

    @Test
    void shouldLexDsub() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dsub.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.DSUB));
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
    void shouldLexDup() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dup.jasm", lexer -> assertNextToken(lexer)
                .hasType(JasmLexer.DUP));
    }

    @Test
    void shouldLexFaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Faload.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.FALOAD));
    }

    @Test
    void shouldLexF2d() {
        runInstructionTest("com/roscopeco/jasm/insntest/F2d.jasm", lexer -> assertNextToken(lexer)
            .hasType(JasmLexer.F2D));
    }

    @Test
    void shouldLexF2i() {
        runInstructionTest("com/roscopeco/jasm/insntest/F2i.jasm", lexer -> assertNextToken(lexer)
            .hasType(JasmLexer.F2I));
    }

    @Test
    void shouldLexF2l() {
        runInstructionTest("com/roscopeco/jasm/insntest/F2l.jasm", lexer -> assertNextToken(lexer)
            .hasType(JasmLexer.F2L));
    }

    @Test
    void shoulfLexFadd() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fadd.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.FADD));
    }

    @Test
    void shouldLexFastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fastore.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.FASTORE));
    }

    @Test
    void shoulfLexFcmpg() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fcmpg.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.FCMPG));
    }

    @Test
    void shoulfLexFcmpl() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fcmpl.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.FCMPL));
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
    void shoulfLexFdiv() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fdiv.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.FDIV));
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
    void shoulfLexFmul() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fmul.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.FMUL));
    }

    @Test
    void shoulfLexFneg() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fneg.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.FNEG));
    }

    @Test
    void shoulfLexFrem() {
        runInstructionTest("com/roscopeco/jasm/insntest/Frem.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.FREM));
    }

    @Test
    void shouldLexFreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Freturn.jasm", FLOAT_RETURN_TYPE_TOKEN, lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.FRETURN));
    }

    @Test
    void shoulfLexFsub() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fsub.jasm", lexer ->
            assertNextToken(lexer)
                .hasType(JasmLexer.FSUB));
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
    void shouldLexIaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iaload.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.IALOAD));
    }

    @Test
    void shouldLexIastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iastore.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.IASTORE));
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
    void shouldLexIreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Ireturn.jasm", lexer -> assertNextToken(lexer)
                .hasType(JasmLexer.IRETURN));
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
    void shouldLexLaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Laload.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.LALOAD));
    }

    @Test
    void shouldLexLastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Lastore.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.LASTORE));
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
    void shouldLexLReturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Lreturn.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.LRETURN)
        );
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
    void shouldLexReturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Return.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.RETURN)
        );
    }

    private static final List<Integer> VOID_RETURN_TYPE_TOKEN = List.of(JasmLexer.TYPE_VOID);
    private static final List<Integer> FLOAT_RETURN_TYPE_TOKEN = List.of(JasmLexer.TYPE_FLOAT);
    private static final List<Integer> REF_RETURN_TYPE_TOKEN = List.of(JasmLexer.QNAME);

    private void runInstructionTest(
            @NonNull final String testCase,
            @NonNull final ThrowingConsumer<JasmLexer> assertions
    ) {
        runInstructionTest(testCase, VOID_RETURN_TYPE_TOKEN, assertions);
    }

    private void runInstructionTest(
            @NonNull final String testCase,
            @NonNull final List<Integer> expectedReturnTokenTypes,
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

        expectedReturnTokenTypes.forEach(token -> assertNextToken(lexer)
                .hasType(token)
        );

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
