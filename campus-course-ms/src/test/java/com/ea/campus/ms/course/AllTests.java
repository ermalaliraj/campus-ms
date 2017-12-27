package com.ea.campus.ms.course;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	CourseServiceTest.class,
	CourseIntegrationTests.class
})
public class AllTests {

}
