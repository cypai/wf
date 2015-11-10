package com.pipai.wf.save;

public enum SaveHeader {
	PARTY("Party");

	private String headerText;

	private SaveHeader(String headerText) {
		this.headerText = headerText;
	}

	@Override
	public String toString() {
		return headerText;
	}

	public static SaveHeader getHeader(String line) {
		String raw = line.substring(1, line.length() - 1);
		for (SaveHeader header : SaveHeader.values()) {
			if (header.headerText.equals(raw)) {
				return header;
			}
		}
		return null;
	}
}
