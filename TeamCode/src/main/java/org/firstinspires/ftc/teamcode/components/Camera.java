package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.components.util.Matrix;

import java.util.List;

public class Camera {
    private Limelight3A limelight;

    public Camera(Limelight3A limelight) {
        this.limelight = limelight;
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0);
        limelight.start();
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
        Pose3D pose = tags.get(0).getTargetPoseCameraSpace();

        Position pos = pose.getPosition();
        double pitch = pose.getOrientation().getPitch(AngleUnit.RADIANS);
        Matrix pitchMatrix = new Matrix(new double[][]{
                {Math.cos(pitch), Math.sin(pitch)},
                {-Math.sin(pitch), Math.cos(pitch)},
        });
        Matrix ZY = pitchMatrix.inverse().times(Matrix.fromVector(new double[]{pos.z, pos.y}));
        return Math.hypot(pos.x, ZY.get(0, 0));
    }
}
