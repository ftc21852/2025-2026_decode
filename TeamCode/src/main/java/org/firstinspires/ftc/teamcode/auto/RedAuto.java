package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.Flywheel;
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
        sequence.run(() -> flywheel.setVelocity(1140));
        sequence.run(transfer::intake);
        sequence.wait(500);
        sequence.run(transfer::stop);
        sequence.wait(500);
        sequence.run(transfer::shoot);
        sequence.run(() -> flywheel.setVelocity(1050));
        sequence.wait(1800);
        /*
        sequence.run(transfer::intake);
        sequence.wait(500);
        sequence.run(transfer::stop);
        sequence.wait(500);
        sequence.run(transfer::shoot);
        sequence.wait(600);
         */
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

        transfer = new Transfer(null, intakeMotor, kickerMotor, telemetry);
        flywheel = new Flywheel(null, flywheelMotor, telemetry);

        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Pose startPose = new Pose(24,123, Math.toRadians(90));
        Pose shootPose = new Pose(60, 84, Math.toRadians(135));
        Pose beforeRow1 = new Pose(60, 84, Math.toRadians(90));
        Pose afterRow1 = new Pose(60, 135, Math.toRadians(90));
        Pose beforeRow2 = new Pose(84, 84, Math.toRadians(90));
        Pose afterRow2 = new Pose(84, 135, Math.toRadians(90));
        Pose beforeRow3 = new Pose(108, 84, Math.toRadians(90));
        Pose afterRow3 = new Pose(108, 135, Math.toRadians(90));

        sequence = new Sequence();
        sequence.run(() -> follower.followPath(pathBetween(startPose, shootPose)));
        sequence.waitUntil(() -> !follower.isBusy());

        addShootToSequence();

        sequence.run(transfer::intake);
        sequence.run(() -> flywheel.setVelocity(0));
        sequence.wait(250);
        sequence.run(() -> follower.followPath(pathBetween(beforeRow1, afterRow1)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(pathBetween(afterRow1, shootPose)));
        sequence.waitUntil(() -> !follower.isBusy());

        addShootToSequence();

        sequence.run(transfer::intake);
        sequence.run(() -> flywheel.setVelocity(0));
        sequence.wait(250);
        sequence.run(() -> follower.followPath(pathBetween(shootPose, beforeRow2)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(pathBetween(beforeRow2, afterRow2)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(pathBetween(afterRow2, shootPose)));
        sequence.waitUntil(() -> !follower.isBusy());

        addShootToSequence();

        sequence.run(transfer::intake);
        sequence.run(() -> flywheel.setVelocity(0));
        sequence.wait(250);
        sequence.run(() -> follower.followPath(pathBetween(shootPose, beforeRow3)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(pathBetween(beforeRow3, afterRow3)));
        sequence.waitUntil(() -> !follower.isBusy());
        sequence.run(() -> follower.followPath(pathBetween(afterRow3, shootPose)));
        sequence.waitUntil(() -> !follower.isBusy());

        addShootToSequence();

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

        telemetry.addData("x: ", follower.getPose().getX());
        telemetry.addData("y: ", follower.getPose().getY());
        telemetry.addData("heading: ", follower.getPose().getHeading());
        telemetry.addData("time: ", opModeTimer.getElapsedTimeSeconds());
    }
}