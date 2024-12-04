<%@page import="java.util.Random"%>
<%@page import="java.util.random.RandomGenerator"%>
<%@page import="model.bean.Account"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	Random ran = new Random();
	Account account = new Account("user"+ran.nextInt(),"lvnminh");
	request.getSession().setAttribute("account", account);
	%>
	<h3>CHOOSE VIDEOS TO MERGE</h3>
	<form id="uploadForm" action = "UploadServlet" method = "post" enctype = "multipart/form-data">
		<input type = "file" name = "filename" multiple><br>
		<input type = "submit" value = "upload">
	</form>
	<div id="progressInfo" style ="display:none">
		<progress id="progressBar" value="0" max = "100"></progress>
		<label id = "progressText">Uploading to server</label>
	</div>
	<div id="mergeForm" style="display:none">
		<h3>UPLOAD SUCCESSFUL</h3>
		<form action="MergeVideoServlet" method ="post">
			<input type = "hidden" id="processID" name="pID">
			<input type = "submit" value = "MERGE">
			
		</form>
	</div>
</body>
<script module>
const uploadForm = document.getElementById('uploadForm');
const progressBar = document.getElementById('progressBar');
const progressText = document.getElementById('progressText');
const progressInfo = document.getElementById('progressInfo');
const mergeForm = document.getElementById('mergeForm');
const processID = document.getElementById('processID');
uploadForm.addEventListener('submit', function (e) {
    e.preventDefault();

    progressInfo.style.display = 'block';

    const formData = new FormData(uploadForm);
    const xhr = new XMLHttpRequest();

    xhr.open('POST', uploadForm.action, true);
    xhr.send(formData);
    // Periodically query the server for upload progress
    const interval = setInterval(() => {
        fetch('http://localhost:8080/MergeVideo/UploadServlet')
            .then(response => response.json())
            .then(data => {
            	let uploaded = data.numFileUploaded;
            	let total = data.totalFileUploaded;
            	let pID = data.processID;
                if (total > 0) {
                    const progress = (uploaded/total) * 100;
                    progressBar.value = progress;
                    progressText.textContent = 'Uploaded: '+uploaded+' of '+total;
                }
                else{
                    progressText.textContent = 'Progressing';
                	
                }
                if (uploaded===total && uploaded){
                    clearInterval(interval);
                    mergeForm.style = "display: block";
                    processID.value = pID;
                    fetch('http://localhost:8080/MergeVideo/UploadServlet/Finish');
                }
            });
    }, 1000);


});
</script>
</html>