package ecommerceParser.priceFinderModule;

import java.util.ArrayList;
import java.util.List;

public class PriceFinder {

    private List<SupplierSource> supplierSourceList = new ArrayList<SupplierSource>();
    private OutputProcessor outputProcessor;
    private List<SearchEngine> searchEngineList;

    public void setSupplierSourceList(List<SupplierSource> supplierSourceList) {
        this.supplierSourceList = supplierSourceList;
    }

    public void setOutputProcessor(OutputProcessor outputProcessor) {
        this.outputProcessor = outputProcessor;
    }

    public void setSearchEngineList(List<SearchEngine> searchEngineList) {
        this.searchEngineList = searchEngineList;
    }

    public void process() {
        List<Item> items = new ArrayList<Item>();
        for (SupplierSource supplierSource : supplierSourceList) {
            List<Item> source = supplierSource.getSource();
            items.addAll(source);
        }

        for (SearchEngine searchEngine : searchEngineList) {
            System.out.println(String.format("Initializing searchEngine - %s", searchEngine.getName()));
            for (Item item : items) {
                System.out.println(String.format("Getting info for item idType = %s id = %s searchEngine = %s",
                        item.getIdType().toString(), item.getId(), searchEngine.getName()));
                ItemPrice itemPrice = searchEngine.searchForItem(item);
                if (itemPrice != null) {
                    item.addPrice(itemPrice);
                }
            }
        }

        System.out.println("Generating output");

        outputProcessor.output(items);

    }
}
