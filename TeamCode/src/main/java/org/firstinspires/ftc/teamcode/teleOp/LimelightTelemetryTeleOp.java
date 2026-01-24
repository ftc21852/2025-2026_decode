package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Limelight Telemetry (One File)", group = "Test")
public class LimelightTelemetryTeleOp extends LinearOpMode {

    private Limelight3A limelight;

    @Override
    public void runOpMode() {

        // Robot Config 里设备名必须叫 "limelight"
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0); // 改成你的 AprilTag pipeline
        limelight.start();

        telemetry.addLine("Limelight ready");
        telemetry.addLine("Press START");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            LLResult result = limelight.getLatestResult();

            boolean hasTarget = result != null && result.isValid();

            telemetry.addData("Has Target", hasTarget);

            if (hasTarget && !result.getFiducialResults().isEmpty()) {

                telemetry.addData("Tx (deg)", result.getTx());
                telemetry.addData("Ty (deg)", result.getTy());
                telemetry.addData("Ta (%)", result.getTa());

                telemetry.addData("Tag ID",
                        result.getFiducialResults().get(0).getFiducialId());

                telemetry.addData("X",
                        result.getFiducialResults().get(0)
                                .getTargetPoseCameraSpace().getPosition().x);

                telemetry.addData("Y",
                        result.getFiducialResults().get(0)
                                .getTargetPoseCameraSpace().getPosition().y);

                telemetry.addData("Z",
                        result.getFiducialResults().get(0)
                                .getTargetPoseCameraSpace().getPosition().z);

                telemetry.addData("Pitch (deg)",
                        result.getFiducialResults().get(0)
                                .getTargetPoseCameraSpace()
                                .getOrientation()
                                .getPitch(AngleUnit.DEGREES));

            } else {
                telemetry.addLine("No AprilTag detected");
            }

            telemetry.update();
        }
    }
}