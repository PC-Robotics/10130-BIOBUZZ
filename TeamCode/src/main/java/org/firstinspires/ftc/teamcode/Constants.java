package org.firstinspires.ftc.teamcode;

/**
 * Hardware names and starting configuration.
 * Update names to exactly match the Robot Configuration on the Driver Hub.
 */
public final class Constants {
    private Constants() {}

    public static final class Names {
        public static final String FRONT_LEFT = "frontLeft";
        public static final String FRONT_RIGHT = "frontRight";
        public static final String BACK_LEFT = "backLeft";
        public static final String BACK_RIGHT = "backRight";

        public static final String INTAKE = "intake";
        public static final String TRANSFER = "transfer";
        public static final String TURRET = "turret";
        public static final String FLYWHEEL = "flywheel";

        public static final String TURRET_LEFT_LIMIT = "turretLeftLimit";
        public static final String TURRET_RIGHT_LIMIT = "turretRightLimit";
        public static final String PINPOINT = "odo";
    }

    public static final class Motors {

        public static final double DRIVE_TICKS_PER_REV = 537.7;
        public static final double TURRET_TICKS_PER_MOTOR_REV = 537.7;

        public static final double FLYWHEEL_TICKS_PER_REV = 28.0;

        public static final double TURRET_REDUCTION = 60.0 / 20.0;
        public static final double TURRET_TICKS_PER_OUTPUT_REV =
                TURRET_TICKS_PER_MOTOR_REV * TURRET_REDUCTION;
    }

    public static final class Drive {
        public static final boolean REVERSE_LEFT = true;
        public static final boolean REVERSE_RIGHT = false;
        public static final double SLOW_MODE = 0.35;
    }

    public static final class Pinpoint {
        public static final double X_OFFSET_MM = 0.0;
        public static final double Y_OFFSET_MM = 0.0;

        public static final boolean REVERSE_X = false;
        public static final boolean REVERSE_Y = false;
    }

    public static final class Turret {
        public static final double MAX_POWER = 0.25;
        public static final double HOLD_POWER_LIMIT = 0.18;
        public static final double POSITION_TOLERANCE_TICKS = 8.0;
        public static final double KP = 0.0020;
        public static final double MIN_MOVING_POWER = 0.07;
        public static final double MAX_TRAVEL_DEGREES = 300.0;
        public static final double MANUAL_STEP_DEGREES = 5.0;
        public static final double HOME_POWER = 0.12;
        public static final double HOME_TIMEOUT_SECONDS = 5.0;
    }

    public static final class Flywheel {
        public static final double START_RPM = 1500.0;
        public static final double RPM_STEP = 100.0;
        public static final double MAX_RPM = 6000.0;
        public static final double MIN_RPM = 0.0;
        public static final double READY_TOLERANCE_RPM = 100.0;
    }
}
