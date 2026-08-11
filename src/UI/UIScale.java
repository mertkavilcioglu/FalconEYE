package UI;

public final class UIScale {

    public static final int DESIGN_WIDTH = 2560;
    public static final int DESIGN_HEIGHT = 1440;

    private static double scale = 1.0;

    private UIScale() {
    }

    public static void update(int width, int height) {

        double scaleX = (double) width / DESIGN_WIDTH;
        double scaleY = (double) height / DESIGN_HEIGHT;
        scale = Math.min(scaleX, scaleY);
    }

    public static int scale(int value) {
        return (int) Math.round(value * scale);
    }

    public static double scale(double value) {
        return value * scale;
    }

    public static double getScale() {
        return scale;
    }
}