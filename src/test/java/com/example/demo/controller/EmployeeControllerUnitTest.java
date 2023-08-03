package com.example.demo.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringJUnitConfig
@SpringBootTest
@DisplayName("EmployeeControllerの結合テスト")
public class EmployeeControllerUnitTest {
	@Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    
    @BeforeEach
    public void setupMockMvc(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
    @Test
    @DisplayName("従業員一覧ボタンをクリックすると従業員一覧画面に遷移するテスト")
    public void testShowEmpList() throws Exception{
        mockMvc.perform(get("/emp_list"))  
            .andExpect(status().isOk())
            .andExpect(view().name(is("emp_list")));
    }
    
    @Test
    @DisplayName("ログインしていない場合、ログイン画面に遷移するテスト")
    public void testNotLoginMovelogin() throws Exception{
    	mockMvc.perform(get("/emp_list"))  
    	.andExpect(status().is3xxRedirection())
    	.andExpect(redirectedUrl("/login"));
    }
    
}
