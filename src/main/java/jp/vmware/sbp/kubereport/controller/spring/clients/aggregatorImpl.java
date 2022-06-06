package jp.vmware.sbp.kubereport.controller.spring.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class aggregatorImpl {

    private RestTemplate restTemplate;

    public aggregatorImpl(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }
}
