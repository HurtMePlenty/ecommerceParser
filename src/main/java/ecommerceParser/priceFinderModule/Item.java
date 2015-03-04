package ecommerceParser.priceFinderModule;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private IdType idType;
    private String id;
    private List<ItemPrice> itemPriceList = new ArrayList<ItemPrice>();

    private Item() {

    }

    public static Item create(IdType idType, String id) {
        Item item = new Item();
        item.idType = idType;
        item.id = id;
        return item;
    }

    public void addPrice(ItemPrice itemPrice) {
        itemPriceList.add(itemPrice);
    }

    public IdType getIdType() {
        return idType;
    }

    public String getId() {
        return id;
    }

    public List<ItemPrice> getItemPriceList() {
        return itemPriceList;
    }

    public enum IdType {
        UPC,
        ISBN
    }
}


