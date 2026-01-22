package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "w/wo_intake+flywheel_chassis")
public class KaylenChassis extends LinearOpMode {

    // Chassis motors

    // wheel motor names: fl, bl, fr, br
    private static final String LF_NAME = "fl";
    private static final String LR_NAME = "bl";
    private static final String RF_NAME = "fr";
    private static final String RR_NAME = "br";
    private static final String FLYWHEEL_NAME = "flywheel";

    private static final String INTAKE_NAME = "intake";


    // find motor class
    private DcMotorEx leftFront, leftRear, rightFront, rightRear;
    private DcMotor flywheel, intake;





    @Override
    public void runOpMode() {
        // Initialize chassis motors
        leftFront = hardwareMap.get(DcMotorEx.class, LF_NAME);
        leftRear  = hardwareMap.get(DcMotorEx.class, LR_NAME);
        rightFront = hardwareMap.get(DcMotorEx.class, RF_NAME);
        rightRear = hardwareMap.get(DcMotorEx.class, RR_NAME);

        try{
            flywheel = hardwareMap.get(DcMotor.class, FLYWHEEL_NAME);
            flywheel.setDirection(DcMotor.Direction.FORWARD);
            flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        } catch(IllegalArgumentException e) {
            flywheel = null;
        }

        try{
            intake = hardwareMap.get(DcMotor.class, INTAKE_NAME);
            intake.setDirection(DcMotor.Direction.FORWARD);
            intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        } catch(IllegalArgumentException e) {
            intake = null;
        }


        // Motor directions
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);


        // Zero power behavior
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        telemetry.addLine("Chassis ready");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            double y  = -gamepad1.left_stick_y ;
            double x  =  gamepad1.left_stick_x;
            double rx =  gamepad1.right_stick_x ;
            //chassis
            leftFront.setPower(y + x + rx);
            leftRear.setPower(y - x + rx);
            rightFront.setPower(y - x - rx);
            rightRear.setPower(y + x - rx);


            if((flywheel != null)&&(gamepad2.a)){
                flywheel.setPower(0.4);
            }
            if((flywheel != null)&&(gamepad2.b)){
                flywheel.setPower(0);
            }


            if((intake != null)&&(gamepad2.dpad_up)){
                intake.setPower(-0.6);
            }
            if((intake != null)&&(gamepad2.dpad_down)){
                intake.setPower(0.6);
            }
            if((intake != null)&&((gamepad2.dpad_right)||(gamepad2.dpad_left))){
                intake.setPower(0);
            }


            telemetry.addData("LF", "%.2f");
            telemetry.addData("LR", "%.2f");
            telemetry.addData("RF", "%.2f");
            telemetry.addData("RR", "%.2f");
            if(flywheel != null) {
                telemetry.addData("Flywheel", "%.2f", flywheel.getPower());
            }
            if(intake != null) {
                telemetry.addData("Intake", "%.2f", intake.getPower());
            }
            telemetry.update();
        }
    }
}