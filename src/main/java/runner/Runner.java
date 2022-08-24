package runner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Year;
import java.util.List;
import java.util.Scanner;

import com.google.devtools.common.options.OptionsParser;

import handlers.CSVWriter;
import handlers.CommandHandler;
import handlers.StudentHandler;
import model.Command;

public class Runner {
	static Arguments arguments;
	static Scanner scanner = new Scanner(System.in);

	public static void main(String... args) throws IOException {
		OptionsParser parser = OptionsParser.newOptionsParser(Arguments.class);
		parser.parseAndExitUponError(args);
		arguments = parser.getOptions(Arguments.class);
		StudentHandler csv = new StudentHandler(arguments.studentFile);
		csv.execute();
		CommandHandler ch = new CommandHandler(csv.getStudentClasses(), csv.getStudentPlacement());
		CSVWriter sectionWriter = new CSVWriter(
				new FileWriter(getLowestFile(new File(arguments.outputDirectory, "SectionOutput.csv"))));
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
