package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.TeleOp.FlywheelUtil.Matrix;

import java.util.function.Function;

public class Flywheel {

    // ------------------------------------------------------------ //
    //            !! PLEASE ENTER DATA BEFORE RUNNING !!            //
    // Input distance-power pairs obtained through flywheel tuning. //
    // ------------------------------------------------------------ //

    private final double[] x = {}; // distance
    private final double[] p = {}; // power

    private Gamepad gamepad;
    private DcMotor flywheel;
    private Telemetry telemetry;

    // private final Function<Double, Double> distanceToPower = getPowerFunction(x, p);

    Function<Double, Double> getPowerFunction(double[] x, double[] p) {
        double[][] a = new double[x.length][2];
        for (int i = 0; i < x.length; i++) {
            a[i][0] = 1 / x[i];
            a[i][1] = 1 / (x[i] * x[i]);
        }
        Matrix A = new Matrix(a);

        double[][] b = new double[p.length][1];
        for (int i = 0; i < p.length; i++) {
            b[i][0] = 1 / (p[i] * p[i]);
        }
        Matrix B = new Matrix(b);

        Matrix solution = A.t().times(A).inverse().times(A.t()).times(B);
        double c = solution.get(0, 0);
        double d = solution.get(1, 0);
        return distance -> Math.sqrt(1 / (c / distance + d / (distance * distance)));
    }

    /*
    public double getDistance() {
        return 0;
        final int FIELD_WIDTH = 48500;
        final int FIELD_HEIGHT = 48500;

        DcMotor leftOdo = hardwareMap.get(DcMotor.class, "leftOdo"); // x
        DcMotor rightOdo = hardwareMap.get(DcMotor.class, "rightOdo"); // y
        int leftTicks = leftOdo.getCurrentPosition();
        int rightTicks = rightOdo.getCurrentPosition();

        double offsetX = FIELD_WIDTH - leftTicks; // since we're testing using the red goal
        double offsetY = FIELD_HEIGHT - rightTicks;
        return Math.hypot(offsetX, offsetY);
    }
    */

    public Flywheel(Gamepad gamepad, DcMotor flywheel, Telemetry telemetry) {
        this.gamepad = gamepad;
        this.flywheel = flywheel;
        this.telemetry = telemetry;

        flywheel.setDirection(DcMotor.Direction.FORWARD);

        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void update() {

        if (gamepad.a) {
            /*
            double flywheelPower = distanceToPower.apply(getDistance());
            flywheel.setPower(flywheelPower);
            telemetry.addData("Flywheel", "%.2f", flywheelPower);
            */
            flywheel.setPower(0.4);
            telemetry.addData("Flywheel", "On");
        } else if (gamepad.b) {
            flywheel.setPower(0);
            telemetry.addData("Flywheel", "Off");
        }
    }
}