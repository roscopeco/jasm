/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import com.roscopeco.jasm.antlr.JasmBaseVisitor;
import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.ICONST_5;
import static org.objectweb.asm.Opcodes.ICONST_M1;
import static org.objectweb.asm.Opcodes.IF_ACMPEQ;
import static org.objectweb.asm.Opcodes.IF_ACMPNE;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.NEW;
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
                Optional.ofNullable(ctx.extends_()).map(e -> e.QNAME().getText()).orElse("java/lang/Object"),
                Optional.ofNullable(ctx.implements_()).stream()
                        .flatMap(i -> i.QNAME().stream())
                        .map(ParseTree::getText)
                        .toArray(String[]::new)
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
        private final HashMap<String, LabelHolder> labels = new HashMap<>();
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

            guardAllLabelsDeclared();

            return ret;
        }

        @Override
        public Void visitLabel(JasmParser.LabelContext ctx) {
            final var label = declareLabel(ctx.LABEL().getText());
            this.methodVisitor.visitLabel(label.label);
            return super.visitLabel(ctx);
        }

        @Override
        public Void visitInsn_aconstnull(JasmParser.Insn_aconstnullContext ctx) {
            this.methodVisitor.visitInsn(ACONST_NULL);
            return super.visitInsn_aconstnull(ctx);
        }

        @Override
        public Void visitInsn_aload(final JasmParser.Insn_aloadContext ctx) {
            this.methodVisitor.visitIntInsn(ALOAD, Integer.parseInt(ctx.atom().getText()));
            return super.visitInsn_aload(ctx);
        }

        @Override
        public Void visitInsn_areturn(JasmParser.Insn_areturnContext ctx) {
            this.methodVisitor.visitInsn(ARETURN);
            return super.visitInsn_areturn(ctx);
        }

        @Override
        public Void visitInsn_dup(JasmParser.Insn_dupContext ctx) {
            this.methodVisitor.visitInsn(DUP);
            return super.visitInsn_dup(ctx);
        }

        @Override
        public Void visitInsn_freturn(JasmParser.Insn_freturnContext ctx) {
            this.methodVisitor.visitInsn(FRETURN);
            return super.visitInsn_freturn(ctx);
        }

        @Override
        public Void visitInsn_goto(JasmParser.Insn_gotoContext ctx) {
            this.methodVisitor.visitJumpInsn(GOTO, getLabel(ctx.NAME().getText()).label);
            return super.visitInsn_goto(ctx);
        }

        @Override
        public Void visitInsn_iconst(final JasmParser.Insn_iconstContext ctx) {
            this.methodVisitor.visitInsn(generateIconstOpcode(ctx.atom()));
            return super.visitInsn_iconst(ctx);
        }

        @Override
        public Void visitInsn_if_acmpeq(JasmParser.Insn_if_acmpeqContext ctx) {
            this.methodVisitor.visitJumpInsn(IF_ACMPEQ, getLabel(ctx.NAME().getText()).label);
            return super.visitInsn_if_acmpeq(ctx);
        }

        @Override
        public Void visitInsn_if_acmpne(JasmParser.Insn_if_acmpneContext ctx) {
            this.methodVisitor.visitJumpInsn(IF_ACMPNE, getLabel(ctx.NAME().getText()).label);
            return super.visitInsn_if_acmpne(ctx);
        }

        @Override
        public Void visitInsn_invokeinterface(final JasmParser.Insn_invokeinterfaceContext ctx) {
            visitNonDynamicInvoke(
                    INVOKEINTERFACE,
                    ctx.owner().getText(),
                    ctx.membername().getText(),
                    ctx.method_descriptor().getText(),
                    true
            );

            return super.visitInsn_invokeinterface(ctx);
        }

        @Override
        public Void visitInsn_invokespecial(final JasmParser.Insn_invokespecialContext ctx) {
            visitNonDynamicInvoke(
                    INVOKESPECIAL,
                    ctx.owner().getText(),
                    ctx.membername().getText(),
                    ctx.method_descriptor().getText(),
                    false
            );

            return super.visitInsn_invokespecial(ctx);
        }

        @Override
        public Void visitInsn_invokestatic(final JasmParser.Insn_invokestaticContext ctx) {
            visitNonDynamicInvoke(
                    INVOKESTATIC,
                    ctx.owner().getText(),
                    ctx.membername().getText(),
                    ctx.method_descriptor().getText(),
                    false
            );

            return super.visitInsn_invokestatic(ctx);
        }

        @Override
        public Void visitInsn_invokevirtual(final JasmParser.Insn_invokevirtualContext ctx) {
            visitNonDynamicInvoke(
                    INVOKEVIRTUAL,
                    ctx.owner().getText(),
                    ctx.membername().getText(),
                    ctx.method_descriptor().getText(),
                    false
            );

            return super.visitInsn_invokevirtual(ctx);
        }

        private void visitNonDynamicInvoke(
                final int opcode,
                @NonNull final String owner,
                @NonNull final String name,
                @NonNull final String descriptor,
                final boolean isInterface
        ) {
            this.methodVisitor.visitMethodInsn(opcode, owner, name, fixDescriptor(descriptor), isInterface);
        }

        @Override
        public Void visitInsn_ireturn(final JasmParser.Insn_ireturnContext ctx) {
            this.methodVisitor.visitInsn(IRETURN);
            return super.visitInsn_ireturn(ctx);
        }

        @Override
        public Void visitInsn_ldc(JasmParser.Insn_ldcContext ctx) {
            this.methodVisitor.visitLdcInsn(generateLdcObject(ctx));
            return super.visitInsn_ldc(ctx);
        }

        @Override
        public Void visitInsn_new(JasmParser.Insn_newContext ctx) {
            this.methodVisitor.visitTypeInsn(NEW, ctx.QNAME().getText());
            return super.visitInsn_new(ctx);
        }

        @Override
        public Void visitInsn_return(final JasmParser.Insn_returnContext ctx) {
            this.methodVisitor.visitInsn(RETURN);
            return super.visitInsn_return(ctx);
        }

        private String generateDescriptor(final JasmParser.MethodContext ctx) {
            final var type = ctx.type();
            final var returnType = type.get(type.size() - 1).getText();
            final var paramTypes = type.subList(0, type.size() - 1).stream()
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

        private Object generateLdcObject(final JasmParser.Insn_ldcContext ctx) {
            if (ctx.atom().int_atom() != null) {
                return Integer.parseInt(ctx.atom().getText());
            } else if (ctx.atom().float_atom() != null) {
                return Float.parseFloat(ctx.atom().getText());
            } else if (ctx.atom().bool_atom() != null) {
                return Boolean.parseBoolean(ctx.atom().getText()) ? 1 : 0;
            } else if (ctx.atom().name_atom() != null) {
                throw new UnsupportedOperationException("TODO");
            } else if (ctx.atom().string_atom() != null) {
                return unescapeConstantString(ctx.atom().getText());
            } else {
                throw new SyntaxErrorException("Unable to generate LDC for unknown type (" + ctx.getText() + ")");
            }
        }

        private String unescapeConstantString(@NonNull final String constant) {
            return constant.substring(1, constant.length() - 1).replace("\"\"", "\"");
        }

        private String fixDescriptor(@NonNull final String languageDescriptor) {
            return languageDescriptor.replaceAll("([IJFDZV]);", "$1");
        }

        private String normaliseLabelName(@NonNull final String labelName) {
            if (labelName.endsWith(":")) {
                return labelName.substring(0, labelName.length() - 1);
            } else {
                return labelName;
            }
        }

        private LabelHolder getLabel(@NonNull final String name) {
            return labels.computeIfAbsent(normaliseLabelName(name), k -> new LabelHolder(new Label(), false));
        }

        private LabelHolder declareLabel(@NonNull final String name) {
            final var normalName = normaliseLabelName(name);
            final var label = labels.get(normalName);

            return Optional.ofNullable(label)
                    .orElse(labels.put(normalName, new LabelHolder(new Label(), true)));
        }

        private void guardAllLabelsDeclared() {
            final var undeclaredLabels = labels.entrySet().stream()
                    .filter(l -> !l.getValue().declared())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.joining());

            if (!undeclaredLabels.isEmpty()) {
                throw new SyntaxErrorException("Labels used but not declared: [" + undeclaredLabels + "]");
            }
        }

        private record LabelHolder(Label label, boolean declared) { }
    }
}
