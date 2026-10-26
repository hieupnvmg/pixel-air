package dev.pixelair.launcher;

/** Pixel positions and velocities; independent of Android for deterministic checks. */
public final class SwipePhysics {
    private SwipePhysics() {}

    public static int target(float position, float travel, float velocity,
                             int first, int last, int width, float minFling, float minTravel) {
        int page;
        if (Math.abs(velocity) >= minFling && Math.abs(travel) >= minTravel) {
            // A reversal flick returns toward the finger, regardless of initial direction.
            page = velocity < 0 ? (int)Math.ceil(position / width)
                                : (int)Math.floor(position / width);
        } else {
            page = Math.round(position / width);
        }
        return Math.max(first, Math.min(last, page));
    }

    public static int duration(float remaining, float velocity, int width) {
        float distance = Math.abs(remaining);
        int millis = Math.abs(velocity) > 1
                ? Math.round(1000 * distance / Math.abs(velocity))
                : Math.round(100 + 200 * Math.min(1, distance / Math.max(1, width)));
        return Math.max(110, Math.min(320, millis));
    }
}
