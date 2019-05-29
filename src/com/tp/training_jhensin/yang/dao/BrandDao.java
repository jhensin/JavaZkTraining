package com.tp.training_jhensin.yang.dao;

import java.sql.SQLException;

import com.tp.baselib.model.MapBean;
import com.tp.baselib.model.MapBeanResultList;

//略 import
public class BrandDao extends TrainingDBDAO {
	@Override
	public String getMainTableName() {
		return "ZK_JHENSIN.WT_BRAND";
	}

//以下依實際需要來實作，下面只是範例
	public MapBean getByBrandName(String QT) throws SQLException {
		return super.queryFirst("SELECT * FROM " + this.getMainTableName() + " WHERE BRAND_NAME=?", QT);
	}

	public MapBeanResultList queryAll() throws SQLException {
		return super.queryMapBeanResultList("SELECT * FROM " + this.getMainTableName() + " ORDER BY BRAND_NAME");
	}

	public MapBeanResultList queryNameNo(String BName,String BNo) throws SQLException {
		return super.queryMapBeanResultList(
				"SELECT * FROM "+this.getMainTableName()+
				" WHERE BRAND_NAME LIKE '%'|| ? ||'%' AND BRAND_NO LIKE '%'|| ? ||'%'" +
				" ORDER BY BRAND_NAME"
				, BName, BNo);
	}
	public MapBeanResultList queryNo(String BNo) throws SQLException {
		return super.queryMapBeanResultList(
				"SELECT * FROM "+this.getMainTableName()+
				" WHERE BRAND_NO LIKE '%'|| ? ||'%'" +
				" ORDER BY BRAND_NAME"
				, BNo);
	}
}
