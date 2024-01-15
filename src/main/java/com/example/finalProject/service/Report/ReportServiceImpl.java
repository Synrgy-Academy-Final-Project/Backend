package com.example.finalProject.service.Report;

import com.example.finalProject.dto.ETicketDTO;
import com.example.finalProject.exception.UserNotFoundException;
import com.example.finalProject.model.user.User;
import com.example.finalProject.repository.TransactionRepository;
import com.example.finalProject.service.user.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService{
    private final AuthenticationServiceImpl authenticationService;
    private final TransactionRepository transactionRepository;
    @Override
    public byte[] exportETicket(String uname, String reportFormat) throws UserNotFoundException, JRException, FileNotFoundException {
        User idUser = authenticationService.getIdUser(uname);
        List<ETicketDTO> allUserTransaction = transactionRepository.getAllUserTransaction(idUser.getId());

        return generateReportUser(allUserTransaction, reportFormat);
    }

    @Override
    public byte[] generateReportUser(List<ETicketDTO> report, String reportFormat) throws FileNotFoundException, JRException {
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

}
