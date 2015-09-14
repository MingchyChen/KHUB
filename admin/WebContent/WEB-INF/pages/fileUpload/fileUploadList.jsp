<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script type="text/javascript">
	$(function() {
		$("#fileListForm input:submit").attr("disabled",$("#htmlCnt_update").attr("disabled"));
		
		$("#fileListForm input:submit").bind("click",function(event){
			$.ajax({
				url:'request/fileUpload/deleteFile',
				type:'GET',
				data: "file_pk="+this.className,
				datatype:'html',
				async:false,
				success:function(htmldata) {
					$("#div_fileList").load('request/fileUpload/htmlCntFileList/' + $("#questId").val()+"/"+$("#cntid").val());
				}
			});
			return false;
		});
		
		$("#fileListForm a").bind("click",function(event){
			return false;
		});
	});
</script>

<form:form id="fileListForm">
	<table>
		<c:forEach items="${sys_fileList}" var="item" varStatus="status">
			<tr>
				<td><input type="hidden" id="pkId" name="pkId"
					value="${item.file_pk }"> <c:choose>
						<c:when
							test="${item.file_type=='gif' || item.file_type=='jpg' || item.file_type=='png' || item.file_type=='bmp' }">
							<img width="80" src="${item.file_name}">
						</c:when>
						<c:when
							test="${item.file_type=='doc' || item.file_type=='docx' || item.file_type=='dotx' }">
							<img width="80" src="images/common/file_icon/doc.png">
						</c:when>
						<c:when test="${item.file_type=='pdf'}">
							<img width="80" src="images/common/file_icon/pdf.png">
						</c:when>
						<c:when test="${item.file_type=='ppt' || item.file_type=='pptx'}">
							<img width="80" src="images/common/file_icon/ppt.png">
						</c:when>
						<c:when test="${item.file_type=='xls' || item.file_type=='xlsx'}">
							<img width="80" src="images/common/file_icon/xls.png">
						</c:when>
						<c:when test="${item.file_type=='txt'}">
							<img width="80" src="images/common/file_icon/txt.png">
						</c:when>
						<c:otherwise>
							<img width="80" src="images/common/file_icon/file.png">
						</c:otherwise>

					</c:choose></td>
				<td>
					<!-- <a href="${item.file_name}"> --> ${item.display_file_name} <!--</a>-->
				</td>
				<td>${item.createddate}</td>
				<td><input type="submit" value="刪除" class="${item.file_pk}">
				</td>
			</tr>
		</c:forEach>
	</table>
</form:form>
