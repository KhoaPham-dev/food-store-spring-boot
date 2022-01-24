package com.landingis.api.storage.projection;

import lombok.Data;

import java.util.Date;

public interface OrdersCollaborator {
    Long getId();
    Double getTotalMoney();
    Integer getState();
    String getCode();
    Date getCreatedDate();
    Double getTotalCollaboratorCommission();
    String getCustomerFullName();
}
