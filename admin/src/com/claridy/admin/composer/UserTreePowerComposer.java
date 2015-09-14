package com.claridy.admin.composer;

import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import com.claridy.common.mechanism.facase.SysMenuService;
import com.claridy.common.util.PowerTreeModel;
import com.claridy.domain.WebFunction;

/**
 * 
 * jianguo nj
 * 菜單權限功能
 * 2014/07/18
 */
public class UserTreePowerComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3369391370563245215L;
	@Wire
	private Tree tree;
	@Wire
	private Checkbox checkAll;
	@Wire
	private PowerTreeModel btm;
	@Wire
	private SysMenuService menuService;

	private List<WebFunction> webFunctionList;
	
	public UserTreePowerComposer() {
		String languageType = null;
		Session session = Sessions.getCurrent();
		if (session.getAttribute("now_Locale") != null) {
			languageType = session.getAttribute("now_Locale").toString();
		}
		menuService = (SysMenuService) SpringUtil.getBean("sysMenuService");
		webFunctionList = menuService.findMenuList(languageType);
		btm = new PowerTreeModel(webFunctionList, menuService);
	}
	
//	@Command
//    @NotifyChange("*")
//    public void checkAll(final Event e){
//		for(int i=0;i<tree.getChildren().size();i++){
//			System.out.println(tree.getChildren().getClass().getName());
//		}
//		for (WebFunction webFunction : webFunctionList) {
//			reverseCheck(webFunction);
//			checkChild(webFunction);
//		}
//		
////        for (final Action action : actions) {
////            action.setChecked(true);
////        }
//    }
	
//	private void checkChild(WebFunction webFunction){
//		if(!btm.isLeaf(webFunction)){
//			String languageType=null;
//			Session session=Sessions.getCurrent();
//			if(session.getAttribute("now_Locale")!=null){
//				languageType=session.getAttribute("now_Locale").toString();
//			} 
//			List<WebFunction> list = menuService.getChildMenu(webFunction,languageType);
//			if(list != null && list.size() > 0){
//				for(WebFunction webFunctionC : list ){
//					reverseCheck(webFunctionC);
//					checkChild(webFunctionC);
//				}
//			}
//		}
//	}
	
	@Listen("onCheck=#checkAll")
	public void checkAll() {
//		openAll();
//		for (WebFunction webFunction : webFunctionList) {
//			reverseCheck(webFunction);
//			checkChild(webFunction);
//		}
		
		Treechildren treechildren = (Treechildren) tree.getTreechildren();
		List<Component> list = treechildren.getChildren();
		for (Component treeitem : list) {
			if(treeitem instanceof Treeitem){
				checkChild((Treeitem)treeitem,checkAll.isChecked());
			}
			// Checkbox checkBox = (Checkbox)treeitem;
			// checkBox.setChecked(true);
//			getTreecell((Treeitem)treeitem);
			
			// checkChild(treeitem);
		}
//		for (WebFunction webFunction : webFunctionList) {
//			webFunction.setChecked(true);
//		}
//		// //btm=new PowerTreeModel(webFunctionList,menuService);
//		// btm.setSelection(webFunctionList);
//		// System.out.println("=====================: "+
//		// btm.getSelectionCount());;
//		// tree.setModel(btm);
//		// System.out.println("=====================: "+list.size());
////		tree.selectAll();
////		tree.selectItem(item)
	}
	
//	@Command
//	public void openChild(){
//		System.out.println("===========================");
//	}
	
	private void checkChild(Treeitem treeitem,Boolean checked){
//		treeitem.setCheckable(true);
		
//		treeitem.setSelected(checked);
//		treeitem.getChildren()
		for(Component treerow : treeitem.getChildren()){
			for(Component treecellComponent : treerow.getChildren()){
				if(treecellComponent instanceof Treeitem){
					checkChild((Treeitem)treecellComponent,checked);
				}else if(treecellComponent instanceof Treecell){
					Treecell treecell = (Treecell)treecellComponent;
					if(treecell.getChildren() != null && treecell.getChildren().size() > 0){
						Component checkboxComponent = treecell.getChildren().get(0);
						if(checkboxComponent instanceof Checkbox){
							Checkbox checkbox = (Checkbox)checkboxComponent;
							checkbox.setChecked(checked);	
						}
					}
					
				}
			}
		}
	}
	
