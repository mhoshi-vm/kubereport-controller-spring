package jp.vmware.sbp.kubereport.controller.spring.reconciler;

import io.kubernetes.client.extended.controller.reconciler.Request;
import io.kubernetes.client.informer.SharedIndexInformer;
import io.kubernetes.client.informer.cache.Cache;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.util.generic.GenericKubernetesApi;
import io.kubernetes.client.util.generic.KubernetesApiResponse;
import jp.vmware.sbp.kubereport.controller.spring.clients.Aggregator;
import jp.vmware.sbp.kubereport.controller.spring.clients.AggregatorImpl;
import jp.vmware.sbp.kubereport.controller.spring.clients.Formatter;
import jp.vmware.sbp.kubereport.controller.spring.clients.FormatterImpl;
import jp.vmware.sbp.kubereport.controller.spring.models.*;
import jp.vmware.sbp.kubereport.controller.spring.reconciler.utils.TestRestAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class SpreadsheetReconcilerTest {

	@LocalServerPort
	private int port;

	@MockBean
	CommandLineRunner commandLineRunner;

	SpreadsheetReconciler spreadsheetReconciler;

	@Mock
	SharedIndexInformer<V1alpha1Spreadsheet> spreadsheetSharedInformer;

	@Mock
	GenericKubernetesApi<V1alpha1Spreadsheet, V1alpha1SpreadsheetList> spreadsheetApi;

	@Mock
	KubernetesApiResponse<V1alpha1Spreadsheet> update;

	Aggregator aggregator;

	Formatter formatter;

	Cache<V1alpha1Spreadsheet> v1alpha1SpreadsheetCache;

	Map<String, String> headers;

	V1alpha1Spreadsheet spreadsheet;

	@Autowired
	TestRestAPI testRestAPI;

	@BeforeEach
	void setup() {

		headers = testRestAPI.getHeaders();

		List<String> resources = new ArrayList<>();
		resources.add("pod");

		spreadsheet = newSpreadsheet(resources);

		v1alpha1SpreadsheetCache = new Cache<>();
		v1alpha1SpreadsheetCache.add(spreadsheet);

		when(spreadsheetSharedInformer.getIndexer()).thenReturn(v1alpha1SpreadsheetCache);

		RestTemplateBuilder restTemplate = new RestTemplateBuilder();
		aggregator = new AggregatorImpl(restTemplate);
		formatter = new FormatterImpl(restTemplate);

		spreadsheetReconciler = new SpreadsheetReconciler(spreadsheetSharedInformer, spreadsheetApi, aggregator,
				formatter);

		when(update.isSuccess()).thenReturn(true);
		when(spreadsheetApi.updateStatus(any(), any())).thenReturn(update);
	}

	@Test
	void reconcile() {
		Request request = new Request("hoge", "hoge");
		spreadsheetReconciler.reconcile(request);
		assertEquals("true", spreadsheet.getStatus().getAggregated().getSuccess());
		assertEquals("true", spreadsheet.getStatus().getFormatted().getSuccess());
		assertEquals("Reconcile Succeeded", spreadsheet.getStatus().getFriendlyDescription());
		assertEquals("application/json", headers.get("content-type"));
		assertEquals("{ \"foo\": \"bar\" }", headers.get("request-body"));
		assertNotNull(headers.get("content-length"));
	}

	V1alpha1Spreadsheet newSpreadsheet(List<String> resources) {
		return new V1alpha1Spreadsheet().metadata(new V1ObjectMeta().name("hoge").namespace("hoge"))
				.spec(new V1alpha1SpreadsheetSpec().kubeAggregatorURL("http://localhost:" + port)
						.kubeFormatterURL("http://localhost:" + port).scrapeResources(resources))
				.status(new V1alpha1SpreadsheetStatus().aggregated(new V1alpha1SpreadsheetStatusAggregated())
						.formatted(new V1alpha1SpreadsheetStatusFormatted()));
	}

}