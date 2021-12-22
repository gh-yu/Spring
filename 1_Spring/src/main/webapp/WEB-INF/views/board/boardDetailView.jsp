<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	#boardDetailTable{width: 800px; margin: auto; border-collapse: collapse; border-left: hidden; border-right: hidden;}
	#boardDetailTable tr td{padding: 5px;}
	.replyTable{margin: auto; width: 500px;}
</style>
</head>
<body>
	<c:import url="../common/menubar.jsp"/>
	
	<h1 align="center">${ board.boardId }번 글 상세보기</h1>
	
	<table border="1" id="boardDetailTable">
		<tr>
			<th>번호</th>
			<td>${ board.boardId }</td>
		</tr>
		<tr>
			<th>제목</th>
			<td>${ board.boardTitle }</td>
		</tr>
		<tr>
			<th>작성자</th>
			<td>${ board.nickName }</td>
		</tr>
		<tr>
			<th>작성날짜</th>
			<td>${ board.boardCreateDate }</td>
		</tr>
		<tr>
			<th>내용</th>
			<%-- <td>${ board.boardContent }</td> --%>
			<!-- 
				이렇게만 두면 엔터가 먹지 않음. 
				DB에는 엔터가 \r\n으로 들어가서 이를 치환해주는 작업 필요
			-->
			
			<% pageContext.setAttribute("newLineChar", "\r\n"); %> <!-- \r\n 말고 그냥 \n도, \r도 가능하다 -->
			<td>${ fn:replace(board.boardContent, newLineChar, "<br>") }</td>
		</tr>
		
		<c:if test="${ !empty board.originalFileName }">
			<tr>
				<th>첨부파일</th>
				<td>
					<a href="${ contextPath }/resources/buploadFiles/${ board.renameFileName }" download="${ board.originalFileName }">${ board.originalFileName }</a>
					<!-- a태그 안에서 다운로드 받을 것이 있을 때 쓰는 속성 download, 얘는 download="fileName" 이라고 해서 fileName을 지정해줄 수 있다. -->
				</td>
			</tr>
		</c:if>
		
		<c:url var="bupView" value="bupView.bo">
			<c:param name="bId" value="${ board.boardId }"/>
			<c:param name="page" value="${ page }"/>
		</c:url>
		<c:url var="bdelete" value="bdelete.bo">
			<c:param name="bId" value="${ board.boardId }"/>
		</c:url>
		<c:url var="blist" value="blist.bo">
			<c:param name="page" value="${ page }"/>
		</c:url>
		
		<c:if test="${ loginUser.id eq board.boardWriter }">
		<tr>
			<td colspan="2" align="center">
				<button onclick="location.href='${ bupView }'">수정하기</button>
				<form action="bdelete.bo" method="post" style="display: inline-block;">
				 <!-- url 고쳐서 삭제 못하도록 post방식으로 보냄 -->
					<input type="hidden" name="bId" value="${ board.boardId }">
					<button onclick="return confirm('정말 삭제하시겠습니까?')">삭제하기</button>
				</form>
				<%-- <button onclick="location.href='${ bdelete }'">삭제하기</button> --%>
			</td>
		</tr>
		</c:if>
		
	</table>
	
	<p align="center">
		<button onclick="location.href='home.do'">시작 페이지로 이동</button>
		<button onclick="location.href='${ blist }'">목록 보기로 이동</button>
	</p>
<!-- 	<script>
		function deleteBoard() {
			$('#boardDetailForm').attr('action', 'bdelete.bo');
			$('#boardDetailForm').submit();
		}
	</script> -->
	
	<br><br>
	
	<table class="replyTable">
		<tr>
			<td><textarea cols="55" rows="3" id="rContent"></textarea></td>
			<td><button id="rSubmit">등록하기</button>
		</tr>
	</table>
	<table class="replyTable" id="rtb">
		<thead>
			<tr><td colspan="2"><b id="rCount"></b></td></tr>
		</thead>
		<tbody></tbody>
	</table>
	
	<script>
		$('#rSubmit').click(function(){
			var rContent = $('#rContent').val();
			var refBoardId = ${board.boardId};
			
			$.ajax({
				url: 'addReply.bo',
				data: {replyContent:rContent, refBoardId:refBoardId},
				success: function(data) {
					console.log(data);
				},
				error: function(data) {
					console.log(data);
				}
			})
		});
	</script>
</body>
</html>