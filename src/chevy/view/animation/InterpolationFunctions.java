package chevy.view.animation;

public class InterpolationFunctions {
    private static double lerp(double a, double b, double t) {
        // 0 <= t <= 1
        return (1 - t) * a + t * b;
    }

    private static double easeInSine(double a, double b, double t) {
        double x = 1 - Math.cos((t * Math.PI) / 2);
        return lerp(a, b, x);
    }

    private static double easeOutSine(double a, double b, double t) {
        double x = Math.sin((t * Math.PI) / 2);
        return lerp(a, b, x);
    }

    private static double easeInOutSine(double a, double b, double t) {
        double x = -1 * (Math.cos(Math.PI * t) - 1) / 2;
        return lerp(a, b, x);
    }

    private static double easeInQuad(double a, double b, double t) {
        double x = t * t;
        return lerp(a, b, x);
    }

    private static double easeOutQuad(double a, double b, double t) {
        double x = 1 - (1 - t) * (1 - t);
        return lerp(a, b, x);
    }

    private static double easeInOutQuad(double a, double b, double t) {
        double x = t < 0.5 ? 2 * t * t : (1 - Math.pow(-2 * t + 2, 2) / 2);
        return lerp(a, b, x);
    }

    private static double easeInCubic(double a, double b, double t) {
        double x = Math.pow(t, 3);
        return lerp(a, b, x);
    }

    private static double easeOutCubic(double a, double b, double t) {
        double x = 1 - Math.pow(1 - t, 3);
        return lerp(a, b, x);
    }

    private static double easeInOutCubic(double a, double b, double t) {
        double x = t < 0.5 ? 4 * t * t * t : (1 - Math.pow(-2 * t + 2, 3) / 2);
        return lerp(a, b, t);
    }

    private static double easeInQuart(double a, double b, double t) {
        double x = Math.pow(t, 4);
        return lerp(a, b, x);
    }

    private static double easeOutQuart(double a, double b, double t) {
        double x = 1 - Math.pow(1 - t, 4);
        return lerp(a, b, x);
    }

    private static double easeInOutQuart(double a, double b, double t) {
        double x = t < 0.5 ? 8 * t * t * t * t : (1 - Math.pow(-2 * t + 2, 4) / 2);
        return lerp(a, b, x);
    }

    private static double easeInQuint(double a, double b, double t) {
        double x = Math.pow(t, 5);
        return lerp(a, b, x);
    }

    private static double easeOutQuint(double a, double b, double t) {
        double x = 1 - Math.pow(1 - t, 5);
        return lerp(a, b, t);
    }

    private static double easeInOutQuint(double a, double b, double t) {
        double x = t < 0.5 ? 16 * t * t * t * t * t : (1 - Math.pow(-2 * t + 2, 5) / 2);
        return lerp(a, b, x);
    }

    private static double easeInExpo(double a, double b, double t) {
        double x = t == 0 ? 0 : Math.pow(2, 10 * t - 10);
        return lerp(a, b, t);
    }

    private static double easeOutExpo(double a, double b, double t) {
        double x = t == 1 ? 1 : (1 - Math.pow(2, -10 * t));
        return lerp(a, b, x);
    }

    private static double easeInOutExpo(double a, double b, double t) {
        double x = t == 0
                ? 0
                : t == 1
                ? 1
                : t < 0.5 ? (Math.pow(2, 20 * t - 10) / 2)
                : ((2 - Math.pow(2, -20 * t + 10)) / 2);
        return lerp(a, b, x);
    }

    private static double easeInCirc(double a, double b, double t) {
        double x = 1 - Math.sqrt(1 - Math.pow(t, 2));
        return lerp(a, b, x);
    }

    private static double easeOutCirc(double a, double b, double t) {
        double x = Math.sqrt(1 - Math.pow(t - 1, 2));
        return lerp(a, b, x);
    }

    private static double easeInOutCirc(double a, double b, double t) {
        double x = t < 0.5
                ? (1 - Math.sqrt(1 - Math.pow(2 * t, 2))) / 2
                : (Math.sqrt(1 - Math.pow(-2 * t + 2, 2)) + 1) / 2;
        return lerp(a, b, x);
    }

    private static double easeInBack(double a, double b, double t) {
        final double c1 = 1.70158;
        final double c3 = c1 + 1;
        double x = c3 * Math.pow(t, 3) - c1 * Math.pow(t, 2);
        return lerp(a, b, x);
    }

    private static double easeOutBack(double a, double b, double t) {
        final double c1 = 1.70158;
        final double c3 = c1 + 1;
        double x = 1 + c3 * Math.pow(t - 1, 3) + c1 * Math.pow(t - 1, 2);
        return lerp(a, b, x);
    }

