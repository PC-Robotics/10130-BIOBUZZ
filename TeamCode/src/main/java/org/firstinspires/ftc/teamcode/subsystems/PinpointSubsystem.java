package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;


public class PinpointSubsystem {
    private final GoBildaPinpointDriver pinpoint;

    public PinpointSubsystem(HardwareMap map) {
        pinpoint = map.get(GoBildaPinpointDriver.class, Constants.Names.PINPOINT);
        pinpoint.setOffsets(Constants.Pinpoint.X_OFFSET_MM, Constants.Pinpoint.Y_OFFSET_MM, DistanceUnit.MM);
        pinpoint.setEncoderResolution(
                GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(
                Constants.Pinpoint.REVERSE_X
                        ? GoBildaPinpointDriver.EncoderDirection.REVERSED
                        : GoBildaPinpointDriver.EncoderDirection.FORWARD,
                Constants.Pinpoint.REVERSE_Y
                        ? GoBildaPinpointDriver.EncoderDirection.REVERSED
                        : GoBildaPinpointDriver.EncoderDirection.FORWARD);
        pinpoint.resetPosAndIMU();
    }

    public void update() { pinpoint.update(); }
    public double getXmm() { return pinpoint.getPosX(); }
    public double getYmm() { return pinpoint.getPosY(); }
    public double getHeadingRadians() { return pinpoint.getHeading(); }
    public double getHeadingDegrees() { return Math.toDegrees(getHeadingRadians()); }
    public double getVelocityXmmPerSec() { return pinpoint.getVelX(); }
    public double getVelocityYmmPerSec() { return pinpoint.getVelY(); }
    public double getHeadingVelocityRadPerSec() { return pinpoint.getHeadingVelocity(); }
    public GoBildaPinpointDriver.DeviceStatus getStatus() { return pinpoint.getDeviceStatus(); }
    public void reset() { pinpoint.resetPosAndIMU(); }
}
