package com.tp.training_jhensin.yang.zul;

import com.tp.baselib.model.MapBeanDAO;
import com.tp.webplugins.admin.zul.GeneralElistboxActionHandler;

public class TrainingGeneralElistboxActionHandler extends GeneralElistboxActionHandler {
	public TrainingGeneralElistboxActionHandler(MapBeanDAO dao, Boolean needDatalog) {
		super(dao, needDatalog);
	}

	public TrainingGeneralElistboxActionHandler(MapBeanDAO dao) {
		super(dao);
	}
}