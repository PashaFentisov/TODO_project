package TODO;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


/**
 * @author Pasha Fentisov
 * @version 1.0
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
    static String sep = File.separator;
    static File file = new File("D:" + sep + "idea project" + sep + "SomeProjects" + sep + "src" + sep + "TODO" + sep + "tasks.txt"); //TODO розобраться з файлом
    transient Scanner scan = new Scanner(System.in);
    private LinkedList<Task> tasksList = new LinkedList<>();
    private List<String> temporaryListToReadFromFile = new ArrayList<>();
    Task task;

    public void fillList() {
        int day;
        int month;
        while (true) {
            System.out.print(ANSI_YELLOW + "Введіть таск який хочете додати до файлу, або stop: " + ANSI_RESET);
            task = new Task(scan.nextLine());
            if (task.getText().equalsIgnoreCase("stop")) {
                break;
            }
            System.out.println(ANSI_YELLOW + "\nВведіть дату до якої треба виконати таск" + ANSI_RESET);
            System.out.print(ANSI_YELLOW + "Введіть день: " + ANSI_RESET);
            if (scan.hasNextInt()) {
                day = scan.nextInt();
                try{
                    task.setDoBefore(task.getDoBefore().withDayOfMonth(day));
                }catch(Exception e){
                    System.out.println("You entered wrong value, you have 1 day to do this task or edit it");
                    task.setDoBefore(task.getDoBefore().withDayOfMonth(LocalDate.now().getDayOfMonth()));
                }
            } else {
                task.setDoBefore(task.getDoBefore().withDayOfMonth(LocalDate.now().getDayOfMonth()));
                System.out.println("You entered wrong value, you have 1 day to do this task or edit it");
            }
            scan.nextLine();
            System.out.print(ANSI_YELLOW + "Введіть місяць: " + ANSI_RESET);
            if (scan.hasNextInt()) {
                month = scan.nextInt();
                try{
                    task.setDoBefore(task.getDoBefore().withMonth(month));
                }catch(Exception e){
                    System.out.println("You entered wrong value, you have to finish this task this month");
                    task.setDoBefore(task.getDoBefore().withMonth(LocalDate.now().getMonthValue()));
                }
            } else {
                System.out.println("You entered wrong value, you have to finish this task this month");
                task.setDoBefore(task.getDoBefore().withMonth(LocalDate.now().getMonthValue()));
            }
            scan.nextLine();
            tasksList.add(task);
            System.out.println(task);
        }
        addTasksToFile();
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
//TODO make this class as Singletone


    public void showListTasks() {
        System.out.println(ANSI_YELLOW + "Ваші таски: " + ANSI_RESET);
        tasksList.forEach(System.out::println);
    }

    public void showTasksInFile() {
        System.out.println(ANSI_YELLOW + "Ваші таски в файлі: " + ANSI_RESET);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                temporaryListToReadFromFile.add(reader.readLine());
            }
            temporaryListToReadFromFile.forEach(System.out::println);
            countAllTasks = temporaryListToReadFromFile.size();
            countDoneTasks = (int) temporaryListToReadFromFile.stream().filter(s -> s.contains("DONE")).count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ANSI_YELLOW + "\nВсього тасків: " + countAllTasks);
        System.out.println("Виконаних тасків: " + countDoneTasks);
        System.out.println("Не виконаних тасків: " + (countAllTasks - countDoneTasks) + ANSI_RESET);
        temporaryListToReadFromFile.clear();
    }

    public void addTasksToFile() {
        System.out.println(ANSI_YELLOW + "Ось таски які будуть додані в файл, якщо ви згодні введіть enter" + ANSI_RESET);
        showListTasks();
        if (!scan.next().equalsIgnoreCase("enter")) {
            //TODO  editList();  and return back
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                for (int i = 0; i < tasksList.size(); i++) {
                    writer.write(tasksList.get(i).toString());
                    writer.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(ANSI_YELLOW + "Succeed" + ANSI_RESET);
            tasksList.clear();
        }
    }

    //TODO зробити спільний файл який буде гітхабі і таски писатимуться туда шлях до нього буде універсальний
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

    public void showDoneTasks() {
        int countOnTime = 0;
        System.out.println(ANSI_GREEN + "Виконанні таски " + ANSI_RESET);
        readFromFileToList();
        countOnTime = (int) temporaryListToReadFromFile.stream().filter(s -> s.contains("DONE")).filter(s -> s.contains("Вчасно")).count();
        temporaryListToReadFromFile.stream().filter(s -> s.contains("DONE")).forEach(System.out::println);
        countDoneTasks = (int) temporaryListToReadFromFile.stream().filter(s -> s.contains("DONE")).count();
        System.out.println(ANSI_YELLOW + "\nВиконано вчасно: " + countOnTime);
        System.out.println("Виконано не вчасно: " + (countDoneTasks - countOnTime) + ANSI_RESET);
        temporaryListToReadFromFile.clear();
    }

    public void showTasksInProgress() {   //TODO виводити кількфсть не з пропущеним строком і скільки осталось до дедлайна часу
        int countTime = 0;
        System.out.println(ANSI_RED + "\nНе виконанні завдання" + ANSI_RESET);
        readFromFileToList();
        temporaryListToReadFromFile.stream().filter(s -> !s.contains("DONE")).forEach(System.out::println);
        countTime = (int) temporaryListToReadFromFile.stream()
                .filter(s -> !s.contains("DONE"))
                .map(s -> s.substring(s.indexOf("Виконати до:")).replace("Виконати до: ", "").trim() + " " + LocalDate.now().getYear())
                .map(string -> LocalDate.parse(string, Task.getFormatForDateOfMade()))
                .filter(doBefore -> doBefore.isBefore(LocalDate.now()))
                .count();
        if (countTime == 0) {
            System.out.println(ANSI_GREEN + countTime + " - З пропущеним строком виконання" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + countTime + " - З пропущеним строком виконання" + ANSI_RESET);
        }
        countTime = (int) temporaryListToReadFromFile.stream().filter(s -> !s.contains("DONE")).count() - countTime;
        System.out.println(ANSI_GREEN + countTime + " - З актуальним строком виконання" + ANSI_RESET);
        temporaryListToReadFromFile.clear();
    }

    private static int intTemp;
    public void deleteTasksFromFile() {  //TODO додати видалення зроблених
        tasksList.clear();
        String temp;
        readFromFileToList();
        while (true) {
            System.out.println(ANSI_YELLOW + "Поточні таски---------------------------------------------------------------------------------------------------------------" + ANSI_RESET);
            temporaryListToReadFromFile.forEach(System.out::println);
            System.out.print(ANSI_YELLOW + "Введіть номер таску якій хочете видалити\nДля видалення всіх таксів - all\nДля видалення зроблених - DONE\nДля закінчення - stop\nПоле для вводу:" + ANSI_RESET);
            temp = scan.next();
            if (temp.equalsIgnoreCase("stop")) {
                break;
            }
            if (temp.equalsIgnoreCase("all")) {
                System.out.print(ANSI_RED + "Всі таски будуть видалені з пам'яті, для підтвердження натисніть enter, для відміни введіть stop: " + ANSI_RESET);
                scan.nextLine();
                if(scan.nextLine().equalsIgnoreCase("stop")){
                    continue;
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(ANSI_GREEN + "Всі таски видалено" + ANSI_RESET);
                return;
            }
            if(temp.equalsIgnoreCase("DONE")){
                //TODO
            }
            else {
                try {
                    intTemp = Integer.parseInt(temp);
                } catch (Exception e) {
                    break;
                }
                for (int i = 0; i < temporaryListToReadFromFile.size(); i++) {
                    if (temporaryListToReadFromFile.get(i).contains("Task " + intTemp + ":")) {
                        temporaryListToReadFromFile.remove(i);
                        System.out.println(ANSI_GREEN + "Task " + intTemp + " видалено" + ANSI_RESET);
                    }
                }
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < temporaryListToReadFromFile.size(); i++) {
                writer.write(temporaryListToReadFromFile.get(i));
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        temporaryListToReadFromFile.clear();
    }

    private void readFromFileToList() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                temporaryListToReadFromFile.add(reader.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//TODO в майбутньому зробити через json запис списку тасків а не тексту
