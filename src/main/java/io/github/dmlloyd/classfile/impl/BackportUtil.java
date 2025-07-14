package io.github.dmlloyd.classfile.impl;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utilities specific to the backport.
 */
public final class BackportUtil {
    private BackportUtil() {}

    // this trick helps to keep a smaller diff up above
    public static class JLA {
        public static int uncheckedCountPositives(byte[] ba, int off, int len) {
            int limit = off + len;
            for (int i = off; i < limit; i++) {
                if (ba[i] < 0) {
                    return i - off;
                }
            }
            return len;
        }
        public static void uncheckedInflateBytesToChars(byte[] src, int srcOff, char[] dst, int dstOff, int len) {
            for (int i = 0; i < len; i++) {
                dst[dstOff++] = (char) Byte.toUnsignedInt(src[srcOff++]);
            }
        }
    }

    public static class ArraysSupport {
        public static int hashCodeOfUnsigned(byte[] a, int fromIndex, int length, int initialValue) {
            return switch (length) {
                case 0 -> initialValue;
                case 1 -> 31 * initialValue + Byte.toUnsignedInt(a[fromIndex]);
                default -> unsignedHashCode(initialValue, a, fromIndex, length);
            };
        }
        private static int unsignedHashCode(int result, byte[] a, int fromIndex, int length) {
            int end = fromIndex + length;
            for (int i = fromIndex; i < end; i++) {
                result = 31 * result + Byte.toUnsignedInt(a[i]);
            }
            return result;
        }
    }

    public static <T, E extends Exception> T throwAsObj(Function<String, E> factory, String msg) throws E {
        throw factory.apply(msg);
    }

    public static <T, E extends Exception> T throwAsObj(Supplier<E> factory) throws E {
        throw factory.get();
    }

    public static <E extends Exception> int throwAsInt(Function<String, E> factory, String msg) throws E {
        throw factory.apply(msg);
    }

    public static <E extends Exception> int throwAsInt(Supplier<E> factory) throws E {
        throw factory.get();
    }
}
