package com.claridy.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.util.resource.Labels;

import com.claridy.dao.IWebAccountDAO;
import com.claridy.dao.IWebEduTrainingClickDAO;
import com.claridy.dao.IWebFarmingNewsClickDAO;
import com.claridy.dao.IWebNewsClickDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.dao.IWebPublicationsClickDAO;
import com.claridy.dao.IWebResearchReportsClickDAO;
import com.claridy.domain.ErmNews;
import com.claridy.domain.SysUseDetail;
import com.claridy.domain.SysUseNumber;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEduTraining;
import com.claridy.domain.WebEduTrainingClick;
import com.claridy.domain.WebFarmingNews;
import com.claridy.domain.WebFarmingNewsClick;
import com.claridy.domain.WebNewsClick;
import com.claridy.domain.WebOrg;
import com.claridy.domain.WebPublication;
import com.claridy.domain.WebPublicationsClick;
import com.claridy.domain.WebReSearchReports;
import com.claridy.domain.WebResearchReportsClick;

@Service
public class SystUseNumberService {
	@Autowired
	private IWebNewsClickDAO webNewsClickDao;// 最新消息
	@Autowired
	private IWebEduTrainingClickDAO webEduClickDao;// 教育訓練
	@Autowired
	private IWebFarmingNewsClickDAO webFarmClickDao;// 農業新聞
	@Autowired
	private IWebResearchReportsClickDAO webReportClickDao;// 研究報告
	@Autowired
	private IWebPublicationsClickDAO webPubClickDao;// 農業出版品

	@Autowired
	private IWebAccountDAO webAccountDao;

	@Autowired
	private IWebOrgDAO webOrgDao;

