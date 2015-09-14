package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmSystemCkrsconDAO;
import com.claridy.domain.ErmSystemCkrscon;
import com.claridy.domain.WebEmployee;

@Repository
public class ErmSystemCkrsconDAO extends BaseDAO implements IErmSystemCkrsconDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public ErmSystemCkrsconDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	/**
	 * 查詢設定偵測資源時間對象
	 * @param uuid
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public ErmSystemCkrscon getErmSystemCkrscon(WebEmployee webEmployee){
		DetachedCriteria criteria=daoimpl.getCriteris(ErmSystemCkrscon.class, webEmployee);
		List<ErmSystemCkrscon> ermSystemCkrsconList = (List<ErmSystemCkrscon>) super
				.findByCriteria(criteria);
		if(ermSystemCkrsconList.size()>0){
			return ermSystemCkrsconList.get(0);
		}else{
			return new ErmSystemCkrscon();
		}
		/*Session session=this.getSession();
		Query query=session.createQuery("from ErmSystemCkrscon where isdataEffid=1");
		ErmSystemCkrscon ermSystemCkrscon=(ErmSystemCkrscon)query.uniqueResult();
		return ermSystemCkrscon;*/
	}
	
	@SuppressWarnings("unchecked")
	public ErmSystemCkrscon getErmSystemCkrsconAll(){
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmSystemCkrscon.class);
		List<ErmSystemCkrscon> ermSystemCkrsconsList=(List<ErmSystemCkrscon>) super.findByCriteria(criteria);
		if(ermSystemCkrsconsList.size()>0){
			return ermSystemCkrsconsList.get(0);
		}else{
			return new ErmSystemCkrscon();
		}
	}
}
