package com.kh.spring.member.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.log.Log4jTest;
import com.kh.spring.member.model.exception.MemberException;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@SessionAttributes("loginUser") // model에 저장된 loginUser를 찾아서 있으면 session에 저장 <- 어노테이션 이용하여 세션에 저장시키기
@Controller // 객체 생성, Controller 성질을 갖게 생성(MemberController를 Controller객체로 등록시키려면 @Controller 어노테이션 추가해야됨, 어노테이션 없을시 객체 등록이 안됨)
public class MemberController {
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	// private MemberService mService = new MemberService();
	
	@Autowired // Marks a constructor, field, setter method, or config method as to be autowired by Spring's dependency injection facilities.
	private MemberService mService; 
	// 인터페이스를 불러오는 것이 됨 -> MemberService이름 변경해도 영향은 없지만 객체를 생성하고 있진 않음
	// MemberServiceImpl클래스에서 @이용 -> @Service("mService") -> 이렇게 하면 객체 관리를 framework가 함(프레임워크가 객체 생성)
	// private MemberService mService;만 하면 객체는 framework통해 만들어졌으나, 만들어진 객체를 mService에 집어넣는, 주입하는 과정이 없음
	// @Autowired를 붙여주면 됨 : DI 의존성 주입 -> pramework가 만들어놓은 객체를 변수 mService에 주입시킴
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login() {
//		System.out.println("로그인 처리합니다.");
//	}
	
	/*********** 파라미터 전송 받기 ***********/
	// 1, HttpServletRequest를 통해 전송받기(JSP/Servlet방식)
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(HttpServletRequest request) {
//		String id1 = request.getParameter("id");
//		String pwd1 = request.getParameter("pwd");
//		
//		System.out.println("id1 : " + id1);
//		System.out.println("pwd1 : " + pwd1);
//		
//	}
	
	// 2. @RequestParam 방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(@RequestParam(value="id", defaultValue="hello") String userId, 
//					  @RequestParam(value="pwd", defaultValue="world") String userPwd,
//					  @RequestParam(value="test", required=false, defaultValue="spring") String test) {
//		// @RequestParam 
//		// value 		: 뷰에서 받아오는 파라미터의 이름, value가 한 개라면 value=샐략하고 "변수명"만 써도 됨
//		// defaultValue : 값이 들어오지 않았거나 null일때 기본적으로 들어갈 value 설정
//		// required 	: 내가 받아오려는 파라미터가 꼭 필요한 파라미터인지, 필수로 받아와야하는지 설정하는 것
//		//				     값이 들어오는 것이 필수가 아닐때 false 지정 -> 값 넣지 않고 이동시 null로 들어옴, defaultValue설정되어 있을시 그 값으로 들어옴
//		//  			     기본값은 true로 만약 값이 들어오지 않는다면 DefaultHandlerExceptionResolver에러 뜸
//		System.out.println("id2 : " + userId);
//		System.out.println("pwd2 : " + userPwd);
//		System.out.println("test : " + test);
//	}
	
	// 3. @RequestParam 생략
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(String id, String pwd) {
//		// 매개변수 이름을 받아올 파라미터명과 일치시켜줌 -> @RequestParam 생략 가능해짐
//		// 받아올 파라미터명과 매개변수명을 동일하게 하면 @RequestParam 생략해도 자동매칭시켜줌
//		// 생략시 defaultValue와 required속성 사용할 수 없게 됨
//		System.out.println("id3 : " + id);
//		System.out.println("pwd4 : " + pwd);
//	}
	
	// 4. @ModelAttribute 방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(@ModelAttribute Member m) {
//		// url의 파라미터명과 객체의 setter명이 일치하면 객체의 그 필드에 setting됨(객체에 기본 생성자와 setter가 있어야 함)
//		//        id        ->     setId
//		System.out.println("id4 : " + m.getId());
//		System.out.println("pwd4 : " + m.getPwd());
//	}
	
	// 5. @ModelAttribute 생략 -> 지양
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(Member m) {
//		System.out.println("id5 : " + m.getId());
//		System.out.println("pwd5 : " + m.getPwd());
//	}
	
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(@ModelAttribute Member m, HttpSession session) {
		
		// 이전 사용했던 new MemberService()해서 객체를 만드는 방식은 주도권이 나한테 있으며 결합도가 높아짐
		// 결합도가 높다는 것을 확인할 수 있는 두 가지는 1. 클래스 명 변경에 직접적인 영향을 받음, 2. 매 요청마다 새로운 service객체 생성
		// MemberService mService = new MemberService();
		// System.out.println(mService);
		// 여기서 MemberService mService = new MemberService();로 객체 생성했을 때
		// com.kh.spring.member.model.service.MemberService@669e740e
		// com.kh.spring.member.model.service.MemberService@6fa508b4
		// 요청 들어갈 때마다 객체 주소값 바뀌는 것 확인 가능 -> 결합도 높음
		// MemberService 클래스명 변경시 여기서 해당 클래스 찾지 못해 에러 뜨는것 확인 가능 -> 결합도 높음
		
		// 맨 위에서 MemberService객체 생성 코드 필드로 작성
		// -> 요청 여러번 들어와도 객체 주소값 동일
		// com.kh.spring.member.model.service.MemberService@52b3c12d
		// com.kh.spring.member.model.service.MemberService@52b3c12d
		// 클래스명 변경시는 에러 여전함 
		// 해결 방법 : MemberService인터페이스 만들어 implements하고 
		// MemberSerivceImpl클래스에서 @Service어노테이션을 통해 객체 관리를 framework가 하도록 시킴(프레임워크가 객체 생성)
		// 그리고 MemberController 필드 쪽에 아래 작성 -> 클래스명 변경에 영향x, 요청 여러 번 들어와도 객체 주소값 동일 -> 결합도 낮음
		// @Autowired 
		// private MemberService mService; 
//		com.kh.spring.member.model.service.MemberServiceImpl@727332a5
//		com.kh.spring.member.model.service.MemberServiceImpl@727332a5
		
//		Member loginMember = mService.memberLogin(m); // 인터페이스의 메소드 호출한 것, MemberServiceImpl클래스에서 추상화 메소드를 오버라이딩(재작성) 강제로 하게 됨
//	
//		if(loginMember != null) {
//			session.setAttribute("loginUser", loginMember);
//		} else {
//			
//		}
		
	/*************** 전달하고자 하는 데이터가 있을 경우에 대한 방법 ***************/
	// 1. Model객체 사용
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public String login(@ModelAttribute Member m, HttpSession session, Model model) {
//	// 반환값 String -> Page이름 리턴해야돼서
//		
//		Member loginMember = mService.memberLogin(m); // 인터페이스의 메소드 호출한 것, MemberServiceImpl클래스에서 추상화 메소드를 오버라이딩(재작성) 강제로 하게 됨
//		
//		// Spring에서는 Model의 addAttribute메소드를 이용하여 view에 결과값 전달 가능
//		// Jsp/Servlet에서 request.setAttribute와 같은 역할
//		if(loginMember != null) {
//			session.setAttribute("loginUser", loginMember);
////			return "../home"; // member-contex.xml 수정하지 않고 여기서 상위폴더를 가리키면 됨(상대적 위치)
//			// 이건 forward방식이라 페이지 이동해도 url이 바뀌지 않음 home.jsp페이지로 갔는데 url이 login.me
//			return "redirect:home.do"; // redirect방식으로 페이지 이동(url변경됨) <- sendRedirect와 같은 역할
//							           // ViewResolver가 home.do url로 이동시킴(새로운 요청)
//		} else {
//			model.addAttribute("msg", "로그인에 실패하였습니다.");
//			return "../common/errorPage";
//		}
//	}
	
	// 2. ModelAndView객체 사용 : 모델(값)과 뷰를 같이 넘김, 반환을 ModelAndView로
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public ModelAndView login(@ModelAttribute Member m, HttpSession session, ModelAndView mv) {
//	// 반환값 ModelAndView -> 모델과 뷰 같이 넘기기 위해 사용 (forward방식)
//		
//		Member loginMember = mService.memberLogin(m);
//		
//		if(loginMember != null) {
//			session.setAttribute("loginUser", loginMember);
//			mv.setViewName("redirect:home.do"); // redirect방식(request에 담아 보내는 값 없음, 페이지 이동만 하면 됨)
//		} else {
//			mv.addObject("msg", "로그인에 실패하였습니다.");
//			mv.setViewName("../common/errorPage"); // forward방식
//		}
//		
//		return mv;
//	}
	
	// 3. session에 저장할 때 @SessionAttributes 사용
	// model객체에 attribute가 추가될 때, 자동으로 추가된 키 값을 찾아 세션에 등록(세션에 등록하려고 하는 것과 일치하면)
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public String login(@ModelAttribute Member m, Model model) {
//	// 반환값 String -> session저장 위해 ModelAndView가 아닌 Model을 사용해야되기 때문에
//		
//		Member loginMember = mService.memberLogin(m);
//		
//		if(loginMember != null) {
//			model.addAttribute("loginUser", loginMember);
//			return "redirect:home.do";
//		} else {
//			throw new MemberException("로그인에 실패하였습니다.");
//		}
//	}
	
	@RequestMapping("logout.me")
	public String logout(SessionStatus session) {
//		session.invalidate();
		// @SessionAttributes("loginUser")로 인해 invalidate() 안 통함
		// @SessionAttributes는 invalidate() 안 통하기 때문에 그에 맞는 방식 이용
		session.setComplete();
		
		return "redirect:home.do";
	}
	
	@RequestMapping("enrollView.me") // post인지 get방식인지 안 적어줘도 알아서 인식
	public String enrollView() {
		logger.debug("회원등록페이지");
		return "memberJoin";
	}
	
	@RequestMapping("minsert.me")
	public String insertMember(@ModelAttribute Member m, 
			  				   @RequestParam("post") String post,
							   @RequestParam("address1") String address1,
							   @RequestParam("address2") String address2) {
	// Spring에서 return타입 기본은 String으로	
		
//		System.out.println(address1);
//		System.out.println(address1.length());
		// 값이 null로 들어오면 ""로 들어옴, 길이는 0
		
		m.setAddress(post + "/" + address1 + "/" + address2); // "/"로 구분해서 저장
		
		// bcrypt 암호화 방식 : 스프링 시큐리티 모듈에서 제공
		//		암호화 + 랜덤한 salt값
		String encPwd = bcrypt.encode(m.getPwd()); // rowPassword 즉 평문이었던 암호 집어넣으면 암호화해줌
		m.setPwd(encPwd); // 암호화된 비밀번호를 저장
		
		int result = mService.insertMember(m);
		
		if (result > 0) {
			return "redirect:home.do";
		} else {
			throw new MemberException("회원 가입에 실패하였습니다.");
		}
	}
	
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String login(@ModelAttribute Member m, Model model) {
	// 반환값 String -> session저장 위해 ModelAndView가 아닌 Model을 사용해야되기 때문에
		
		// 내 코드
//		String rowPwd = m.getPwd(); // 평문 암호
//		String encPwd = mService.getEncPwd(m.getId()); // 암호화된 암호 select해옴
//		
//		if (encPwd != null && bcrypt.matches(rowPwd, encPwd)) { // 평문 암호와 암호화된 암호가 매치된다면
//			m.setPwd(encPwd); // m의 평문암호를 암호화된 암호로 변경한 후 loginMember select해옴
//			Member loginMember = mService.memberLogin(m); 
//			
//			if(loginMember != null) {
//				model.addAttribute("loginUser", loginMember);
//				return "redirect:home.do";
//			} else {
//				throw new MemberException("로그인에 실패하였습니다."); // select 못한 경우(해당 id 없거나 비활 상태 or DB오류)
//			}
//		} else {
//			throw new MemberException("로그인에 실패하였습니다."); // select 못한 경우(해당 id 없거나 비밀번호가 일치하지 않는 경우 or DB오류)
//		}
		
		// 수업 코드
		// memberLogin 쿼리에서 where pwd 조건 제거함
		Member loginMember = mService.memberLogin(m);
		if (loginMember != null && bcrypt.matches(m.getPwd(), loginMember.getPwd())) { // 평문 암호와 암호화된 암호가 같은지 확인해주는 메소드
			model.addAttribute("loginUser", loginMember);
			// logger.info(loginMember.getId());
			return "redirect:home.do";
		} else {
			throw new MemberException("로그인에 실패하였습니다."); // select 실패한 경우(해당 id 없거나 비활 상태 or DB오류) & 비밀번호가 일치하지 않는 경우
		}
	}
	
	@RequestMapping("myinfo.me")
	public String myInfoView() {
		return "mypage";
	}
	
	@RequestMapping("mupdateView.me")
	public String updateFormView() {
		return "memberUpdateForm";
	}
	
	@RequestMapping("mupdate.me")
	public String updateMember(@ModelAttribute Member m, 
							   @RequestParam("post") String post,
							   @RequestParam("address1") String address1,
							   @RequestParam("address2") String address2,
							   Model model) {
		m.setAddress(post + "/" + address1 + "/" + address2);
		
		int result = mService.updateMember(m);
		
		if (result > 0) {
			Member loginUser = mService.memberLogin(m);
			model.addAttribute("loginUser", loginUser); 
			// model에 add하면 알아서 session에 등록됨 <- 클래스 이름 위 어노테이션으로 loginUser일시 session에 등록되게 함 @SessionAttributes("loginUser")
			return "redirect:myinfo.me";
		} else {
			throw new MemberException("회원정보 수정에 실패하였습니다.");
		}
	}
	
	@RequestMapping("mpwdUpdateView.me")
	public String updatePwdFormView() {
		return "memberPwdupdateForm";
	}
	
	@RequestMapping("mPwdUpdate.me")
	public String updatePwd(@RequestParam("pwd") String oldPwd,
							@RequestParam("newPwd1") String newPwd,
							/* HttpSession session, */ Model model) {
		
//		Member m = (Member)session.getAttribute("loginUser");
		Member m = (Member)model.getAttribute("loginUser");
		if (bcrypt.matches(oldPwd, m.getPwd())) {
			String encPwd = bcrypt.encode(newPwd);
			m.setPwd(encPwd);
			int result = mService.updatePwd(m);
			
			if (result > 0) {
				model.addAttribute("loginUser", m);
				return "mypage";
			} else {
				throw new MemberException("비밀번호 수정에 실패하였습니다."); // 수정에 실패한 경우(DB 오류)
			}
			
		} else {
			throw new MemberException("비밀번호 수정에 실패하였습니다."); // 현재비밀번호를 잘못 적은 경우(알럿창으로 대신?)
		}
		
	}
	
//	@RequestMapping("mPwdUpdate.me") // 수업 코드
//	public String updatePwd(@RequestParam("pwd") String oldPwd,
//							@RequestParam("newPwd1") String newPwd,
//							/* HttpSession session, */ Model model) {
//		
//		Member m = (Member)model.getAttribute("loginUser");
//		String encPwd = bcrypt.encode(newPwd);
//		
//		int result = 0;
//		if (bcrypt.matches(oldPwd, m.getPwd())) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("id", m.getId());
//			map.put("newPwd", encPwd);
//			
//			result = mService.updatePassword(map);
//		} 
//		
//		if (result > 0) {
//			m.setPwd(encPwd);
//			model.addAttribute("loginUser", m);
//			return "mypage";
//		} else {
//			throw new MemberException("비밀번호 수정에 실패하였습니다."); // 수정에 실패한 경우(DB 오류), 비밀번호가 일치하지 않은 경우
//		}
//	}
	
	// String 리턴 -> 내 방식
//	@RequestMapping("dupId.me")
//	@ResponseBody
//	public String duplicateId(@RequestParam("id") String id) {
//		int result = mService.duplicateId(id);
//		
//		return String.valueOf(result); // String.valueOf(int값) : int를 String으로 변환
//	}
	
	// PrintWriter 이용해서 값 보내주기 -> 수업 방식
	@RequestMapping("dupId.me")
	public void duplicateId(@RequestParam("id") String id, HttpServletResponse response) {
		String result = mService.duplicateId(id) == 0 ? "NoDup" : "Dup";
		
		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}