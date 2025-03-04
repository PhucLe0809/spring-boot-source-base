package com.mobile.base.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private long totalElements;
}
