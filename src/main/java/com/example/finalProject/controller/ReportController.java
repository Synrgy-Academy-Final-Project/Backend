package com.example.finalProject.controller;

import com.example.finalProject.exception.UserNotFoundException;
import com.example.finalProject.security.util.EmailUtil;
import com.example.finalProject.service.Report.ReportServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
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
            path = "/eticket"
    )
    public ResponseEntity<String> exportETicket(Principal principal) throws JRException, FileNotFoundException, UserNotFoundException, MessagingException {
        String uname = principal.getName();
        byte[] pdfBytes = reportService.exportETicket(uname,"pdf");
        emailUtil.sendEticket(uname, pdfBytes);
        return new ResponseEntity<>("E-Ticket has been sent to email", HttpStatus.OK);
    }
}
