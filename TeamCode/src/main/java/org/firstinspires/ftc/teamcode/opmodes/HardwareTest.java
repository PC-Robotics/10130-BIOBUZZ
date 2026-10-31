package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.Constants;

/**
 * Basic motor direction test. Use only after securing the robot and clearing moving parts.
 * Bumpers select a motor; left stick applies low test power.
 */
@TeleOp(name = "Hardware Test - Low Power", group = "Test")
public class HardwareTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        DcMotorEx[] motors = {
                hardwareMap.get(DcMotorEx.class, Constants.Names.FRONT_LEFT),
                hardwareMap.get(DcMotorEx.class, Constants.Names.FRONT_RIGHT),
                hardwareMap.get(DcMotorEx.class, Constants.Names.BACK_LEFT),
                hardwareMap.get(DcMotorEx.class, Constants.Names.BACK_RIGHT),
                hardwareMap.get(DcMotorEx.class, Constants.Names.INTAKE),
                hardwareMap.get(DcMotorEx.class, Constants.Names.TRANSFER),
                hardwareMap.get(DcMotorEx.class, Constants.Names.TURRET),
                hardwareMap.get(DcMotorEx.class, Constants.Names.FLYWHEEL)
        };
        String[] names = {"frontLeft", "frontRight", "backLeft", "backRight",
                "intake", "transfer", "turret", "flywheel"};
        int selected = 0;
        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setPower(0);
        }

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.dpad_up) selected = (selected + 1) % motors.length;
            if (gamepad1.dpad_down) selected = (selected + motors.length - 1) % motors.length;
            double power = -gamepad1.left_stick_y * 0.15;
            for (int i = 0; i < motors.length; i++) {
                motors[i].setPower(i == selected ? power : 0);
            }
            telemetry.addData("Selected motor", names[selected]);
            telemetry.addData("Power", "%.2f", power);
            telemetry.addLine("D-pad up/down selects. Left stick controls at max 0.15.");
            telemetry.update();
            sleep(20);
        }
        for (DcMotorEx motor : motors) motor.setPower(0);
    }
}
