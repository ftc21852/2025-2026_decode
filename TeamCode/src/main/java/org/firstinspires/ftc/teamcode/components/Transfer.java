package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
public class Transfer {
    private Gamepad gamepad;
    private DcMotorEx intake;
    private DcMotorEx kicker;
    private Telemetry telemetry;

    public Transfer(Gamepad gamepad, DcMotorEx intakeMotor, DcMotorEx kickerMotor, Telemetry telemetry) {
        this.gamepad = gamepad;

        this.intake = intakeMotor;
        this.kicker = kickerMotor;

        intakeMotor.setDirection(DcMotorEx.Direction.REVERSE);
        kickerMotor.setDirection(DcMotorEx.Direction.FORWARD);

        intakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        kickerMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        intakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        kickerMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        this.telemetry = telemetry;
    }

    public void intake() {
        intake.setPower(0.7);
        kicker.setVelocity(-1000);
    }

    public void reverse() {
        intake.setPower(0.4);
        kicker.setVelocity(1000);
    }

    public void shoot() {
        intake.setPower(0.5);
        kicker.setVelocity(1000);
    }

    public void stop() {
        intake.setPower(0);
        kicker.setVelocity(0);
    }

    public void update() {
        if (gamepad.left_trigger > 0.1) {
            shoot();
        } else if (gamepad.right_trigger > 0.1) {
            intake();
        } else if (gamepad.right_bumper) {
            reverse();
        } else {
            stop();
        }

        telemetry.addData("Intake speed", "%.2f", intake.getVelocity());
        telemetry.addData("Kicker speed", "%.2f", kicker.getVelocity());
    }
}
