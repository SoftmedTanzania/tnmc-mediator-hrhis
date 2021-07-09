package tz.go.moh.him.tnmc.mediator.hrhis;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.junit.Assert;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.testing.MockHTTPConnector;

import java.util.Collections;
import java.util.Map;

/**
 * Represents a mock destination.
 */
public class MockDestination extends MockHTTPConnector {

    /**
     * The expected message type
     */
    private final String expectedMessageType;

    public MockDestination(String expectedMessageType) {
        this.expectedMessageType = expectedMessageType;
    }

    /**
     * Gets the response.
     *
     * @return Returns the response.
     */
    @Override
    public String getResponse() {
        try {
            return IOUtils.toString(DefaultOrchestratorTest.class.getClassLoader().getResourceAsStream("response.json"));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the status code.
     *
     * @return Returns the status code.
     */
    @Override
    public Integer getStatus() {
        return 200;
    }

    /**
     * Gets the HTTP headers.
     *
     * @return Returns the HTTP headers.
     */
    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    /**
     * Handles the message.
     *
     * @param msg The message.
     */
    @Override
    public void executeOnReceive(MediatorHTTPRequest msg) {
        Assert.assertEquals("Bearer token", msg.getHeaders().get(HttpHeaders.AUTHORIZATION));
        Assert.assertEquals("pageSize", msg.getParams().get(0).getKey());
        Assert.assertEquals("5", msg.getParams().get(0).getValue());
        Assert.assertEquals("page", msg.getParams().get(1).getKey());
        Assert.assertEquals("1", msg.getParams().get(1).getValue());
    }
}