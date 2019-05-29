package com.tp.training_jhensin.yang.dao;

public class TPDAOFactory {
	private static final BrandDao wtbrandDAO = new BrandDao();

	public static BrandDao getWtBrandDAO() {
		return wtbrandDAO;
	}

}