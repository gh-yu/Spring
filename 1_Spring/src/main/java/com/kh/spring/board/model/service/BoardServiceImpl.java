package com.kh.spring.board.model.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.BoardDAO;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;

@Service("bService")
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardDAO bDAO;
	
	@Autowired
	private SqlSessionTemplate sqlSession;	

	@Override
	public int getListCount() {
		int listCount = bDAO.getListCount(sqlSession);
		return listCount;
	}

	@Override
	public ArrayList<Board> selectList(PageInfo pi) {
		ArrayList<Board> list = bDAO.selectList(sqlSession, pi);
		return list;
	}

	@Override
	public int insertBoard(Board b) {
		int result = bDAO.insertBoard(sqlSession, b);
		return result;
	}

	@Override
	@Transactional
	public Board selectBoard(int bId, String upd) {
	
		int result = 0;
		if (upd == null) {
			result = bDAO.updateCount(sqlSession, bId); // readCount
		}

		Board b = null;
		if (result > 0 || upd.equals("Y")) {
			b = bDAO.selectBoard(sqlSession, bId);
		}

		return b;
	}

	@Override
	public int updateBoard(Board b) {
		int result = bDAO.updateBoard(sqlSession, b);
		return result;
	}

	@Override
	public int deleteBoard(int bId) {
		int result = bDAO.deleteBoard(sqlSession, bId);
		return result;
	}

	@Override
	public int insertReply(Reply r) {
		return bDAO.insertReply(sqlSession, r);
	}

	@Override
	public ArrayList<Reply> selectReplyList(int boardId) {
		return bDAO.selectReplyList(sqlSession, boardId);
	}

	@Override
	public ArrayList<Board> getTopList() {
		return bDAO.getTopList(sqlSession);
	}
}
