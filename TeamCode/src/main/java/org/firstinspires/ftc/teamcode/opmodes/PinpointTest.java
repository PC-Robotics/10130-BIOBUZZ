package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.PinpointSubsystem;

@TeleOp(name = "Pinpoint Test", group = "Test")
public class PinpointTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        PinpointSubsystem pinpoint = new PinpointSubsystem(hardwareMap);
        telemetry.addLine("Keep robot still during IMU calibration.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            pinpoint.update();
            telemetry.addData("Status", pinpoint.getStatus());
            telemetry.addData("X mm", "%.1f", pinpoint.getXmm());
            telemetry.addData("Y mm", "%.1f", pinpoint.getYmm());
            telemetry.addData("Heading deg", "%.1f", pinpoint.getHeadingDegrees());
            telemetry.addData("X velocity mm/s", "%.1f", pinpoint.getVelocityXmmPerSec());
            telemetry.addData("Y velocity mm/s", "%.1f", pinpoint.getVelocityYmmPerSec());
            telemetry.update();
        }
    }
}
