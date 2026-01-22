package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Chassis {
    private Gamepad gamepad;
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;
    private Telemetry telemetry;

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

    public void update() {
        double x = gamepad.left_stick_x;
        double y = gamepad.left_stick_y;
        double rx = gamepad.right_stick_x;

        double frontLeftPower = y - x - rx;
        double frontRightPower = y + x + rx;
        double backLeftPower = y + x - rx;
        double backRightPower = y - x + rx;

        frontLeft.setPower(frontLeftPower * 0.6);
        frontRight.setPower(frontRightPower * 0.6);
        backLeft.setPower(backLeftPower * 0.6);
        backRight.setPower(backRightPower * 0.6);

        telemetry.addData("X-velocity", x);
        telemetry.addData("Y-velocity", y);
        telemetry.addData("Rotation", -rx);
    }

    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}
