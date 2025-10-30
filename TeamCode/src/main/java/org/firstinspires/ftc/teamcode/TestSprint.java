package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.evenloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Test Minecraft Sprint", group = "Linear Opmode")
public class TestSprint extends LinearOpMode {

    @Override
    public void runOpMode() {
        Hardware hw = Hardware.getInstance(this);
        hw.init(hardwareMap);

        waitForStart();

        while(opModeIsActive()) {

            // Read Joystick inputs

            // Disabled for testing 
            
            // double y = -gamepad1.left_stick_y; // Forwards and Backwards
            // double x = gamepad1.left_stick_x; // Strafing
            // double rx = gamepad1.right_stick_x; // Rotation

            double y = applyDeadzone(-gamepad1.left_stick_y, 0.1);
            double x = applyDeadzone(gamepad1.left_stick_x, 0.1);
            double rx = applyDeadzone(gamepad1.right_stick_x, 0.1);

            // Determines how far the stick is being pushed
            double stickMagnitude = Math.sqrt(x * x + y * y);
            stickMagnitude = Range.clip(stickMagnitude, 0, 1);

            // smooth scaling,  Cubic curve for better control at low speeds
            double baseScale = Math.pow(stickMagnitude, 3);
            
            // Vanilla (Toggle) Sprinting 
            double moveScale = 0.6 + (0.4 * baseScale); // 0.6 walking, 1 full sprint

            // Key Held
            if(gamepad1.left_stick_button && stickMagnitude > 0.9) {
                moveScale *= 1.3; // Multiplies it so it moves faster
            }

            // Rotation Scaling might remove later idk im ceo
            // This makes it so that the same thing that happens with the left stick
            // Happens with the right stick lmk if I should remove this because I just want to test it out
            // Is this a good explenation, let me know in the comments below. Don't forget to like and subscribe

            // Disabled for testing

            // double turnScale = Math.pow(Math.abs(rx), 3);
            // rx *= turnScale;

            // Mecanum Drive Calcs (Short for Calculations)
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator * moveScale;
            double backLeftPower = (y - x + rx) / denominator * moveScale;
            double frontRightPower = (y - x - rx) / denominator * moveScale;
            double backRightPower = (y + x - rx) / denominator * moveScale;

            // Sets The Power idk why im commenting on this Why do you guys need these comments
            hw.frontLeft.setPower(frontLeftPower);
            hw.backLeft.setPower(backLeftPower);
            hw.frontRight.setPower(frontRightPower);
            hw.backRight.setPower(backRightPower);

            telemetry.addData("Stick Magnitude", stickMagnitude);
            telemetry.addData("Move Scale", moveScale);
            telemetry.addData("Stick Held", gamepad1.left_stick_button);
            telemetry.addData("Front Left", frontLeftPower);
            telemetry.addData("Front Right", frontRightPower);
            telemetry.addData("Back Left", backLeftPower);
            telemetry.addData("Back Right", backRightPower);
            telemetry.update();
        }
    }

    private double applyDeadzone(double value, double deadzone) {
        if(Math.abs(value) < deadzone) {
            return 0;
        } else {
            return Math.signum(value) * (Math.abs(value) - deadzone) / (1 - deadzone);
        }
    }
}