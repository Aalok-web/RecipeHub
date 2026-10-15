package com.recipehub.service;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class RecipeViewTrackerTest {

    @Test
    public void countsConcurrentViewsWithoutLosingIncrements() throws InterruptedException {
        RecipeViewTracker tracker = new RecipeViewTracker();
        int workers = 8;
        int incrementsPerWorker = 2_000;
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(workers);
        List<java.util.concurrent.Future<?>> tasks = new ArrayList<>();

        try {
            for (int i = 0; i < workers; i++) {
                tasks.add(pool.submit(() -> {
                    start.await();
                    for (int j = 0; j < incrementsPerWorker; j++) {
                        tracker.recordView(42L);
                    }
                    return null;
                }));
            }

            start.countDown();
            for (java.util.concurrent.Future<?> task : tasks) {
                try {
                    task.get(10, TimeUnit.SECONDS);
                } catch (java.util.concurrent.ExecutionException e) {
                    throw new AssertionError("Concurrent counter task failed", e.getCause());
                } catch (java.util.concurrent.TimeoutException e) {
                    throw new AssertionError("Concurrent counter task timed out", e);
                }
            }

            assertEquals((long) workers * incrementsPerWorker, tracker.getViewCount(42L));
            assertEquals(0L, tracker.getViewCount(999L));
        } finally {
            pool.shutdownNow();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNonPositiveRecipeIds() {
        new RecipeViewTracker().recordView(0L);
    }
}
