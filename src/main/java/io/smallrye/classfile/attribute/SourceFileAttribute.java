/*
 * Copyright (c) 2022, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package io.smallrye.classfile.attribute;

import io.smallrye.classfile.Attribute;
import io.smallrye.classfile.AttributeMapper;
import io.smallrye.classfile.AttributeMapper.AttributeStability;
import io.smallrye.classfile.Attributes;
import io.smallrye.classfile.ClassElement;
import io.smallrye.classfile.ClassFile;
import io.smallrye.classfile.constantpool.Utf8Entry;

import io.smallrye.classfile.impl.BoundAttribute;
import io.smallrye.classfile.impl.TemporaryConstantPool;
import io.smallrye.classfile.impl.UnboundAttribute;

/**
 * Models the {@link Attributes#sourceFile() SourceFile} attribute (JVMS {@jvms
 * 4.7.10}), which indicates the name of the source file from which this {@code
 * class} file was compiled.
 * <p>
 * This attribute only appears on classes, and does not permit {@linkplain
 * AttributeMapper#allowMultiple multiple instances} in a class.  It has a data
 * dependency on the {@linkplain AttributeStability#CP_REFS constant pool}.
 * <p>
 * The attribute was introduced in the Java SE Platform version 5.0, major
 * version {@value ClassFile#JAVA_5_VERSION}.
 *
 * @see Attributes#sourceFile()
 * @jvms 4.7.10 The {@code SourceFile} Attribute
 * @since 24
 */
public sealed interface SourceFileAttribute
        extends Attribute<SourceFileAttribute>, ClassElement
        permits BoundAttribute.BoundSourceFileAttribute, UnboundAttribute.UnboundSourceFileAttribute {

    /**
     * {@return the name of the source file from which this class was compiled}
     */
    Utf8Entry sourceFile();

    /**
     * {@return a source file attribute}
     *
     * @param sourceFile the source file name
     */
    static SourceFileAttribute of(String sourceFile) {
        return of(TemporaryConstantPool.INSTANCE.utf8Entry(sourceFile));
    }

    /**
     * {@return a source file attribute}
     *
     * @param sourceFile the source file name
     */
    static SourceFileAttribute of(Utf8Entry sourceFile) {
        return new UnboundAttribute.UnboundSourceFileAttribute(sourceFile);
    }
}
