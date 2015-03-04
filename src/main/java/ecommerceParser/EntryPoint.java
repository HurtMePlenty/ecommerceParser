package ecommerceParser;

import ecommerceParser.priceFinderModule.PriceFinder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EntryPoint {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        PriceFinder priceFinder = context.getBean("priceFinder", PriceFinder.class);

        System.out.println();
        System.out.println();
        System.out.println("--------------------------------");
        System.out.println();
        System.out.println("started");

        priceFinder.process();
        if (System.console() != null) {
            System.out.println("Successfully finished. Press enter to exit...");
            System.console().readLine();
        }
    }
}