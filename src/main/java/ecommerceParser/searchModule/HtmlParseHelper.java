package ecommerceParser.searchModule;

import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HtmlParseHelper {
    public static Double parsePrice(String html) {
        Document doc = Jsoup.parse(html);

        Elements result = doc.select("li[id^=result_]");
        if (result.size() == 0) {
            return null;
        }

        Element firstResultBlock = result.first();

        Double price = null;
        for (Element priceElem : firstResultBlock.select("span.a-color-price")) {
            Element parent = priceElem.parent();
            if (parent.outerHtml().contains("used")) {
                continue;
            }

            String priceString = priceElem.text();
            try {
                priceString = priceString.replace("$", "");
                Double priceDouble = Double.valueOf(priceString);
                if (price == null || price > priceDouble) {
                    price = priceDouble;
                }

            } catch (Exception e) {
                //suppress and look forward
            }
        }

        //check special Amazon price

        Elements specialPrice = result.select("a:contains(Click to see price)");
        if (specialPrice.size() > 0) {
            String url = specialPrice.first().attr("href");
            try {
                Thread.sleep(500);
                String content = Request.Get(url).connectTimeout(500).execute().returnContent().asString();
                Document bestPriceDocument = Jsoup.parse(content);
                Elements dataDiv = bestPriceDocument.select("#fbt_x_title");
                if (dataDiv.size() > 0) {
                    String bestPriceString = dataDiv.first().attr("data-price");
                    bestPriceString = bestPriceString.replace("$", "");
                    try {
                        Double bestPrice = Double.valueOf(bestPriceString);
                        if (price == null || bestPrice < price) {
                            price = bestPrice;
                        }
                    } catch (Exception e) {
                        //just suppress
                    }
                }

            } catch (Exception e) {
                //suppress
                //System.out.println(e);
            }
        }


        if (price != null) {
            return price;
        }

        return null;
    }
}
