package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.Camera;
import org.firstinspires.ftc.teamcode.components.Flywheel;
import org.firstinspires.ftc.teamcode.components.Transfer;

@Autonomous (name = "Auto Practice")
public class AutoPractice extends OpMode {
    private Follower follower;
    private Timer opModeTimer;
    Transfer transfer;
    Flywheel flywheel;
    private Auto auto;

    public void start() {
        auto.begin();
        opModeTimer.resetTimer();
    }

    @Override
    public void loop() {
        follower.update();
        auto.update();
        telemetry.addData("flywheel nominal speed", flywheel.getNominalSpeed());
        telemetry.addData("flywheel actual speed", flywheel.getActualSpeed());
        telemetry.setMsTransmissionInterval(20);
        telemetry.update();
    }

    @Override
    public void init() {
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        Servo led = hardwareMap.get(Servo.class, "led");

        DcMotorEx flywheelTop = hardwareMap.get(DcMotorEx.class, "flywheel-top");
        DcMotorEx flywheelBottom = hardwareMap.get(DcMotorEx.class, "flywheel-bottom");
        Servo gateServo = hardwareMap.get(Servo.class, "gate");
        Servo hoodServo = hardwareMap.get(Servo.class, "hood");

        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");

        Camera camera = new Camera(limelight, telemetry);

        transfer = new Transfer(null, intakeMotor, gateServo, telemetry);
        flywheel = new Flywheel(null, flywheelTop, flywheelBottom, hoodServo, led, camera, telemetry);

        auto = new Auto(follower);

        auto
                .startAt(5.1, 5.0) .facing(3)

                .run(flywheel, 1070)
                .run(() -> flywheel.setHoodAngle(30))
                .wait(2.0)

                .goTo(4.6, 4.5) .facing(1_30)
                .run(transfer::openGate)
                .run(transfer::forward)
                .wait(2.0)
                .stop(transfer)
                .run(transfer::closeGate)

                .goTo(4.0, 3.5) .facing(3)
                .run(transfer::forward)
                .goTo(5.1, 3.5) .facing(3)
                .wait(0.5)
                .stop(transfer)

                .goTo(4.6, 4.5) .facing(1_30)
                .run(transfer::openGate)
                .wait(0.5)
                .run(transfer::forward)
                .wait(2.0)
                .stop(transfer)
                .run(transfer::closeGate)

                .goTo(4.0, 2.5) .facing(3)
                .run(transfer::forward)
                .goTo(5.3, 2.5) .facing(3)
                .goTo(4.8, 2.5) .facing(3)
                .stop(transfer)

                .goTo(4.6, 4.5) .facing(1_30)
                .run(transfer::openGate)
                .wait(0.5)
                .run(transfer::forward)
                .wait(2.0)
                .stop(transfer)
                .run(transfer::closeGate)

                .goTo(4.0, 1.5) .facing(3)
                .run(transfer::forward)
                .goTo(5.3, 1.5) .facing(3)
                .goTo(4.8, 1.5) .facing(3)
                .stop(transfer)

                .goTo(4.6, 4.6) .facing(1_30)
                .run(transfer::openGate)
                .wait(0.5)
                .run(transfer::forward)
                .wait(2.0)
                .stop(transfer)
                .run(transfer::closeGate)

                .stop();
    }
}