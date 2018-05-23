package org.aion.avm.core.arraywrapping;

import org.aion.avm.core.util.Assert;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;
import org.objectweb.asm.signature.*;
import org.objectweb.asm.util.*;


public class ArrayWrappingClassAdapter extends ClassVisitor {

    public ArrayWrappingClassAdapter(ClassVisitor visitor) {
        super(Opcodes.ASM6, visitor);
    }

    public MethodVisitor visitMethod(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions) {
        //System.out.println(descriptor);
        //System.out.println(ArrayWrappingBytecodeFactory.updateMethodDesc(descriptor));
        //System.out.println("**************************************");

        //updateMethodDescriptor(descriptor);

        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        //logger.info("Method: access = {}, name = {}, descriptor = {}, signature = {}, exceptions = {}", access, name, descriptor, signature, exceptions);

        return new ArrayWrappingMethodAdapter(mv, access, name, descriptor);
    }


}
