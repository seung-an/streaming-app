package com.example.streamingapp.repository;

import com.example.streamingapp.domain.History;
import com.example.streamingapp.domain.HistoryPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, HistoryPK> {

    List<History> findAllByHistoryPKMemberCodeOrderByWatchDtDesc(Integer memberCode);
}
