package com.claridy.common.mechanism.facase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

public class JasperHelper {
	public static final String PRINT_TYPE = "print";
	public static final String PDF_TYPE = "pdf";
	public static final String EXCEL_TYPE = "excel";
	public static final String HTML_TYPE = "html";
	public static final String WORD_TYPE = "word";

	public static void prepareReport(JasperReport jasperReport, String type) {
	}
	//export word
	public static void exportWord(JasperPrint jasperPrint,
			String defaultFilename, HttpServletRequest request,
			HttpServletResponse response) throws IOException, JRException {
	            response.setContentType("application/msword;charset=utf-8");  
	            String defaultname=null;  
	             if(defaultFilename.trim()!=null&&defaultFilename!=null){  
	                defaultname=defaultFilename+".doc";  
	             }else{  
	                defaultname="export.doc";  
	             }  
	            String fileName = new String(defaultname.getBytes("GBK"), "utf-8");  
	            response.setHeader("Content-disposition", "attachment; filename="  
	              + fileName);  
	            JRExporter exporter = new JRRtfExporter();  
	            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
	            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response  
	              .getOutputStream());  
	  
	            exporter.exportReport();  
	}
	// export PDF
	public static void exportPDF(JasperPrint jasperPrint,
			String defaultFilename, HttpServletRequest request,
			HttpServletResponse response) throws IOException, JRException {
		response.setContentType("application/pdf");
		String defaultname = null;
		if (defaultFilename.trim() != null && defaultFilename != null) {
			defaultname = defaultFilename + ".pdf";
		} else {
			defaultname = "export.pdf";
		}
		String fileName = new String(defaultname.getBytes("GBK"), "utf-8");
		response.setHeader("Content-disposition", "attachment; filename="
				+ fileName);
		ServletOutputStream ouputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	// export excel
	public static void exportExcel(JasperPrint jasperPrint,
			String defaultFilename, HttpServletRequest request,
			HttpServletResponse response) throws IOException, JRException {
		/*
		 * 設置頭資訊
		 */
		response.setContentType("application/vnd.ms-excel");
		String defaultname = null;
		if (defaultFilename.trim() != null && defaultFilename != null) {
			defaultname = defaultFilename + ".xls";
		} else {
			defaultname = "export.xls";
		}
		String fileName = new String(defaultname.getBytes("gbk"), "utf-8");
		response.setHeader("Content-disposition", "attachment; filename="
				+ fileName);
		ServletOutputStream ouputStream = response.getOutputStream();
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
		exporter.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
				Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				Boolean.FALSE);
		exporter.exportReport();
		ouputStream.flush();
		ouputStream.close();
	}

	public static void exportmain(HttpServletRequest request,
			HttpServletResponse response, String exportType, String jaspername,
			List lists, String defaultFilename) {
		String root_path = request.getSession().getServletContext()
				.getRealPath("/");
		root_path = root_path.replace('\\', '/');
		String reportFilePath = root_path + "report/" + jaspername;
		File file = new File(reportFilePath);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		export(lists, exportType, defaultFilename, is, request, response);
	}

	private static void export(Collection datas, String type,
			String defaultFilename, InputStream is, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
			prepareReport(jasperReport, type);
			JRDataSource ds = new JRBeanCollectionDataSource(datas, false);
			Map parameters = new HashMap();
			// parameters.put("wheresql", " and status='3'");
			/*
			 * parameters.put("wheresql", ""); String diver =
			 * "oracle.jdbc.driver.OracleDriver"; String url =
			 * "jdbc:oracle:thin:@192.168.1.156:1521:orcl"; String username =
			 * "qqwcrm0625"; String password = "qqwcrm"; ReportDataSource
			 * datasource = new ReportDataSource(); datasource.setDiver(diver);
			 * datasource.setUrl(url); datasource.setUsername(username);
			 * datasource.setPassword(password); Connection
			 * con=getConnection(datasource);
			 */
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, ds);
			if (EXCEL_TYPE.equals(type)) {
				exportExcel(jasperPrint, defaultFilename, request, response);
			} else if (WORD_TYPE.equals(type)) {
				exportWord(jasperPrint, defaultFilename, request, response);
			}else if(PDF_TYPE.equals(type)){
				exportPDF(jasperPrint, defaultFilename, request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}