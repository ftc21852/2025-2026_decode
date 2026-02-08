package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.components.util.Matrix;

import java.util.List;

public class Camera {
    private Limelight3A limelight;
    private Telemetry telemetry;

    public Camera(Limelight3A limelight, Telemetry telemetry) {
        this.limelight = limelight;
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0);
        limelight.start();

        this.telemetry = telemetry;
    }

    public double getGroundDistance() {
        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid()) {
            return Double.POSITIVE_INFINITY;
        }
        List<FiducialResult> tags = result.getFiducialResults();
        if (tags.isEmpty()) {
            return Double.NEGATIVE_INFINITY;
        }
        Pose3D pose = tags.get(0).getCameraPoseTargetSpace();

        Position pos = pose.getPosition();
        return Math.hypot(pos.x, pos.z);
    }

    public void update() {
    }
}
