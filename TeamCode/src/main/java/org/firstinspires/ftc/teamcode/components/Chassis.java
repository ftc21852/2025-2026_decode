package org.firstinspires.ftc.teamcode.components;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Chassis {
    private Gamepad gamepad;
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;
    private Follower follower;
    private Telemetry telemetry;
    private boolean override = false;

    public Chassis(Gamepad gamepad, DcMotorEx frontLeft, DcMotorEx backLeft, DcMotorEx frontRight, DcMotorEx backRight, Follower follower, Telemetry telemetry) {
        this.gamepad = gamepad;

        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;

        frontLeft.setDirection(DcMotorEx.Direction.FORWARD);
        frontRight.setDirection(DcMotorEx.Direction.REVERSE);
        backLeft.setDirection(DcMotorEx.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.REVERSE);

        frontLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        this.follower = follower;
        follower.startTeleOpDrive();

        this.telemetry = telemetry;
    }

    public void override() {
        override = true;
    }

    public void stopOverride() {
        override = false;
    }

    public void update() {
        double x = gamepad.left_stick_x;
        double y = -gamepad.left_stick_y;
        double rx = gamepad.right_stick_x;

        if (Math.abs(x) < 0.1) {
            x = 0;
        }
        if (Math.abs(y) < 0.1) {
            y = 0;
        }
        /*
        if (gamepad.b) {
            gamepad.rumble(Gamepad.RUMBLE_DURATION_CONTINUOUS);
            follower.setTeleOpDrive(-y, -x, -rx * 0.7);
        } else {
            gamepad.stopRumble();
            follower.setTeleOpDrive(-y * 0.7, -x * 0.7, -rx * 0.5);
        }
        /*/
        double frontLeftPower = y + x + rx;
        double frontRightPower = y - x - rx;
        double backLeftPower = y - x + rx;
        double backRightPower = y + x - rx;

        double motorSpeed = 2000;
        if (gamepad.b) {
            motorSpeed = 2400;
            gamepad.rumble(Gamepad.RUMBLE_DURATION_CONTINUOUS);
        } else {
            gamepad.stopRumble();
        }

        frontLeft.setVelocity(frontLeftPower * motorSpeed);
        frontRight.setVelocity(frontRightPower * motorSpeed);
        backLeft.setVelocity(backLeftPower * motorSpeed);
        backRight.setVelocity(backRightPower * motorSpeed);

        telemetry.addData("fl", frontLeft.getVelocity());
        telemetry.addData("fr", frontRight.getVelocity());
        telemetry.addData("bl", backLeft.getVelocity());
        telemetry.addData("br", backRight.getVelocity());
        //*/
    }

    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}
