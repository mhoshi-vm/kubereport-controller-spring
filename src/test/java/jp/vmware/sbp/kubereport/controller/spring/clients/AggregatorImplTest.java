package jp.vmware.sbp.kubereport.controller.spring.clients;

import jp.vmware.sbp.kubereport.controller.spring.configuration.Constants;
import jp.vmware.sbp.kubereport.controller.spring.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AggregatorImplTest {

    private V1alpha1Spreadsheet spreadsheet;

    RestTemplateBuilder builder;
    RestTemplate restTemplate;

    AggregatorImpl aggregator;

    Map<String, String> aggregated;

    @BeforeEach
    void setUp() {

        builder = mock(RestTemplateBuilder.class);
        restTemplate = mock(RestTemplate.class);

        Mockito.when(builder.build()).thenReturn(restTemplate);

        aggregator = new AggregatorImpl(builder);
        aggregated = new HashMap<>();

        spreadsheet = new V1alpha1Spreadsheet();
        V1alpha1SpreadsheetSpec spreadsheetSpec = new V1alpha1SpreadsheetSpec();
        V1alpha1SpreadsheetStatus spreadsheetStatus = new V1alpha1SpreadsheetStatus();
        V1alpha1SpreadsheetStatusAggregated spreadsheetStatusAggregated = new V1alpha1SpreadsheetStatusAggregated();
        V1alpha1SpreadsheetStatusFormatted spreadsheetStatusFormatted = new V1alpha1SpreadsheetStatusFormatted();

        spreadsheetStatus.setAggregated(spreadsheetStatusAggregated);
        spreadsheetStatus.setFormatted(spreadsheetStatusFormatted);
        spreadsheet.setStatus(spreadsheetStatus);
        spreadsheet.setSpec(spreadsheetSpec);
    }

    @Test
    void execSuccess() {

        V1alpha1SpreadsheetSpec spreadsheetSpec = spreadsheet.getSpec();
        Mockito.when(restTemplate.getForObject("http://hoge" + Constants.KUBERNETES.get("pod"),String.class)).thenReturn("aaaa");

        spreadsheetSpec.setKubeAggregatorURL("http://hoge");
        List<String> resources = new ArrayList<String>();
        resources.add("pod");
        spreadsheetSpec.setScrapeResources(resources);

        aggregated =  aggregator.exec(spreadsheet);
        assertEquals("true", spreadsheet.getStatus().getAggregated().getSuccess());
    }

    @Test
    void execFailed() {

        V1alpha1SpreadsheetSpec spreadsheetSpec = spreadsheet.getSpec();

        spreadsheetSpec.setKubeAggregatorURL("http://hoge");
        List<String> resources = new ArrayList<String>();
        resources.add("aaa");
        spreadsheetSpec.setScrapeResources(resources);

        aggregator.exec(spreadsheet);
        aggregated =  aggregator.exec(spreadsheet);
        assertEquals("false", spreadsheet.getStatus().getAggregated().getSuccess());
        assertEquals("java.lang.Exception: Resource aaa does not exist", spreadsheet.getStatus().getAggregated().getError());
    }

    @Test
    void nullResource() {

        V1alpha1SpreadsheetSpec spreadsheetSpec = spreadsheet.getSpec();

        spreadsheetSpec.setKubeAggregatorURL("http://hoge");
        aggregator.exec(spreadsheet);
        aggregated =  aggregator.exec(spreadsheet);
        assertTrue(aggregated.isEmpty());
    }
}