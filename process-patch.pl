#!/usr/bin/perl

use strict;
use warnings;

chdir "jdk";

my $usage = "Usage: $0 <old-JDK-rev> <new-JDK-rev>";

my $from = shift @ARGV or die $usage;
my $to = shift @ARGV or die $usage;

open my $git_fh, "-|", "git", "diff", "-M75", $from."..".$to, "--", "src/java.base/share/classes/java/lang/classfile", "src/java.base/share/classes/jdk/internal/classfile/impl"
    or die "Failed to run git: $?";

while ($_ = <$git_fh>) {
    s[jdk\.internal\.javac\.PreviewFeature][io.github.dmlloyd.classfile.extras.PreviewFeature]g;
    s[java\.lang\.reflect\.AccessFlag][io.github.dmlloyd.classfile.extras.reflect.AccessFlag]g;
    s[java\.lang\.reflect\.ClassFileFormatVersion][io.github.dmlloyd.classfile.extras.reflect.ClassFileFormatVersion]g;
    s[java\.lang\.constant\.ModuleDesc][io.github.dmlloyd.classfile.extras.constant.ModuleDesc]g;
    s[java\.lang\.constant\.PackageDesc][io.github.dmlloyd.classfile.extras.constant.PackageDesc]g;
    s[ConstantDescs\.INIT_NAME][ExtraConstantDescs.INIT_NAME]g;
    s[ConstantDescs\.CLASS_INIT_NAME][ExtraConstantDescs.CLASS_INIT_NAME]g;
    s[ClassDesc\.ofInternalName][ExtraClassDesc.ofInternalName]g;
    s[java(.)lang.classfile][io$1github$1dmlloyd$1classfile]g;
    s[jdk(.)internal.classfile][io$1github$1dmlloyd$1classfile]g;
    s[jdk(.)internal.constant][io$1github$1dmlloyd$1classfile$1extras$1constant]g;
    s[src/java\.base/share/classes][src/main/java]g;
    print $_;
}