//	private void openAll(){
//		Treechildren treechildren = (Treechildren) tree.getTreechildren();
////		if(treechildren != null){
//			List<Component> list = treechildren.getChildren();
//			for (Component treeitem : list) {
//				if(treeitem instanceof Treeitem){
//					openChild((Treeitem)treeitem);
//				}
//			}
////		}
//	
//	}
//	
//	private void openChild(Treeitem treeitem){
//		treeitem.setOpen(true);
//		
//		for(Component treerow : treeitem.getChildren()){
//			for(Component treecellComponent : treerow.getChildren()){
//				if(treecellComponent instanceof Treeitem){
//					openChild((Treeitem)treecellComponent);
//				}
//			}
//		}
//	}
	
//	@Listen("onSelect = #tree")
//	public void onSelect(SelectEvent<Treeitem, String> event) {
//        Treeitem ref = event.getReference();
////        openChild(ref);
//    	checkChild(ref,ref.isSelected());
//    	
//    	Boolean bflag = ref.isSelected();
//    	for(Component treecellComponent : ref.getParent().getChildren()){
//    		Boolean bnear = ((Treeitem)treecellComponent).isSelected();
//    		if(bflag != bnear){
//    			bflag = bnear;
//    			break;
//    		}
//    		
//    	}
//    	if(bflag == ref.isSelected()){
//    		if(ref.getParentItem() != null){
//    			ref.getParentItem().setSelected(bflag);
//    		}
////    		ref.getParentItem();
//    		//System.out.println(((Treechildren)ref.getParent()).getp);
////    		for(Component nearTreeitemC : ){
////    			((Treeitem)nearTreeitemC).setSelected(bflag);
////    		}
//    		
//    	}
//    	if(bflag != ref.isSelected() && ref.isSelected() == false){
//    		if(ref.getParentItem() != null){
//    			ref.getParentItem().setSelected(false);
//    		}
//    	}
//    	
//    	checkAllSelected();
////        Set<Treeitem> newSelection = new HashSet<Treeitem>(event.getSelectedItems());
////        if (ref.isSelected()) {
////            if (selected != null) {
////                Set<Treeitem> deselected = new HashSet<Treeitem>(selected);
////                deselected.removeAll(newSelection);
////                for (Treeitem t : deselected) {
////                    System.out.println("Deselected " + t.getLabel());
////                }
////            }
////            System.out.println("Selected " + ref.getLabel());
////        } else {
////        	System.out.println("Deselected " + ref.getLabel());
////        }
////        selected = newSelection;
//    }
	
	private void checkAllSelected(){
		Boolean bflag = true;
		Treechildren treechildren = (Treechildren) tree.getTreechildren();
		List<Component> list = treechildren.getChildren();
		for (Component treeitem : list) {
			if(treeitem instanceof Treeitem){
				Checkbox checkbox = (Checkbox)treeitem.getLastChild().getFirstChild().getFirstChild();
				if(!checkbox.isChecked()){
					bflag = false;
					break;
				}
//				checkChild((Treeitem)treeitem,checkAll.isChecked());
			}			
		}
		checkAll.setChecked(bflag);
	}
	
//	private void getTreecell(Treeitem treeitem){
//		for(Component treerow : treeitem.getChildren()){
//			for(Component treecellComponent : treerow.getChildren()){
//				System.out.println(treecellComponent.getClass().getName());
//				
//				if(treecellComponent instanceof Treeitem){
//					getTreecell((Treeitem)treecellComponent);
//					tree.selectItem((Treeitem)treecellComponent);
////					for(Component treerow2 : treecellComponent.getChildren()){
////						System.out.println(treerow2.getChildren().getClass().getName());
////					}
//				}else{
//					Treecell treecell = (Treecell) treecellComponent;
//					
//					System.out.println("==============="+treecell.getContext()+":"+treecell.getLabel());
//				}
//			}
//		}
//	}

