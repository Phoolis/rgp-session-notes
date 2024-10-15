package phool.rpg_session_notes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import phool.rpg_session_notes.domain.Campaign;
import phool.rpg_session_notes.domain.Session;
import phool.rpg_session_notes.repository.SessionRepository;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session createSessionForCampaign(Session session, Campaign campaign) {
        int sessionCount = sessionRepository.countByCampaign(campaign);
        Session newSession = new Session();
        newSession.setSessionDate(session.getSessionDate());
        newSession.setSessionNumber(sessionCount + 1);
        newSession.setCampaign(campaign);
        campaign.addSession(newSession);
        return sessionRepository.save(newSession);
    }

}
