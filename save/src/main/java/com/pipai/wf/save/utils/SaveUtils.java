package com.pipai.wf.save.utils;

import java.util.ArrayList;
import java.util.Arrays;

import com.pipai.wf.save.SaveHeader;

public final class SaveUtils {

	private SaveUtils() {
	}

	public static ArrayList<String> getLinesUnderHeader(SaveHeader header, String rawSaveData) {
		String[] lines = rawSaveData.split("\n");
		ArrayList<String> retLines = new ArrayList<>();
		boolean isUnderHeader = false;
		lines = Arrays.stream(lines).map(String::trim).toArray((size) -> new String[size]);
		for (String line : lines) {
			if (isHeader(line)) {
				SaveHeader currentHeader = SaveHeader.getHeader(line).get();
				if (currentHeader.equals(header)) {
					isUnderHeader = true;
				} else {
					isUnderHeader = false;
				}
				continue;
			}
			if (isUnderHeader) {
				retLines.add(line);
			}
		}
		return retLines;
	}

	public static boolean isHeader(String line) {
		return line.trim().startsWith("[") && line.trim().endsWith("]");
	}

}
