package com.excelsisproject.productservice.services;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.entities.Order;
import com.excelsisproject.productservice.entities.Product;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;


import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.sql.DataSource;

@Service
public class JReportService {


    public void exportProductJReport(HttpServletResponse response, List<Product> products) throws JRException, IOException {
        //Get file and compile it
        File file = ResourceUtils.getFile("classpath:product_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(products);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "sample text");
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        //Export report
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }

    public void exportOrderJReport(HttpServletResponse response, List<Order> orders) throws JRException, IOException {
        //Get file and compile it
        File file = ResourceUtils.getFile("classpath:order_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orders);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "sample text");
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        //Export report
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }

    public void exportInvoice(HttpServletResponse response, OrderDto orderDto) throws JRException, IOException, SQLException {

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ecommerce_web", "postgres", "postgresql");

        File file = ResourceUtils.getFile("classpath:factura.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderId", orderDto.getOrderId());
        parameters.put("firstName", orderDto.getFirstName());
        parameters.put("lastName", orderDto.getLastName());
        parameters.put("ruc", orderDto.getRuc());
        parameters.put("orderDate", orderDto.getDateOrdered());
        parameters.put("userPhoneNumber", orderDto.getUserPhoneNumber());
        parameters.put("userAddress", orderDto.getUserAddress());
        parameters.put("totalPrice", orderDto.getTotalPrice());
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        //Export report
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }
}