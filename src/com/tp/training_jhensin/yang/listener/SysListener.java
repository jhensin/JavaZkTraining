package com.tp.training_jhensin.yang.listener;

import java.net.URL;

import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppCleanup;
import org.zkoss.zk.ui.util.WebAppInit;

import com.tp.baselib.model.Schemas;
import com.tp.baselib.util.DbConnHelper;
import com.tp.baselib.util.ResourceUtils;
import com.tp.webplugins.admin.util.AdminZkListener;


public class SysListener implements WebAppInit, WebAppCleanup {
	@Override
	public void init(WebApp wapp) throws Exception {
// 載入 proxool 設定

		String proxoolXmlPath = wapp.getRealPath("/WEB-INF/proxool.xml");
		JAXPConfigurator.configure(proxoolXmlPath, false);

		// 載入 schemas 設定
		URL schemasUrl = ResourceUtils.getResource("/schemas_training_jhensin.yang.txt");
		Schemas.loadSchemas(schemasUrl);

		try {
			AdminZkListener.init(wapp);
		} catch (Exception e) {
			throw e;
		} finally {
			DbConnHelper.closeThreadLocal();
		}
	}

	@Override
	public void cleanup(WebApp wapp) throws Exception {
		AdminZkListener.cleanup(wapp);
		DbConnHelper.closeThreadLocal();
	}
}
