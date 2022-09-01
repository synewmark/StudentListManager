package com.newmark.StudentListManager.model;

import static com.newmark.StudentListManager.model.YearTrack.FRESHMAN;
import static com.newmark.StudentListManager.model.YearTrack.JUNIORAI;
import static com.newmark.StudentListManager.model.YearTrack.JUNIORBA;
import static com.newmark.StudentListManager.model.YearTrack.JUNIORDS;
import static com.newmark.StudentListManager.model.YearTrack.SENIORAI;
import static com.newmark.StudentListManager.model.YearTrack.SENIORDS;
import static com.newmark.StudentListManager.model.YearTrack.SOPHMORE;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public enum OfferedClasses {
	MAT1160, MAT2105, COM1300, COM2113, COM2113L, COM2512, COM2545, COM2545L, COM3563, COM3640, COM3760, COM3800,
	COM3820, COM4010;

	public final String subject;
	public final int course;

	OfferedClasses() {
		this.subject = this.toString().substring(0, 3);
		this.course = Integer.parseInt(this.toString().substring(3).replaceAll("\\D.*", ""));
	}

	static Map<YearTrack, StudentPlacement> rule = new LinkedHashMap<>();

	static {
		// "c" is "registeredClasses" collection shorter name to make reading easier
		rule.put(FRESHMAN, (c) -> (c.contains(COM1300)));

		rule.put(SOPHMORE, (c) -> (c.contains(COM2545)));

		rule.put(JUNIORDS, (c) -> (c.contains(COM2512) && c.contains(COM3820) && c.contains(COM3800)));

		rule.put(SENIORDS, (c) -> (c.contains(COM3563) && c.contains(COM3760)));

		rule.put(JUNIORBA,
				(c) -> (c.contains(COM3640) && c.contains(COM3760)) && (c.contains(COM3800) ^ c.contains(COM3820)));

		rule.put(JUNIORAI, (c) -> (c.contains(COM3760)) && (!c.contains(COM4010) && !c.contains(COM3563)));

		rule.put(SENIORAI, (c) -> (c.contains(COM4010)));
	}

	public static YearTrack determinePlacement(Collection<OfferedClasses> registeredClasses) {
		for (Entry<YearTrack, StudentPlacement> entry : rule.entrySet()) {
			if (entry.getValue().check(registeredClasses)) {
				return entry.getKey();
			}
		}
		return null;
	}

	private interface StudentPlacement {
		public boolean check(Collection<OfferedClasses> registeredClasses);
	}
}
