import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Autocomplete {


    public static void main(String[] args) {
        if (args.length != 0)
        {
            if (args.length == 1)
            {
                if (isNumeric(args[0])) {
                    int x = Integer.parseInt(args[0]);
                    airports(x);
                }
                else {
                    System.out.println("Значение параметра должно быть цифровым!");
                }
            }
            else{
                System.out.println("Для работы программы необходим только один параметр!");
            }
        }
        else{
            System.out.println("Для работы программы необходим один параметр!");
        }
    }

    public static boolean isNumeric(String str) {
        //Метод проверяет, являются ли значения параметра цифровыми
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void airports(int x){
        //Блок объявления переменных
        TreeMap<String, String> map = new TreeMap<>();
        TreeMap<Integer, String> sortedMap = new TreeMap<>();
        ClassLoader classLoader = Autocomplete.class.getClassLoader();
        String file = "airports.csv";
        String line;

        //"Вытаскивание" файла CSV из ресурсов проекта
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

                //основной цикл поиска
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

                    //Если элементы столбца цифровые, происходит сортировка и их вывод в консоль
                    if (isNumeric(map.firstKey())){
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            sortedMap.put(Integer.parseInt(entry.getKey()), entry.getValue());
                        }
                        for (Map.Entry<Integer, String> entry : sortedMap.entrySet()) {
                            System.out.println(entry.getKey() + "[" + entry.getValue() + "]");
                        }
                    }
                    //Если элементы столбца ,буквенные, сразу происходит их вывод в консоль,
                    //так как они уже отсортированы в лексикографическом порядке
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
}

