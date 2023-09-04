package com.excelsisproject.productservice.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.entities.Order;
import com.excelsisproject.productservice.entities.Product;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;


import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class JReportService {

    private static final String URL = "jdbc:postgresql://localhost:5432/ecommerce_web";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgresql";

    @Autowired
    private EmailService emailService;


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

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

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
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        //Export report
        //JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());

        // send to mail
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        ByteArrayDataSource attachment = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
        emailService.sendInvoiceEmail(orderDto.getUserEmail(), attachment);
    }

    public void exportTicket(HttpServletResponse response, OrderDto orderDto) throws JRException, IOException, SQLException {

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

        File file = ResourceUtils.getFile("classpath:ticket.jrxml");
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
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        //Export report
        //JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());

        // send to mail
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        ByteArrayDataSource attachment = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
        emailService.sendTicketEmail(orderDto.getUserEmail(), attachment);
    }
}