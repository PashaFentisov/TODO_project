package TODO;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task{
    static private DateTimeFormatter formatForDateOfMade = DateTimeFormatter.ofPattern("d MMMM yyyy");
    static private DateTimeFormatter formatForExpiryDate = DateTimeFormatter.ofPattern("d MMMM");
    private String text;
    private boolean isDone;
    private LocalDate doBefore = LocalDate.now();
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

    public static DateTimeFormatter getFormatForDateOfMade() {
        return formatForDateOfMade;
    }

    public static DateTimeFormatter getFormatForExpiryDate() {
        return formatForExpiryDate;
    }

    public Task() {
        this("null");
    }

    public LocalDate getDoBefore() {
        return doBefore;
    }

    public void setDoBefore(LocalDate doBefore) {
        this.doBefore = doBefore;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("Task %d: %-50s задано: %-20s Виконати до: %s", number, text,
                             LocalDate.now().format(formatForDateOfMade),
                             doBefore.format(formatForExpiryDate));
    }
}
