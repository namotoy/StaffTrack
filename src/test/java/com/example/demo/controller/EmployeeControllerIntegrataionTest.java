package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("EmployeeControllerの結合テスト")
public class EmployeeControllerIntegrataionTest {

	//MockMVオブジェクト
	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("ログインしていない場合に各画面へ直接アクセスできないテスト")
	public void testRedirectToLoginWhenNotLoggedIn() throws Exception {
		// ユーザーがログインしていない状態でemp_listへのGETリクエストを模倣
		mockMvc.perform(MockMvcRequestBuilders.get("/StaffTrack/emp_list"))
		// レスポンスがリダイレクトとして、指定されたURLへ遷移することを期待
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.redirectedUrl("/StaffTrack/login"));
	}
	
	@Test
	@DisplayName("ログインボタンをクリックするとメニュー画面に遷移するテスト")
	public void LoginToMenuTest() throws Exception{
		//"/login"からPOSTでアクセスするリクエストを作成
		mockMvc.perform(post("/StaffTrack/login") 
				// フォームのパラメータをセット
				.param("empId", "50002") 
				.param("password", "1111")) 
		//リクエストの結果のHttpStatusがOKであることを検証
		.andExpect(status().is3xxRedirection())
		//リダイレクト先のURLが"/menu"であることを検証
		.andExpect(redirectedUrl("/StaffTrack/menu"));
	}
	
	@Test
	@DisplayName("従業員一覧ボタンをクリックすると従業員一覧画面に遷移するテスト")
	public void testShowEmpList() throws Exception{
		  //セッションを作成
	    MockHttpSession session = new MockHttpSession();
	    session.setAttribute("username", "validUsername");
		//"/emp_list"にGETでアクセスするリクエストを作成
		mockMvc.perform(get("/StaffTrack/emp_list")
				// セッションを設定
	            .session(session)
				// フォームのパラメータをセット
				.param("empId", "50002") 
				.param("password", "1111")) 
		//リクエストの結果のHttpStatusがOKであることを検証
		.andExpect(status().isOk())
		//遷移先のviewの名前がemp_listであることを検証
		.andExpect(view().name(is("emp_list")));
	}
	
	@Test
	@DisplayName("従業員登録ボタンをクリックすると従業員登録画面に遷移するテスト")
	public void testShowRegist() throws Exception{
		//セッションを作成
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("username", "validUsername");
		//"/emp_regist"にGETでアクセスするリクエストを作成
		mockMvc.perform(get("/StaffTrack/emp_regist")
				// セッションを設定
				.session(session)
				// フォームのパラメータをセット
				.param("empId", "50002") 
				.param("password", "1111")) 
		//リクエストの結果のHttpStatusがOKであることを検証
		.andExpect(status().isOk())
		//遷移先のviewの名前がemp_registであることを検証
		.andExpect(view().name(is("emp_regist")));
	}
	
	@Test
	@DisplayName("従業員検索ボタンをクリックすると従業員検索画面に遷移するテスト")
	public void testShowSearch() throws Exception{
		//セッションを作成
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("username", "validUsername");
		//"/emp_search"にGETでアクセスするリクエストを作成
		mockMvc.perform(get("/StaffTrack/emp_search")
				// セッションを設定
				.session(session)
				// フォームのパラメータをセット
				.param("empId", "50002") 
				.param("password", "1111")) 
		//リクエストの結果のHttpStatusがOKであることを検証
		.andExpect(status().isOk())
		//遷移先のviewの名前がemp_searchであることを検証
		.andExpect(view().name(is("emp_search")));
	}
	
	@Test
	@DisplayName("従業員削除ボタンをクリックすると従業員削除画面に遷移するテスト")
	public void testShowDelete() throws Exception{
		//セッションを作成
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("username", "validUsername");
		//"/emp_delete"にGETでアクセスするリクエストを作成
		mockMvc.perform(get("/StaffTrack/emp_delete")
				// セッションを設定
				.session(session)
				// フォームのパラメータをセット
				.param("empId", "50002") 
				.param("password", "1111")) 
		//リクエストの結果のHttpStatusがOKであることを検証
		.andExpect(status().isOk())
		//遷移先のviewの名前がemp_deleteであることを検証
		.andExpect(view().name(is("emp_delete")));
	}
	
	@Test
	@DisplayName("従業員更新ボタンをクリックすると従業員更新画面に遷移するテスト")
	public void testShowupdate() throws Exception{
		//セッションを作成
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("username", "validUsername");
		//"/emp_update"にGETでアクセスするリクエストを作成
		mockMvc.perform(get("/StaffTrack/emp_update")
				// セッションを設定
				.session(session)
				// フォームのパラメータをセット
				.param("empId", "50002") 
				.param("password", "1111")) 
		//リクエストの結果のHttpStatusがOKであることを検証
		.andExpect(status().isOk())
		//遷移先のviewの名前がemp_updateであることを検証
		.andExpect(view().name(is("emp_update")));
	}

}
