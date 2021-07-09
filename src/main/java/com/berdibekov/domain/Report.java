package com.berdibekov.domain;

import lombok.Data;

import java.util.List;

@Data
public class Report {

    private List<Task> tasks;
    private User user;
    private Project project;
}
