package com.landingis.api.controller;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.dto.employee.EmployeeDto;
import com.landingis.api.dto.orders.OrdersDto;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.employee.CreateEmployeeForm;
import com.landingis.api.form.orders.CreateOrdersForm;
import com.landingis.api.mapper.EmployeeMapper;
import com.landingis.api.mapper.OrdersDetailMapper;
import com.landingis.api.mapper.OrdersMapper;
import com.landingis.api.service.LandingIsApiService;
import com.landingis.api.storage.criteria.EmployeeCriteria;
import com.landingis.api.storage.criteria.OrdersCriteria;
import com.landingis.api.storage.model.*;
import com.landingis.api.storage.repository.*;
import com.landingis.api.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrdersController extends ABasicController{
    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrdersDetailRepository ordersDetailRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CollaboratorRepository collaboratorRepository;

    @Autowired
    CollaboratorProductRepository collaboratorProductRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    OrdersDetailMapper ordersDetailMapper;

    @Autowired
    LandingIsApiService landingIsApiService;

    private String generateCode() {
        String code = StringUtils.generateRandomString(8);
        code = code.replace("0", "A");
        code = code.replace("O", "Z");
        return code;
    }

    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<OrdersDto>> list(OrdersCriteria ordersCriteria, Pageable pageable){
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED,"Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<OrdersDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Page<Orders> listOrders = ordersRepository.findAll(ordersCriteria.getSpecification(), pageable);
        ResponseListObj<OrdersDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(ordersMapper.fromEntityListToOrdersDtoList(listOrders.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listOrders.getTotalPages());
        responseListObj.setTotalElements(listOrders.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<OrdersDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<OrdersDto> result = new ApiMessageDto<>();

        Orders orders = ordersRepository.findById(id).orElse(null);
        if(orders == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Not found orders.");
        }
        result.setData(ordersMapper.fromEntityToOrdersDto(orders));
        result.setMessage("Get orders success");
        return result;
    }


    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> create(@Valid @RequestBody CreateOrdersForm createOrdersForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Orders orders = ordersMapper.fromCreateOrdersFormToEntity(createOrdersForm);
        boolean isCreatedByCollaborator = false; // Kiểm tra xem người tạo đơn có phải CTV ko
        Customer customerCheck = customerRepository.findById(createOrdersForm.getCustomerId()).orElse(null);
        if (customerCheck == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "customer is not existed");
        }
        Integer checkSaleOff = createOrdersForm.getSaleOff();
        if(!(checkSaleOff >= LandingISConstant.MIN_OF_PERCENT) || !(checkSaleOff <= LandingISConstant.MAX_OF_PERCENT)){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "saleOff is not accepted");
        }
        if(createOrdersForm.getEmployeeId() == null && createOrdersForm.getCollaboratorId() == null){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "employee and collaborator can not be null");
        }
        if(createOrdersForm.getCollaboratorId() != null){
            if(createOrdersForm.getEmployeeId() != null){
                throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "employee should be null");
            }
            Collaborator collaboratorCheck = collaboratorRepository.findById(createOrdersForm.getCollaboratorId()).orElse(null);
            if(collaboratorCheck == null){
                throw new RequestException(ErrorCode.COLLABORATOR_ERROR_NOT_FOUND, "collaborator is not existed");
            }
            isCreatedByCollaborator = true;
            orders.setEmployee(collaboratorCheck.getEmployee());
        }
        if(createOrdersForm.getCollaboratorId() == null){
            Employee employeeCheck = employeeRepository.findById(createOrdersForm.getEmployeeId()).orElse(null);
            if (employeeCheck == null) {
                throw new RequestException(ErrorCode.EMPLOYEE_ERROR_NOT_FOUND, "employee is not existed");
            }
        }
        orders.setCode(generateCode());
        Orders savedOrder = ordersRepository.save(orders);
        /*-----------------------Xử lý orders detail------------------ */
        Double amountPrice = 0.0;  //Tổng tiền hóa đơn
        List<OrdersDetail> ordersDetailList = ordersDetailMapper
                .fromCreateOrdersDetailFormListToOrdersDetailList(createOrdersForm.getCreateOrdersDetailFormList());
        int checkIndex = 0;
        for (OrdersDetail ordersDetail : ordersDetailList){
            Product productCheck = productRepository.findById(ordersDetail.getProduct().getId()).orElse(null);
            if (productCheck == null){
                throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "product in index "+checkIndex+"is not existed");
            }
            Double productPrice = productCheck.getPrice();
            ordersDetail.setPrice(productPrice);
            amountPrice = amountPrice + productPrice * (ordersDetail.getAmount()); // Tổng tiền hóa đơn
            if(isCreatedByCollaborator){
                CollaboratorProduct collaboratorProductCheck = collaboratorProductRepository
                        .findByCollaboratorIdAndProductId(createOrdersForm.getCollaboratorId(),ordersDetail.getProduct().getId());
                if(collaboratorProductCheck == null){
                    throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_NOT_FOUND, "collaborator-product in index "+checkIndex+"is not existed");
                }
                ordersDetail.setKind(collaboratorProductCheck.getKind());
                ordersDetail.setValue(collaboratorProductCheck.getValue());
                if(collaboratorProductCheck.getKind() == LandingISConstant.COLLABORATOR_KIND_PERCENT){
                    Double collaboratorCommission = (productPrice * collaboratorProductCheck.getValue()/100) * ordersDetail.getAmount();
                }
                if (collaboratorProductCheck.getKind() == LandingISConstant.COLLABORATOR_KIND_DOLLAR){
                    Double collaboratorCommission = collaboratorProductCheck.getValue() * ordersDetail.getAmount();
                }
            }
            ordersDetail.setOrders(savedOrder);
        }
        ordersDetailRepository.saveAll(ordersDetailList);

        /*-----------------------Quay lại xử lý orders------------------ */
        orders.setTotalMoney(amountPrice);
        orders.setVat(LandingISConstant.ORDER_VAT);
        ordersRepository.save(orders);
        apiMessageDto.setMessage("Create orders success");
        return apiMessageDto;
    }
}