//	private void checkChild(Component treeitem) {
//		if (treeitem != null) {
//			List<Component> list = treeitem.getChildren();
//			if (list != null) {
//				// for(Component treeitem:list){
//				// checkChild(treeitem);
//				// }
//			}
//		}
//	}
	
//	private void reverseCheck(WebFunction webFunction){
//		if(webFunction != null){
//			if(webFunction.isChecked()){
//				webFunction.setChecked(false);
//			}else{
//				webFunction.setChecked(true);
//			}
//		}
//	}

	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		tree.setModel(btm);
//		tree.setMultiple(true);
//		tree.setCheckmark(true);
		tree.setTreeitemRenderer(new TreeitemRenderer<WebFunction>() {
			public void render(Treeitem item, WebFunction data, int index)
					throws Exception {
				item.setOpen(true);
				Treerow treerow = new Treerow();
				item.appendChild(treerow);
				final Treecell treecell = new Treecell();
				treerow.appendChild(treecell);
				final Checkbox checkbox = new Checkbox();
				checkbox.setValue(data.getUuid());
				checkbox.setLabel(Labels.getLabel(data.getMutiFuncId()));
				checkbox.addEventListener("onCheck", new EventListener<CheckEvent>() {

					public void onEvent(CheckEvent event) throws Exception {
						Checkbox checkbox = (Checkbox)event.getTarget();
						Treeitem ref = (Treeitem)checkbox.getParent().getParent().getParent();
//				        openChild(ref);
				    	checkChild(ref,checkbox.isChecked());
				    	
				    	Boolean bflag = checkbox.isChecked();
				    	for(Component treeComponent : ref.getParent().getChildren()){
				    		Component treecellComponent = treeComponent.getFirstChild().getFirstChild();
				    		if(treecellComponent instanceof Treecell){
				    			Treecell treecell = (Treecell)treeComponent.getFirstChild().getFirstChild();
								Component checkboxComponent = treecell.getFirstChild();
								if(checkboxComponent instanceof Checkbox){
									Boolean bnear = ((Checkbox)checkboxComponent).isChecked();
						    		if(bflag != bnear){
						    			bflag = bnear;
						    			break;
						    		}	
								}
				    		}
				    		
				    	}
						if (bflag == checkbox.isChecked()) {
							if (ref.getParentItem() != null) {
								Treecell treecell = (Treecell) ref.getParentItem().getLastChild().getFirstChild();
								if (treecell != null) {
									Component checkboxComponent = treecell.getFirstChild();
									if (checkboxComponent instanceof Checkbox) {
										Checkbox parentCheckbox = (Checkbox) checkboxComponent;
										parentCheckbox.setChecked(bflag);
									}
								}
								// ref.getParentItem().setSelected(bflag);
							}
							// ref.getParentItem();
							// System.out.println(((Treechildren)ref.getParent()).getp);
							// for(Component nearTreeitemC : ){
							// ((Treeitem)nearTreeitemC).setSelected(bflag);
							// }

						}
				    	if(bflag != checkbox.isChecked() && checkbox.isChecked() == false){
				    		if(ref.getParentItem() != null){
				    			Treecell treecell = (Treecell) ref.getParentItem().getLastChild().getFirstChild();
				    			if(treecell != null){
				    				Component checkboxComponent = treecell.getFirstChild();
				    				if(checkboxComponent instanceof Checkbox){
				    					((Checkbox)checkboxComponent).setChecked(false);
				    				}
				    			}
//				    			ref.getParentItem().setSelected(false);
				    		}
				    	}
				    	
				    	checkAllSelected();
					}
				  });

				treecell.appendChild(checkbox);

				// item.setLabel(data.getFuncName());
			}
		});
//		tree.onInitRender();
//		tree.getTreechildren()
//		tree.isInvalidated();
	}

	@Override
	public void doFinally() throws Exception {
		super.doFinally();
		//openAll();
	}

	public PowerTreeModel getBtm() {
		return btm;
	}

	public List<WebFunction> getWebFunctionList() {
		return webFunctionList;
	}

	public void setWebFunctionList(List<WebFunction> webFunctionList) {
		this.webFunctionList = webFunctionList;
	}
	
}
