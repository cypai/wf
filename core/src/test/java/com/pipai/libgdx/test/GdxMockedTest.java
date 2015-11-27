package com.pipai.libgdx.test;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class GdxMockedTest {

	@BeforeClass
	public static void mockGdxFiles() {
		Gdx.files = Mockito.mock(Files.class);
		Mockito.when(Gdx.files.internal(Matchers.anyString())).thenAnswer(new Answer<FileHandle>() {
			@Override
			public FileHandle answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return new FileHandle("assets/" + (String) args[0]);
			}
		});
		Mockito.when(Gdx.files.local(Matchers.anyString())).thenAnswer(new Answer<FileHandle>() {
			@Override
			public FileHandle answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return new FileHandle((String) args[0]);
			}
		});
		Mockito.when(Gdx.files.external(Matchers.anyString())).thenAnswer(new Answer<FileHandle>() {
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
