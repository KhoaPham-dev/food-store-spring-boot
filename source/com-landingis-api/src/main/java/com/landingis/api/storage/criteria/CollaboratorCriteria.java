package com.landingis.api.storage.criteria;

import com.landingis.api.storage.model.Account;
import com.landingis.api.storage.model.Collaborator;
import com.landingis.api.storage.model.Employee;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
@Data
public class CollaboratorCriteria {
    private Long id;
    private Integer sex;
    private Integer status;
    private String phone;
    private String fullName;
    private Integer employeeId;

    public Specification<Collaborator> getSpecification() {
        return new Specification<Collaborator>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Collaborator> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
                if(getEmployeeId() != null) {
                    Join<Employee, Collaborator> joinEmployee = root.join("employee", JoinType.INNER);
                    predicates.add(cb.equal(joinEmployee.get("id")), getEmployeeId());
                }
                if(getPhone() != null) {
                    Join<Account, Collaborator> joinAccount = root.join("account", JoinType.INNER);
                    predicates.add(cb.like(cb.lower(joinAccount.get("phone")), "%" + getPhone().toLowerCase() + "%"));
                }
                if(getFullName() != null) {
                    Join<Account, Collaborator> joinAccount = root.join("account", JoinType.INNER);
                    predicates.add(cb.like(cb.lower(joinAccount.get("fullName")), "%" + getFullName().toLowerCase() + "%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
