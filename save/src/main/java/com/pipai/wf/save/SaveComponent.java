package com.pipai.wf.save;

public interface SaveComponent {

	String serialize();

	void deserialize(String rawData) throws CorruptedSaveException;

}
