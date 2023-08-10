package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.repositories.OrderRepository;
import com.excelsisproject.productservice.services.JReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@RestController
@RequestMapping("/api/JReport")
public class JReportController {

    private OrderRepository orderRepository;
    private JReportService jReportService;

    @GetMapping("/productPdf/export")
    public void createProductReport(HttpServletResponse response) throws IOException, JRException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        jReportService.exportProductJReport(response);
    }

}
