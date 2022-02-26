package frc.robot.commands.auto;


import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.Constants;
import frc.robot.commands.collect.CloseCollectCommand;
import frc.robot.commands.collect.CollectCommand;
import frc.robot.commands.shooting.ShootingCommand;
import frc.util.SuperNavX;
import frc.util.commands.TimeCommand;
import frc.util.pathGenerator.commandAuto.AutoGenerator;
import frc.util.pathGenerator.drive_controls.EncoderAndNavxDriveControl;
import frc.robot.subsystems.CartridgeSystem;
import frc.robot.subsystems.CollectSystem;
import frc.robot.subsystems.DriveSystem;
import frc.robot.subsystems.ShootingSystem;

public class twoBall extends AutoGenerator {
    public twoBall(DriveSystem driveSystem, SuperNavX navX, ShootingSystem shootingSystem, CartridgeSystem cartridgeSystem, CollectSystem collectSystem) {
            super("2 ball", driveSystem.getAutoGains(), driveSystem, navX, 200);
            
            Constants.L1.inReverse();
            addCommands(new ParallelDeadlineGroup(new TimeCommand(2500), new ShootingCommand(shootingSystem, cartridgeSystem)));
            addCommands(addFollowPathCommand(Constants.L1, new EncoderAndNavxDriveControl(driveSystem, navX)));
            addCommands(new ParallelDeadlineGroup(addFollowPathCommand(Constants.L180, new EncoderAndNavxDriveControl(driveSystem, navX)),new CollectCommand(cartridgeSystem, collectSystem)));
            addCommands(new ParallelDeadlineGroup(addFollowPathCommand(Constants.L2, new EncoderAndNavxDriveControl(driveSystem, navX)),new CollectCommand(cartridgeSystem, collectSystem)));
            addCommands(addFollowPathCommand(Constants.L360, new EncoderAndNavxDriveControl(driveSystem, navX)));
            addCommands(new CloseCollectCommand(collectSystem, true));
            addCommands(addFollowPathCommand(Constants.L3, new EncoderAndNavxDriveControl(driveSystem, navX)));
            addCommands(new ParallelDeadlineGroup(new TimeCommand(2500), new ShootingCommand(shootingSystem, cartridgeSystem)));

    }
}
