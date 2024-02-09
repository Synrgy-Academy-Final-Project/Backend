package com.example.finalProject.controller;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.exception.UserNotFoundException;
import com.example.finalProject.security.util.EmailUtil;
import com.example.finalProject.service.Report.ReportServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("report")
public class ReportController {
    private final ReportServiceImpl reportService;
    private final EmailUtil emailUtil;
    @PostMapping(
            path = "/eticket/{orderId}"
    )
    public ResponseEntity<ResponseDTO> exportETicket(Principal principal, @PathVariable UUID orderId) throws JRException, FileNotFoundException, UserNotFoundException, MessagingException {
        String uname = principal.getName();
        byte[] pdfBytes = reportService.exportETicket2(uname,orderId,"pdf");
        emailUtil.sendEticket(uname, pdfBytes);
        return new ResponseEntity<>(new ResponseDTO(200, "E-Ticket has been sent to email"), HttpStatus.OK);
    }

    @PostMapping(
            path = "/eticket-link/{orderId}"
    )
    public ResponseEntity<ResponseDTO> exportETicket2(Principal principal, @PathVariable UUID orderId, HttpServletResponse response) throws JRException, IOException, UserNotFoundException, MessagingException {
        java.lang.String uname = principal.getName();
        ResponseDTO pdf = reportService.exportETicketLink(uname, orderId, "pdf", response);
        return new ResponseEntity<>(pdf, HttpStatus.OK);
    }
}
