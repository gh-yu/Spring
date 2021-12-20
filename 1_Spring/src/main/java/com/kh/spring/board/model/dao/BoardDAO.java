package com.kh.spring.board.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;

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
		int result = sqlSession.insert("boardMapper.insertBoard", b);
		return result;
	}
	
}
