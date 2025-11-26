///usr/bin/env jbang "$0" "$@" ; exit $?

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 */
public class ProcessPatch {
    public static final List<Path> paths = Stream.of(
        "src/java.base/share/classes/java/lang/classfile",
        "src/java.base/share/classes/jdk/internal/classfile",
        "src/java.base/share/classes/java/lang/reflect/AccessFlag.java",
        "src/java.base/share/classes/java/lang/reflect/ClassFileFormatVersion.java",
        "src/java.base/share/classes/java/lang/constant/ModuleDesc.java",
        "src/java.base/share/classes/java/lang/constant/PackageDesc.java",
        "src/java.base/share/classes/jdk/internal/constant/ConstantUtils.java",
        "src/java.base/share/classes/jdk/internal/constant/ModuleDescImpl.java",
        "src/java.base/share/classes/jdk/internal/constant/PackageDescImpl.java"
    ).map(Path::of).toList();

    public static final List<Replacement> replacements = List.of(
        new Replacement("@Stable", "/*@Stable*/"),
        new Replacement("@AOTSafeClassInitializer", "/*@AOTSafeClassInitializer*/"),
        new Replacement("@ForceInline", "/*@ForceInline*/"),
        new Replacement("case [A-Za-z_][A-Za-z0-9_]*(?:\\.[A-Za-z_][A-Za-z0-9_]*)*(?:<[^>]+>)? [A-Za-z_][A-Za-z0-9_]* ->.*", "//$0"),
        new Replacement("import jdk.internal.access.JavaLangAccess", "import static io.smallrye.classfile.impl.BackportUtil.JLA"),
        new Replacement("import jdk.internal.util.ArraysSupport", "import static io.smallrye.classfile.impl.BackportUtil.ArraysSupport"),
        new Replacement("private static final JavaLangAccess JLA = SharedSecrets.getJavaLangAccess\\(\\);", "//$0"),
        new Replacement("jdk\\.internal\\.javac\\.PreviewFeature", "io.smallrye.classfile.extras.PreviewFeature"),
        new Replacement("java(.)lang.reflect.(AccessFlag|ClassFileFormatVersion)", "io$1smallrye$1classfile$1extras$1reflect$1$2"),
        new Replacement("java(.)lang.constant.(ModuleDesc|PackageDesc)", "io$1smallrye$1classfile$1extras$1constant$1$2"),
        new Replacement("jdk(.)internal.constant.(ModuleDescImpl|PackageDescImpl|ConstantUtils)", "io$1smallrye$1classfile$1extras$1constant$1$2"),
        new Replacement("ConstantDescs\\.INIT_NAME", "ExtraConstantDescs.INIT_NAME"),
        new Replacement("ConstantDescs\\.CLASS_INIT_NAME", "ExtraConstantDescs.CLASS_INIT_NAME"),
        new Replacement("ClassDesc\\.ofInternalName", "ExtraClassDesc.ofInternalName"),
        new Replacement("java(.)lang.classfile", "io$1smallrye$1classfile"),
        new Replacement("jdk(.)internal.classfile", "io$1smallrye$1classfile"),
        new Replacement("(import jdk\\.internal\\.constant\\.(ReferenceClass|PrimitiveClass|ClassOrInterface)DescImpl.*)", "//$1"),
        new Replacement("jdk(.)internal.constant", "io$1smallrye$1classfile$1extras$1constant"),
        new Replacement("package java\\.lang\\.reflect", "package io.smallrye.classfile.extras.reflect"),
        new Replacement("package java\\.lang\\.constant", "package io.smallrye.classfile.extras.constant"),
        new Replacement("src/java\\.base/share/classes", "src/main/java"),
        new Replacement("(import jdk\\.internal\\..*)", "//$1"),
        new Replacement("(import sun\\..*)", "//$1")
    );

