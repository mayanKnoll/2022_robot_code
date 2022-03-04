package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.RobotButtons;
import frc.robot.Constants;
import frc.util.OutputSystem;
import frc.util.SuperInterface;
import frc.util.SuperNavX;
import frc.util.SuperSystem;
import frc.util.PID.Gains;
import frc.util.motor.SuperSparkMax;
import frc.util.commands.DriveWithJoysticksAccCommand;
import frc.util.commands.ResetSensorsCommand;


public class DriveSystem extends OutputSystem implements SuperInterface{
  private SuperSparkMax RM = new SuperSparkMax(Constants.CAN_DRIVE_RM_MOTOR, MotorType.kBrushless,
      Constants.AMPER_LIMIT, Constants.reverse, IdleMode.kBrake);
  private SuperSparkMax RS1 = new SuperSparkMax(Constants.CAN_DRIVE_RS1_MOTOR, MotorType.kBrushless,
      Constants.AMPER_LIMIT, Constants.reverse,
      IdleMode.kBrake);
  private SuperSparkMax RS2 = new SuperSparkMax(Constants.CAN_DRIVE_RS2_MOTOR, MotorType.kBrushless,
      Constants.AMPER_LIMIT, Constants.reverse,
      IdleMode.kBrake);
  private SuperSparkMax LM = new SuperSparkMax(Constants.CAN_DRIVE_LM_MOTOR, MotorType.kBrushless,
      Constants.AMPER_LIMIT, !Constants.reverse,
      IdleMode.kBrake);
  private SuperSparkMax LS1 = new SuperSparkMax(Constants.CAN_DRIVE_LS1_MOTOR, MotorType.kBrushless,
      Constants.AMPER_LIMIT, !Constants.reverse,
      IdleMode.kBrake);
  private SuperSparkMax LS2 = new SuperSparkMax(Constants.CAN_DRIVE_LS2_MOTOR, MotorType.kBrushless,
      Constants.AMPER_LIMIT, !Constants.reverse,
      IdleMode.kBrake);
      

  private final double ENCODER_2_METER = 4.0/74.0;

  public static final Gains visionGains = new Gains("visionGains", 0.07, 0, 0.14);
  //public static final Gains autoGains = new Gains("autoGains", 0.1825, 0.05, 1, 0, 0.03);
  public static final Gains autoGains = new Gains("autoGains", 0.225, 0.075, 8, 0, 0);
  public static final Gains turnGains = new Gains("turnGains", 0.15, 0, 0.3);

  public DriveSystem() {
    super("Drive");
    RM.setIdleMode(IdleMode.kCoast);
    RS1.setIdleMode(IdleMode.kCoast);
    RS2.setIdleMode(IdleMode.kCoast);
    LM.setIdleMode(IdleMode.kCoast);
    LS1.setIdleMode(IdleMode.kCoast);
    LS2.setIdleMode(IdleMode.kCoast);
    // LS1.follow(LM);
    // LS2.follow(LM);
    // RS1.follow(RM);
    // RS2.follow(RM);
    resetSensors(0);
    getTab().addCommandToDashboard("Reset Sensors",new ResetSensorsCommand(this, 0));
    setDefaultCommand(new DriveWithJoysticksAccCommand(this,
        () -> 0.8 * RobotButtons.driverJoystick.getRawAxis(5)  + 0.2 * Math.pow(RobotButtons.driverJoystick.getRawAxis(5), 3)
        ,() -> Constants.DIRCTION *(1 * RobotButtons.driverJoystick.getRawAxis(0)  + 0 * Math.pow(RobotButtons.driverJoystick.getRawAxis(0), 3)), 1, 1));
  }

  @Override
  public void periodic() {
    getTab().putInDashboard("left position", getLeftPosition(), true);
    getTab().putInDashboard("right position", getRightPosition(), true);
    getTab().putInDashboard("left encoder position", getLeftEncoderDistance(), true);
    getTab().putInDashboard("right encoder position", getRightEncoderDistance(), true);

  }

  public void tank(double left, double right) {
    LM.set(left);
    LM.set(left);
    LM.set(left);
    RM.set(right);
    RM.set(right);
    RM.set(right);
  }

  public void setOutput(double setOutput) {
    LM.set(setOutput);
    RM.set(setOutput);
  }

  public Gains getGains() {
    return visionGains;
  }

  public Gains getAutoGains() {
    return autoGains;
  }

  public Gains getTurnGains() {
    return turnGains;
  }

  public double getOutput() {
    return Double.NaN;
  }

  public double getPosition() {
    return (Math.abs(RM.getEncoder().getPosition() * ENCODER_2_METER)
        + Math.abs(LM.getEncoder().getPosition() * ENCODER_2_METER)) / 2;
  }

  public double getRightEncoderDistance() {
    return RM.getEncoder().getPosition() * ENCODER_2_METER;
  }

  public double getLeftEncoderDistance() {
    return LM.getEncoder().getPosition() * ENCODER_2_METER;
  }

  public double getRightPosition() {
    return RM.getEncoder().getPosition();
  }

  public double getLeftPosition() {
    return LM.getEncoder().getPosition();
  }

  public void stopOutput() {
    LM.set(0);
    RM.set(0);
  }

  @Override
  public void resetSensors(double pos) {
    RM.reset(pos);
    LM.reset(pos);
    
  }
}