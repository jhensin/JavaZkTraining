package com.tp.training_jhensin.yang.dao;

public class TPDAOFactory {
	private static final BrandDao wtbrandDAO = new BrandDao();
	private static final BrandSeasonDAO wtBrandSeasonDAO = new BrandSeasonDAO();
	private static final StyleDAO wtStyleDAO = new StyleDAO();

	public static BrandDao getWtBrandDAO() {
		return wtbrandDAO;
	}

	public static BrandSeasonDAO getWtBrandSeasonDao() {
		return wtBrandSeasonDAO;
	}

	public static StyleDAO getStyleDAO() {
		return wtStyleDAO;
	}
}