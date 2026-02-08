package org.firstinspires.ftc.teamcode.teleOp;

import org.firstinspires.ftc.teamcode.auto.Constants;
import com.pedropathing.follower.Follower;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.components.Camera;
import org.firstinspires.ftc.teamcode.components.Chassis;
import org.firstinspires.ftc.teamcode.components.Flywheel;
import org.firstinspires.ftc.teamcode.components.Lift;
import org.firstinspires.ftc.teamcode.components.Transfer;

@TeleOp(name = "One Driver")
public class OneDriver extends LinearOpMode {
    @Override
    public void runOpMode() {
        // ctrl hub
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "fl");           // 0
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "fr");          // 1
        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");     // 2
        DcMotorEx liftMotor = hardwareMap.get(DcMotorEx.class, "lift");         // 3
        Servo led = hardwareMap.get(Servo.class, "led");

        // expn hub
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "bl");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "br");
        DcMotorEx flywheelTop = hardwareMap.get(DcMotorEx.class, "flywheel-top");
        DcMotorEx flywheelBottom = hardwareMap.get(DcMotorEx.class, "flywheel-bottom");
        Servo gateServo = hardwareMap.get(Servo.class, "gate");
        Servo hoodServo = hardwareMap.get(Servo.class, "hood");

        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");

        Follower follower = Constants.createFollower(hardwareMap);

        Chassis chassis = new Chassis(gamepad1, frontLeft, backLeft, frontRight, backRight, follower, telemetry);
        Transfer transfer = new Transfer(gamepad1, intakeMotor, gateServo, telemetry);
        Camera camera = new Camera(limelight, telemetry);
        Flywheel flywheel = new Flywheel(gamepad1, flywheelTop, flywheelBottom, hoodServo, led, camera, telemetry);
        Lift lift = new Lift(gamepad1, liftMotor, telemetry);

        Auto autoFlywheel = new Auto(null);
        autoFlywheel
                .run(transfer::override)
                .run(flywheel::override)
                .run(flywheel, 1260)
                .run(transfer::closeGate)
                .run(transfer::forward)
                .wait(1.0)
                .run(transfer::openGate)
                .wait(2.0)
                .run(transfer::stopOverride)
                .run(flywheel::stopOverride)
                .run(autoFlywheel::stop)
                .stop();

        Auto autoPath = new Auto(follower);
        autoPath
                .startAt(5.1, 5.0) .facing(3)
                .anchor()
                .run(chassis::override)
                .goTo(3.5, 3.5)
                .run(chassis::stopOverride)
                .run(autoPath::stop)
                .stop();

        follower.setStartingPose(new Pose(5.1 * 24, 5.0 * 24, 0));
        waitForStart();

        while (opModeIsActive()) {
            chassis.update();
            transfer.update();
            flywheel.update();
            lift.update();
            autoFlywheel.update();
            camera.update();
            follower.update();

            if (gamepad1.xWasPressed()) {
                autoFlywheel.begin();
            }
            telemetry.update();
        }
    }
}