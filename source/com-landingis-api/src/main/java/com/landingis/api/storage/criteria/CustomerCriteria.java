package com.landingis.api.storage.criteria;

import com.landingis.api.storage.model.Customer;
import com.landingis.api.storage.model.Group;
import lombok.Data;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CustomerCriteria {
    private Long id;
    private Long accountId;
    private String address;
    private Date birthDay;
    private Integer sex;
    private String note;
    private Integer status;

    public Specification<Customer> getSpecification() {
        return new Specification<Customer>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(getAccountId() != null){
                    predicates.add(cb.equal(root.get("accountId"), getAccountId()));
                }
                if(!StringUtils.isEmpty(getAddress())){
                    predicates.add(cb.like(cb.lower(root.get("name")), "%"+getAddress().toLowerCase()+"%"));
                }
                if (getBirthDay() != null){
                    predicates.add(cb.equal(root.get("birthDay"),getBirthDay()));
                }
                if (getSex() != null){
                    predicates.add(cb.equal(root.get("sex"),getSex()));
                }
                if(!StringUtils.isEmpty(getNote())){
                    predicates.add(cb.like(cb.lower(root.get("note")), "%"+getNote().toLowerCase()+"%"));
                }
                if(getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
