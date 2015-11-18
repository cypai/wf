package com.pipai.libgdx.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class GdxMockedTest {

	@BeforeClass
	public static void mockGdxFiles() {
		Gdx.files = mock(Files.class);
		when(Gdx.files.internal(anyString())).thenAnswer(new Answer<FileHandle>() {
			@Override
			public FileHandle answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return new FileHandle("assets/" + (String) args[0]);
			}
		});
		when(Gdx.files.local(anyString())).thenAnswer(new Answer<FileHandle>() {
			@Override
			public FileHandle answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return new FileHandle((String) args[0]);
			}
		});
		when(Gdx.files.external(anyString())).thenAnswer(new Answer<FileHandle>() {
			@Override
			public FileHandle answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				File file = new File("" + (String) args[0]);
				return new FileHandle(file);
			}
		});
	}

	@AfterClass
	public static void unmock() {
		Gdx.files = null;
	}
}
