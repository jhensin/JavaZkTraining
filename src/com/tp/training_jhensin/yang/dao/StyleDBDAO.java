package com.tp.training_jhensin.yang.dao;

import java.sql.SQLException;

import com.tp.baselib.model.MapBean;
import com.tp.baselib.model.MapBeanResultList;

public class StyleDBDAO extends TrainingDBDAO {
	@Override
	public String getMainTableName() {
		return "ZK_JHENSIN.WT_BRAND_SEASON";
	}

	public MapBean getByBrandSeasonNo(String QT) throws SQLException {
		return super.queryFirst("SELECT * FROM " + this.getMainTableName() + " WHERE STYLE_NO=?", QT);
	}

	public MapBeanResultList queryAll() throws SQLException {
		return super.queryMapBeanResultList("SELECT * FROM " + this.getMainTableName() + " ORDER BY STYLE_NO");
	}
}
