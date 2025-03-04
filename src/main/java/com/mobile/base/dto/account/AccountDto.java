package com.mobile.base.dto.account;

import com.mobile.base.dto.BaseAdminDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountDto extends BaseAdminDto {
    @Schema(description = "Username")
    private String username;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Phone number")
    private String phone;
}
