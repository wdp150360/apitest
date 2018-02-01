package base.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.constants.CommonConstants;
import base.exception.CSVException;
import base.util.CSVUtils;

/**
 * 用来加载testcase
 */
public class TestCaseLoader {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseLoader.class);

    private String fileName;

    public TestCaseLoader(String fileName) {
        this.fileName = fileName;
    }

    //定义文件是否存在
    private List<List<Object>> getCaseContentList(String base) {
        try {
            return CSVUtils.getCSVDataList(getFilePath(base));
        } catch (Exception ex) {
            logger.warn("Get csv datalist error {}", ex);
            throw new CSVException(CSVException.CSV_FILE_NOT_EXISTS);
        }

    }

    private String getFilePath(String base) {
        logger.debug("Base dir is: {}", base);
        return FilenameUtils.concat(base, fileName);
    }

    public Object[][] loadCases() {
        return loadCases(CommonConstants.TEST_CASE_CONTENT_BASE_DIR);
    }

    /**
     * 从csv文件获得数据，自动组装成dataProvider需要的数据
     *
     * @return object[][]
     */
    public Object[][] loadCases(String base) {
        List<List<Object>> caseContentList = getCaseContentList(base);
        Object[][] resultObject = new Object[caseContentList.size()][caseContentList.get(0).size()];
        try {
            for (int i = 0; i < caseContentList.size(); ++i) {
                List<Object> caseRow = caseContentList.get(i);
                for (int j = 0; j < caseRow.size(); ++j) {
                    resultObject[i][j] = caseRow.get(j);
                }
            }
        } catch (Exception ex) {
            throw new CSVException(CSVException.CSV_FILE_READ_FAIL);
        } finally {
            return resultObject;
        }
    }

    public List<Map<String, Object>> getParams() {
        return getParams(CommonConstants.TEST_CASE_CONTENT_BASE_DIR);
    }

    /**
     * 从csv文件中获得List，每个List中的对象是个Map，这个Map是csv文件的每一行
     * Map的key是csv的第一行
     *
     * @return
     */
    public List<Map<String, Object>> getParams(String base) {
        try {
            return CSVUtils.getCSVDataMap(getFilePath(base));
        } catch (Exception ex) {
            logger.error("get csv data map error {}", ex);
            throw new CSVException(CSVException.CSV_FILE_NOT_EXISTS);
        }
    }

}
