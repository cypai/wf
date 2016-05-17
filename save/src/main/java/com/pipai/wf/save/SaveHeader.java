package com.pipai.wf.save;

import java.util.Objects;
import java.util.Optional;

public final class SaveHeader {
	private final String headerText;

	public SaveHeader(String headerText) {
		String trimmed = headerText.trim();
		if (trimmed.startsWith("[")) {
			trimmed = trimmed.substring(1, trimmed.length());
		}
		if (trimmed.endsWith("]")) {
			trimmed = trimmed.substring(0, trimmed.length() - 1);
		}
		this.headerText = trimmed;
	}

	@Override
	public String toString() {
		return "[" + headerText + "]";
	}

	public static Optional<SaveHeader> getHeader(String line) {
		String trimmedLine = line.trim();
		return trimmedLine.startsWith("[") && trimmedLine.endsWith("]")
				? Optional.of(new SaveHeader(trimmedLine))
				: Optional.empty();
	}

	@Override
	public int hashCode() {
		return Objects.hash(headerText);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof SaveHeader && Objects.equals(headerText, ((SaveHeader) obj).headerText);
	}

}
