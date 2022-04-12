import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {

            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            return csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String listToJson(List<Employee> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Type listType = new TypeToken<List<Employee>>() {
        }.getType();

        return gson.toJson(list, listType);
    }

    public static void writeString(String fileName, String json) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Employee> parseXML(String fileName) {

        File file = new File(fileName);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc;

        try {
            doc = factory.newDocumentBuilder().parse(file);
        } catch (Exception e) {
            System.out.println("Ошибка парсинга " + e.getMessage());
            return null;
        }

        Node root = doc.getFirstChild();
        NodeList elements = root.getChildNodes();

        List<Employee> staff = new ArrayList<>();

        for (int i = 0; i < elements.getLength(); i++) {

            if (elements.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            staff.add(getEmployee(elements.item(i)));
        }

        return staff;
    }

    public static Employee getEmployee(Node element) {

        Employee employee = new Employee();
        NodeList employeeItemList = element.getChildNodes();

        for (int j = 0; j < employeeItemList.getLength(); j++) {

            if (employeeItemList.item(j).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            switch (employeeItemList.item(j).getNodeName()) {
                case "id":
                    employee.id = Integer.parseInt(employeeItemList.item(j).getTextContent());
                    break;
                case "firstName":
                    employee.firstName = employeeItemList.item(j).getTextContent();
                    break;
                case "lastName":
                    employee.lastName = employeeItemList.item(j).getTextContent();
                    break;
                case "country":
                    employee.country = employeeItemList.item(j).getTextContent();
                    break;
                case "age":
                    employee.age = Integer.parseInt(employeeItemList.item(j).getTextContent());
                    break;
            }
        }

        return employee;
    }

    public static String readString(String fileName) {

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            StringBuilder jsonSB = new StringBuilder();
            String tmpString;

            while ((tmpString = br.readLine()) != null) {
                jsonSB.append(tmpString);
            }

            return jsonSB.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

/*      Это решение короче и проще

        JSONParser parser = new JSONParser();
        try {
            // Сразу парсим массив JSON из файла
            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(fileName));
            Gson gson = new Gson();
            List<Employee> employees = new ArrayList<>();
            // Обходим массив
            for (Object elem : jsonArray) {
                JSONObject jsonObject = (JSONObject) elem;
                // Получаем объект и добавляем его в список
                employees.add(gson.fromJson(jsonObject.toString(), Employee.class));
            }
            for (Employee emp : employees) {
                System.out.println(emp);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
*/

    }

    public static List<Employee> jsonToList(String json) {

        JSONArray jsonArray = null;
        JSONParser parser = new JSONParser();

        try {
            jsonArray = (JSONArray) parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Employee> staff = new ArrayList<>();
        Gson gson = new Gson();

        if (jsonArray != null) {

            for (Object o : jsonArray) {

                Employee employee = gson.fromJson(o.toString(), Employee.class);
                staff.add(employee);
            }
        }

        return staff;
    }
}