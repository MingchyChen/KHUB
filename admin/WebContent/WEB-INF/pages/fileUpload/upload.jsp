<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
</head>
<body>
	<form id='fForm' class="form-actions form-horizontal"
		action="Admin/fileUpload/adResFileUpload2"
		encType="multipart/form-data" target="uploadf" method="post">
		<div class="control-group">
			<label class="control-label">上传文件:</label>
			<div class="controls">
				<input type="file" name="file" style="width: 550">

			</div>
			<label class="control-label">上传进度:</label>
			<div class="controls">
				<div class="progress progress-success progress-striped"
					style="width: 50%">
					<div id='proBar' class="bar"
						style="width: 0%; background: #F00; color: #FFF"></div>
				</div>
			</div>
		</div>

		<div class="control-group">
			<div class="controls">
				<button type="button" id="subbut" class="btn">上传</button>
			</div>
		</div>
	</form>
	<iframe name="uploadf" style="display: none"></iframe>
</body>
</html>
<script>  
$(document).ready(function(){  
    $('#subbut').bind('click',  
            function(){  
    			$('#fForm').submit();
                var eventFun = function(){  
                    $.ajax({  
                        type: 'GET',  
                        url: 'Admin/fileUpload/process.json',  
                        data: {},  
                        dataType: 'json',  
                        success : function(data){  
                                $('#proBar').css('width',data.rate+''+'%');  
                                $('#proBar').empty();  
                                $('#proBar').append(data.show);   
                                if(data.rate == 100){
                                	alert("上传成功");
                                    window.clearInterval(intId);
                                    $('div#div_upload2').dialog('close'); 
                                }
                        }
                    });
                };
                var intId = window.setInterval(eventFun,500);    
    });  
});  
</script>

