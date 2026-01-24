package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Date;

public class ArtifactSystem {
    private Gamepad gamepad;
    private DcMotorEx intakeMotor;
    private DcMotorEx kickerMotor;
    private DcMotorEx flywheelMotor;
    private Telemetry telemetry;

    public boolean isShooting = false;
    private String shootPhase;
    public double shootStartTime;
    private long timeOfNextShootPhase;


    private boolean flywheelRunning = false;
    private boolean leftBumperPressed = false;
    private final double SPEED_DECREASE_PER_MS = 0.1;

    public ArtifactSystem(Gamepad gamepad, DcMotorEx intakeMotor, DcMotorEx kickerMotor, DcMotorEx flywheelMotor, Telemetry telemetry) {
        this.gamepad = gamepad;
        this.intakeMotor = intakeMotor;
        this.kickerMotor = kickerMotor;
        this.flywheelMotor = flywheelMotor;
        this.telemetry = telemetry;

        intakeMotor.setDirection(DcMotorEx.Direction.REVERSE);
        kickerMotor.setDirection(DcMotorEx.Direction.FORWARD);
        flywheelMotor.setDirection(DcMotorEx.Direction.FORWARD);

        intakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        kickerMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        flywheelMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        intakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        kickerMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        flywheelMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    private void intake() {
        intakeMotor.setPower(0.6);
        kickerMotor.setVelocity(-1000);
    }

    private void reverse() {
        intakeMotor.setPower(0.4);
        kickerMotor.setVelocity(1000);
    }

    private void stopIntakeAndKicker() {
        intakeMotor.setPower(0);
        kickerMotor.setVelocity(0);
    }

    private void shoot() {
        intakeMotor.setPower(0.4);
        kickerMotor.setVelocity(1000);
        /*
        if (!isShooting) {
            isShooting = true;
            shootStartTime = timeOfNextShootPhase = new Date().getTime();
            shootPhase = "shoot";
        }
        if (new Date().getTime() >= timeOfNextShootPhase) {
            switch (shootPhase) {
                case "shoot":
                    intakeMotor.setPower(0.4);
                    kickerMotor.setVelocity(1000);

                    timeOfNextShootPhase += 500;
                    shootPhase = "intake";
                    break;
                case "intake":
                    intake();

                    timeOfNextShootPhase += 500;
                    shootPhase = "stop";
                    break;
                case "stop":
                    stopIntakeAndKicker();

                    timeOfNextShootPhase += 250;
                    shootPhase = "shoot";
                    break;
            }
        }
         */
    }

    public void update() {
        if (gamepad.left_trigger > 0.1) {
            shoot();
        } else {
            isShooting = false;
            shootStartTime = Double.POSITIVE_INFINITY;
            if (gamepad.right_trigger > 0.1) {
                intake();
            } else if (gamepad.right_bumper) {
                reverse();
            } else {
                stopIntakeAndKicker();
            }
        }

        if (gamepad.left_bumper && !leftBumperPressed) {
            flywheelRunning = !flywheelRunning;
        }
        leftBumperPressed = gamepad.left_bumper;
        if (flywheelRunning) {
            double baseSpeed = 1330;
            double shootTimeElapsed = new Date().getTime() - shootStartTime;
            double speedDecrease = 0; // Math.max(shootTimeElapsed * SPEED_DECREASE_PER_MS, 0);

            flywheelMotor.setVelocity(baseSpeed - speedDecrease);
            telemetry.addData("Nominal flywheel speed", Math.round(baseSpeed - speedDecrease));
            telemetry.addData("Actual flywheel speed", flywheelMotor.getVelocity());
            if (flywheelMotor.getVelocity() >= baseSpeed) {
                telemetry.addLine("           |");
                telemetry.addLine("   _      |");
                telemetry.addLine(" /    \\    |_/");
                telemetry.addLine("|      |   |  \\");
                telemetry.addLine(" \\ _ /    |   \\");
            }
        } else {
            flywheelMotor.setVelocity(0);
            telemetry.addLine("Flywheel off");
        }
    }
}