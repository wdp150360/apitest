package base.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.exception.CSVException;
import base.replacer.VariableReplacer;
import au.com.bytecode.opencsv.CSVReader;

public final class CSVUtils {
    private static Logger logger = LoggerFactory.getLogger(CSVUtils.class);
	
    /*
     * 判断csvFileName是否存在
     */
    private static CSVReader loadCSVResource(String csvFileName) {
        CSVReader csvReader = null;
        try {
            if (StringUtils.isBlank(csvFileName)) {
                throw new CSVException(CSVException.CSV_FILE_NAME_IS_BLANK);
            }
            String path = CSVUtils.class.getClassLoader().getResource("").getPath();
            // for chinese encoding problem
            File file = new File(URLDecoder.decode(FilenameUtils.concat(path, csvFileName), "utf-8"));
            if (!file.exists()) {
                throw new CSVException(CSVException.CSV_FILE_NOT_EXISTS);
            }
            FileReader fReader = new FileReader(file);
            csvReader = new CSVReader(fReader);
        } catch (CSVException ex) {
            throw new CSVException(ex.getMessage());
        } catch (Exception ex) {
            throw new CSVException(CSVException.CSV_FILE_LOAD_FAIL);
        }
        return csvReader;
    }

    /**
     * List返回csv的每一行，[]的每一条内容被解析成Map，其余的解析成String
     *
     * @param csvFileName
     *
     * @return List<List<Object>>
     */
    public static List<List<Object>> getCSVDataList(String csvFileName) {
        Map<String, String> variableValueMap = new HashMap<String, String>();
        CSVReader csvReader = loadCSVResource(csvFileName);
        if (csvReader == null) {
            throw new CSVException(CSVException.CSV_FILE_LOAD_FAIL);
        }
        List<List<Object>> resultSet = new ArrayList<List<Object>>();
        try {
            String[] keys = csvReader.readNext();
            List<String[]> data = csvReader.readAll();
            for (int linePos = 0; linePos < data.size(); linePos++) {
                String[] row = data.get(linePos);
                if (row.length != keys.length) {
                    logger.warn("key length wrong, expect length is {}, but now is {}", keys.length, row.length);
                    continue;
                }
                List<Object> values = new ArrayList<Object>();
                for (int rowPos = 0; rowPos < row.length; rowPos++) {
                    String element = row[rowPos];
                    List<Object> tempList = CSVElementUtils.fileterElement(element);
                    values.add(tempList.get(0));
                    Map<String, String> _variableValueMap = (Map<String, String>) tempList.get(1);
                    if (!_variableValueMap.isEmpty()) {
                        for (Map.Entry<String, String> entry : _variableValueMap.entrySet()) {
                            variableValueMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
                // 行变量替换
                if (!variableValueMap.isEmpty()) {
                    for (int valuePos = 0; valuePos < values.size(); ++valuePos) {
                        Object element = values.get(valuePos);
                        if (element instanceof Map) {
                            // Map类型的element替换变量值
                            for (Map.Entry<String, String> entry : ((Map<String, String>) element).entrySet()) {
                                String filteredElement =
                                        new VariableReplacer(entry.getValue()).setVariableValueMap(variableValueMap)
                                                .doReplacement();
                                ((Map<String, String>) element).put(entry.getKey(), filteredElement);
                            }
                            values.set(valuePos, element);
                        } else if (element instanceof String) {
                            String filteredElement =
                                    new VariableReplacer((String) element).setVariableValueMap(variableValueMap)
                                            .doReplacement();
                            values.set(valuePos, filteredElement);
                        }
                    }
                }
                resultSet.add(values);
            }
        } catch (Exception ex) {
//            throw new CSVException(CSVException.CSV_FILE_READ_FAIL);
            throw new CSVException(CSVException.CSV_FILE_PARSE_FAIL1);
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (IOException e) {
                    throw new CSVException(CSVException.CSV_FILE_READER_CLOSE_FAIL);
                }
            }
        }
        return resultSet.isEmpty() ? null : resultSet;
    }

    /**
     * 已Map的List形式获取csv的数据，第一行作为所有的key，csv每一行都是List中的一个map
     *
     * @param csvFileName
     *
     * @return
     */
    public static List<Map<String, Object>> getCSVDataMap(String csvFileName) {
        CSVReader csvReader = loadCSVResource(csvFileName);
        if (csvReader == null) {
            throw new CSVException(CSVException.CSV_FILE_LOAD_FAIL);
        }
        List<Map<String, Object>> resultSet = new ArrayList<Map<String, Object>>();
        try {
            String[] keys = csvReader.readNext();
            List<String[]> data = csvReader.readAll();
            for (int i = 0; i < data.size(); i++) {
                String[] row = data.get(i);
                if (row.length != keys.length) {
                    logger.warn("key length wrong, expect length is {}, but now is {}", keys.length, row.length);
                    continue;
                }
                Map<String, Object> map = new HashMap<String, Object>();
                for (int j = 0; j < row.length; j++) {
                    String element = row[j];
                    map.put(keys[j], CSVElementUtils.fileterElement(element));
                }
                resultSet.add(map);
            }
        } catch (Exception ex) {
            throw new CSVException(CSVException.CSV_FILE_READ_FAIL);
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (IOException e) {
                    throw new CSVException(CSVException.CSV_FILE_READER_CLOSE_FAIL);
                }
            }
        }
        return resultSet.isEmpty() ? null : resultSet;
    }

        public static void main(String[] args) {
//            List<List<Object>> listObjects = getCSVDataList("testcases\\a.csv");
//            List<Map<String, String>> data = getCSVDataMap("testcases\\sample.csv");
//            System.out.println(listObjects);
//        	List<Map<String, Object>> data = getCSVDataMap("testcases\\testDemo.csv");
//        	System.out.println(data.get(0));
        }
}
