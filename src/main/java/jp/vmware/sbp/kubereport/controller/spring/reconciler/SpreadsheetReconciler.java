package jp.vmware.sbp.kubereport.controller.spring.reconciler;

import io.kubernetes.client.extended.controller.reconciler.Reconciler;
import io.kubernetes.client.extended.controller.reconciler.Request;
import io.kubernetes.client.extended.controller.reconciler.Result;
import io.kubernetes.client.informer.SharedIndexInformer;
import io.kubernetes.client.informer.SharedInformer;
import io.kubernetes.client.informer.cache.Lister;
import jp.vmware.sbp.kubereport.controller.spring.models.V1alpha1Spreadsheet;
import org.springframework.stereotype.Component;

@Component
public class SpreadsheetReconciler implements Reconciler {

    private final SharedInformer<V1alpha1Spreadsheet> spreadsheetSharedInformer;

    private final Lister<V1alpha1Spreadsheet> spreadsheetLister;

    public SpreadsheetReconciler(SharedIndexInformer<V1alpha1Spreadsheet> spreadsheetSharedInformer) {
        this.spreadsheetSharedInformer = spreadsheetSharedInformer;
        this.spreadsheetLister = new Lister<>(spreadsheetSharedInformer.getIndexer());
    }

    // *OPTIONAL*
    // If you want to hold the controller from running util some condition..
    public boolean informerReady() {
        return spreadsheetSharedInformer.hasSynced();
    }

    @Override
    public Result reconcile(Request request) {

        String name = request.getName();
        String namespace = request.getNamespace();

        V1alpha1Spreadsheet v1alpha1Spreadsheet = spreadsheetLister.get(namespace + "/" + name);

        //System.out.println(v1alpha1Spreadsheet.toString());

        System.out.println("triggered reconciling ");
        return new Result(false);
    }
}

