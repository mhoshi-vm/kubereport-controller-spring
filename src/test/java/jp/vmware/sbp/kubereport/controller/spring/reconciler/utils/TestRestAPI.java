package jp.vmware.sbp.kubereport.controller.spring.reconciler.utils;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestRestAPI {

	Map<String, String> headers;

	public TestRestAPI() {
		this.headers = new HashMap<>();
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	@GetMapping("/api/v1/resource/pods")
	public String Aggregator() {
		return "{ \"foo\": \"bar\" }";
	}

	@PostMapping(value = "/api/v1/resource/pods", produces = MediaType.APPLICATION_JSON_VALUE)
	public void Formatter(@RequestBody String full, @RequestHeader Map<String, String> header) {
		headers.putAll(header);
		headers.put("request-body", full);
	}

}
