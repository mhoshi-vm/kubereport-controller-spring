package jp.vmware.sbp.kubereport.controller.spring.clients;

import jp.vmware.sbp.kubereport.controller.spring.configuration.Constants;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1Spreadsheet;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

@Component
public class FormatterImpl implements Formatter {

	private final RestTemplate restTemplate;

	public FormatterImpl(RestTemplateBuilder restTemplate) {
		this.restTemplate = restTemplate.build();
	}

	public void exec(V1alpha1Spreadsheet spreadsheet, Map<String, String> aggregated) {

		spreadsheet.getStatus().getFormatted().setStartedAt(new Date().toString());
		spreadsheet.getStatus().getFormatted().setSuccess("false");

		String url = spreadsheet.getSpec().getKubeFormatterURL();

		try {
			for (String key : aggregated.keySet()) {

				try {
					String resourceUrl = Constants.KUBERNETES.get(key);
					if (resourceUrl == null) {
						throw new Exception("Resource " + key + " does not exist");
					}
					String fullUrl = url + resourceUrl;
					restTemplate.postForObject(fullUrl, aggregated.get(key), String.class);
				}
				catch (Exception e) {
					spreadsheet.getStatus().getFormatted().setError(e.toString());
					return;
				}
			}
		}
		finally {
			spreadsheet.getStatus().getFormatted().setUpdateAt(new Date().toString());
		}
		spreadsheet.getStatus().getFormatted().setSuccess("true");
	}

}
