package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.components.util.Matrix;

import java.util.ArrayList;
import java.util.function.Function;

public class Flywheel {

    // ------------------------------------------------------------ //
    //            !! PLEASE ENTER DATA BEFORE RUNNING !!            //
    // Input distance-power pairs obtained through flywheel tuning. //
    // ------------------------------------------------------------ //

    private final double[] x = {}; // distance
    private final double[] v = { 1218, 1330, 1500 }; // power

    private Gamepad gamepad;
    private DcMotorEx flywheel;
    private Telemetry telemetry;

    private boolean flywheelRunning = false;
    private boolean leftBumperPressed = false;
    private int flywheelSpeed;

    // private final Function<Double, Double> distanceToSpeed = getSpeedFunction(x, v);

    private static Function<Double, Double> getSpeedFunction(double[] x, double[] v) {

        double[][] a = new double[x.length][2];
        for (int i = 0; i < x.length; i++) {
            a[i][0] = 1 / x[i];
            a[i][1] = 1 / (x[i] * x[i]);
        }
        Matrix A = new Matrix(a);

        double[][] b = new double[v.length][1];
        for (int i = 0; i < v.length; i++) {
            b[i][0] = 1 / (v[i] * v[i]);
        }
        Matrix B = new Matrix(b);

        Matrix solution = A.transposed().times(A).inverse().times(A.transposed()).times(B);
        double c = solution.get(0, 0);
        double d = solution.get(1, 0);
        return distance -> Math.sqrt(1 / (c / distance + d / (distance * distance)));
    }

    public Flywheel(Gamepad gamepad, DcMotorEx flywheel, Telemetry telemetry) {
        this.gamepad = gamepad;
        this.flywheel = flywheel;
        this.telemetry = telemetry;

        flywheel.setDirection(DcMotorEx.Direction.FORWARD);

        flywheel.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        flywheel.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void printOk() {
        telemetry.addLine("            |");
        telemetry.addLine("   _       |");
        telemetry.addLine(" /    \\    |_/");
        telemetry.addLine("|       |   |  \\");
        telemetry.addLine(" \\ _ /    |    \\");
    }

    public void setVelocity(double speed) {
        flywheelSpeed = (int) speed;
        flywheel.setVelocity(speed);
    }

    public boolean upToSpeed() {
        return Math.abs(flywheel.getVelocity() - flywheelSpeed) <= 10;
    }

    public void update() {
        if (gamepad.x) {
            flywheelSpeed = 1210;
        } else if (gamepad.y) {
            flywheelSpeed = 1330;
        } else if (gamepad.b) {
            flywheelSpeed = 1500;
        }
        if (gamepad.left_bumper && !leftBumperPressed) {
            flywheelRunning = !flywheelRunning;
        }
        leftBumperPressed = gamepad.left_bumper;
        if (flywheelRunning) {
            /*
            double distance = camera.getDistance();
            double flywheelSpeed = distanceToSpeed.apply(distance);
            telemetry.addData("Distance from goal", distance);
            telemetry.addData("Flywheel nominal speed", flywheelSpeed);
            /*/
            switch (flywheelSpeed) {
                case 1210:
                    telemetry.addData("Speed setting", "Close");
                    break;
                case 1330:
                    telemetry.addData("Speed setting", "Medium");
                    break;
                case 1500:
                    telemetry.addData("Speed setting", "Far");
                    break;
            }
            //*/

            flywheel.setVelocity(flywheelSpeed);

            if (upToSpeed()) {
                printOk();
            }
        } else {
            flywheel.setVelocity(0);
        }
    }
}