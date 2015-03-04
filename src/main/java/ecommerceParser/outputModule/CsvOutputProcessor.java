package ecommerceParser.outputModule;

import ecommerceParser.priceFinderModule.Item;
import ecommerceParser.priceFinderModule.ItemPrice;
import ecommerceParser.priceFinderModule.OutputProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CsvOutputProcessor implements OutputProcessor {
    private String outputCsvFileName;
    private String csvSeparator;


    public void setOutputCsvFileName(String outputCsvFileName) {
        this.outputCsvFileName = outputCsvFileName;
    }

    public void setCsvSeparator(String csvSeparator) {
        this.csvSeparator = csvSeparator;
    }

    @Override
    public void output(List<Item> itemList) {
        File file = new File(outputCsvFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException("Was unable to create output file", e);
            }
        }

        //form header for our csv
        List<String> keys = new ArrayList<String>();
        for (Item item : itemList) {
            for (ItemPrice itemPrice : item.getItemPriceList()) {
                if (!keys.contains(itemPrice.getSupplierId())) {
                    keys.add(itemPrice.getSupplierId());
                }
            }
        }

        StringBuilder result = new StringBuilder();

        //form header
        result.append("IdType");
        result.append(csvSeparator);
        result.append("Id");

        for (String key : keys) {
            result.append(csvSeparator);
            result.append(key);
        }

        //form body
        for (Item item : itemList) {
            result.append("\n");
            result.append(item.getIdType().toString());
            result.append(csvSeparator);
            result.append(item.getId());

            keyLoop:
            for (String key : keys) {
                result.append(csvSeparator);
                for (ItemPrice itemPrice : item.getItemPriceList()) {
                    if (itemPrice.getSupplierId().equals(key)) {
                        result.append(itemPrice.getPrice());
                        continue keyLoop;
                    }
                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(result.toString().getBytes(Charset.forName("UTF-8")));
            System.out.println(String.format("Output file with name '%s' was successfully formed", outputCsvFileName));
        } catch (Exception e) {
            throw new RuntimeException("Was unable to write to output file", e);
        }

    }

}
