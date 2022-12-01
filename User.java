package TODO;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 * @author Pasha Fentisov
 */

public class User {

    private int countDoneTasks = 0;
    private int countAllTasks = 0;

    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_GREEN = "\u001B[32m";

    public static final String ANSI_RED = "\u001B[31m";

    public static final String ANSI_YELLOW = "\u001B[33m";
    //TODO Pattern p = Pattern.compile("\\d{1,3}");
    //TODO Matcher m = null;
    String sep = File.separator;
    File file = new File("D:"+sep+"idea project"+sep+"SomeProjects"+sep+"src"+sep+"TODO"+sep+"tasks.txt"); //TODO розобраться з файлом
    Scanner scan = new Scanner(System.in);
    private LinkedList<Task> tasksList = new LinkedList<>();
    Task task;
    private int day;
    private int month;
    private LocalDate expiryDate;


    public void fillList() {
        while (true) {
            System.out.print(ANSI_YELLOW + "Введіть таск який хочете додати до файлу, або stop: " + ANSI_RESET);
            task = new Task(scan.nextLine());
            if (task.getText().equalsIgnoreCase("stop")) {
                break;
            }
            System.out.println(ANSI_YELLOW + "\nВведіть дату до якої треба виконати таск" + ANSI_RESET);
            System.out.print(ANSI_YELLOW + "Введіть день: " + ANSI_RESET);
            if(scan.hasNextInt()) {
                day = scan.nextInt();
                if (day > 31 || day <= 0) {
                    day = LocalDate.now().getDayOfMonth();
                    System.out.println("You entered wrong value, you have 1 day to do this task or edit it");
                }
            }
            System.out.print(ANSI_YELLOW + "Введіть місяць: " + ANSI_RESET);
            if(scan.hasNextInt()) {
                month = scan.nextInt();
                if (month > 12 || month <= 0) {
                    month = LocalDate.now().getMonthValue();
                    System.out.println("You entered wrong value, you have to finish this task in this month");
                }
            }
            scan.nextLine();
            expiryDate = LocalDate.of(LocalDate.now().getYear(), month, day);
            task.setDoBefore(expiryDate);
            tasksList.add(task);
            System.out.println(task);
        }
//        addTasksToFile();
    }

//    /**
//     * редагуєм список тасків перед записом їх у файл
//     * в гілці if викликаєм метод для додавання тасків до списку
//     * в гілці else видаляєм таски з списку перед подачею до файлу
//     */
//    public void editList() {
//        showListTasks();
//        System.out.print(ANSI_YELLOW + "ви хочете додати(add) таск чи видалити(delete)" + ANSI_RESET);
//        System.out.println();
//        String s = scan.next();
//        if (s.equalsIgnoreCase("add")) {
//            scan.nextLine();
//            fillList();
//            return;
//        } else {
//            int i;
//            while (true) {
//                showListTasks();
//                System.out.print(ANSI_YELLOW + "Який таск за номером ви хочете видалити: " + ANSI_RESET);
//                try {
//                    i = scan.nextInt();
//                    tasks.remove(i);
//                    System.out.print(ANSI_YELLOW + "Таск було видалено якщо бажаєте закінчити редагування введіть stop" + ANSI_RESET);
//                    System.out.println();
//                    if (scan.next().equalsIgnoreCase("stop")) {
//                        showListTasks();
//                        break;
//                    }
//                } catch (Exception e) {
//                        System.out.println(ANSI_YELLOW + "        !!!неправильне значення!!!" + ANSI_RESET);
//                        System.out.println();
//                    scan.nextLine();
//                }
//            }
//            addTasksToFile();
//        }
//
//    }
//
//    /**
//     * виводимо список з тасками перед записом у файл
//     */
//    public void showListTasks() {
//        System.out.print(ANSI_YELLOW + "Ваші таски: " + ANSI_RESET);
//        System.out.println();
//        for (int i = 0; i < tasks.size(); i++) {
//            String x = "Task №" + i + " " + tasks.get(i);
//            System.out.print(x);
//            System.out.println();
//        }
//    }
//
//    /**
//     * виводимо всі таски з файлу
//     */
//    public void showTasksInFile() {
//        System.out.print(ANSI_YELLOW + "Ваші таски в файлі: " + ANSI_RESET);
//        System.out.println();
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            while (reader.ready()) {
//                s = reader.readLine();
//                countAllTasks++;
//                if (s.contains("DONE")) {
//                    countDoneTasks++;
//                }
//                System.out.print(s);
//                System.out.println();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.print(ANSI_YELLOW + "\nВсього тасків: " + countAllTasks);
//        System.out.println();
//        System.out.print("Виконаних тасків: " + countDoneTasks);
//        System.out.println();
//        System.out.print("Не виконаних тасків: " + (countAllTasks - countDoneTasks) + ANSI_RESET);
//        System.out.println();
//    }
//
//    /**
//     * передаємо всі таски з списку до файлу
//     * якщо щось не так викликаєм метод editList()
//     * очищаєм список для подальших операцій
//     */
//    public void addTasksToFile() {
//            System.out.println(ANSI_YELLOW + "Ось таски які будуть додані в файл, якщо ви згодні введіть enter" + ANSI_RESET);
//            System.out.println();
//        showListTasks();
//        if (!scan.next().equalsIgnoreCase("enter")) {
//            editList();
//        } else {
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
//                for (int i = 0; i < tasks.size(); i++) {
//                    writer.write(tasks.get(i));
//                    writer.newLine();
//                }
//                writer.flush();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//                    System.out.print(ANSI_YELLOW + "Succeed" + ANSI_RESET);
//                    System.out.println();
//            tasks.clear();
//        }
//    }
//
//    /**
//     * Помічаєм вибраний таск як зроблений (DONE) і помічаєм датою виконання
//     * Зчитуєм місткість файлу в список, вибираєм таск за номером, редагуєм і заносим назад в список
//     * Після завершення записуєм список в файл
//     *
//     * @see Main Main
//     */
//    public void makeTaskDone() {
//        tasks.clear();
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            while (reader.ready()) {
//                tasks.add(reader.readLine());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        int j;
//
//        while (true) {
//            String isBefore;
//            showListTasks();
//            System.out.print(ANSI_YELLOW + "Вкажіть номер таску який уже зробили, або будь який інший текст: " + ANSI_RESET);
//            String s = scan.next();
//            m = p.matcher(s);
//            if (m.matches()) {
//                dt = LocalDate.now();
//                j = Integer.parseInt(s);
//                try {
//                    date = tasks.get(j).substring(tasks.get(j).indexOf("Виконати до")).replace("Виконати до ", "").trim() + " " + LocalDate.now().getYear();
//                    ontime = LocalDate.parse(date, dtf);
//                    if (dt.isAfter(ontime)) {
//                        s = "З запізненням";
//                    } else {
//                        s = "Вчасно";
//                    }
//                } catch (Exception e) {
//                    s = "";
//                }
//                tasks.set(j, String.format("%-120s %-4s %s %s", tasks.get(j), "DONE", dt.format(dtf), s));
//            } else {
//                break;
//            }
//        }
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//            for (int i = 0; i < tasks.size(); i++) {
//                writer.write(tasks.get(i));
//                writer.newLine();
//            }
//            writer.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Зчитуєм з файла рядки і якщо поміченні як DONE то виводимо у консоль
//     */
//    public void showDoneTasks() {
//        int countOnTime = 0;
//        int countNotOnTime = 0;
//        String s = "";
//                System.out.print(ANSI_GREEN + "Виконанні таски " + ANSI_RESET);
//                System.out.println();
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            while (reader.ready()) {
//                s = reader.readLine();
//                if (s.contains("DONE")) {
//                            System.out.print(s);
//                            System.out.println();
//                    if (s.contains("Вчасно")) {
//                        countOnTime++;
//                    } else {
//                        countNotOnTime++;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//                System.out.print(ANSI_YELLOW + "\nВиконано вчасно: " + countOnTime);
//                System.out.println();
//                System.out.print("Виконано не вчасно: " + countNotOnTime + ANSI_RESET);
//                System.out.println();
//    }
//
//    /**
//     * Зчитуєм рядок з файлу, якщо він ще не помічений як DONE то виводимо
//     */
//    public void showTasksInProgress() {
//        int countTime = 0;
//        int i = 0;
//        String s = "";
//                System.out.print(ANSI_RED + "\nНе виконанні завдання" + ANSI_RESET);
//                System.out.println();
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            while (reader.ready()) {
//                s = reader.readLine();
//                if (!s.contains("DONE")) {
//                            System.out.print(i + ". " + s);
//                            System.out.println();
//                    try {
//                        date = s.substring(s.indexOf("Виконати до")).replace("Виконати до ", "").trim() + " " + LocalDate.now().getYear();
//                        ontime = LocalDate.parse(date, dtf);
//                        if (ontime.isBefore(LocalDate.now())) {
//                            countTime++;
//                        }
//                    } catch (Exception e) {
//                    }
//                    i++;
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (countTime == 0) {
//                    System.out.print(ANSI_GREEN + countTime + " - З пропущеним строком виконання" + ANSI_RESET);
//                    System.out.println();
//        } else {
//                    System.out.print(ANSI_RED + countTime + " - З пропущеним строком виконання" + ANSI_RESET);
//                    System.out.println();
//        }
//    }
//
//    /**
//     * Зчитуєм рядки з файлу в список
//     * Обираєм який таск хочем видалити з списку
//     * По закінченню відредагований список записуєм в файл
//     * Є варінт повністю очистити файл ввівши "all"
//     */
//    public void deleteTasksFromFile() {  //TODO додати видалення зроблених
//        tasks.clear();
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            while (reader.ready()) {
//                tasks.add(reader.readLine());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        int k;
//        String s;
//        while (true) {
//                    System.out.print(ANSI_YELLOW + "Введіть номер таску якій хочете видалити, якщо хочете видалити все введіть all, для закінчення введіть stop" + ANSI_RESET);
//                    System.out.println();
//            for (int i = 0; i < tasks.size(); i++) {
//                String x = "Task №" + i + " " + tasks.get(i);
//                        System.out.print(x);
//                        System.out.println();
//            }
//            s = scan.next();
//            if (s.equalsIgnoreCase("stop")) {
//                break;
//            }
//            if (s.equalsIgnoreCase("all")) {
//                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//                    writer.write("");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                try {
//                    k = Integer.parseInt(s);
//                    tasks.remove(k);
//                } catch (Exception e) {
//
//                }
//            }
//        }
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//            for (int i = 0; i < tasks.size(); i++) {
//                writer.write(tasks.get(i));
//                writer.newLine();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
