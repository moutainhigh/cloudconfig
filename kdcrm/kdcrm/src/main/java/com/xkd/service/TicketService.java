package com.xkd.service;

import com.xkd.mapper.TicketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TicketService {

    @Autowired
    TicketMapper ticketMapper;

    public  Integer saveTickets(List<Map<String,Object>> tickets){

        return ticketMapper.saveTickets(tickets);
    }

    public List<Map<String,Object>> selectTicketsByMeetingId(String meetingId) {
        return ticketMapper.selectTicketsByMeetingId(meetingId);
    }

    public Integer updateTicketById(Map<String, Object> map) {
        return ticketMapper.updateTicketById(map);
    }

    public Integer getTicketSavingByTicketId(String ticketId) {
        return ticketMapper.getTicketSavingByTicketId(ticketId);
    }

    public Integer updateTicketSavingById(String ticketId, int number) {
        return ticketMapper.updateTicketSavingById(ticketId,number);
    }

    public Map<String,Object> selectTicketById(String ticketId) {
        return ticketMapper.selectTicketById(ticketId);
    }

    public Map<String,String> selectUserOrderByOrderNo(String orderNo) {
        return ticketMapper.selectUserOrderByOrderNo(orderNo);
    }

    public Integer updateUserTicketById(Map<String, String> csOrder) {
        return ticketMapper.updateUserTicketById(csOrder);
    }

    public List<Map<String,Object>> selectTicketsByContent(String meetingId, String content, int currentPageInt,
                                                           int pageSizeInt, String payStatus, String mgroup, String ticketType) {
        return ticketMapper.selectTicketsByContent(meetingId,content,currentPageInt,pageSizeInt,payStatus,mgroup,ticketType);
    }

    public List<Map<String,Object>> selectTicketNumberByOderId(String orderId) {
        return ticketMapper.selectTicketNumberByOderId(orderId);
    }

    public Integer ensureGetTickets(Map<String, Object> maps) {
        return ticketMapper.ensureGetTickets(maps);
    }

    public Integer selectTicketsTotalByContent(String meetingId, String content,String payStatus, String mgroup, String ticketType) {
        return ticketMapper.selectTicketsTotalByContent(meetingId,content,payStatus,mgroup,ticketType);
    }

    public List<Map<String,Object>> selectorderTicketByOderId(String orderId) {
        return ticketMapper.selectorderTicketByOderId(orderId);
    }

    public Map<String,Object> selectPayMessageAfterPay(String meetingId, String mhtOrderNo) {
        return ticketMapper.selectPayMessageAfterPay(meetingId,mhtOrderNo);
    }

    public List<Map<String,Object>> selectUserTicketsByUserId(String userId, String meetingId) {
        return ticketMapper.selectUserTicketsByUserId(userId,meetingId);
    }

    public Map<String,String> selectUserOrderById(String id) {
        return ticketMapper.selectUserOrderById(id);
    }
}
