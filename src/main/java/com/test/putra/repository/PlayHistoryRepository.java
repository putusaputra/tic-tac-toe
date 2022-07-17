package com.test.putra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.putra.model.PlayHistory;

public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Integer> {
	List<PlayHistory> findByPlayId(Integer playId);
}
