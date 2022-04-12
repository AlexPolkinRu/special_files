import java.util.List;

public class JsonToObjects {
    public static void main(String[] args) {

        String json = Util.readString("data.json");

        List<Employee> list = Util.jsonToList(json);

        for (Employee employee : list) {
            System.out.println(employee);
        }

    }
}
