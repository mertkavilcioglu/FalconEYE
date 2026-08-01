package Mathf;

public final class UnitConverter {

    private UnitConverter(){}

    public static final double METERS_PER_NAUTICAL_MILE = 1852.0;
    public static final double FEET_PER_METER = 3.28084;
    public static final double KNOTS_PER_METER_PER_SECOND = 1.94384;

    // Distance
    public static double metersToNM(double meters){
        return meters / METERS_PER_NAUTICAL_MILE;
    }

    public static double nmToMeters(double nm){
        return nm * METERS_PER_NAUTICAL_MILE;
    }

    public static double metersToFeet(double meters){
        return meters * FEET_PER_METER;
    }

    public static double feetToMeters(double feet){
        return feet / FEET_PER_METER;
    }

    // Speed
    public static double metersPerSecondToKnots(double mps){
        return mps * KNOTS_PER_METER_PER_SECOND;
    }

    public static double knotsToMetersPerSecond(double knots){
        return knots / KNOTS_PER_METER_PER_SECOND;
    }
}