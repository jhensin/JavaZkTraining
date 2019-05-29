package com.tp.training_jhensin.yang.dao;

import com.tp.baselib.model.MapBeanDAO;

//略 import
public abstract class TrainingDBDAO extends MapBeanDAO {
	@Override
	public String getMainDBName() {
		return "zk_js";
	}
}