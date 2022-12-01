package TODO;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task{
    private DateTimeFormatter formatForDateOfMade = DateTimeFormatter.ofPattern("d MMMM yyyy");
    private DateTimeFormatter formatForExpiryDate = DateTimeFormatter.ofPattern("d MMMM");
    private String text;
    private boolean isDone;
    private LocalDate doBefore;
    private LocalDate doneDate;
    private boolean isOnTime;
    private int number;
    private static int countOfTasks;

    static{
        try {
            if(Files.readAllLines(User.file.toPath()).size()!=0){
                Task auxilaryvar = new Task();
                try(BufferedReader reader = new BufferedReader(new FileReader(User.file))){
                    while(reader.ready()){
                        auxilaryvar.text = reader.readLine();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                countOfTasks = Integer.parseInt(auxilaryvar.text.substring(5,6));
                auxilaryvar = null;
            }else{
                countOfTasks = 0;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Task(String text) {
        this.text = text;
        this.number = ++countOfTasks;
    }

    public Task() {
        this("null");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public LocalDate getDoBefore() {
        return doBefore;
    }

    public void setDoBefore(LocalDate doBefore) {
        this.doBefore = doBefore;
    }

    public LocalDate getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDate doneDate) {
        this.doneDate = doneDate;
    }

    public boolean isOnTime() {
        return isOnTime;
    }

    public void setOnTime(boolean onTime) {
        isOnTime = onTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public static int getCountOfTasks() {
        return countOfTasks;
    }

    public static void setCountOfTasks(int countOfTasks) {
        Task.countOfTasks = countOfTasks;
    }

    @Override
    public String toString() {
        return String.format("Task %d: %-50s задано: %-20s Виконати до: %s", number, text,
                             LocalDate.now().format(formatForDateOfMade),
                             doBefore.format(formatForExpiryDate));
    }
}
