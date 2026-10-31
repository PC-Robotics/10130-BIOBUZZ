package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

@TeleOp(name = "Main TeleOp - Starter", group = "Competition")
public class MainTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        boolean fieldCentric = false;
        boolean previousRightStick = false;
        boolean previousX = false;
        boolean previousY = false;
        boolean previousDpadLeft = false;
        boolean previousDpadRight = false;
        boolean previousDpadUp = false;
        boolean previousDpadDown = false;
        boolean previousStart = false;

        telemetry.addLine("Initialized. Check hardware before enabling.");
        telemetry.addLine("Center Buttom: home turret | Square: center turret | Cross: flywheel toggle");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            robot.update();

            boolean rightStick = gamepad1.right_stick_button;
            if (rightStick && !previousRightStick) fieldCentric = !fieldCentric;
            previousRightStick = rightStick;

            boolean start = gamepad1.start;
            if (start && !previousStart) robot.pinpoint.reset();
            previousStart = start;

            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;
            double scale = gamepad1.left_trigger > 0.1 ? 0.35 : 1.0;

            if (fieldCentric) {
                robot.drive.driveFieldCentric(forward, strafe, turn,
                        robot.pinpoint.getHeadingRadians(), scale);
            } else {
                robot.drive.driveRobotCentric(forward, strafe, turn, scale);
            }

            robot.intake.setPower(gamepad1.right_trigger > 0.1 ? gamepad1.right_trigger : (gamepad1.a ? -0.5 : 0.0));

            if (gamepad1.b) robot.transfer.setPower(0.65);
            else if (gamepad1.left_bumper && robot.flywheel.isAtSpeed()) robot.transfer.setPower(0.65);
            else if (gamepad1.left_bumper) robot.transfer.setPower(0.0);
            else robot.transfer.setPower(0.0);

            boolean x = gamepad1.cross;
            if (x && !previousX) robot.flywheel.toggle();
            previousX = x;

            boolean y = gamepad1.square;
            if (y && !previousY) robot.turret.center();
            previousY = y;

            boolean dpadLeft = gamepad1.dpad_left;
            if (dpadLeft && !previousDpadLeft) robot.turret.moveByDegrees(-5.0);
            previousDpadLeft = dpadLeft;

            boolean dpadRight = gamepad1.dpad_right;
            if (dpadRight && !previousDpadRight) robot.turret.moveByDegrees(5.0);
            previousDpadRight = dpadRight;

            boolean dpadUp = gamepad1.dpad_up;
            if (dpadUp && !previousDpadUp) robot.flywheel.adjustTarget(100.0);
            previousDpadUp = dpadUp;

            boolean dpadDown = gamepad1.dpad_down;
            if (dpadDown && !previousDpadDown) robot.flywheel.adjustTarget(-100.0);
            previousDpadDown = dpadDown;

            if (gamepad1.guideWasPressed() && robot.turret.getState() == TurretSubsystem.State.UNHOMED) {
                robot.turret.beginHoming();
            }

            telemetry.addData("Drive mode", fieldCentric ? "Field-centric" : "Robot-centric");
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
