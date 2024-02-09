package com.example.finalProject.service.Report;

import com.example.finalProject.dto.ReportETicketDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.exception.UserNotFoundException;
import com.example.finalProject.model.user.User;
import com.example.finalProject.repository.TransactionRepository;
import com.example.finalProject.service.user.AuthenticationServiceImpl;
import com.example.finalProject.utils.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.sql.Time;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReportServiceImpl implements ReportService{
    private final AuthenticationServiceImpl authenticationService;
    private final TransactionRepository transactionRepository;
    private final Response responses;
//    @Override
//    public byte[] exportETicket(String uname, UUID orderId, String reportFormat) throws UserNotFoundException, JRException, FileNotFoundException {
//        User idUser = authenticationService.getIdUser(uname);
//
////        UUID transactionId = UUID.fromString("ddfe442e-6e8d-41b6-9c35-576f196b63e2");
////        UUID userId = UUID.fromString("e91086da-8c82-4053-9c37-1ed4de568214");
//
//        List<Object[]> tes = transactionRepository.getDataTransactionUser(orderId, idUser.getId());
//        List<ReportETicketDTO> tesDTOS = tes.stream().map(array -> new ReportETicketDTO(
//                (String) array[0],
//                (String) array[1],
//                (String) array[2],
//                (String) array[3],
//                (String) array[4],
//                (Time) array[5],
//                (Date) array[6],
//                (Time) array[7],
//                (Date) array[8],
//                (String) array[9],
//                (String) array[10],
//                (String) array[11],
//                (String) array[12],
//                (String) array[13],
//                (String) array[14],
//                (String) array[15],
//                (String) array[16],
//                (String) array[17],
//                (String) array[18]
//        )).toList();
//
//        System.out.println(tesDTOS);
//
//        return generateReportUser(tesDTOS, reportFormat);
//    }

    @Override
    public byte[] exportETicket2(String uname, UUID orderId, String reportFormat) throws UserNotFoundException, JRException, FileNotFoundException {
        User idUser = authenticationService.getIdUser(uname);

//        UUID transactionId = UUID.fromString("ddfe442e-6e8d-41b6-9c35-576f196b63e2");
//        UUID userId = UUID.fromString("e91086da-8c82-4053-9c37-1ed4de568214");

        List<Object[]> tes = transactionRepository.getDataTransactionUser(orderId, idUser.getId());
        System.out.println(tes);
        List<ReportETicketDTO> tesDTOS = tes.stream().map(array -> new ReportETicketDTO(
                (String) array[0],
                (String) array[1],
                (String) array[2],
                (String) array[3],
                (String) array[4],
                (Time) array[5],
                (Date) array[6],
                (Time) array[7],
                (Date) array[8],
                (String) array[9],
                (String) array[10],
                (String) array[11],
                (String) array[12],
                (String) array[13],
                (String) array[14],
                (String) array[15],
                (Integer) array[16],
                (Integer) array[17],
                (String) array[18],
                (Integer) array[19],
                (Integer) array[20],
                (Integer) array[21],
                (Integer) array[22],
                (Integer) array[23],
                (Long) array[24],
                (Integer) array[25],
                (Long) array[26],
                (Long) array[27],
                (Integer) array[28],
                (Long) array[29],
                (Long) array[30],
                (Integer) array[31],
                (Long) array[32],
                (Long) array[33],
                (Integer) array[34],
                (Long) array[35],
                (String) array[36],
                (Integer) array[37],
                (String) array[38],
                (Integer) array[39]

        )).toList();

        System.out.println(tesDTOS);

        return generateReportUser2(tesDTOS, reportFormat);
    }

    @Override
    public ResponseDTO exportETicketLink(String uname, UUID orderId, String reportFormat, HttpServletResponse response) throws JRException, IOException, UserNotFoundException {
        User idUser = authenticationService.getIdUser(uname);

//        UUID transactionId = UUID.fromString("ddfe442e-6e8d-41b6-9c35-576f196b63e2");
//        UUID userId = UUID.fromString("e91086da-8c82-4053-9c37-1ed4de568214");

        List<Object[]> tes = transactionRepository.getDataTransactionUser(orderId, idUser.getId());
        System.out.println(tes);
        List<ReportETicketDTO> tesDTOS = tes.stream().map(array -> new ReportETicketDTO(
                (String) array[0],
                (String) array[1],
                (String) array[2],
                (String) array[3],
                (String) array[4],
                (Time) array[5],
                (Date) array[6],
                (Time) array[7],
                (Date) array[8],
                (String) array[9],
                (String) array[10],
                (String) array[11],
                (String) array[12],
                (String) array[13],
                (String) array[14],
                (String) array[15],
                (Integer) array[16],
                (Integer) array[17],
                (String) array[18],
                (Integer) array[19],
                (Integer) array[20],
                (Integer) array[21],
                (Integer) array[22],
                (Integer) array[23],
                (Long) array[24],
                (Integer) array[25],
                (Long) array[26],
                (Long) array[27],
                (Integer) array[28],
                (Long) array[29],
                (Long) array[30],
                (Integer) array[31],
                (Long) array[32],
                (Long) array[33],
                (Integer) array[34],
                (Long) array[35],
                (String) array[36],
                (Integer) array[37],
                (String) array[38],
                (Integer) array[39]

        )).toList();

        System.out.println(tesDTOS);
        String result = generateReportUserLink(tesDTOS, reportFormat, response);
        return responses.suksesDTO(result);
    }

    public InputStream loadJrxmlFile() throws IOException {
        Resource resource = new ClassPathResource("reports/Eticket.jrxml");
        return resource.getInputStream();
    }

    @Override
    public String generateReportUserLink(List<ReportETicketDTO> report, String reportFormat, HttpServletResponse response) throws IOException, JRException, IOException {
        JasperReport jasperReport;
//        File file = ResourceUtils.getFile("classpath:reports/Eticket.jrxml");
        loadJrxmlFile();
//        jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        jasperReport = JasperCompileManager.compileReport(loadJrxmlFile());
        JRSaver.saveObject(jasperReport, "Eticket.jasper"); // Compile to .jasper file instead of .jrxml
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(report);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("total", "Total Product: " + report.size());

        // Set the parameter to avoid an unnecessary warning about missing parameter
        parameters.put(JRParameter.REPORT_LOCALE, java.util.Locale.ENGLISH);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Export report to PDF format
        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        byte[] reportContent = baos.toByteArray();

        // Write the PDF content to the HttpServletResponse
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment; filename=Eticket.pdf");
        response.setContentLength(reportContent.length);
        response.getOutputStream().write(reportContent);
        response.getOutputStream().flush();
        response.getOutputStream().close();

        return "Report generated successfully";
    }
    @Override
    public byte[] generateReportUser2(List<ReportETicketDTO> report, String reportFormat) throws FileNotFoundException, JRException {
        JasperReport jasperReport;

        File file = ResourceUtils.getFile("classpath:reports/Eticket.jrxml");
        jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRSaver.saveObject(jasperReport, "Eticket.jrxml");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(report);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("total", "Total Product: "+report.size());
        JasperPrint jasperPrint = null;
        byte[] reportContent;

        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            switch (reportFormat) {
                case "pdf" -> reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
                case "xml" -> reportContent = JasperExportManager.exportReportToXml(jasperPrint).getBytes();
                default -> throw new RuntimeException("Unknown report format");
            }
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        return reportContent;
    }

    //    @Override
//    public byte[] generateReportUser(List<ReportETicketDTO> report, String reportFormat) throws FileNotFoundException, JRException {
//        JasperReport jasperReport;
//
//        File file = ResourceUtils.getFile("classpath:reports/Eticket.jrxml");
//        jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//        JRSaver.saveObject(jasperReport, "Eticket.jrxml");
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(report);
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("total", "Total Product: "+report.size());
//        JasperPrint jasperPrint = null;
//        byte[] reportContent;
//
//        try {
//            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//            switch (reportFormat) {
//                case "pdf" -> reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
//                case "xml" -> reportContent = JasperExportManager.exportReportToXml(jasperPrint).getBytes();
//                default -> throw new RuntimeException("Unknown report format");
//            }
//        } catch (JRException e) {
//            throw new RuntimeException(e);
//        }
//        return reportContent;
//    }

}
