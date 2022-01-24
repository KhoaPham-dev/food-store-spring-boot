package com.landingis.api.storage.criteria;

import com.landingis.api.storage.model.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ImportExportCriteria {
    private Long id;//
    private Integer categoryKind;//
    private Integer kind;//
    private Integer status;//
    private Date from;
    private Date to;

    public Specification<ImportExport> getSpecification() {
        return new Specification<ImportExport>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<ImportExport> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(getFrom() != null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), getFrom()));
                }
                if(getTo() != null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), getTo()));
                }
                if(getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if(getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if(getCategoryKind() != null) {
                    Join<Category, ImportExport> joinCategory = root.join("category", JoinType.INNER);
                    predicates.add(cb.equal(joinCategory.get("kind"), getCategoryKind()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
