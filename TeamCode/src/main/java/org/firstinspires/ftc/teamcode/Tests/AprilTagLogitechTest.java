package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;

@Autonomous(name = "AprilTag Logitech Test")
public class AprilTagLogitechTest extends OpMode {
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    @Override
    public void init() {
        // Initialize the AprilTag processor
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();

        // Initialize the VisionPortal with the Logitech webcam named "webcam" and enable live view
        visionPortal = new VisionPortal.Builder()
            .setCamera(hardwareMap.get(WebcamName.class, "webcam"))
            .addProcessor(aprilTag)
            .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
            .enableLiveView(true)
            .build();

        telemetry.addData("Status", "AprilTag initialized with Logitech webcam and live view enabled");
        telemetry.update();
    }

    @Override
    public void start() {
        // Start streaming when the op mode starts
        visionPortal.resumeStreaming();
    }

    @Override
    public void loop() {
        // Get current detections
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Collect ball colors based on detected tag IDs
        StringBuilder ballColors = new StringBuilder("Ball colors: ");
        for (AprilTagDetection detection : currentDetections) {
            String color = getBallColor(detection.id);
            if (color != null) {
                ballColors.append(color).append(", ");
            }
        }
        if (ballColors.length() > 13) { // Remove trailing comma and space
            ballColors.setLength(ballColors.length() - 2);
        } else {
            ballColors.append("None detected");
        }
        telemetry.addLine(ballColors.toString());

        // Display info for each detection
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }

        // Key information
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");

        telemetry.update();
    }

    private String getBallColor(int id) {
        switch (id) {
            case 1: return "Purple";
            case 2: return "Green";
            case 3: return "Purple";
            default: return null; // Unknown tag
        }
    }

    @Override
    public void stop() {
        // Close the vision portal to save resources
        visionPortal.close();
    }
}