<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%int pID = (int)request.getSession(false).getAttribute("pID"); %>
	<h1>Progress<label for="pID"><%=pID %></label> is going on</h1>
	<label>Merge </label>
	<progress id = "progressBarMerge" value = "0" max = "100"></progress>
	<label id = "progressTextMerge">0%</label><br>
	<label>HD </label>
	<progress id = "progressBarHD" value = "0" max = "100"></progress>
	<label id = "progressTextHD">0%</label>
	<div id="downloadForm" style="display:none">
		<h3>MERGE SUCCESSFUL</h3>
		<form action="DownloadServlet" method ="post">
			<input type = "hidden" name = "processID" value = "<%=pID %>">
			<input type = "submit" value = "Download">
		</form>
	</div>
</body>
<script>
    const pid = document.querySelector("label[for='pID']").textContent;
	const progressBarMerge = document.getElementById("progressBarMerge");
	const progressTextMerge = document.getElementById("progressTextMerge");
	const progressBarHD = document.getElementById("progressBarHD");
	const progressTextHD = document.getElementById("progressTextHD");
    const downloadForm = document.getElementById("downloadForm");
    var intervalID = setInterval(()=>{
        fetch("http://localhost:8080/MergeVideo/MergeVideoServlet?pID="+pid)
        .then(response=>{
            return response.json();
        })
        .then(data=>{
            const progressMerge = data.progressMerge;
            const progressHD = data.progressHD;
            progressBarMerge.value = Math.round(progressMerge*100)/100;
            progressBarHD.value = Math.round(progressHD*100)/100;
            progressTextMerge.textContent = ""+progressMerge+"%";
            progressTextHD.textContent = ""+progressHD+"%";
            if (Math.abs(progressMerge-100)<1e-6 && Math.abs(progressHD-100)<1e-6){
                clearInterval(intervalID);
                downloadForm.style = "display:block";
            }
        });
    },500)
</script>
</html>