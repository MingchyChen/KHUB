package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IermSysPhoneticCompareDAO;
import com.claridy.domain.ErmSysPhoneticCompare;
import com.claridy.domain.ErmSysSchedule;
import com.claridy.domain.WebEmployee;

@Repository
public class ErmSysPhoneticCompareDAO extends BaseDAO implements
		IermSysPhoneticCompareDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired  
	public ErmSysPhoneticCompareDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<ErmSysPhoneticCompare> findErmSys(ErmSysPhoneticCompare ermsys,WebEmployee webEmployee) {
		DetachedCriteria criteria = daoimpl.getCriteris(ErmSysPhoneticCompare.class,
				webEmployee);
		criteria.add(Restrictions.eq("isDataEffId", 1));
		if(ermsys.getCharacterCn()!=null&&!ermsys.getCharacterCn().equals("")){
			criteria.add(Restrictions.eq("characterCn", ermsys.getCharacterCn()));
		}
		if(ermsys.getUuid()!=null&&!ermsys.getUuid().equals("")){
			criteria.add(Restrictions.eq("uuid", ermsys.getUuid()));
		}
		
		List<ErmSysPhoneticCompare> ermsysList=criteria.getExecutableCriteria(getSession()).list();
		return ermsysList;
	}

}
