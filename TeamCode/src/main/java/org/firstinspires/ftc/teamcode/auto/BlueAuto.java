package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.components.Flywheel;
import org.firstinspires.ftc.teamcode.components.Transfer;
import org.firstinspires.ftc.teamcode.components.util.Sequence;

@Autonomous (name = "Blue")
public class BlueAuto extends OpMode {
    private Follower follower;
    private Timer opModeTimer;
    private Sequence sequence;
    Transfer transfer;
    Flywheel flywheel;

    private void addShootToSequence() {
        sequence.run(() -> flywheel.setSpeed(1110));
        sequence.run(transfer::intake);
        sequence.wait(500);
        sequence.run(transfer::stop);
        sequence.wait(500);
        sequence.run(transfer::shoot);
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

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        DcMotorEx kickerMotor = hardwareMap.get(DcMotorEx.class, "kicker");
        DcMotorEx flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheel");

        transfer = new Transfer(null, intakeMotor, null, telemetry);
        flywheel = new Flywheel(null, flywheelMotor, flywheelMotor, null, null, telemetry);

        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Pose startPose = new Pose(24,21, Math.toRadians(-90));
        Pose shootPose = new Pose(56, 56, Math.toRadians(-135));
        Pose beforeRow1 = new Pose(60, 44, Math.toRadians(-90));
        Pose afterRow1 = new Pose(60, 16, Math.toRadians(-90));
        Pose pushGate = new Pose(70, 14, Math.toRadians(-180));
        Pose beforeRow2 = new Pose(84, 44, Math.toRadians(-90));
        Pose afterRow2 = new Pose(84, 9, Math.toRadians(-90));
        Pose beforeRow3 = new Pose(108, 44, Math.toRadians(-90));
        Pose afterRow3 = new Pose(108, 9, Math.toRadians(-90));

        PathChain gatePath = follower.pathBuilder()
                .addPath(new BezierCurve(afterRow1, new Pose(65, 20), pushGate))
                .setConstantHeadingInterpolation(Math.toRadians(-180))
                .build();

        sequence = new Sequence();
        sequence.run(() -> follower.followPath(pathBetween(startPose, shootPose)));
        sequence.waitUntil(() -> !follower.isBusy());

        addShootToSequence();

        sequence.run(transfer::intake);
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

        sequence.run(transfer::intake);
        sequence.run(() -> flywheel.setSpeed(0));
        sequence.wait(250);
        sequence.run(() -> follower.followPath(pathBetween(shootPose, beforeRow2)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(pathBetween(beforeRow2, afterRow2)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(pathBetween(afterRow2, shootPose)));
        sequence.waitUntil(() -> !follower.isBusy());

        addShootToSequence();

        sequence.run(transfer::intake);
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