    public static void main(String[] argsArray) throws IOException {
        Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown thread") {
            public void run() {
                mainThread.interrupt();
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    throw new Error("Interrupted waiting for exit");
                }
            }
        });
        List<String> args = List.of(argsArray);
        Iterator<String> iter = args.iterator();
        String command = iter.next();
        switch (command) {
            case "patch" -> {
                // generate a patch
                String from = iter.next();
                String to = iter.next();
                try (CloseableProcess cp = runGit(from, to)) {
                    try (BufferedReader br = cp.process().inputReader(StandardCharsets.UTF_8)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            System.out.println(replaceAll(line));
                        }
                    }
                }
            }
            case "current" -> {
                boolean ws = false;
                if (iter.hasNext() && iter.next().equals("-w")) {
                    ws = true;
                }
                // do a full comparison of the current status
                for (Path path : paths) {
                    fullDiff(path, ws);
                }
            }
        }
    }

    public static String replaceAll(String orig) {
        for (Replacement replacement : replacements) {
            orig = replacement.pattern().matcher(orig).replaceAll(replacement.replacement());
        }
        return orig;
    }

    private static void fullDiff(final Path path, final boolean ws) throws IOException {
        Path jdkPath = Path.of("jdk").resolve(path);
        if (Files.isDirectory(jdkPath)) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(jdkPath)) {
                for (Path subPath : ds) {
                    fullDiff(path.resolve(subPath.getFileName()), ws);
                }
            }
        } else {
            // not only do we diff the content, we also diff the path
            Path srcPath = Path.of(replaceAll(path.toString()));
            Path destPath = srcPath;
            if (! Files.exists(srcPath)) {
                srcPath = Path.of("/dev/null");
            }
            // the path is actually in the JDK directory
            List<String> command = new ArrayList<>(15);
            command.add("git");
            command.add("--no-pager");
            command.add("diff");
            if (ws) command.add("-w");
            command.add("--no-index");
            command.add("-M75");
            command.add("--");
            command.add(srcPath.toString());
            command.add("-");
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            pb.redirectInput(ProcessBuilder.Redirect.PIPE);
            pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
            try (CloseableProcess cp = new CloseableProcess(pb.start())) {
                try (CloseableThread ignored = new CloseableThread(() -> {
                    try (BufferedReader br = cp.process().inputReader(StandardCharsets.UTF_8)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.equals("+++ -")) {
                                System.out.println("+++ " + destPath);
                            } else if (line.equals("diff --git - -")) {
                                System.out.println("diff --git " + destPath + " " + destPath);
                            } else {
                                System.out.println(line);
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("Failed to process output: " + e);
                    }
                })) {
                    try (BufferedWriter bw = cp.process().outputWriter(StandardCharsets.UTF_8)) {
                        try (BufferedReader br = Files.newBufferedReader(jdkPath)) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                bw.write(replaceAll(line));
                                bw.write(System.lineSeparator());
                            }
                        }
                    }
                }
            }
        }
    }

    public static CloseableProcess runGit(String from, String to) throws IOException {
        List<String> command = Stream.concat(Stream.of(
            "git",
            "diff",
            "-M75",
            from + ".." + to,
            "--"
        ), paths.stream().map(Path::toString)).toList();
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        pb.redirectInput(ProcessBuilder.Redirect.from(new File("/dev/null")));
        pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
        return new CloseableProcess(pb.start());
    }

    public record Replacement(Pattern pattern, String replacement) {
        public Replacement(String pattern, String replacement) {
            this(Pattern.compile(pattern), replacement);
        }
    }

    public record CloseableThread(Thread thread) implements AutoCloseable {
        public CloseableThread {
            thread.start();
        }

        public CloseableThread(Runnable task) {
            this(new Thread(task));
        }

        public void close() {
            boolean intr = false;
            try {
                for (; ; )
                    try {
                        thread.join();
                        return;
                    } catch (InterruptedException e) {
                        intr = true;
                        thread.interrupt();
                    }
            } finally {
                if (intr) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public record CloseableProcess(Process process, String commandLine) implements Closeable {
        public CloseableProcess(Process process) {
            this(process, process.info().commandLine().orElse(""));
        }

        public void close() {
            boolean intr = false;
            try {
                for (; ; )
                    try {
                        process.waitFor();
                        return;
                    } catch (InterruptedException e) {
                        intr = true;
                        process.destroy();
                    }
            } finally {
                if (intr) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