    private static double easeInOutBack(double a, double b, double t) {
        final double c1 = 1.70158;
        final double c2 = c1 * 1.525;
        double x = t < 0.5
                ? (Math.pow(2 * t, 2) * ((c2 + 1) * 2 * t - c2)) / 2
                : (Math.pow(2 * t - 2, 2) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2;
        return lerp(a, b, x);
    }

    private static double easeInElastic(double a, double b, double t) {
        final double c4 = (2 * Math.PI) / 3;
        double x = t == 0
                ? 0
                : t == 1
                ? 1
                : -1 * Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * c4);
        return lerp(a, b, x);
    }

    private static double easeOutElastic(double a, double b, double t) {
        final double c4 = (2 * Math.PI) / 3;
        double x = t == 0
                ? 0
                : t == 1
                ? 1
                : Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * c4) + 1;
        return lerp(a, b, x);
    }

    private static double easeInOutElastic(double a, double b, double t) {
        final double c5 = (2 * Math.PI) / 4.5;
        final double c6 = Math.sin((20 * t - 11.125) * c5);
        double x = t == 0
                ? 0
                : t == 1
                ? 1
                : t < 0.5
                ? -1 * (Math.pow(2, 20 * t - 10) * c6) / 2
                : (Math.pow(2, -20 * t + 10) * c6) / 2 + 1;
        return lerp(a, b, x);
    }

    private static double easeInBounce(double a, double b, double t) {
        double x = 1 - easeOutBounce(1 - t);
        return lerp(a, b, x);
    }

    private static double easeOutBounce(double a, double b, double t) {
        return lerp(a, b, easeOutBounce(t));
    }

    private static double easeOutBounce(double t) {
        final double n1 = 7.5625f;
        final double d1 = 2.75f;

        if (t < 1 / d1) {
            return n1 * t * t;
        }
        else if (t < 2 / d1) {
            return (n1 * (t -= 1.5f / d1) * t + 0.75f);
        }
        else if (t < 2.5f / d1) {
            return(n1 * (t -= 2.25f / d1) * t + 0.9375f);
        }
        else {
            return (n1 * (t -= 2.625f / d1) * t + 0.984375f);
        }
    }

    private static double easeInOutBounce(double a, double b, double t) {
        double x = t < 0.5
                ? (1 - easeOutBounce(t)) / 2
                : (1 + easeOutBounce(t - 1)) / 2;
        return lerp(a, b, x);
    }


    public static double interpolate(double a, double b, double t, InterpolationTypes type) {
        return switch (type) {
            case EASE_IN_SINE -> easeInSine(a, b, t);
            case EASE_OUT_SINE -> easeOutSine(a, b, t);
            case EASE_IN_OUT_SINE -> easeInOutSine(a, b, t);
            case EASE_IN_QUAD -> easeInQuad(a, b, t);
            case EASE_OUT_QUAD -> easeOutQuad(a, b, t);
            case EASE_IN_OUT_QUAD -> easeInOutQuad(a, b, t);
            case EASE_IN_CUBIC -> easeInCubic(a, b, t);
            case EASE_OUT_CUBIC -> easeOutCubic(a, b, t);
            case EASE_IN_OUT_CUBIC -> easeInOutCubic(a, b, t);
            case EASE_IN_QUART -> easeInQuart(a, b, t);
            case EASE_OUT_QUART -> easeOutQuart(a, b, t);
            case EASE_IN_OUT_QUART -> easeInOutQuart(a, b, t);
            case EASE_IN_QUINT -> easeInQuint(a, b, t);
            case EASE_OUT_QUINT -> easeOutQuint(a, b, t);
            case EASE_IN_OUT_QUINT -> easeInOutQuint(a, b, t);
            case EASE_IN_EXPO -> easeInExpo(a, b, t);
            case EASE_OUT_EXPO -> easeOutExpo(a, b, t);
            case EASE_IN_OUT_EXPO -> easeInOutExpo(a, b, t);
            case EASE_IN_CIRC -> easeInCirc(a, b, t);
            case EASE_OUT_CIRC -> easeOutCirc(a, b, t);
            case EASE_IN_OUT_CIRC -> easeInOutCirc(a, b, t);
            case EASE_IN_BACK -> easeInBack(a, b, t);
            case EASE_OUT_BACK -> easeOutBack(a, b, t);
            case EASE_IN_OUT_BACK -> easeInOutBack(a, b, t);
            case EASE_IN_ELASTIC -> easeInElastic(a, b, t);
            case EASE_OUT_ELASTIC -> easeOutElastic(a, b, t);
            case EASE_IN_OUT_ELASTIC -> easeInOutElastic(a, b, t);
            case EASE_IN_BOUNCE -> easeInBounce(a, b, t);
            case EASE_OUT_BOUNCE -> easeOutBounce(a, b, t);
            case EASE_IN_OUT_BOUNCE -> easeInOutBounce(a, b, t);
            case LINEAR -> lerp(a, b, t);
        };
    }

    public static double interpolate(double a, double b, double t) {
        return lerp(a, b, t);
    }
}
