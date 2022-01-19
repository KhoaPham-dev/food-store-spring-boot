package com.landingis.api.dto.orders;

import com.landingis.api.dto.ABasicAdminDto;
import com.landingis.api.dto.collaborator.CollaboratorDto;
import com.landingis.api.dto.customer.CustomerDto;
import com.landingis.api.dto.employee.EmployeeDto;
import com.landingis.api.storage.model.Collaborator;
import com.landingis.api.storage.model.Employee;
import lombok.Data;

@Data
public class OrdersDto extends ABasicAdminDto {
    private Long id;
    private CustomerDto customerDto;
    private Integer saleOff;
    private Double totalMoney;
    private Integer vat;
    private Integer state;
    private Integer prevState;
    private String address;
    private String receiverName;
    private String receiverPhone;
    private EmployeeDto employeeDto;
    private CollaboratorDto collaboratorDto;
    private String code;
    private Integer paymentMethod;
    private OrdersDetailDto ordersDetailDto;
}
