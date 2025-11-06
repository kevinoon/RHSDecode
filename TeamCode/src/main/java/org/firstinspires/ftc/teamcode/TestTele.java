package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "Test Tele", group = "Linear Opmode")
public class TestTele extends LinearOpMode {

    @Override
    public void runOpMode() {
        Hardware hw = Hardware.getInstance(this);
        hw.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            // --- Mecanum / Omni Drive Control ---
            double y = -gamepad1.left_stick_y;    // forward/back
            double x = gamepad1.left_stick_x;     // strafe
            double rx = gamepad1.right_stick_x;   // rotate

            // Optional debug printout
            System.out.println("Left Stick Y: " + y + 
                               " | Left Stick X: " + x + 
                               " | Right Stick X: " + rx);

            // Apply power directly (no normalization)
            hw.frontLeft.setPower(y + x + rx);
            hw.backLeft.setPower(y - x + rx);
            hw.frontRight.setPower(y - x - rx);
            hw.backRight.setPower(y + x - rx);

            // --- Intake and Shooter (gamepad2) ---
            // Shooter: use right trigger or right bumper to run shooter
            if (gamepad2.right_trigger > 0.05) {
                hw.setShooterPower(gamepad2.right_trigger); // Might need to change this to one
            } else if (gamepad2.right_bumper) {
                hw.setShooterPower(1.0);
            } else {
                hw.stopShooter();
            }

            // Intake: left trigger to intake in, left bumper to reverse/out

            // Left trigger intake in, left bumper spit it out
            if (gamepad2.left_trigger > 0.05) {
                hw.setIntakePower(gamepad2.left_trigger); // Might need to change this to one
            } else if (gamepad2.left_bumper) {
                hw.setIntakePower(-1.0);
            } else {
                hw.stopIntake();
            }

            // --- transLeft and TransRight Servos (gamepad2) ---
            if (gamepad2.dpad_up > 0.05) {
                hw.transLeft.setPosition(1.0);  // Adjust positions as needed
                hw.transRight.setPosition(1.0);
            }


            /*
             * Fallback controller: use gamepad1 if gamepad2 is not available
             */

             if(gamepad1.right_trigger > 0.05){
                 hw.setShooterPower(gamepad1.right_trigger);
             } else if (gamepad1.right_bumper){
                 hw.setShooterPower(1.0);
             } else {
                 hw.stopShooter();
             }

                if(gamepad1.left_trigger > 0.05){
                    hw.setIntakePower(gamepad1.left_trigger);
                } else if (gamepad1.left_bumper){
                    hw.setIntakePower(-1.0);
                } else {
                    hw.stopIntake();
                }

            telemetry.addData("Front Left Power", hw.frontLeft.getPower());
            telemetry.addData("Front Right Power", hw.frontRight.getPower());
            telemetry.addData("Back Left Power", hw.backLeft.getPower());
            telemetry.addData("Back Right Power", hw.backRight.getPower());
            telemetry.addData("Shooter Power", hw.shooterMotor == null ? "(not configured)" : hw.shooterMotor.getPower());
            if (hw.intakeMotor != null) {
                // intakeMotor is a DcMotor so if it is not there it is not there
                telemetry.addData("Intake Servo", hw.intakeMotor.getPower());
            }
            telemetry.update();
        }
    }
}
