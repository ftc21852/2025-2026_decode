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

    public long loopStartTime;
    private boolean looping = false;
    private int loopStage = 0;
    private long nextLoopTime;

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
    }

    private void out() {
        intake.setPower(0.4);
        kicker.setVelocity(1000);
    }

    private void reverse() {
        intake.setPower(-0.6);
        kicker.setVelocity(-1000);
    }

    private void stop() {
        intake.setPower(0);
        kicker.setVelocity(0);
    }

    public void update() {
        if (gamepad.left_trigger > 0.1) {
            if (looping) {
                if (new Date().getTime() >= nextLoopTime) {
                    switch (loopStage) {
                        case 0:
                        case 1:
                            out();
                            nextLoopTime += 500;
                            loopStage = 2;
                            break;
                        case 2:
                            in();
                            nextLoopTime += 500;
                            loopStage = 3;
                            break;
                        case 3:
                            stop();
                            nextLoopTime += 250;
                            loopStage = 1;
                            break;
                    }
                }
            } else {
                looping = true;
                loopStartTime = nextLoopTime = new Date().getTime();
                loopStage = 0;
            }
        } else {
            looping = false;
            loopStartTime = -1;
            if (gamepad.right_trigger > 0.1) {
                in();
            } else if (gamepad.right_bumper) {
                reverse();
            } else {
                stop();
            }
        }

        telemetry.addData("Intake speed", "%.2f", intake.getVelocity());
        telemetry.addData("Kicker speed", "%.2f", kicker.getVelocity());
    }
}
