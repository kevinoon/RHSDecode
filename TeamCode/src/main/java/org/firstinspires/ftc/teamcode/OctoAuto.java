package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name = "OctoAuto (Blocks to Java)")
public class OctoAuto extends LinearOpMode {

  private CRServo LeftMiddleIntake;
  private CRServo RightMiddleIntake;
  private DcMotor fl;
  private DcMotor bl;
  private DcMotor fr;
  private DcMotor br;
  private DcMotor outtake;
  private DcMotor intake;

  /**
   * Changes servo powers to inverse to keep balls
   */
  private void KeepBalls() {
    LeftMiddleIntake.setDirection(CRServo.Direction.REVERSE);
    RightMiddleIntake.setDirection(CRServo.Direction.REVERSE);
    LeftMiddleIntake.setPower(1);
    RightMiddleIntake.setPower(0);
    sleep(1000);
  }

  /**
   * Describe this function...
   */
  private void StrafeLeft() {
    fl.setPower(0.8);
    bl.setPower(0.8);
    fr.setPower(-0.8);
    br.setPower(-0.8);
  }

  /**
   * Changes servo powers to shoot ball
   */
  private void Shoot() {
    LeftMiddleIntake.setDirection(CRServo.Direction.REVERSE);
    RightMiddleIntake.setDirection(CRServo.Direction.FORWARD);
    outtake.setDirection(DcMotor.Direction.REVERSE);
    ((DcMotorEx) outtake).setVelocity(1000);
    LeftMiddleIntake.setPower(1);
    RightMiddleIntake.setPower(0.3);
    sleep(2000);
    RightMiddleIntake.setPower(0);
    sleep(600);
    RightMiddleIntake.setPower(0.3);
    ((DcMotorEx) outtake).setVelocity(1100);
    sleep(2000);
    RightMiddleIntake.setPower(0);
    sleep(300);
    ((DcMotorEx) outtake).setVelocity(1100);
    RightMiddleIntake.setPower(0.3);
    sleep(4200);
    RightMiddleIntake.setPower(0);
  }

  /**
   * Describe this function...
   */
  private void RotateLeft() {
    fl.setPower(1);
    bl.setPower(1);
    fr.setPower(1);
    br.setPower(1);
  }

  /**
   * Describe this function...
   */
  private void StrafeRight() {
    fl.setPower(-0.8);
    bl.setPower(-0.8);
    fr.setPower(0.8);
    br.setPower(0.8);
  }

  /**
   * Stops wheel movement
   */
  private void StopMove() {
    bl.setPower(0);
    br.setPower(0);
    fl.setPower(0);
    fr.setPower(0);
    sleep(100);
  }

  /**
   * Changes servo powers to shoot ball
   */
  private void Shoot2() {
    LeftMiddleIntake.setDirection(CRServo.Direction.REVERSE);
    RightMiddleIntake.setDirection(CRServo.Direction.FORWARD);
    outtake.setDirection(DcMotor.Direction.REVERSE);
    ((DcMotorEx) outtake).setVelocity(950);
    LeftMiddleIntake.setPower(1);
    RightMiddleIntake.setPower(0.3);
    sleep(2000);
    RightMiddleIntake.setPower(0);
    sleep(600);
    RightMiddleIntake.setPower(0.3);
    ((DcMotorEx) outtake).setVelocity(1200);
    sleep(2000);
    RightMiddleIntake.setPower(0);
    sleep(300);
    ((DcMotorEx) outtake).setVelocity(950);
    RightMiddleIntake.setPower(0.3);
    sleep(4200);
    RightMiddleIntake.setPower(0);
  }

  /**
   * Describe this function...
   */
  private void RotateRight() {
    fl.setPower(-1);
    bl.setPower(-1);
    fr.setPower(-1);
    br.setPower(-1);
  }

  /**
   * Changes wheel powers to move forward
   */
  private void Drive() {
    fl.setPower(1);
    bl.setPower(-1);
    fr.setPower(-1);
    br.setPower(1);
  }

  /**
   * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
   * Comment Blocks show where to place Initialization code (runs once, after touching the
   * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
   * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
   * Stopped).
   */
  @Override
  public void runOpMode() {
    LeftMiddleIntake = hardwareMap.get(CRServo.class, "LeftMiddleIntake");
    RightMiddleIntake = hardwareMap.get(CRServo.class, "RightMiddleIntake");
    fl = hardwareMap.get(DcMotor.class, "fl");
    bl = hardwareMap.get(DcMotor.class, "bl");
    fr = hardwareMap.get(DcMotor.class, "fr");
    br = hardwareMap.get(DcMotor.class, "br");
    outtake = hardwareMap.get(DcMotor.class, "outtake");
    intake = hardwareMap.get(DcMotor.class, "intake");

    waitForStart();
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        outtake.setDirection(DcMotor.Direction.REVERSE);
        ((DcMotorEx) outtake).setVelocity(1100);
        intake.setPower(-0.8);
        sleep(800);
        Backwards();
        sleep(400);
        StopMove();
        Shoot();
        GrabBall();
        KeepBalls();
        ((DcMotorEx) outtake).setVelocity(1000);
        RightMiddleIntake.setDirection(CRServo.Direction.FORWARD);
        RightMiddleIntake.setPower(1);
        sleep(1000);
        Shoot2();
        break;
        
        telemetry.addData("Whats the point of having programmers when your just gonna do it all yourself");
        telemetry.update();
      }
    }
  }

  /**
   * Changes wheel powers to go in reverse
   */
  private void Backwards() {
    fl.setPower(-1);
    bl.setPower(1);
    fr.setPower(1);
    br.setPower(-1);
  }

  /**
   * Describe this function...
   */
  private void GrabBall() {
    sleep(200);
    RotateLeft();
    sleep(90);
    StrafeLeft();
    sleep(230);
    StopMove();
    Drive();
    sleep(200);
    StopMove();
    slowDrive();
    sleep(2800);
    StopMove();
    StopMove();
    StopMove();
    Backwards();
    sleep(300);
    StopMove();
    RotateRight();
    sleep(140);
    StopMove();
    StrafeRight();
    sleep(380);
    StopMove();
  }

  /**
   * Changes wheel powers to move forward
   */
  private void slowDrive() {
    fl.setPower(0.2);
    bl.setPower(-0.2);
    fr.setPower(-0.2);
    br.setPower(0.2);
  }
}