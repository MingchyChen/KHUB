<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script type="text/javascript" src="js/jquery/ajaxfileupload.js"></script>
<script type="text/javascript">
	$(function() {
		$("#timeOutMsg").hide();
		$.ajaxSetup({
			cache : false
		});
	});
	
	function ajaxFileUpload() {
		var timer;
		$("#timeOutMsg").hide();
		timer=setTimeout("showTimeOut()", 30000);
		$.ajaxFileUpload({
			url : 'request/fileUpload/htmlCntFileUpload/' + $("#cntid").val()
					+ "/" + $("#objName").val(),
			secureuri : false,
			fileElementId : 'fileToUpload',
			name : 'fileToUpload',
			dataType : 'json',
			beforeSend : function() {
			},
			complete : function() {
				clearTimeout(timer);
				$("#timeOutMsg").hide();
			},
			success : function(data, status) {
				//$('#adpic').val("document\\sourceFile_1\\"+data.newFileName);
				//$('#img').attr("src","document\\sourceFile_1\\"+data.newFileName);
				$('div#div_upload').dialog('close');
			},
			error : function(data, status, e) {
			}
		});
		return false;
	}

	function showTimeOut() {
		$("#timeOutMsg").show();
	}
</script>

<form:form name="form" action="" method="POST"
	enctype="multipart/form-data">
	<span style="font-size: 12px">文件格式僅限:ppt、pptx、xls、xlsx、doc、docx、pdf、txt、jpg、png、gif、bmp、tiff(*最大允許10MB)</span>
	<span id="timeOutMsg" style="font-size: 12px; color: red">上傳文件失敗，請確定文件大小是否超過10MB</span>
	<input id="fileToUpload" type="file" size="45"
		onchange="checkFileTypeAndSize(this)" name="fileToUpload"
		class="input">
	<button class="button" id="buttonUpload" name="buttonUpload"
		onclick="return ajaxFileUpload();" disabled="disabled">上傳</button>
</form:form>

