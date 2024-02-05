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
    public ResponseDTO getScheduleFlight(String departureCode, String arrivalCode, Date departureDate, String airplaneClass,
                                         String departureTimeFilter, String companyName, String hasBaggage, String hasInflightEntertainment, String hasMeals,
                                         String hasUSB, String hasWIFI, String hasRefund, String hasReschedule, Pageable pageable) {
        try
        {
            Map<String, String> varInput = new HashMap<>();
            varInput.put("departureTimeFilter", departureTimeFilter);
            varInput.put("companyName", companyName);
            varInput.put("hasBaggage", hasBaggage);
            varInput.put("hasInflightEntertainment", hasInflightEntertainment);
            varInput.put("hasMeals", hasMeals);
            varInput.put("hasUSB", hasUSB);
            varInput.put("hasWIFI", hasWIFI);
            varInput.put("hasRefund", hasRefund);
            varInput.put("hasReschedule", hasReschedule);

            List<String> listFilter = new ArrayList<>();
            for (Map.Entry<String, String> entry : varInput.entrySet()) {
                if (!entry.getValue().isEmpty()){
                    String filter = "lower(sf.\""+entry.getKey()+"\") = lower('"+entry.getValue()+"')";
                    listFilter.add(filter);
                }
            }
            String filter;
            if (listFilter.size()>1){
                String tempFilter = "";
                for (int i = 0; i<listFilter.size();i++){
                    if (i == 0){
                        tempFilter = listFilter.get(0) + "\n";
                    }else {
                        tempFilter = tempFilter + "and " + listFilter.get(i) + "\n";
                    }
                }
                filter = "where \n" + tempFilter;
            }else {
                filter = "where " + listFilter.get(0);
            }

            String sql = "select * from (" +
                    "select c.\"name\" as \"companyName\", c.url as \"urlLogo\", " +
                    "a.\"name\" as \"airplaneName\", a.code as \"airplaneCode\", ac.airplane_class as \"airplaneClass\", ac.capacity as \"capacity\", atf.flight_time, " +
                    "upper(?) as \"departureCode\", upper(?) as \"arrivalCode\", " +
                    "(select concat(ap.city, ' (',upper(ap.code) ,')') from airports ap where ap.code = upper(?) limit 1) as \"departureCityCode\" ,\n" +
                    "(select concat(ap.city, ' (',upper(ap.code) ,')') from airports ap where ap.code = upper(?) limit 1) as \"arrivalCityCode\"," +
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
                    "as2.wifi as \"wifi\", \n" +
                    "CASE\n" +
                    "    WHEN as2.baggage IS NOT NULL AND as2.cabin_baggage IS NOT null and as2.baggage  != 0 and as2.cabin_baggage != 0 THEN initcap('Baggage')\n" +
                    "    ELSE NULL\n" +
                    "end as \"hasBaggage\",\n" +
                    "CASE\n" +
                    "    WHEN as2.inflight_entertainment IS true and as2.inflight_entertainment IS NOT NULL THEN initcap('Entertainment') \n" +
                    "    ELSE NULL\n" +
                    "end as \"hasInflightEntertainment\",\n" +
                    "CASE\n" +
                    "    WHEN as2.meals IS true and as2.meals IS NOT NULL THEN initcap('Meals') \n" +
                    "    ELSE NULL \n" +
                    "end as \"hasMeals\",\n" +
                    "CASE\n" +
                    "    WHEN as2.electric_socket IS true and as2.electric_socket IS NOT NULL THEN upper('USB') \n" +
                    "    ELSE NULL \n" +
                    "end as \"hasUSB\",\n" +
                    "case \n" +
                    "    WHEN as2.wifi IS true and as2.wifi IS NOT NULL THEN upper('WIFI') \n" +
                    "    ELSE NULL \n" +
                    "end as \"hasWIFI\",\n" +
                    "CASE\n" +
                    "    WHEN as2.refund != 0 and as2.refund IS NOT NULL THEN initcap('Refund') \n" +
                    "    ELSE NULL \n" +
                    "end as \"hasRefund\",\n" +
                    "CASE\n" +
                    "    WHEN as2.reschedule is true and as2.reschedule  IS NOT NULL THEN initcap('Reschedule') \n" +
                    "    ELSE NULL \n" +
                    "end as \"hasReschedule\",\n" +
                    "CASE\n" +
                    "    WHEN atf.flight_time between time '00:00:00' and time '12:00:00' and atf.flight_time IS NOT NULL THEN initcap('Pagi')\n" +
                    "    WHEN atf.flight_time between time '12:00:00' and time '15:00:00' and atf.flight_time IS NOT NULL THEN initcap('Siang')\n" +
                    "    WHEN atf.flight_time between time '15:00:00' and time '18:00:00' and atf.flight_time IS NOT NULL THEN initcap('Sore')\n" +
                    "    WHEN atf.flight_time between time '18:00:00' and time '24:00:00' and atf.flight_time IS NOT NULL THEN initcap('Malam')\n" +
                    "    ELSE NULL \n" +
                    "end as \"departureTimeFilter\"" +
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
                    "and as2.deleted_date is null) sf " + filter;
//            System.out.println(sql);
            String pattern = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String departureDateStr = simpleDateFormat.format(departureDate);

            String countQuery = "select count(*) from (" + sql + ") as subquery";

            List<ScheduleFlightDTO> resultList = jdbcTemplate.query(sql + " LIMIT ? OFFSET ?",
                    new Object[]{departureCode, arrivalCode,departureCode, arrivalCode,
                            departureDateStr, departureDateStr, departureCode, arrivalCode, departureDateStr,
                            departureCode, arrivalCode, airplaneClass, pageable.getPageSize(), pageable.getOffset()},
                    new BeanPropertyRowMapper<>(ScheduleFlightDTO.class));

            List<ScheduleFlightResponseDTO> scheduleFlightResponseDTOS = new ArrayList<>();

            for (int i=0; i<resultList.size(); i++){

                LocalDate date = resultList.get(i).getDepartureTime().toLocalDateTime().toLocalDate();
                List<Object[]> totalSeat = transactionRepository.getTotalSeatTransactionAirplane(resultList.get(i).getAirplaneId(), resultList.get(i).getAirplaneClassId(), resultList.get(i).getAirplaneFlightTimeId(), date);
                // System.out.println(resultList.get(i).getAirplaneId());
                // System.out.println(resultList.get(i).getAirplaneClassId());
                // System.out.println(resultList.get(i).getAirplaneFlightTimeId());
                // System.out.println(date);
//                System.out.println(totalSeat);
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
                    // System.out.println("masuk");
                    // System.out.println(Math.toIntExact(totalSeatData.get(0).getTotalSeatTransaction()));
                    seat = Math.toIntExact(totalSeatData.get(0).getTotalSeatTransaction());
                }
                // System.out.println("iter " + i);
                // System.out.println(seat);

                scheduleFlightResponseDTOS.add(
                        new ScheduleFlightResponseDTO(resultList.get(i).getCompanyName(), resultList.get(i).getUrlLogo(), resultList.get(i).getAirplaneId(),
                                resultList.get(i).getAirplaneName(), resultList.get(i).getAirplaneCode(), resultList.get(i).getAirplaneClassId(),
                                resultList.get(i).getAirplaneClass(), (resultList.get(i).getCapacity()-seat), new AirplaneServiceDTO(
                                resultList.get(i).getBaggage(), resultList.get(i).getCabinBaggage(), resultList.get(i).getMeals(),
                                resultList.get(i).getTravelInsurance(), resultList.get(i).getInflightEntertainment(),
                                resultList.get(i).getElectricSocket(), resultList.get(i).getWifi(), resultList.get(i).getReschedule(),
                                resultList.get(i).getRefund()),
                                resultList.get(i).getAirplaneFlightTimeId(), resultList.get(i).getFlightTime(),
                                resultList.get(i).getDepartureCode(), resultList.get(i).getDepartureCityCode(),
                                resultList.get(i).getArrivalCode(), resultList.get(i).getArrivalCityCode(),
                                resultList.get(i).getDepartureTime(), resultList.get(i).getArrivalTime(),
                                resultList.get(i).getTotalPrice()
                        ));
            }


            Long totalCount = jdbcTemplate.queryForObject(countQuery, Long.class, departureCode, arrivalCode,departureCode, arrivalCode,
                    departureDateStr, departureDateStr, departureCode, arrivalCode, departureDateStr, departureCode, arrivalCode, airplaneClass);
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
