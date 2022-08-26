package com.newmark.SlackHelperBot.model;

public class CommandEnum {
	static enum Command {
		ADDSTUDENT(true), REMOVESTUDENT(true), MOVESTUDENT(true), CREATECHANNEL(true), ARCHIVECHANNEL(false),
		UNARCHIVECHANNEL(false);

		final boolean requireThirdArg;

		private Command(boolean requireThirdArg) {
			this.requireThirdArg = requireThirdArg;
		}
	}

	public static void checkCommandValidity(String command, String year, String section, String... args) {
		Command c;
		try {
			c = Command.valueOf(command);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Command: " + command + "not recognized");
		}
		int n;
		try {
			n = Integer.parseInt(year);
			if (n < 2022 || n > 2100) {
				throw new IllegalArgumentException("Year: " + n + " is out of range of expected values");
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Year: " + year + " is invalid");
		}
		if (!section.isBlank() && !section.equalsIgnoreCase("DS") && !section.equalsIgnoreCase("AI")) {
			throw new IllegalArgumentException(
					"Section: " + section + " is invalid, expected: a blank value, \"DS\" or \"AI\"");
		}
		if (c.requireThirdArg && args.length == 0) {
			throw new IllegalArgumentException(
					"Command: " + command + " requires a third argument, but none is present");
		}
	}
}
