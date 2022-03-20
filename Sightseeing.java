import java.util.Arrays;
import java.util.HashMap;

public class Sightseeing {
    /*
    Массивы в которых сохранена информация про достопремичательности, их важность, времязатратность и имена.
    В обоих массивах они отсортированы по убыванию важности.
     */
    private static final int[] time = {12, 8, 8, 7, 3, 11, 20, 10, 4, 10,
            16, 14, 4, 2, 18, 14, 2, 6, 4, 24};
    private static final String[] names = {"Всероссийский музей А.С. Пушкина и филиалы", "Литературно-мемориальный музей Ф.М. Достоевского",
    "Казанский собор", "Кунсткамера", "Екатерининский дворец", "Зоологический музей", "Петропавловская крепость", "Русский музей",
    "Спас на Крови", "Исаакиевский собор", "Эрмитаж", "Зимний дворец Петра I", "Музей восковых фигур", "Петербургский музей кукол",
    "Ленинградский зоопарк", "Музей современного искусства Эрарта", "Медный всадник", "Музей микроминиатюры «Русский Левша»",
    "Музей обороны и блокады Ленинграда", "Навестить друзей"};

    /*
    Я считаю, что целью нашего алгоритма будет максимизация суммы "важности" всех мест, которые можно посетить за 16 часов. 16 часов взяты так как,
    мы приехали в Питер, 16 часов погуляли, поспали 8, еще 16 погуляли и наконец поспали еще 8. В случае если часы сна можно варьировать,
    поспав например сначала 10 часов, а потом 6 задача существенно усложняется и алгоритма с осмысленной сложностью я не знаю.
    Т.к задача в целом является разновидностью задачи о рюкзаке, то и функция названа точно также. Используется динамическое программированние,
    одновременно с таблицей результатов, хранится и таблица с информацией о том, какие конкретно достопремичательнсти мы посещаем в том или ином
    случае.
     */
    public static HashMap<Integer, String> backpackEx(int n, int m, int[] time) {
        int[][] dp = new int[n+1][m+1];
        String[][] indexes = new String[n+1][m+1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                indexes[i][j] = "0";
                if (i == 0 || j == 0) {
                    dp[i][j] = 0;
                } else if (i == 1) {
                    if (time[0] <= j) {          // Просто записываем первую достопримечательность, если она влезает в кол-во часов.
                        dp[1][j] = 21-i;         // т.к мы максимизируем важность, то 1ая по важности достопримечательность должна иметь максимальный коэфициент.
                        indexes[1][j]+=" " + i;
                    }
                } else {
                    if (time[i-1] >= j) {
                        dp[i][j] = dp[i-1][j];
                        indexes[i][j] = indexes[i-1][j];
                    } else {
                        int newImportance = (21 - i) + dp[i-1][j - time[i-1]];  // Мы на каждом шаге выбираем между предыдущим вариантом и суммой, которая состоит из
                        if (newImportance > dp[i-1][j]) {                       // важности текущей достопримечательности и максимальной суммы важности оставшегося времени.
                            dp[i][j] = newImportance;
                            indexes[i][j] = indexes[i-1][j - time[i-1]] + " " + i;
                        } else {
                            dp[i][j] = dp[i-1][j];
                            indexes[i][j] = indexes[i-1][j];
                        }
                    }
                }
            }
        }
        String[] stringResult = indexes[n][m].split(" ");
        HashMap<Integer, String> result = new HashMap<>();
        for (int i = 1; i < stringResult.length; i++) {
            result.put(Integer.parseInt(stringResult[i]), names[Integer.parseInt(stringResult[i])-1]);
        }
        return result;
    }

    public static void main(String[] args) {
        HashMap<Integer,String> firstDay = backpackEx(20, 32, time);  // т.к работать с дробными числами неудобно, я домножил все часы и общее необходимое время на 2.
        String list = "В первый день стоит посетить: ";
        System.out.println(firstDay);
        int[] newTime = Arrays.copyOf(time, time.length);
        for (int i = 0; i < newTime.length; i++) {
            if (firstDay.containsKey(i+1)) {
                newTime[i]+=100;                  // чтобы уже посещенные в первый день достопримечательности не могли быть использованны во второй день,
                list+=firstDay.get(i+1) + ", ";   // я увеличиваю время необходимое на их посещение на бесконечно большое (для данных услови) число часов.
            }
        }
        HashMap<Integer, String> secondDay = backpackEx(20, 32, newTime);
        list+= "а во второй день лучше всего зайти в: ";
        for (int i = 0; i < newTime.length; i++) {
            if (secondDay.containsKey(i+1)) {
                list+=secondDay.get(i+1) + ", ";
            }
        }
        System.out.println(secondDay);
        System.out.println(list.substring(0, list.length() - 2) + "."); //убираем запятую с конца и меняем ее на точку.
    }
}
