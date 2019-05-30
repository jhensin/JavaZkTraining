package com.tp.training_jhensin.yang.ctrler;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

import com.tp.baselib.model.MapBean;
import com.tp.baselib.model.MapBeanResultList;
import com.tp.baselib.util.Multilingual;
import com.tp.baselib.zul.Egrid;
import com.tp.baselib.zul.InputsContainer.EditEvent;
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
		String brandName = bean.get("BRAND_NAME");
		String brandNo = bean.get("BRAND_NO");
		if (brandName.isEmpty() || brandNo.isEmpty()) {
			MapBeanResultList data = TPDAOFactory.getWtBrandDAO().queryAll();
			this.indexLbox.setModel(new ListModelList<>(data));
			return true;
		} else {
			MapBeanResultList data = TPDAOFactory.getWtBrandDAO().queryNameNo(brandName, brandNo);
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

		@Override
		protected String[][] getUkColNames() {
			return new String[][] { { "BRAND_NO" }, { "BRAND_CODE" } };
		}

		@Override
		public void onBeforeSave(EditEvent event) throws Exception {
			super.onBeforeSave(event);
			Egrid grid = this.getEgrid();
			MapBean bean = grid.getBean();
			String brandCode = bean.get("BRAND_CODE");

			if(!brandCode.matches("[A-Za-z0-9]{2}")) {
				Component comp = grid.getChildByFld("BRAND_CODE");
				throw new WrongValueException(comp, Multilingual.getByUserLocale("jhensin.msg.checkInputboandcode", true, true));
			}
		}
	}
}
