package app.trackwizz.connect.util;

import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

public class HttpClient {

    /**
     * Returns instance of {@link WebClient} for a given http base url
     *
     * @param baseUrl http base url
     * @return instance of {@link WebClient}
     */
    public static WebClient getWebClient(String baseUrl) {
        return WebClient
                .builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(20 * 1024 * 1024)) // TODO review size (20MB) default is 256KB DataBufferLimitException
                        .build())
                .build();
    }
}
