package nick;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class AugustusClassLoader extends ClassLoader {
    public static AugustusClassLoader INSTANCE;
    private static final Map<String, Class<?>> CLASSES = new HashMap<>();
    private static final MethodHandle FIND;

    static {
        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final Method m = ClassLoader.class.getDeclaredMethod("findClass", String.class);
            m.setAccessible(true);
            FIND = lookup.unreflect(m);
        } catch (Throwable _t) {
            throw new RuntimeException(_t);
        }
    }

    private static byte[] read(final InputStream is) throws Throwable {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        final byte[] buf = new byte[8192];
        int r;
        while ((r = is.read(buf)) != -1) os.write(buf, 0, r);
        return os.toByteArray();
    }

    private final ClassLoader delegate;
    private final Map<String, byte[]> cached, classes, resources;

    public AugustusClassLoader(final ClassLoader delegate, final Map<String, byte[]> classes, final Map<String, byte[]> resources) {
        INSTANCE = this;
        this.delegate = delegate;
        this.cached = Objects.requireNonNull(Data.cache());
        this.classes = classes;
        this.resources = resources;
        this.overwrite();

        try {
            patch("472g");
            patch("47eJ");
            patch("47Ua");
            patch("47cq");
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
        }
    }

    private void patch(final String klass) {
        try {
            cached.put("net.".concat(klass), read(Objects.requireNonNull(AugustusClassLoader.class.getClassLoader().getResourceAsStream(klass.concat(".class")))));
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
        }
    }

    private void overwrite() {
        try {
            final Field field = ClassLoader.class.getDeclaredField("parent");
            field.setAccessible(true);
            field.set(delegate, this);
        } catch (Throwable _t) {
            throw new RuntimeException(_t);
        }
    }

    private static String format(final String name) {
        return name.replace('.', '/').concat(".class");
    }

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        try {
            return CLASSES.computeIfAbsent(name, key -> {
                try {
                    return this.findClass(key);
                } catch (Throwable _t) {
                    if (!(_t instanceof ClassNotFoundException)) {
                        _t.printStackTrace(System.err);
                        System.exit(0);
                    }
                }

                return null;
            });
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
            throw new ClassNotFoundException(name);
        }
    }

    @Override
    public Class<?> findClass(final String name) throws ClassNotFoundException {
        try {
            if (cached.containsKey(name)) {
                final byte[] data = Objects.requireNonNull(cached.getOrDefault(name, null));
                return defineClass(name, data, 0, data.length);
            }

            if (classes.containsKey(format(name))) {
                final byte[] data = Objects.requireNonNull(classes.getOrDefault(format(name), null));
                return defineClass(name, data, 0, data.length);
            }
        } catch (ClassFormatError _t) {
            throw new RuntimeException("Failed to load " + name, _t);
        }

        try {
            final Class<?> k = (Class<?>) FIND.invoke(delegate, name);
            return k != null ? k : super.findClass(name);
        } catch (Throwable _t) {
            if (_t instanceof ClassNotFoundException) throw ((ClassNotFoundException) _t);
            throw new RuntimeException(_t);
        }
    }

    @Override
    public InputStream getResourceAsStream(final String name) {
        if (resources.containsKey(name)) {
            return new ByteArrayInputStream(resources.get(name));
        }

        if (name.equals("net/my.Sus.top.main.bre")) {
            return new ByteArrayInputStream(new byte[]{118, -105, 63, 109, -36, 29, -96, 101, -18, -11, 8, -13, -58, 27, -51, 30, -71, 6, -111, 72, 66, 84, 21, -110, 62, -128, 5, 83, 60, 39, -100, 70});
        } else if (name.equals("net/Cmw5FzWZE1s1fngeD5LW2wQ5mrdceWxFugsLrgE7")) {
            return new ByteArrayInputStream(new byte[]{107, 119, 97, 115, 103, 120, 97, 85, 74, 70, 52, 68, 83, 54, 121, 66, 80, 111, 105, 109, 116, 78, 77, 89, 70, 65, 100, 75, 104, 90, 73, 99, 77, 106, 87, 98, 73, 43, 107, 110, 74, 50, 69, 61, 35, 35, 83, 83, 56, 56, 35, 35, 98, 48, 115, 47, 56, 103, 112, 81, 71, 109, 70, 52, 103, 50, 81, 77, 70, 87, 98, 49, 104, 111, 70, 83, 67, 54, 119, 107, 85, 112, 75, 83, 49, 90, 87, 83, 56, 111, 67, 122, 83, 57, 52, 61});
        }

        try {
            return delegate.getResourceAsStream(name);
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
            System.err.flush();
            return new ByteArrayInputStream(new byte[0]);
        }
    }

    @Override
    public URL getResource(final String name) {
        return delegate.getResource(name);
    }

    public static Class<?> defineClassProxy(final String name, final byte[] data, final int offset, final int len) {
        return INSTANCE.defineClass(name, data, offset, len);
    }
}