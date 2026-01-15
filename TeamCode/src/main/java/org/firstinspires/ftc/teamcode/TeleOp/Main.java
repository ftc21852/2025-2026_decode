package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Main")

public class Main extends LinearOpMode {
    @Override
    public void runOpMode() {
        // Initialize chassis motors
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "fl");
        DcMotorEx backLeft  = hardwareMap.get(DcMotorEx.class, "bl");
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "fr");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "br");

        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        DcMotor kickerMotor = hardwareMap.get(DcMotor.class, "kicker");
        DcMotor flywheelMotor = hardwareMap.get(DcMotor.class, "flywheel");

        Chassis chassis = new Chassis(gamepad1, frontLeft, backLeft, frontRight, backRight, telemetry);
        Transfer transfer = new Transfer(gamepad2, intakeMotor, kickerMotor, telemetry);
        Flywheel flywheel = new Flywheel(gamepad2, flywheelMotor, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            chassis.update();
            transfer.update();
            flywheel.update();
            telemetry.update();
        }
    }
}
