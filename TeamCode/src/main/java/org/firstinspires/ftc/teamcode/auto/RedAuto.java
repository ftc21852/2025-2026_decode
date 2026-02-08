package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.geometry.BezierCurve;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.Camera;
import org.firstinspires.ftc.teamcode.components.Chassis;
import org.firstinspires.ftc.teamcode.components.Flywheel;
import org.firstinspires.ftc.teamcode.components.Lift;
import org.firstinspires.ftc.teamcode.components.util.Sequence;
import org.firstinspires.ftc.teamcode.components.Transfer;

@Autonomous (name = "Red")
public class RedAuto extends OpMode {
    private Follower follower;
    private Timer opModeTimer;
    private Sequence sequence;
    Transfer transfer;
    Flywheel flywheel;

    private void addShootToSequence() {
        sequence.run(() -> flywheel.setSpeed(1110));
        sequence.run(transfer::forward);
        sequence.wait(500);
        sequence.run(transfer::stop);
        sequence.wait(500);
        sequence.run(transfer::forward);
        sequence.run(() -> flywheel.setSpeed(1050));
        sequence.wait(1800);
    }

    public PathChain pathBetween(Pose start, Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierLine(start, end))
                .setLinearHeadingInterpolation(start.getHeading(), end.getHeading())
                .build();
    }

    @Override
    public void init() {
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);

        // ctrl hub
        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        Servo led = hardwareMap.get(Servo.class, "led");

        // expn hub
        DcMotorEx flywheelTop = hardwareMap.get(DcMotorEx.class, "flywheel-top");
        DcMotorEx flywheelBottom = hardwareMap.get(DcMotorEx.class, "flywheel-bottom");
        Servo gateServo = hardwareMap.get(Servo.class, "gate");
        Servo hoodServo = hardwareMap.get(Servo.class, "hood");

        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");

        Camera camera = new Camera(limelight, telemetry);

        transfer = new Transfer(null, intakeMotor, gateServo, telemetry);
        flywheel = new Flywheel(null, flywheelTop, flywheelBottom, hoodServo, led, camera, telemetry);

        Pose startPose = new Pose(24,123, Math.toRadians(90));
        Pose shootPose = new Pose(56, 88, Math.toRadians(135));
        Pose beforeRow1 = new Pose(60, 100, Math.toRadians(90));
        Pose afterRow1 = new Pose(60, 128, Math.toRadians(90));
        Pose pushGate = new Pose(70, 130, Math.toRadians(180));
        Pose beforeRow2 = new Pose(84, 100, Math.toRadians(90));
        Pose afterRow2 = new Pose(84, 135, Math.toRadians(90));
        Pose beforeRow3 = new Pose(108, 100, Math.toRadians(90));
        Pose afterRow3 = new Pose(108, 135, Math.toRadians(90));

        PathChain gatePath = follower.pathBuilder()
                .addPath(new BezierCurve(afterRow1, new Pose(65, 124), pushGate))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        sequence = new Sequence();
        sequence.run(() -> follower.followPath(pathBetween(startPose, shootPose)));
        sequence.waitUntil(() -> !follower.isBusy());

        addShootToSequence();

        sequence.run(transfer::forward);
        sequence.run(() -> flywheel.setSpeed(0));
        sequence.wait(250);
        sequence.run(() -> follower.followPath(pathBetween(beforeRow1, afterRow1)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(gatePath));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.wait(500);
        sequence.run(() -> follower.followPath(pathBetween(pushGate, shootPose)));
        sequence.waitUntil(() -> !follower.isBusy());

        addShootToSequence();

        sequence.run(transfer::forward);
        sequence.run(() -> flywheel.setSpeed(0));
        sequence.wait(250);
        sequence.run(() -> follower.followPath(pathBetween(shootPose, beforeRow2)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(pathBetween(beforeRow2, afterRow2)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(pathBetween(afterRow2, shootPose)));
        sequence.waitUntil(() -> !follower.isBusy());

        addShootToSequence();

        sequence.run(transfer::forward);
        sequence.run(() -> flywheel.setSpeed(0));
        sequence.wait(250);
        sequence.run(() -> follower.followPath(pathBetween(shootPose, beforeRow3)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(pathBetween(beforeRow3, afterRow3)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(pathBetween(afterRow3, shootPose)));
        sequence.waitUntil(() -> !follower.isBusy());

        addShootToSequence();

        sequence.run(() -> follower.followPath(pathBetween(shootPose, beforeRow2)));

        follower.setPose(startPose);
    }

    public void start() {
        opModeTimer.resetTimer();
        sequence.begin();
    }

    @Override
    public void loop() {
        follower.update();
        sequence.update();
    }
}