<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Home</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<h1 align="center">Spring에서의 Ajax 사용</h1>
	
	<button onclick="location.href='test.do'">테스트</button>
	
	<ol>
		<li>
			서버 쪽 컨트롤러로 값 보내기 <button id="test1">테스트</button>
			<script>
/* 				test1.onclick = function() {
					alert("dd");
				}  */
				
/* 				$("#test1").click(function(){
					alert("dd");
				}); */
				
				$("#test1").on('click', function(){
					$.ajax({
						url: 'test1.do',
						data: {name:'강건강', age:20},
						success: function(data){
							console.log(data);
							if(data == 'ok') { // println으로 보냈을때는 .trim()해줘야함, print로 보내면 trim() 안 해줘도 됨
								alert('전송 성공');
							} else if (data == 'fail') {
								alert('전송 실패');
							}
						},
						error: function(data){
							console.log(data);
						}
					});
				});
			</script>
		</li>
		
		<li>
			컨트롤러에서 리턴하는 JSON객체 받아서 출력하기<button id="test2">테스트</button>
			<div id="d2">
				
			</div>
			<script>
 				$("#test2").click(function(){ 
 					$.ajax({
 						url: 'test2.do',
 						dataType: 'json',
 						success: function(data){
 							console.log(data);
 							// data = JSON.parse(data); // dataType 지정 대신 이렇게 parse메소드 사용해서 String으로 넘어온 값을 파싱할 수 있음
							
							$('#d2').html('번호: ' + data.no
 										+ '<br>제목: ' + data.title
 										// 1. 컨트롤러에서 String을 리턴했을시
 														// 컨트롤러에서 인코딩해서 보냈으면 여기서 디코딩해줘야함
 														// 띄어쓰기가 +로 표시되기 때문에 replace(정규표현식, 바꿀 문자열) 
 										// + '<br>작성자: ' + decodeURIComponent(data.writer.replace(/\+/g, ' ')) 
 										// + '<br>내용: ' + decodeURIComponent(data.content.replaceAll('+', ' ')));  
										// 2. 컨트롤러에서 PrintWriter의 print메소드로 JSON객체 보냈을시
										+ '<br>작성자: ' + data.writer
										+ '<br>내용: ' + data.content);
 						},
						error: function(data){
 							console.log(data);
 						}
 					});
 				});
			</script>
		</li>
		<li>
			컨트롤러에서 리턴하는 JSON배열을 받아서 출력하기<button id="test3">테스트</button>
			<div id="d3">
				
			</div>
			<script>
				$('#test3').on('click', function(){
					$.ajax({
						url: 'test3.do',
						dataType: 'json',
						success: function(data){
							console.log(data);
							var html = ""; // values
							for (var i in data) {
								html += "아이디: " + data[i].userId + ", 비밀번호: " + data[i].userPwd + ", 이름: " + data[i].userName + 
										", 나이: " + data[i].age + ", 이메일: " + data[i].email + ", 폰번호: " + data[i].phone + "<br>" 
							}
							
							$('#d3').html(html);
							// $('#d3').append(html); // append하면 여러번 누르면 계속 붙여짐
						},
						error: function(data){
							console.log(data);
						}
					});
				});
			</script>
		</li>
	</ol>
</body>
</html>
