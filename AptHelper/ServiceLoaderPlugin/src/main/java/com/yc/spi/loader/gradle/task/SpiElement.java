package com.yc.spi.loader.gradle.task;

public class SpiElement implements Comparable<SpiElement> {

    final String name;
    final int priority;

    public SpiElement(final String name, final int priority) {
        this.name = name;
        this.priority = priority;
    }

    private SpiElement(final String name, final String priority) {
        this(name, parsePriority(priority));
    }

    @Override
    public int compareTo(final SpiElement o) {
        return this.priority > o.priority ? 1 : this.priority < o.priority ? -1 : 0;
    }

    public static int parsePriority(final String s) {
        try {
            return Integer.parseInt(s);
        } catch (final NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SpiElement)) {
            return false;
        }

        final SpiElement e = (SpiElement) o;
        return this.name.equals(e.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }


}
