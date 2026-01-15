package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Chassis {
    private Gamepad gamepad;
    private DcMotorEx FL, BL, FR, BR;
    private Telemetry telemetry;

    public Chassis(Gamepad gamepad, DcMotorEx frontLeft, DcMotorEx backLeft, DcMotorEx frontRight, DcMotorEx backRight, Telemetry telemetry) {
        this.gamepad = gamepad;

        FL = frontLeft;
        BL = backLeft;
        FR = frontRight;
        BR = backRight;

        FL.setDirection(DcMotorEx.Direction.REVERSE);
        BL.setDirection(DcMotorEx.Direction.REVERSE);
        FR.setDirection(DcMotorEx.Direction.FORWARD);
        BR.setDirection(DcMotorEx.Direction.FORWARD);

        FL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        this.telemetry = telemetry;
    }

    public void update() {
        double y = -gamepad.left_stick_y;
        double x = gamepad.left_stick_x;
        double rx = gamepad.right_stick_x;

        double powerFL = y + x + rx;
        double powerBL = y - x + rx;
        double powerFR = y - x - rx;
        double powerBR = y + x - rx;

        FL.setPower(powerFL);
        BL.setPower(powerBL);
        FR.setPower(powerFR);
        BR.setPower(powerBR);

        telemetry.addData("LF", "%.2f", powerFL);
        telemetry.addData("LR", "%.2f", powerBL);
        telemetry.addData("RF", "%.2f", powerFR);
        telemetry.addData("RR", "%.2f", powerBR);
    }

    public void stop() {
        FL.setPower(0);
        BL.setPower(0);
        FR.setPower(0);
        BR.setPower(0);
    }
}
