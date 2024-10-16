package phool.rpg_session_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.Session;
import phool.rpg_session_notes.repository.SessionRepository;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session findById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
    }

    public Session createSessionForCampaign(Session session, Campaign campaign) {
        int sessionCount = sessionRepository.countByCampaign(campaign);
        Session newSession = new Session();
        newSession.setSessionDate(session.getSessionDate());
        newSession.setSessionNumber(sessionCount + 1);
        newSession.setCampaign(campaign);
        campaign.addSession(newSession);
        return sessionRepository.save(newSession);
    }

    public List<Session> findAllByCampaign(Campaign campaign) {
        return sessionRepository.findByCampaign(campaign);
    }

}
