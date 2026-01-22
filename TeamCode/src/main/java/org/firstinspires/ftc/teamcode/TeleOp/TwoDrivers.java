package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.Date;

@TeleOp(name = "Two Drivers")

public class TwoDrivers extends LinearOpMode {
    @Override
    public void runOpMode() {
        // Initialize chassis motors
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "fl");
        DcMotorEx backLeft  = hardwareMap.get(DcMotorEx.class, "bl");
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "fr");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "br");

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        DcMotorEx kickerMotor = hardwareMap.get(DcMotorEx.class, "kicker");
        DcMotorEx flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheel");

        flywheelMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        Chassis chassis = new Chassis(gamepad1, frontLeft, backLeft, frontRight, backRight, telemetry);
        ArtifactSystem artifactSystem = new ArtifactSystem(gamepad1, intakeMotor, kickerMotor, flywheelMotor, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            chassis.update();
            artifactSystem.update();
            telemetry.addData("Flywheel velocity", "%.2f", flywheelMotor.getVelocity());
            telemetry.update();
        }
    }
}
