package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.teamcode.threadopmode.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="DriveTest1")
public class DriveTest1 extends ThreadOpMode {

    //Declaring drivetrain members
    public DcMotor fr = null;
    public DcMotor fl = null;
    public DcMotor bl = null;
    public DcMotor br = null;

    public Servo gripper = null;

    //Hardware initialization code
    @Override
    public void mainInit() {

        //Telemetry Init
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Drivetrain hardware
        fr  = hardwareMap.get(DcMotor.class, "frontright");
        fl  = hardwareMap.get(DcMotor.class, "frontleft");
        bl  = hardwareMap.get(DcMotor.class, "backleft");
        br  = hardwareMap.get(DcMotor.class, "backright");

        //Reversing power for left wheels
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);

        gripper = hardwareMap.get(Servo.class, "gripper");

        registerThread(new TaskThread(new TaskThread.Actions() {
            @Override
            public void loop() {
                double y = -gamepad1.left_stick_y;
                double x = gamepad1.left_stick_x;
                double rx = gamepad1.right_stick_x;

                double yAdj = y * Math.abs(y) * Math.abs(y);
                double xAdj = x * Math.abs(x) * Math.abs(x);

                if (y == 0.0) {
                    fl.setPower(xAdj);
                    bl.setPower(-xAdj);
                    fr.setPower(-xAdj);
                    br.setPower(xAdj);
                }

                if (Math.abs(x) < Math.abs(y * Math.tan(3.1415 / 6))) {
                    fl.setPower(yAdj);
                    bl.setPower(yAdj);
                    fr.setPower(yAdj);
                    br.setPower(yAdj);
                }

                if (Math.abs(rx) > 0) {
                    fl.setPower(-rx);
                    bl.setPower(-rx);
                    fr.setPower(rx);
                    br.setPower(rx);
                }
            }
        }));

        registerThread(new TaskThread(new TaskThread.Actions() {
            @Override
            public void loop() {
                if (gamepad1.x) {
                    gripper.setPosition(0.5);
                }
                if (gamepad1.b) {
                    gripper.setPosition(0.5);
                }
            }
        }));
    }

    @Override
    public void mainLoop() {
        //No need
    }
}