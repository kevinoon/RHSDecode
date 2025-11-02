package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hardware {
    private static Hardware self;
    private OpMode opMode;
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    private DcMotor testMotor;
    private Servo testServo;
    private Servo testServo1;
    // Intake options (choose one):
    // 1) Single-servo intake named "intake" in the robot config (preferred here).
    //    If you want a single servo intake, uncomment the init line below for it.
    public Servo intakeMotor; // single-servo intake (optional)

    // 2) Dual-servo intake (left/right). Many teams use two servos instead of one motor.
    public Servo intakeServoLeft;
    public Servo intakeServoRight;

    // shooter motor
    public DcMotor shooterMotor;
    // Servo to hold/release artifacts for shooting (ExampleTele called this "artifactstopper").
    public Servo artifactStopper;
    
    // Powers can be adjusted here
    private double intakePower = 0.5;
    private double shootPower = 0.5;

    private Hardware(OpMode opMode) {
        self = this;
        this.opMode = opMode;
    }

    public static Hardware getInstance(OpMode opMode) {
        if (self == null) self = new Hardware(opMode);
        return self;
    }

    public void init(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

         testMotor = hardwareMap.get(DcMotor.class, "testMotor");
        testServo = hardwareMap.get(Servo.class, "testServo");
        testServo1 = hardwareMap.get(Servo.class, "testServo1");

    // If you used a single servo for intake, you can initialize it like this (currently commented):
    // intakeMotor = hardwareMap.get(Servo.class, "intake");

    // Intake servos (if present)
    intakeServoLeft = hardwareMap.get(Servo.class, "intakeServoLeft");
    intakeServoRight = hardwareMap.get(Servo.class, "intakeServoRight");

        // Shooter motor
        shooterMotor = hardwareMap.get(DcMotor.class, "shooter");
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Artifact stopper servo (optional)
        try {
            artifactStopper = hardwareMap.get(Servo.class, "artifactstopper");
        } catch (Exception e) {
            artifactStopper = null;
        }
    }

    public void setToRunToPosition() {
        setPower(0);
        setTarget(0);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setPower(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    public void setTarget(int ticks) {
        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);
    }

    public void setToNoEncoder() {
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setWithEncoder() {
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

     public void strafeLeft(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }   
    
    public void strafeRight(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }

    // Set intake power. Supports either a single-servo (`intakeMotor`) or a dual-servo intake
    // (`intakeServoLeft`/`intakeServoRight`). `power` is in [-1.0 .. 1.0].
    public void setIntakePower(double power) {
        // If a single servo named `intakeMotor` is configured, map power -> position.
        if (intakeMotor != null) {
            double pos = 0.5; // neutral
            if (power > 0.05) pos = 1.0;      // intake in
            else if (power < -0.05) pos = 0.0; // out
            intakeMotor.setPosition(pos);
            return;
        }

        // If dual servos are present, map positive power to "intake in" and negative to "out".
        // We use 3 discrete states: in (1.0/0.0), stop (0.5/0.5), out (0.0/1.0). Adjust if your
        // servos require different neutral or travel ranges.
        if (intakeServoLeft != null && intakeServoRight != null) {
            double leftPos = 0.5;
            double rightPos = 0.5;
            if (power > 0.05) { // intake in
                leftPos = 1.0;
                rightPos = 0.0;
            } else if (power < -0.05) { // out
                leftPos = 0.0;
                rightPos = 1.0;
            }
            intakeServoLeft.setPosition(leftPos);
            intakeServoRight.setPosition(rightPos);
        }
    }

    // Stop intake (set to neutral). Works for single-servo or dual-servo intake.
    public void stopIntake() {
        if (intakeMotor != null) {
            intakeMotor.setPosition(0.5);
            return;
        }
        if (intakeServoLeft != null && intakeServoRight != null) {
            // set to neutral/stop positions
            intakeServoLeft.setPosition(0.5);
            intakeServoRight.setPosition(0.5);
        }
    }

    // Set shooter motor power (-1.0 .. 1.0). Safe if shooterMotor is not configured.
    public void setShooterPower(double power) {
        if (shooterMotor != null) shooterMotor.setPower(power);
    }

    // Stop shooter.
    public void stopShooter() {
        if (shooterMotor != null) shooterMotor.setPower(0);
    }
}