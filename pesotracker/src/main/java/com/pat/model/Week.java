package com.pat.model;
 import java.time.LocalDate;



public class Week {
    private LocalDate start;
    private LocalDate end;


    public Week(LocalDate start, LocalDate end){
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart(){
        return start;
    }

    public LocalDate getEnd(){
        return end;
    }

    @Override
    public String toString(){
        return start + " - " + end;
    }

    public boolean contains(LocalDate date){
        return !date.isBefore(start) && !date.isAfter(end);
    }
}

