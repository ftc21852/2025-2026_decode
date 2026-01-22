package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {
    private Gamepad gamepad;
    private DcMotorEx lift;
    private Telemetry telemetry;

    public Lift(Gamepad gamepad, DcMotorEx lift, Telemetry telemetry) {
        this.gamepad = gamepad;
        this.lift = lift;
        this.telemetry = telemetry;

        lift.setDirection(DcMotorEx.Direction.REVERSE);

        lift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    public void update() {
        if (gamepad.dpad_up) {
            lift.setPower(0.6);
        } else if (gamepad.dpad_down) {
            lift.setPower(-0.05);
        } else {
            lift.setPower(0);
        }
    }
}
