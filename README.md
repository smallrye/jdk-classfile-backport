Unofficial JDK Classfile API Backport
========

[![Maven Central](https://img.shields.io/maven-central/v/io.smallrye.classfile/jdk-classfile-backport?color=green)](https://search.maven.org/search?q=g:io.smallrye.classfile%20AND%20a:jdk-classfile-backport)

This is a backport to JDK 17 of the new classfile API found in JDK 21 and later.

Bugs in this project should be reported to [the GitHub issue tracker](https://github.com/smallrye/jdk-classfile-backport/issues) first. Bugs in this project are likely to be a result of backporting. Some bugs might be relayed upstream by the project maintainer(s), subject to testing and verification; in this case, the upstream bug will be linked for easier tracking.

Relocation notice
-----------------

This project has been relocated from old to new Maven coordinates, and also has a new package name, as of version 26.

Please use the new Maven coordinates in your dependency declaration:

```xml
    <dependency>
        <groupId>io.smallrye.classfile</groupId>
        <artifactId>jdk-classfile-backport</artifactId>
    </dependency>
```

The classfile API now has a base package name of `io.smallrye.classfile`. Generally it is sufficient to search-and-replace the old package name (`io.github.dmlloyd.classfile`) in your projects to use the new API version.

Releases
--------

Releases of the project roughly track releases of the corresponding JDK from which it is backported. This means that version 24.x of this project corresponds to the state of the upstream classfile API in JDK 24, and so on.

Binary compatibility is maintained with a strictness corresponding to that of the upstream API.

It is currently planned to continue to backport features indefinitely. The major version of this project will continue to correspond to the JDK from which the changes were backported. When planning a transition from this library to the official API, be sure that the major version of this library corresponds to the target JDK to avoid a situation where you start using features which are not available in the JDK version you want to target, causing difficulties when migrating.

The release schedule is fairly ad-hoc and irregular, but will generally align with that of the upstream JDK. If you encounter a bug which has been fixed in this project but not yet released, feel free to open an issue to request a release.

Getting started
---------------

After adding the appropriate Maven dependency (see the Maven release badge above), the easiest entry points are:

For parsing a class:

```java
byte[] b = Files.readAllBytes(Path.of("some/file.class"));
ClassModel model = ClassFile.of().parse(b);
// now, do something with `model`...
```

Or for writing a class:

```java
byte[] b = ClassFile.of().build(classDesc, classBuilder -> {
    // ... build the class here ...
});
```
More information
----------------

For more information on this API, see:

* [JEP 484](https://openjdk.org/jeps/484)
* [The official upstream documentation at Oracle](https://docs.oracle.com/en/java/javase/24/docs/api/java.base/java/lang/classfile/package-summary.html)
