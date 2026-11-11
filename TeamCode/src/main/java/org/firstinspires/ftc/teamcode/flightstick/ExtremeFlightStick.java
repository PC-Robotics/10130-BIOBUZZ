package org.firstinspires.ftc.teamcode.flightstick;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Logitech Extreme 3D Pro for FTC, loosely modeled on WPILib's Joystick class.
 *
 * Conventions (pilot-style, NOT FTC-gamepad-style):
 *   pitch    +1 = stick pushed forward (nose down)
 *   roll     +1 = stick right
 *   twist    +1 = twisted clockwise (right rudder)
 *   throttle  0 = slider back, 1 = slider forward
 *
 * Usage:
 *   ExtremeFlightStick stick = new ExtremeFlightStick(gamepad1);
 *   while (opModeIsActive()) {
 *       stick.update();                       // once per loop, first thing
 *       if (stick.wasPressed(ExtremeFlightStick.Button.TRIGGER)) { ... }
 *   }
 */
public class ExtremeFlightStick {

    public enum Button {
        TRIGGER(1), THUMB(2), B3(3), B4(4), B5(5), B6(6),
        B7(7), B8(8), B9(9), B10(10), B11(11), B12(12);
        public final int number;
        Button(int n) { number = n; }
    }

    /** 8-way hat switch. degrees matches WPILib POV: 0 = up, clockwise, -1 = centered. */
    public enum Hat {
        CENTER(-1), UP(0), UP_RIGHT(45), RIGHT(90), DOWN_RIGHT(135),
        DOWN(180), DOWN_LEFT(225), LEFT(270), UP_LEFT(315);
        public final int degrees;
        Hat(int d) { degrees = d; }
    }

    private final Gamepad gamepad;
    private final FlightStickMapping map;

    private double deadband = 0.05;
    private double twistDeadband = 0.10;   // twist axes are usually noisier
    private double expo = 0.0;             // 0 = linear, 1 = full cubic

    private final boolean[] now = new boolean[13];
    private final boolean[] last = new boolean[13];
    private Hat hat = Hat.CENTER, lastHat = Hat.CENTER;
    private double roll, pitch, twist, throttle;

    public ExtremeFlightStick(Gamepad gamepad) {
        this(gamepad, FlightStickMapping.defaultGuess());
    }

    public ExtremeFlightStick(Gamepad gamepad, FlightStickMapping mapping) {
        this.gamepad = gamepad;
        this.map = mapping;
    }

    // ---- tuning ------------------------------------------------------------
    public ExtremeFlightStick setDeadband(double d)      { deadband = clamp(d, 0, 0.9); return this; }
    public ExtremeFlightStick setTwistDeadband(double d) { twistDeadband = clamp(d, 0, 0.9); return this; }
    /** Like RC "expo": 0 = linear, 1 = cubic. Softens small movements for fine control. */
    public ExtremeFlightStick setExpo(double e)          { expo = clamp(e, 0, 1); return this; }

    // ---- per-loop update ---------------------------------------------------
    /** Call exactly once at the top of every loop iteration. */
    public void update() {
        System.arraycopy(now, 0, last, 0, now.length);
        for (int i = 1; i <= 12; i++) now[i] = map.buttons[i].read(gamepad);

        lastHat = hat;
        hat = readHat();

        roll  = shape(sign(map.invertRoll)  * map.roll.read(gamepad),  deadband);
        pitch = shape(sign(map.invertPitch) * map.pitch.read(gamepad), deadband);
        twist = shape(sign(map.invertTwist) * map.twist.read(gamepad), twistDeadband);

        double t = sign(map.invertThrottle) * map.throttle.read(gamepad);
        throttle = clamp(map.throttleIsZeroToOne ? t : (t + 1.0) / 2.0, 0, 1);
    }

    // ---- axes --------------------------------------------------------------
    public double getRoll()     { return roll; }
    public double getPitch()    { return pitch; }
    public double getTwist()    { return twist; }
    public double getThrottle() { return throttle; }

    /** Raw values with only inversion applied — handy for calibration/telemetry. */
    public double getRawRoll()  { return sign(map.invertRoll)  * map.roll.read(gamepad); }
    public double getRawPitch() { return sign(map.invertPitch) * map.pitch.read(gamepad); }
    public double getRawTwist() { return sign(map.invertTwist) * map.twist.read(gamepad); }

    /** Stick deflection 0..1 (like WPILib getMagnitude). */
    public double getMagnitude() { return Math.min(1.0, Math.hypot(roll, pitch)); }

    /** Stick direction in degrees, 0 = forward, clockwise positive (like WPILib getDirectionDegrees). */
    public double getDirectionDegrees() { return Math.toDegrees(Math.atan2(roll, pitch)); }

    // ---- buttons -----------------------------------------------------------
    public boolean isPressed(Button b)   { return now[b.number]; }
    public boolean wasPressed(Button b)  { return now[b.number] && !last[b.number]; }
    public boolean wasReleased(Button b) { return !now[b.number] && last[b.number]; }

    public boolean isPressed(int n)   { return now[checkButton(n)]; }
    public boolean wasPressed(int n)  { return now[checkButton(n)] && !last[n]; }
    public boolean wasReleased(int n) { return !now[checkButton(n)] && last[n]; }

    // ---- hat ---------------------------------------------------------------
    public Hat getHat()          { return hat; }
    public int getPOV()          { return hat.degrees; }
    public boolean hatChanged()  { return hat != lastHat; }

    /** Hat turns rumble-free trim etc. into edge events: true once when it moves INTO direction h. */
    public boolean hatWasPressed(Hat h) { return hat == h && lastHat != h; }

    // ---- haptics passthrough (the Extreme 3D Pro has no motors; harmless no-op) ----
    public Gamepad getGamepad() { return gamepad; }

    // ---- internals ---------------------------------------------------------
    private Hat readHat() {
        boolean u = map.hatUp.read(gamepad), d = map.hatDown.read(gamepad);
        boolean l = map.hatLeft.read(gamepad), r = map.hatRight.read(gamepad);
        if (u && r) return Hat.UP_RIGHT;
        if (u && l) return Hat.UP_LEFT;
        if (d && r) return Hat.DOWN_RIGHT;
        if (d && l) return Hat.DOWN_LEFT;
        if (u) return Hat.UP;
        if (d) return Hat.DOWN;
        if (l) return Hat.LEFT;
        if (r) return Hat.RIGHT;
        return Hat.CENTER;
    }

    /** Deadband with rescale (no jump at the edge), then expo curve. */
    private double shape(double v, double db) {
        v = clamp(v, -1, 1);
        double a = Math.abs(v);
        if (a < db) return 0;
        double scaled = (a - db) / (1 - db);
        double curved = (1 - expo) * scaled + expo * scaled * scaled * scaled;
        return Math.copySign(curved, v);
    }

    private static int checkButton(int n) {
        if (n < 1 || n > 12) throw new IllegalArgumentException("Button must be 1..12");
        return n;
    }

    private static double sign(boolean invert) { return invert ? -1.0 : 1.0; }

    private static double clamp(double v, double lo, double hi) { return Math.max(lo, Math.min(hi, v)); }
}
