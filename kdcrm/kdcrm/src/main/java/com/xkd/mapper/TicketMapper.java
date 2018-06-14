package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TicketMapper {

    Integer saveTickets(@Param("tickets")List<Map<String,Object>> tickets);

    List<Map<String,Object>> selectTicketsByMeetingId(@Param("meetingId") String meetingId);

    Integer updateTicketById(Map<String, Object> map);

    Integer getTicketSavingByTicketId(@Param("ticketId")String ticketId);

    Integer updateTicketSavingById(@Param("ticketId")String ticketId, @Param("number")int number);

    Map<String,Object> selectTicketById(@Param("ticketId")String ticketId);

    Map<String,String> selectUserOrderByOrderNo(@Param("orderNo")String orderNo);

    Integer updateUserTicketById(Map<String, String> userTicket);

    List<Map<String,Object>> selectTicketsByContent(@Param("meetingId") String meetingId, @Param("content") String content,
                                                    @Param("currentPageInt") int currentPageInt, @Param("pageSizeInt") int pageSizeInt,
                                                    @Param("payStatus") String payStatus, @Param("mgroup") String mgroup, @Param("ticketType")String ticketType);

    List<Map<String,Object>> selectTicketNumberByOderId(@Param("orderId")String orderId);

    Integer ensureGetTickets(Map<String, Object> maps);

    Integer selectTicketsTotalByContent(@Param("meetingId") String meetingId, @Param("content") String content,
                                        @Param("payStatus") String payStatus,@Param("mgroup")  String mgroup,
                                        @Param("ticketType")  String ticketType);

    List<Map<String,Object>> selectorderTicketByOderId(@Param("orderId") String orderId);

    Map<String,Object> selectPayMessageAfterPay(@Param("meetingId")String meetingId, @Param("mhtOrderNo")String mhtOrderNo);

    List<Map<String,Object>> selectUserTicketsByUserId(@Param("userId")String userId, @Param("meetingId")String meetingId);

    Map<String,String> selectUserOrderById(@Param("id")String id);
}
