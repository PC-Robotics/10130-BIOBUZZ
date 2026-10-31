package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Constants;

public class DriveSubsystem {
    private final DcMotorEx frontLeft, frontRight, backLeft, backRight;

    public DriveSubsystem(HardwareMap map) {
        frontLeft = map.get(DcMotorEx.class, Constants.Names.FRONT_LEFT);
        frontRight = map.get(DcMotorEx.class, Constants.Names.FRONT_RIGHT);
        backLeft = map.get(DcMotorEx.class, Constants.Names.BACK_LEFT);
        backRight = map.get(DcMotorEx.class, Constants.Names.BACK_RIGHT);

        frontLeft.setDirection(Constants.Drive.REVERSE_LEFT ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        backLeft.setDirection(Constants.Drive.REVERSE_LEFT ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        frontRight.setDirection(Constants.Drive.REVERSE_RIGHT ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        backRight.setDirection(Constants.Drive.REVERSE_RIGHT ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void driveRobotCentric(double forward, double strafe, double turn, double scale) {
        double fl = forward + strafe + turn;
        double fr = forward - strafe - turn;
        double bl = forward - strafe + turn;
        double br = forward + strafe - turn;
        double max = Math.max(1.0, Math.max(Math.abs(fl),
                Math.max(Math.abs(fr), Math.max(Math.abs(bl), Math.abs(br)))));
        setPowers(fl / max * scale, fr / max * scale, bl / max * scale, br / max * scale);
    }

    public void driveFieldCentric(double forward, double strafe, double turn,
                                  double headingRadians, double scale) {
        double cos = Math.cos(-headingRadians);
        double sin = Math.sin(-headingRadians);
        double robotForward = forward * cos - strafe * sin;
        double robotStrafe = forward * sin + strafe * cos;
        driveRobotCentric(robotForward, robotStrafe, turn, scale);
    }

    private void setPowers(double fl, double fr, double bl, double br) {
        frontLeft.setPower(fl);
        frontRight.setPower(fr);
        backLeft.setPower(bl);
        backRight.setPower(br);
    }

    private void setMode(DcMotor.RunMode mode) {
        frontLeft.setMode(mode); frontRight.setMode(mode);
        backLeft.setMode(mode); backRight.setMode(mode);
    }

    private void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        frontLeft.setZeroPowerBehavior(behavior); frontRight.setZeroPowerBehavior(behavior);
        backLeft.setZeroPowerBehavior(behavior); backRight.setZeroPowerBehavior(behavior);
    }

    public void stop() { setPowers(0, 0, 0, 0); }
}
