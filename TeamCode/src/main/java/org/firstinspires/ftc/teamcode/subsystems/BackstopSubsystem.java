package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/** goBILDA speed servo in 5-turn mode, used as a position servo (0..1 = 5 turns). */
public class BackstopSubsystem {
    private final Servo servo;
    private double position = Constants.Backstop.SMALL_POS;

    public BackstopSubsystem(HardwareMap map) {
        servo = map.get(Servo.class, Constants.Names.BACKSTOP);
        servo.setPosition(position);
    }

    public void setPosition(double pos) {
        position = Math.max(0.0, Math.min(1.0, pos));
        servo.setPosition(position);
    }

    public void nudge(double delta) { setPosition(position + delta); }
    public void setSmall() { setPosition(Constants.Backstop.SMALL_POS); }
    public void setLarge() { setPosition(Constants.Backstop.LARGE_POS); }
    public double getPosition() { return position; }
}