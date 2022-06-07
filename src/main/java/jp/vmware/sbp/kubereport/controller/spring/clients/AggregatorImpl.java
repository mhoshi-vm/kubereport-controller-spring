package jp.vmware.sbp.kubereport.controller.spring.clients;

import jp.vmware.sbp.kubereport.controller.spring.configuration.Constants;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1Spreadsheet;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1SpreadsheetSpec;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1SpreadsheetStatusAggregated;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AggregatorImpl implements Aggregator {

	private final RestTemplate restTemplate;

	public AggregatorImpl(RestTemplateBuilder restTemplate) {
		this.restTemplate = restTemplate.build();
	}

	@Override
	public Map<String, String> exec(V1alpha1SpreadsheetSpec spreadsheetSpec, V1alpha1SpreadsheetStatusAggregated statusAggregated) {
		Map<String, String> aggregatorList = new HashMap<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		statusAggregated.setStartedAt(simpleDateFormat.format(new Date()));
		statusAggregated.setSuccess("false");

		String url = spreadsheetSpec.getKubeAggregatorURL();

		for (String resource : spreadsheetSpec.getScrapeResources()) {

			String fullUrl = url + Constants.KUBERNETES.get(resource);
			try {
				aggregatorList.put(resource, restTemplate.getForObject(fullUrl, String.class));
			}
			catch (Exception e) {
				statusAggregated.setError(e.toString());
				return null;
			}
		}

		statusAggregated.setSuccess("true");
		statusAggregated.setUpdateAt(simpleDateFormat.format(new Date()));
		return aggregatorList;

	}

}
