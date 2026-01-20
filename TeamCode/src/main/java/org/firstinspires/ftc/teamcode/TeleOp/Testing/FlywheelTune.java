package org.firstinspires.ftc.teamcode.TeleOp.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.TeleOp.Chassis;
import org.firstinspires.ftc.teamcode.TeleOp.Transfer;
import org.firstinspires.ftc.teamcode.TeleOp.FlywheelUtil.BinarySearch;

@TeleOp(name = "Flywheel Tuning")
public class FlywheelTune extends LinearOpMode {
    public double getDistance() {
        return 0;
        /*
        final int FIELD_WIDTH = 48500;
        final int FIELD_HEIGHT = 48500;

        DcMotorEx leftOdo = hardwareMap.get(DcMotorEx.class, "leftOdo"); // x
        DcMotorEx rightOdo = hardwareMap.get(DcMotorEx.class, "rightOdo"); // y
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
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "fl");
        DcMotorEx backLeft  = hardwareMap.get(DcMotorEx.class, "bl");
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "fr");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "br");

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        DcMotorEx kickerMotor = hardwareMap.get(DcMotorEx.class, "kicker");
        DcMotorEx flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");

        flywheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        Chassis chassis = new Chassis(gamepad1, frontLeft, backLeft, frontRight, backRight, telemetry);
        Transfer transfer = new Transfer(gamepad1, intakeMotor, kickerMotor, telemetry);

        // Motor directions
        flywheel.setDirection(DcMotorEx.Direction.FORWARD);

        // Zero power behavior
        flywheel.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

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
                    if (gamepad1.dpad_up && !dpad_up) {
                        System.out.println("Launched too far at speed " + start.getMid());
                        start.goLow();
                        flywheel.setVelocity(start.getMid());
                    }
                    if (gamepad1.dpad_down && !dpad_down) {
                        System.out.println("Launched too close at speed " + start.getMid());
                        start.goHigh();
                        flywheel.setVelocity(start.getMid());
                    }
                    if (gamepad1.dpad_right && !dpad_right) {
                        System.out.println("Scored at speed " + start.getMid());
                        low = new BinarySearch(start.getLow(), start.getMid());
                        tuneMode = "find lower bound";
                        flywheel.setVelocity(low.getMid());
                        System.out.println("Finding lower bound");
                    }
                    if (gamepad1.x && !x) {
                        tuneMode = "";
                        start = null;
                        low = null;
                        high = null;
                        flywheel.setVelocity(0);
                    }
                    break;
                case "find lower bound":
                    telemetry.addData("Lower bound", "%.3f", low.getHigh());
                    if (gamepad1.dpad_down && !dpad_down) {
                        System.out.println("Launched too close at speed " + low.getMid());
                        low.goHigh();
                        flywheel.setVelocity(low.getMid());
                    }
                    if (gamepad1.dpad_right && !dpad_right) {
                        System.out.println("Scored at speed " + low.getMid());
                        low.goLow();
                        flywheel.setVelocity(low.getMid());
                    }
                    if (gamepad1.a && !a) {
                        low = new BinarySearch(start.getLow(), start.getMid());
                        flywheel.setVelocity(low.getMid());
                        System.out.println("Finding lower bound");
                    }
                    if (gamepad1.y && !y) {
                        high = new BinarySearch(start.getMid(), start.getHigh());
                        tuneMode = "find upper bound";
                        flywheel.setVelocity(high.getMid());
                        System.out.println("Finding upper bound");
                    }
                    if (gamepad1.b && !b) {
                        if (high == null) {
                            System.out.println("Cannot produce results: test upper bound first");
                        } else {
                            System.out.println("The best speed at distance " + getDistance() + " is " + (low.getHigh() + high.getLow()) / 2);
                        }
                    }
                    if (gamepad1.x && !x) {
                        if (high != null) {
                            System.out.println("The best speed at distance " + getDistance() + " is " + (low.getHigh() + high.getLow()) / 2);
                        }
                        tuneMode = "";
                        start = null;
                        low = null;
                        high = null;
                        flywheel.setVelocity(0);
                        System.out.println("Ending test");
                    }
                    break;
                case "find upper bound":
                    telemetry.addData("Upper bound", "%.3f", high.getLow());
                    if (gamepad1.dpad_up && !dpad_up) {
                        System.out.println("Launched too far at speed " + high.getMid());
                        high.goLow();
                        flywheel.setVelocity(high.getMid());
                    }
                    if (gamepad1.dpad_right && !dpad_right) {
                        System.out.println("Scored at speed " + high.getMid());
                        high.goHigh();
                        flywheel.setVelocity(high.getMid());
                    }
                    if (gamepad1.a && !a) {
                        low = new BinarySearch(start.getLow(), start.getMid());
                        tuneMode = "find lower bound";
                        flywheel.setVelocity(low.getMid());
                        System.out.println("Finding lower bound");
                    }
                    if (gamepad1.y && !y) {
                        high = new BinarySearch(start.getMid(), start.getHigh());
                        flywheel.setVelocity(high.getMid());
                        System.out.println("Finding upper bound");
                    }
                    if (gamepad1.b && !b) {
                        System.out.println("The best speed at distance " + getDistance() + " is " + (low.getHigh() + high.getLow()) / 2);
                    }
                    if (gamepad1.x && !x) {
                        System.out.println("The best speed at distance " + getDistance() + " is " + (low.getHigh() + high.getLow()) / 2);
                        tuneMode = "";
                        start = null;
                        low = null;
                        high = null;
                        flywheel.setVelocity(0);
                        System.out.println("Ending test");
                    }
                    break;
                default:
                    chassis.update();

                    if (gamepad1.x && !x) {
                        chassis.stop();

                        System.out.println("Starting tuning at distance " + getDistance());
                        tuneMode = "first goal";
                        start = new BinarySearch(0, 3000);
                        flywheel.setVelocity(start.getMid());
                    }
            }
            
            transfer.update();

            dpad_up = gamepad1.dpad_up;
            dpad_down = gamepad1.dpad_down;
            dpad_right = gamepad1.dpad_right;
            a = gamepad1.a;
            b = gamepad1.b;
            x = gamepad1.x;
            y = gamepad1.y;

            telemetry.addData("Flywheel", "%.2f", flywheel.getVelocity());
            telemetry.update();
        }
    }
}