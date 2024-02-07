package com.example.finalProject.service.Report;

import com.example.finalProject.dto.ReportETicketDTO;
import com.example.finalProject.exception.UserNotFoundException;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

public interface ReportService {

    byte[] exportETicket(String uname, UUID transactionId, String reportFormat) throws UserNotFoundException, JRException, FileNotFoundException;

    byte[] generateReportUser(List<ReportETicketDTO> report, String reportFormat) throws FileNotFoundException, JRException;
}
