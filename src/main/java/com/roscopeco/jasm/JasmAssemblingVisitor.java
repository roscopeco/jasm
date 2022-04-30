/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import java.util.stream.Collectors;

import com.roscopeco.jasm.antlr.JasmBaseVisitor;
import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.antlr.v4.runtime.RuleContext;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.ICONST_5;
import static org.objectweb.asm.Opcodes.ICONST_M1;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V17;

class JasmAssemblingVisitor extends JasmBaseVisitor<Void> {
    private final ClassVisitor vistor;
    private final Modifiers modifiers;

    JasmAssemblingVisitor(
            @NonNull final ClassVisitor visitor,
            @NonNull final Modifiers modifiers
    ) {
        this.vistor = visitor;
        this.modifiers = modifiers;
    }

    @Override
    public Void visitClass(final JasmParser.ClassContext ctx) {
        vistor.visit(
                V17,
                this.modifiers.mapModifiers(ctx.modifier()),
                ctx.classname().getText(),
                null,
                "java/lang/Object",
                null
        );

        final var ret= super.visitClass(ctx);
        vistor.visitEnd();
        return ret;
    }

    @Override
    public Void visitField(final JasmParser.FieldContext ctx) {
        final var fv = vistor.visitField(
                this.modifiers.mapModifiers(ctx.modifier()),
                ctx.membername().getText(),
                ctx.type().getText(),
                null,
                null
        );

        final var ret= super.visitField(ctx);
        fv.visitEnd();
        return ret;
    }

    @Override
    public Void visitMethod(final JasmParser.MethodContext ctx) {
        return new JasmMethodVisitor().visitMethod(ctx);
    }

    private class JasmMethodVisitor extends JasmBaseVisitor<Void> {
        private MethodVisitor methodVisitor;

        @Override
        public Void visitMethod(final JasmParser.MethodContext ctx) {
            this.methodVisitor = JasmAssemblingVisitor.this.vistor.visitMethod(
                    modifiers.mapModifiers(ctx.modifier()),
                    ctx.membername().getText(),
                generateDescriptor(ctx),
                null,
                null
            );

            final var ret = super.visitMethod(ctx);

            this.methodVisitor.visitMaxs(0, 0);
            this.methodVisitor.visitEnd();

            return ret;
        }

        @Override
        public Void visitInsn_aload(final JasmParser.Insn_aloadContext ctx) {
            this.methodVisitor.visitIntInsn(ALOAD, Integer.parseInt(ctx.atom().getText()));
            return super.visitInsn_aload(ctx);
        }

        @Override
        public Void visitInsn_iconst(final JasmParser.Insn_iconstContext ctx) {
            this.methodVisitor.visitInsn(generateIconstOpcode(ctx.atom()));
            return super.visitInsn_iconst(ctx);
        }

        @Override
        public Void visitInsn_invokespecial(final JasmParser.Insn_invokespecialContext ctx) {
            this.methodVisitor.visitMethodInsn(
                    INVOKESPECIAL,
                    ctx.owner().getText(),
                    ctx.membername().getText(),
                    fixDescriptor(ctx.method_descriptor().getText()),
                    false
            );

            return super.visitInsn_invokespecial(ctx);
        }

        @Override
        public Void visitInsn_ireturn(final JasmParser.Insn_ireturnContext ctx) {
            this.methodVisitor.visitInsn(IRETURN);
            return super.visitInsn_ireturn(ctx);
        }

        @Override
        public Void visitInsn_return(final JasmParser.Insn_returnContext ctx) {
            this.methodVisitor.visitInsn(RETURN);
            return super.visitInsn_return(ctx);
        }

        private String generateDescriptor(final JasmParser.MethodContext ctx) {
            final var returnType = ctx.type(0).getText();
            final var paramTypes = ctx.type().stream()
                    .skip(1)
                    .map(RuleContext::getText)
                    .collect(Collectors.joining());

            return "(" + paramTypes + ")" + returnType;
        }

        private int generateIconstOpcode(final JasmParser.AtomContext ctx) {
            try {
                return switch (Integer.parseInt(ctx.getText())) {
                    case -1 -> ICONST_M1;
                    case 0 -> ICONST_0;
                    case 1 -> ICONST_1;
                    case 2 -> ICONST_2;
                    case 3 -> ICONST_3;
                    case 4 -> ICONST_4;
                    case 5 -> ICONST_5;
                    default -> throw new SyntaxErrorException("Invalid operand to ICONST (must be in range -1 to 5)");
                };
            } catch (NumberFormatException e) {
                throw new SyntaxErrorException("Invalid non-numeric operand for ICONST (found '" + ctx.getText() + "')");
            }
        }

        private String fixDescriptor(final String languageDescriptor) {
            return languageDescriptor.replaceAll("([IJFDZV]);", "$1");
        }
    }
}
