package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Date;

public class Transfer {
    private Gamepad gamepad;
    private DcMotorEx intakeMotor;
    private Servo gateServo;
    private Telemetry telemetry;
    private long startShootTime = 0;
    private boolean override = false;

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

    public void forward() {
        intakeMotor.setVelocity(2000);
    }

    public void reverse() {
        intakeMotor.setVelocity(-1700);
    }

    public void stop() {
        intakeMotor.setVelocity(0);
    }

    public void openGate() {
        gateServo.setPosition(0.4);
    }

    public void closeGate() {
        gateServo.setPosition(0);
    }

    public void override() {
        override = true;
    }

    public void stopOverride() {
        override = false;
    }

    public void update() {
        if (override) {
            return;
        }
        if (gamepad.left_trigger > 0.1) {
            if (gateServo.getPosition() < 0.2) {
                startShootTime = new Date().getTime() + 500;
            }
            if (new Date().getTime() > startShootTime) {
                forward();
            } else {
                stop();
            }
            openGate();
        } else if (gamepad.right_trigger > 0.1) {
            forward();
            closeGate();
        } else if (gamepad.right_bumper) {
            reverse();
            closeGate();
        } else {
            stop();
        }

        telemetry.addData("gate position", gateServo.getPosition());

        telemetry.addData("Intake speed", "%.2f", intakeMotor.getVelocity());
    }
}
