package org.aion.avm.java.lang;

import org.aion.avm.internal.IObject;

public class Boolean extends Object implements Comparable<Boolean>{

    public static final Boolean avm_TRUE = new Boolean(true);

    public static final Boolean avm_FALSE = new Boolean(false);

    public static final Class<Boolean> avm_TYPE = new Class(java.lang.Boolean.TYPE);

    private final boolean value;

    public Boolean(boolean b) {
        this.value = b;
    }

    public Boolean(String s) {
        this(avm_parseBoolean(s));
    }

    public static boolean avm_parseBoolean(String s){
        return java.lang.Boolean.parseBoolean(s.getUnderlying());
    }

    public boolean avm_booleanValue() {
        return value;
    }

    public static Boolean avm_valueOf(boolean b) {
        return b ? avm_TRUE : avm_FALSE;
    }

    public static Boolean avm_valueOf(String s) {
        return avm_parseBoolean(s) ? avm_TRUE : avm_FALSE;
    }

    public static String avm_toString(boolean b) {
        return b ? (new String("true")) : (new String("false"));
    }

    public String avm_toString() {
        return value ? (new String("true")) : (new String("false"));
    }

    @Override
    public int avm_hashCode() {
        return Boolean.avm_hashCode(value);
    }

    public static int avm_hashCode(boolean value) {
        return value ? 1231 : 1237;
    }

    public boolean avm_equals(IObject obj) {
        if (obj instanceof Boolean) {
            return value == ((Boolean)obj).avm_booleanValue();
        }
        return false;
    }

    public int avm_compareTo(Boolean b) {
        return avm_compare(this.value, b.value);
    }

    public static int avm_compare(boolean x, boolean y) {
        return (x == y) ? 0 : (x ? 1 : -1);
    }

    public static boolean avm_logicalAnd(boolean a, boolean b) {
        return a && b;
    }

    public static boolean avm_logicalOr(boolean a, boolean b) {
        return a || b;
    }

    public static boolean avm_logicalXor(boolean a, boolean b) {
        return a ^ b;
    }

    //=======================================================
    // Methods below are used by runtime and test code only!
    //========================================================

    @Override
    public boolean equals(java.lang.Object obj) {
        return obj instanceof Boolean && this.value == ((Boolean) obj).value;
    }

    @Override
    public java.lang.String toString() {
        return java.lang.Boolean.toString(this.value);
    }

    //========================================================
    // Methods below are excluded from shadowing
    //========================================================

    //public static boolean avm_getBoolean(java.lang.String name){}

}
