package com.qizy.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
    @Value("${es.hostnames}")
    private String[] esHosts;
    @Value("${es.port}")
    private Integer esPort;
    @Value("${es.scheme}")
    private String esScheme;

    /**
     *
     * @return
     */
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        HttpHost[] hosts = new HttpHost[esHosts.length];
        for (int i = 0;i<esHosts.length;i++) {
            HttpHost httpHost = new HttpHost(esHosts[i], esPort, esScheme);
            hosts[i] = httpHost;
        }
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(hosts)
        );

        return client;
    }
}
