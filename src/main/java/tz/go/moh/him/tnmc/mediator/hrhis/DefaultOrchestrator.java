package tz.go.moh.him.tnmc.mediator.hrhis;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpHeaders;
import org.json.JSONObject;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultOrchestrator extends UntypedActor {
    /**
     * The mediator configuration.
     */
    private final MediatorConfig config;
    /**
     * The logger instance.
     */
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    /**
     * The request handler that handles requests and responses.
     */
    private ActorRef requestHandler;

    /**
     * Initializes a new instance of the {@link DefaultOrchestrator} class.
     *
     * @param config The mediator configuration.
     */
    public DefaultOrchestrator(MediatorConfig config) {
        this.config = config;
    }

    /**
     * Forwards the message to the Tanzania Nursing and Midwifery Council
     *
     * @param message to be sent to the TNMC
     */
    private void forwardToTnmc(List<Pair<String, String>> params, String message) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String scheme;
        String host;
        String path;
        String token;
        int portNumber;
        if (config.getDynamicConfig().isEmpty()) {
            log.debug("Dynamic config is empty, using config from mediator.properties");
            if (config.getProperty("destination.scheme").equals("https")) {
                scheme = "https";
            } else {
                scheme = "http";
            }

            host = config.getProperty("destination.host");
            portNumber = Integer.parseInt(config.getProperty("destination.api.port"));
            path = config.getProperty("destination.api.path");
            token = config.getProperty("destination.token");
            String authHeader = "Bearer " + token;
            headers.put(HttpHeaders.AUTHORIZATION, authHeader);

        } else {
            log.debug("Using dynamic config");

            JSONObject connectionProperties = new JSONObject(config.getDynamicConfig()).getJSONObject("destinationConnectionProperties");

            host = connectionProperties.getString("destinationHost");
            portNumber = connectionProperties.getInt("destinationPort");
            scheme = connectionProperties.getString("destinationScheme");

            if (connectionProperties.has("destinationToken")) {
                token = connectionProperties.getString("destinationToken");

                // if we have a token
                // we want to add the token as the Basic Auth header in the HTTP request
                if (token != null && !"".equals(token)) {
                    String authHeader = "Bearer " + token;
                    headers.put(HttpHeaders.AUTHORIZATION, authHeader);
                }
            }
            path = connectionProperties.getString("destinationPath");
        }

        MediatorHTTPRequest forwardToTnmcRequest = new MediatorHTTPRequest(
                requestHandler, getSelf(), "Sending Data to TNMC", "GET", scheme,
                host, portNumber, path, message, headers, params
        );

        ActorSelection actor = getContext().actorSelection(config.userPathFor("http-connector"));
        config.setSSLContext(new MediatorConfig.SSLContext(true));
        actor.tell(forwardToTnmcRequest, getSelf());
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof MediatorHTTPRequest) {
            log.info("Sending data TNMC ...");
            requestHandler = ((MediatorHTTPRequest) msg).getRequestHandler();
            forwardToTnmc(((MediatorHTTPRequest) msg).getParams(), ((MediatorHTTPRequest) msg).getBody());
        } else if (msg instanceof MediatorHTTPResponse) { //respond
            log.info("Received response from TNMC");
            requestHandler.tell(((MediatorHTTPResponse) msg).toFinishRequest(), getSelf());
        } else {
            unhandled(msg);
        }
    }
}
