<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<style>#tb{margin: auto; width: 700px;}</style>
</head>
<body>
<!-- <h1>
	Hello world!  
</h1> -->
 	<c:import url="common/menubar.jsp"/>
 	
	<script>
		$(function(){
			var msg = '${msg}';
			if (msg != '') {
				alert(msg);
			}
		});
	</script>
	
 	<h1 align="center">게시글 TOP5 목록</h1>
 	<table id="tb" border="1">
 		<thead>
 			<tr>
 				<th>번호</th>
 				<th>제목</th>
 				<th>작성자</th>
 				<th>날짜</th>
 				<th>조회수</th>
 				<th>첨부파일</th>
 			</tr>	
 		</thead>
 		<tbody align="center"></tbody>
 	</table>
 	<script>
 		function topList() { // 조회수 기준 상위 5개 게시글 가져옴
 			$.ajax({
 				url: 'topList.bo',
 				dataType: 'json',
 				success: function(data){
 					console.log(data);
 					
					$tableBody = $('#tb tbody');
					$tableBody.html(''); // 중복되지 않게 안의 내용 지웠다 다시 채움
					
					var $tr;
					
					if (data.length > 0) {
						for(var i in data) {
							$tr = $('<tr>');
							// $bId = $('<td width="20">').text(data[i].boardId));
							$tr.append($('<td width="50">').text(data[i].boardId));
							$tr.append($('<td>').text(data[i].boardTitle));
							$tr.append($('<td width="100">').text(data[i].nickName));
							$tr.append($('<td width="100">').text(data[i].boardCreateDate));
							$tr.append($('<td width="70">').text(data[i].boardCount));
							$tr.append($('<td width="100">').text(data[i].originalFileName == null ? '' : '◎'));
							
							// $attachment = $('<td width="100">');
							// $attachment.text(data[i].originalFileName == null ? '' : '◎');
							
// 							if (data[i].originalFileName != null) {
// 								$attachment = $('<td width="100">').text('◎');
// 							} else {
// 								$attachment = $('<td width="100">').text('');
// 							} 
							
							//$tr.append($bId).append($title);
							
							$tableBody.append($tr);
						}
					} else {
						$tr = $('<tr>');
						$tr.append($('<td colspan="6">').text("등록된 게시글이 없습니다."));
						
						$tableBody.append($tr);
					}
 				},
 				error: function(data){
 					console.log(data);
 				}
 			});
 		}
 		
 		$(function(){
 			topList();
 			
 			setInterval(function(){
 				topList();
 			}, 5000);
 		});
 	</script>
 	
<%-- <P>  The time on the server is ${serverTime}. </P>  --%>
</body>
</html>
