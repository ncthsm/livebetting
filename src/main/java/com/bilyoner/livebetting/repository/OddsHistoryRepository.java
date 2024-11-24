package com.bilyoner.livebetting.repository;

import com.bilyoner.livebetting.entity.Match;
import com.bilyoner.livebetting.entity.OddsHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OddsHistoryRepository extends JpaRepository<OddsHistory, Integer> {
    Page<OddsHistory> findByMatchOrderByTimestampAsc(Match match, Pageable pageable);

}
