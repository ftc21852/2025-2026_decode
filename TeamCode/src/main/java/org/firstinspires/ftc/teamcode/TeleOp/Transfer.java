package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Date;

/*
run kicker 2s
run intake 2s
repeat
 */
public class Transfer {
    private Gamepad gamepad;
    private DcMotorEx intake;
    private DcMotorEx kicker;
    private Telemetry telemetry;

    private String mode = "stop";
    private boolean alternating = false;
    private long nextAlternatingTime;

    public Transfer(Gamepad gamepad, DcMotorEx intake, DcMotorEx kicker, Telemetry telemetry) {
        this.gamepad = gamepad;

        this.intake = intake;
        this.kicker = kicker;

        intake.setDirection(DcMotorEx.Direction.REVERSE);
        kicker.setDirection(DcMotorEx.Direction.FORWARD);

        intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        kicker.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        intake.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        kicker.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        this.telemetry = telemetry;
    }

    private void in() {
        intake.setPower(0.6);
        kicker.setVelocity(-1000);
        mode = "in";
    }

    private void out() {
        intake.setPower(0.4);
        kicker.setVelocity(1000);
        mode = "out";
    }

    private void reverse() {
        intake.setPower(-0.6);
        kicker.setVelocity(-1000);
        mode = "reverse";
    }

    private void stop() {
        intake.setPower(0);
        kicker.setVelocity(0);
        mode = "stop";
    }

    public void update() {
        if (gamepad.left_bumper) {
            if (alternating) {
                if (new Date().getTime() >= nextAlternatingTime) {
                    if (mode == "out") {
                        stop();
                        nextAlternatingTime += 750;
                    } else {
                        out();
                        nextAlternatingTime += 750;
                    }
                }
            } else {
                alternating = true;
                nextAlternatingTime = new Date().getTime();
            }
        } else {
            alternating = false;
            if (gamepad.right_trigger > 0.1) {
                in();
            } else if (gamepad.left_trigger > 0.1) {
                out();
            } else if (gamepad.right_bumper) {
                reverse();
            } else if (gamepad.y) {
                intake.setPower(0.6);
                kicker.setVelocity(-450);
            } else {
                stop();
            }
        }

        telemetry.addData("Intake speed", "%.2f", intake.getVelocity());
        telemetry.addData("Kicker speed", "%.2f", kicker.getVelocity());
    }
}