	/**
	 * 根據時間單位查詢系統使用量統計集合 報表剛進去調用的方法
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<SysUseNumber> findSysUseNumberList(Date startDate, Date endDate, String parentOrgId) {
		List<SysUseNumber> sysUserListRet = new ArrayList<SysUseNumber>();
		List<WebAccount> webAccountList = webAccountDao.findAccountList();
		List<WebOrg> parentOrgList = webOrgDao.findParentOrg(parentOrgId);

		// 最新消息
		List<WebNewsClick> webNewsClickList = webNewsClickDao.findWebNewsClickList(startDate, endDate);
		// 教育訓練
		List<WebEduTrainingClick> webEduClickList = webEduClickDao.findWebEduClickList(startDate, endDate);
		// 農業新聞
		List<WebFarmingNewsClick> webFarmClickList = webFarmClickDao.findWebFarmClickList(startDate, endDate);
		// 研究報告
		List<WebResearchReportsClick> webReportClickList = webReportClickDao.findWebReportClickList(startDate, endDate);
		// 農業出版品
		List<WebPublicationsClick> webPubClickList = webPubClickDao.findWebPubClickList(startDate, endDate);

		for (int i = 0; i < parentOrgList.size(); i++) {
			WebOrg webOrg = parentOrgList.get(i);
			SysUseNumber sysUseNumberTemp = new SysUseNumber();
			int newsNumber = 0;// 最新消息
			int eduNumber = 0;// 教育訓練
			int farmNumber = 0;// 農業新聞
			int reportNumber = 0;// 研究報告
			int pubNumber = 0;// 農業出版品
			// 最新消息
			for (WebNewsClick webNewsClick : webNewsClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webNewsClick.getWebAccountUuid())) {
						if (webAccount.getParentorgid().equals(webOrg.getOrgId())) {
							newsNumber++;
						}
					}
				}
			}
			// 教育訓練
			for (WebEduTrainingClick webEduClick : webEduClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webEduClick.getWebAccountUuid())) {
						if (webAccount.getParentorgid().equals(webOrg.getOrgId())) {
							eduNumber++;
						}
					}
				}
			}
			// 農業新聞
			for (WebFarmingNewsClick webFarmClick : webFarmClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webFarmClick.getWebaccountUuid())) {
						if (webAccount.getParentorgid().equals(webOrg.getOrgId())) {
							farmNumber++;
						}
					}
				}
			}
			// 研究報告
			for (WebResearchReportsClick webReportClick : webReportClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webReportClick.getWebaccountUuid())) {
						if (webAccount.getParentorgid().equals(webOrg.getOrgId())) {
							reportNumber++;
						}
					}
				}
			}
			// 農業出版品
			for (WebPublicationsClick webPubClick : webPubClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webPubClick.getWebAccountUuid())) {
						if (webAccount.getParentorgid().equals(webOrg.getOrgId())) {
							pubNumber++;
						}
					}
				}
			}
			sysUseNumberTemp.setParentOrgName(webOrg.getOrgName());
			sysUseNumberTemp.setParentId(webOrg.getOrgId());
			sysUseNumberTemp.setParentSort(webOrg.getSort());
			sysUseNumberTemp.setNewsNumber(newsNumber);
			sysUseNumberTemp.setEduNumber(eduNumber);
			sysUseNumberTemp.setFarmNumber(farmNumber);
			sysUseNumberTemp.setReportNumber(reportNumber);
			sysUseNumberTemp.setPubNumber(pubNumber);
			sysUserListRet.add(sysUseNumberTemp);
		}
		return sysUserListRet;
	}

	/**
	 * 根據時間單位查詢個組室系統使用量集合 點擊單位名稱調用的方法
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<SysUseNumber> findSysUseNumberListByParent(Date startDate, Date endDate, String parentOrgId) {
		List<SysUseNumber> sysUserListRet = new ArrayList<SysUseNumber>();
		List<WebAccount> webAccountList = webAccountDao.findAccountList();
		List<WebOrg> orgList = webOrgDao.findOrg(parentOrgId);

		// 最新消息
		List<WebNewsClick> webNewsClickList = webNewsClickDao.findWebNewsClickList(startDate, endDate);
		// 教育訓練
		List<WebEduTrainingClick> webEduClickList = webEduClickDao.findWebEduClickList(startDate, endDate);
		// 農業新聞
		List<WebFarmingNewsClick> webFarmClickList = webFarmClickDao.findWebFarmClickList(startDate, endDate);
		// 研究報告
		List<WebResearchReportsClick> webReportClickList = webReportClickDao.findWebReportClickList(startDate, endDate);
		// 農業出版品
		List<WebPublicationsClick> webPubClickList = webPubClickDao.findWebPubClickList(startDate, endDate);

		for (int i = 0; i < orgList.size(); i++) {
			WebOrg webOrg = orgList.get(i);
			SysUseNumber sysUseNumberTemp = new SysUseNumber();
			int newsNumber = 0;// 最新消息
			int eduNumber = 0;// 教育訓練
			int farmNumber = 0;// 農業新聞
			int reportNumber = 0;// 研究報告
			int pubNumber = 0;// 農業出版品
			// 最新消息
			for (WebNewsClick webNewsClick : webNewsClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webNewsClick.getWebAccountUuid())) {
						if (webAccount.getOrgid() != null && !"".equals(webAccount.getOrgid()) && webAccount.getOrgid().equals(webOrg.getOrgId())
								&& webAccount.getParentorgid().equals(parentOrgId)) {
							newsNumber++;
						}
					}
				}
			}
			// 教育訓練
			for (WebEduTrainingClick webEduClick : webEduClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webEduClick.getWebAccountUuid())) {
						if (webAccount.getOrgid() != null && !"".equals(webAccount.getOrgid()) && webAccount.getOrgid().equals(webOrg.getOrgId())
								&& webAccount.getParentorgid().equals(parentOrgId)) {
							eduNumber++;
						}
					}
				}
			}
			// 農業新聞
			for (WebFarmingNewsClick webFarmClick : webFarmClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webFarmClick.getWebaccountUuid())) {
						if (webAccount.getOrgid() != null && !"".equals(webAccount.getOrgid()) && webAccount.getOrgid().equals(webOrg.getOrgId())
								&& webAccount.getParentorgid().equals(parentOrgId)) {
							farmNumber++;
						}
					}
				}
			}
			// 研究報告
			for (WebResearchReportsClick webReportClick : webReportClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webReportClick.getWebaccountUuid())) {
						if (webAccount.getOrgid() != null && !"".equals(webAccount.getOrgid()) && webAccount.getOrgid().equals(webOrg.getOrgId())
								&& webAccount.getParentorgid().equals(parentOrgId)) {
							reportNumber++;
						}
					}
				}
			}
			// 農業出版品
			for (WebPublicationsClick webPubClick : webPubClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webPubClick.getWebAccountUuid())) {
						if (webAccount.getOrgid() != null && !"".equals(webAccount.getOrgid()) && webAccount.getOrgid().equals(webOrg.getOrgId())
								&& webAccount.getParentorgid().equals(parentOrgId)) {
							pubNumber++;
						}
					}
				}
			}
			sysUseNumberTemp.setOrgName(webOrg.getOrgName());
			sysUseNumberTemp.setOrgId(webOrg.getOrgId());
			sysUseNumberTemp.setParentId(parentOrgId);
			sysUseNumberTemp.setParentSort(webOrg.getSort());
			sysUseNumberTemp.setNewsNumber(newsNumber);
			sysUseNumberTemp.setEduNumber(eduNumber);
			sysUseNumberTemp.setFarmNumber(farmNumber);
			sysUseNumberTemp.setReportNumber(reportNumber);
			sysUseNumberTemp.setPubNumber(pubNumber);
			sysUserListRet.add(sysUseNumberTemp);
		}

		SysUseNumber OsysUseNumberTemp = new SysUseNumber();
		int OnewsNumber = 0;// 最新消息
		int OeduNumber = 0;// 教育訓練
		int OfarmNumber = 0;// 農業新聞
		int OreportNumber = 0;// 研究報告
		int OpubNumber = 0;// 農業出版品
		// 最新消息
		for (WebNewsClick webNewsClick : webNewsClickList) {
			for (int k = 0; k < webAccountList.size(); k++) {
				WebAccount webAccount = webAccountList.get(k);
				if (webAccount.getUuid().equals(webNewsClick.getWebAccountUuid())) {
					if (webAccount.getParentorgid().equals(parentOrgId) && ("".equals(webAccount.getOrgid()) || webAccount.getOrgid() == null)) {
						OnewsNumber++;
					}
				}
			}
		}
		// 教育訓練
		for (WebEduTrainingClick webEduClick : webEduClickList) {
			for (int k = 0; k < webAccountList.size(); k++) {
				WebAccount webAccount = webAccountList.get(k);
				if (webAccount.getUuid().equals(webEduClick.getWebAccountUuid())) {
					if (webAccount.getParentorgid().equals(parentOrgId) && ("".equals(webAccount.getOrgid()) || webAccount.getOrgid() == null)) {
						OeduNumber++;
					}
				}
			}
		}
		// 農業新聞
		for (WebFarmingNewsClick webFarmClick : webFarmClickList) {
			for (int k = 0; k < webAccountList.size(); k++) {
				WebAccount webAccount = webAccountList.get(k);
				if (webAccount.getUuid().equals(webFarmClick.getWebaccountUuid())) {
					if (webAccount.getParentorgid().equals(parentOrgId) && ("".equals(webAccount.getOrgid()) || webAccount.getOrgid() == null)) {
						OfarmNumber++;
					}
				}
			}
		}
		// 研究報告
		for (WebResearchReportsClick webReportClick : webReportClickList) {
			for (int k = 0; k < webAccountList.size(); k++) {
				WebAccount webAccount = webAccountList.get(k);
				if (webAccount.getUuid().equals(webReportClick.getWebaccountUuid())) {
					if (webAccount.getParentorgid().equals(parentOrgId) && ("".equals(webAccount.getOrgid()) || webAccount.getOrgid() == null)) {
						OreportNumber++;
					}
				}
			}
		}
		// 農業出版品
		for (WebPublicationsClick webPubClick : webPubClickList) {
			for (int k = 0; k < webAccountList.size(); k++) {
				WebAccount webAccount = webAccountList.get(k);
				if (webAccount.getUuid().equals(webPubClick.getWebAccountUuid())) {
					if (webAccount.getParentorgid().equals(parentOrgId) && ("".equals(webAccount.getOrgid()) || webAccount.getOrgid() == null)) {
						OpubNumber++;
					}
				}
			}
		}

		// 其他
		OsysUseNumberTemp.setOrgName(Labels.getLabel("webEmployee.tboxIdType.other"));
		OsysUseNumberTemp.setOrgId("other" + parentOrgId);
		OsysUseNumberTemp.setParentId(parentOrgId);
		OsysUseNumberTemp.setNewsNumber(OnewsNumber);
		OsysUseNumberTemp.setEduNumber(OeduNumber);
		OsysUseNumberTemp.setFarmNumber(OfarmNumber);
		OsysUseNumberTemp.setReportNumber(OreportNumber);
		OsysUseNumberTemp.setPubNumber(OpubNumber);
		sysUserListRet.add(OsysUseNumberTemp);

		int newsNumber = 0;// 最新消息
		int eduNumber = 0;// 教育訓練
		int farmNumber = 0;// 農業新聞
		int reportNumber = 0;// 研究報告
		int pubNumber = 0;// 農業出版品
		for (int i = 0; i < sysUserListRet.size(); i++) {
			SysUseNumber sysUseNumberTemp = sysUserListRet.get(i);
			newsNumber += sysUseNumberTemp.getNewsNumber();
			eduNumber += sysUseNumberTemp.getEduNumber();
			farmNumber += sysUseNumberTemp.getFarmNumber();
			reportNumber += sysUseNumberTemp.getReportNumber();
			pubNumber += sysUseNumberTemp.getPubNumber();
		}
		SysUseNumber sysUseNumberTemp = new SysUseNumber();
		// 小計
		sysUseNumberTemp.setOrgName(Labels.getLabel("subTitle"));
		sysUseNumberTemp.setOrgId("");
		sysUseNumberTemp.setParentId(parentOrgId);
		sysUseNumberTemp.setNewsNumber(newsNumber);
		sysUseNumberTemp.setEduNumber(eduNumber);
		sysUseNumberTemp.setFarmNumber(farmNumber);
		sysUseNumberTemp.setReportNumber(reportNumber);
		sysUseNumberTemp.setPubNumber(pubNumber);
		sysUserListRet.add(sysUseNumberTemp);

		return sysUserListRet;
	}

	/**
	 * 根據時間單位查詢系統使用量統計詳細信息 第一層點擊數字和第二層點擊小計數字調用的方法
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<SysUseDetail> findSysUseDetListByParentOrg(Date startDate, Date endDate, String parentOrgId, String type) {
		List<SysUseDetail> sysUseDetListRet = new ArrayList<SysUseDetail>();
		List<WebAccount> webAccountList = webAccountDao.findAccountList();
		// 最新消息
		List<ErmNews> webNewsList = webNewsClickDao.findAllWebNews();
		List<WebNewsClick> webNewsClickList = webNewsClickDao.findWebNewsClickList(startDate, endDate);
		// 教育訓練
		List<WebEduTraining> webEduList = webEduClickDao.findAllWebEdu();
		List<WebEduTrainingClick> webEduClickList = webEduClickDao.findWebEduClickList(startDate, endDate);
		// 農業新聞
		List<WebFarmingNews> webFarmList = webFarmClickDao.findAllWebFarm();
		List<WebFarmingNewsClick> webFarmClickList = webFarmClickDao.findWebFarmClickList(startDate, endDate);
		// 研究報告
		List<WebReSearchReports> webReportList = webReportClickDao.findAllWebReport();
		List<WebResearchReportsClick> webReportClickList = webReportClickDao.findWebReportClickList(startDate, endDate);
		// 農業出版品
		List<WebPublication> webPubList = webPubClickDao.findAllWebPub();
		List<WebPublicationsClick> webPubClickList = webPubClickDao.findWebPubClickList(startDate, endDate);

		if (type.equals("new")) {
			// 最新消息
			for (WebNewsClick webNewsClick : webNewsClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webNewsClick.getWebAccountUuid())) {
						if (webAccount.getParentorgid().equals(parentOrgId)) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webNewsClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webNewsClick.getVisitIp());
							for (ErmNews webNews : webNewsList) {
								if (webNews.getUuid().equals(webNewsClick.getUuid())) {
									sysUseDetailTemp.setTitle(webNews.getMatterZhTw());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else if (type.equals("edu")) {
			// 教育訓練
			for (WebEduTrainingClick webEduClick : webEduClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webEduClick.getWebAccountUuid())) {
						if (webAccount.getParentorgid().equals(parentOrgId)) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webEduClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webEduClick.getVisitIp());
							for (WebEduTraining webEdu : webEduList) {
								if (webEdu.getUuid().equals(webEduClick.getUuid())) {
									sysUseDetailTemp.setTitle(webEdu.getMatterZhTw());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else if (type.equals("farm")) {
			// 農業新聞
			for (WebFarmingNewsClick webFarmClick : webFarmClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webFarmClick.getWebaccountUuid())) {
						if (webAccount.getParentorgid().equals(parentOrgId)) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webFarmClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webFarmClick.getVisitIp());
							for (WebFarmingNews webFarm : webFarmList) {
								if (webFarm.getUuid().equals(webFarmClick.getUuid())) {
									sysUseDetailTemp.setTitle(webFarm.getTitle());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else if (type.equals("report")) {
			// 研究報告
			for (WebResearchReportsClick webReportClick : webReportClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webReportClick.getWebaccountUuid())) {
						if (webAccount.getParentorgid().equals(parentOrgId)) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webReportClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webReportClick.getVisitIp());
							for (WebReSearchReports webReport : webReportList) {
								if (webReport !=null) {
									if (webReport.getUuid().equals(webReportClick.getUuid())) {
										sysUseDetailTemp.setTitle(webReport.getSubjectZhTw());
									}
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else {
			// 農業出版品
			for (WebPublicationsClick webPubClick : webPubClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webPubClick.getWebAccountUuid())) {
						if (webAccount.getParentorgid().equals(parentOrgId)) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webPubClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webPubClick.getVisitIp());
							for (WebPublication webPub : webPubList) {
								if (webPub.getUuid().equals(webPubClick.getUuid())) {
									sysUseDetailTemp.setTitle(webPub.getTitleZhTw());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		}

		return sysUseDetListRet;
	}

	/**
	 * 根據時間單位组室为空查詢系統使用量統計詳細信息 第二層點擊其他調用的方法
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<SysUseDetail> findSysUseDetailListByParentOrgOther(Date startDate, Date endDate, String parentOrgId, String type) {
		List<SysUseDetail> sysUseDetListRet = new ArrayList<SysUseDetail>();
		List<WebAccount> webAccountList = webAccountDao.findAccountList();
		// 最新消息
		List<ErmNews> webNewsList = webNewsClickDao.findAllWebNews();
		List<WebNewsClick> webNewsClickList = webNewsClickDao.findWebNewsClickList(startDate, endDate);
		// 教育訓練
		List<WebEduTraining> webEduList = webEduClickDao.findAllWebEdu();
		List<WebEduTrainingClick> webEduClickList = webEduClickDao.findWebEduClickList(startDate, endDate);
		// 農業新聞
		List<WebFarmingNews> webFarmList = webFarmClickDao.findAllWebFarm();
		List<WebFarmingNewsClick> webFarmClickList = webFarmClickDao.findWebFarmClickList(startDate, endDate);
		// 研究報告
		List<WebReSearchReports> webReportList = webReportClickDao.findAllWebReport();
		List<WebResearchReportsClick> webReportClickList = webReportClickDao.findWebReportClickList(startDate, endDate);
		// 農業出版品
		List<WebPublication> webPubList = webPubClickDao.findAllWebPub();
		List<WebPublicationsClick> webPubClickList = webPubClickDao.findWebPubClickList(startDate, endDate);

		if (type.equals("new")) {
			// 最新消息
			for (WebNewsClick webNewsClick : webNewsClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webNewsClick.getWebAccountUuid())) {
						if (webAccount.getParentorgid().equals(parentOrgId) && (webAccount.getOrgid() == null || webAccount.getOrgid().equals(""))) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webNewsClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webNewsClick.getVisitIp());
							for (ErmNews webNews : webNewsList) {
								if (webNews.getUuid().equals(webNewsClick.getUuid())) {
									sysUseDetailTemp.setTitle(webNews.getMatterZhTw());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else if (type.equals("edu")) {
			// 教育訓練
			for (WebEduTrainingClick webEduClick : webEduClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webEduClick.getWebAccountUuid())) {
						if (webAccount.getParentorgid().equals(parentOrgId) && (webAccount.getOrgid() == null || webAccount.getOrgid().equals(""))) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webEduClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webEduClick.getVisitIp());
							for (WebEduTraining webEdu : webEduList) {
								if (webEdu.getUuid().equals(webEduClick.getUuid())) {
									sysUseDetailTemp.setTitle(webEdu.getMatterZhTw());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else if (type.equals("farm")) {
			// 農業新聞
			for (WebFarmingNewsClick webFarmClick : webFarmClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webFarmClick.getWebaccountUuid())) {
						if (webAccount.getParentorgid().equals(parentOrgId) && (webAccount.getOrgid() == null || webAccount.getOrgid().equals(""))) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webFarmClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webFarmClick.getVisitIp());
							for (WebFarmingNews webFarm : webFarmList) {
								if (webFarm.getUuid().equals(webFarmClick.getUuid())) {
									sysUseDetailTemp.setTitle(webFarm.getTitle());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else if (type.equals("report")) {
			// 研究報告
			for (WebResearchReportsClick webReportClick : webReportClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webReportClick.getWebaccountUuid())) {
						if (webAccount.getParentorgid().equals(parentOrgId) && (webAccount.getOrgid() == null || webAccount.getOrgid().equals(""))) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webReportClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webReportClick.getVisitIp());
							for (WebReSearchReports webReport : webReportList) {
								if (webReport.getUuid().equals(webReportClick.getUuid())) {
									sysUseDetailTemp.setTitle(webReport.getSubjectZhTw());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else {
			// 農業出版品
			for (WebPublicationsClick webPubClick : webPubClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webPubClick.getWebAccountUuid())) {
						if (webAccount.getParentorgid().equals(parentOrgId) && (webAccount.getOrgid() == null || webAccount.getOrgid().equals(""))) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webPubClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webPubClick.getVisitIp());
							for (WebPublication webPub : webPubList) {
								if (webPub.getUuid().equals(webPubClick.getUuid())) {
									sysUseDetailTemp.setTitle(webPub.getTitleZhTw());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		}

		return sysUseDetListRet;
	}

	/**
	 * 根據時間組室查詢系統使用量統計詳細信息 第二層組室點擊數字調用的方法
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<SysUseDetail> findSysUseDetListByOrg(Date startDate, Date endDate, String orgId, String type) {
		List<SysUseDetail> sysUseDetListRet = new ArrayList<SysUseDetail>();
		List<WebAccount> webAccountList = webAccountDao.findAccountList();
		// 最新消息
		List<ErmNews> webNewsList = webNewsClickDao.findAllWebNews();
		List<WebNewsClick> webNewsClickList = webNewsClickDao.findWebNewsClickList(startDate, endDate);
		// 教育訓練
		List<WebEduTraining> webEduList = webEduClickDao.findAllWebEdu();
		List<WebEduTrainingClick> webEduClickList = webEduClickDao.findWebEduClickList(startDate, endDate);
		// 農業新聞
		List<WebFarmingNews> webFarmList = webFarmClickDao.findAllWebFarm();
		List<WebFarmingNewsClick> webFarmClickList = webFarmClickDao.findWebFarmClickList(startDate, endDate);
		// 研究報告
		List<WebReSearchReports> webReportList = webReportClickDao.findAllWebReport();
		List<WebResearchReportsClick> webReportClickList = webReportClickDao.findWebReportClickList(startDate, endDate);
		// 農業出版品
		List<WebPublication> webPubList = webPubClickDao.findAllWebPub();
		List<WebPublicationsClick> webPubClickList = webPubClickDao.findWebPubClickList(startDate, endDate);

		if (type.equals("new")) {
			// 最新消息
			for (WebNewsClick webNewsClick : webNewsClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webNewsClick.getWebAccountUuid())) {
						if (webAccount.getOrgid() != null && webAccount.getOrgid().equals(orgId)) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webNewsClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webNewsClick.getVisitIp());
							for (ErmNews webNews : webNewsList) {
								if (webNews.getUuid().equals(webNewsClick.getUuid())) {
									sysUseDetailTemp.setTitle(webNews.getMatterZhTw());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else if (type.equals("edu")) {
			// 教育訓練
			for (WebEduTrainingClick webEduClick : webEduClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webEduClick.getWebAccountUuid())) {
						if (webAccount.getOrgid() != null && webAccount.getOrgid().equals(orgId)) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webEduClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webEduClick.getVisitIp());
							for (WebEduTraining webEdu : webEduList) {
								if (webEdu.getUuid().equals(webEduClick.getUuid())) {
									sysUseDetailTemp.setTitle(webEdu.getMatterZhTw());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else if (type.equals("farm")) {
			// 農業新聞
			for (WebFarmingNewsClick webFarmClick : webFarmClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webFarmClick.getWebaccountUuid())) {
						if (webAccount.getOrgid() != null && webAccount.getOrgid().equals(orgId)) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webFarmClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webFarmClick.getVisitIp());
							for (WebFarmingNews webFarm : webFarmList) {
								if (webFarm.getUuid().equals(webFarmClick.getUuid())) {
									sysUseDetailTemp.setTitle(webFarm.getTitle());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else if (type.equals("report")) {
			// 研究報告
			for (WebResearchReportsClick webReportClick : webReportClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webReportClick.getWebaccountUuid())) {
						if (webAccount.getOrgid() != null && webAccount.getOrgid().equals(orgId)) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webReportClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webReportClick.getVisitIp());
							for (WebReSearchReports webReport : webReportList) {
								if (webReport.getUuid().equals(webReportClick.getUuid())) {
									sysUseDetailTemp.setTitle(webReport.getSubjectZhTw());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		} else {
			// 農業出版品
			for (WebPublicationsClick webPubClick : webPubClickList) {
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webPubClick.getWebAccountUuid())) {
						if (webAccount.getOrgid() != null && webAccount.getOrgid().equals(orgId)) {
							SysUseDetail sysUseDetailTemp = new SysUseDetail();
							sysUseDetailTemp.setCreateDate(webPubClick.getVisitTime());
							sysUseDetailTemp.setAccountId(webAccount.getAccountId());
							sysUseDetailTemp.setAccountName(webAccount.getNameZhTw());
							sysUseDetailTemp.setAccountIp(webPubClick.getVisitIp());
							for (WebPublication webPub : webPubList) {
								if (webPub.getUuid().equals(webPubClick.getUuid())) {
									sysUseDetailTemp.setTitle(webPub.getTitleZhTw());
								}
							}
							sysUseDetListRet.add(sysUseDetailTemp);
						}
					}
				}
			}
		}

		return sysUseDetListRet;
	}
}
