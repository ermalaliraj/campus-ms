package com.ea.campus.ms.payment.it;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

	@Autowired
    private MockMvc mockMvc;

    @Test
    public void loginWithValidUserThenAuthenticated() throws Exception {

    	RequestBuilder rb = null;
    	ResultMatcher rm = null;
        mockMvc.perform(rb).andExpect(rm);
    }
    

}
