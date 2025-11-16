package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Auto")
public class Auto extends LinearOpMode {

    private double shootPower = 0.8;
    private boolean isShooting = false;
    private int modes;
    private Hardware hw; // Class-level variable for Hardware

    @Override
    public void runOpMode() throws InterruptedException {
        hw = Hardware.getInstance(this);
        hw.init(hardwareMap);
        hw.setToNoEncoder();

        waitForStart();

        if(opModeIsActive()) {
            int mode = 1; // Declare mode inside this method

            if (mode == 1) { // Use comparison operator
                redBlueShoot();
            } else if (mode == 2) {
                redFarShoot();
            } else if (mode == 3) {
                blueFarShoot();
            }
        }
    }

    private void redBlueShoot() {
        hw.setPower(1);
        sleep(500);
        hw.setPower(0);
        hw.shooterMotor.setPower(1);
    }

    private void redFarShoot() {
        hw.setPower(1);
        sleep(500);
        hw.stopMotor();
        // hw turn 45 degrees right
        hw.frontLeft.setPower(1);
        hw.backLeft.setPower(1);
        sleep(250);
        hw.stopMotor();
        hw.setShooterPower(shootPower);
    }

    private void blueFarShoot() {
        hw.setPower(1);
        sleep(500);
        hw.stopMotor();
        // hw turn 45 degrees left
        hw.frontRight.setPower(1);
        hw.backRight.setPower(1);
        sleep(250);
        hw.stopMotor();
        hw.setShooterPower(shootPower);
    }
    
    private void collectBallBlue() {
        hw.setPower(-1);
        sleep(500);
        hw.frontRight.setPower(1);
        hw.backRight.setPower(1);
        sleep(250);
        hw.stopMotor();
    }
    
    private void collectBallRed() {
        hw.setPower(-1);
        sleep(500);
        hw.frontLeft.setPower(1);
        hw.backLeft.setPower(1);
        sleep(250);
        hw.stopMotor();
    }
}