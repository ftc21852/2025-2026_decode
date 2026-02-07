package org.firstinspires.ftc.teamcode.components;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.auto.Constants;

public class Chassis {
    private Gamepad gamepad;
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;
    private Telemetry telemetry;
    private Follower follower;

    public Chassis(Gamepad gamepad, DcMotorEx frontLeft, DcMotorEx backLeft, DcMotorEx frontRight, DcMotorEx backRight, Telemetry telemetry) {
        this.gamepad = gamepad;

        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;

        frontLeft.setDirection(DcMotorEx.Direction.FORWARD);
        frontRight.setDirection(DcMotorEx.Direction.REVERSE);
        backLeft.setDirection(DcMotorEx.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        this.telemetry = telemetry;
    }

    public Chassis(Gamepad gamepad, DcMotorEx frontLeft, DcMotorEx backLeft, DcMotorEx frontRight,
                   DcMotorEx backRight, Telemetry telemetry, Pose startingPose, HardwareMap hardwareMap) {
        this.gamepad = gamepad;

        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;

        frontLeft.setDirection(DcMotorEx.Direction.FORWARD);
        frontRight.setDirection(DcMotorEx.Direction.REVERSE);
        backLeft.setDirection(DcMotorEx.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose);
        follower.update();

        this.telemetry = telemetry;
    }

    public void update() {
        double x = gamepad.left_stick_x;
        double y = gamepad.left_stick_y;
        double rx = gamepad.right_stick_x;

        double frontLeftPower = y - x - rx;
        double frontRightPower = y + x + rx;
        double backLeftPower = y + x - rx;
        double backRightPower = y - x + rx;

        frontLeft.setPower(frontLeftPower * 0.8);
        frontRight.setPower(frontRightPower * 0.8);
        backLeft.setPower(backLeftPower * 0.8);
        backRight.setPower(backRightPower * 0.8);

        telemetry.addData("X-velocity", x);
        telemetry.addData("Y-velocity", y);
        telemetry.addData("Rotation", -rx);
    }

    public void teleOpDrive() {
        double x = gamepad.left_stick_x;
        double y = gamepad.left_stick_y;
        double rx = gamepad.right_stick_x;

        follower.setTeleOpDrive(
                -y, -x, -rx,
                true
        );
    }

    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void followPath(PathChain path) {
        follower.followPath(path);
    }


    public void startTeleOp() {
        follower.startTeleopDrive();
    }

    public boolean isBusy() {
        return follower.isBusy();
    }

    public Pose getPose() {
        return follower.getPose();
    }

    public Follower getFollower() {
        return follower;
    }

    public void pedroUpdate() {
        follower.update();
    }

}
