package org.firstinspires.ftc.teamcode.pedroPathing;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous
public class BlueAuto extends OpMode {
    private Follower follower;
    private Timer pathTimer, opModeTimer;

    public enum PathState {
        //START POSITION - END POSITION
        //DRIVE > MOVEMENT STATE
        //SHOOT > ATTEMPT TO SCORE AN ARTIFACT
        DRIVE_STARTPOS_SHOOTPOS,
        SHOOT_PRELOAD,
        DRIVE_SHOOTPOS_FIRSTINTAKEPOS,
        DRIVE_FIRSTINTAKEPOS_SHOOTPOS,
        DRIVE_SHOOTPOS_SECONDINTAKESTARTPOS,
        DRIVE_SECONDINTAKESTARTPOS_SECONDINTAKEENDPOS,
        DRIVE_SECONDINTAKEENDPOS_SHOOTPOS,
        DRIVE_SHOOTPOS_THIRDINTAKESTARTPOS,
        DRIVE_THIRDINTAKESTARTPOS_THIRDINTAKEENDPOS,
        DRIVE_THIRDINTAKEENDPOS_SHOOTPOS
    }

    PathState pathState;

    private final Pose startPose = new Pose(20.392546583850923,122.35527950310559, Math.toRadians(135));
    private final Pose shootPose = new Pose(59.39875776397515, 83.58260869565218, Math.toRadians(135));
    private final Pose intakeFirstLinePose = new Pose(11.40745341614907, 83.56149068322983, Math.toRadians(180));
    private final Pose intakeSecondLineStartPose = new Pose(60.165217391304346, 60.26832298136644, Math.toRadians(180));
    private final Pose intakeSecondLineEndPose = new Pose(6.913043478260868, 59.83478260869566, Math.toRadians(180));
    private final Pose intakeThirdLineStartPose = new Pose(59.89937888198758, 35.96645962732919, Math.toRadians(180));
    private final Pose intakeThirdLineEndPose = new Pose(7.0236024844720495, 34.94285714285715, Math.toRadians(180));

    private PathChain driveStartPosShootPos, driveShootPosFirstIntakePos, driveFirstIntakePosShootPos,
            driveShootPosSecondIntakeStartPos, driveSecondIntakeStartPosSecondIntakeEndPos,
            driveSecondIntakeEndPosShootPos, driveShootPosThirdIntakeStartPos,
            driveThirdIntakeStartPosThirdIntakeEndPos, driveThirdIntakeEndPosShootPos
            ;
    private DcMotorEx intakeMotor;
    private DcMotorEx kickerMotor;
    private DcMotorEx flywheelMotor;

    public void buildPaths() {
        //put in coordinates for starting pos > ending pos
        driveStartPosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
        driveShootPosFirstIntakePos = follower.pathBuilder()
                .addPath(new BezierLine(shootPose,intakeFirstLinePose))
                .setLinearHeadingInterpolation(shootPose.getHeading(),intakeFirstLinePose.getHeading())
                .build();
        driveFirstIntakePosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(intakeFirstLinePose, shootPose))
                .setLinearHeadingInterpolation(intakeFirstLinePose.getHeading(), shootPose.getHeading())
                .build();
        driveShootPosSecondIntakeStartPos = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, intakeSecondLineStartPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), intakeSecondLineStartPose.getHeading())
                .build();
        driveSecondIntakeStartPosSecondIntakeEndPos = follower.pathBuilder()
                .addPath(new BezierLine(intakeSecondLineStartPose, intakeSecondLineEndPose))
                .setLinearHeadingInterpolation(intakeSecondLineStartPose.getHeading(), intakeSecondLineEndPose.getHeading())
                .build();
        driveSecondIntakeEndPosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(intakeSecondLineEndPose, shootPose))
                .setLinearHeadingInterpolation(intakeSecondLineEndPose.getHeading(), shootPose.getHeading())
                .build();
        driveShootPosThirdIntakeStartPos = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, intakeThirdLineStartPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), intakeThirdLineStartPose.getHeading())
                .build();
        driveThirdIntakeStartPosThirdIntakeEndPos = follower.pathBuilder()
                .addPath(new BezierLine(intakeThirdLineStartPose, intakeThirdLineEndPose))
                .setLinearHeadingInterpolation(intakeThirdLineStartPose.getHeading(), intakeThirdLineEndPose.getHeading())
                .build();
        driveThirdIntakeEndPosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(intakeThirdLineEndPose, shootPose))
                .setLinearHeadingInterpolation(intakeThirdLineEndPose.getHeading(), shootPose.getHeading())
                .build();

    }


    public void statePathUpdate() {
        switch(pathState) {
            case DRIVE_STARTPOS_SHOOTPOS:
                follower.followPath(driveStartPosShootPos,false);
                setPathState(PathState.DRIVE_SHOOTPOS_FIRSTINTAKEPOS);
                break;
            case DRIVE_SHOOTPOS_FIRSTINTAKEPOS:
                follower.followPath(driveShootPosFirstIntakePos,false);
                setPathState(PathState.DRIVE_FIRSTINTAKEPOS_SHOOTPOS);
                break;
            case DRIVE_FIRSTINTAKEPOS_SHOOTPOS:
                follower.followPath(driveFirstIntakePosShootPos, false);
                setPathState(PathState.DRIVE_SHOOTPOS_SECONDINTAKESTARTPOS);
                break;


            case SHOOT_PRELOAD:
                // check is follower done its path
                if(!follower.isBusy()) {
                    //TODO ADD LOGIC TO THE FLYWHEEL SHOOTER
                    telemetry.addLine("Done Path 1");
                }
                break;
            default:
                telemetry.addLine("No State Commanded");
                break;
        }
    }/*
    driveStartPosShootPos, driveShootPosFirstIntakePos, driveFirstIntakePosShootPos,
            driveShootPosSecondIntakeStartPos, driveSecondIntakeStartPosSecondIntakeEndPos,
            driveSecondIntakeEndPosShootPos, driveShootPosThirdIntakeStartPos,
            driveThirdIntakeStartPosThirdIntakeEndPos, driveThirdIntakeEndPosShootPos
    */


    public void setPathState(PathState newState) {
        pathState = newState;
        pathTimer.resetTimer();

    }

    @Override
    public void init(){
        pathState = PathState.DRIVE_STARTPOS_SHOOTPOS;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        opModeTimer.resetTimer();
        follower = Constants.createFollower(hardwareMap);
        //TODO add in any other init mechanisms
        kickerMotor = hardwareMap.get(DcMotorEx.class, "kicker");
        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheel");
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");

        intakeMotor.setDirection(DcMotorEx.Direction.REVERSE);
        kickerMotor.setDirection(DcMotorEx.Direction.FORWARD);
        flywheelMotor.setDirection(DcMotorEx.Direction.FORWARD);

        intakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        kickerMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        flywheelMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        intakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        kickerMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        flywheelMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        buildPaths();
        follower.setPose(startPose);

    }

    public void start() {
        opModeTimer.resetTimer();
        setPathState(pathState);
    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();

        telemetry.addData("path state: ", pathState.toString());
        telemetry.addData("x: ", follower.getPose().getX());
        telemetry.addData("y: ", follower.getPose().getY());
        telemetry.addData("heading: ", follower.getPose().getHeading());
        telemetry.addData("path time: ", pathTimer.getElapsedTimeSeconds());

        }
}
