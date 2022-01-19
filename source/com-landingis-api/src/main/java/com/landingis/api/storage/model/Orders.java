package com.landingis.api.storage.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE+"orders")
@EntityListeners(AuditingEntityListener.class)
public class Orders extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Integer saleOff = 0; // Giảm giá đơn hàng (giảm trước khi tính VAT)
    private Double totalMoney; // Tổng tiền hàng
    private Integer vat; // VAT

    private Integer state; // Trạng thái hiện tại (nhớ tạo constants) 0 created, 1. accepted(da thanh toan), 2 Shipping, 3 done, 4 cancel

    @Column(name = "prev_state")
    private Integer prevState; // Trạng thái trước đó
    private String address; // Dia chi giao hang
    private String receiverName; // Ten nguoi nhan
    private String receiverPhone; // Sdt nguoi nhan

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee; // Nhân viên tạo đơn hàng (nếu người tạo đơn là 1 collaborator thì nhớ gán nhân viên của collaborator đó cho field này)

    @ManyToOne
    @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator; // Ctv tạo đơn hàng (có thể có hoặc null)

    private String code; // Random 6 chữ

    private Boolean isReleaseEmployee = false; // Đã trả lương nv hay chưa (tạm thời để vậy chưa xài)
    private Boolean isReleaseCollaborator = false; // Đã trả lương ctv hay chưa (tạm thời để vậy chưa xài đến)

    private Integer paymentMethod; // Phương thức thanh toán: 1: COD, 2: Online
}
