package dev.pixelair.launcher;

/** Analytic critically damped spring: independent of display frame rate. Units are px and seconds. */
public final class SpringMotion {
    private final double target, displacement, velocity, omega;
    public SpringMotion(double start, double target, double velocity, double omega) {
        this.target = target;
        this.displacement = start - target;
        this.velocity = velocity;
        this.omega = omega;
    }
    public double position(double seconds) {
        double t = Math.max(0, seconds);
        return target + (displacement + (velocity + omega * displacement) * t) * Math.exp(-omega * t);
    }
    public double velocity(double seconds) {
        double t = Math.max(0, seconds);
        return (velocity - omega * (velocity + omega * displacement) * t) * Math.exp(-omega * t);
    }
    public static float rubberBand(float position, float first, float last, float limit) {
        if (position < first) return first - limit * (1 - (float)Math.exp((position - first) / limit));
        if (position > last) return last + limit * (1 - (float)Math.exp((last - position) / limit));
        return position;
    }
    public static float inverseRubberBand(float position, float first, float last, float limit) {
        if (position < first) return first + limit * (float)Math.log(Math.max(.001, 1 - (first - position) / limit));
        if (position > last) return last - limit * (float)Math.log(Math.max(.001, 1 - (position - last) / limit));
        return position;
    }
    public static float velocityFactor(float position, float first, float last, float limit) {
        float excess = position < first ? first - position : position > last ? position - last : 0;
        return Math.max(0, 1 - excess / limit);
    }

}
