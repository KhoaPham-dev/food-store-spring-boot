package com.landingis.api.storage.criteria;

import com.landingis.api.storage.model.*;
import lombok.Data;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CustomerCriteria {
    private Long id;
    private Integer sex;
    private Integer status;
    private String phone;
    private String fullName;

    public Specification<Customer> getSpecification() {
        return new Specification<Customer>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getSex() != null){
                    predicates.add(cb.equal(root.get("sex"),getSex()));
                }
                if(getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if(getPhone() != null) {
                    Join<Account, Customer> joinAccount = root.join("account", JoinType.INNER);
                    predicates.add(cb.equal(joinAccount.get("phone"), getPhone()));
                }
                if(getFullName() != null) {
                    Join<Account, Customer> joinAccount = root.join("account", JoinType.INNER);
                    predicates.add(cb.equal(joinAccount.get("fullName"), getFullName()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
