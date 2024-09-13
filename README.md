Unofficial JDK Classfile API Backport
========

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.dmlloyd/jdk-classfile-backport/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.dmlloyd/jdk-classfile-backport)


This is a backport to JDK 17 of the new classfile API found in JDK 21 and later.

Bugs in this project should be reported to https://github.com/dmlloyd/jdk-classfile-backport/issues first. Bugs in this project are likely to be a result of backporting. Some bugs might be relayed upstream by the project maintainer(s), subject to testing and verification; in this case, the upstream bug will be linked for easier tracking.

Releases
--------

Releases of the project roughly track releases of the corresponding JDK from which it is backported. This means that version 23.x of this project corresponds to the state of the upstream classfile API in JDK 23, and so on.

The upstream classfile API is expected to leave preview for Java 24. At that time, it is expected that binary compatibility will be maintained with a strictness corresponding to that of the upstream API. Until then, there can (and will) be various API breakages which occur between major versions. Keep this in mind when planning integration into new projects.

It is currently planned to continue to backport features from Java versions beyond 24. The major version of this project will continue to correspond to the JDK from which the changes were backported. When planning a transition from this library to the official API, be sure that the major version of this library corresponds to the target JDK to avoid a situation where you start using features which are not available in the JDK version you want to target, causing difficulties when migrating.

The release schedule is fairly ad-hoc and irregular. If you encounter a bug which has been fixed in this project but not yet released, feel free to open an issue to request a release.

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
