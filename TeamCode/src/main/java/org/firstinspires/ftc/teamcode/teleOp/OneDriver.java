package org.firstinspires.ftc.teamcode.teleOp;

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

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

import java.util.function.Supplier;

@TeleOp(name = "One Driver")
public class OneDriver extends LinearOpMode {

    private Follower follower;
    private boolean automatedDrive = false;
    private static final Pose SHOOTING_POSE = new Pose(60, 60, Math.toRadians(-135));
    private Supplier<PathChain> toShootingPath;

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
        //TODO FIX THIS HAS TO BE END AUTO POSE
        Chassis chassis = new Chassis(gamepad1, frontLeft, backLeft, frontRight, backRight, telemetry, new Pose(24,21, Math.toRadians(-90)), hardwareMap);

        Transfer transfer = new Transfer(gamepad1, intakeMotor, gateServo, telemetry);
        Flywheel flywheel = new Flywheel(gamepad1, flywheelTop, flywheelBottom, hoodServo, led, telemetry);
        Lift lift = new Lift(gamepad1, liftMotor, telemetry);
        Camera camera = new Camera(limelight);

        Auto autoFlywheel = new Auto(null);


        autoFlywheel.
                run(flywheel, 1110).
                run(transfer::intake).
                wait(0.5).
                stop(transfer).
                wait(0.5).
                run(transfer::shoot).
                run(flywheel, 1060).
                wait(1.8).
                stop(flywheel).
                run(autoFlywheel::stop).
        stop();

        toShootingPath = () -> chassis.getFollower().pathBuilder()
                .addPath(new Path(new BezierLine(chassis.getFollower()::getPose, SHOOTING_POSE)))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(
                        chassis.getFollower()::getHeading, SHOOTING_POSE.getHeading(), 0.8))
                .build();

        waitForStart();
        chassis.startTeleOp();

        while (opModeIsActive()) {
            chassis.pedroUpdate();
            transfer.update();
            flywheel.update();
            lift.update();
            autoFlywheel.update();

            if (!automatedDrive) {
                chassis.teleOpDrive();
            }

            if (gamepad1.bWasPressed() && !automatedDrive) {
                chassis.followPath(toShootingPath.get());
                automatedDrive = true;
            }

            if (automatedDrive && !chassis.isBusy()) {
                automatedDrive = false;
                chassis.startTeleOp();
                autoFlywheel.begin();
            }


            if (gamepad1.aWasPressed()) {
                autoFlywheel.begin();
            }

            telemetry.addData("Limelight distance", camera.getGroundDistance());
            telemetry.update();
        }
    }
}