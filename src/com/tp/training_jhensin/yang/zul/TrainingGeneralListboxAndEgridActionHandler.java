package com.tp.training_jhensin.yang.zul;

import com.tp.baselib.model.MapBeanDAO;
import com.tp.webplugins.admin.zul.GeneralListboxAndEgridActionHandler;

public class TrainingGeneralListboxAndEgridActionHandler extends GeneralListboxAndEgridActionHandler {
	public TrainingGeneralListboxAndEgridActionHandler(MapBeanDAO dao, Boolean needDatalog) {
		super(dao, needDatalog);
	}

	public TrainingGeneralListboxAndEgridActionHandler(MapBeanDAO dao) {
		super(dao);
	}

	@Override
	protected String[][] getUkColNames(){
		return new String[][] {{"BRAND_NO"},{"BRAND_CODE"}};
	}
}