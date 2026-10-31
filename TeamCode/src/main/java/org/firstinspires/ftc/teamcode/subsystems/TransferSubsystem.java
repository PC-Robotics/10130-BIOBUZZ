package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Constants;

public class TransferSubsystem {
    private final DcMotorEx motor;

    public TransferSubsystem(HardwareMap map) {
        motor = map.get(DcMotorEx.class, Constants.Names.TRANSFER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setPower(double power) { motor.setPower(Math.max(-1, Math.min(1, power))); }
    public void stop() { motor.setPower(0); }
}
