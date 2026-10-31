package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Constants;

public class FlywheelSubsystem {
    private final DcMotorEx motor;
    private double targetRpm = Constants.Flywheel.START_RPM;
    private boolean enabled = false;

    public FlywheelSubsystem(HardwareMap map) {
        motor = map.get(DcMotorEx.class, Constants.Names.FLYWHEEL);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void toggle() {
        enabled = !enabled;
        apply();
    }

    public void setEnabled(boolean value) {
        enabled = value;
        apply();
    }

    public void adjustTarget(double deltaRpm) {
        targetRpm = Math.max(Constants.Flywheel.MIN_RPM,
                Math.min(Constants.Flywheel.MAX_RPM, targetRpm + deltaRpm));
        apply();
    }

    private void apply() {
        if (enabled) {
            double ticksPerSecond = targetRpm / 60.0 * Constants.Motors.FLYWHEEL_TICKS_PER_REV;
            motor.setVelocity(ticksPerSecond);
        } else {
            motor.setVelocity(0);
            motor.setPower(0);
        }
    }

    public double getCurrentRpm() {
        return motor.getVelocity() / Constants.Motors.FLYWHEEL_TICKS_PER_REV * 60.0;
    }

    public double getTargetRpm() { return targetRpm; }
    public boolean isEnabled() { return enabled; }

    public boolean isAtSpeed() {
        return enabled && Math.abs(getCurrentRpm() - targetRpm)
                <= Constants.Flywheel.READY_TOLERANCE_RPM;
    }

    public void stop() {
        enabled = false;
        motor.setVelocity(0);
        motor.setPower(0);
    }
}
