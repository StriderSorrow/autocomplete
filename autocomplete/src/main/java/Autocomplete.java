import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Autocomplete {
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void airports(int x){
        String file = "airports.csv";

        TreeMap<String, String> map = new TreeMap<>();
        TreeMap<Integer, String> sortedMap = new TreeMap<>();


        ClassLoader classLoader = Autocomplete.class.getClassLoader();

        String line;
        try (InputStream inputStream = classLoader.getResourceAsStream(file)) {
            assert inputStream != null;
            try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(streamReader)){

                Scanner console = new Scanner(System.in);
                System.out.print("Введите текст: ");
                String search = console.nextLine();

                if (Objects.equals(search, "!quit")){
                    System.exit(0);
                }
                long start = System.currentTimeMillis();
                while ((line = reader.readLine()) != null) {
                    String[] row = line.split(",");
                    for(int i = 0; i < row.length; i++){
                        if (i + 1 == x) {
                            if (row[i].toLowerCase().startsWith('"' + search.toLowerCase())) {
                                map.put(row[i], line);
                            }
                        }
                    }
                }
                if (!map.isEmpty()) {
                    if (isNumeric(map.firstKey())){
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            sortedMap.put(Integer.parseInt(entry.getKey()), entry.getValue());
                        }
                        for (Map.Entry<Integer, String> entry : sortedMap.entrySet()) {
                            System.out.println(entry.getKey() + "[" + entry.getValue() + "]");
                        }
                    }
                    else {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            System.out.println(entry.getKey() + "[" + entry.getValue() + "]");
                        }
                    }
                }
                else{
                    System.out.println("Совпадений не найдено");
                }
                long finish = System.currentTimeMillis();
                long elapsed = finish - start;
                System.out.println("Найдено "+ map.size() +" строк. На их поиск затрачено " + elapsed + "мс.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        airports(x);
    }

    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        airports(x);
    }

}

