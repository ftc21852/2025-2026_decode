package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.components.util.Matrix;

import java.util.function.Function;
import java.util.Date;

public class Flywheel {

    // ------------------------------------------------------------ //
    //            !! PLEASE ENTER DATA BEFORE RUNNING !!            //
    // Input distance-power pairs obtained through flywheel tuning. //
    // ------------------------------------------------------------ //

    private final double[] x = {}; // distance
    private final double[] v = { 1218, 1330, 1500 }; // speed

    private Gamepad gamepad;
    private DcMotorEx flywheelTop;
    private DcMotorEx flywheelBottom;
    private Servo hoodServo;
    private Servo led;
    private Telemetry telemetry;

    private boolean flywheelRunning = false;
    private double flywheelSpeed;

    private int hoodOffsetTime;
    private int hoodDirection;
    private long lastPollTime;

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

    public Flywheel(Gamepad gamepad, DcMotorEx flywheelTop, DcMotorEx flywheelBottom, Servo hoodServo, Servo led, Telemetry telemetry) {
        this.gamepad = gamepad;
        this.flywheelTop = flywheelTop;
        this.flywheelBottom = flywheelBottom;
        this.hoodServo = hoodServo;
        this.led = led;
        this.telemetry = telemetry;

        flywheelTop.setDirection(DcMotorEx.Direction.FORWARD);
        flywheelTop.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        flywheelTop.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        flywheelBottom.setDirection(DcMotorEx.Direction.FORWARD);
        flywheelBottom.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        flywheelBottom.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        hoodServo.setDirection(Servo.Direction.REVERSE);
        hoodOffsetTime = 0;
        hoodDirection = 0;

        flywheelSpeed = 1260;
    }

    public void printOk() {
        telemetry.addLine("            |");
        telemetry.addLine("   _       |");
        telemetry.addLine(" /    \\    |_/");
        telemetry.addLine("|       |   |  \\");
        telemetry.addLine(" \\ _ /    |    \\");
    }

    public void setSpeed(double speed) {
        flywheelSpeed = (int) speed;
        flywheelTop.setVelocity(speed);
        flywheelBottom.setVelocity(speed);
    }

    public double getNominalSpeed() {
        return flywheelSpeed;
    }

    public double getActualSpeed() {
        return (flywheelTop.getVelocity() + flywheelBottom.getVelocity()) / 2;
    }

    public boolean speedIsOffByLessThan(double error) {
        return Math.abs(this.getActualSpeed() - flywheelSpeed) < error;
    }

    public void update() {
        if (gamepad.leftBumperWasPressed()) {
            flywheelRunning = !flywheelRunning;
        }
        if (flywheelRunning) {
            /*
            double distance = camera.getDistance();
            double flywheelSpeed = distanceToSpeed.apply(distance);
            telemetry.addData("Distance from goal", distance);
            telemetry.addData("Flywheel nominal speed", flywheelSpeed);
            /*/

            setSpeed(1260);
            telemetry.addData("Flywheel nominal speed", flywheelSpeed);
            telemetry.addData("Flywheel speed", this.getActualSpeed());
            if (speedIsOffByLessThan(5)) {
                printOk();
            }
        } else {
            this.setSpeed(0);
        }

        if (gamepad.dpadUpWasPressed()) {
            hoodDirection = 1;
        } else if (gamepad.dpadDownWasPressed()) {
            hoodDirection = -1;
        }
        if (gamepad.dpadUpWasReleased()) {
            hoodDirection = gamepad.dpad_down ? -1 : 0;
        }
        if (gamepad.dpadDownWasReleased()) {
            hoodDirection = gamepad.dpad_up ? 1 : 0;
        }
        long currentTime = new Date().getTime();
        hoodOffsetTime += (int) (currentTime - lastPollTime) * hoodDirection;
        if (hoodOffsetTime > 1000) {
            hoodOffsetTime = 1000;
        } else if (hoodOffsetTime < 0) {
            hoodOffsetTime = 0;
        }
        hoodServo.setPosition(hoodOffsetTime * 0.7 / 1000);
        led.setPosition(0.5);
        lastPollTime = currentTime;

        // telemetry.addData("led position", led.getPosition());
        telemetry.addData("hood position", hoodServo.getPosition());
    }
}