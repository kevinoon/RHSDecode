package org.firstinspires.ftc.teamcode.Testing;

// HAZEN CODE

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubSystems.Intake;
import org.firstinspires.ftc.teamcode.SubSystems.Shooter;
import org.firstinspires.ftc.teamcode.utils.GamepadEvents;
@TeleOp(group = "TeleOp", name = "TestIntake")
public class TestIntake extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Intake intake = new Intake(hardwareMap);
        GamepadEvents controller = new GamepadEvents(gamepad1);
        waitForStart();
        double power = 0.8;
        while(opModeIsActive())
        {
            if(controller.dpad_up.onPress())
            {
                power += 0.1;
            }
            if(controller.dpad_down.onPress())
            {
                power -= 0.1;
            }
            intake.setPower(power);

//            shooter.shoot(optimalRPM.getRPM(3.5));
            telemetry.addData("Power: ", intake.getPower());
            telemetry.update();
            controller.update();
        }
    }
}