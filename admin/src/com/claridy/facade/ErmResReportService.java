package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmResReportDAO;
import com.claridy.domain.ErmResourcesRscon;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.WebEmployee;

@Service
public class ErmResReportService {
	@Autowired
	private IErmResReportDAO ErmResReportDAO;

	public List<Object> findErmResourcesRsconAll(int firstYear, int secondYear) {
		return ErmResReportDAO.findErmResourcesRsconAll(firstYear, secondYear);
	}

	public List<Object> findSendReport(int firstYear, String firstMonth,
			int secondYear, String secondMonth) {
		return ErmResReportDAO.findSendReport(firstYear, firstMonth,
				secondYear, secondMonth);
	}


}
