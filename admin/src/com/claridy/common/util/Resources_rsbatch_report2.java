package com.claridy.common.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

/**
 * 批次匯入導出錯誤資訊
 * @author nj
 *
 */
public class Resources_rsbatch_report2 {

	private int columnWidth=150;//colums的寬度
	private String headName="";//導出報表的表頭
	/**
	 * 結果列數
	 */
	private int columns ;
	/**
	 * 字體大小
	 */
	private int fontsize=10;
	//無參構造函數
	//public Resources_rsbatch_report(){}
	//有參構造函數
	public Resources_rsbatch_report2(String headTextContent){
		headName=headTextContent; 
	}
	
	public JasperReport getJasperReport(String[] titleName,String[] titleValue,int size) throws JRException{
		//創建一個報表設計圖的實例
		JasperDesign jDesign=new JasperDesign();
		//設置報表名稱
		jDesign.setName("Resources_rsbatch_report");
		//設置上邊距
		jDesign.setTopMargin(0);
		//設置左邊距
		jDesign.setLeftMargin(0);
		//設置表頭
		JRDesignBand rTitle=new JRDesignBand();
		//設置表頭的高度
		rTitle.setHeight(60);
		//報表Title
		JRDesignStaticText headText=new JRDesignStaticText();
		//內容
		headText.setText(headName);
		//設置標籤的橫座標
		headText.setX(0);
		//設置標籤的縱座標
		headText.setY(0);
		//設置標籤的高度
		headText.setHeight(60);
		//設置標籤的寬度(根據表頭的長度)
		headText.setWidth(300);
		//字體大小
		headText.setFontSize(10);
		//給字體加粗
		headText.setBold(true);
		//設置內容水準垂直居中
		headText.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
		//設置內容垂直方向居中
		headText.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
		//將設置完的Title放到表頭中
		rTitle.addElement(headText);
		//將表頭資訊添加到設計圖中
		jDesign.setTitle(rTitle);
		
		//循環添加Fields
		for(int i=0;i<titleValue.length;i++){
		//定義fields
		JRDesignField field=new JRDesignField();
		field.setName(titleValue[i]);
		if(i<=(size-1)){
			field.setValueClass(String.class);
		}else{
			field.setValueClass(Number.class);
		}
		jDesign.addField(field);
		}
		//添加表頭欄位
		JRDesignBand columnHeader=new JRDesignBand();
		columnHeader.setHeight(20);
		//循環將表頭欄位添加到title中
		for(int i=0;i<titleName.length;i++){
			JRDesignStaticText columnsText=new JRDesignStaticText();
			//設置內容
			columnsText.setText(titleName[i]);
			//橫座標
			columnsText.setX(i*columnWidth);
			//縱座標
			columnsText.setY(0);
			//寬度
			columnsText.setWidth(columnWidth);
			//高度
			columnsText.setHeight(20);
			//字體大小
			columnsText.setFontSize(10);
			//字體加粗
			columnsText.setBold(true);
			//設置內容水準垂直居中
			columnsText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
			//設置內容垂直方向居中
			columnsText.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
			//設置邊框
			JRLineBox jrLineBox = columnsText.getLineBox();
			jrLineBox.getPen().setLineWidth(1.0f);
			columnHeader.addElement(columnsText);
		}
		//將面板添加到Design對應的模塊
		jDesign.setColumnHeader(columnHeader);
		
		JRDesignBand detail=new JRDesignBand();
		detail.setHeight(20);
		//循環定義TextField
		for(int i=0;i<titleValue.length;i++){
			JRDesignTextField textField=new JRDesignTextField();
			//X軸座標
			textField.setX(i*columnWidth);
			//Y軸座標
			textField.setY(0);
			//寬度
			textField.setWidth(columnWidth);
			//高度
			textField.setHeight(20);
			//字體大小
			textField.setFontSize(10);
			textField.setBlankWhenNull(true);
			//內容自動換行
			textField.setStretchWithOverflow(true);
			//高度統一
			textField.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			//設置邊框
			JRLineBox jrLineBox = textField.getLineBox();
			jrLineBox.getPen().setLineWidth(1.0f);
			//textField.setPattern("#,##0.00_");
			//綁定數據設置
			JRDesignExpression expression=new JRDesignExpression();
			expression.setText("$F{"+titleValue[i]+"}");
			if(i<=(size-1)){
				expression.setValueClass(String.class);
				//設置內容水準垂直向左
				textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
			}else{
				expression.setValueClass(Number.class);
				//設置內容水準垂直向右
				textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
			}
			textField.setExpression(expression);
			//將控件添加到面板中
			detail.addElement(textField);
		}
		//將面板添加到Design中
		JRDesignSection section=(JRDesignSection)jDesign.getDetailSection();
		section.addBand(detail);
		//忽視頁碼
		jDesign.setIgnorePagination(true);
		//編譯Design
		return JasperCompileManager.compileReport(jDesign);
		
	}
	/**
	 * 生成報表
	 * @return
	 * @throws JRException
	 */
	public JasperReport getJasperReport(String date,String []reportMonth,String[] lang) throws JRException{
		columns = reportMonth.length*2+4;
		JasperDesign design = new JasperDesign();
		//報表名稱
		design.setName("resources_ckrs_report");
		//距離頂部距離
		design.setTopMargin(0);
		//距離左邊距離
		design.setLeftMargin(0);
		//表頭
		JRDesignBand title = new JRDesignBand();
		title.setHeight(20);
		//報表title
		JRDesignStaticText headText = new JRDesignStaticText();
		headText.setText(lang[0]+"("+date+")");//電子資源狀況報表
		headText.setX(0);
		headText.setY(0);
		headText.setHeight(20);
		//設備標題的寬度，實現標題居中的效果
		int titleWidth = (reportMonth.length+2)*columnWidth*2+100;
		headText.setWidth((titleWidth<columnWidth*10)?titleWidth:columnWidth*11);
		headText.setFontSize(12);
		headText.setBold(true);
		headText.setVerticalAlignment(VerticalAlignEnum.MIDDLE);//垂直居中
//		headText.setVerticalAlignment((byte)2);//垂直居中
		headText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		title.addElement(headText);
		design.setTitle(title);
		//column header
		JRDesignBand columnHeader = new JRDesignBand();
		columnHeader.setHeight(40);
		//column橫座標
		int titleXtag=0;
		//資源類型
		JRDesignStaticText headText1 = new JRDesignStaticText();
		headText1.setText(lang[1]);//資源類型
		headText1.setX(titleXtag);
		headText1.setY(0);
		headText1.setHeight(40);
		headText1.setWidth(columnWidth);
		headText1.setFontSize(fontsize);
		headText1.setVerticalAlignment(VerticalAlignEnum.MIDDLE);//垂直居中
		headText1.setHorizontalAlignment(HorizontalAlignEnum.CENTER);//居中
		headText1.setBold(true);//加粗
		JRLineBox jrLineBox1 = headText1.getLineBox();
		jrLineBox1.getPen().setLineWidth(1.0f);//邊框
		columnHeader.addElement(headText1);
		titleXtag= titleXtag+columnWidth;
		//題名
		JRDesignStaticText headText2 = new JRDesignStaticText();
		headText2.setText(lang[2]);//題名
		headText2.setX(titleXtag);
		headText2.setY(0);
		headText2.setHeight(40);
		headText2.setWidth(columnWidth*2);
		headText2.setFontSize(fontsize);
		headText2.setBold(true);//加粗
		headText2.setVerticalAlignment(VerticalAlignEnum.MIDDLE);//垂直居中
		headText2.setHorizontalAlignment(HorizontalAlignEnum.CENTER);//居中
		JRLineBox jrLineBox2 = headText2.getLineBox();
		jrLineBox2.getPen().setLineWidth(1.0f);//邊框
		columnHeader.addElement(headText2);
		titleXtag= titleXtag+columnWidth*2;
		//月份
		int monthwidth = 200;
		for(int i=0;i<reportMonth.length;i++){
			//月份column
			JRDesignStaticText headText3 = new JRDesignStaticText();
			headText3.setText(reportMonth[i]);
			headText3.setX(titleXtag+(i*200));
			headText3.setY(0);
			headText3.setHeight(20);
			headText3.setWidth(monthwidth);
			headText3.setFontSize(fontsize);
			headText3.setVerticalAlignment(VerticalAlignEnum.MIDDLE);//垂直居中
			headText3.setHorizontalAlignment(HorizontalAlignEnum.CENTER);//居中
			headText3.setBold(true);//加粗
			JRLineBox jrLineBox3 = headText3.getLineBox();
			jrLineBox3.getPen().setLineWidth(1.0f);//邊框
			columnHeader.addElement(headText3);
			
			//連線狀態正常column
			JRDesignStaticText headText6 = new JRDesignStaticText();
			headText6.setText(lang[3]);//連線狀態(正常)
			headText6.setX(titleXtag+(i*monthwidth));
			headText6.setY(20);
			headText6.setHeight(20);
			headText6.setWidth(columnWidth);
			headText6.setFontSize(fontsize);
			headText6.setVerticalAlignment(VerticalAlignEnum.MIDDLE);//垂直居中
			headText6.setHorizontalAlignment(HorizontalAlignEnum.CENTER);//居中
			headText6.setBold(true);//加粗
			JRLineBox jrLineBox6 = headText6.getLineBox();
			jrLineBox6.getPen().setLineWidth(1.0f);//邊框
			columnHeader.addElement(headText6);
//			//連線狀態異常column
			JRDesignStaticText headText7 = new JRDesignStaticText();
			headText7.setText(lang[4]);//連線狀態(異常)
			headText7.setX(titleXtag+(i*monthwidth)+columnWidth);
			headText7.setY(20);
			headText7.setHeight(20);
			headText7.setWidth(columnWidth);
			headText7.setFontSize(fontsize);
			headText7.setVerticalAlignment(VerticalAlignEnum.MIDDLE);//垂直居中
			headText7.setHorizontalAlignment(HorizontalAlignEnum.CENTER);//居中
			headText7.setBold(true);//加粗
			JRLineBox jrLineBox7 = headText7.getLineBox();
			jrLineBox7.getPen().setLineWidth(1.0f);//邊框
			columnHeader.addElement(headText7);
		}
		//合計column
		JRDesignStaticText headText4 = new JRDesignStaticText();
		headText4.setText(lang[5]);//合計
		headText4.setX(columnWidth*3+reportMonth.length*monthwidth);
		headText4.setY(0);
		headText4.setHeight(20);
		headText4.setWidth(columnWidth*2);
		headText4.setFontSize(fontsize);
		headText4.setVerticalAlignment(VerticalAlignEnum.MIDDLE);//垂直居中
		headText4.setHorizontalAlignment(HorizontalAlignEnum.CENTER);//居中
		headText4.setBold(true);
		JRLineBox jrLineBox4 = headText4.getLineBox();
		jrLineBox4.getPen().setLineWidth(1.0f);//邊框
		columnHeader.addElement(headText4);
		//連線狀態（正常）合計column
		JRDesignStaticText headText8 = new JRDesignStaticText();
		headText8.setText(lang[3]);//連線狀態(正常)
		headText8.setX(columnWidth*3+reportMonth.length*monthwidth);
		headText8.setY(20);
		headText8.setHeight(20);
		headText8.setWidth(columnWidth);
		headText8.setFontSize(fontsize);
		headText8.setVerticalAlignment(VerticalAlignEnum.MIDDLE);//垂直居中
		headText8.setHorizontalAlignment(HorizontalAlignEnum.CENTER);//居中
		headText8.setBold(true);
		JRLineBox jrLineBox8 = headText8.getLineBox();
		jrLineBox8.getPen().setLineWidth(1.0f);//邊框
		columnHeader.addElement(headText8);
		//連線狀態（異常）合計column
		JRDesignStaticText headText9 = new JRDesignStaticText();
		headText9.setText(lang[4]);//連線狀態(異常)
		headText9.setX(columnWidth*4+reportMonth.length*monthwidth);
		headText9.setY(20);
		headText9.setHeight(20);
		headText9.setWidth(columnWidth);
		headText9.setFontSize(fontsize);
		headText9.setVerticalAlignment(VerticalAlignEnum.MIDDLE);//垂直居中
		headText9.setHorizontalAlignment(HorizontalAlignEnum.CENTER);//居中
		headText9.setBold(true);
		JRLineBox jrLineBox9 = headText9.getLineBox();
		jrLineBox9.getPen().setLineWidth(1.0f);//邊框
		columnHeader.addElement(headText9);
		design.setColumnHeader(columnHeader);
//		//detail
		JRDesignBand detail = new JRDesignBand();
		detail.setHeight(20);
		
		//定義field
		//資源類型field
		JRDesignField field1 = new JRDesignField();
		field1.setName("resources_id");
		field1.setValueClass(String.class);
		design.addField(field1);
		//題名 field
		JRDesignField field2 = new JRDesignField();
		field2.setName("title");
		field2.setValueClass(String.class);
		design.addField(field2);
		//定義所有月份狀態field
		for(int l=2;l<columns;l++){
			JRDesignField field = new JRDesignField();
			field.setName("month_status"+l);
			field.setValueClass(Integer.class);
			design.addField(field);
		}
		
		int xFlag = 0;
//		int widthField = 100;
		//定義deatil欄位
		//資源類型field
		JRDesignTextField detailTextField1 = new JRDesignTextField();
		detailTextField1.setStretchWithOverflow(true);//自動換行
		detailTextField1.setX(xFlag);
		detailTextField1.setY(0);
		detailTextField1.setWidth(columnWidth);
		detailTextField1.setHeight(20);
		detailTextField1.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);//高度統一
		JRLineBox detailLineBox1 = detailTextField1.getLineBox();
		detailLineBox1.getPen().setLineWidth(1.0f);//邊框
		JRDesignExpression expression1 = new JRDesignExpression();
		expression1.setText("$F{resources_id}");
		expression1.setValueClass(String.class);
		detailTextField1.setExpression(expression1);	
		detail.addElement(detailTextField1);
		xFlag = xFlag+columnWidth;
		//題名field
		JRDesignTextField detailTextField2 = new JRDesignTextField();
		detailTextField2.setStretchWithOverflow(true);
		detailTextField2.setX(xFlag);
		detailTextField2.setY(0);
		detailTextField2.setWidth(columnWidth*2);
		detailTextField2.setHeight(20);
		detailTextField2.setStretchWithOverflow(true);//自動換行
		detailTextField2.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);//高度統一
		JRLineBox detailLineBox2 = detailTextField2.getLineBox();
		detailLineBox2.getPen().setLineWidth(1.0f);//邊框
		JRDesignExpression expression2 = new JRDesignExpression();
		expression2.setText("$F{title}");
		expression2.setValueClass(String.class);
		detailTextField2.setExpression(expression2);			
		detail.addElement(detailTextField2);
		xFlag = xFlag+columnWidth*2;
		for(int k=2;k<columns;k++){
			//筆數field
			JRDesignTextField detailTextField3 = new JRDesignTextField();
			detailTextField3.setStretchWithOverflow(true);
			detailTextField3.setX(xFlag);
			detailTextField3.setY(0);
			detailTextField3.setWidth(columnWidth);
			detailTextField3.setHeight(20);
			JRLineBox detailLineBox3 = detailTextField3.getLineBox();
			detailLineBox3.getPen().setLineWidth(1.0f);//邊框
			detailTextField3.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);//高度統一
			JRDesignExpression expression3 = new JRDesignExpression();
			expression3.setText("$F{month_status"+k+"}");
			expression3.setValueClass(Integer.class);
			detailTextField3.setExpression(expression3);			
			detail.addElement(detailTextField3);
			xFlag = xFlag+columnWidth;

		}
//		design.setDetail(detail);
		JRDesignSection section = (JRDesignSection)design.getDetailSection();
		section .addBand(detail);

		design.setIgnorePagination(true);
		return JasperCompileManager.compileReport(design);
	}
	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
}
