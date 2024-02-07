package com.example.finalProject.service.Report;

import com.example.finalProject.dto.ReportETicketDTO;
import com.example.finalProject.exception.UserNotFoundException;
import com.example.finalProject.model.user.User;
import com.example.finalProject.repository.TransactionRepository;
import com.example.finalProject.service.user.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService{
    private final AuthenticationServiceImpl authenticationService;
    private final TransactionRepository transactionRepository;
    @Override
    public byte[] exportETicket(String uname, UUID orderId, String reportFormat) throws UserNotFoundException, JRException, FileNotFoundException {
        User idUser = authenticationService.getIdUser(uname);

//        UUID transactionId = UUID.fromString("ddfe442e-6e8d-41b6-9c35-576f196b63e2");
//        UUID userId = UUID.fromString("e91086da-8c82-4053-9c37-1ed4de568214");

        List<Object[]> tes = transactionRepository.getDataTransactionUser(orderId, idUser.getId());
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
                (String) array[16],
                (String) array[17],
                (String) array[18]
        )).toList();

        System.out.println(tesDTOS);

        return generateReportUser(tesDTOS, reportFormat);
    }

    @Override
    public byte[] generateReportUser(List<ReportETicketDTO> report, String reportFormat) throws FileNotFoundException, JRException {
        JasperReport jasperReport;

//        try {
//            jasperReport = (JasperReport)
//                    JRLoader.loadObject(ResourceUtils.getFile("Eticket"));
//        } catch (FileNotFoundException | JRException e) {
//            try {
//                File file = ResourceUtils.getFile("classpath:reports/Eticket.jrxml");
//                jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//                JRSaver.saveObject(jasperReport, "Eticket.jrxml");
//            } catch (FileNotFoundException | JRException ex) {
//                throw new RuntimeException(e);
//            }
//        }
        File file = ResourceUtils.getFile("classpath:reports/Eticket2.jrxml");
        jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRSaver.saveObject(jasperReport, "Eticket2.jrxml");
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

}
