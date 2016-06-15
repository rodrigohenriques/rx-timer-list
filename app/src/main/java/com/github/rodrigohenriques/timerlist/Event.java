package com.github.rodrigohenriques.timerlist;

public class Event {
    private String name;
    private Long elapsedTime;

    public Event(String name, Long elapsedTime) {
        this.name = name;
        this.elapsedTime = elapsedTime;
    }

    public String getName() {
        return name;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }
}