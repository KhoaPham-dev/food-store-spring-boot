package com.landingis.api.controller;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.dto.orders.OrdersDetailDto;
import com.landingis.api.dto.orders.OrdersDto;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.orders.CreateOrdersForm;
import com.landingis.api.form.orders.DeleteOrdersDetailForm;
import com.landingis.api.form.orders.UpdateOrdersForm;
import com.landingis.api.form.orders.UpdateStateOrdersForm;
import com.landingis.api.mapper.OrdersDetailMapper;
import com.landingis.api.mapper.OrdersMapper;
import com.landingis.api.service.LandingIsApiService;
import com.landingis.api.storage.criteria.OrdersCriteria;
import com.landingis.api.storage.model.*;
import com.landingis.api.storage.repository.*;
import com.landingis.api.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
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
    AccountRepository accountRepository;

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
        List<OrdersDetailDto> ordersDetailDtoList = ordersDetailMapper
                .fromEntityListToOrdersDetailDtoList(ordersDetailRepository.findAllByOrderId(id));
        OrdersDto ordersDto = ordersMapper.fromEntityToOrdersDto(orders);
        ordersDto.setOrdersDetailDtoList(ordersDetailDtoList);
        result.setData(ordersDto);
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
        List<OrdersDetail> ordersDetailList = ordersDetailMapper
                .fromCreateOrdersDetailFormListToOrdersDetailList(createOrdersForm.getCreateOrdersDetailFormList());
        Orders orders = ordersMapper.fromCreateOrdersFormToEntity(createOrdersForm);
        setCustomerCreateForm(orders,createOrdersForm);
        Integer checkSaleOff = createOrdersForm.getSaleOff();
        if((checkSaleOff < LandingISConstant.MIN_OF_PERCENT) || (checkSaleOff > LandingISConstant.MAX_OF_PERCENT)){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "saleOff is not accepted");
        }
        Long checkAccount = getCurrentUserId();
        Employee checkEmployee = employeeRepository.findById(checkAccount).orElse(null);
        Collaborator checkCollaborator= collaboratorRepository.findById(checkAccount).orElse(null);
        validateCollaboratorAndEmployee(checkCollaborator,checkEmployee, ordersDetailList,orders);
        orders.setCode(generateCode());
        Orders savedOrder = ordersRepository.save(orders);
        /*-----------------------Xử lý orders detail------------------ */
        amountPriceCal(orders,ordersDetailList,checkCollaborator,savedOrder);  //Tổng tiền hóa đơn
        ordersDetailRepository.saveAll(ordersDetailList);
        /*-----------------------Quay lại xử lý orders------------------ */

        ordersRepository.save(orders);
        apiMessageDto.setMessage("Create orders success");
        return apiMessageDto;
    }
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateOrdersForm updateOrdersForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        // Lúc này orders sẽ là orders đang trong database (orders cũ)
        Orders orders = ordersRepository.findById(updateOrdersForm.getId()).orElse(null);
        if(orders == null){
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Orders Not Found");
        }
        if(!orders.getState().equals(LandingISConstant.ORDERS_STATE_CREATED)){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Can not update info in this state");
        }
        setCustomerUpdateForm(updateOrdersForm, orders);
        checkSizeProducts(updateOrdersForm);
        //Map update form vô orders --> orders lúc này là orders mới với thông tin mới
        ordersMapper.fromUpdateOrdersFormToEntity(updateOrdersForm,orders);
        List<OrdersDetail> ordersDetailDeleteList = setDeleteList(updateOrdersForm);
        List<OrdersDetail> ordersDetailUpdateList = ordersDetailMapper
                .fromUpdateOrdersDetailFormListToOrdersDetailList(updateOrdersForm.getUpdateOrdersDetailFormList());
        if(ordersDetailUpdateList != null && !ordersDetailDeleteList.isEmpty()){
            checkSameProduct(ordersDetailUpdateList,ordersDetailDeleteList);
        }
        if(!ordersDetailDeleteList.isEmpty()){
            ordersDetailRepository.deleteAll(ordersDetailDeleteList);
        }
        if(ordersDetailUpdateList != null){
            checkProducts(orders.getId(),ordersDetailUpdateList);
            amountPriceCal(orders,ordersDetailUpdateList,orders.getCollaborator(),orders);
            ordersDetailRepository.saveAll(ordersDetailUpdateList);
        }
        ordersRepository.save(orders);
        apiMessageDto.setMessage("Update orders success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update-state", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateStateOrdersForm updateStateOrdersForm, BindingResult bindingResult) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Orders orders = ordersRepository.findById(updateStateOrdersForm.getId()).orElse(null);
        if(orders == null){
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Orders Not Found");
        }
        checkNewState(updateStateOrdersForm,orders);
        Integer prevState = orders.getState();
        orders.setState(updateStateOrdersForm.getState());
        orders.setPrevState(prevState);
        ordersRepository.save(orders);
        apiMessageDto.setMessage("Update orders state success");
        return apiMessageDto;
    }

    @PutMapping(value = "/cancel-orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> cancelOrders(@PathVariable("id") Long id) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to cancel orders.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Orders orders = ordersRepository.findById(id).orElse(null);
        if(orders == null){
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Order Not Found");
        }
        checkState(orders);
        Integer prevState = orders.getState();
        orders.setState(LandingISConstant.ORDERS_STATE_CANCELED);
        orders.setPrevState(prevState);
        ordersRepository.save(orders);
        apiMessageDto.setMessage("Cancel order success");
        return apiMessageDto;
    }

    private void setCustomerCreateForm(Orders orders, CreateOrdersForm createOrdersForm) {
        Customer customerCheck = customerRepository.findByPhone(createOrdersForm.getReceiverPhone());
        if (customerCheck == null) {
            Account savedAccount = new Account();
            savedAccount.setPhone(createOrdersForm.getReceiverPhone());
            savedAccount.setFullName(createOrdersForm.getReceiverName());
            savedAccount.setKind(LandingISConstant.USER_KIND_CUSTOMER);

            Customer savedCustomer = new Customer();
            savedCustomer.setAccount(savedAccount);
            savedCustomer.setAddress(createOrdersForm.getAddress());
            savedCustomer = customerRepository.save(savedCustomer);
            orders.setCustomer(savedCustomer);
        }
        else{
            orders.setCustomer(customerCheck);
        }
    }

    private void validateCollaboratorAndEmployee(Collaborator checkCollaborator, Employee checkEmployee , List<OrdersDetail> ordersDetailList, Orders orders) {
        int checkIndex = 0;
        if(checkCollaborator != null){
            orders.setEmployee(checkCollaborator.getEmployee());
            orders.setCollaborator(checkCollaborator);
            for (OrdersDetail ordersDetail : ordersDetailList){
                CollaboratorProduct collaboratorProductCheck = collaboratorProductRepository
                        .findByCollaboratorIdAndProductId(checkCollaborator.getId(),ordersDetail.getProduct().getId());
                if(collaboratorProductCheck == null){
                    throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_NOT_FOUND, "collaborator-product in index "+checkIndex+"is not existed");
                }
                checkIndex++;
            }
        }
        if(checkEmployee != null){
            orders.setEmployee(checkEmployee);
        }
    }


    private Double amountComission(OrdersDetail ordersDetail, Double productPrice, Collaborator checkCollaborator) {
        CollaboratorProduct collaboratorProductCheck = collaboratorProductRepository
                .findByCollaboratorIdAndProductId(checkCollaborator.getId(),ordersDetail.getProduct().getId());
        ordersDetail.setKind(collaboratorProductCheck.getKind());
        ordersDetail.setValue(collaboratorProductCheck.getValue());
        if(collaboratorProductCheck.getKind().equals(LandingISConstant.COLLABORATOR_KIND_PERCENT)){
            double percentComission = collaboratorProductCheck.getValue()/100;    // phần trăm nhận được
            double comssionPerProduct = productPrice * percentComission;    // tiền nhận được ở mỗi sản phẩm
            return  (comssionPerProduct * ordersDetail.getAmount());        // tổng tiền nhận được bằng tiền nhận mỗi sản phẩm nhân số lượng
        }
        else if (collaboratorProductCheck.getKind().equals(LandingISConstant.COLLABORATOR_KIND_DOLLAR)){
            return collaboratorProductCheck.getValue() * ordersDetail.getAmount();
        }
        return null;
    }



    private void amountPriceCal(Orders orders,List<OrdersDetail> ordersDetailList,Collaborator checkCollaborator, Orders savedOrder) {
        int checkIndex = 0;
        Double amountPrice = 0.0;
        for (OrdersDetail ordersDetail : ordersDetailList){
            Double collaboratorCommission = null;
            Product productCheck = productRepository.findById(ordersDetail.getProduct().getId()).orElse(null);
            if (productCheck == null){
                throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "product in index "+checkIndex+"is not existed");
            }
            Double productPrice = productCheck.getPrice();
            Double priceProductAfterSale = productPrice - (productPrice * productCheck.getSaleoff() / 100);
            ordersDetail.setPrice(priceProductAfterSale);
            amountPrice = amountPrice + priceProductAfterSale * (ordersDetail.getAmount()); // Tổng tiền hóa đơn
            if(checkCollaborator != null){
                collaboratorCommission = amountComission(ordersDetail,productPrice,checkCollaborator);
            }
            ordersDetail.setCollaboratorCommission(collaboratorCommission);
            ordersDetail.setOrders(savedOrder);
            checkIndex++;
        }
        Double totalMoney = totalMoneyHaveToPay(amountPrice,orders);
        orders.setTotalMoney(totalMoney);
        orders.setVat(LandingISConstant.ORDER_VAT);
    }

    private Double totalMoneyHaveToPay(Double amountPrice,Orders orders) {
        double saleOff = (double)orders.getSaleOff() / 100;             // Tính saleOff (%)
        Double amountAfterSaleOff =  amountPrice - amountPrice * saleOff;                  // Tổng tiền sau khi trừ saleOff
        double VAT = (double)LandingISConstant.ORDER_VAT / 100;             // Tính VAT (%)
        amountPrice = amountAfterSaleOff + amountAfterSaleOff * VAT ;              // Tiền sau cùng bằng tiền sau khi saleOff cộng với VAT (10% tổng tiền)
        return Math.round(amountPrice * 100.0) / 100.0;          // Làm tròn đến thập phân thứ 2
    }



    private List<OrdersDetail> setDeleteList(UpdateOrdersForm updateOrdersForm) {
        if(updateOrdersForm.getDeleteOrdersDetailFormList() == null){
            return Collections.emptyList();
        }
        int checkIndex = 0;
        List<OrdersDetail> ordersDetailDeleteList = new ArrayList<>();
        for (DeleteOrdersDetailForm deleteOrdersDetailForm : updateOrdersForm.getDeleteOrdersDetailFormList()){
            OrdersDetail ordersDetail = ordersDetailRepository.findById(deleteOrdersDetailForm.getOrderDetailId()).orElse(null);
            if(ordersDetail == null){
                throw new RequestException(ErrorCode.ORDERS_DETAIL_ERROR_NOT_FOUND, "Not found orders detail in index " + checkIndex);
            }
            ordersDetailDeleteList.add(ordersDetail);
            checkIndex ++;
        }
        return ordersDetailDeleteList;
    }


    private void checkSameProduct(List<OrdersDetail> ordersDetailUpdateList, List<OrdersDetail> ordersDetailDeleteList) {
        int checkIndex = 0;
        for(OrdersDetail ordersDetail : ordersDetailDeleteList){
            if (ordersDetailUpdateList.contains(ordersDetail)){
                throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Product in index "+ checkIndex +"can not be same");
            }
            checkIndex ++;
        }
    }

    private void checkProducts(Long ordersId ,List<OrdersDetail> ordersDetailList) {
        int checkIndex = 0;
        for (OrdersDetail ordersDetail : ordersDetailList){
            OrdersDetail ordersDetailCheck = ordersDetailRepository.findByProductIdAndOrdersId(ordersDetail.getProduct().getId(),ordersId);
            if(ordersDetailCheck == null){
                throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Product in index "+ checkIndex +"not have in orders");
            }
            ordersDetailCheck.setAmount(ordersDetail.getAmount());
            ordersDetailList.set(checkIndex,ordersDetailCheck);
            checkIndex++;
        }
    }

    private void checkSizeProducts(UpdateOrdersForm updateOrdersForm) {
        int sizeOfUpdate = 0;
        int sizeOfDelete = 0;
        if(updateOrdersForm.getUpdateOrdersDetailFormList() != null){
           sizeOfUpdate  = updateOrdersForm.getUpdateOrdersDetailFormList().size();
        }
        if(updateOrdersForm.getDeleteOrdersDetailFormList() != null){
            sizeOfDelete  = updateOrdersForm.getDeleteOrdersDetailFormList().size();
        }
        int sizeOfOrderDetailProduct = ordersDetailRepository.countByOrdersId(updateOrdersForm.getId());
        if(sizeOfOrderDetailProduct != (sizeOfDelete + sizeOfUpdate)){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Size of product not equal old orders");
        }
    }

    private void checkNewState(UpdateStateOrdersForm updateStateOrdersForm,Orders orders) {
        // state mới phải lớn hơn state cũ
        if((updateStateOrdersForm.getState() <= orders.getState())){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Update orders state must mor than or equal old state");
        }
        // State 3 4 không thể update
        Integer state = orders.getState();
        if(state.equals(LandingISConstant.ORDERS_STATE_DONE) || state.equals(LandingISConstant.ORDERS_STATE_CANCELED)){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Can not update orders in state 3 or 4");
        }
    }

    private void setCustomerUpdateForm(UpdateOrdersForm updateOrdersForm, Orders orders) {
        Customer customerCheck = customerRepository.findByPhone(updateOrdersForm.getReceiverPhone());
        if (customerCheck == null) {
            {
                Account savedAccount = new Account();
                savedAccount.setPhone(updateOrdersForm.getReceiverPhone());
                savedAccount.setFullName(updateOrdersForm.getReceiverName());
                savedAccount.setKind(LandingISConstant.USER_KIND_CUSTOMER);

                Customer savedCustomer = new Customer();
                savedCustomer.setAccount(savedAccount);
                savedCustomer.setAddress(updateOrdersForm.getAddress());
                savedCustomer = customerRepository.save(savedCustomer);
                orders.setCustomer(savedCustomer);
            }
        }
        else{
            orders.setCustomer(customerCheck);
        }
    }
    private String generateCode() {
        String code = StringUtils.generateRandomString(8);
        code = code.replace("0", "A");
        code = code.replace("O", "Z");
        return code;
    }

    private void checkState(Orders orders) {
        Integer state = orders.getState();
        if(state.equals(LandingISConstant.ORDERS_STATE_DONE) || state.equals(LandingISConstant.ORDERS_STATE_CANCELED)){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Can not cancel order in this state");
        }
    }
}
