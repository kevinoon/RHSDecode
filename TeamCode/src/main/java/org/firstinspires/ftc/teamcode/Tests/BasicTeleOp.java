package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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
        frontLeft.setDirection(DcMotor.Direction.REVERSE);  // keep reversed
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

        waitForStart();

        // --- Control Loop (Mecanum Drive Only) ---
        while (opModeIsActive()) {

            double y = -gamepad1.left_stick_y;  // forward/back
            double x = gamepad1.left_stick_x;   // strafe
            double rx = gamepad1.right_stick_x; // rotation

            System.out.println("Left Stick Y: " + y + " Left Stick X: " + x + " Right Stick X: " + rx);

            // Basic mecanum drive control
            frontLeft.setPower(y + x + rx);
            backLeft.setPower(y - x + rx);
            frontRight.setPower(y - x - rx);
            backRight.setPower(y + x - rx);

            telemetry.addData("Y", y);
            telemetry.addData("X", x);
            telemetry.addData("RX", rx);
            telemetry.update();
        }
    }
}
