package com.mobile.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiMessageDto<T> {
    private boolean result = true;
    private String code = null;
    private T data = null;
    private String message = null;
}
