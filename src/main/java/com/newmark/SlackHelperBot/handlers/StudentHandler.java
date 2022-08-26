package com.newmark.SlackHelperBot.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.newmark.SlackHelperBot.model.CSVStudent;
import com.newmark.SlackHelperBot.model.OfferedClasses;
import com.newmark.SlackHelperBot.model.YearTrack;

public class StudentHandler {
	private final Reader csvReader;

	private final SortedMap<String, Set<OfferedClasses>> studentToClass = new TreeMap<>();
	private final SortedMap<String, YearTrack> studentPlacement = new TreeMap<>();

	private boolean executed = false;

	public StudentHandler(File csvFile) throws IOException {
		try {
			this.csvReader = new FileReader(csvFile);
		} catch (FileNotFoundException e) {
			throw new IOException(e);
		}
	}

	public StudentHandler(String csvString) {
		this.csvReader = new StringReader(csvString);
	}

	public void execute() throws IOException {
		CSVReader handler = new CSVReader(csvReader);
		for (CSVStudent student : handler) {
			studentToClass.putIfAbsent(student.email(), EnumSet.noneOf(OfferedClasses.class));
			studentToClass.get(student.email()).add(student.course());
		}

		for (Entry<String, Set<OfferedClasses>> pair : studentToClass.entrySet()) {
			studentPlacement.put(pair.getKey(), OfferedClasses.determinePlacement(pair.getValue()));
		}
		executed = true;
	}

	public SortedMap<String, YearTrack> getStudentPlacement() {
		if (!executed) {
			throw new IllegalStateException();
		}
		return studentPlacement;
	}

	public SortedMap<String, Set<OfferedClasses>> getStudentClasses() {
		if (!executed) {
			throw new IllegalStateException();
		}
		return studentToClass;
	}

}
