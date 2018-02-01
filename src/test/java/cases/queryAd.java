package cases;

import java.net.URL;
import java.util.Map;

import modtest.TestCaseBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.builder.HttpAssertorBuilder;


import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

/*
 * auth:tianzhenbiao
 * /service/ad/queryAd  查询广告
 */

public class queryAd extends TestCaseBase {

	private final Logger logger = LoggerFactory.getLogger(queryAd.class);

	//加载数据
	@DataProvider
	public Object[][] dataProvider() {
		caseFileName = "queryAd.csv";
		return super.dataProvider();
	}

	
	@Test(dataProvider = "dataProvider")
	public void queryAd(String path,
			String caseName, Map<String, String> keyValue) throws HttpRequestException, Exception {
		//可增加多个节点Map
		
		//封装URL地址
		URL reqUrl = new URL(BASE_URL, path);
		HttpRequest httpRequest = HttpRequest.get(reqUrl);
		//logger.debug("");

	    //校验指定节点数据
		HttpAssertorBuilder httpAssertorBuilder = new HttpAssertorBuilder(
				httpRequest, caseName);

		httpAssertorBuilder.status(200).jsonKeyValueCompare(keyValue)
				.doAssertion();
		//可增加多子节点校验，语法为：data.bannerlist.xxx

		
	}

}
