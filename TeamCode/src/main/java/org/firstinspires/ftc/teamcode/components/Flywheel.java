package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.components.util.Matrix;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.Date;

public class Flywheel {
    private class ShotData {
        public double distance;
        public double angle;
        public double speed;

        public ShotData(double distance, double angle, double speed) {
            this.distance = distance;
            this.angle = angle;
            this.speed = speed;
        }

        public String toString() {
            return String.format("%.2f m | %.1f° | %d RPM", distance, angle, (int) speed);
        }
    }

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
    private Camera camera;
    private Telemetry telemetry;

    private boolean flywheelRunning = false;
    private double flywheelSpeed;

    private int hoodOffsetTime = 0;
    private int hoodDirection = 0;
    private long lastPollTime;

    private boolean override = false;

    private ArrayList<ShotData> scores;

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

    public Flywheel(Gamepad gamepad, DcMotorEx flywheelTop, DcMotorEx flywheelBottom, Servo hoodServo, Servo led, Camera camera, Telemetry telemetry) {
        this.gamepad = gamepad;
        this.flywheelTop = flywheelTop;
        this.flywheelBottom = flywheelBottom;
        this.hoodServo = hoodServo;
        this.led = led;
        this.camera = camera;
        this.telemetry = telemetry;

        flywheelTop.setDirection(DcMotorEx.Direction.FORWARD);
        flywheelTop.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        flywheelTop.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        flywheelBottom.setDirection(DcMotorEx.Direction.FORWARD);
        flywheelBottom.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        flywheelBottom.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        hoodServo.setDirection(Servo.Direction.REVERSE);

        flywheelSpeed = 1260; // 1260

        scores = new ArrayList<ShotData>();
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

    public void setHoodAngle(double angle) {
        hoodServo.setPosition((angle - 30) * (300 / 27.0) / 300);
    }

    public void override() {
        override = true;
    }

    public void stopOverride() {
        override = false;
    }

    public void update() {
        if (override) {
            return;
        }
        if (gamepad.aWasPressed()) {
            flywheelRunning = !flywheelRunning;
        }
        if (flywheelRunning) {
            /*
            double distance = camera.getDistance();
            double flywheelSpeed = distanceToSpeed.apply(distance);
            telemetry.addData("Distance from goal", distance);
            telemetry.addData("Flywheel nominal speed", flywheelSpeed);
            /*/
            // 1240
            setSpeed(1040);
            telemetry.addData("Flywheel nominal speed", flywheelSpeed);
            telemetry.addData("Flywheel speed", this.getActualSpeed());
            if (flywheelSpeed != 0 && speedIsOffByLessThan(10)) {
                led.setPosition(0.388);
            } else {
                led.setPosition(0);
            }
        } else {
            setSpeed(0);
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
        lastPollTime = currentTime;

        double hoodAngle = hoodServo.getPosition() * 300 * (27.0 / 300) + 30;

        telemetry.addData("hood angle", hoodAngle);

        if (gamepad.yWasPressed()) {
            scores.add(new ShotData(camera.getGroundDistance(), hoodAngle, getActualSpeed()));
        }

        for (ShotData data: scores) {
            telemetry.addLine(data.toString());
        }
    }
}