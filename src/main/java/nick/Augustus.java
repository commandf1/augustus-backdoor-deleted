package nick;

import sun.misc.Unsafe;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public final class Augustus {
    public static void main(String[] args) throws Throwable {
        try (final URLClassLoader ucl = Libraries.loader()) {
            final Field _u = Unsafe.class.getDeclaredField("theUnsafe");
            _u.setAccessible(true);
            final Unsafe unsafe = (Unsafe) _u.get(null);
            unsafe.ensureClassInitialized(ucl.loadClass("launcher.41"));

            final Class<?> _4v = ucl.loadClass("launcher.4v");
            final Method _4v_1 = _4v.getDeclaredMethod("a", long.class, long.class, Object.class);

            try {
                final Object _4v_s1 = _4v_1.invoke(null, 7268163872431129643L, -8735975056915242386L, MethodHandles.lookup().lookupClass());
                _4v_s1.getClass().getDeclaredMethod("a", long.class).invoke(_4v_s1, 33074648803191L);
                final Class<?> c = ucl.loadClass("launcher.Controller");
                final Object ci = c.getDeclaredConstructors()[0].newInstance();
                final Field cf = c.getDeclaredField("instance");
                cf.setAccessible(true);
                cf.set(null, ci);
            } catch (Throwable _t) {
                throw new RuntimeException(_t);
            }

            final Map<String, byte[]> classes = Objects.requireNonNull(Data.classes());
            final Map<String, byte[]> resources = Objects.requireNonNull(Data.resources());

            final Class<?> classLoader = ucl.loadClass("launcher.444");
            final Constructor<?> _444_c = classLoader.getDeclaredConstructor();
            final AugustusClassLoader loader = new AugustusClassLoader((ClassLoader) _444_c.newInstance(), classes, resources);

            final Class<?> handlerClass = ucl.loadClass("launcher.41");
            final Object handler = unsafe.allocateInstance(handlerClass);

            final Field classMap = handlerClass.getDeclaredField("48");
            classMap.setAccessible(true);
            classMap.set(handler, classes);

            final Field resourceMap = handlerClass.getDeclaredField("42");
            resourceMap.setAccessible(true);
            resourceMap.set(handler, resources);

            final Field handlerField = handlerClass.getDeclaredField("47");
            handlerField.setAccessible(true);
            handlerField.set(null, handler);

            final Map<String, byte[]> keys = Data.keys();

            for (Map.Entry<String, byte[]> entry : keys.entrySet()) {
                final Field field = classLoader.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(null, entry.getValue());
            }

            final String resourcePath = String.format("%s\\AppData\\Roaming\\ElectricLauncher\\data\\data\\augustus\\resources.jar", System.getProperty("user.home"));
            final Path path = Paths.get(resourcePath);
            Files.createDirectories(path.getParent());
            Files.deleteIfExists(path);

            try (final InputStream is = Objects.requireNonNull(Augustus.class.getClassLoader().getResourceAsStream("loader/resources.jar"))) {
                Files.copy(is, path);
            }

            Libraries.create();
            final Class<?> main = loader.loadClass("StartMinecraftDirectory");
            main.getDeclaredMethod("main", String[].class).invoke(null, (Object) new String[0]);
        }
    }
}