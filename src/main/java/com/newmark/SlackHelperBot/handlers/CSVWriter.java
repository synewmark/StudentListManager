package com.newmark.SlackHelperBot.handlers;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.newmark.SlackHelperBot.model.Command;

public class CSVWriter {
	private final Writer writer;

	public CSVWriter(Writer writer) {
		this.writer = writer;
	}

	public void write(Collection<Command> commands) throws IOException {
		CSVPrinter printer = new CSVPrinter(writer, CSVFormat.EXCEL);
		for (Command command : commands) {
			printer.printRecord((Object[]) getStringArray(command));
		}
		printer.flush();
	}

	private String[] getStringArray(Command command) {
		String[] arguments = new String[command.args().length + 3];
		arguments[0] = command.command();
		arguments[1] = command.year();
		arguments[2] = command.section();
		System.arraycopy(command.args(), 0, arguments, 3, command.args().length);
		return arguments;
	}
}
