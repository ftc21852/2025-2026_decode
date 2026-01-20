package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "One Driver")

public class OneDriver extends LinearOpMode {
    @Override
    public void runOpMode() {
        // ctrl hub
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "fl");
        DcMotorEx backLeft  = hardwareMap.get(DcMotorEx.class, "bl");
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "fr");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "br");

        // expn hub
        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        DcMotorEx kickerMotor = hardwareMap.get(DcMotorEx.class, "kicker");
        DcMotorEx flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheel");
        DcMotorEx liftMotor = hardwareMap.get(DcMotorEx.class, "lift");

        Chassis chassis = new Chassis(gamepad1, frontLeft, backLeft, frontRight, backRight, telemetry);
        Transfer transfer = new Transfer(gamepad1, intakeMotor, kickerMotor, telemetry);
        Flywheel flywheel = new Flywheel(gamepad1, flywheelMotor, telemetry);
        Lift lift = new Lift(gamepad1, liftMotor, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            chassis.update();
            transfer.update();
            flywheel.update();
            lift.update();
            telemetry.update();
        }
    }
}
