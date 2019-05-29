package com.tp.training_jhensin.yang.ctrler;

import org.zkoss.zk.ui.select.annotation.Wire;

import com.tp.baselib.model.MapBean;
import com.tp.baselib.model.MapBeanResultList;
import com.tp.baselib.zul.Elistbox;
import com.tp.baselib.zul.ListModelList;
import com.tp.baselib.zul.Window;
import com.tp.training_jhensin.yang.dao.TPDAOFactory;
import com.tp.training_jhensin.yang.zul.TrainingBaseComposer;
import com.tp.training_jhensin.yang.zul.TrainingGeneralElistboxActionHandler;

public class BrandElistboxController extends TrainingBaseComposer {
	@Wire
	private Elistbox masterLbox;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		this.masterLbox.setActionHandler(new MasterActionHandler());

		MapBeanResultList data = TPDAOFactory.getWtBrandDAO().queryAll();
		this.masterLbox.setModel(new ListModelList<>(data));
	}

	private class MasterActionHandler extends TrainingGeneralElistboxActionHandler{

		public MasterActionHandler() {
			super( TPDAOFactory.getWtBrandDAO(), false );
		}
	}

	@Override
	public boolean onQuery(MapBean bean) throws Exception{
		String BName = bean.get("BRAND_NAME");
		String BNo = bean.get("BRAND_NO");
		System.out.println(BName);
		System.out.println(BNo);
		if(BName.isEmpty() || BNo.isEmpty()){
			return false;
		}
		MapBeanResultList data = TPDAOFactory.getWtBrandDAO().queryNameNo(BName, BNo);
		this.masterLbox.setModel(new ListModelList<>(data));
		return true;
	}

}