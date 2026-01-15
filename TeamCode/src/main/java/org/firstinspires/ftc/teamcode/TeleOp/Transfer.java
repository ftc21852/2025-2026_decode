package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Transfer {
    private Gamepad gamepad;
    private DcMotor intake;
    private DcMotor kicker;
    private Telemetry telemetry;

    public Transfer(Gamepad gamepad, DcMotor intake, DcMotor kicker, Telemetry telemetry) {
        this.gamepad = gamepad;

        this.intake = intake;
        this.kicker = kicker;

        intake.setDirection(DcMotor.Direction.REVERSE);
        kicker.setDirection(DcMotor.Direction.REVERSE);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        kicker.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.telemetry = telemetry;
    }

    public void update() {
        if (gamepad.left_bumper) {
            intake.setPower(0.6);
            kicker.setPower(0.6);

            telemetry.addLine("Intake running forward");
            telemetry.addLine("Kicker running");
        } else {
            if (gamepad.dpad_left || gamepad.dpad_right) {
                intake.setPower(0);
            } else if (gamepad.dpad_up) {
                intake.setPower(0.6);
                telemetry.addLine("Intake running forward");
            } else if (gamepad.dpad_down) {
                intake.setPower(-0.6);
                telemetry.addLine("Intake running backward");
            }
            kicker.setPower(0);
        }
    }
}
