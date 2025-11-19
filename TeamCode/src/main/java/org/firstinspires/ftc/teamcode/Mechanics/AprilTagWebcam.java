package org.firstinspires.ftc.teamcode.Mechanics;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.util.ArrayList;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;
import android.util.Size;

public class AprilTagWebcam {
    private AprilTagProcessor aprilTagProccessor;
    private VisionPortal visionPortal;

    private List<AprilTagDetection> detectedTags = new ArrayList<>();

    private Telemetry telemetry;

    public void init(HardwareMap hw, Telemetry telemetry) {
        this.telemetry = telemetry;

        aprilTagProccessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hw.get(WebcamName.class, "Webcam"));
        // Corrected line using the Size object
        builder.setCameraResolution(new Size(640,480));
        builder.addProcessor(aprilTagProccessor);
        visionPortal = builder.build();
    }

    public void update() {
        detectedTags = aprilTagProccessor.getDetections();
    }

    public List<AprilTagDetection> getDetectedTags() {
        return detectedTags;
    }

    public void displayDetectionTelemetry(AprilTagDetection detectionId) {
        if(detectionId == null) return;

        if (detectionId.metadata != null) {
            telemetry.addLine(String.format("\n==== (ID %d) %s", detectionId.id, detectionId.metadata.name));
            telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detectionId.ftcPose.x, detectionId.ftcPose.y, detectionId.ftcPose.z));
            telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detectionId.ftcPose.pitch, detectionId.ftcPose.roll, detectionId.ftcPose.yaw));
            telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detectionId.ftcPose.range, detectionId.ftcPose.bearing, detectionId.ftcPose.elevation));
        } else {
            telemetry.addLine(String.format("\n==== (ID %d) Unknown", detectionId.id));
            telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detectionId.center.x, detectionId.center.y));
        }
    }

    public AprilTagDetection getTagBySpecificId(int id) {
        for(AprilTagDetection detection : detectedTags) {
            if(detection.id == id) {
                return detection;
            }
        }
        return null;
    }

    public void stop() {
        if(visionPortal != null)  {visionPortal.close(); }
    }
}