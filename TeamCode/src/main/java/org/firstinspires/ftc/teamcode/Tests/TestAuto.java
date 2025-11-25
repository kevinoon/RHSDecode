package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Mechanics.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.teamcode.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name= "Auto Test")
public class TestAuto extends LinearOpMode {
    public int mode = defaultMode(); // 1 is default
    
    private Hardware hw;
    
    private AprilTagWebcam aprilTagWebcam;

    @Override
    public void runOpMode() {
        Hardware hw = Hardware.getInstance(this);
        hw.init(hardwareMap);
        hw.setToNoEncoder();
        
        
        // Initialize webcam here (inside a method)
        aprilTagWebcam = new AprilTagWebcam(); // or new AprilTagWebcam(this) depending on API
        aprilTagWebcam.init(hardwareMap, telemetry); // if needed by that class

        telemetry.addData("Mode", mode);
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            
        aprilTagWebcam.update();
        AprilTagDetection id20 = aprilTagWebcam.getTagBySpecificId(20);
        aprilTagWebcam.displayDetectionTelemetry(id20);
        
        }
    }

    private void redBlueShoot() {
        hw.setPower(1);
        sleep(500);
        hw.stopMotor();
        hw.transLeft.setPower(1);
        hw.transRight.setPower(1);
        hw.shooterMotor.setPower(1);
        sleep(15000);
        hw.stopMotor();
    }

    private void redFarShoot() {
        // So it goes with the intake and shooting facing forward

        // IF WE DON'T CHANGE DRIVE TRAIN
        hw.setPower(-1);
//        hw.setPower(1);
        sleep(750);
        hw.stopMotor();
        hw.strafeLeft(1.0);
//        hw.strafeRight();
        hw.stopMotor();
        hw.setPower(-1);
//        hw.setPower(1);
        sleep(100);
        hw.stopMotor();
        // These might need to be reversed for the forward drive train
        hw.frontLeft.setPower(1);
        hw.backLeft.setPower(1);
        sleep(250);
        hw.stopMotor();
        hw.transLeft.setPower(1);
        hw.transRight.setPower(1);
        hw.shooterMotor.setPower(1);
        sleep(15000);
        hw.stopMotor();
    }

    private void blueFarShoot() {
        hw.setPower(-1);
//        hw.setPower(1);
        sleep(750);
        hw.stopMotor();
        hw.strafeRight(1.0);
//        hw.strafeLeft();
        hw.stopMotor();
        hw.setPower(-1);
//        hw.setPower(1);
        sleep(100);
        // These might need to be reversed for the forward drive train
        hw.frontRight.setPower(1);
        hw.backRight.setPower(1);
        sleep(250);
        hw.stopMotor();
        hw.transLeft.setPower(1);
        hw.transRight.setPower(1);
        hw.shooterMotor.setPower(1);
        sleep(15000);
        hw.stopMotor();
    }


    public int defaultMode() {
        return 1;
    }


}
