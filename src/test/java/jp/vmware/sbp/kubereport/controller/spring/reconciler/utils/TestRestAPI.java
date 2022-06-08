package jp.vmware.sbp.kubereport.controller.spring.reconciler.utils;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestAPI {

	@GetMapping("/api/v1/resource/pods")
	public String Aggregator() {
		return "{ \"foo\": \"bar\" }";
	}

	@PostMapping(value = "/api/v1/resource/pods", produces = MediaType.APPLICATION_JSON_VALUE)
	public void Formatter(@RequestBody String full) {
		System.out.println(full);
	}

}
