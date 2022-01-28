package com.kh.spring.board.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;

@Repository("bDAO")
public class BoardDAO {
	
	public int getListCount(SqlSessionTemplate sqlSession) {
		int listCount = sqlSession.selectOne("boardMapper.getListCount");
			
		return listCount;
	}

	public ArrayList<Board> selectList(SqlSessionTemplate sqlSession, PageInfo pi) {
		
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());

		ArrayList<Board> list = (ArrayList)sqlSession.selectList("boardMapper.selectList", null, rowBounds);
		
		return list;
	}

	public int insertBoard(SqlSessionTemplate sqlSession, Board b) {
//		int result = sqlSession.insert("boardMapper.insertBoard", b);
//		return result;
		return sqlSession.insert("boardMapper.insertBoard", b);
	}

	public int updateCount(SqlSessionTemplate sqlSession, int bId) {
		return sqlSession.update("boardMapper.updateCount", bId);
	}	
	
	public Board selectBoard(SqlSessionTemplate sqlSession, int bId) {
		Board b = sqlSession.selectOne("boardMapper.selectBoard", bId);
		return b;
	}
	
	public int updateBoard(SqlSessionTemplate sqlSession, Board b) {
		return sqlSession.update("boardMapper.updateBoard", b);
	}

	public int deleteBoard(SqlSessionTemplate sqlSession, int bId) {
		return sqlSession.update("boardMapper.deleteBoard", bId);
	}

	public int insertReply(SqlSessionTemplate sqlSession, Reply r) {
		return sqlSession.insert("boardMapper.insertReply", r);
	}

	public ArrayList<Reply> selectReplyList(SqlSessionTemplate sqlSession, int boardId) {
		return (ArrayList)sqlSession.selectList("boardMapper.selectReplyList", boardId);
	}

	public ArrayList<Board> getTopList(SqlSessionTemplate sqlSession) {
		return (ArrayList)sqlSession.selectList("boardMapper.getTopList");
	}
}
