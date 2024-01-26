package com.example.finalProject.service.scheduleFlight;

import com.example.finalProject.dto.ScheduleFlightDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.utils.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleFlightServiceImpl implements ScheduleFlightService{
    private final Response response;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public ResponseDTO getScheduleFlight(String departureCode, String arrivalCode, Date departureDate, String airplaneClass, Pageable pageable) {
        try
        {
            String sql = "select c.\"name\" as \"companyName\", c.url as \"urlLogo\", " +
                    "a.\"name\" as \"airplaneName\", a.code as \"airplaneCode\", ac.airplane_class as \"airplaneClass\", ac.capacity as \"capacity\", atf.flight_time, " +
                    "upper(?) as \"departureCode\", upper(?) as \"arrivalCode\", " +
                    "concat(to_date(?, 'dd-mm-yyyy'),' ',atf.flight_time)::timestamp with time zone AT TIME ZONE 'Asia/Jakarta' as \"departureTime\", " +
                    "(select concat(to_date(?, 'dd-mm-yyyy'),' ',atf.flight_time)::timestamp with time zone AT TIME ZONE 'Asia/Jakarta' + (ba.duration || ' minutes')::interval from baseprice_airports ba " +
                    "where upper(ba.departure_code) = upper(?) and upper(ba.arrival_code) = upper(?) and ba.deleted_date is null limit 1) as \"arrivalTime\", " +
                    "(a.airplane_price + " +
                    "coalesce ((select bd.date_price from baseprice_dates bd where to_date(?, 'dd-mm-yyyy') = to_date(to_char(bd.date_time, 'dd-mm-yyyy'), 'dd-mm-yyyy') and deleted_date is null limit 1), 0)   + " +
                    "(select airport_price from baseprice_airports ba where upper(departure_code) = upper(?) and upper(arrival_code) = upper(?) and deleted_date is null limit 1) + " +
                    "ac.airplane_class_price + " +
                    "atf.airplane_flight_time_price) as totalPrice " +
                    "from airplanes a " +
                    "join companies c on a.company_id = c.id " +
                    "join airplane_classes ac on a.id = ac.airplane_id " +
                    "join airplane_flight_times atf on a.id = atf.airplane_id " +
                    "where initcap(ac.airplane_class) = initcap(?) " +
                    "and a.deleted_date is null " +
                    "and c.deleted_date is null " +
                    "and ac.deleted_date is null " +
                    "and atf.deleted_date is null";

            String pattern = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String departureDateStr = simpleDateFormat.format(departureDate);

            String countQuery = "select count(*) from (" + sql + ") as subquery";

            List<ScheduleFlightDTO> resultList = jdbcTemplate.query(sql + " LIMIT ? OFFSET ?",
                    new Object[]{departureCode, arrivalCode, departureDateStr, departureDateStr, departureCode, arrivalCode, departureDateStr, departureCode, arrivalCode, airplaneClass, pageable.getPageSize(), pageable.getOffset()},
                    new BeanPropertyRowMapper<>(ScheduleFlightDTO.class));



            Long totalCount = jdbcTemplate.queryForObject(countQuery, Long.class, departureCode, arrivalCode, departureDateStr, departureDateStr, departureCode, arrivalCode, departureDateStr, departureCode, arrivalCode, airplaneClass);
            if (resultList.isEmpty()){
                return response.dataNotFound("Schedule Flight");
            }
            if (resultList.get(0).getArrivalTime() == null || resultList.get(0).getTotalPrice() == null){
                resultList.clear();
                totalCount = 0L;
                PageImpl<ScheduleFlightDTO> flightDTOS = new PageImpl<>(resultList, pageable, totalCount);
                return response.suksesDTO(flightDTOS);
            }
//            String originalDateString = String.valueOf(resultList.get(0).getDepartureTime());
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//            Date originalDate = dateFormat.parse(originalDateString);
//            // Convert to Instant and then to ZonedDateTime in the target time zone (Asia/Jakarta)
//            Instant instant = originalDate.toInstant();
//            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Jakarta"));
//
//            // Convert back to Date if necessary
//            Date indonesiaDate = Date.from(zonedDateTime.toInstant());
//            System.out.println(indonesiaDate);

//            resultList.get().set
            PageImpl<ScheduleFlightDTO> flightDTOS = new PageImpl<>(resultList, pageable, totalCount);
            return response.suksesDTO(flightDTOS);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }
}
