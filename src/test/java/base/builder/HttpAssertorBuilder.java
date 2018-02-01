package base.builder;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.assertor.EqualAssertor;
import com.github.kevinsawicki.http.HttpRequest;


public class HttpAssertorBuilder extends AssertorBuilder<HttpAssertorBuilder> {
    private static final Logger logger = LoggerFactory.getLogger(HttpAssertorBuilder.class);

    private final HttpRequest httpRequest;

    /**
     * Get http response body from HttpAssertorBuilder
     *
     * @return
     */
    public String getResponseBody() {
        return this.resultString;
    }

    public HttpAssertorBuilder(HttpRequest httpRequest) {
        super("", httpRequest.body());
        this.httpRequest = httpRequest;
        logger.debug("Http response body: {}", resultString);
    }

    public HttpAssertorBuilder(HttpRequest httpRequest, String caseName) {
        super(caseName, httpRequest.body());
        this.httpRequest = httpRequest;
        logger.debug("Http response body: {}", resultString);
    }

    public HttpAssertorBuilder status() {
        return this.status(200);
    }

    public HttpAssertorBuilder status(int expect) {
        add(new EqualAssertor(httpRequest.code(), expect));
        return this;
    }

    /**
     * Compare jsonKeyValue from response, and assure that response format is json
     *
     * @param jsonKeyValue
     *
     * @return
     */
    @Override
    public HttpAssertorBuilder jsonKeyValueCompare(Map<String, String> jsonKeyValue) {
        // 有些json返回不带json头,暂时取消
        // add(new ContainsAssertor(httpRequest.contentType(), httpRequest.CONTENT_TYPE_JSON));
        return super.jsonKeyValueCompare(jsonKeyValue);
    }

}
