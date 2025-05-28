package nick;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class Connection {
    public static AtomicBoolean LOCK = new AtomicBoolean(true);
    public static Object INSTANCE;
    public static Object KO;

    public static void end() throws Throwable {
        LOCK.set(false);

        final Field _u = Unsafe.class.getDeclaredField("theUnsafe");
        _u.setAccessible(true);
        final Unsafe unsafe = (Unsafe) _u.get(null);

        final Class<?> k = AugustusClassLoader.INSTANCE.loadClass("net.40U");
        final Field _inst = k.getDeclaredField("45");
        _inst.setAccessible(true);
        final Object inst = _inst.get(null);

        final Field f1 = k.getDeclaredField("45G");
        f1.setAccessible(true);
        f1.set(inst, f1.getType().getDeclaredConstructor().newInstance());

        final Field f2 = k.getDeclaredField("45h");
        f2.setAccessible(true);
        f2.set(inst, f2.getType().getDeclaredConstructor().newInstance());

        final Field f3 = k.getDeclaredField("47");
        f3.setAccessible(true);
        final Object _479q = unsafe.allocateInstance(f3.getType());
        final Object eventBus = AugustusClassLoader.INSTANCE.loadClass("net.47yA").getDeclaredMethod("49").invoke(null);
        final Method subscribe = eventBus.getClass().getDeclaredMethod("subscribe", int.class, Object.class);
        subscribe.invoke(eventBus, 32, _479q);
        f3.set(inst, _479q);

        final Field f4 = k.getDeclaredField("45y");
        f4.setAccessible(true);
        final long l = 0x7b98d0ee355cL ^ ((0x64a2ea4d2b93L ^ (128610728015459L ^ 0x42B400C4E94BL)) ^ 0x1E3771766B99L);
        f4.set(inst, f4.getType().getDeclaredConstructor(long.class).newInstance(l));

        final Field f5 = k.getDeclaredField("40");
        f5.setAccessible(true);
        f5.set(inst, f5.getType().getDeclaredConstructor().newInstance());
    }

    static {
        try {
            final Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            final Unsafe unsafe = (Unsafe) f.get(null);
            INSTANCE = unsafe.allocateInstance(AugustusClassLoader.INSTANCE.loadClass("net.47Ua"));
            final Field atomic = INSTANCE.getClass().getDeclaredField("47h");;
            atomic.setAccessible(true);
            atomic.set(INSTANCE, LOCK);
            final Field ko = INSTANCE.getClass().getDeclaredField("47T");
            ko.setAccessible(true);
            KO = unsafe.allocateInstance(ko.getType());
            ko.set(INSTANCE, KO);
            final Field mag = KO.getClass().getDeclaredField("42");
            mag.setAccessible(true);
            mag.set(KO, new int[0]);
            final Field queue = INSTANCE.getClass().getDeclaredField("47M");
            queue.setAccessible(true);
            queue.set(INSTANCE, new ConcurrentLinkedQueue<>());
            final Field lock = INSTANCE.getClass().getDeclaredField("47g");
            lock.setAccessible(true);
            lock.set(INSTANCE, new ReentrantReadWriteLock());
            final Field decoder = INSTANCE.getClass().getDeclaredField("41");
            decoder.setAccessible(true);
            decoder.set(INSTANCE, AugustusClassLoader.INSTANCE.loadClass("net.47S5").getDeclaredConstructor().newInstance());
        } catch (Throwable _t) {
            throw new RuntimeException(_t);
        }
    }
}