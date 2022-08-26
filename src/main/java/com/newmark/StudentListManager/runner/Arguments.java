package com.newmark.StudentListManager.runner;

import java.io.File;

import com.google.devtools.common.options.Converter;
import com.google.devtools.common.options.Converters.BooleanConverter;
import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import com.google.devtools.common.options.OptionsParsingException;

public class Arguments extends OptionsBase {
	@Option(name = "studentFile", abbrev = 'i', help = "Location of CSV file", defaultValue = "", converter = FileConverter.class)
	public File studentFile;
	@Option(name = "outputDirectory", abbrev = 'o', help = "Directory to output file, optional", defaultValue = "", converter = FileConverter.class)
	public File outputDirectory;
	@Option(name = "year", abbrev = 'y', help = "Current year", defaultValue = "-1", converter = IntegerConverter.class)
	public int year;
	@Option(name = "firstRun", abbrev = 'f', help = "Set to true if this is the first time this is being run on this workspace", defaultValue = "False", converter = BooleanConverter.class)
	public boolean firstRun;

	public static class FileConverter implements Converter<File> {
		@Override
		public File convert(String input) throws OptionsParsingException {
			if (input == null || input.isBlank()) {
				return null;
			}
			return new File(input);
		}

		@Override
		public String getTypeDescription() {
			return "File from String";
		}
	}

	public static class IntegerConverter implements Converter<Integer> {

		@Override
		public Integer convert(String input) throws OptionsParsingException {
			try {
				return Integer.parseInt(input);
			} catch (NumberFormatException e) {
				throw new OptionsParsingException("Cannot parse number: " + input);
			}
		}

		@Override
		public String getTypeDescription() {
			return "Integer from String";
		}
	}
}
