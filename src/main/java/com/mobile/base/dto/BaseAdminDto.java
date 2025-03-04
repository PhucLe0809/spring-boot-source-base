package com.mobile.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseAdminDto {
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Created Date")
    private LocalDateTime createdDate;

    @Schema(description = "Modified Date")
    private LocalDateTime modifiedDate;
}
