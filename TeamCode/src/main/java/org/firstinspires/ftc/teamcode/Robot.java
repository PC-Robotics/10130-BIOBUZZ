package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.subsystems.*;

public class Robot {
    public final DriveSubsystem drive;
    public final IntakeSubsystem intake;
    public final TransferSubsystem transfer;
    public final TurretSubsystem turret;
    public final FlywheelSubsystem flywheel;
    public final PinpointSubsystem pinpoint;

    public Robot(HardwareMap hardwareMap) {
        drive = new DriveSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap);
        transfer = new TransferSubsystem(hardwareMap);
        turret = new TurretSubsystem(hardwareMap);
        flywheel = new FlywheelSubsystem(hardwareMap);
        pinpoint = new PinpointSubsystem(hardwareMap);
    }

    public void update() {
        pinpoint.update();
        turret.update();
    }

    public void stop() {
        drive.stop();
        intake.stop();
        transfer.stop();
        turret.stop();
        flywheel.stop();
    }
}
