package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Basic Autonomous", group="Linear Opmode")
public class BasicAutonomous extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    static final double COUNTS_PER_MOTOR_REV = 537.6; // GoBilda 5202 motor
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV) / (WHEEL_DIAMETER_INCHES * Math.PI);

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        if (opModeIsActive()) {
            driveForward(100, 0.5); // Drive forward 100 inches
            sleep(7000);
            // turnRight(90, 0.5);    // Turn right 90 degrees
        }
    }

    private void driveForward(double inches, double power) {
        int moveCounts = (int)(inches * COUNTS_PER_INCH);
        setTargetPosition(moveCounts, moveCounts, moveCounts, moveCounts);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);

        while (opModeIsActive() && motorsAreBusy()) {
            telemetry.addData("Driving Forward", inches + " inches");
            telemetry.update();
        }
        stopMotors();
    }

    private void turnRight(double degrees, double power) {
        // Approximate method for turning. You should tune this based on your robot's turning characteristics.
        double robotTurnCircumference = 18.85; // Adjust this based on your robot (inches)
        double turnDistance = (degrees / 360.0) * robotTurnCircumference;
        int moveCounts = (int)(turnDistance * COUNTS_PER_INCH);

        // Left wheels forward, right wheels backward
        setTargetPosition(moveCounts, -moveCounts, moveCounts, -moveCounts);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);

        while (opModeIsActive() && motorsAreBusy()) {
            telemetry.addData("Turning Right", degrees + " degrees");
            telemetry.update();
        }
        stopMotors();
    }

    private void setPower(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    private void stopMotors() {
        setPower(0);
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

    private boolean motorsAreBusy() {
        return frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy();
    }
    
    private void fullStop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}