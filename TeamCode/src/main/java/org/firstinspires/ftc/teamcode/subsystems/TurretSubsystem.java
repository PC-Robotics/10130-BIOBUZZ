package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Constants;

/**
 * Turret homing and position control.
 * Limit-switch polarity and motor direction MUST be verified on the actual robot.
 */
public class TurretSubsystem {
    public enum State { UNHOMED, HOMING_LEFT, HOMING_RIGHT, READY, FAULT }

    private final DcMotorEx motor;
    private final DigitalChannel leftLimit, rightLimit;
    private final ElapsedTime homeTimer = new ElapsedTime();
    private State state = State.UNHOMED;
    private int leftTicks = 0;
    private int rightTicks = 0;
    private int targetTicks = 0;
    private boolean homingSwitchWasSeen = false;

    public TurretSubsystem(HardwareMap map) {
        motor = map.get(DcMotorEx.class, Constants.Names.TURRET);
        leftLimit = map.get(DigitalChannel.class, Constants.Names.TURRET_LEFT_LIMIT);
        rightLimit = map.get(DigitalChannel.class, Constants.Names.TURRET_RIGHT_LIMIT);
        leftLimit.setMode(DigitalChannel.Mode.INPUT);
        rightLimit.setMode(DigitalChannel.Mode.INPUT);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    //TODO: Make sure that the limiter switches work they way I think they do
    private boolean isLeftPressed() { return !leftLimit.getState(); }
    private boolean isRightPressed() { return !rightLimit.getState(); }

    public void beginHoming() {
        if (state == State.FAULT) return;
        homeTimer.reset();
        homingSwitchWasSeen = false;

        if (isLeftPressed() || isRightPressed()) {
            state = State.FAULT;
            motor.setPower(0);
            return;
        }
        state = State.HOMING_LEFT;
    }

    public void update() {
        if (state == State.HOMING_LEFT) {
            if (homeTimer.seconds() > Constants.Turret.HOME_TIMEOUT_SECONDS) {
                fail();
                return;
            }
            if (isLeftPressed()) {
                motor.setPower(0);
                leftTicks = motor.getCurrentPosition();
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftTicks = 0;
                homeTimer.reset();
                homingSwitchWasSeen = true;
                state = State.HOMING_RIGHT;
            } else {
                motor.setPower(-Constants.Turret.HOME_POWER);
            }
        } else if (state == State.HOMING_RIGHT) {
            if (homeTimer.seconds() > Constants.Turret.HOME_TIMEOUT_SECONDS) {
                fail();
                return;
            }
            if (isRightPressed()) {
                motor.setPower(0);
                rightTicks = motor.getCurrentPosition();
                targetTicks = (leftTicks + rightTicks) / 2;
                state = State.READY;
                motor.setTargetPosition(targetTicks);
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motor.setPower(Constants.Turret.MAX_POWER);
            } else {
                motor.setPower(Constants.Turret.HOME_POWER);
            }
        } else if (state == State.READY) {
            if (isLeftPressed() && motor.getPower() < 0) {
                motor.setPower(0);
            }
            if (isRightPressed() && motor.getPower() > 0) {
                motor.setPower(0);
            }
            if (motor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
                double error = targetTicks - motor.getCurrentPosition();
                double power = Math.min(Constants.Turret.MAX_POWER,
                        Math.max(Constants.Turret.MIN_MOVING_POWER,
                                Math.abs(error) * Constants.Turret.KP));
                if (Math.abs(error) <= Constants.Turret.POSITION_TOLERANCE_TICKS) {
                    motor.setPower(0);
                } else {
                    motor.setPower(power);
                }
            }
        }
    }

    public void moveByDegrees(double degrees) {
        if (state != State.READY) return;
        double ticks = degrees / 360.0 * Constants.Motors.TURRET_TICKS_PER_OUTPUT_REV;
        int proposed = (int) Math.round(targetTicks + ticks);
        int min = Math.min(leftTicks, rightTicks);
        int max = Math.max(leftTicks, rightTicks);
        targetTicks = Math.max(min, Math.min(max, proposed));
        motor.setTargetPosition(targetTicks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(Constants.Turret.MAX_POWER);
    }

    public void center() {
        if (state != State.READY) return;
        targetTicks = (leftTicks + rightTicks) / 2;
        motor.setTargetPosition(targetTicks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(Constants.Turret.MAX_POWER);
    }

    private void fail() {
        motor.setPower(0);
        state = State.FAULT;
    }

    public void stop() { motor.setPower(0); }
    public State getState() { return state; }
    public int getPositionTicks() { return motor.getCurrentPosition(); }
    public int getTargetTicks() { return targetTicks; }
    public boolean isHomed() { return state == State.READY; }
    public boolean sawHomeSwitch() { return homingSwitchWasSeen; }
}
