package com.mobile.base.model.entity;

import com.mobile.base.model.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "db_moneylover_accounts")
@Getter
@Setter
public class Account extends Auditable {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = com.mobile.base.service.id.IdGenerator.class)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;
}
