package com.example.finalProject.service.Report;

import com.example.finalProject.dto.ETicketDTO;
import com.example.finalProject.exception.UserNotFoundException;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;

public interface ReportService {
//    byte[] exportETicket(Principal principal, String reportFormat) throws UserNotFoundException, JRException, FileNotFoundException;

    byte[] exportETicket(String uname, String reportFormat) throws UserNotFoundException, JRException, FileNotFoundException;

    byte[] generateReportUser(List<ETicketDTO> report, String reportFormat) throws FileNotFoundException, JRException;
}
