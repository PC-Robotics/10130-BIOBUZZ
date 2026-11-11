package org.firstinspires.ftc.teamcode.flightstick;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Tells {@link ExtremeFlightStick} which {@link Gamepad} field each physical control
 * on the Logitech Extreme 3D Pro shows up in.
 *
 * WHY THIS EXISTS: In FRC, the Driver Station sends raw HID axes/buttons, so FRC
 * libraries can say "axis 3 = throttle". In FTC, the Driver Station app squeezes every
 * controller into the standard {@link Gamepad} object (left_stick_x, a, dpad_up, ...),
 * and how a non-standard device lands in those fields depends on the DS app version.
 * So: run the FlightStickDiscovery OpMode, move each control, and fill this in.
 */
public class FlightStickMapping {

    /** Reads one analog value (-1..1 or 0..1) out of a Gamepad. */
    public interface AxisSource { float read(Gamepad g); }

    /** Reads one button out of a Gamepad. */
    public interface ButtonSource { boolean read(Gamepad g); }

    private static final ButtonSource NONE = g -> false;

    AxisSource roll  = g -> 0f;   // stick left/right
    AxisSource pitch = g -> 0f;   // stick forward/back
    AxisSource twist = g -> 0f;   // stick rotation (rudder)
    AxisSource throttle = g -> 0f; // slider on the base

    boolean invertRoll, invertPitch, invertTwist, invertThrottle;

    /** true if the throttle field already reads 0..1 (e.g. a trigger); false if it reads -1..1. */
    boolean throttleIsZeroToOne = false;

    /** Index 1..12 = buttons as numbered on the stick. Index 0 unused. */
    final ButtonSource[] buttons = new ButtonSource[13];

    ButtonSource hatUp = NONE, hatDown = NONE, hatLeft = NONE, hatRight = NONE;

    public FlightStickMapping() {
        for (int i = 0; i < buttons.length; i++) buttons[i] = NONE;
    }

    // ---- fluent setters ----------------------------------------------------
    public FlightStickMapping roll(AxisSource s)     { roll = s; return this; }
    public FlightStickMapping pitch(AxisSource s)    { pitch = s; return this; }
    public FlightStickMapping twist(AxisSource s)    { twist = s; return this; }
    public FlightStickMapping throttle(AxisSource s, boolean zeroToOne) {
        throttle = s; throttleIsZeroToOne = zeroToOne; return this;
    }

    public FlightStickMapping invertRoll(boolean b)     { invertRoll = b; return this; }
    public FlightStickMapping invertPitch(boolean b)    { invertPitch = b; return this; }
    public FlightStickMapping invertTwist(boolean b)    { invertTwist = b; return this; }
    public FlightStickMapping invertThrottle(boolean b) { invertThrottle = b; return this; }

    /** @param number button number printed on the stick, 1..12 */
    public FlightStickMapping button(int number, ButtonSource s) {
        if (number < 1 || number > 12) throw new IllegalArgumentException("Button must be 1..12");
        buttons[number] = s; return this;
    }

    public FlightStickMapping hat(ButtonSource up, ButtonSource down, ButtonSource left, ButtonSource right) {
        hatUp = up; hatDown = down; hatLeft = left; hatRight = right; return this;
    }

    /**
     * A STARTING GUESS for how the Extreme 3D Pro shows up. It is very likely wrong in
     * places for your DS version. Confirm every line with FlightStickDiscovery and edit.
     */
    public static FlightStickMapping defaultGuess() {
        return new FlightStickMapping()
                .roll(g -> g.left_stick_x)
                .pitch(g -> g.left_stick_y)
                .invertPitch(true)                 // FTC: stick forward = negative y
                .twist(g -> g.right_stick_x)
                .throttle(g -> g.right_stick_y, false)
                .invertThrottle(true)              // so "throttle forward" = more power
                .hat(g -> g.dpad_up, g -> g.dpad_down, g -> g.dpad_left, g -> g.dpad_right)
                .button(1,  g -> g.a)                   // trigger
                .button(2,  g -> g.b)                   // thumb button
                .button(3,  g -> g.x)
                .button(4,  g -> g.y)
                .button(5,  g -> g.left_bumper)
                .button(6,  g -> g.right_bumper)
                .button(7,  g -> g.left_trigger > 0.5)
                .button(8,  g -> g.right_trigger > 0.5)
                .button(9,  g -> g.back)
                .button(10, g -> g.start)
                .button(11, g -> g.left_stick_button)
                .button(12, g -> g.right_stick_button);
    }
}
