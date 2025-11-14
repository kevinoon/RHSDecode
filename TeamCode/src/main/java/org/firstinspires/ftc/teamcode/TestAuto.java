package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@Autonomous(name = "Test Autonomous")
public class TestAuto extends LinearOpMode {

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;
    private double shootPower = 0.8;
    private boolean isShooting = false;
    private int modes;

    @Override
    public void runOpMode() throws InterruptedException {
        Hardware hw = Hardware.getInstance(this);
        hw.init(hardwareMap);
        hw.setToNoEncoder();

        initVision();

        telemetry.addData("Status", "Initialized. Waiting for start...");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        switch(modes) {
            case 1 -> redBlueShoot();
            case 2 -> blueFarShoot();
            case 3 -> redFarShoot();
        }

        // Close camera stream to save CPU
        if (visionPortal != null) visionPortal.close();
    }

    private void initVision() {
        try {
            aprilTag = AprilTagProcessor.easyCreateWithDefaults();
            visionPortal = VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName.class, "webcam"), aprilTag);
        } catch (Exception e) {
            aprilTag = null;
            visionPortal = null;
        }
    }

    private void displayVisionPortalData() {
        if (aprilTag == null) return;
        List<AprilTagDetection> detections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", detections.size());
        for (AprilTagDetection d : detections) {
            if (d.metadata != null) {
                telemetry.addData("ID", d.id + " (" + d.metadata.name + ")");
                telemetry.addData("XYZ (in)", String.format("%6.1f %6.1f %6.1f", d.ftcPose.x, d.ftcPose.y, d.ftcPose.z));
            } else {
                telemetry.addData("ID", d.id + " (unknown)");
                telemetry.addData("Center", String.format("%6.0f %6.0f", d.center.x, d.center.y));
            }
        }
        telemetry.update();
    }

    private void shoot(Hardware hw) throws InterruptedException {
        if (isShooting) return;
        isShooting = true;

        if (hw.shooterMotor != null) hw.setShooterPower(shootPower);
        sleep(250);

        if (hw.shooterMotor != null) hw.stopShooter();
        sleep(1500); // wait before allowing next shot

        isShooting = false;
    }

    private void redBlueShoot() {
        // Test with robot to see how far it needs to go
        hw.setPower(-1);
        sleep(500);
        hw.stopMotor();
        hw.shoot(shootPower);
    }



    // Fix these for the robot
    private void redFarShoot() {
        hw.setPower(1);
        sleep(500);
        hw.stopMotor();
        // hw turn 45 degrees right
        hw.shoot(shootPower);
    }

    private void blueFarShoot() {
        hw.setPower(1);
        sleep(500);
        hw.stopMotor();
        // hw turn 45 degrees left
        hw.shoot(shootPower);
    }
}