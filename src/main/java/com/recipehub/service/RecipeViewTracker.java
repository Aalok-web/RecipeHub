package com.recipehub.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Thread-safe, in-memory view counters shared by recipe detail requests.
 * Counts are intended for lightweight session-lifetime popularity feedback,
 * not analytics or durable reporting.
 */
public final class RecipeViewTracker {
    private final ConcurrentHashMap<Long, LongAdder> viewsByRecipe = new ConcurrentHashMap<>();

    /** Records one view for a valid recipe ID. */
    public void recordView(long recipeId) {
        if (recipeId <= 0) {
            throw new IllegalArgumentException("Recipe ID must be positive");
        }
        viewsByRecipe.computeIfAbsent(recipeId, ignored -> new LongAdder()).increment();
    }

    /** Returns the current count, or zero when the recipe has not been viewed. */
    public long getViewCount(long recipeId) {
        LongAdder count = viewsByRecipe.get(recipeId);
        return count == null ? 0L : count.sum();
    }
}
