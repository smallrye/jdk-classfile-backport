/*
 * Copyright (c) 2022, Oracle and/or its affiliates. All rights reserved.
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
package io.github.dmlloyd.classfile;

import io.github.dmlloyd.classfile.attribute.CompilationIDAttribute;
import io.github.dmlloyd.classfile.attribute.DeprecatedAttribute;
import io.github.dmlloyd.classfile.attribute.EnclosingMethodAttribute;
import io.github.dmlloyd.classfile.attribute.InnerClassesAttribute;
import io.github.dmlloyd.classfile.attribute.ModuleAttribute;
import io.github.dmlloyd.classfile.attribute.ModuleHashesAttribute;
import io.github.dmlloyd.classfile.attribute.ModuleMainClassAttribute;
import io.github.dmlloyd.classfile.attribute.ModulePackagesAttribute;
import io.github.dmlloyd.classfile.attribute.ModuleResolutionAttribute;
import io.github.dmlloyd.classfile.attribute.ModuleTargetAttribute;
import io.github.dmlloyd.classfile.attribute.NestHostAttribute;
import io.github.dmlloyd.classfile.attribute.NestMembersAttribute;
import io.github.dmlloyd.classfile.attribute.PermittedSubclassesAttribute;
import io.github.dmlloyd.classfile.attribute.RecordAttribute;
import io.github.dmlloyd.classfile.attribute.RuntimeInvisibleAnnotationsAttribute;
import io.github.dmlloyd.classfile.attribute.RuntimeInvisibleTypeAnnotationsAttribute;
import io.github.dmlloyd.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import io.github.dmlloyd.classfile.attribute.RuntimeVisibleTypeAnnotationsAttribute;
import io.github.dmlloyd.classfile.attribute.SignatureAttribute;
import io.github.dmlloyd.classfile.attribute.SourceDebugExtensionAttribute;
import io.github.dmlloyd.classfile.attribute.SourceFileAttribute;
import io.github.dmlloyd.classfile.attribute.SourceIDAttribute;
import io.github.dmlloyd.classfile.attribute.SyntheticAttribute;
import io.github.dmlloyd.classfile.attribute.UnknownAttribute;

/**
 * A {@link ClassfileElement} that can appear when traversing the elements
 * of a {@link ClassModel} or be presented to a {@link ClassBuilder}.
 */
public sealed interface ClassElement extends ClassfileElement
        permits AccessFlags, Superclass, Interfaces, ClassfileVersion,
                FieldModel, MethodModel,
                CustomAttribute, CompilationIDAttribute, DeprecatedAttribute,
                EnclosingMethodAttribute, InnerClassesAttribute,
                ModuleAttribute, ModuleHashesAttribute, ModuleMainClassAttribute,
                ModulePackagesAttribute, ModuleResolutionAttribute, ModuleTargetAttribute,
                NestHostAttribute, NestMembersAttribute, PermittedSubclassesAttribute,
                RecordAttribute,
                RuntimeInvisibleAnnotationsAttribute, RuntimeInvisibleTypeAnnotationsAttribute,
                RuntimeVisibleAnnotationsAttribute, RuntimeVisibleTypeAnnotationsAttribute,
                SignatureAttribute, SourceDebugExtensionAttribute,
                SourceFileAttribute, SourceIDAttribute, SyntheticAttribute, UnknownAttribute {
}
