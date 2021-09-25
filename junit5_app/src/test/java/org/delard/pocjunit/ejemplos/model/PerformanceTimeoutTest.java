package org.delard.pocjunit.ejemplos.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTimeout;


class PerformanceTimeoutTest extends ClasePadreTest{

    @Test
    @Tag("timeout")
    @Timeout(1)
    void pruebaTimeout() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(900);
    }

    @Test
    @Tag("timeout")
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    void pruebaTimeout2() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(900);
    }

    @Test
    @Tag("timeout")
    void testTimeoutAssertions() {
        assertTimeout(Duration.ofSeconds(5), () ->
            TimeUnit.MILLISECONDS.sleep(4000)
        );
    }

}
