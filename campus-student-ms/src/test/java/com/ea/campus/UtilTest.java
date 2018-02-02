package com.ea.campus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ea.campus.ms.restutil.util.ObjectMapperUtil;
import com.ea.campus.ms.student.dto.PaymentType;
import com.ea.campus.ms.student.dto.StudentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
public class UtilTest {

	private static final transient Logger log = LoggerFactory.getLogger(UtilTest.class);

	@Test
	public void mapper() {
		
		Object object = new StudentDTO("1", "ermal", "aliraj", "DevOps", PaymentType.NEUTRAL);
		ObjectMapper mapper = ObjectMapperUtil.getDefaultObjectMapper();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		String str = null;
		try {
			mapper.writeValue(out, object);
			str = new String(out.toByteArray());
		} catch (IOException var5) {
			
		}
		
		log.debug("object as json: "+str);
	}

}
