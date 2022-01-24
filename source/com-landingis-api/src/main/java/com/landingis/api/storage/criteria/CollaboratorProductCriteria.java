package com.landingis.api.storage.criteria;

import com.landingis.api.storage.model.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class CollaboratorProductCriteria {
    private Long id;
    private Long collaboratorId;
    private Long productId;
    private Integer status;

    public Specification<CollaboratorProduct> getSpecification() {
        return new Specification<CollaboratorProduct>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<CollaboratorProduct> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if(getCollaboratorId() != null) {
                    Join<Collaborator, CollaboratorProduct> joinCollaborator = root.join("collaborator", JoinType.INNER);
                    predicates.add(cb.equal(joinCollaborator.get("id"), getCollaboratorId()));
                }
                if(getProductId() != null) {
                    Join<Product, CollaboratorProduct> joinProduct= root.join("product", JoinType.INNER);
                    predicates.add(cb.equal(joinProduct.get("id"), getProductId()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
