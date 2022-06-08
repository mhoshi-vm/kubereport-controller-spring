package jp.vmware.sbp.kubereport.controller.spring.clients;

import jp.vmware.sbp.kubereport.controller.spring.configuration.Constants;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1Spreadsheet;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1SpreadsheetSpec;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1SpreadsheetStatusAggregated;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class AggregatorImpl implements Aggregator {

	private final RestTemplate restTemplate;

	public AggregatorImpl(RestTemplateBuilder restTemplate) {
		this.restTemplate = restTemplate.build();
	}

	@Override
	public Map<String, String> exec(V1alpha1Spreadsheet spreadsheet) {
		Map<String, String> aggregatorList = new HashMap<>();

		V1alpha1SpreadsheetSpec spreadsheetSpec = spreadsheet.getSpec();
		V1alpha1SpreadsheetStatusAggregated statusAggregated = spreadsheet.getStatus().getAggregated();

		statusAggregated.setStartedAt(new Date().toString());
		statusAggregated.setSuccess("false");

		String url = spreadsheetSpec.getKubeAggregatorURL();

		try {
			List<String> resources = new ArrayList<>();
			if(spreadsheetSpec.getScrapeResources() != null){
				resources = spreadsheetSpec.getScrapeResources();
			}
			for (String resource : resources) {

				try {
					String resourceUrl = Constants.KUBERNETES.get(resource);
					if (resourceUrl == null){
						throw new Exception("Resource "+ resource +" does not exist");
					}
					String fullUrl = url + resourceUrl;
					aggregatorList.put(resource, restTemplate.getForObject(fullUrl, String.class));
				}
				catch (Exception e) {
					statusAggregated.setError(e.toString());
					return null;
				}
			}
		}
		finally {
			statusAggregated.setUpdateAt(new Date().toString());
		}

		statusAggregated.setSuccess("true");
		return aggregatorList;
	}

}
