package com.example.demo.controller;

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
    @DisplayName("ログインボタンをクリックするとメニュー画面に遷移するテスト")
    public void LoginToMenuTest() throws Exception{
    	//"/login"からPOSTでアクセスするリクエストを作成
        mockMvc.perform(post("/login") 
        // フォームのパラメータをセット
	        .param("empId", "50001") 
	        .param("password", "sato2023")) 
        	//リクエストの結果のHttpStatusがOKであることを検証
            .andExpect(status().is3xxRedirection())
          //リダイレクト先のURLが"/login"であることを検証
        	.andExpect(redirectedUrl("/menu"));    }
}
