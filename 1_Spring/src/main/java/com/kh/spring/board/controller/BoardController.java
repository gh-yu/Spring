package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.kh.spring.board.common.Pagination;
import com.kh.spring.board.model.exception.BoardException;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.member.model.vo.Member;

@Controller // 어노테이션으로 컨트롤러 등록
public class BoardController {
	
	@Autowired
	private BoardService bService;
	
	@RequestMapping("blist.bo")
	public /*String*/ModelAndView boardList(@RequestParam(value="page", required=false) Integer page, 
											/*Model model*/ ModelAndView mv) {
		
		int currentPage = 1;
		if(page != null) {
			currentPage = page;
		}
		
		int listCount = bService.getListCount();
	
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount);
		
		ArrayList<Board> list = bService.selectList(pi);
		
		if (list != null ) {
			// 1) 반환 값이 String인 상태에서 view에 데이터 전달
//			model.addAttribute("list", list);
//			model.addAttribute("pi", pi);
			
			// 2) 반환 값을 ModelAndView로 변경한 상태에서 view에 데이터 전달
			mv.addObject("pi", pi);
			mv.addObject("list", list);
			mv.setViewName("boardListView");
		} else {
			throw new BoardException("게시글 리스트 조회에 실패하였습니다.");
		}
		
//		return "boardListView";
		return mv;
	}
	
	@RequestMapping("binsertView.bo")
	public String boardInsertView() {
		return "boardInsertForm";
	}
	
	@RequestMapping("binsert.bo")
	public String boardInsert(@ModelAttribute Board b, @RequestParam("uploadFile") MultipartFile uploadFile,
							  HttpServletRequest request) {
		System.out.println(b);
		System.out.println(uploadFile);
		System.out.println(uploadFile.getOriginalFilename());
		
		if (uploadFile != null && !uploadFile.isEmpty()) {
			String renameFileName = saveFile(uploadFile, request); // 업로드파일 폴더에 저장하려면 request가 필요
			
			if (renameFileName != null) {
				b.setOriginalFileName(uploadFile.getOriginalFilename());
				b.setRenameFileName(renameFileName);
			}
		}
		
		int result = bService.insertBoard(b);
		
		if (result > 0) {
			return "redirect:blist.bo";
		} else {
			throw new BoardException("게시글 등록에 실패하였습니다.");
		}
	}
	
	public String saveFile(MultipartFile file, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		// getServletContext() -> application에 접근
		// resources : webapp 밑의 resources
		String savePath = root + "/buploadFiles";
		
		File folder = new File(savePath);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String originFileName = file.getOriginalFilename();
		String renameFileName 
			= sdf.format(new Date(System.currentTimeMillis())) + "." + originFileName.substring(originFileName.indexOf(".") + 1);
		//                                             현재시간        + . + 확장자
		 
		String renamePath = folder + "/" + renameFileName;
		try {
			file.transferTo(new File(renamePath));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return renameFileName;
	}
	
	@RequestMapping("bdetail.bo")
	public /*String*/ModelAndView boardDetail(@RequestParam("bId") int bId, @RequestParam("page") int page, 
											  @RequestParam(value="upd", required=false) String upd, /*Model model*/ModelAndView mv) {
		Board b = bService.selectBoard(bId, upd);
		
		if (b != null) {
//			model.addAttribute("board", b);
//			model.addAttribute("page", page);
			mv.addObject("board", b).addObject("page", page); // 둘 다반환값이 ModelAndView이기 때문에 메소드 체이닝 가능
			mv.setViewName("boardDetailView");
		} else {
			throw new BoardException("게시글 상세 조회에 실패하였습니다.");
		}
		
		return mv; // 반환값을 String으로 하는 방식, ModelAndView로 하는 방식 둘 다 써도 됨
	}
	
	@RequestMapping("bupView.bo")	
	public String boardUpdateView(@RequestParam("bId") int bId, @RequestParam("page") int page, Model model) {
		
		String upd = "Y";
		Board b = bService.selectBoard(bId, upd); // 수정이기 때문에 조회수 안 올라가게 하는 조치 필요(나중)
		
		model.addAttribute("board", b).addAttribute("page", page);
		
		return "boardUpdateForm";
	}
	
	@RequestMapping("bupdate.bo")
	public String updateBoard(@ModelAttribute Board b, @RequestParam("page") int page,
							  @RequestParam("reloadFile") MultipartFile reloadFile, HttpServletRequest request) {
		// System.out.println(reloadFile.getOriginalFilename().length()); // length가 0이면 들어온 파일 없는 것 (reloadFile.isEmpty()로도 확인 가능)
		if (reloadFile != null && !reloadFile.isEmpty()) { // 수정할 파일 존재
			// 수정할 파일 존재 + 기존 파일 존재 = 기존 파일 삭제
			if (b.getRenameFileName() != null) {
				deleteFile(b.getRenameFileName(), request);
			}
			
			String renameFileName = saveFile(reloadFile, request);
			
			if (renameFileName != null) {
				b.setOriginalFileName(reloadFile.getOriginalFilename());
				b.setRenameFileName(renameFileName);
			}
		}
		
		int result = bService.updateBoard(b);
		
		if (result > 0) {
			// model.addAttribute("page", page); // model의 속성에 추가한 후 페이지이름만 리턴해줘도 됨
			return "redirect:bdetail.bo?bId=" + b.getBoardId() + "&page=" + page + "&upd=Y";
		} else {
			throw new BoardException("게시글 수정에 실패했습니다.");
		}
	}

	private void deleteFile(String fileName, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savePath = root + "/buploadFiles";
		
		File f = new File(savePath + "/" + fileName);
		
		if (f.exists()) {
			f.delete();
		}
	}
	
	@RequestMapping("bdelete.bo")
	public String deleteBoard(@RequestParam("bId") int bId, HttpServletRequest request) {
		
		Board b = bService.selectBoard(bId, "Y");
		if (b.getOriginalFileName() != null) { // 파일 있으면 파일도 삭제
			deleteFile(b.getRenameFileName(), request);
		}
		
		int result = bService.deleteBoard(bId);
		
		if (result <= 0) {
			throw new BoardException("게시글 삭제에 실패했습니다.");
		}
		
		return "redirect:blist.bo";
	}
	
	@RequestMapping("addReply.bo")
	@ResponseBody
	public String insertReply(@ModelAttribute Reply r, HttpSession session) {
//		Member m = (Member)model.getAttribute("loginUser"); // 클래스 이름 위에 @SessionAttributes("loginUser") 추가해야됨
		Member m = (Member)session.getAttribute("loginUser");
		r.setReplyWriter(m.getId());
		
		int result = bService.insertReply(r);
		
		if (result > 0) {
			return "success";
		} else {
			throw new BoardException("댓글 등록에 실패하였습니다.");
		}
	}

	// Json으로 댓글 리스트 보내기
	// web.xml에 forceEncoding true로 설정해줘서 produces 안해도 ajax dataType속성 값을 'json'으로 해놨으면 UTF-8로 인코딩되어 전달됨
//	@RequestMapping(value="rList.bo", produces="application/json; charset=UTF-8") 
//	@ResponseBody
//	public String replyList(@RequestParam("boardId") int boardId) {
//		ArrayList<Reply> list = bService.selectReplyList(boardId);
//		
//		JSONArray jArr = new JSONArray();
//		for (Reply r : list) {
//			JSONObject jObj = new JSONObject();
//			jObj.put("replyId", r.getReplyId());
//			jObj.put("replyContent", r.getReplyContent());
//			jObj.put("refBoardId", r.getRefBoardId());
//			jObj.put("replyWriter", r.getReplyWriter());
//			jObj.put("nickName", r.getNickName());
//			jObj.put("replyCreateDate", r.getReplyCreateDate().toString());
//			// JSON으로는 Date타입을 보내면 error가 나기 때문에 toString해줌(Gson은 상관 없음. 대신 날짜형식 원하는 형식 있으면 포맷 필요)
//			// 보내지긴 보내지는데 ajax에서 success가 아닌 error쪽에 data 찍히면서 parsererror 뜸
//			// JSON은 Date형식을 인식하지 못함
//			jObj.put("replyModifyDate", r.getReplyModifyDate().toString());
//			jObj.put("replyStatus", r.getReplyStatus());
//			
//			jArr.add(jObj);
//		}
//		
//		return jArr.toJSONString();
//	}	
	
	// Gson으로 댓글 리스트 보내기
	@RequestMapping("rList.bo")
	public void getReplyList(@RequestParam("boardId") int boardId, HttpServletResponse response) {
		ArrayList<Reply> list = bService.selectReplyList(boardId);
		
		GsonBuilder gb = new GsonBuilder().setDateFormat("yyyy-MM-dd"); // 원하는 날짜 형식으로 포맷 (메소드 체이닝)
		Gson gson = gb.create();
		try {
			//response.setContentType("application/json; charset=UTF-8");
			// -> web.xml에 forceEncoding true로 설정해줘서 setContentType안해도
			// ajax dataType속성 값을 'json'으로 해놨으면 UTF-8로 인코딩되어 전달됨
			
			gson.toJson(list, response.getWriter());
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("topList.bo")
	public void getTopList(HttpServletResponse response) {
		ArrayList<Board> list = bService.getTopList();
		
		// response.setContentType("application/json; charset=UTF-8"); 
		GsonBuilder gb = new GsonBuilder().setDateFormat("yyyy-MM-dd");
		Gson gson = gb.create();
		try {
			gson.toJson(list, response.getWriter());
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
