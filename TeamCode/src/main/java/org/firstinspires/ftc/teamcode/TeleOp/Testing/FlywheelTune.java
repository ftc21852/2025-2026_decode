package org.firstinspires.ftc.teamcode.TeleOp.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.TeleOp.Chassis;
import org.firstinspires.ftc.teamcode.TeleOp.FlywheelUtil.BinarySearch;

@TeleOp(name = "Flywheel Tuning")
public class FlywheelTune extends LinearOpMode {

    // wheel motor names: fl, bl, fr, br
    private static final String LF_NAME = "fl";
    private static final String LR_NAME = "bl";
    private static final String RF_NAME = "fr";
    private static final String RR_NAME = "br";
    private static final String FLYWHEEL_NAME = "flywheel";

    // find motor class
    private DcMotorEx leftFront, leftRear, rightFront, rightRear;
    private DcMotor flywheel;

    public double getDistance() {
        return 0;
        /*
        final int FIELD_WIDTH = 48500;
        final int FIELD_HEIGHT = 48500;

        DcMotor leftOdo = hardwareMap.get(DcMotor.class, "leftOdo"); // x
        DcMotor rightOdo = hardwareMap.get(DcMotor.class, "rightOdo"); // y
        int leftTicks = leftOdo.getCurrentPosition();
        int rightTicks = rightOdo.getCurrentPosition();

        double offsetX = FIELD_WIDTH - leftTicks; // since we're testing using the red goal
        double offsetY = FIELD_HEIGHT - rightTicks;
        return Math.hypot(offsetX, offsetY);
        */
    }

    @Override
    public void runOpMode() {
        // Initialize chassis motors
        leftFront = hardwareMap.get(DcMotorEx.class, LF_NAME);
        leftRear  = hardwareMap.get(DcMotorEx.class, LR_NAME);
        rightFront = hardwareMap.get(DcMotorEx.class, RF_NAME);
        rightRear = hardwareMap.get(DcMotorEx.class, RR_NAME);

        flywheel = hardwareMap.get(DcMotor.class, FLYWHEEL_NAME);

        Chassis chassis = new Chassis(gamepad1, leftFront, leftRear, rightFront, rightRear, telemetry);

        // Motor directions
        flywheel.setDirection(DcMotor.Direction.FORWARD);

        // Zero power behavior
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        System.out.println("Chassis + Flywheel + servo ready");
        telemetry.update();
        waitForStart();

        String tuneMode = "";

        BinarySearch start = null;
        BinarySearch low = null;
        BinarySearch high = null;

        boolean dpad_up = false, dpad_down = false, dpad_right = false;
        boolean a = false, b = false, x = false,y = false;

        while (opModeIsActive()) {
            switch (tuneMode) {
                case "first goal":
                    if (gamepad2.dpad_up && !dpad_up) {
                        System.out.println("Launched too far at power " + start.getMid());
                        start.goLow();
                        flywheel.setPower(start.getMid());
                    }
                    if (gamepad2.dpad_down && !dpad_down) {
                        System.out.println("Launched too close at power " + start.getMid());
                        start.goHigh();
                        flywheel.setPower(start.getMid());
                    }
                    if (gamepad2.dpad_right && !dpad_right) {
                        System.out.println("Scored at power " + start.getMid());
                        low = new BinarySearch(start.getLow(), start.getMid());
                        tuneMode = "find lower bound";
                        flywheel.setPower(low.getMid());
                        System.out.println("Finding lower bound");
                    }
                    if (gamepad2.x && !x) {
                        tuneMode = "";
                        start = null;
                        low = null;
                        high = null;
                        flywheel.setPower(0);
                    }
                    break;
                case "find lower bound":
                    telemetry.addData("Lower bound", "%.3f", low.getHigh());
                    if (gamepad2.dpad_down && !dpad_down) {
                        System.out.println("Launched too close at power " + low.getMid());
                        low.goHigh();
                        flywheel.setPower(low.getMid());
                    }
                    if (gamepad2.dpad_right && !dpad_right) {
                        System.out.println("Scored at power " + low.getMid());
                        low.goLow();
                        flywheel.setPower(low.getMid());
                    }
                    if (gamepad2.a && !a) {
                        low = new BinarySearch(start.getLow(), start.getMid());
                        flywheel.setPower(low.getMid());
                        System.out.println("Finding lower bound");
                    }
                    if (gamepad2.y && !y) {
                        high = new BinarySearch(start.getMid(), start.getHigh());
                        tuneMode = "find upper bound";
                        flywheel.setPower(high.getMid());
                        System.out.println("Finding upper bound");
                    }
                    if (gamepad2.b && !b) {
                        if (high == null) {
                            System.out.println("Cannot produce results: test upper bound first");
                        } else {
                            System.out.println("The best power at distance " + getDistance() + " is " + (low.getHigh() + high.getLow()) / 2);
                        }
                    }
                    if (gamepad2.x && !x) {
                        if (high != null) {
                            System.out.println("The best power at distance " + getDistance() + " is " + (low.getHigh() + high.getLow()) / 2);
                        }
                        tuneMode = "";
                        start = null;
                        low = null;
                        high = null;
                        flywheel.setPower(0);
                        System.out.println("Ending test");
                    }
                    break;
                case "find upper bound":
                    telemetry.addData("Upper bound", "%.3f", high.getLow());
                    if (gamepad2.dpad_up && !dpad_up) {
                        System.out.println("Launched too far at power " + high.getMid());
                        high.goLow();
                        flywheel.setPower(high.getMid());
                    }
                    if (gamepad2.dpad_right && !dpad_right) {
                        System.out.println("Scored at power " + high.getMid());
                        high.goHigh();
                        flywheel.setPower(high.getMid());
                    }
                    if (gamepad2.a && !a) {
                        low = new BinarySearch(start.getLow(), start.getMid());
                        tuneMode = "find lower bound";
                        flywheel.setPower(low.getMid());
                        System.out.println("Finding lower bound");
                    }
                    if (gamepad2.y && !y) {
                        high = new BinarySearch(start.getMid(), start.getHigh());
                        flywheel.setPower(high.getMid());
                        System.out.println("Finding upper bound");
                    }
                    if (gamepad2.b && !b) {
                        System.out.println("The best power at distance " + getDistance() + " is " + (low.getHigh() + high.getLow()) / 2);
                    }
                    if (gamepad2.x && !x) {
                        System.out.println("The best power at distance " + getDistance() + " is " + (low.getHigh() + high.getLow()) / 2);
                        tuneMode = "";
                        start = null;
                        low = null;
                        high = null;
                        flywheel.setPower(0);
                        System.out.println("Ending test");
                    }
                    break;
                default:
                    chassis.update();

                    if (gamepad2.x && !x) {
                        chassis.stop();

                        System.out.println("Starting tuning at distance " + getDistance());
                        tuneMode = "first goal";
                        start = new BinarySearch(0, 1);
                        flywheel.setPower(start.getMid());
                    }
            }

            dpad_up = gamepad2.dpad_up;
            dpad_down = gamepad2.dpad_down;
            dpad_right = gamepad2.dpad_right;
            a = gamepad2.a;
            b = gamepad2.b;
            x = gamepad2.x;
            y = gamepad2.y;

            telemetry.addData("Flywheel", "%.2f", flywheel.getPower());
            telemetry.update();
        }
    }
}