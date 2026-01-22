package org.firstinspires.ftc.teamcode.TeleOp;

import java.util.List;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class Camera {
    private Limelight3A limelight;

    public Camera(Limelight3A limelight) {
        this.limelight = limelight;
        limelight.pipelineSwitch(0);
    }

    public double getDistance() {
        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid()) {
            return Double.POSITIVE_INFINITY;
        }
        List<FiducialResult> tags = result.getFiducialResults();
        if (tags == null || tags.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }
        FiducialResult tag = tags.get(0);

        Position pos = tag.getTargetPoseCameraSpace().getPosition();
        return Math.hypot(pos.x, pos.y);
    }
}
