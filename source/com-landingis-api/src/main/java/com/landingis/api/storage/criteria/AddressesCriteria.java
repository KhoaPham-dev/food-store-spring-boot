package com.landingis.api.storage.criteria;

import com.landingis.api.storage.model.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class AddressesCriteria {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private Province district;
    private Province commune;
    private Province province;

    public Specification<Addresses> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Addresses> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                if(!StringUtils.isEmpty(getAddress())) {
                    predicates.add(cb.like(cb.lower(root.get("address")), "%" + getAddress().toLowerCase() + "%"));
                }

                if(!StringUtils.isEmpty(getName())) {
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
                }
                if(!StringUtils.isEmpty(getPhone())) {
                    predicates.add(cb.like(cb.lower(root.get("phone")), "%" + getPhone().toLowerCase() + "%"));
                }
                if(getDistrict() != null) {
                    Join<Province, Addresses> joinProvince = root.join("province", JoinType.INNER);
                    predicates.add(cb.equal(joinProvince.get("kind"),2));
                }
                if(getCommune() != null) {
                    Join<Province, Addresses> joinProvince = root.join("province", JoinType.INNER);
                    predicates.add(cb.equal(joinProvince.get("kind"),1));
                }
                if(getProvince() != null) {
                    Join<Province, Addresses> joinProvince = root.join("province", JoinType.INNER);
                    predicates.add(cb.equal(joinProvince.get("kind"),3));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
