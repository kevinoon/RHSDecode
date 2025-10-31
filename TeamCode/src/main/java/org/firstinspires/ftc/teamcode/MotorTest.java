package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous

public class MotorTest extends LinearOpMode {
    
    private DcMotor testMotor;

    // public void init() {
    //     testServo = hardwareMap.get(Servo.class, "testServo");
    // }
    
    public void runOpMode() {
        
        testMotor = hardwareMap.get(DcMotor.class, "testMotor");
        
        waitForStart();
        
        if(opModeIsActive()) {
            testMotor.setPower(1.0);
            sleep(5000);
            testMotor.setPower(0);
            sleep(5000);

            // These guys fr cant build anything what is the point of this, we have an hour left and they have made 0 progress on the robot
        }
    }
}