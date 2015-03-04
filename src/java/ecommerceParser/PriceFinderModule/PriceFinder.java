package ecommerceParser.priceFinderModule;

import java.util.ArrayList;
import java.util.List;

public class PriceFinder {

    private List<SupplierSource> supplierSourceList = new ArrayList<SupplierSource>();
    private OutputProcessor outputProcessor;
    private SearchEngine searchEngine;

    public void setSupplierSourceList(List<SupplierSource> supplierSourceList) {
        this.supplierSourceList = supplierSourceList;
    }

    public void setOutputProcessor(OutputProcessor outputProcessor) {
        this.outputProcessor = outputProcessor;
    }

    public void setSearchEngine(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    public void process() {
        List<Item> items = new ArrayList<Item>();
        for(SupplierSource supplierSource : supplierSourceList){
           List<Item> source = supplierSource.getSource();
            items.addAll(source);
        }

        for (Item item : items){
            ItemPrice itemPrice = searchEngine.searchForItem(item);
            if(itemPrice != null) {
                item.addPrice(itemPrice);
            }
        }

    }
}
