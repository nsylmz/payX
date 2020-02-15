package com.nsylmz.payx.netflixzuulapigatewayserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.stereotype.Component;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;;

@Component
public class PayXEurekaDiscoveryClient extends EurekaDiscoveryClient implements DiscoveryClient {

    private final EurekaClient eurekaClient;

    public PayXEurekaDiscoveryClient(@Qualifier("eurekaClient") EurekaClient eurekaClient, EurekaClientConfig clientConfig) {
    	super(eurekaClient, clientConfig);
        this.eurekaClient = eurekaClient;
    }

    @Override
    public List<String> getServices() {
        Applications applications = this.eurekaClient.getApplications();
        if (applications == null) {
            return Collections.emptyList();
        }
        List<Application> registered = applications.getRegisteredApplications();
        List<String> names = new ArrayList<>();
        for (Application app : registered) {
            if (app.getInstances().isEmpty()) {
                continue;
            }
            names.add(app.getName().toLowerCase(Locale.ROOT));
        }
        return names;
    }
}