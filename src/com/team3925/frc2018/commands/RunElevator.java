package com.team3925.frc2018.commands;

import com.team3925.frc2018.OI;
import com.team3925.frc2018.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class RunElevator extends Command{
	
	
	private static final double MAX_HEIGHT = 12;
	
	@Override
	protected void execute() {
		Elevator.getInstance().setHeight(MAX_HEIGHT * OI.getInstance().getElevator());
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
}