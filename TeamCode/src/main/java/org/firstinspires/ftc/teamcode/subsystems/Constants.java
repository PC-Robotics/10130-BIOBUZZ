package org.firstinspires.ftc.teamcode.subsystems;

public final class Constants {
    private Constants() {}

    /** Hardware map config names. MUST match Robot Configuration EXACTLY. */
    public static final class Names {
        public static final String FRONT_LEFT = "frontLeft";
        public static final String FRONT_RIGHT = "frontRight";
        public static final String BACK_LEFT = "backLeft";
        public static final String BACK_RIGHT = "backRight";
        public static final String INTAKE = "intake";
        public static final String TRANSFER = "transfer";
        public static final String TURRET = "turret";
        public static final String FLYWHEEL = "flywheel";
        public static final String BACKSTOP = "backstop";

        public static final String PINPOINT = "pinpoint";
    }

    public static final class Drive {
            // true = reverse that motor. Tune per wheel with HardwareTest.
            public static final boolean REVERSE_FRONT_LEFT = true;
            public static final boolean REVERSE_FRONT_RIGHT = false;
            public static final boolean REVERSE_BACK_LEFT = false;
            public static final boolean REVERSE_BACK_RIGHT = true;


    }

    public static final class Motors {

        public static final double FLYWHEEL_TICKS_PER_REV = 28.0;

        // 312 rpm goBILDA Yellow Jacket = 537.7 ticks/rev, then 20T -> 60T = 3:1.
        public static final double TURRET_TICKS_PER_OUTPUT_REV = 537.7 * (60.0 / 20.0); // ~1613.1
    }

    public static final class Flywheel {
        public static double START_RPM = 3000;
        public static double MIN_RPM = 2500;
        public static double MAX_RPM = 6000;
        public static double READY_TOLERANCE_RPM = 100;
    }

    public static final class Turret {
        public static final double MAX_POWER = 0.4;
        public static final double LEFT_LIMIT_DEGREES = 90;   // max rotation left of center
        public static final double RIGHT_LIMIT_DEGREES = 100;  // max rotation right of center
        // Right stick = positive ticks. If your turret turns the opposite way, swap the signs.
        public static final int MIN_TICKS = -(int) (LEFT_LIMIT_DEGREES / 360.0 * Motors.TURRET_TICKS_PER_OUTPUT_REV);
        public static final int MAX_TICKS = (int) (RIGHT_LIMIT_DEGREES / 360.0 * Motors.TURRET_TICKS_PER_OUTPUT_REV);
    }

    public static final class Backstop {
        public static final double SMALL_POS = 0.610;  // PLACEHOLDERS: tune with gamepad2 d-pad + telemetry
        public static final double LARGE_POS = 0.07;
    }

    // Only needed by PinpointSubsystem / PinpointTest. Delete those files and this class if you drop odometry.
    public static final class Pinpoint {
        public static final double X_OFFSET_MM = 0;
        public static final double Y_OFFSET_MM = 0;
        public static final boolean REVERSE_X = false;
        public static final boolean REVERSE_Y = false;
    }
}