package com.tp.training_jhensin.yang.ctrler;

import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

import com.tp.baselib.model.MapBean;
import com.tp.baselib.model.MapBeanResultList;
import com.tp.baselib.zul.Egrid;
import com.tp.baselib.zul.ListModelList;
import com.tp.baselib.zul.Listbox;
import com.tp.baselib.zul.Listitem;
import com.tp.baselib.zul.Window;
import com.tp.training_jhensin.yang.dao.TPDAOFactory;
import com.tp.training_jhensin.yang.zul.TrainingBaseComposer;
import com.tp.training_jhensin.yang.zul.TrainingGeneralListboxAndEgridActionHandler;

public class BrandEgridCotroller extends TrainingBaseComposer {

	@Wire
	private Listbox indexLbox;
	@Wire
	private Egrid masterGrid;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		this.masterGrid.setActionHandler(new MasterActionHandler());
		super.doQuery(); // 預設帶出資料，若不需要，可不寫

		// MapBeanResultList data = TPDAOFactory.getWtBrandDAO().queryAll();
		// this.indexLbox.setModel(new ListModelList<>(data));
	}

	@Override
	public boolean onQuery(MapBean bean) throws Exception {
		String BName = bean.get("BRAND_NAME");
		String BNo = bean.get("BRAND_NO");
		// System.out.println(BName);
		// System.out.println(BNo);
		if (BName.isEmpty() || BNo.isEmpty()) {
			MapBeanResultList data = TPDAOFactory.getWtBrandDAO().queryAll();
			this.indexLbox.setModel(new ListModelList<>(data));
			return false;
		} else {
			MapBeanResultList data = TPDAOFactory.getWtBrandDAO().queryNameNo(BName, BNo);
			this.indexLbox.setModel(new ListModelList<>(data));
			return true;
		}
	}

	@Listen("onItemFocus=#indexLbox")
	public void onIndexItemFocus() throws Exception {
		Listitem focusedItem = this.indexLbox.getFocusedItem();
		MapBean bean = focusedItem.getValue();
		this.masterGrid.setBean(bean); // 將資料帶入至右邊的 Egrid
	}

	private class MasterActionHandler extends TrainingGeneralListboxAndEgridActionHandler {

		public MasterActionHandler() {
			super(TPDAOFactory.getWtBrandDAO(), false);
		}
	}
}
