package com.landingis.api.storage.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE+"employee")
public class Employee extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "account_id")
    /*@MapsId*/
    private Account account;

    private Date birthday;
    private Integer sex;

    @Column(name = "agency_address")
    private String address;

    private String identityNumber; //so cmnd
    private Date dateOfIssue; // ngay cap
    private String placeOfIssue; //noi cap

    private String bankNo;
    private String bankName;
    private String branchName;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "label_color")
    private String labelColor;

    private Double salary = 0d;
}
