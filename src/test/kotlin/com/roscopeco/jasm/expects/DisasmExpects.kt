package com.roscopeco.jasm.expects

object DisasmExpects {
    @JvmField
    val EMPTY_ENUM = """
    public enum class com/roscopeco/jasm/model/disasm/EmptyEnum {
        public values()[com/roscopeco/jasm/model/disasm/EmptyEnum {
            label0:
            getstatic com/roscopeco/jasm/model/disasm/EmptyEnum.${'$'}VALUES [com/roscopeco/jasm/model/disasm/EmptyEnum
            invokevirtual Lcom/roscopeco/jasm/model/disasm/EmptyEnum;.clone()java/lang/Object
            checkcast Lcom/roscopeco/jasm/model/disasm/EmptyEnum;
            areturn
        }
        
        public valueOf(java/lang/String)com/roscopeco/jasm/model/disasm/EmptyEnum {
            label0:
            null com/roscopeco/jasm/model/disasm/EmptyEnum
            aload 0
            invokestatic java/lang/Enum.valueOf(java/lang/Class, java/lang/String)java/lang/Enum
            checkcast com/roscopeco/jasm/model/disasm/EmptyEnum
            areturn
            label1:
        }
        
        <init>(java/lang/String, I)V {
            label0:
            aload 0
            aload 1
            iload 2
            invokespecial java/lang/Enum.<init>(java/lang/String, I)V
            return
            label1:
        }
        
        <clinit>()V {
            label0:
            iconst 0
            anewarray com/roscopeco/jasm/model/disasm/EmptyEnum
            putstatic com/roscopeco/jasm/model/disasm/EmptyEnum.${'$'}VALUES [com/roscopeco/jasm/model/disasm/EmptyEnum
            return
        }
    }
    """.trimIndent()
}