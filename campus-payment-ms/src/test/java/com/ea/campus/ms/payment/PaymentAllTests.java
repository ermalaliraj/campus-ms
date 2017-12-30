package com.ea.campus.ms.payment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	PaymentServiceTest.class,
	PaymentIntegrationTests.class
})
public class PaymentAllTests {

}
