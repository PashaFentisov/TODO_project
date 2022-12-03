package TODO;

import java.io.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    static File file = new File("tasks.txt"); //TODO розобраться з файлом
    transient Scanner scan = new Scanner(System.in);
    private LinkedList<Task> tasksList = new LinkedList<>();
    private List<String> temporaryListToReadFromFile = new LinkedList<>();
    private Task task;
    private static User user;

    private User() {
    }

    public synchronized static User getInstatnce(){
        if(user==null){
            user = new User();
        }
        return user;
    }


    public void fillList() {
        int day;
        int month;
        String tempString;
        while (true) {
            System.out.print(ANSI_YELLOW + "Введіть таск який хочете додати до файлу, або stop: " + ANSI_RESET);
            tempString = scan.nextLine();
            if (tempString.equalsIgnoreCase("stop")) {
                break;
            }
            task = new Task(tempString);
            System.out.println(ANSI_YELLOW + "\nВведіть дату до якої треба виконати таск" + ANSI_RESET);
            System.out.print(ANSI_YELLOW + "Введіть день: " + ANSI_RESET);
            if (scan.hasNextInt()) {
                day = scan.nextInt();
                try {
                    task.setDoBefore(task.getDoBefore().withDayOfMonth(day));
                } catch (Exception e) {
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
                try {
                    task.setDoBefore(task.getDoBefore().withMonth(month));
                } catch (Exception e) {
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

    public void editList() {
        showListTasks();
        System.out.print(ANSI_YELLOW + "ви хочете додати(add) таск чи видалити(delete) таск: " + ANSI_RESET);
        String tempString = scan.next();
        if (tempString.equalsIgnoreCase("add")) {
            scan.nextLine();
            fillList();
        } else {
            int i;
            while (true) {
                showListTasks();
                System.out.print(ANSI_YELLOW + "Який таск за номером ви хочете видалити: " + ANSI_RESET);
                tempString = scan.next();
                try {
                    i = Integer.parseInt(tempString);
                } catch (Exception e) {
                    break;
                }
                for (int j = 0; j < tasksList.size(); j++) {
                    if (tasksList.get(j).getNumber()==i) {
                        tasksList.remove(j);
                        System.out.println(ANSI_GREEN + "Task " + i + " видалено" + ANSI_RESET);
                        for (int k = 0; k < tasksList.size(); k++) {
                            if(tasksList.get(k).getNumber()>i){
                                tasksList.get(k).setNumber(tasksList.get(k).getNumber()-1);
                            }
                        }
                    }
                }
            }
            addTasksToFile();
        }
    }

    public void showListTasks() {
        System.out.println(ANSI_YELLOW + "Ваші таски: " + ANSI_RESET);
        tasksList.forEach(System.out::println);
    }

    public void showTasksInFile() {
        System.out.println(ANSI_YELLOW + "Ваші таски в файлі: " + ANSI_RESET);
        readFromFileToList();
        temporaryListToReadFromFile.forEach(System.out::println);
        countAllTasks = temporaryListToReadFromFile.size();
        countDoneTasks = (int) temporaryListToReadFromFile.stream().filter(s -> s.contains("DONE")).count();
        System.out.println(ANSI_YELLOW + "\nВсього тасків: " + countAllTasks);
        System.out.println("Виконаних тасків: " + countDoneTasks);
        System.out.println("Не виконаних тасків: " + (countAllTasks - countDoneTasks) + ANSI_RESET);
        temporaryListToReadFromFile.clear();
    }

    public void addTasksToFile() {
        System.out.println(ANSI_YELLOW + "Ось таски які будуть додані в файл, якщо ви згодні введіть enter" + ANSI_RESET);
        showListTasks();
        if (!scan.next().equalsIgnoreCase("enter")) {
            editList();
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

    public void makeTaskDone() {
        Pattern p = Pattern.compile("\\d{1,4}");
        Matcher m;
        temporaryListToReadFromFile.clear();
        readFromFileToList();
        int j;
        while (true) {
            temporaryListToReadFromFile.forEach(System.out::println);
            System.out.print(ANSI_YELLOW + "Вкажіть номер таску який уже зробили, або будь який інший текст: " + ANSI_RESET);
            String s = scan.next();
            m = p.matcher(s);
            if (m.matches()) {
                j = Integer.parseInt(s);
                try {
                    if (LocalDate.now().isAfter(LocalDate.parse(temporaryListToReadFromFile.get(j - 1).substring(temporaryListToReadFromFile.get(j - 1).indexOf("Виконати до:")).replace("Виконати до: ", "").trim() + " " + LocalDate.now().getYear(), Task.getFormatForDateOfMade()))) {
                        s = "З запізненням";
                    } else {
                        s = "Вчасно";
                    }
                } catch (Exception e) {
                    s = "";
                }
                temporaryListToReadFromFile.set(j - 1, String.format("%-120s %-4s %s %s", temporaryListToReadFromFile.get(j - 1), "DONE", LocalDate.now().format(Task.getFormatForDateOfMade()), s));
            } else {
                break;
            }
        }
        writeTaskListToFile();
        temporaryListToReadFromFile.clear();
    }

    private void writeTaskListToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < temporaryListToReadFromFile.size(); i++) {
                writer.write(temporaryListToReadFromFile.get(i));
                writer.newLine();
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void showTasksInProgress() {   //TODO виводити скільки осталось часу до дедлайна
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

    public void deleteTasksFromFile() {
        int intTemp;
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
                if (scan.nextLine().equalsIgnoreCase("stop")) {
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
            if (temp.equalsIgnoreCase("DONE")) {
                System.out.print(ANSI_RED + "Всі виконані таски будуть видалені з пам'яті, для підтвердження натисніть enter, для відміни введіть stop: " + ANSI_RESET);
                scan.nextLine();
                if (scan.nextLine().equalsIgnoreCase("stop")) {
                    continue;
                }
                for (int i = 0; i < temporaryListToReadFromFile.size(); i++) {
                    if (temporaryListToReadFromFile.get(i).contains("DONE")) {
                        temporaryListToReadFromFile.remove(i);
                        --i;
                    }
                }
                System.out.println(ANSI_GREEN + "Всі виконані таски видалено" + ANSI_RESET);
                break;

            } else {
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

