package com.kh.ajax.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.ajax.model.vo.Sample;
import com.kh.ajax.model.vo.User;

@Controller
public class TestController {

	@Autowired
	private Sample sam;
	
	@RequestMapping("test.do")
	public void test() {
		System.out.println(sam);
	}
	
	// 1. ServletOutputStream을 이용하여 전송
	@RequestMapping("test1.do")
	public void test1(@RequestParam("name") String name, @RequestParam("age") int age, HttpServletResponse response) {
		// 반환값 없음 (다른 뷰로 이동하는게 아니기 때문에)
		
		try {
			PrintWriter out = response.getWriter(); // 예외 try~catch해도 되고, throws해도 됨
			
			if(name.equals("강건강") && age == 20) {
				out.print("ok");
			} else {
				out.print("fail");
			}
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	@RequestMapping("test2.do")
//	@ResponseBody // return하는 String이 화면(뷰) 이름이 아닌, 뷰로 응답할 데이터로 인지시키기 위해 추가
//	public String test2() {
//		
//		// JSON객체 반환 위해 라이브러리 등록 필요 -> pom.xml에서 추가
//		JSONObject job = new JSONObject();
//		job.put("no", 123); // map방식
//		job.put("title", "return json object test");
//		try {
//			job.put("writer", URLEncoder.encode("남나눔", "UTF-8")); // URLEncoder이용하여 UTF-8로 인코딩
//			job.put("content", URLEncoder.encode("JSON 객체를 뷰로 리턴합니다.", "UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		
//		return job.toJSONString();
//	}
	
//	@RequestMapping("test2.do")
//	public void test2(HttpServletResponse response) {
//		
//		// JSON객체 반환 위해 라이브러리 등록 필요 -> pom.xml에서 추가
//		JSONObject job = new JSONObject();
//		job.put("no", 123); // map방식
//		job.put("title", "return json object test");
//		job.put("writer", "남나눔"); 
//		job.put("content", "JSON 객체를 뷰로 리턴합니다.");
//		
//		try {
//			// response.setContentType("text/html; charset=UTF-8"); // dataType: 'json'했을시 이렇게 보내도 됨
//			response.setContentType("application/json; charset=UTF-8");
//			PrintWriter out = response.getWriter(); // 인코딩 해서 String 리턴하는 방법 대신 PrintWriter 이용
//			out.println(job);
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
	
	@RequestMapping(value="test2.do", produces="application/json; charset=UTF-8") // 보낼 방식 여기 적어줌(한글을 인식시키기 위해)
	@ResponseBody // return하는 String이 화면(뷰) 이름이 아닌, 뷰로 응답할 데이터로 인지시키기 위해 추가
	public String test2() {
		
		// JSON객체 반환 위해 라이브러리 등록 필요 -> pom.xml에서 추가
		JSONObject job = new JSONObject();
		job.put("no", 123); // map방식
		job.put("title", "return json object test");
		job.put("writer", "남나눔");
		job.put("content", "JSON 객체를 뷰로 리턴합니다.");
		
		return job.toJSONString();
	}
	
	@RequestMapping(value="test3.do", produces="application/json; charset=UTF-8")
	@ResponseBody
	public /* void */String test3(/* HttpServletResponse response */) {
		ArrayList<User> list = new ArrayList<User>();
		list.add(new User("u111", "p111", "강건강", 20, "k111@y.com", "01011111111"));
		list.add(new User("u222", "p222", "남나눔", 21, "n222@y.com", "01022222222"));
		list.add(new User("u333", "p333", "도대담", 22, "d222@y.com", "01033333333"));
		list.add(new User("u444", "p444", "류라라", 23, "r222@y.com", "01044444444"));
		list.add(new User("u555", "p555", "문미미", 24, "m222@y.com", "01055555555"));
		// 연습용이라 DB 안 거치고 list에 값 직접 넣음
		
		JSONArray jArr = new JSONArray();
//		for(int i = 0; i < list.size(); i++) {
		for(User user : list) {
			JSONObject jObj = new JSONObject();
//			userObj.put("userId", list.get(i).getUserId());
			jObj.put("userId", user.getUserId());
			jObj.put("userPwd", user.getUserPwd());
			jObj.put("userName", user.getUserName());
			jObj.put("age", user.getAge());
			jObj.put("email", user.getEmail());
			jObj.put("phone", user.getPhone());
			
			jArr.add(jObj);
		}
		
		return jArr.toJSONString();
//		response.setContentType("application/json; charset=UTF-8");
//		PrintWriter out;
//		try {
//			out = response.getWriter();
//			out.println(jArr); // jArr을 JSONObject에 담아서 보내도 됨(Map방식으로 배열 보내기)
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
}
