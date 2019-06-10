package com.tp.training_jhensin.yang.dao;

import java.sql.SQLException;

import com.tp.baselib.model.MapBean;
import com.tp.baselib.model.MapBeanResultList;

public class BrandSeasonDAO extends TrainingDBDAO {
	@Override
	public String getMainTableName() {
		return "ZK_JHENSIN.WT_BRAND_SEASON";
	}

	public MapBeanResultList getBySeasonFkBrandNo(String QT) throws SQLException {
		return super.queryMapBeanResultList("SELECT * FROM " + this.getMainTableName() + " WHERE BRAND_ID=? ORDER BY SEASON_NO DESC", QT);
	}

	public MapBean getBySeasonFkBrandNoCount(String QT) throws SQLException {
		return super.queryFirst("SELECT count(*) as row_num FROM " + this.getMainTableName() + " WHERE BRAND_ID=? ORDER BY SEASON_NO DESC", QT);
	}

	public MapBeanResultList queryAll() throws SQLException {
		return super.queryMapBeanResultList("SELECT * FROM " + this.getMainTableName() + " ORDER BY SEASON_NO");
	}
}
