package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.*;

/**
 * PLAYER 1 (driver)
 *  Left stick = drive/strafe, Right stick X = turn, L2 = slow mode
 *  R2 = intake, Cross = reverse intake
 *  Circle = transfer (manual), L1 = transfer only when flywheel is at speed
 *  Square = flywheel on/off, D-pad up/down = flywheel RPM +/-100
 *
 * PLAYER 2 (operator)
 *  Right stick X = turret, L2 = slow turret
 *  Triangle = re-zero turret (only when centered)
 *  Cross = backstop small ball, Circle = backstop large ball
 *  D-pad left/right = nudge backstop
 */
@TeleOp(name = "Main TeleOp", group = "Competition")
public class MainTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);

        telemetry.addLine("Initialized. Turret must be CENTERED before start.");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if(!robot.flywheel.isAtSpeed()){
                gamepad1.setLedColor(0,0,255, Gamepad.LED_DURATION_CONTINUOUS);

                gamepad2.setLedColor(255,0,0, Gamepad.LED_DURATION_CONTINUOUS);
            }
            // ---------- Player 1 ----------
            double scale = gamepad1.left_trigger > 0.1 ? 0.35 : 1.0;
            robot.drive.driveRobotCentric(-gamepad1.left_stick_y, gamepad1.left_stick_x,
                    gamepad1.right_stick_x, scale);

            robot.intake.setPower(gamepad1.right_trigger > 0.1 ? gamepad1.right_trigger
                    : (gamepad1.cross ? -.4 : 0.0));

            boolean feed = gamepad1.circle
                    || (gamepad1.right_bumper);
            robot.transfer.setPower(feed ? 0.65 : 0.0);

            if (gamepad1.squareWasPressed()) robot.flywheel.toggle();
            if (gamepad1.dpadUpWasPressed()) robot.flywheel.adjustTarget(100);
            if (gamepad1.dpadDownWasPressed()) robot.flywheel.adjustTarget(-100);
            if(robot.flywheel.isAtSpeed()){
                gamepad1.rumble(1000);
                gamepad1.setLedColor(0,255,0, Gamepad.LED_DURATION_CONTINUOUS);

                gamepad2.setLedColor(0,255,0, Gamepad.LED_DURATION_CONTINUOUS);
                gamepad2.rumble(1000);
            }

            // ---------- Player 2 ----------
            double turretScale = gamepad2.left_trigger > 0.1 ? 0.4 : 1.0;
            //robot.turret.setManualPower(gamepad2.right_stick_x * -turretScale);

            if (gamepad2.triangleWasPressed()) robot.turret.zero();
            if (gamepad2.crossWasPressed()) robot.backstop.setSmall();
            if (gamepad2.circleWasPressed()) robot.backstop.setLarge();
            if (gamepad2.dpadLeftWasPressed()) robot.backstop.nudge(-0.01);
            if (gamepad2.dpadRightWasPressed()) robot.backstop.nudge(0.01);
            if(gamepad2.dpadUpWasPressed()) robot.flywheel.adjustTarget(100);
            if(gamepad2.dpadDownWasPressed()) robot.flywheel.adjustTarget(-100);

            // ---------- Telemetry ----------
            telemetry.addData("Turret", "%d ticks / %.1f deg",
                    robot.turret.getPositionTicks(), robot.turret.getAngleDegrees());
            telemetry.addData("Flywheel RPM", "%.0f / %.0f",
                    robot.flywheel.getCurrentRpm(), robot.flywheel.getTargetRpm());
            telemetry.addData("Flywheel ready", robot.flywheel.isAtSpeed());
            telemetry.addData("Backstop", "%.3f", robot.backstop.getPosition());
            telemetry.update();
        }
        robot.stop();
    }
}