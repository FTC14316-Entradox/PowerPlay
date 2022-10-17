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

    public DcMotor cascade  = null;

    public Servo gripper = null;
    public Servo slide1 = null;
    public Servo slide2 = null;
    public double timetime = 0;
    public int servosFixed = 0;

    int sum = 2;
    double speedAdj = 1;

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

        cascade  = hardwareMap.get(DcMotor.class, "cascade");

        //Reversing power for left wheels
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

        gripper = hardwareMap.get(Servo.class, "gripper");
        slide1 = hardwareMap.get(Servo.class, "wire1");
        slide2 = hardwareMap.get(Servo.class, "wire2");

        registerThread(new TaskThread(new TaskThread.Actions() {
            @Override
            public void loop() {
                double y = -gamepad1.left_stick_y;
                double x = gamepad1.left_stick_x;
                double rx = gamepad1.right_stick_x;

                double yAdj = y * Math.abs(y) * Math.abs(y);
                double xAdj = x * Math.abs(x) * Math.abs(x);

                if (y == 0.0) {
                    fl.setPower(xAdj*speedAdj);
                    bl.setPower(-xAdj*speedAdj);
                    fr.setPower(-xAdj*speedAdj);
                    br.setPower(xAdj*speedAdj);
                }

                if (Math.abs(x) < Math.abs(y * Math.tan(3.1415 / 6))) {
                    fl.setPower(yAdj*speedAdj);
                    bl.setPower(yAdj*speedAdj);
                    fr.setPower(yAdj*speedAdj);
                    br.setPower(yAdj*speedAdj);
                }

                //Make this more sensitive and add buttons for fast turn otherwise
                if (Math.abs(rx) > 0) {
                    fl.setPower(-rx*0.6);
                    bl.setPower(-rx*0.6);
                    fr.setPower(rx*0.6);
                    br.setPower(rx*0.6);
                }
            }
        }));

        registerThread(new TaskThread(new TaskThread.Actions() {
            @Override
            public void loop() {
                if (gamepad1.x) {
                    fl.setPower(-1);
                    bl.setPower(-1);
                    fr.setPower(1);
                    br.setPower(1);
                }
                if (gamepad1.b) {
                    fl.setPower(1);
                    bl.setPower(1);
                    fr.setPower(-1);
                    br.setPower(-1);
                }
            }
        }));

        //Speed control
        registerThread(new TaskThread(new TaskThread.Actions() {
            @Override
            public void loop() {
                if (gamepad1.y) {
                    sum = sum + 1;
                }
                if (sum % 2 == 0) {
                    speedAdj = 1;
                }
                if (sum % 2 == 1) {
                    speedAdj = 0.3;
                }
            }
        }));

        registerThread(new TaskThread(new TaskThread.Actions() {
            @Override
            public void loop() {
                if (gamepad2.x) {
                    gripper.setPosition(0.5);
                }
                if (gamepad2.b) {
                    gripper.setPosition(0.6);
                }
            }
        }));

        registerThread(new TaskThread(new TaskThread.Actions() {
            @Override
            public void loop() {
                while (gamepad2.y) {
                    cascade.setPower(0.8);
                }
                while (gamepad2.a) {
                    cascade.setPower(-0.8);
                }
                cascade.setPower(0);
            }
        }));

        registerThread(new TaskThread(new TaskThread.Actions() {
            @Override
            public void loop() {
                if (servosFixed == 0) {
                    slide1.setDirection(Servo.Direction.REVERSE);
                    slide2.setDirection(Servo.Direction.FORWARD);

                    slide1.setPosition(0.6);
                    timetime = getRuntime();
                    while ((timetime + 1) > getRuntime()) {
                        telemetry.addData("Waiting", "1 second1");
                    }
                    slide2.setPosition(0.54);
                    servosFixed = 1;
                }
            }
        }));
    }

    @Override
    public void mainLoop() {
        //No need
    }
}

//grace comment (test)