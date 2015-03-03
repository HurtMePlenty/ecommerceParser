import ecommerceParser.priceFinderModule.PriceFinder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EntryPoint {

    public static void main(String[] args) {
        System.out.println("started");

        ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        PriceFinder priceFinder = context.getBean("priceFinder", PriceFinder.class);
        priceFinder.process();
        if (System.console() != null) {
            System.out.println("Successfully finished. Press enter to exit...");
            System.console().readLine();
        }
    }
}