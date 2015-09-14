package com.claridy.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmCodeGeneralCodeDAO;
import com.claridy.dao.IErmResourcesSubjectIdDAO;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesSubject;

@Service
public class ErmResourcesSubjectService {

	
	@Autowired
	private IErmResourcesSubjectIdDAO ermResourcsSubjectDAO;
	@Autowired
	private IErmCodeGeneralCodeDAO ermGeneralDao;
	/**
	 * 查詢全部
	 * @param webEmployee
	 * @return
	 */
	public List<ErmResourcesSubject> findSubjectList(String resourcesId){
		 List<ErmResourcesSubject> list= ermResourcsSubjectDAO.findSubjectList(resourcesId);
		 List<ErmResourcesSubject> listRet=new ArrayList<ErmResourcesSubject>();
		 for (int i = 0; i < list.size(); i++) {
			 ErmResourcesSubject subjectTemp=list.get(i);
			 String typeId=resourcesId.substring(0,2);
			 ErmCodeGeneralCode ermGeneralTemp=new ErmCodeGeneralCode();
			 if(typeId.equals("DB")){
				 ermGeneralTemp=ermGeneralDao.findByItemIDAndGeneralcodeId("DBSUB",subjectTemp.getSubjectId());
			 }else if(typeId.equals("EJ")){
				 ermGeneralTemp=ermGeneralDao.findByItemIDAndGeneralcodeId("EJSUB",subjectTemp.getSubjectId());
			 }else if(typeId.equals("EB")){
				 ermGeneralTemp=ermGeneralDao.findByItemIDAndGeneralcodeId("EBSUB",subjectTemp.getSubjectId());
			 }else{
				 ermGeneralTemp=ermGeneralDao.findByItemIDAndGeneralcodeId("WSSUB",subjectTemp.getSubjectId());
			 }
			 subjectTemp.setSubjectName(ermGeneralTemp.getName1());
			 listRet.add(subjectTemp);
		}
		 return listRet;
	}
	public ErmResourcesSubject getSubject(String resourcesId,String subjectId){
		return ermResourcsSubjectDAO.getSubject(resourcesId, subjectId);
	}
	/**
	 * 新增方法
	 * @param resReltitle
	 */
	public void create(ErmResourcesSubject resSubject){
		ermResourcsSubjectDAO.create(resSubject);
	}
	/**
	 * 修改方法
	 * @param resReltitle
	 */
	public void update(ErmResourcesSubject resSubject){
		ermResourcsSubjectDAO.merge(resSubject);
	}
	/**
	 * 刪除方法
	 * @param resReltitle
	 */
	public void delete(ErmResourcesSubject resSubject){
		ermResourcsSubjectDAO.delete(resSubject);
	}
}
