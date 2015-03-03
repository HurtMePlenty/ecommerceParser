package ecommerceParser.PriceFinder;

public class ItemPrice {

    private double price;
    private String supplierId;

    private ItemPrice() {

    }

    public static ItemPrice create(double price, String supplierId){
        ItemPrice itemPrice = new ItemPrice();
        itemPrice.price = price;
        itemPrice.supplierId = supplierId;
        return itemPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
}
