package com.xkd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.InvitationMapper;
import com.xkd.model.Invitation;
import com.xkd.model.InvitationPages;
import com.xkd.model.InvitationPagesElement;

@Service
public class InvitationService {

	@Autowired
	private InvitationMapper invitationMapper;

	public void saveInvitation(Invitation invitation) {
		invitationMapper.saveInvitation(invitation);
	}

	public void saveInvitationPages(InvitationPages invitationPages) {
		invitationMapper.saveInvitationPages(invitationPages);
	}

	public void editInvitation(Invitation invitation) {
		invitationMapper.editInvitation(invitation);
		
	}

	public void editInvitationPages(InvitationPages invitationPages) {
		invitationMapper.editInvitationPages(invitationPages);
		
	}

	public void saveElement(InvitationPagesElement invitationPagesElement) {
		invitationMapper.saveElement(invitationPagesElement);
		
	}

	public void editElement(InvitationPagesElement invitationPagesElement) {
		invitationMapper.editElement(invitationPagesElement);
		
	}

	public void deletePagesElement(String id) {
		invitationMapper.deletePagesElement(id);
	}

	public void deleteInvitation(String id, String userId) {
		invitationMapper.deleteInvitation(id,userId);
	}

	public Invitation getInvitationById(String id) {
		Invitation invitation = invitationMapper.getInvitationById(id);
		if(null == invitation){
			return null;
		}
		List<InvitationPages> pagesList =  invitationMapper.getPagesListByInvitationId(id);
		List<InvitationPagesElement> elementList = null;
		if(null != pagesList){
			elementList = invitationMapper.getElementListByInvitationId(id);
			if(null != elementList){
				for (InvitationPages pages : pagesList) {
					for (InvitationPagesElement element : elementList) {
						if(element.getPagesId().equals(pages.getId())){
							List<InvitationPagesElement> pagesElement = pages.getElements();
							if(null == pagesElement){
								pagesElement = new ArrayList<>();
							}
							pagesElement.add(element);
							pages.setElements(pagesElement);
						}
					}
				}
			}
		}
		invitation.setPages(pagesList);
		return invitation;
	}

	public List<Invitation> getInvitationList(Map<String, Object> map) {
		return invitationMapper.getInvitationList(map);
	}

	public void changeInvitationBindMeeting(String meetingId, String invitationId, String userId) {
		invitationMapper.changeInvitationBindMeeting(meetingId,invitationId,userId);
	}

	public List<InvitationPages> getPagesTempletList() {
		List<InvitationPages> pagesList =  invitationMapper.getPagesTempletList();
		List<InvitationPagesElement> elementList = invitationMapper.getElementTempletList();
		if(null != pagesList){
			if(null != elementList){
				for (InvitationPages pages : pagesList) {
					for (InvitationPagesElement element : elementList) {
						if(element.getPagesId().equals(pages.getId())){
							List<InvitationPagesElement> pagesElement = pages.getElements();
							if(null == pagesElement){
								pagesElement = new ArrayList<>();
							}
							pagesElement.add(element);
							pages.setElements(pagesElement);
						}
					}
				}
			}
		}
		return pagesList;
	}


}
