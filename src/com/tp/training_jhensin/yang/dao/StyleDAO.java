package com.tp.training_jhensin.yang.dao;

import java.sql.SQLException;

import com.tp.baselib.model.MapBean;
import com.tp.baselib.model.MapBeanResultList;

public class StyleDAO extends TrainingDBDAO {
	@Override
	public String getMainTableName() {
		return "ZK_JHENSIN.WT_STYLE";
	}

	public MapBean getByBrandSeasonNo(String QT) throws SQLException {
		return super.queryFirst("SELECT * FROM " + this.getMainTableName() + " WHERE STYLE_NO=?", QT);
	}

	public MapBean getOneRow() throws SQLException {
		return super.queryFirst("SELECT * FROM " + this.getMainTableName() + " WHERE ROWNUM = 1");
	}

	public MapBeanResultList queryAll() throws SQLException {
		return super.queryMapBeanResultList("SELECT * FROM " + this.getMainTableName() + " ORDER BY STYLE_NO");
	}

	public MapBeanResultList queryTreeData(String param) throws SQLException {
		return super.queryMapBeanResultList("SELECT A.* FROM " + this.getMainTableName() + " A" + " WHERE EXISTS("
				+ "SELECT 1 FROM " + this.getMainTableName() + " B" + " WHERE B.COL_A LIKE '%'|| ? ||'%'"
				+ " AND B. TREE_PATH LIKE '%/'|| A. XXX_ID ||'/%'" + ")" + " ORDER BY A.SORT", param);
	}

	public MapBeanResultList queryLevel0(String param) throws SQLException {
		return super.queryMapBeanResultList("SELECT  DISTINCT B.BRAND_NO as TREE_LABEL, B.BRAND_ID FROM "
				+ this.getMainTableName() + " A left join " + TPDAOFactory.getWtBrandDAO().getMainTableName()
				+ " B on A.BRAND_ID = B.BRAND_ID WHERE B.BRAND_ID like '%'|| ? ||'%' ORDER BY BRAND_NO ASC", param);
	}

	public MapBeanResultList queryLevel1(String brandId, String styleNo) throws SQLException {
		return super.queryMapBeanResultList(
				"SELECT DISTINCT SEASON_NO || SUBSTR(STYLE_YEAR, -2)  as SEASON, SEASON_NO || SUBSTR(STYLE_YEAR, -2)  as TREE_LABEL, STYLE_YEAR, SEASON_NO, BRAND_ID FROM "
				+ this.getMainTableName() + " WHERE BRAND_ID =? and STYLE_NO like '%'|| ? ||'%' group by BRAND_ID, STYLE_YEAR, SEASON_NO, SEASON_NO || SUBSTR(STYLE_YEAR, -2)", brandId, styleNo);
	}

	public MapBeanResultList queryLevel2(String brandId, String season, String styleNo) throws SQLException {
		return super.queryMapBeanResultList(
				"SELECT TB.*, TB.STYLE_NO as TREE_LABEL FROM " + this.getMainTableName() + " TB WHERE BRAND_ID=? and SEASON_NO || SUBSTR(STYLE_YEAR, -2)=? and STYLE_NO like '%'|| ? ||'%' ORDER BY STYLE_ID ASC",
				brandId, season, styleNo);
	}

	public MapBean getStyleData(String brandId,String year, String seasonNo) throws SQLException {
		//return super.queryFirst("SELECT DISTINCT SEASON_NO || SUBSTR(STYLE_YEAR, -2)  as SEASON, SEASON_NO || SUBSTR(STYLE_YEAR, -2)  as TREE_LABEL, STYLE_ID, BRAND_ID FROM " + this.getMainTableName() + " WHERE BRAND_ID=? and SEASON_NO || SUBSTR(STYLE_YEAR, -2) =?", brandId, season);
		return super.queryFirst("SELECT SEASON_NO || SUBSTR(STYLE_YEAR, -2)  as SEASON, SEASON_NO || SUBSTR(STYLE_YEAR, -2)  as TREE_LABEL, STYLE_YEAR, SEASON_NO, BRAND_ID FROM "
				+ this.getMainTableName() + " WHERE BRAND_ID =? and STYLE_YEAR =? and SEASON_NO =? group by BRAND_ID, STYLE_YEAR, SEASON_NO, SEASON_NO || SUBSTR(STYLE_YEAR, -2)", brandId, year, seasonNo);
	}
}
