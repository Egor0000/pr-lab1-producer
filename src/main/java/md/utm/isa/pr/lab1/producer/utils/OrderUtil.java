package md.utm.isa.pr.lab1.producer.utils;

import java.util.concurrent.atomic.AtomicLong;

public class OrderUtil {
    private static final AtomicLong id = new AtomicLong(0);

    public static long getNextOrderId() {
        return id.incrementAndGet();
    }
}
