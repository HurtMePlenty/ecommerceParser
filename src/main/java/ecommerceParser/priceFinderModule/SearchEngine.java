package ecommerceParser.priceFinderModule;

public interface SearchEngine {
    ItemPrice searchForItem(Item item);
    String getName();
}
