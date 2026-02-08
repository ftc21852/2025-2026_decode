package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.components.Flywheel;
import org.firstinspires.ftc.teamcode.components.Transfer;
import org.firstinspires.ftc.teamcode.components.util.Sequence;

import java.util.function.BooleanSupplier;

public class Auto extends Sequence {
    private Follower follower;
    public PathChain lastPathChain;
    private double lastPathChainStartHeading;
    private boolean startingHeadingSet = false;

    public Auto(Follower follower) {
        this.follower = follower;
    }

    public Auto() {

    }

    private static double clockToRadians(double heading) {
        if (heading > 100) {
            heading = Math.floor(heading / 100) + heading % 100 / 60;
        }
        return Math.toRadians((3 - heading) * 30);
    }

    public Auto startAt(double x, double y) {
        Pose pose = new Pose(x * 24, y * 24);
        lastPathChain = follower.pathBuilder()
                .addPath(new BezierLine(pose, pose))
                .build();
        lastPathChainStartHeading = 0;
        follower.setPose(pose);
        return this;
    }

    public Auto startAt(int x, int y) {
        return startAt(x / 24.0, y / 24.0);
    }

    public Auto anchor() {
        Pose pose = follower.getPose();
        lastPathChain = follower.pathBuilder()
                .addPath(new BezierLine(pose, pose))
                .build();
        return this;
    }

    public Auto goTo(double x, double y) {
        Pose startPose = lastPathChain.endPose();
        lastPathChainStartHeading = lastPathChain.getFinalHeadingGoal();

        final PathChain pathChain = lastPathChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose, new Pose(x * 24, y * 24)))
                .build();
        run(() -> follower.followPath(pathChain));
        waitUntil(() -> !follower.isBusy());
        return this;
    }

    public Auto goTo(int x, int y) {
        return goTo(x / 24.0, y / 24.0);
    }

    public Auto facing(double x, double y) {
        lastPathChain.setHeadingInterpolator(HeadingInterpolator.facingPoint(x * 24, y * 24));
        return this;
    }

    public Auto facing(int x, int y) {
        return facing(x / 24.0, y / 24.0);
    }

    public Auto facing(int clockTime) {
        return facing(clockToRadians(clockTime));
    }

    public Auto facing(double heading) {
        if (!startingHeadingSet) {
            PathChain pathChain = lastPathChain;
            follower.setPose(pathChain.endPose().setHeading(heading));
        } else {
            lastPathChain.setHeadingInterpolator(HeadingInterpolator.linear(lastPathChainStartHeading, heading));
        }
        startingHeadingSet = true;
        return this;
    }

    public Auto turnTo(int clockTime) {
        return turnTo(clockToRadians(clockTime));
    }

    public Auto turnTo(double heading) {
        Pose pose = lastPathChain.endPose();
        run(() -> follower.turnTo(heading));
        waitUntil(() -> !follower.isTurning());
        lastPathChain = follower.pathBuilder()
                .addPath(new BezierLine(pose, pose))
                .setLinearHeadingInterpolation(heading, heading)
                .build();
        return this;
    }

    public Auto run(Flywheel flywheel, double speed) {
        run(() -> flywheel.setSpeed(speed));
        return this;
    }

    public Auto stop(Transfer transfer) {
        run(transfer::stop);
        return this;
    }

    public Auto stop(Flywheel flywheel) {
        run(() -> flywheel.setSpeed(0));
        return this;
    }

    public Auto run(Runnable function) {
        super.run(function);
        return this;
    }

    public Auto wait(double seconds) {
        super.wait((int) (seconds * 1000));
        return this;
    }

    public Auto wait(int milliseconds) {
        super.wait(milliseconds);
        return this;
    }

    public Auto waitUntil(BooleanSupplier condition) {
        super.waitUntil(condition);
        return this;
    }
}
