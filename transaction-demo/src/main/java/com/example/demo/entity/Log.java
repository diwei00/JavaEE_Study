package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Log {
    private int id;
    private LocalDateTime timestamp;
    private String message;
}
