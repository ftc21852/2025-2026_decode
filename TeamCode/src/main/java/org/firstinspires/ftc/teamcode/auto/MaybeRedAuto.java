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

@Autonomous (name = "Maybe Red")
public class MaybeRedAuto extends OpMode {
    private Follower follower;
    private Timer opModeTimer;
    Transfer transfer;
    Flywheel flywheel;
    private Auto auto;

    public void start() {
        auto.begin();
        opModeTimer.resetTimer();
    }

    @Override
    public void loop() {
        follower.update();
        auto.update();
    }

    @Override
    public void init() {
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        DcMotorEx kickerMotor = hardwareMap.get(DcMotorEx.class, "kicker");
        DcMotorEx flywheelTop = hardwareMap.get(DcMotorEx.class, "flywheel-top");
        DcMotorEx flywheelBottom = hardwareMap.get(DcMotorEx.class, "flywheel-bottom");

        transfer = new Transfer(null, intakeMotor, null, telemetry);
        flywheel = new Flywheel(null, flywheelTop, flywheelBottom, null, null,  telemetry);

        auto = new Auto(follower);

        auto.
                startAt(123, 120). facing(3).
                goTo(56, 56). facing(1_30).

                run(flywheel, 1110).
                run(transfer::intake).
                wait(0.5).
                stop(transfer).
                wait(0.5).
                run(transfer::shoot).
                run(flywheel, 1060).
                wait(1.8).

                stop(flywheel).
                run(transfer::intake).
                wait(0.25).
                goTo(100, 84). facing(3).
                goTo(135, 84).
                goTo(126, 74). facing(12).
                goTo(135, 74).
                wait(0.5).
                goTo(56, 56). facing(1_30).

                run(flywheel, 1110).
                run(transfer::intake).
                wait(0.5).
                stop(transfer).
                wait(0.5).
                run(transfer::shoot).
                run(flywheel, 1060).
                wait(1.8).

                stop(flywheel).
                run(transfer::intake).
                goTo(100, 60). facing(3).
                goTo(135, 60).
                goTo(56, 56). facing(1_30).

                run(flywheel, 1110).
                run(transfer::intake).
                wait(0.5).
                stop(transfer).
                wait(0.5).
                run(transfer::shoot).
                run(flywheel, 1060).
                wait(1.8).

                stop(flywheel).
                run(transfer::intake).
                goTo(100, 36). facing(3).
                goTo(135, 36).
                goTo(56, 56). facing(1_30).

                run(flywheel, 1110).
                run(transfer::intake).
                wait(0.5).
                stop(transfer).
                wait(0.5).
                run(transfer::shoot).
                run(flywheel, 1060).
                wait(1.8).

                goTo(120, 74). facing(6).
        stop();
    }
}