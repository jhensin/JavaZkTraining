package com.tp.training_jhensin.yang.ctrler;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

import com.tp.baselib.model.MapBean;
import com.tp.baselib.model.MapBeanResultList;
import com.tp.baselib.util.Msgbox;
import com.tp.baselib.util.Multilingual;
import com.tp.baselib.zul.Egrid;
import com.tp.baselib.zul.Elistbox;
import com.tp.baselib.zul.InputsContainer.EditEvent;
import com.tp.baselib.zul.ListModelList;
import com.tp.baselib.zul.Listbox;
import com.tp.baselib.zul.Listitem;
import com.tp.baselib.zul.Window;
import com.tp.training_jhensin.yang.dao.TPDAOFactory;
import com.tp.training_jhensin.yang.zul.TrainingBaseComposer;
import com.tp.training_jhensin.yang.zul.TrainingGeneralElistboxActionHandler;
import com.tp.training_jhensin.yang.zul.TrainingGeneralListboxAndEgridActionHandler;

public class BrandEgridCotroller extends TrainingBaseComposer {

	private String mainBrandID;

	@Wire
	private Listbox indexLbox;
	@Wire
	private Egrid masterGrid;
	@Wire
	private Elistbox seasonLbox;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		this.masterGrid.setActionHandler(new MasterActionHandler());
		this.seasonLbox.setActionHandler(new SeasonActionHandler());

		super.doQuery(); // 預設帶出資料，若不需要，可不寫

		// MapBeanResultList data = TPDAOFactory.getWtBrandDAO().queryAll();
		// this.indexLbox.setModel(new ListModelList<>(data));
	}

	@Override
	public boolean onQuery(MapBean bean) throws Exception {
		String brandName = bean.get("BRAND_NAME");
		String brandNo = bean.get("BRAND_NO");
		if (brandName.isEmpty() && brandNo.isEmpty()) {
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

		this.mainBrandID = bean.get("BRAND_ID");
		MapBeanResultList detailData = TPDAOFactory.getWtBrandSeasonDao().getBySeasonFkBrandNo(this.mainBrandID);
		this.seasonLbox.setModel(new ListModelList<>(detailData));
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

			if (!brandCode.matches("[A-Z0-9]{2}")) {
				Component comp = grid.getChildByFld("BRAND_CODE");
				throw new WrongValueException(comp,
						Multilingual.getByUserLocale("jhensin.msg.checkInputboandcode", true, true));
			}
		}
		@Override
		public void onBeforeDelete (EditEvent event) throws Exception {
			super.onBeforeDelete(event);
			Egrid grid = this.getEgrid();
			MapBean bean = grid.getBean();
			String masterId = bean.get("BRAND_ID");
			MapBean count = TPDAOFactory.getWtBrandSeasonDao().getBySeasonFkBrandNoCount(masterId);
			Object rows = count.get("ROW_NUM");
			Integer rowNum = Integer.parseInt(rows.toString());
			if(rowNum > 0) {
				Msgbox.warn (Multilingual.getByUserLocale("jhensin.msg.hasDetailError", true, true));
				this.stop();
				return;
			}

		}
	}

	private class SeasonActionHandler extends TrainingGeneralElistboxActionHandler {

		public SeasonActionHandler() {
			super(TPDAOFactory.getWtBrandSeasonDao(), false);
		}

		@Override
		protected void initNewBean(MapBean bean, EditEvent event) throws Exception {
			// 這裡可用來對新增的 bean 給初始值
			MapBean masterBean = this.getMasterFocusedBean();
			String masterId = masterBean.get("BRAND_ID");
			bean.put("BRAND_ID", masterId); // 通常初始就會給主檔ID，以便 UK 檢查
		}

		@Override
		protected String[][] getUkColNames() {
			return new String[][] { { "#BRAND_ID","SEASON_NO" } };
		}
	}
}
