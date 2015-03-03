package ecommerceParser.priceFinderModule;

import java.util.ArrayList;
import java.util.List;

public class PriceFinder {

    private List<SupplierSource> supplierSourceList = new ArrayList<SupplierSource>();
    private OutputProcessor outputProcessor;

    public void setSupplierSourceList(List<SupplierSource> supplierSourceList) {
        this.supplierSourceList = supplierSourceList;
    }

    public void setOutputProcessor(OutputProcessor outputProcessor) {
        this.outputProcessor = outputProcessor;
    }

    public void process() {

    }
}
