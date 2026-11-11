package org.firstinspires.ftc.teamcode.flightstick;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.*;
.javaflightstick.ExtremeFlightStick;
import org.firstinspires.ftc.teamcode.flightstick.ExtremeFlightStick.Button;
import org.firstinspires.ftc.teamcode.flightstick.ExtremeFlightStick.Hat;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

/**
 * Same robot functions as MainTeleOp, driven with a Logitech Extreme 3D Pro on gamepad1.
 * MainTeleOp stays as the backup for normal gamepads.
 *
 * Before using this, run "Flight Stick Discovery" (or the Driver Station's TestGamepad
 * utility) and fix FlightStickMapping.defaultGuess() to match what you see.
 *
 * Stick:      pitch = forward/back, roll = strafe, twist = turn
 * Throttle:   drive speed (slider back = SLOW_MODE, forward = full)
 * Hat:        left/right = turret -/+ 5 deg, up/down = flywheel +/- 100 RPM
 *
 * Buttons on the stick head:
 *   1 Trigger  feed transfer (only when flywheel is at speed)
 *   2 Thumb    intake in (hold)
 *   3          intake reverse (hold)
 *   4          force transfer (hold)
 *   5          flywheel on/off
 *   6          center turret
 * Buttons on the base:
 *   7          toggle field-centric / robot-centric
 *   8          reset Pinpoint position + heading
 *   9          home turret (only when UNHOMED)
 *   10-12      unused
 */
@TeleOp(name = "Flight Stick TeleOp", group = "Competition")
public class FlightStickTeleOp extends LinearOpMode {

    private static final double TRANSFER_POWER = 0.65;
    private static final double INTAKE_POWER = 1.0;
    private static final double INTAKE_REVERSE_POWER = -0.5;

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        ExtremeFlightStick stick = new ExtremeFlightStick(gamepad1)
                .setDeadband(0.05)
                .setTwistDeadband(0.12)
                .setExpo(0.4);
        boolean fieldCentric = false;

        telemetry.addLine("Flight stick on gamepad1. Run Flight Stick Discovery first if buttons seem wrong.");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            stick.update();
            robot.update();

            // ---- drive ----
            if (stick.wasPressed(Button.B7)) fieldCentric = !fieldCentric;
            if (stick.wasPressed(Button.B8)) robot.pinpoint.reset();

            double forward = stick.getPitch();
            double strafe = stick.getRoll();
            double turn = stick.getTwist();
            double minScale = Constants.Drive.SLOW_MODE;
            double scale = minScale + (1.0 - minScale) * stick.getThrottle();

            if (fieldCentric) {
                robot.drive.driveFieldCentric(forward, strafe, turn,
                        robot.pinpoint.getHeadingRadians(), scale);
            } else {
                robot.drive.driveRobotCentric(forward, strafe, turn, scale);
            }//

            // ---- intake ----
            if (stick.isPressed(Button.THUMB)) robot.intake.setPower(INTAKE_POWER);
            else if (stick.isPressed(Button.B3)) robot.intake.setPower(INTAKE_REVERSE_POWER);
            else robot.intake.setPower(0.0);

            // ---- transfer ----
            if (stick.isPressed(Button.B4)) robot.transfer.setPower(TRANSFER_POWER);
            else if (stick.isPressed(Button.TRIGGER) && robot.flywheel.isAtSpeed()) robot.transfer.setPower(TRANSFER_POWER);
            else robot.transfer.setPower(0.0);

            // ---- flywheel ----
            if (stick.wasPressed(Button.B5)) robot.flywheel.toggle();
            if (stick.hatWasPressed(Hat.UP)) robot.flywheel.adjustTarget(Constants.Flywheel.RPM_STEP);
            if (stick.hatWasPressed(Hat.DOWN)) robot.flywheel.adjustTarget(-Constants.Flywheel.RPM_STEP);

            // ---- turret ----
            if (stick.wasPressed(Button.B6)) robot.turret.center();
            if (stick.hatWasPressed(Hat.LEFT)) robot.turret.moveByDegrees(-Constants.Turret.MANUAL_STEP_DEGREES);
            if (stick.hatWasPressed(Hat.RIGHT)) robot.turret.moveByDegrees(Constants.Turret.MANUAL_STEP_DEGREES);
            if (stick.wasPressed(Button.B9) && robot.turret.getState() == TurretSubsystem.State.UNHOMED) {
                robot.turret.beginHoming();
            }

            // ---- telemetry ----
            telemetry.addData("Drive mode", fieldCentric ? "Field-centric" : "Robot-centric");
            telemetry.addData("Stick pitch/roll/twist", "%.2f / %.2f / %.2f", forward, strafe, turn);
            telemetry.addData("Throttle", "%.0f%% (drive scale %.0f%%)", stick.getThrottle() * 100, scale * 100);
            telemetry.addData("Pinpoint status", robot.pinpoint.getStatus());
            telemetry.addData("X / Y (mm)", "%.1f / %.1f", robot.pinpoint.getXmm(), robot.pinpoint.getYmm());
            telemetry.addData("Heading (deg)", "%.1f", robot.pinpoint.getHeadingDegrees());
            telemetry.addData("Turret", robot.turret.getState());
            telemetry.addData("Turret ticks", robot.turret.getPositionTicks());
            telemetry.addData("Flywheel RPM", "%.0f / %.0f",
                    robot.flywheel.getCurrentRpm(), robot.flywheel.getTargetRpm());
            telemetry.addData("Flywheel ready", robot.flywheel.isAtSpeed());
            telemetry.update();
        }
        robot.stop();
    }
}
