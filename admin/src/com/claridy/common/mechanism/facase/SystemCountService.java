package com.claridy.common.mechanism.facase;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.ISys_CountDAO;
import com.claridy.common.mechanism.domain.Sys_Count;

@Service
public class SystemCountService {

	@Autowired
	private ISys_CountDAO syscountDAO;

	/***
	 * findSysCount 通過obj_pk和obj_name獲得點閱數
	 * 
	 * @param obj_pk
	 * @param obj_name
	 * @return long
	 */
	public long findSysCount(String obj_pk, String obj_name,String obj_time) {

		Sys_Count syscount = new Sys_Count();
		long count = 0;
		try {
			syscount = this.syscountDAO.find(obj_pk, obj_name,obj_time);
			if (syscount != null && !syscount.equals("")) {
				count = syscount.getCount();
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return count;
	}

	/***
	 * saveOrUpdateCount 通過obj_pk和obj_name新增和更新點閱數
	 * 
	 * @param obj_pk
	 * @param obj_name
	 * @return long
	 */
	public long saveOrUpdateCount(String obj_pk, String obj_name) {
		Sys_Count syscount = new Sys_Count();
		obj_name =obj_name.toUpperCase();
		SimpleDateFormat formatter; 
	    formatter = new SimpleDateFormat ("yyyy-MM-dd"); 
	    String ctime = formatter.format(new Date());
		long count = this.findSysCount(obj_pk, obj_name,ctime);
		count = count + 1;
		syscount.setCount(count);
		syscount.setObj_pk(obj_pk);
		syscount.setObj_name(obj_name);
		syscount.setObj_time(ctime);
		this.syscountDAO.saveOrUpdate(syscount);
		long sumCount = syscountDAO.findSumCount(obj_pk, obj_name);
		return sumCount;
	}
}