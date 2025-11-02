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

        // Autonomous routine inspired by ExampleTele: drive to goal, shoot, move to player station, shoot
        driveToGoal(hw);
        shootThreeArtifacts(hw, 3);
        driveToPlayerStationAndBack(hw);
        shootThreeArtifacts(hw, 3);

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

        // If artifact stopper servo exists, open it to allow one artifact through
        if (hw.artifactStopper != null) {
            hw.artifactStopper.setPosition(0.0); // release
        }

        if (hw.shooterMotor != null) hw.setShooterPower(shootPower);
        sleep(250);

        if (hw.artifactStopper != null) {
            hw.artifactStopper.setPosition(0.2); // stop next artifact
        }
        sleep(200);

        if (hw.shooterMotor != null) hw.stopShooter();
        sleep(1500); // wait before allowing next shot

        isShooting = false;
    }

    private void shootThreeArtifacts(Hardware hw, int n) throws InterruptedException {
        int remaining = n;
        while (opModeIsActive() && remaining > 0) {
            if (!isShooting) {
                shoot(hw);
                remaining--;
            }
            displayVisionPortalData();
        }
    }

    private void driveToGoal(Hardware hw) throws InterruptedException {
        // Drive forward for a fixed time (tweak timings for your robot)
        hw.setPower(1.0);
        sleep(2300);
        hw.setPower(0);
        // small turn
        hw.frontLeft.setPower(0.5);
        hw.frontRight.setPower(-0.5);
        hw.backLeft.setPower(0.5);
        hw.backRight.setPower(-0.5);
        sleep(220);
        hw.setPower(0);
        sleep(500);
    }

    private void driveToPlayerStationAndBack(Hardware hw) throws InterruptedException {
        hw.setPower(-1.0);
        sleep(2800);
        hw.setPower(0);
        sleep(10000);
        hw.setPower(1.0);
        sleep(2800);
        hw.setPower(0);
        sleep(500);
    }

    

}