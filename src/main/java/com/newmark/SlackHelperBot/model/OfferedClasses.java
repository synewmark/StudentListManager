package com.newmark.SlackHelperBot.model;

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

	static Map<YearTrack, OfferedClasses[]> containsRule = new LinkedHashMap<>();
	static Map<YearTrack, OfferedClasses[]> doesNotContainRule = new LinkedHashMap<>();

	static {
		containsRule.put(YearTrack.FRESHMAN, new OfferedClasses[] { COM1300 });

		containsRule.put(YearTrack.SOPHMOREAI, new OfferedClasses[] { COM2545, COM3640, MAT2105 });
		doesNotContainRule.put(YearTrack.SOPHMOREAI, new OfferedClasses[] { COM2113 });

		containsRule.put(YearTrack.SOPHMOREDS, new OfferedClasses[] { COM2545, COM2113 });
		doesNotContainRule.put(YearTrack.SOPHMOREDS, new OfferedClasses[] { MAT2105, COM3640 });

		containsRule.put(YearTrack.SOPHMOREBA, new OfferedClasses[] { COM2545 });

		containsRule.put(YearTrack.SENIORAI, new OfferedClasses[] { COM4010 });

		containsRule.put(YearTrack.SENIORDS, new OfferedClasses[] { COM3563, COM3760 });

		containsRule.put(YearTrack.JUNIORAI, new OfferedClasses[] { COM3760 });
		doesNotContainRule.put(YearTrack.JUNIORAI, new OfferedClasses[] { COM4010, COM3563 });

		containsRule.put(YearTrack.JUNIORDS, new OfferedClasses[] { COM2512, COM3820 });
	}

	public static YearTrack determinePlacement(Collection<OfferedClasses> registeredClasses) {
		outerloop: for (Entry<YearTrack, OfferedClasses[]> entry : containsRule.entrySet()) {
			for (OfferedClasses course : entry.getValue()) {
				if (!registeredClasses.contains(course)) {
					continue outerloop;
				}
			}
			for (OfferedClasses course : doesNotContainRule.getOrDefault(entry.getKey(), new OfferedClasses[0])) {
				if (registeredClasses.contains(course)) {
					continue outerloop;
				}
			}
			return entry.getKey();
		}
		if (registeredClasses.contains(COM3640) && registeredClasses.contains(COM3760)
				&& (registeredClasses.contains(COM3800) ^ registeredClasses.contains(COM3820))) {
			return YearTrack.JUNIORBA;

		}
		return null;
	}
}
