package ecommerceParser.searchModule;

import ecommerceParser.priceFinderModule.Item;
import ecommerceParser.priceFinderModule.ItemPrice;
import ecommerceParser.priceFinderModule.SearchEngine;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

public class AmazonSearchEngine implements SearchEngine {
    private String engineName;
    private static final String ENDPOINT = "ecs.amazonaws.com";
    private static final String AWS_SECRET_KEY = "GddwgIPu5HOCZlnulZlcQEJ5uHF26PDzjxHMij5F";
    private static final String AWS_ACCESS_KEY_ID = "AKIAJB6FL7ISSVDIVWSQ";

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    @Override
    public ItemPrice searchForItem(Item item) {

        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get instance of singedRequestHelper", e);
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "ItemLookup");
        params.put("ItemId", item.getId());
        params.put("ResponseGroup", "Large");

        params.put("AssociateTag", "Alexey-20");
        params.put("IdType", item.getIdType().toString());
        params.put("SearchIndex", "All");

        String requestUrl = helper.sign(params);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            int a = 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
