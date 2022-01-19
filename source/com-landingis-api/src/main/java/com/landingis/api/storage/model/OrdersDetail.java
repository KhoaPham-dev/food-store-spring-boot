package com.landingis.api.storage.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = TablePrefix.PREFIX_TABLE+"order_details")
public class OrdersDetail{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Double price;
    private Integer amount;
    private String note;

    private Integer kind; // Loại tính hoa hồng: % hoặc $ (chính là kind của collaborator product)
    private Double value; // giá trị hoa hồng: tùy theo kind bên trên
    private Double collaboratorCommission = 0d;//so tien hoa hồng
}
