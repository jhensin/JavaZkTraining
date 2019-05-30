

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import org.logicalcobwebs.proxool.ProxoolException;

import com.tp.baselib.model.Schemas;
import com.tp.baselib.util.ResourceUtils;

public class SchemasGenerator {
	public static void main(String[] args) throws IOException, ProxoolException, SQLException {

		URL url = ResourceUtils.getResource("/");
		String classPath = url.getPath();
		String prjPath = classPath.substring(0, classPath.indexOf("/build/"));
		String proxoolXmlPath = prjPath + "/WebContent/WEB-INF/proxool.xml";
		Schemas.generateSchemaInfo(prjPath + "/src/schemas_tmp.txt" // 輸出位置
				, false // 是否要附加在原內容後面,false 則取代
				, proxoolXmlPath // 用於取連線資訊
				, "zk_js" // 連線名稱
				, null // owner 名,如果 proxool.xml 中的帳號即是 owner,則這裡可 null
				, "WT_%" // tablePattern,格式應該是同 like 語法,預設 "%" (見下方說明)
				, new String[] { "^(?=ADMIN_)\\w+$" }); // ignorePatterns (見下方說明)
// 產生後請再自行做一些人工補充或修正
	}
}