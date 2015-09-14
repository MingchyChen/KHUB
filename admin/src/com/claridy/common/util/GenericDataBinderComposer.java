package com.claridy.common.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;

abstract public class GenericDataBinderComposer extends GenericAutowireComposer {
	private static final long serialVersionUID = -8962566563467903754L;
	protected DataBinder binder;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		binder = new AnnotateDataBinder(comp);
		comp.setAttribute("binder", binder);
	}

	protected void loadAll() {
		binder.loadAll();
	}

	protected void loadAttribute(Component comp, String attr) {
		binder.loadAttribute(comp, attr);
	}

	protected void loadComponent(Component comp) {
		binder.loadComponent(comp);
	}

}
