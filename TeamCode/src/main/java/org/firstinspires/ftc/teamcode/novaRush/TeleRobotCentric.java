package org.firstinspires.ftc.teamcode.novaRush;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name = "Robor Centric Nova Rush")
public class TeleRobotCentric extends OpMode {

    private final RobotHardware robot = new RobotHardware();
    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y; // Stick is inverted: pushing up would equal pushing down without inversion
        double x = gamepad1.left_stick_x; // Stick is not inverted
        double r = gamepad1.right_stick_x; // Rotates the robot clockwise using the right stick
        robot.drive(y, x, r);

        robot.setIntakePower(gamepad2.left_trigger - gamepad2.right_trigger); // Operate the intake with the left and right triggers on gamepad 2
        robot.setSlidePower(-gamepad2.right_stick_y); // Give the slides power with the right stick up and down on gamepad 2

        /*
        if (gamepad2.right_bumper) {
            robot.setGateOpen(true);
        }
        else if (gamepad2.left_bumper) {
            robot.setGateOpen(false);
        } */
    }

}
