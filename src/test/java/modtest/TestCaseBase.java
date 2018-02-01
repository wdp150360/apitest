package modtest;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import base.service.TestCaseLoader;
import common.Environment;

@ContextConfiguration(locations = {"classpath*:spring-*.xml"})
public class TestCaseBase extends AbstractTestNGSpringContextTests {
    public static Environment environment;
    protected static URL BASE_URL;
    private static Logger logger = LoggerFactory.getLogger(TestCaseBase.class);
    protected String caseFileName;

    @Parameters({"targetEnvironment"})
    @BeforeTest(alwaysRun = true)
    public void beforeTestRun(@Optional("test") String targetEnvironment) {
        try {
            logger.debug("Target Environment is {}", targetEnvironment);
            BASE_URL = new URL(Environment.getUrl(targetEnvironment));
        } catch (MalformedURLException e) {
            logger.error("访问URL生成出错，环境为{}", targetEnvironment);
        }

    }

    @DataProvider(name = "dataprovider")
    public Object[][] dataProvider() {
        return new TestCaseLoader(caseFileName).loadCases();
    }

}
