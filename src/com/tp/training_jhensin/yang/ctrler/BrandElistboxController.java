package com.tp.training_jhensin.yang.ctrler;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

import com.tp.baselib.model.MapBean;
import com.tp.baselib.model.MapBeanResultList;
import com.tp.baselib.util.Msgbox;
import com.tp.baselib.util.Multilingual;
import com.tp.baselib.zul.Elistbox;
import com.tp.baselib.zul.InputsContainer.EditEvent;
import com.tp.baselib.zul.ListModelList;
import com.tp.baselib.zul.Listitem;
import com.tp.baselib.zul.Window;
import com.tp.training_jhensin.yang.dao.TPDAOFactory;
import com.tp.training_jhensin.yang.zul.TrainingBaseComposer;
import com.tp.training_jhensin.yang.zul.TrainingGeneralElistboxActionHandler;

public class BrandElistboxController extends TrainingBaseComposer {
	@Wire
	private Elistbox masterLbox;
	@Wire
	private Elistbox seasonLbox;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		this.masterLbox.setActionHandler(new MasterActionHandler());
		this.seasonLbox.setActionHandler(new SeasonActionHandler());

		MapBeanResultList data = TPDAOFactory.getWtBrandDAO().queryAll();
		this.masterLbox.setModel(new ListModelList<>(data));


		//this.seasonLbox.setModel(new ListModelList<>());
	}

	private class MasterActionHandler extends TrainingGeneralElistboxActionHandler{

		public MasterActionHandler() {
			super( TPDAOFactory.getWtBrandDAO(), false );
		}


		@Override
		protected String[][] getUkColNames() {
			return new String[][] { { "BRAND_NO" }, { "BRAND_CODE" } };
		}

		@Override
		public void onBeforeSave(EditEvent event) throws Exception {
			super.onBeforeSave(event);
			Elistbox elistbox = this.getElistbox();
			List<Listitem> items = elistbox.getSortedSelectedItems();
			for(Listitem item : items){
				MapBean bean = item.getBean();
				String columnValue = bean.get("BRAND_CODE");
				this.checkBrandCode(columnValue, item, "BRAND_CODE");
			}
		}

		public void checkBrandCode(String columnValue, Listitem item, String columnName) {
			if (!columnValue.matches("[A-Z0-9]{2}")) {
				Component comp = item.getChildByFld(columnName);
				throw new WrongValueException(comp,
						Multilingual.getByUserLocale("jhensin.msg.checkInputboandcode", true, true));
			}
		}

		@Override
		public void onBeforeDelete (EditEvent event) throws Exception {
			super.onBeforeDelete(event);
			Elistbox elistbox = this.getElistbox();
			List<Listitem> items = elistbox.getSortedSelectedItems();
			for(Listitem item : items){
				MapBean bean = item.getBean();
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
	}

	@Override
	public boolean onQuery(MapBean bean) throws Exception{
		String brandName = bean.get("BRAND_NAME");
		String brandNo = bean.get("BRAND_NO");
		if (brandName.isEmpty() && brandNo.isEmpty()) {
			MapBeanResultList data = TPDAOFactory.getWtBrandDAO().queryAll();
			this.masterLbox.setModel(new ListModelList<>(data));
			return true;
		} else {
			MapBeanResultList data = TPDAOFactory.getWtBrandDAO().queryNameNo(brandName, brandNo);
			this.masterLbox.setModel(new ListModelList<>(data));
			return true;
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
			return new String[][] { { "BRAND_ID","SEASON_NO" } };
		}

	}

	@Listen("onItemFocus=#masterLbox")
	public void onLboxItemFocus() throws Exception {
		Listitem focusedItem = this.masterLbox.getFocusedItem();
		MapBean bean = focusedItem.getValue();
		//this.masterGrid.setBean(bean); // 將資料帶入至右邊的 Egrid

		String masterId = bean.get("BRAND_ID");
		MapBeanResultList detailData = TPDAOFactory.getWtBrandSeasonDao().getBySeasonFkBrandNo(masterId);
		this.seasonLbox.setModel(new ListModelList<>(detailData));
	}
}