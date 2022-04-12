import java.util.List;

public class XmlToJson {
    public static void main(String[] args) {

        List<Employee> list = Util.parseXML("data.xml");
        Util.writeString("data2.json", Util.listToJson(list));

    }
}
