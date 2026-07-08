package com.projects.school.reportesservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class PageWrapper<T> {

    private List<T> content;

}
