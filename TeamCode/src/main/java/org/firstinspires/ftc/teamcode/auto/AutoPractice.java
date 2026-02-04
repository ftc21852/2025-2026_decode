package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.components.Flywheel;
import org.firstinspires.ftc.teamcode.components.Transfer;
import org.firstinspires.ftc.teamcode.components.util.Sequence;

@Autonomous
public class AutoPractice extends OpMode {
    private Follower follower;
    private Timer opModeTimer;
    private Sequence sequence;
    Transfer transfer;
    Flywheel flywheel;
    Auto auto;

    public void start() {
        auto.begin();
    }

    @Override
    public void loop() {
        follower.update();
        auto.update();
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        DcMotorEx flywheelTop = hardwareMap.get(DcMotorEx.class, "flywheel-top");
        DcMotorEx flywheelBottom = hardwareMap.get(DcMotorEx.class, "flywheel-bottom");

        transfer = new Transfer(null, intakeMotor, null, telemetry);
        Flywheel flywheel = new Flywheel(gamepad1, flywheelTop, flywheelBottom, null, null, telemetry);

        auto = new Auto(follower);

        auto.startAt(24, 123).facing(9_00); // must have this line

        auto.goTo(60, 72); // inches
        auto.goTo(2.5, 3.0); // mats

        auto.turnTo(6); // 6 o'clock aka south
        auto.turnTo(6_00); // more precise clock direction
        auto.turnTo(Math.toRadians(-90)); // -90° counterclockwise of east aka south

        // move while facing a point
        auto.goTo(48, 48).facing(0, 144);
        auto.goTo(2.0, 2.0).facing(0.0, 6.0);

        // move while facing a direction (you can mix and match units lol)
        auto.goTo(48, 48).facing(6_00);
        auto.goTo(48, 48).facing(Math.toRadians(-90));
        auto.goTo(2.0, 2.0).facing(6_00);
        auto.goTo(2.0, 2.0).facing(Math.toRadians(-90));

        auto.run(transfer::intake);
        auto.run(transfer::shoot);
        auto.run(transfer::reverse);

        auto.run(flywheel, 1500);
        auto.waitUntil(() -> flywheel.speedIsOffByLessThan(5));
        auto.wait(100);

        auto.stop(transfer);
        auto.stop(flywheel);
    }
}
