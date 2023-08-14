package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.dto.RequestDto;
import com.excelsisproject.productservice.entities.Order;
import com.excelsisproject.productservice.entities.Product;
import com.excelsisproject.productservice.mappers.OrderMapper;
import com.excelsisproject.productservice.mappers.ProductMapper;
import com.excelsisproject.productservice.repositories.OrderRepository;
import com.excelsisproject.productservice.repositories.ProductRepository;
import com.excelsisproject.productservice.services.FilterSpecification;
import com.excelsisproject.productservice.services.JReportService;
import com.excelsisproject.productservice.services.OrderService;
import com.excelsisproject.productservice.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/JReport")
public class JReportController {

    private ProductService productService;
    private OrderService orderService;
    private JReportService jReportService;

//    @GetMapping("/productPdf/export")
//    public void createProductReport(HttpServletResponse response) throws IOException, JRException {
//        response.setContentType("application/pdf");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
//        String currentDateTime = dateFormatter.format(new Date());
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
//        response.setHeader(headerKey, headerValue);
//
//        jReportService.exportProductJReport(response);
//    }

    @PostMapping("/productPdf/export")
    public void createProductReport(HttpServletResponse response, @RequestBody RequestDto requestDto) throws IOException, JRException {

        List<Product> products = productService.productFilter(requestDto).stream().map(ProductMapper::mapToProduct)
                .collect(Collectors.toList());

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        jReportService.exportProductJReport(response, products);
    }

    @PostMapping("/orderPdf/export")
    public void createOrderReport(HttpServletResponse response, @RequestBody RequestDto requestDto) throws IOException, JRException {

        List<Order> orders = orderService.orderFilter(requestDto).stream().map(OrderMapper::mapToOrder)
                .collect(Collectors.toList());

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        jReportService.exportOrderJReport(response, orders);

    }
}
