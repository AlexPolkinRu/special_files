import java.util.List;

public class CsvToJson {
    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = Util.parseCSV(columnMapping, fileName);

        String json = Util.listToJson(list);

        Util.writeString("data.json", json);

    }

}