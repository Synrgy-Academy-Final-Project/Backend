//package com.example.finalProject;
//
//import com.example.finalProject.entity.Flight;
//import com.example.finalProject.repository.FlightRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//@Component
//@EnableScheduling
//public class ScheduledTasks {
//
//    @Autowired
//    FlightRepository flightRepository;
//
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//
//    @Scheduled(cron = "*/5 * * * * *")
//    public void reportCurrentTime() {
//        Flight flights = flightRepository.findById(UUID.fromString("f9ec99bb-a9d1-4824-b1b3-4a5512cb379b")).get();
//        System.out.println(flights.getDepartureDate());
//        System.out.println("The time is now : " + dateFormat.format(new Date()));
//    }
//}
