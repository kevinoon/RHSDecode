package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Advanced Autonomous 5204", group="Linear Opmode")
public class AdvancedAutonomous extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    static final double COUNTS_PER_MOTOR_REV = 537.7; // GoBilda 5204
    static final double WHEEL_DIAMETER_INCHES = 3.78; // Real GoBilda wheel diameter
    static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * Math.PI);
    static final double ROBOT_TURN_CIRCUMFERENCE = 18.85; // Adjust via testing (distance wheels travel for 360° turn)

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Ready to start");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            // Example sequence: forward → right → backward → left → spin 5x
            driveInches(24, 0.5);      // forward 24"
            strafeInches(24, 0.5);     // right 24"
            driveInches(-24, 0.5);     // backward 24"
            strafeInches(-24, 0.5);    // left 24"
            turnDegrees(360 * 5, 0.6); // spin 5 times
            fullStop();
        }
    }

    /** Move straight (positive = forward, negative = backward) */
    private void driveInches(double inches, double power) {
        int move = (int)(inches * COUNTS_PER_INCH);
        setTargetPosition(move, move, move, move);
        runToPosition(power);
        telemetryLoop("Driving " + inches + " inches");
    }

    /** Strafe left/right (positive = right, negative = left) */
    private void strafeInches(double inches, double power) {
        int move = (int)(inches * COUNTS_PER_INCH);
        // Mecanum strafing: frontLeft opposite of backLeft, etc.
        setTargetPosition(move, -move, -move, move);
        runToPosition(power);
        telemetryLoop("Strafing " + inches + " inches");
    }

    /** Turn right (positive degrees = clockwise, negative = counterclockwise) */
    private void turnDegrees(double degrees, double power) {
        double turnInches = (degrees / 360.0) * ROBOT_TURN_CIRCUMFERENCE;
        int move = (int)(turnInches * COUNTS_PER_INCH);
        setTargetPosition(move, -move, move, -move);
        runToPosition(power);
        telemetryLoop("Turning " + degrees + "°");
    }

    /** --- Helper Methods --- **/

    private void runToPosition(double power) {
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);
    }

    private void telemetryLoop(String msg) {
        while (opModeIsActive() && motorsAreBusy()) {
            telemetry.addData("Status", msg);
            telemetry.addData("FL Pos", frontLeft.getCurrentPosition());
            telemetry.addData("FR Pos", frontRight.getCurrentPosition());
            telemetry.addData("BL Pos", backLeft.getCurrentPosition());
            telemetry.addData("BR Pos", backRight.getCurrentPosition());
            telemetry.update();
        }
        stopMotors();
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void setTargetPosition(int fl, int fr, int bl, int br) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + fl);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + fr);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + bl);
        backRight.setTargetPosition(backRight.getCurrentPosition() + br);
    }

    private void setRunMode(DcMotor.RunMode mode) {
        frontLeft.setMode(mode);
        frontRight.setMode(mode);
        backLeft.setMode(mode);
        backRight.setMode(mode);
    }

    private void setPower(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    private boolean motorsAreBusy() {
        return frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy();
    }

    private void stopMotors() { setPower(0); }
    private void fullStop() { stopMotors(); }
}
