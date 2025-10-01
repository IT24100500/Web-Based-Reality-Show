package com.example.demo.Service;

import com.example.demo.DAO.ContestantDAO;
import com.example.demo.Entity.Contestant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContestantService {

    private final ContestantDAO contestantDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate; // ✅ used for custom ID generation


    public ContestantService(ContestantDAO contestantDAO) {

        this.contestantDAO = contestantDAO;
    }

    public List<Contestant> getAllContestants() {

        return contestantDAO.findAll();
    }

    public List<Contestant> findByEpisodeId(String episodeId) {

        return contestantDAO.findByEpisodeId(episodeId);
    }

    public List<Contestant> findByStatus(String status) {

        return contestantDAO.findByStatus(status);
    }

    public Optional<Contestant> findContestantById(String contestantId) {

        return contestantDAO.findById(contestantId);
    }


    /** ✅ Custom ID generator: C001, C002, … */
    public String generateContestantId() {
        Integer maxId = jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(CAST(SUBSTRING(contestant_id, 2) AS UNSIGNED)), 0) FROM contestant",
                Integer.class
        );
        return "C" + String.format("%03d", maxId + 1);
    }
    @Transactional
    public void saveContestant(Contestant contestant) {
        if (validateContestant(contestant)) {
            // ✅ Assign custom ID before saving
            if (contestant.getContestantId() == null || contestant.getContestantId().isBlank()) {
                contestant.setContestantId(generateContestantId());
            }
            contestantDAO.save(contestant);
        } else {
            throw new IllegalArgumentException("Invalid contestant data");
        }
    }

    @Transactional
    public int updateContestant(Contestant contestant) {
        if (validateContestant(contestant)) {
            return contestantDAO.update(contestant);
        }
        throw new IllegalArgumentException("Invalid contestant data");
    }

    @Transactional
    public int deleteContestant(String contestantId) {

        return contestantDAO.delete(contestantId);
    }

    public boolean validateContestant(Contestant contestant) {
        return contestant != null &&
                contestant.getName() != null && !contestant.getName().isBlank() &&
                contestant.getLastName() != null && !contestant.getLastName().isBlank() &&
                contestant.getAge() > 0 &&
                contestant.getDob() != null &&
                contestant.getStatus() != null && !contestant.getStatus().isBlank() &&
                contestant.getShow() != null &&
                contestant.getShow().getEpisodeId() != null &&
                !contestant.getShow().getEpisodeId().isBlank();
    }
}
