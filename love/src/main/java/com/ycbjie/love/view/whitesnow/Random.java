package com.ycbjie.love.view.whitesnow;

class Random {

    private static final java.util.Random RANDOM = new java.util.Random();

    Random() {

    }

    float getRandom(float lower, float upper) {
        float min = Math.min(lower, upper);
        return getRandom(Math.max(lower, upper) - min) + min;
    }

    float getRandom(float upper) {
        return RANDOM.nextFloat() * upper;
    }

    int getRandom(int upper) {
        return RANDOM.nextInt(upper);
    }
}
