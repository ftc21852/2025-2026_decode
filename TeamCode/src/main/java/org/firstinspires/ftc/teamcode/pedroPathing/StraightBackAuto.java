package org.firstinspires.ftc.teamcode.pedroPathing;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class StraightBackAuto extends OpMode {
    private Follower follower;
    private Timer pathTimer, opModeTimer;

    public enum PathState {
        //START POSITION - END POSITION
        //DRIVE > MOVEMENT STATE
        //SHOOT > ATTEMPT TO SCORE AN ARTIFACT
        DRIVE_STARTPOS_SHOOTPOS,
        SHOOT_PRELOAD
    }

    PathState pathState;

    private final Pose startPose = new Pose(20.392546583850923,122.35527950310559, Math.toRadians(138));
    private final Pose shootPose = new Pose(59.39875776397515, 83.58260869565218, Math.toRadians(138));
    private PathChain driveStartPosShootPos;

    public void buildPaths() {
        //put in coordinates for starting pos > ending pos
        driveStartPosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
    }


    public void statePathUpdate() {
        switch(pathState) {
            case DRIVE_STARTPOS_SHOOTPOS:
                follower.followPath(driveStartPosShootPos,true);
                pathState = PathState.SHOOT_PRELOAD;
                break;

            case SHOOT_PRELOAD:
                //TODO ADD LOGIC TO THE FLYWHEEL SHOOTER
                // check is follower done its path



        }
    }

    @Override
    public void init(){

    }

    @Override
    public void loop() {

        }
}
