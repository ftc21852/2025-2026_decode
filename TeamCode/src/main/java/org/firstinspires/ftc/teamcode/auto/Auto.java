package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.teamcode.components.util.Sequence;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.components.Transfer;
import org.firstinspires.ftc.teamcode.components.Flywheel;

public class Auto extends Sequence {
    private Follower follower;
    private Pose lastPose;
    private PathChain lastPathChain;

    public Auto(Follower follower) {
        this.follower = follower;
    }

    private static double clockToRadians(double heading) {
        if (heading > 100) {
            heading = Math.floor(heading / 100) + heading % 100 / 60;
        }
        return Math.toRadians((3 - heading) * 30);
    }

    public Auto startAt(double x, double y) {
        run(() -> follower.setPose(new Pose(x * 24, y * 24)));
        lastPose = new Pose(x * 24, y * 24);
        return this;
    }

    public Auto startAt(int x, int y) {
        return startAt(x / 24.0, y / 24.0);
    }

    public Auto goTo(double x, double y) {
        lastPathChain = follower.pathBuilder()
                .addPath(new BezierLine(lastPose, lastPose = new Pose(x * 24, y * 24)))
                .build();
        lastPose.setHeading(lastPathChain.getFinalHeadingGoal());
        run(() -> follower.followPath(lastPathChain));
        waitUntil(() -> !follower.isBusy());
        return this;
    }

    public Auto goTo(int x, int y) {
        return goTo(x / 24.0, y / 24.0);
    }

    public Auto facing(double x, double y) {
        lastPathChain.setHeadingInterpolator(HeadingInterpolator.facingPoint(x * 24, y * 24));
        lastPose.setHeading(lastPathChain.getFinalHeadingGoal());
        return this;
    }

    public Auto facing(int x, int y) {
        return facing(x / 24.0, y / 24.0);
    }

    public Auto facing(int clockTime) {
        return facing(clockToRadians(clockTime));
    }

    public Auto facing(double heading) {
        if (lastPathChain != null) {
            lastPathChain.setHeadingInterpolator(HeadingInterpolator.linear(lastPose.getHeading(), heading));
        }
        lastPose.setHeading(heading);
        return this;
    }

    public Auto turnTo(int clockTime) {
        return turnTo(clockToRadians(clockTime));
    }

    public Auto turnTo(double heading) {
        run(() -> follower.followPath(follower.pathBuilder()
                .addPath(new BezierLine(lastPose, lastPose))
                .setConstantHeadingInterpolation(heading)
                .build()));
        lastPose.setHeading(heading);
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
}

/*

Hornets strategy:
shoot 3 balls together, high efficiency

the spin speed between 3 balls is different but is minimized

use hood to find the best angle, the first ball higher
the second is middle
the third is lower

after good finding, this is the most efficient robot design we could make.

*victory sound effects*

We need major redesign, and these are what we will be working on:

1.two flywheel motors(very very very very very very important)
advantages:
more consistent speed
don't overheat

smaller friction between hood and flywheel(very very very important):
material don't wear out

adjustable hood: servo (important)
Tuning in auto and


intake(very very important): alternating between dental tubing and flex wheels
advantages: smooth and quick intake.

method:
first intake set: dental tubes+wheels
second intake set: only dental tubes.

linear slope x
a curve from the start is good. this makes sure thar there are no abrupt curve up to the flywheel, and slowly
added the angle to give a smooth connection with the flywheel
small curve to keep balls in the middle

chassis: aligns with our design

side plates
notice that good robot have designs that have a sturdy base and common shapes like squares.
This makes sures that the robot has a low CM, and won't get bumped around and overkilled by the other team's
defense.

belts are not of main concern. Two of the hornets all use belts, which turn totally fine.

motors
1. flywheel
2. flywheel
3. intake
4. lift
5. fl
6. fr
7. bl
8. br

servos
1. lid
2. hood

output
1. telemetry
2. led x2

pedro pathing


 */