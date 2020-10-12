package com.gnnu.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Student implements Serializable {
    private Integer id;
    private String sname;
    private String snum;
    private Double score;
}
