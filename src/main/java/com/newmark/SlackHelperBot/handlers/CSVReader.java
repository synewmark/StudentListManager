package com.newmark.SlackHelperBot.handlers;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.newmark.SlackHelperBot.model.CSVStudent;
import com.newmark.SlackHelperBot.model.OfferedClasses;

public class CSVReader implements Iterable<CSVStudent>, Iterator<CSVStudent> {
	private final Iterator<CSVRecord> csvRecordIterator;

	public CSVReader(Reader reader) throws IOException {
		CSVFormat format = CSVFormat.Builder.create(CSVFormat.EXCEL).setHeader().setSkipHeaderRecord(true).build();
		CSVParser parser = format.parse(reader);
		ensureHeaderValsExist(parser.getHeaderNames());
		this.csvRecordIterator = parser.iterator();
	}

	@Override
	public boolean hasNext() {
		return csvRecordIterator.hasNext();
	}

	@Override
	public CSVStudent next() {
		CSVRecord record = csvRecordIterator.next();
		ensureRecordIsValid(record);
		return new CSVStudent(OfferedClasses.valueOf(record.get("Subject") + record.get("Course")),
				record.get("YU_Email"));
	}

	private void ensureRecordIsValid(CSVRecord record) {
		if (!record.isSet("Subject")) {
			throw new IllegalArgumentException(
					"Record " + record.getRecordNumber() + " does not contain header \"Subject\"");
		}
		if (!record.isSet("Course")) {
			throw new IllegalArgumentException(
					"Record " + record.getRecordNumber() + " does not contain header \"Course\"");
		}
		if (!record.isSet("YU_Email")) {
			throw new IllegalArgumentException(
					"Record " + record.getRecordNumber() + " does not contain header \"YU_Email\"");
		}
		try {
			OfferedClasses.valueOf(record.get("Subject") + record.get("Course"));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"Subject: " + record.get("Subject") + record.get("Course") + "is unrecognized");
		}
	}

	private void ensureHeaderValsExist(List<String> headers) {
		if (!headers.contains("Subject")) {
			throw new IllegalArgumentException("Record does not contain header \"Subject\"");
		}
		if (!headers.contains("Course")) {
			throw new IllegalArgumentException("Record does not contain header \"Course\"");
		}
		if (!headers.contains("YU_Email")) {
			throw new IllegalArgumentException("Record does not contain header \"YU_Email\"");
		}
	}

	@Override
	public Iterator<CSVStudent> iterator() {
		return this;
	}

}
