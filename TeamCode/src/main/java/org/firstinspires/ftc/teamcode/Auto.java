package main.java.org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name= "Auto")
public class Auto extends LinearOpMode {
    @Config // Change mode in config files before each match
    public int mode = defaultMode(); // 1 is default



    @Override
    public void runOpMode() {
        Hardware hw = Hardware.getInstance(this);
        hw.init(hardwareMap);
        hw.setToNoEncoder();

        telemetry.addData("Mode", mode);
        telemetry.update();

        waitForStart();

        while(opModeIsActive) {

            switch(mode) {
                case 1:
                    redBlueShoot();
                    break;
                case 2:
                    redFarShoot();
                    break;
                case 3:
                    blueFarShoot();
                    break;
            }

            telemetry.addData("All Roads lead to Rome");
            telemetry.update();
        }
    }

    private void redBlueShoot() {
        hw.setPower(1);
        sleep(500);
        hw.stopMotor();
        hw.transLeft.setPower(1);
        hw.transRight.setPower(1);
        hw.shooterMotor.setPower(1);
        sleep(15000);
        hw.stopMotor();
    }

    private void redFarShoot() {
        // So it goes with the intake and shooting facing forward

        // IF WE DON'T CHANGE DRIVE TRAIN
        hw.setPower(-1);
//        hw.setPower(1);
        sleep(750);
        hw.stopMotor();
        hw.strafeLeft();
//        hw.strafeRight();
        hw.stopMotor();
        hw.setPower(-1);
//        hw.setPower(1);
        sleep(100);
        hw.stopMotor();
        // These might need to be reversed for the forward drive train
        hw.frontLeft.setPower(1);
        hw.backLeft.setPower(1);
        sleep(250);
        hw.stopMotor();
        hw.transLeft.setPower(1);
        hw.transRight.setPower(1);
        hw.shooterMotor.setPower(1);
        sleep(15000);
        hw.stopMotor();
    }

    private void blueFarShoot() {
        hw.setPower(-1);
//        hw.setPower(1);
        sleep(750);
        hw.stopMotor();
        hw.strafeRight();
//        hw.strafeLeft();
        hw.stopMotor();
        hw.setPower(-1);
//        hw.setPower(1);
        sleep(100);
        // These might need to be reversed for the forward drive train
        hw.frontRight.setPower(1);
        hw.backRight.setPower(1);
        sleep(250);
        hw.stopMotor();
        hw.transLeft.setPower(1);
        hw.transRight.setPower(1);
        hw.shooterMotor.setPower(1);
        sleep(15000);
        hw.stopMotor();
    }


    public int defaultMode() {
        return 1;
    }


}
