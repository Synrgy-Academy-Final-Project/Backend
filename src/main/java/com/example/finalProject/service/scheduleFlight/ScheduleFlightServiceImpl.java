package com.example.finalProject.service.scheduleFlight;

import com.example.finalProject.dto.*;
import com.example.finalProject.repository.TransactionRepository;
import com.example.finalProject.utils.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ScheduleFlightServiceImpl implements ScheduleFlightService{
    private final Response response;
    private final JdbcTemplate jdbcTemplate;
    private final TransactionRepository transactionRepository;

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
                    "atf.airplane_flight_time_price) as totalPrice, " +
                    "a.id as \"airplaneId\",\n" +
                    "ac.id as \"airplaneClassId\",\n" +
                    "atf.id as \"airplaneFlightTimeId\"," +
                    "as2.baggage as \"baggage\",\n" +
                    "as2.cabin_baggage as \"cabinBaggage\",\n" +
                    "as2.refund as \"refund\",\n" +
                    "as2.electric_socket as \"electricSocket\",\n" +
                    "as2.inflight_entertainment as \"inflightEntertainment\",\n" +
                    "as2.meals as \"meals\",\n" +
                    "as2.reschedule as \"reschedule\",\n" +
                    "as2.travel_insurance as \"travelInsurance\",\n" +
                    "as2.wifi as \"wifi\" " +
                    "from airplanes a " +
                    "join companies c on a.company_id = c.id " +
                    "join airplane_classes ac on a.id = ac.airplane_id " +
                    "join airplane_flight_times atf on a.id = atf.airplane_id " +
                    "join airplane_service as2 on ac.id = as2.airplane_class_id " +
                    "where initcap(ac.airplane_class) = initcap(?) " +
                    "and a.deleted_date is null " +
                    "and c.deleted_date is null " +
                    "and ac.deleted_date is null " +
                    "and atf.deleted_date is null " +
                    "and as2.deleted_date is null";

            String pattern = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String departureDateStr = simpleDateFormat.format(departureDate);

            String countQuery = "select count(*) from (" + sql + ") as subquery";

            List<ScheduleFlightDTO> resultList = jdbcTemplate.query(sql + " LIMIT ? OFFSET ?",
                    new Object[]{departureCode, arrivalCode, departureDateStr, departureDateStr, departureCode, arrivalCode, departureDateStr, departureCode, arrivalCode, airplaneClass, pageable.getPageSize(), pageable.getOffset()},
                    new BeanPropertyRowMapper<>(ScheduleFlightDTO.class));

            List<ScheduleFlightResponseDTO> scheduleFlightResponseDTOS = new ArrayList<>();

            for (int i=0; i<resultList.size(); i++){
                LocalDate date = resultList.get(i).getDepartureTime().toLocalDateTime().toLocalDate();
                System.out.println(date);
                System.out.println(resultList.get(i).getAirplaneId());
                System.out.println(resultList.get(i).getAirplaneClassId());
                System.out.println(resultList.get(i).getAirplaneFlightTimeId());
                List<Object[]> totalSeat = transactionRepository.getTotalSeatTransactionAirplane(resultList.get(i).getAirplaneId(), resultList.get(i).getAirplaneClassId(), resultList.get(i).getAirplaneFlightTimeId(), date);
//                UUID airplaneId = UUID.fromString("f54740ad-a260-404c-be33-0b0ab7f7072c");
//                UUID airplaneClassId = UUID.fromString("2d60c23f-28b0-4bf7-beec-c5459fb00939");
//                UUID airplaneTimeId = UUID.fromString("7ca5a5cc-bb95-4c46-bbeb-08ab54584426");
//                List<Object[]> totalSeat = transactionRepository.getTotalSeatTransactionAirplane2(airplaneId, airplaneClassId, airplaneTimeId, date);
                System.out.println(totalSeat);
                Integer seat = 0;
                if (!totalSeat.isEmpty()){
                    List<TotalSeatDTO> totalSeatData = totalSeat.stream().map(array -> new TotalSeatDTO(
                            (Long) array[0],
                            (Date) array[1],
                            (Time) array[2],
                            (Date) array[3],
                            (Time) array[4],
                            (UUID) array[5],
                            (UUID) array[6],
                            (UUID) array[7]
                    )).toList();
                    seat = Math.toIntExact(totalSeatData.get(i).getTotalSeatTransaction());
                }
                System.out.println(seat);
//                System.out.println(totalSeatData.get(i).getTotalSeatTransaction());
                scheduleFlightResponseDTOS.add(
                        new ScheduleFlightResponseDTO(resultList.get(i).getCompanyName(), resultList.get(i).getUrlLogo(), resultList.get(i).getAirplaneId(),
                                resultList.get(i).getAirplaneName(), resultList.get(i).getAirplaneCode(), resultList.get(i).getAirplaneClassId(),
                                resultList.get(i).getAirplaneClass(), (resultList.get(i).getCapacity() - seat), new AirplaneServiceDTO(
                                resultList.get(i).getBaggage(), resultList.get(i).getCabinBaggage(), resultList.get(i).getMeals(),
                                resultList.get(i).getTravelInsurance(), resultList.get(i).getInflightEntertainment(),
                                resultList.get(i).getElectricSocket(), resultList.get(i).getWifi(), resultList.get(i).getReschedule(),
                                resultList.get(i).getRefund()),
                                resultList.get(i).getAirplaneFlightTimeId(), resultList.get(i).getFlightTime(), resultList.get(i).getDepartureCode(),
                                resultList.get(i).getArrivalCode(), resultList.get(i).getDepartureTime(), resultList.get(i).getArrivalTime(),
                                resultList.get(i).getTotalPrice()
                        ));
            }

            Long totalCount = jdbcTemplate.queryForObject(countQuery, Long.class, departureCode, arrivalCode, departureDateStr, departureDateStr, departureCode, arrivalCode, departureDateStr, departureCode, arrivalCode, airplaneClass);
            if (resultList.isEmpty()){
                resultList.clear();
                totalCount = 0L;
                PageImpl<ScheduleFlightDTO> flightDTOS = new PageImpl<>(resultList, pageable, totalCount);
                return response.suksesDTO(flightDTOS);
            }
            if (resultList.get(0).getArrivalTime() == null || resultList.get(0).getTotalPrice() == null){
                resultList.clear();
                totalCount = 0L;
                PageImpl<ScheduleFlightDTO> flightDTOS = new PageImpl<>(resultList, pageable, totalCount);
                return response.suksesDTO(flightDTOS);
            }

            PageImpl<ScheduleFlightResponseDTO> flightDTOS = new PageImpl<>(scheduleFlightResponseDTOS, pageable, totalCount);
            return response.suksesDTO(flightDTOS);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }
}
