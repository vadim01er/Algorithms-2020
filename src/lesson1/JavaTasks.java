package lesson8.lesson1;

import kotlin.NotImplementedError;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.abs;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */

    //Трудоемкость(T): O(nlogn)
    //Ресурсоемкость(R): O(n)
    static public void sortTimes(String inputName, String outputName) {
        try (FileReader reader = new FileReader(inputName); FileWriter writer = new FileWriter(outputName)) {
            Scanner scanner = new Scanner(reader);
            ArrayList<Integer> list = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss aa");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    list.add((int)simpleDateFormat.parse(line).getTime());
                } catch (ParseException e) {
                    throw new NotImplementedError();
                }
            }
            StringBuilder str = new StringBuilder();
            list.stream().sorted().forEach(value -> str.append(simpleDateFormat.format(new Date(value))).append("\n"));
            writer.write(str.toString());
        } catch (IOException e) {
            throw new NotImplementedError();
        }
    }

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    //Трудоемкость(T): O(nlogn)
    //Ресурсоемкость(R): O(n)
    static public void sortAddresses(String inputName, String outputName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputName, StandardCharsets.UTF_8));
             FileWriter fileWriter = new FileWriter(new File(outputName), StandardCharsets.UTF_8)) {
            String line;
            TreeMap<String, TreeSet<String>> addressAndPeople = new TreeMap<>((o1, o2) -> {
                String[] arrO1 = o1.split(" ");
                String[] arrO2 = o2.split(" ");
                if (arrO1[0].compareTo(arrO2[0]) == 0) {
                    return Integer.compare(Integer.parseInt(arrO1[1]), Integer.parseInt(arrO2[1]));
                } else return arrO1[0].compareTo(arrO2[0]);
            });
            while ((line = bufferedReader.readLine()) != null) {
                String[] arrLine = line.split(" - ");
                if (!addressAndPeople.containsKey(arrLine[1])) addressAndPeople.put(arrLine[1], new TreeSet<>());
                addressAndPeople.get(arrLine[1]).add(arrLine[0]);
            }
            StringBuilder str = new StringBuilder();
            addressAndPeople.forEach((key, value) -> str.append(key)
                    .append(" - ")
                    .append(String.join(", ", value))
                    .append("\n"));
            fileWriter.write(String.valueOf(str));
        } catch (IOException e) {
            throw new NotImplementedError();
        }
    }

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    //Трудоемкость(T): O(n + k)
    //Ресурсоемкость(R): O(n)
    static public void sortTemperatures(String inputName, String outputName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputName));
             FileWriter writer = new FileWriter(outputName)) {
            String line;
            final int minValue = 2730;
            List<Integer> arrayList = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                int number = Integer.parseInt(line.replace(".", ""));
                number += minValue;
                arrayList.add(number);
            }
            int[] arrInt = new int[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) arrInt[i] = arrayList.get(i);
            for (int value : Sorts.countingSort(arrInt, 7730)) {
                writer.write(((float) value - minValue) / 10 + "\n");
            }
        } catch (IOException e) {
            throw new NotImplementedError();
        }
    }

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
