package com.landingis.api.storage.criteria;

import com.landingis.api.storage.model.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class ImportExportCriteria {
    private Long id;//
    private String code;//
    private Integer categoryKind;//
    private Integer kind;//
    private Long accountId;
    private Integer status;//

    public Specification<ImportExport> getSpecification() {
        return new Specification<ImportExport>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<ImportExport> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getCode() != null){
                    predicates.add(cb.like(cb.lower(root.get("code")), "%" + getCode().toLowerCase() + "%"));
                }
                if(getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if(getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if(getCategoryKind() != null) {
                    Join<Category, ImportExport> joinCategory = root.join("category", JoinType.INNER);
                    predicates.add(cb.equal(cb.lower(joinCategory.get("kind")), getCategoryKind()));
                }
                if(getAccountId() != null) {
                    Join<Account, ImportExport> joinAccount = root.join("account", JoinType.INNER);
                    predicates.add(cb.equal(cb.lower(joinAccount.get("id")), getAccountId()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
