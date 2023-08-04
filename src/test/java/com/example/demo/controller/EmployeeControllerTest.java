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
public class EmployeeControllerTest {
	@Autowired
	//テスト対象クラス
    private WebApplicationContext context;

	//MockMVオブジェクト
    private MockMvc mockMvc;
    
    @BeforeEach
    public void setupMockMvc(){
    	//MockMVCオブジェクトにテスト対象メソッドを設定
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
    @Test
    @DisplayName("従業員一覧ボタンをクリックすると従業員一覧画面に遷移するテスト")
    public void testShowEmpList() throws Exception{
    	//"/emp_list"にGETでアクセスするリクエストを作成
        mockMvc.perform(get("/emp_list"))  
        	//リクエストの結果のHttpStatusがOKであることを検証
            .andExpect(status().isOk())
            //遷移先のviewの名前がemp_listであることを検証
            .andExpect(view().name(is("emp_list")));
    }
    
    @Test
    @DisplayName("ログインしていない場合、ログイン画面に遷移するテスト")
    public void testNotLoginMovelogin() throws Exception{
    	//"/emp_list"にGETでアクセスするリクエストを作成
    	mockMvc.perform(get("/emp_list"))  
    	//リクエストの結果のHttpStatusが3xx（リダイレクト）であることを検証
    	.andExpect(status().is3xxRedirection())
    	//リダイレクト先のURLが"/login"であることを検証
    	.andExpect(redirectedUrl("/login"));
    }
}
