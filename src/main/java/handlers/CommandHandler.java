package handlers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import model.Command;
import model.OfferedClasses;
import model.YearTrack;

public class CommandHandler {
	Map<OfferedClasses, SortedSet<String>> classList = new EnumMap<>(OfferedClasses.class);
	Map<YearTrack, SortedSet<String>> sectionList = new EnumMap<>(YearTrack.class);
	SortedMap<Integer, SortedSet<String>> gradeList = new TreeMap<>(Comparator.reverseOrder());

	public CommandHandler(SortedMap<String, Set<OfferedClasses>> studentToClass,
			SortedMap<String, YearTrack> studentPlacement) {
		for (Entry<String, Set<OfferedClasses>> pair : studentToClass.entrySet()) {
			for (OfferedClasses course : pair.getValue()) {
				if (!course.subject.equals("COM")) {
					continue;
				}
				classList.putIfAbsent(course, new TreeSet<>());
				classList.get(course).add(pair.getKey());
			}
		}

		for (Entry<String, YearTrack> pair : studentPlacement.entrySet()) {
			if (pair.getValue() == null) {
				continue;
			}
			sectionList.putIfAbsent(pair.getValue(), new TreeSet<>());
			sectionList.get(pair.getValue()).add(pair.getKey());

			gradeList.putIfAbsent(YearTrack.yearsFromGraduating(pair.getValue().prefix), new TreeSet<>());
			gradeList.get(YearTrack.yearsFromGraduating(pair.getValue().prefix)).add(pair.getKey());
		}
	}

	public List<Command[]> generateGradeCommands(int year, boolean firstExecution) {
		String command = firstExecution ? "ADDSTUDENT" : "MOVESTUDENT";
		List<Command[]> list = new ArrayList<>();
		for (Entry<Integer, SortedSet<String>> pair : gradeList.entrySet()) {
			int i = 0;
			Command[] commands;
			if (firstExecution || pair.getKey() == 4) {
				commands = new Command[2];
				commands[i++] = new Command("CREATECHANNEL", String.valueOf(year + pair.getKey()), "", "PRIVATE");
			} else {
				commands = new Command[1];
			}
			commands[i++] = new Command(command, String.valueOf(year + pair.getKey()), "",
					pair.getValue().toArray(new String[0]));
			list.add(commands);
		}
		return list;

	}

	public List<Command[]> generateSectionCommands(int year, boolean firstExecution) {
		String command = firstExecution ? "ADDSTUDENT" : "MOVESTUDENT";
		List<Command[]> list = new ArrayList<>();
		for (Entry<YearTrack, SortedSet<String>> pair : sectionList.entrySet()) {
			if (isYearOnly(pair.getKey())) {
				continue;
			}
			int i = 0;
			Command[] commands;
			if (firstExecution || pair.getKey().prefix.equals("SOPHMORE")) {
				commands = new Command[2];
				commands[i++] = new Command("CREATECHANNEL",
						String.valueOf(year + YearTrack.yearsFromGraduating(pair.getKey())),
						Objects.toString(pair.getKey().track, ""), "PRIVATE");
			} else {
				commands = new Command[1];
			}
			commands[i++] = new Command(command, String.valueOf(year + YearTrack.yearsFromGraduating(pair.getKey())),
					Objects.toString(pair.getKey().track, ""), pair.getValue().toArray(new String[0]));
			list.add(commands);
		}
		return list;
	}

	private boolean isYearOnly(YearTrack yearTrack) {
		return (yearTrack == YearTrack.FRESHMAN || yearTrack.toString().endsWith("BA"));
	}
}
