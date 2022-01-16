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
    private String fullName;
    private String phone;
    private String address;
    private Province district;
    private Province commune;
    private Province province;

    public Specification<Province> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Province> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                if(!StringUtils.isEmpty(getAddress())) {
                    predicates.add(cb.like(cb.lower(root.get("address")), "%" + getAddress().toLowerCase() + "%"));
                }

                if(getPhone() != null) {
                    Join<Account, Addresses> joinAccount = root.join("account", JoinType.INNER);
                    predicates.add(cb.like(cb.lower(joinAccount.get("phone")), "%" + getPhone().toLowerCase() + "%"));
                }
                if(getFullName() != null) {
                    Join<Account, Addresses> joinAccount = root.join("account", JoinType.INNER);
                    predicates.add(cb.like(cb.lower(joinAccount.get("fullName")), "%" + getFullName().toLowerCase() + "%"));
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
