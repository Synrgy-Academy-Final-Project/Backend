package com.example.finalProject.service.Report;

import com.example.finalProject.dto.ReportETicketDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ReportService {

    byte[] exportETicket2(String uname, UUID orderId, String reportFormat) throws UserNotFoundException, JRException, FileNotFoundException;


    ResponseDTO exportETicketLink(String uname, UUID orderId, String reportFormat, HttpServletResponse response) throws JRException, IOException, UserNotFoundException;

    String generateReportUserLink(List<ReportETicketDTO> report, String reportFormat, HttpServletResponse response) throws IOException, JRException, IOException;

    byte[] generateReportUser2(List<ReportETicketDTO> report, String reportFormat) throws FileNotFoundException, JRException;
}
