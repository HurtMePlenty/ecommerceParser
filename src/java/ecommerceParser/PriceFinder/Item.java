package ecommerceParser.PriceFinder;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private IdType idType;

    private List<ItemPrice> itemPriceList = new ArrayList<ItemPrice>();

    private Item() {

    }

    public static Item create(IdType idType) {
        Item item = new Item();
        item.idType = idType;
        return item;
    }

    public void addPrice(ItemPrice itemPrice) {
        itemPriceList.add(itemPrice);
    }

    public IdType getIdType() {
        return idType;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }


    public enum IdType {
        UPC,
        ISBN
    }
}


