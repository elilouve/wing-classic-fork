package com.elilouve.wingsclassic.util;

/** Fonctions mathématiques reprises du mod Wings d'origine (MIT, Pau101). Remplace la version du lot A. */
public final class WingsMath {
    private WingsMath() {}

    public static final float PI = (float) Math.PI;

    public static float toRadians(final float degrees) {
        return degrees * (float) (Math.PI / 180.0D);
    }

    public static float toDegrees(final float radians) {
        return radians * (float) (180.0D / Math.PI);
    }

    public static float lerp(final float a, final float b, final float t) {
        return t <= 0.0F ? a : t >= 1.0F ? b : a + (b - a) * t;
    }

    public static float lerpDegrees(final float a, final float b, final float t) {
        return a + t * difference(a, b, 360.0F);
    }

    public static double lerpDegrees(final double a, final double b, final double t) {
        return a + t * difference(a, b, 360.0D);
    }

    private static float difference(final float a, final float b, final float rot) {
        return mod(b - a + rot / 2.0F, rot) - rot / 2.0F;
    }

    private static double difference(final double a, final double b, final double rot) {
        return mod(b - a + rot / 2.0D, rot) - rot / 2.0D;
    }

    public static float mod(final float a, final float b) {
        return (a % b + b) % b;
    }

    public static double mod(final double a, final double b) {
        return (a % b + b) % b;
    }

    public static float easeInOut(final float t) {
        return -((float) Math.cos(PI * t) - 1.0F) / 2.0F;
    }

    public static float easeOutCirc(final float t) {
        return (float) Math.sqrt(1.0F - (t - 1.0F) * (t - 1.0F));
    }

    public static float transform(final float x, final float domainMin, final float domainMax, final float rangeMin, final float rangeMax) {
        if (x <= domainMin) {
            return rangeMin;
        }
        if (x >= domainMax) {
            return rangeMax;
        }
        return (rangeMax - rangeMin) * (x - domainMin) / (domainMax - domainMin) + rangeMin;
    }
}
