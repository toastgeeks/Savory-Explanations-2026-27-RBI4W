package org.firstinspires.ftc.teamcode.novaRush;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class RobotHardware {
    private DcMotor rf;
    private DcMotor lf;
    private DcMotor rr;
    private DcMotor lr;

    private DcMotor intake;
    private DcMotor slides;
    private boolean slideHolding = false;

    /*
    private Servo gate;

    private static final double GATE_OPEN_POSITION= 0.5;
    private static final double GATE_CLOSED_POSITION = 0.0;

     */

    private static final int SLIDE_MIN_POSITION = 0;     // fully retracted
    private static final int SLIDE_MAX_POSITION = 2800;  // Fully extended
    private static final int SLIDE_MEDIUM_POSITION = (SLIDE_MIN_POSITION + SLIDE_MAX_POSITION) / 2 ; // Medium position for reaching the low bucket. This may need tuned
    private static final double SLIDE_HOLD_POWER = .03; // Make sure this value doesn't make the slides go up, but high enough to resist gravity
    public void init(HardwareMap hwMap) {
        rf = hwMap.get(DcMotor.class, "rf");
        lf = hwMap.get(DcMotor.class, "lf");
        rr = hwMap.get(DcMotor.class, "rr");
        lr = hwMap.get(DcMotor.class, "lr");

        intake = hwMap.get(DcMotor.class, "intake");
        slides = hwMap.get(DcMotor.class, "slides");
        slides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // zero the encoder at startup position
        slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);    // then switch to a runnable mode

        /*
        gate = hwMap.get(Servo.class, "gate");
        gate.setPosition(GATE_CLOSED_POSITION); // Start closed

        Not plugged in
         */

        rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lr.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        rf.setDirection(DcMotorSimple.Direction.FORWARD);
        rr.setDirection(DcMotorSimple.Direction.FORWARD);

        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        slides.setDirection(DcMotorSimple.Direction.FORWARD);


    }


    public void setSlidePower(double power) {
        int currentPosition = slides.getCurrentPosition();

    // Treats a tiny bit of trigger input as no input so no accidental movement is made
    if (Math.abs(power) < .05) {
        if (!slideHolding) {
            slides.setTargetPosition(currentPosition);
            slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slides.setPower(SLIDE_HOLD_POWER);
            slideHolding = true;
        }
        return;
    }

    //Sets the hold mode so setPower is working normally because the driver is giving real input
    if (slideHolding) {
        slides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideHolding = false;
    }

        // Block further extension past the max
    if (currentPosition >= SLIDE_MAX_POSITION && power > 0) {
        power = 0;
    }
    if (currentPosition <= SLIDE_MIN_POSITION && power < 0) {
        power = 0;
    }

        slides.setPower(power);
    }

    // Moves the slide to a preset medium extension and holds there
    public void goToMediumPosition() {
        slides.setTargetPosition(SLIDE_MEDIUM_POSITION);
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slides.setPower(0.5); // tune this — speed the slide moves at to reach the target
        slideHolding = true;
    }

    // y = forward/back, x = strafe left/right, r = rotate (positive, clockwise)
    public void drive(double y, double x, double r) {
        double rfPower = y - x - r;
        double lfPower = y + x + r;
        double rrPower = y + x - r;
        double lrPower = y - x + r;

        double max = Math.max(1.0, Math.max(Math.abs(rfPower),
                Math.max(Math.abs(lfPower), Math.max(Math.abs(rrPower), Math.abs(lrPower)))));

        // Power to the wheels
        rf.setPower(rfPower / max);
        lf.setPower(lfPower / max);
        rr.setPower(rrPower / max);
        lr.setPower(lrPower / max);
    }

    // Power to the intake
    public void setIntakePower(double power) {
        intake.setPower(power);
    }

    // Opens or closes the bucket gate
    /*
    public void setGateOpen(boolean open) {
        gate.setPosition(open ? GATE_OPEN_POSITION : GATE_CLOSED_POSITION);
    }

    Not plugged in
     */

}