package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(9.6)
            .forwardZeroPowerAcceleration(-40.506033987808095)
            .lateralZeroPowerAcceleration(-59.651478326558454)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.08, 0, 0.001, 0.02))
            .headingPIDFCoefficients(new PIDFCoefficients(0.43, 0, 0.01, 0.025))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.01, 0, 0.0005,0.6, 0.01))
            .centripetalScaling(0.0007)
            ;

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1.0)

            .leftFrontMotorName("fl")
            .rightFrontMotorName("fr")
            .leftRearMotorName("bl")
            .rightRearMotorName("br")

            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)

            .xVelocity(77.6160110113189)
            .yVelocity(64.00160854069267);
    public static PathConstraints pathConstraints = new PathConstraints(0.99,
            100,
            1.2,
            1);
    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(-6.5)
            .strafePodX(-7.5)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinPoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }

}
