package nick;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class Libraries {
    private static final String[] LIBS = {
            "avutil-ttv-51.dll",
            "freetype-jni-64.dll",
            "jinput-dx8.dll",
            "jinput-dx8_64.dll",
            "jinput-raw.dll",
            "jinput-raw_64.dll",
            "jinput-wintab.dll",
            "libmfxsw64.dll",
            "libmp3lame-ttv.dll",
            "lwjgl.dll",
            "lwjgl64.dll",
            "OpenAL32.dll",
            "OpenAL64.dll",
            "swresample-ttv-0.dll",
            "twitchsdk.dll"
    };

    private static void rebuild(final Path dir) {
        try {
            String path = System.getProperty("java.library.path") + File.pathSeparator + dir;
            System.setProperty("java.library.path", path);

            final Field field = ClassLoader.class.getDeclaredField("sys_paths");
            field.setAccessible(true);
            final String[] curr = (String[]) Objects.requireNonNull(field.get(null));
            final String[] patched = new String[curr.length + 1];
            System.arraycopy(curr, 0, patched, 0, curr.length);
            patched[patched.length - 1] = dir.toString();
            field.set(null, patched);
        } catch (Throwable _t) {
            throw new RuntimeException(_t);
        }
    }

    private static Path delete(final Path path) {
        path.toFile().deleteOnExit();
        return path;
    }

    public static void create() {
        try {
            final Path dir = delete(Files.createTempDirectory("augustus_natives"));

            for (final String lib : LIBS) {
                try (final InputStream is = Objects.requireNonNull(Libraries.class.getClassLoader().getResourceAsStream("natives/".concat(lib)))) {
                    final Path out = delete(dir.resolve(lib));
                    Files.deleteIfExists(out);
                    Files.copy(is, out);
                }
            }

            rebuild(dir);
        } catch (Throwable _t) {
            throw new RuntimeException(_t);
        }
    }

    public static URLClassLoader loader() {
        try {
            final Path dir = delete(Files.createTempDirectory("augustus_loader"));
            final Path jar = delete(dir.resolve("ElectricLauncher.jar"));

            try (final InputStream is = Objects.requireNonNull(Libraries.class.getClassLoader().getResourceAsStream("loader/ElectricLauncher.jar"))) {
                Files.deleteIfExists(jar);
                Files.copy(is, jar);
            }

            return new URLClassLoader(new URL[]{jar.toUri().toURL()});
        } catch (Throwable _t) {
            throw new RuntimeException(_t);
        }
    }
}