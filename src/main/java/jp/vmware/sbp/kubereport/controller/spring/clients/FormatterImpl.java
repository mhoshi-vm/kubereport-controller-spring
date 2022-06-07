package jp.vmware.sbp.kubereport.controller.spring.clients;

import jp.vmware.sbp.kubereport.controller.spring.configuration.Constants;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1Spreadsheet;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class FormatterImpl implements Formatter {

	private final RestTemplate restTemplate;

	public FormatterImpl(RestTemplateBuilder restTemplate) {
		this.restTemplate = restTemplate.build();
	}

	public void exec(V1alpha1Spreadsheet spreadsheet, Map<String, String> aggregated) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		spreadsheet.getStatus().getFormatted().setStartedAt(simpleDateFormat.format(new Date()));
		spreadsheet.getStatus().getFormatted().setSuccess("false");

		String url = spreadsheet.getSpec().getKubeFormatterURL();

		for (String key : aggregated.keySet()) {
			String fullUrl = url + Constants.KUBERNETES.get(key);
			try {
				restTemplate.patchForObject(fullUrl, aggregated.get(key), String.class);
			}
			catch (Error e) {
				spreadsheet.getStatus().getFormatted().setError(e.toString());
				return;
			}
		}
		spreadsheet.getStatus().getFormatted().setSuccess("true");
		spreadsheet.getStatus().getFormatted().setUpdateAt(simpleDateFormat.format(new Date()));
		return;
	}

}
