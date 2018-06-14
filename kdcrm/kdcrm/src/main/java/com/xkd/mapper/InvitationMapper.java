package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Invitation;
import com.xkd.model.InvitationPages;
import com.xkd.model.InvitationPagesElement;

public interface InvitationMapper {

	void saveInvitation(Invitation invitation);

	void saveInvitationPages(InvitationPages invitationPages);

	void editInvitation(Invitation invitation);

	void editInvitationPages(InvitationPages invitationPages);

	void saveElement(InvitationPagesElement invitationPagesElement);

	void editElement(InvitationPagesElement invitationPagesElement);
	
	void deletePagesElement(@Param("id") String id);

	void deleteInvitation(@Param("id") String id, @Param("userId") String userId);

	List<InvitationPages> getPagesListByInvitationId(@Param("invitationId") String invitationId);
	
	List<InvitationPagesElement> getElementListByInvitationId(@Param("invitationId") String invitationId);

	Invitation getInvitationById(@Param("id") String id);

	List<Invitation> getInvitationList(Map<String, Object> map);

	void changeInvitationBindMeeting(@Param("meetingId") String meetingId, @Param("invitationId") String invitationId, @Param("userId") String userId);

	List<InvitationPages> getPagesTempletList();

	List<InvitationPagesElement> getElementTempletList();

}
