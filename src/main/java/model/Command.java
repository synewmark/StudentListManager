package model;

import java.util.Arrays;

public record Command(String command, String year, String section, String... args) {
	public Command {
		CommandEnum.checkCommandValidity(command, year, section, args);
	}

	public Command(String[] commandArray) {
		this(commandArray[0].toUpperCase(), commandArray[1], commandArray[2].toUpperCase(),
				Arrays.copyOfRange(commandArray, 3, commandArray.length));
	}
}
