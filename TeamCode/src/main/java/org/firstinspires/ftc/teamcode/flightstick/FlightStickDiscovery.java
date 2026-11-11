package org.firstinspires.ftc.teamcode.flightstick;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Run this FIRST. Move one control on the flight stick at a time and watch which
 * Gamepad field changes. Write it down, then update FlightStickMapping.defaultGuess()
 * (or build your own mapping) to match.
 *
 * Fields that are currently active are listed at the top so they're easy to spot.
 */
@TeleOp(name = "Flight Stick Discovery", group = "FlightStick")
public class FlightStickDiscovery extends LinearOpMode {

    @Override
    public void runOpMode() {
        telemetry.addLine("Plug in the stick, assign it as gamepad1, press PLAY.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            Gamepad g = gamepad1;
            StringBuilder active = new StringBuilder();

            axis(active, "left_stick_x", g.left_stick_x);
            axis(active, "left_stick_y", g.left_stick_y);
            axis(active, "right_stick_x", g.right_stick_x);
            axis(active, "right_stick_y", g.right_stick_y);
            axis(active, "left_trigger", g.left_trigger);
            axis(active, "right_trigger", g.right_trigger);

            btn(active, "a", g.a);  btn(active, "b", g.b);
            btn(active, "x", g.x);  btn(active, "y", g.y);
            btn(active, "left_bumper", g.left_bumper);
            btn(active, "right_bumper", g.right_bumper);
            btn(active, "dpad_up", g.dpad_up);     btn(active, "dpad_down", g.dpad_down);
            btn(active, "dpad_left", g.dpad_left); btn(active, "dpad_right", g.dpad_right);
            btn(active, "back", g.back);   btn(active, "start", g.start);
            btn(active, "guide", g.guide);
            btn(active, "left_stick_button", g.left_stick_button);
            btn(active, "right_stick_button", g.right_stick_button);

            telemetry.addLine("=== ACTIVE NOW ===");
            telemetry.addLine(active.length() == 0 ? "(nothing)" : active.toString());
            telemetry.addLine();
            telemetry.addLine("=== ALL AXES ===");
            telemetry.addData("left_stick  x/y", "%.2f / %.2f", g.left_stick_x, g.left_stick_y);
            telemetry.addData("right_stick x/y", "%.2f / %.2f", g.right_stick_x, g.right_stick_y);
            telemetry.addData("triggers  L/R", "%.2f / %.2f", g.left_trigger, g.right_trigger);
            telemetry.addLine();
            telemetry.addLine("Note: the throttle slider has no spring. Leave it at one end");
            telemetry.addLine("while testing other controls, or it will always look 'active'.");
            telemetry.update();
        }
    }

    private static void axis(StringBuilder sb, String name, float v) {
        if (Math.abs(v) > 0.15) sb.append(String.format("%s=%.2f  ", name, v));
    }

    private static void btn(StringBuilder sb, String name, boolean v) {
        if (v) sb.append(name).append("  ");
    }
}
