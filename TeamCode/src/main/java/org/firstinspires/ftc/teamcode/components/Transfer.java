package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Transfer {
    private Gamepad gamepad;
    private DcMotorEx intakeMotor;
    private Servo gateServo;
    private Telemetry telemetry;

    public Transfer(Gamepad gamepad, DcMotorEx intakeMotor, Servo gateServo, Telemetry telemetry) {
        this.gamepad = gamepad;

        this.intakeMotor = intakeMotor;
        intakeMotor.setDirection(DcMotorEx.Direction.REVERSE);
        intakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        intakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        this.gateServo = gateServo;
        gateServo.setDirection(Servo.Direction.REVERSE);

        this.telemetry = telemetry;
    }

    public void intake() {
        intakeMotor.setPower(0.7);
        gateServo.setPosition(0);
    }

    public void reverse() {
        intakeMotor.setPower(-0.7);
        gateServo.setPosition(0);
    }

    public void shoot() {
        intakeMotor.setPower(0.5);
        gateServo.setPosition(0.4);
    }

    public void stop() {
        intakeMotor.setPower(0);
    }

    public void update() {
        if (gamepad.left_trigger > 0.1) {
            telemetry.addLine("shoot");
            shoot();
        } else if (gamepad.right_trigger > 0.1) {
            telemetry.addLine("intake");
            intake();
        } else if (gamepad.right_bumper) {
            telemetry.addLine("reverse");
            reverse();
        } else {
            stop();
        }

        telemetry.addData("gate position", gateServo.getPosition());

        telemetry.addData("Intake speed", "%.2f", intakeMotor.getVelocity());
    }
}
