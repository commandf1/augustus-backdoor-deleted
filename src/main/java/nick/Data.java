package nick;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.InflaterInputStream;

public final class Data {
    public static InputStream decompress(final InputStream compressed) throws IOException {
        try (final InflaterInputStream inflater = new InflaterInputStream(compressed); final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            final byte[] buf = new byte[4096];
            int len;
            while ((len = inflater.read(buf)) > 0) out.write(buf, 0, len);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private static String readString(final DataInputStream dis) throws Throwable {
        final byte[] data = new byte[dis.readInt()];
        dis.readFully(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    private static char[] readCharArray(final DataInputStream dis) throws Throwable {
        final byte coder = dis.readByte();
        final char[] c = new char[dis.readInt()];

        for (int i = 0; i < c.length; i++) {
            final byte lo = dis.readByte();
            final byte hi = dis.readByte();
            c[i] = (char) ((hi << 8) | (lo & 0xFF));
        }

        return c;
    }

    private static byte[] readByteArray(final DataInputStream dis) throws Throwable {
        final byte[] b = new byte[dis.readInt()];
        dis.readFully(b);
        return b;
    }

    public static Map<String, byte[]> resources() {
        try {
            final Map<String, byte[]> resources = new HashMap<>();
            final DataInputStream dis = new DataInputStream(decompress(Objects.requireNonNull(Data.class.getClassLoader().getResourceAsStream("data/resources.bin"))));

            while (dis.available() > 0) {
                final String key = new String(readCharArray(dis));
                final byte[] data = readByteArray(dis);
                resources.put(key, data);
            }

            dis.close();

            return resources;
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
            return null;
        }
    }

    public static Map<String, byte[]> classes() {
        try {
            final Map<String, byte[]> classes = new HashMap<>();
            final DataInputStream dis = new DataInputStream(decompress(Objects.requireNonNull(Data.class.getClassLoader().getResourceAsStream("data/classes.bin"))));

            while (dis.available() > 0) {
                final String key = new String(readCharArray(dis));
                final byte[] data = readByteArray(dis);
                classes.put(key, data);
            }

            dis.close();

            return classes;
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
            return null;
        }
    }

    public static Map<String, byte[]> cache() {
        try {
            final Map<String, byte[]> cache = new HashMap<>();
            final DataInputStream dis = new DataInputStream(Objects.requireNonNull(Data.class.getClassLoader().getResourceAsStream("data/cache.bin")));

            while (dis.available() > 0) {
                final String name = readString(dis).replace('/', '.');
                final byte[] klass = readByteArray(dis);
                cache.put(name, klass);
            }

            return cache;
        } catch (Throwable _t) {
            throw new RuntimeException(_t);
        }
    }

    public static Map<String, byte[]> keys() {
        try {
            final Map<String, byte[]> cache = new HashMap<>();
            final DataInputStream dis = new DataInputStream(Objects.requireNonNull(Data.class.getClassLoader().getResourceAsStream("data/keys.bin")));

            while (dis.available() > 0) {
                final String name = readString(dis).replace('/', '.');
                final byte[] klass = readByteArray(dis);
                cache.put(name, klass);
            }

            return cache;
        } catch (Throwable _t) {
            throw new RuntimeException(_t);
        }
    }
}