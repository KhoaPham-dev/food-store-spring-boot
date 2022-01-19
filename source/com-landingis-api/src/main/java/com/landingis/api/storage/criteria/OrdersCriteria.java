package com.landingis.api.storage.criteria;

import com.landingis.api.storage.model.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersCriteria {
    private Long id;
    private Long customerId;
    private Long collaboratorId;
    private Integer state;
    private String code;
    private Integer paymentMethod;
    private Integer status;

    public Specification<Orders> getSpecification() {
        return new Specification<Orders>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Orders> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getPaymentMethod() != null){
                    predicates.add(cb.equal(root.get("paymentMethod"),getPaymentMethod()));
                }
                if (getState() != null){
                    predicates.add(cb.equal(root.get("state"),getState()));
                }
                if(getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if(getCustomerId() != null) {
                    Join<Customer, Orders> joinCustomer = root.join("customer", JoinType.INNER);
                    predicates.add(cb.equal(cb.lower(joinCustomer.get("id")), getCustomerId()));
                }
                if(getCollaboratorId() != null) {
                    Join<Collaborator, Orders> joinCollaborator = root.join("collaborator", JoinType.INNER);
                    predicates.add(cb.equal(cb.lower(joinCollaborator.get("id")), getCollaboratorId()));
                }
                if(getCode() != null) {
                    predicates.add(cb.like(cb.lower(root.get("code")), "%" + getCode().toLowerCase() + "%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
