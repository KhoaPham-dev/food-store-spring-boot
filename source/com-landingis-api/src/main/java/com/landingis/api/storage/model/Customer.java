package com.landingis.api.storage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE+"customer")
public class Customer extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "account_id")
    @MapsId
    private Account account;

    @Column(name = "address")
    private String address;

    private Date birthday;

    private Integer sex;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    private Boolean isAdminCreated = true;

}
