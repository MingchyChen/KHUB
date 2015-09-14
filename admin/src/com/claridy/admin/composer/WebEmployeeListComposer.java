package com.claridy.admin.composer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.Resources_rsbatch_dataSource;
import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.WebEmployeeListService;
import com.claridy.facade.WebFunctionEmployeeService;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebSysLogService;

/**
 * zfdong nj 管理管理者權限管理作業 清單列 2014/8/6
 */
public class WebEmployeeListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4495610820701239659L;
	@Wire
	private Listbox employeeLix;
	@Wire
	private Textbox employeeIdBox;

	@Wire
	private Textbox employeeNameBox;
	@Wire
	private Combobox idTypeBox;
	@Wire
	private Combobox orgNameBox;

	@WireVariable
	private List<WebEmployee> webEmployeeList;

	@WireVariable
	private List<WebOrg> webOrgList;

	@Wire
	private Radiogroup orOrAnd;

	@Wire
	private Window addWebEmployeeWin;

	@Wire
	private Radiogroup rgroupIsManager;

	@WireVariable
	private WebEmployee webEmployee;

	private final Logger log = LoggerFactory.getLogger(getClass());

	public WebEmployee getWebEmployee() {
		return webEmployee;
	}

	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
	}

	@Listen("onClick=#pagSearchBtn")
	public void search() {
		try {
			String employeeId = employeeIdBox.getValue().trim();
			String employeeName = employeeNameBox.getValue().trim();
			employeeId = XSSStringEncoder.encodeXSSString(employeeId);
			employeeName = XSSStringEncoder.encodeXSSString(employeeName);
			WebEmployee webEmployee = new WebEmployee();
			webEmployee.setEmployeeId(employeeId);
			webEmployee.setEmployeeName(employeeName);
			int orOrAn = 0;
			int isManger = -1;
			if (orgNameBox.getSelectedItem() != null && !orgNameBox.getSelectedItem().getValue().equals("0")) {
				List<WebOrg> parentOrg = ((WebEmployeeListService) SpringUtil.getBean("webEmployeeListService")).findParentOrg(orgNameBox
						.getSelectedItem().getValue().toString());
				webEmployee.setParentWebOrg(parentOrg.get(0));
			}
			if (idTypeBox.getSelectedItem() != null) {
				webEmployee.setIdType(Integer.parseInt((String) idTypeBox.getSelectedItem().getValue()));
			}
			if (orOrAnd.getSelectedItem() != null) {
				String sorOrAn = orOrAnd.getSelectedItem().getValue();
				orOrAn = Integer.parseInt(sorOrAn);
			}
			if (rgroupIsManager.getSelectedItem() != null) {
				isManger = Integer.parseInt(rgroupIsManager.getSelectedItem().getValue().toString());
			}
			if ((employeeId != null && !"".equals(employeeId)) || (employeeName != null && !"".equals(employeeName))
					|| (orOrAnd.getSelectedItem() != null) || (!idTypeBox.getSelectedItem().getValue().equals("0"))
					|| (!orgNameBox.getSelectedItem().getValue().equals("0")) || (rgroupIsManager.getSelectedItem() != null)) {
			} else {
				ZkUtils.showExclamation(Labels.getLabel("searchIsNull"), Labels.getLabel("info"));
				return;
			}
			webEmployee.setIsManager(isManger);
			WebEmployee webEmp = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebEmployee> webEmployeeList = ((WebEmployeeListService) SpringUtil.getBean("webEmployeeListService")).findWebEmployee(webEmployee,
					webEmp, orOrAn, 1);
			WebEmployee webEmployeeTmp = new WebEmployee();

			for (int i = 0; i < webEmployeeList.size(); i++) {
				if (webEmployeeList.get(i).getWeborg() != null && webEmployeeList.get(i).getWeborg().getOrgId() == null) {
					webEmployeeList.get(i).getWeborg().setOrgId("");
				}

				webEmployeeTmp.setEmployeesn(webEmployeeList.get(i).getDataOwner());
				List<WebEmployee> webEmployeeListTemp = ((WebEmployeeListService) SpringUtil.getBean("webEmployeeListService")).findWebEmployee(
						webEmployeeTmp, webEmp, 0, 1);
				if (webEmployeeListTemp.size() > 0) {
					webEmployeeList.get(i).setDataOwner(webEmployeeListTemp.get(0).getEmployeeName());
				} else {
					webEmployeeList.get(i).setDataOwner("");
				}

			}

			ListModelList<WebEmployee> WebEmployeeModel = new ListModelList<WebEmployee>(webEmployeeList);
			WebEmployeeModel.setMultiple(true);
			employeeLix.setModel(WebEmployeeModel);
		} catch (WrongValueException e) {
			log.error("查詢webEmployee異常" + e);
		} catch (NumberFormatException e) {
			log.error("查詢webEmployee異常" + e);
		}
	}

	@Listen("onClick=#addBtn")
	public void addWebEmployee() {
		addWebEmployeeWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/webemployee/webEmployeeAdd.zul", null, null);
		addWebEmployeeWin.doModal();
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick=#deleteBtn")
	public void deleteWebEmployee() {
		int updateChecked = employeeLix.getSelectedCount();
		try {
			if (updateChecked > 0) {
				// “你確定要刪除該資料嗎？”
				ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener() {
					public void onEvent(Event event) {
						int clickButton = (Integer) event.getData();
						if (clickButton == Messagebox.OK) {
							Set<Listitem> listitem = employeeLix.getSelectedItems();
							WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
							for (Listitem employee : listitem) {
								webEmployee = employee.getValue();
								WebEmployee webEmployeeTmp = new WebEmployee();
								webEmployeeTmp.setEmployeesn(webEmployee.getEmployeesn());
								// webEmployeeTmp=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findWebEmployee(webEmployeeTmp,
								// loginwebEmployee, 0, 1).get(0);
								webEmployeeTmp = ((WebEmployeeListService) SpringUtil.getBean("webEmployeeListService")).getWebEmployee(webEmployee
										.getEmployeesn());
								((WebEmployeeListService) SpringUtil.getBean("webEmployeeListService")).deleteWebEmployee(webEmployeeTmp);
								((WebFunctionEmployeeService) SpringUtil.getBean("webFunctionEmployeeService")).deleteWebFunctionEmp(webEmployee
										.getEmployeesn());
								((WebSysLogService) SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),
										loginwebEmployee.getEmployeesn(), "employee_" + webEmployee.getEmployeesn());
							}

							ZkUtils.refurbishMethod("webemployee/webEmployee.zul");
						}
					}
				});
			} else {
				ZkUtils.showExclamation(Labels.getLabel("deleteData"), Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("刪除webEmployee異常" + e);
		}
	}

	@Listen("onClick=#showAll")
	public void shoaAll() {
		try {
			WebEmployee webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webEmployeeList = ((WebEmployeeListService) SpringUtil.getBean("webEmployeeListService")).findWebEmployee(null, webEmployee, 0, 1);
			employeeIdBox.setValue("");
			employeeNameBox.setValue("");
			idTypeBox.setSelectedIndex(0);
			orgNameBox.setSelectedIndex(0);
			rgroupIsManager.setSelectedItem(null);
			orOrAnd.setSelectedItem(null);
			WebEmployee webEmployeeTmp = new WebEmployee();
			for (int i = 0; i < webEmployeeList.size(); i++) {
				if (webEmployeeList.get(i).getWeborg() != null && webEmployeeList.get(i).getWeborg().getOrgId() == null) {
					webEmployeeList.get(i).getWeborg().setOrgId("");
				}
				webEmployeeTmp.setEmployeesn(webEmployeeList.get(i).getDataOwner());
				List<WebEmployee> webEmployeeListTemp = ((WebEmployeeListService) SpringUtil.getBean("webEmployeeListService")).findWebEmployee(
						webEmployeeTmp, webEmployee, 0, 1);
				if (webEmployeeListTemp.size() > 0) {
					webEmployeeList.get(i).setDataOwner(webEmployeeListTemp.get(0).getEmployeeName());
				} else {
					webEmployeeList.get(i).setDataOwner("");
				}

			}
			ListModelList<WebEmployee> tmpLML = new ListModelList<WebEmployee>(webEmployeeList);
			tmpLML.setMultiple(true);
			employeeLix.setModel(tmpLML);
		} catch (Exception e) {
			log.error("查詢所有webEmployee異常" + e);
		}
	}

	/*
	 * 汇出execl (non-Javadoc)
	 * 
	 * @see
	 * org.zkoss.zk.ui.select.SelectorComposer#doAfterCompose(org.zkoss.zk.ui
	 * .Component)
	 */
	@Listen("onClick=#execleBtn")
	public void remit() {
		try {
			int count = employeeLix.getSelectedCount();
			if (count > 0) {

				String[] title = { Labels.getLabel("account"), Labels.getLabel("name"), Labels.getLabel("unit"), Labels.getLabel("orgid"),
						Labels.getLabel("ermCodePublisherperson.title"), Labels.getLabel("phone"), "E-Mail", Labels.getLabel("state") };
				String[] value = { "employeesn", "employeename", "parentWebOrg.orgName", "webOrg.orgName", "titleZhTw", "tel", "email", "status" };
				Set<Listitem> listiem = employeeLix.getSelectedItems();

				String realth = ((SystemProperties) SpringUtil.getBean("systemProperties")).systemDocumentPath;
				String fileName = "document/upload/WebEmployeeExcel.xls";
				List<String[]> webAccountList = new ArrayList<String[]>();
				for (Listitem webEmployee : listiem) {
					WebEmployee webEmployeeTmp = webEmployee.getValue();
					String titleZhTw = "";
					String status = "";
					if (webEmployeeTmp.getIsLock() == 1) {
						status = Labels.getLabel("searchInfo.isdisable");
					} else if (webEmployeeTmp.getIsLock() == 0) {
						status = Labels.getLabel("searchInfo.disable");
					}
					if (webEmployeeTmp.getIdType() == 3) {
						titleZhTw = Labels.getLabel("webEmployeeAdd.tboxIdType.use");
					}
					if (webEmployeeTmp.getIdType() == 4) {
						titleZhTw = Labels.getLabel("webEmployee.tboxIdType.fuse");
					}
					if (webEmployeeTmp.getIdType() == 5) {
						titleZhTw = Labels.getLabel("webEmployee.tboxIdType.aidUse");
					}
					if (webEmployeeTmp.getIdType() == 6) {
						titleZhTw = Labels.getLabel("webEmployee.tboxIdType.technician");
					}
					if (webEmployeeTmp.getIdType() == 7) {
						titleZhTw = Labels.getLabel("webEmployee.tboxIdType.ftechnician");
					}
					if (webEmployeeTmp.getIdType() == 8) {
						titleZhTw = Labels.getLabel("webEmployee.tboxIdType.other");
					}
					String[] values = { webEmployeeTmp.getEmployeeId(), webEmployeeTmp.getEmployeeName(),
							webEmployeeTmp.getParentWebOrg().getOrgName(), "", titleZhTw, webEmployeeTmp.getTel(), webEmployeeTmp.getEmail(), status };
					if (webEmployeeTmp.getWeborg() != null && webEmployeeTmp.getWeborg().getOrgId() != null) {
						values[3] = webEmployeeTmp.getWeborg().getOrgName();
					}

					webAccountList.add(values);
				}
				List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
				Resources_rsbatch_report rsbatch_report = new Resources_rsbatch_report(Labels.getLabel("webAccount.excelExport"));
				JasperReport jasperReport = rsbatch_report.getJasperReport(title, value);
				JRDataSource dataSource = new Resources_rsbatch_dataSource(value, webAccountList);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
				jasperPrintList.add(jasperPrint);
				JExcelApiExporter excelExporter = new JExcelApiExporter();
				excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
				excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, realth + fileName);
				excelExporter.exportReport();
				Executions.sendRedirect(fileName);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("selectOut"), Labels.getLabel("info"));
			}
		} catch (JRException e) {
			log.error("" + e);
		}
	}

	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			webOrgList = ((WebEmployeeListService) SpringUtil.getBean("webEmployeeListService")).findParentOrg(null);
			Comboitem com = new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue("0");
			orgNameBox.appendChild(com);
			for (int i = 0; i < webOrgList.size(); i++) {
				WebOrg webOrg = new WebOrg();
				webOrg = webOrgList.get(i);
				com = new Comboitem();
				com.setLabel(webOrg.getOrgName());
				com.setValue(webOrg.getOrgId());
				orgNameBox.appendChild(com);
			}
			orgNameBox.setSelectedIndex(0);
			idTypeBox.setSelectedIndex(0);
			WebEmployee webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webEmployeeList = ((WebEmployeeListService) SpringUtil.getBean("webEmployeeListService")).findWebEmployee(null, webEmployee, 0, 1);
			for (int i = 0; i < webEmployeeList.size(); i++) {
				if (webEmployeeList.get(i).getWeborg() != null && webEmployeeList.get(i).getWeborg().getOrgId() == null) {
					webEmployeeList.get(i).getWeborg().setOrgId("");
				}

				WebEmployee webEmployeeListTemp = ((WebEmployeeListService) SpringUtil.getBean("webEmployeeListService")).getEmpById(webEmployeeList
						.get(i).getDataOwner());
				if (webEmployeeListTemp != null) {
					webEmployeeList.get(i).setDataOwner(webEmployeeListTemp.getEmployeeName());
				} else {
					webEmployeeList.get(i).setDataOwner("");
				}

			}
			ListModelList<WebEmployee> tmpLML = new ListModelList<WebEmployee>(webEmployeeList);
			tmpLML.setMultiple(true);
			employeeLix.setModel(tmpLML);
		} catch (Exception e) {
			log.error("初始化webEmployee異常" + e);
		}

	}

}
