package com.tp.training_jhensin.yang.zul;

import com.tp.baselib.model.MapBeanDAO;
import com.tp.webplugins.admin.zul.GeneralTreeAndEgridActionHandler;

public class TrainingGeneralTreeAndEgridActionHandler extends GeneralTreeAndEgridActionHandler {
	public TrainingGeneralTreeAndEgridActionHandler(MapBeanDAO dao, Boolean needDatalog) {
		super(dao, needDatalog);
	}

	public TrainingGeneralTreeAndEgridActionHandler(MapBeanDAO dao) {
		super(dao);
	}
}