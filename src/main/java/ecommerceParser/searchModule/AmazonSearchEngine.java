package ecommerceParser.searchModule;

import ecommerceParser.priceFinderModule.Item;
import ecommerceParser.priceFinderModule.ItemPrice;
import ecommerceParser.priceFinderModule.SearchEngine;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AmazonSearchEngine implements SearchEngine {
    private String engineName;
    private static final String AMAZON_DIRECT_URL = "http://www.amazon.com/s/?keywords=%s&page=1";
    private static final String ENDPOINT = "ecs.amazonaws.com";
    private static final String AWS_SECRET_KEY = "";
    private static final String AWS_ACCESS_KEY_ID = "";

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
            Thread.sleep(500); //against amazon server 503 code
            Document doc = db.parse(requestUrl);

            Element root = doc.getDocumentElement();
            if (root.getElementsByTagName("Error").getLength() > 0) {
                return findWithDirectGetHttp(item);
            }

            NodeList lowestPrices = root.getElementsByTagName("LowestNewPrice");
            if (lowestPrices.getLength() == 0) {
                return findWithDirectGetHttp(item);
            }


            Double lowest = null;
            for (int i = 0; i < lowestPrices.getLength(); i++) {
                NodeList formattedPriceResult = ((Element) lowestPrices.item(0)).getElementsByTagName("FormattedPrice");
                if (formattedPriceResult.getLength() != 1) {
                    continue;
                }
                Node formattedPrice = formattedPriceResult.item(0);
                String content = formattedPrice.getTextContent();
                content = content.replace("$", "");
                try {
                    Double price = Double.valueOf(content);
                    if (lowest == null || price < lowest) {
                        lowest = price;
                    }
                } catch (Exception e) {
                    //suppress
                    //System.out.println(e);
                }
            }

            if (lowest != null) {
                return ItemPrice.create(lowest, engineName);
            }

            return findWithDirectGetHttp(item);

        } catch (Exception e) {
            //suppress
            //System.out.println(e);
            return findWithDirectGetHttp(item);
        }

    }

    @Override
    public String getName() {
        return engineName;
    }

    private ItemPrice findWithDirectGetHttp(Item item) {
        try {
            String url = String.format(AMAZON_DIRECT_URL, item.getId());
            String content = Request.Get(url).connectTimeout(500).execute().returnContent().asString();
            Double price = HtmlParseHelper.parsePrice(content);
            if (price != null) {
                return ItemPrice.create(price, engineName);
            }

            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
