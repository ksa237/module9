import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {

        Function<String, List<String>> strToInt = origin -> {

            List<String> l = new ArrayList<>() ;
            l.add(origin);
            return l;


        };

        List<String> intResult = strToInt.apply("1234");
        System.out.println(intResult);

    }


}
