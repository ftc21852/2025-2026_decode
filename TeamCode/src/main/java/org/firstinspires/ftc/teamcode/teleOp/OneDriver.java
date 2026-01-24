package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.components.Camera;
import org.firstinspires.ftc.teamcode.components.Chassis;
import org.firstinspires.ftc.teamcode.components.Flywheel;
import org.firstinspires.ftc.teamcode.components.Lift;
import org.firstinspires.ftc.teamcode.components.Transfer;

@TeleOp(name = "One Driver")
public class OneDriver extends LinearOpMode {
    @Override
    public void runOpMode() {
        // ctrl hub
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "fl");           // 0
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "fr");          // 1
        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");     // 2
        DcMotorEx liftMotor = hardwareMap.get(DcMotorEx.class, "lift");         // 3

        // expn hub
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "bl");            // 0
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "br");           // 1
        DcMotorEx kickerMotor = hardwareMap.get(DcMotorEx.class, "kicker");     // 2
        DcMotorEx flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheel"); // 3

        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");

        Chassis chassis = new Chassis(gamepad1, frontLeft, backLeft, frontRight, backRight, telemetry);
        Transfer transfer = new Transfer(gamepad1, intakeMotor, kickerMotor, telemetry);
        Flywheel flywheel = new Flywheel(gamepad1, flywheelMotor, telemetry);
        Lift lift = new Lift(gamepad1, liftMotor, telemetry);
        Camera camera = new Camera(limelight);

        waitForStart();

        while (opModeIsActive()) {
            chassis.update();
            transfer.update();
            flywheel.update();
            lift.update();
            telemetry.addData("Limelight distance", camera.getGroundDistance());
            telemetry.update();
        }
    }
}