package io.smallrye.classfile.extras.constant;

import java.lang.constant.ClassDesc;
import java.util.Objects;

/**
 *
 */
public final class ExtraClassDesc {
    private ExtraClassDesc() {}

    public static ClassDesc ofInternalName(String name) {
        ConstantUtils.validateInternalClassName(Objects.requireNonNull(name));
        return ClassDesc.ofDescriptor("L" + name + ";");
    }
}
