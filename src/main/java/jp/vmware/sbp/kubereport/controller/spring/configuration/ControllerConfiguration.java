package jp.vmware.sbp.kubereport.controller.spring.configuration;

import io.kubernetes.client.extended.controller.Controller;
import io.kubernetes.client.extended.controller.builder.ControllerBuilder;
import io.kubernetes.client.extended.controller.builder.DefaultControllerBuilder;
import io.kubernetes.client.informer.SharedIndexInformer;
import io.kubernetes.client.informer.SharedInformerFactory;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.generic.GenericKubernetesApi;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1Spreadsheet;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1SpreadsheetList;
import jp.vmware.sbp.kubereport.controller.spring.reconciler.SpreadsheetReconciler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ControllerConfiguration {

	@Bean
	public CommandLineRunner commandLineRunner(SharedInformerFactory sharedInformerFactory,
			Controller spreadsheetController) {
		return args -> {
			System.out.println("starting informers..");
			sharedInformerFactory.startAllRegisteredInformers();

			System.out.println("running controller..");
			spreadsheetController.run();
		};
	}

	@Bean
	public Controller SpreadsheetController(SharedInformerFactory sharedInformerFactory,
			SpreadsheetReconciler reconciler) {
		DefaultControllerBuilder builder = ControllerBuilder.defaultBuilder(sharedInformerFactory);
		builder = builder.watch((q) -> ControllerBuilder.controllerWatchBuilder(V1alpha1Spreadsheet.class, q)
				.withResyncPeriod(Duration.ofMinutes(1)).build());
		builder.withWorkerCount(2);
		builder.withReadyFunc(reconciler::informerReady);
		return builder.withReconciler(reconciler).withName("SpreadsheetController").build();
	}

	@Bean
	public GenericKubernetesApi<V1alpha1Spreadsheet, V1alpha1SpreadsheetList> spreadsheetApi(ApiClient apiClient) {
		return new GenericKubernetesApi<>(V1alpha1Spreadsheet.class, V1alpha1SpreadsheetList.class, "sbp.vmware.jp",
				"v1alpha1", "spreadsheet", apiClient);
	}

	@Bean
	public SharedIndexInformer<V1alpha1Spreadsheet> spreadsheetSharedIndexInformer(ApiClient apiClient,
			SharedInformerFactory sharedInformerFactory) {
		GenericKubernetesApi<V1alpha1Spreadsheet, V1alpha1SpreadsheetList> genericApi = new GenericKubernetesApi<>(
				V1alpha1Spreadsheet.class, V1alpha1SpreadsheetList.class, "sbp.vmware.jp", "v1alpha1", "spreadsheet",
				apiClient);
		return sharedInformerFactory.sharedIndexInformerFor(genericApi, V1alpha1Spreadsheet.class, 0);
	}



}
