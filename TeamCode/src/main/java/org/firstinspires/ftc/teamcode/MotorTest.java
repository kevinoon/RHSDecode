package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp

public class MotorTest extends LinearOpMode {
    private Servo testServo;

    // public void init() {
    //     testServo = hardwareMap.get(Servo.class, "testServo");
    // }
    
    public void runOpMode() {

        testServo = hardwareMap.get(Servo.class, "testServo");
        
        waitForStart();
        
        if(opModeIsActive()) {
            testServo.setPosition(1);
            sleep(5000);
            testServo.setPosition(0);
            sleep(5000);
        }
    }
}