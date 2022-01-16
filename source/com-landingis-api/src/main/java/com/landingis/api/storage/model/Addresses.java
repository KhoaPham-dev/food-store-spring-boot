package com.landingis.api.storage.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE+"addresses")
public class Addresses extends Auditable<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private Province district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commune_id")
    private Province commune;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;
}
