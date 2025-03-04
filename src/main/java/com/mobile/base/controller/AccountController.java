package com.mobile.base.controller;

import com.mobile.base.controller.base.BaseController;
import com.mobile.base.dto.ApiMessageDto;
import com.mobile.base.dto.account.AccountDto;
import com.mobile.base.exception.ResourceNotFoundException;
import com.mobile.base.mapper.AccountMapper;
import com.mobile.base.model.entity.Account;
import com.mobile.base.repository.AccountRepository;
import com.mobile.base.utils.ApiMessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController extends BaseController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountMapper accountMapper;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<AccountDto> getAccount(@PathVariable Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return ApiMessageUtils.success(accountMapper.fromEntityToAccountDto(account), "Get account successfully");
    }
}
