package com.newmark.StudentListManager.runner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Year;
import java.util.List;
import java.util.Scanner;

import com.google.devtools.common.options.OptionsParser;
import com.newmark.StudentListManager.handlers.CSVWriter;
import com.newmark.StudentListManager.handlers.CommandHandler;
import com.newmark.StudentListManager.handlers.StudentHandler;
import com.newmark.StudentListManager.model.Command;

public class Runner {
	static Arguments arguments;
	static Scanner scanner = new Scanner(System.in);

	public static void main(String... args) throws IOException {
		OptionsParser parser = OptionsParser.newOptionsParser(Arguments.class);
		parser.parseAndExitUponError(args);
		arguments = parser.getOptions(Arguments.class);
		checkArgs(arguments);
		StudentHandler csv = new StudentHandler(arguments.studentFile);
		csv.execute();
		CommandHandler ch = new CommandHandler(csv.getStudentClasses(), csv.getStudentPlacement());
		if (arguments.outputDirectory != null) {
			arguments.outputDirectory.mkdirs();
		}
		CSVWriter sectionWriter = new CSVWriter(
				new FileWriter(getLowestFile(new File(arguments.outputDirectory, "TrackOutput.csv"))));
		CSVWriter gradeWriter = new CSVWriter(
				new FileWriter(getLowestFile(new File(arguments.outputDirectory, "GradeOutput.csv"))));
		int year = arguments.year == -1 ? Year.now().getValue() : arguments.year;
		for (Command[] commands : ch.generateGradeCommands(year, arguments.firstRun)) {
			gradeWriter.write(List.of(commands));
		}
		for (Command[] commands : ch.generateSectionCommands(year, arguments.firstRun)) {
			sectionWriter.write(List.of(commands));
		}
	}

	private static void checkArgs(Arguments arguments) {
		if (arguments.studentFile == null || !arguments.studentFile.canRead()) {
			throw new IllegalArgumentException("Cannot read from file: " + arguments.studentFile);
		}
		if (!arguments.studentFile.toString().endsWith(".csv")) {
			throw new IllegalArgumentException("Student file must be a csv: " + arguments.studentFile);
		}
		if (arguments.outputDirectory != null && !arguments.outputDirectory.canWrite()) {
			throw new IllegalArgumentException("Cannot write to directory file: " + arguments.studentFile);
		}
		if (arguments.year != -1 && arguments.year < 2020 || arguments.year > 2100) {
			throw new IllegalArgumentException("Year: " + arguments.year + " is invalid must be 2020 > < 2100");
		}
	}

	public static File getLowestFile(File file) {
		if (!file.exists()) {
			return file;
		}
		String fileString = file.toString();
		int extensionPos = fileString.lastIndexOf('.');
		String fileBeforeExtension = fileString.substring(0, extensionPos);
		String extension = fileString.substring(extensionPos);
		for (int i = 1;; i++) {
			File newFile = new File(fileBeforeExtension + "_" + i + extension);
			if (!newFile.exists()) {
				return newFile;
			}
		}
	}

}