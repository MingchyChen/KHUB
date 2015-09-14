package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.mechanism.domain.Sys_Pagination;
import com.claridy.dao.ICategoryDAO;
import com.claridy.domain.Category;

@Service
public class SelCategoryService {
	@Autowired
	protected ICategoryDAO categorydao;

	/**
	 * 後臺分頁查詢
	 * 
	 * @param matter
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Sys_Pagination getCategoryList(String categoryname, String parent,
			String[] sort, int currentPage, int pageSize) {
		Sys_Pagination Sys_Pagination = categorydao.getSelCategoryList(
				categoryname, parent, sort, currentPage, pageSize);
		return Sys_Pagination;
	}

	/**
	 * 新增修改刪除方法
	 * 
	 * @param webopus
	 */
	public void editCategory(Category category) {
		categorydao.merge(category);
	}

	/**
	 * 根據主鍵查詢資訊
	 * 
	 * @param uuid
	 * @return
	 */
	public Category findCategory(String categorycode) {
		return categorydao.find(categorycode);
	}

	/**
	 * 查詢類別集合
	 * 
	 * @param parent
	 * @return
	 */
	public List<Category> findCategoryList(String parent) {
		List<Category> list = categorydao.findCategoryList(parent);
		return list;
	}

	/**
	 * 查詢菜單集合
	 * 
	 * @param parent
	 * @return
	 */
	public List<Category> findMenuList() {
		List<Category> categorylist = categorydao.findAll();
		return categorylist;
	}

	public List<Category> getParList(String sysparent) {
		List<Category> list = categorydao.findCategoryList(sysparent);
		return list;
	}

	public String getParentList(String sysparent) {
		String ajax = "";
		List<Category> list = categorydao.findCategoryList(sysparent);
		if ((list == null) || (list.isEmpty())) {
			ajax = "";
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("<option value=''>=請選擇=</option>");
			for (Category category : list) {
				sb.append("<option value='").append(category.getCategorycode())
						.append("'>").append(category.getCategoryname())
						.append("</option>");
			}
			ajax = sb.toString();
		}
		return ajax;
	}

	/**
	 * 根據上層分類獲取總數
	 * 
	 * @param parent
	 * @return
	 */
	public String getNum(String categorycode, String parent) {
		return categorydao.getNum(categorycode, parent);
	}
}
