package ecommerceParser.pupplierModule;

import ecommerceParser.priceFinderModule.Item;
import ecommerceParser.priceFinderModule.SupplierSource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvSupplierSource implements SupplierSource {

    private String inputCsvFileName;
    private String csvSeparator;

    public void setInputCsvFileName(String inputCsvFileName) {
        this.inputCsvFileName = inputCsvFileName;
    }

    public void setCsvSeparator(String csvSeparator) {
        this.csvSeparator = csvSeparator;
    }

    @Override
    public List<Item> getSource() {
        try {
            List<Item> result = new ArrayList<Item>();
            byte[] bytes = Files.readAllBytes(Paths.get(inputCsvFileName));
            String content = new String(bytes, StandardCharsets.UTF_8);
            String[] lines = content.split("/n");

            if (lines.length > 0) {
                String header = lines[0];
                //todo check supplier names here. Currently unused
                String[] headerColumns = header.split(csvSeparator);
                if (headerColumns.length < 2) {
                    throw new RuntimeException("Input csv file should contain at least 2 columns - id type and id");
                }

                for (String line : lines) {
                    if (!line.equals(header)) {
                        String[] columns = line.split(csvSeparator);
                        if (columns.length < 2) {
                            throw new RuntimeException("Input csv file should contain at least 2 columns - id type and id");
                        }

                        String idType = columns[0].toUpperCase();
                        String id = columns[1];
                        Item.IdType type;
                        try {
                            type = Item.IdType.valueOf(idType);
                        } catch (Exception e) {
                            throw new RuntimeException(String.format("Unknown idType %s. Only UPC and ISBN are allowed", idType));
                        }
                        Item item = Item.create(type, id);
                        result.add(item);
                    }
                }
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException(String.format("Unable to read input file with name = %s", inputCsvFileName));
        }
    }
}
