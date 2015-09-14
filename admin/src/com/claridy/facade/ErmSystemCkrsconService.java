package com.claridy.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmSystemCkrsconDAO;
import com.claridy.domain.ErmSystemCkrscon;
import com.claridy.domain.WebEmployee;

@Service
public class ErmSystemCkrsconService {

	@Autowired
	public IErmSystemCkrsconDAO ermSystemCkrsconDao;
	/**
	 * 查詢設定偵測資源時間對象
	 * @param uuid
	 * @return
	 */
	public ErmSystemCkrscon getErmSystemCkrscon(WebEmployee webEmployee){
		return ermSystemCkrsconDao.getErmSystemCkrscon(webEmployee);
	}
	/**
	 * 修改方法
	 * @param noticeTemp
	 */
	public void update(ErmSystemCkrscon ermSystemCkrscon){
		ermSystemCkrsconDao.merge(ermSystemCkrscon);
	}
	
	
	public ErmSystemCkrscon getErmSystemCkrsconAll(){
		return ermSystemCkrsconDao.getErmSystemCkrsconAll();
	}
}
