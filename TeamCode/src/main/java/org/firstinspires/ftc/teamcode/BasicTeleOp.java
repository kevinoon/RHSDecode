package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.openftc.easyopencv.*;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvCameraFactory;

@TeleOp(name = "Basic TeleOp", group = "Linear Opmode")
public class BasicTeleOp extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    @Override
    public void runOpMode() {

        // --- Hardware Mapping ---
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // --- Motor Directions ---
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // --- Set Zero Power Behavior ---
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // --- Set to Run Without Encoders ---
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addLine("Ready to drive!");
        telemetry.update();

        waitForStart();

        // --- Control Loop ---
        while (opModeIsActive()) {

            // ----- Option 1: ARCADE DRIVE -----
            double drive = -gamepad1.left_stick_y;  // forward/back
            double turn = gamepad1.right_stick_x;   // left/right turn

            double leftPower = drive + turn;
            double rightPower = drive - turn;

            // Normalize values so neither exceed 1.0
            leftPower = Math.max(-1.0, Math.min(1.0, leftPower));
            rightPower = Math.max(-1.0, Math.min(1.0, rightPower));

            frontLeft.setPower(leftPower);
            backLeft.setPower(leftPower);
            frontRight.setPower(rightPower);
            backRight.setPower(rightPower);

            // ----- Option 2: STRAFING (if mecanum or omni wheels) -----
            // Uncomment this section if your robot can strafe sideways
            double y = -gamepad1.left_stick_y; // forward/back
            double x = gamepad1.left_stick_x;  // strafe
            double rx = gamepad1.right_stick_x; // rotation
            System.out.println("Left Stick Y: " + y + "Left Stick X: " + x + "Right Stick X: " + rx);
            frontLeft.setPower(y + x + rx);
            backLeft.setPower(y - x + rx);
            frontRight.setPower(y - x - rx);
            backRight.setPower(y + x - rx);

            telemetry.addData("Left Power", leftPower);
            telemetry.addData("Right Power", rightPower);
            telemetry.update();
        }
    }
}
