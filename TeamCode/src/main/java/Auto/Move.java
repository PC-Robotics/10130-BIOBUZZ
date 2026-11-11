package Auto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

@Autonomous(name = "Simple Auto - Forward", group = "Competition")
public class Move extends LinearOpMode {
    private static final double DRIVE_POWER = 0.5;
    private static final long DRIVE_TIME_MS = 500;

    @Override
    public void runOpMode() {
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);

        telemetry.addLine("Ready. Drives forward " + DRIVE_TIME_MS + " ms.");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        drive.driveRobotCentric(DRIVE_POWER, 0, 0, 1.0);
        sleep(DRIVE_TIME_MS);
        drive.stop();
    }
}