package jp.vmware.sbp.kubereport.controller.spring.reconciler;

import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.extended.controller.reconciler.Reconciler;
import io.kubernetes.client.extended.controller.reconciler.Request;
import io.kubernetes.client.extended.controller.reconciler.Result;
import io.kubernetes.client.informer.SharedIndexInformer;
import io.kubernetes.client.informer.SharedInformer;
import io.kubernetes.client.informer.cache.Lister;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.util.generic.GenericKubernetesApi;
import io.kubernetes.client.util.generic.KubernetesApiResponse;
import jp.vmware.sbp.kubereport.controller.spring.clients.Aggregator;
import jp.vmware.sbp.kubereport.controller.spring.clients.Formatter;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1Spreadsheet;

import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1SpreadsheetList;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1SpreadsheetStatus;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1SpreadsheetStatusAggregated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class SpreadsheetReconciler implements Reconciler {

	private final SharedInformer<V1alpha1Spreadsheet> spreadsheetSharedInformer;

	private final Lister<V1alpha1Spreadsheet> spreadsheetLister;

	private final Aggregator aggregator;

	private final Formatter formatter;

	private ApiClient api;

	public SpreadsheetReconciler(SharedIndexInformer<V1alpha1Spreadsheet> spreadsheetSharedInformer,
			Aggregator aggregator, Formatter formatter, ApiClient apiClient) {
		this.spreadsheetSharedInformer = spreadsheetSharedInformer;
		this.aggregator = aggregator;
		this.formatter = formatter;
		this.api = apiClient;
		this.spreadsheetLister = new Lister<>(spreadsheetSharedInformer.getIndexer());
	}

	private static final Logger logger = LoggerFactory.getLogger(SpreadsheetReconciler.class);

	// *OPTIONAL*
	// If you want to hold the controller from running util some condition..
	public boolean informerReady() {
		return spreadsheetSharedInformer.hasSynced();
	}

	@Override
	public Result reconcile(Request request) {

		String name = request.getName();
		String namespace = request.getNamespace();

		Map<String, String> aggeregated = new HashMap<>();

		V1alpha1Spreadsheet spreadsheet = spreadsheetLister.get(namespace + "/" + name);
		V1alpha1SpreadsheetStatus spreadsheetStatus = spreadsheet.getStatus();
		V1alpha1SpreadsheetStatusAggregated statusAggregated = spreadsheetStatus.getAggregated();

		GenericKubernetesApi<V1alpha1Spreadsheet, V1alpha1SpreadsheetList> status = new GenericKubernetesApi<>(
				V1alpha1Spreadsheet.class, V1alpha1SpreadsheetList.class, "sbp.vmware.jp", "v1alpha1", "spreadsheet",
				api);

		spreadsheetStatus.setFriendlyDescription("Reconciling");

		logger.info("Starting aggregation call");
		aggeregated = aggregator.exec(spreadsheet.getSpec(), statusAggregated);

		System.out.printf(statusAggregated.toString());
		System.out.printf(spreadsheet.getStatus().getAggregated().toString());

		if (spreadsheet.getStatus().getAggregated().getSuccess() == "false") {
			spreadsheet.getStatus().setFriendlyDescription("Spring Reconcile Failed");
			logger.warn("Reconcile Failed : " + spreadsheet.getStatus().getAggregated().getError());
			KubernetesApiResponse<V1alpha1Spreadsheet> update = status.updateStatus(spreadsheet, obj -> obj.getStatus());
			if (!update.isSuccess()){
				logger.warn("Failed to update status");
			}
			return new Result(true);
		}

		logger.info("Starting formatter call");
		formatter.exec(spreadsheet, aggeregated);

		if (spreadsheet.getStatus().getFormatted().getSuccess() == "false") {
			spreadsheet.getStatus().setFriendlyDescription("Spring Reconcile Failed");
			logger.warn("Reconcile Failed : " + spreadsheet.getStatus().getFormatted().getError());
			KubernetesApiResponse<V1alpha1Spreadsheet> update = status.updateStatus(spreadsheet, obj -> obj.getStatus());
			if (!update.isSuccess()){
				logger.warn("Failed to update status");
			}
			return new Result(true);
		}

		spreadsheet.getStatus().setFriendlyDescription("Reconcile Succeded");
		logger.info("Reconcile Succeeded");
		KubernetesApiResponse<V1alpha1Spreadsheet> update = status.updateStatus(spreadsheet, obj -> obj.getStatus());
		if (!update.isSuccess()){
			logger.warn("Failed to update status");
		}
		return new Result(false);
	}

}
