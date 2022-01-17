package com.landingis.api.storage.criteria;


import com.landingis.api.storage.model.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
@Data
public class ProductCriteria {
    private Long id;
    private String name;
    private Long categoryId;
    private Integer saleoff;
    private Integer status;

    public Specification<Product> getSpecification() {
        return new Specification<Product>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(!StringUtils.isEmpty(getName())){
                    predicates.add(cb.like(cb.lower(root.get("name")), "%"+getName().toLowerCase()+"%"));
                }
                if(getCategoryId() != null) {
                    Join<Category, Product> joinCategory = root.join("category", JoinType.INNER);
                    predicates.add(cb.equal(joinCategory.get("id"),getCategoryId()));
                }
                if(getSaleoff() != null) {
                    predicates.add(cb.equal(root.get("saleoff"), getSaleoff()));
                }
                if(getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
