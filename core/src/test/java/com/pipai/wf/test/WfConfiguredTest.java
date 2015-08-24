package com.pipai.wf.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.config.WFConfig;

public class WfConfiguredTest extends GdxMockedTest {
	
	@BeforeClass
	public static void resetConfig() {
		WFConfig.resetConfig();
	}
	
	@AfterClass
	public static void unsetConfig() {
		WFConfig.unsetConfig();
	}

}
