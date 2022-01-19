package com.landingis.api.storage.criteria;

import com.landingis.api.storage.model.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersDetailCriteria {
    private Long id;
    private Long ordersId;
    private Long productId;
    private Integer kind;
    private Integer status;

    public Specification<OrdersDetail> getSpecification() {
        return new Specification<OrdersDetail>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<OrdersDetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getKind() != null){
                    predicates.add(cb.equal(root.get("kind"),getKind()));
                }
                if (getStatus() != null){
                    predicates.add(cb.equal(root.get("status"),getStatus()));
                }
                if(getOrdersId() != null) {
                    Join<Orders, OrdersDetail> joinOrders = root.join("orders", JoinType.INNER);
                    predicates.add(cb.equal(cb.lower(joinOrders.get("id")), getOrdersId()));
                }
                if(getProductId() != null) {
                    Join<Product, OrdersDetail> joinProduct = root.join("product", JoinType.INNER);
                    predicates.add(cb.equal(cb.lower(joinProduct.get("id")), getProductId()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
