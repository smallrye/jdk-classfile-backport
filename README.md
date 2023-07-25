Unofficial JDK Classfile API Backport
========

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.dmlloyd/jdk-classfile-preview/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.dmlloyd/jdk-classfile-preview)


This is a (very) unofficial backport to JDK 17 of the new classfile API found in JDK 21 and later. That API is non-public (not even preview) at the time of this writing, so this project gives no compatibility guarnatees and in fact may break compatibility at any time with no notice.

However, it should suffice to allow projects to test out the new API and give feedback which could then potentially even be relayed to the upstream project.

Bugs in this project should be reported to https://github.com/dmlloyd/jdk-classfile-preview/issues first. Bugs in this project are likely to be a result of backporting. Some bugs might be relayed upstream by the project maintainer(s), subject to testing and verification; in this case, the upstream bug will be linked for easier tracking.

Releases
--------

There is, at present, no release schedule. The maintainer(s) may, at their discretion and at any time, update the upstream snapshot, and manually apply differences from the upstream project into the downstream backport.

If you encounter a bug which has been fixed in this project but not yet released, feel free to open an issue to request a release.

Getting started
---------------

After adding the appropriate Maven dependency (see the Maven release badge above), the easiest entry points are:

For parsing a class:

```java
byte[] b = Files.readAllBytes(Path.of("some/file.class"));
ClassModel model = Classfile.of().parse(b);
// now, do something with `model`...
```

Or for writing a class:

```java
byte[] b = Classfile.of().build(classDesc, classBuilder -> {
    // ... build the class here ...
});
```
