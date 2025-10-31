package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous

public class MotorTest extends LinearOpMode {
    
    private DcMotor testMotor;
    private Servo testServo;
    private Servo testServo1;

    // public void init() {
    //     testServo0 = hardwareMap.get(Servo.class, "testServo0");
    //     testServo1 = hardwareMap.get(Servo.class, "testServo1");
    //  }
    
    public void runOpMode() {
        
        testMotor = hardwareMap.get(DcMotor.class, "testMotor");
        testServo = hardwareMap.get(Servo.class, "testServo");
        testServo1 = hardwareMap.get(Servo.class, "testServo1");
        
        waitForStart();
        
        if(opModeIsActive()) {
            testMotor.setPower(1.0);
            testServo.setPosition(1.0);
            testServo1.setPosition(1.0);
            sleep(5000);
            testMotor.setPower(0);
            testServo.setPosition(0);
            testServo1.setPosition(0);
            sleep(5000);

            // These guys fr cant build anything what is the point of this, we have an hour left and they have made 0 progress on the robot
        }
    }
}