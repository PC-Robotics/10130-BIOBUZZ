package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.subsystems.*;

public class Robot {
    public final DriveSubsystem drive;
    public final IntakeSubsystem intake;
    public final TransferSubsystem transfer;
    public final FlywheelSubsystem flywheel;
    public final TurretSubsystem turret;
    public final BackstopSubsystem backstop;

    public Robot(HardwareMap map) {
        drive = new DriveSubsystem(map);
        intake = new IntakeSubsystem(map);
        transfer = new TransferSubsystem(map);
        flywheel = new FlywheelSubsystem(map);
        turret = new TurretSubsystem(map);
        backstop = new BackstopSubsystem(map);
    }

    public void stop() {
        drive.stop();
        intake.stop();
        transfer.stop();
        flywheel.stop();
        turret.stop();
    }
}