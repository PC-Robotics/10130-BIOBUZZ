package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.subsystems.Constants;

/**
 * Manual turret. Encoder zeroes at init, so START THE TURRET CENTERED.
 * Soft limits stop it from over-rotating (cable protection).
 */
public class TurretSubsystem {
    private final DcMotorEx motor;

    public TurretSubsystem(HardwareMap map) {
        motor = map.get(DcMotorEx.class, Constants.Names.TURRET);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /** input -1..1 (stick). Positive = whatever direction your motor spins forward. */
    public void setManualPower(double input) {
        double power = Math.max(-1, Math.min(1, input)) * Constants.Turret.MAX_POWER;
        int pos = motor.getCurrentPosition();
        if (pos >= Constants.Turret.MAX_TICKS && power > 0) power = 0;
        if (pos <= Constants.Turret.MIN_TICKS && power < 0) power = 0;
        motor.setPower(power);
    }

    /** Call when the turret is physically centered to re-zero. */
    public void zero() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public int getPositionTicks() { return motor.getCurrentPosition(); }

    public double getAngleDegrees() {
        return motor.getCurrentPosition() / Constants.Motors.TURRET_TICKS_PER_OUTPUT_REV * 360.0;
    }

    public void stop() { motor.setPower(0); }
}