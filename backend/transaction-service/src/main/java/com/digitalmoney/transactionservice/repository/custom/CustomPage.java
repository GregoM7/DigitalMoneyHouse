package com.digitalmoney.transactionservice.repository.custom;

import lombok.Data;

import java.util.List;

@Data
public class CustomPage<T>{
    private Long totalElements;
    private List<T> content;
}
