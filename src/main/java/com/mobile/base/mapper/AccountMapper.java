package com.mobile.base.mapper;

import com.mobile.base.dto.account.AccountDto;
import com.mobile.base.form.account.CreateAccountForm;
import com.mobile.base.model.entity.Account;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {})
public interface AccountMapper {
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateAccountFormToEntity")
    Account fromCreateAccountFormToEntity(CreateAccountForm createAccountForm);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateFromUpdateAccountForm")
    void updateFromUpdateAccountForm(@MappingTarget Account account, CreateAccountForm createAccountForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToAccountDto")
    AccountDto fromEntityToAccountDto(Account account);

    @IterableMapping(elementTargetType = AccountDto.class, qualifiedByName = "fromEntityToAccountDto")
    @Named("fromEntitiesToAccountDtoList")
    List<AccountDto> fromEntitiesToAccountDtoList(List<Account> accounts);
}
