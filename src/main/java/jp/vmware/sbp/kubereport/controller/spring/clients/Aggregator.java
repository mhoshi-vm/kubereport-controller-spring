package jp.vmware.sbp.kubereport.controller.spring.clients;

import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1Spreadsheet;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1SpreadsheetSpec;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1SpreadsheetStatusAggregated;

import java.util.Map;

public interface Aggregator {

	Map<String, String> exec(V1alpha1Spreadsheet spreadsheet);

}
