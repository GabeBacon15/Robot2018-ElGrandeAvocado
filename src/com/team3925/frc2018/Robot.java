package com.team3925.frc2018;

import com.team3925.frc2018.commands.DriveManual;
import com.team3925.frc2018.commands.RunElevator;
import com.team3925.frc2018.commands.RunElevatorRaw;
import com.team3925.frc2018.commands.RunIntakeLiftRaw;
import com.team3925.frc2018.commands.ZeroIntake;
import com.team3925.frc2018.commands.autos.CenterSwitchAuto;
import com.team3925.frc2018.commands.autos.DriveForwardAuto;
import com.team3925.frc2018.subsystems.Elevator;
import com.team3925.frc2018.subsystems.Intake;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	DriveManual drive;
	RunElevator elevate;
	RunElevatorRaw elevateRaw;
	RunIntakeLiftRaw liftRaw;
	Command testAuto;
	Command zeroIntake;
	UsbCamera camera;
	SendableChooser<String> autoSelector;
	private static boolean isElevatorZeroed;

	@Override
	public void robotInit() {
		drive = new DriveManual(OI.getInstance());
		elevateRaw = new RunElevatorRaw();
		liftRaw = new RunIntakeLiftRaw();
		zeroIntake = new ZeroIntake();
		
		//Camera:
		camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(320, 240);
		camera.setFPS(12);
		
		//Autonomous Selector:
		autoSelector = new SendableChooser<String>();
		autoSelector.addDefault("Drive Forward", "DriveForward");
		autoSelector.addObject("Center Switch", "Center");
		autoSelector.addObject("Left Scale", "LeftScale");
		SmartDashboard.putData("Auto", autoSelector);
		
		isElevatorZeroed = false;
	}

	@Override
	public void autonomousInit() {
		Intake.getInstance().setIntakeRollers(0.25); //Hold da shit
		
		//Zero subsystems:
		Elevator.getInstance().zero();
		Intake.getInstance().zeroLift();
		
		//Sets the default case:
		testAuto = new DriveForwardAuto();
		
		if (autoSelector.getSelected().equals("DriveForward")) {
			testAuto = new DriveForwardAuto();
		}else if(autoSelector.getSelected().equals("Center")){
			testAuto = new CenterSwitchAuto(DriverStation.getInstance().getGameSpecificMessage().charAt(0));
		}
		testAuto.start();
		isElevatorZeroed = true;
	}

	@Override
	public void autonomousPeriodic() {
	}

	public void teleopInit() {
		if (isElevatorZeroed == false) {
			Elevator.getInstance().zero();
			Intake.getInstance().zeroLift();
			System.out.println("ZeroCalled");
		}
		drive.start();
		Intake.getInstance().setIntakeRollers(0.25);
	}

	@Override
	public void teleopPeriodic() {
		if (Elevator.getInstance().getLimitSwitch()) {
			Elevator.getInstance().zero();
		}
	}

	@Override
	public void robotPeriodic() {
		Scheduler.getInstance().run();
	}
}
