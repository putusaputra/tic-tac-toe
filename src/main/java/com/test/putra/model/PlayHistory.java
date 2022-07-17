package com.test.putra.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "play_history")
public class PlayHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "play_id")
	private Integer playId;
	
	@Column(name = "player", length = 50)
	private String player;
	
	@Column(name = "row_num")
	private Integer rowNum;
	
	@Column(name = "column_num")
	private Integer columnNum;
	
	@Column(name = "is_win")
	private Boolean isWin;
	
	@Column(name = "is_tie")
	private Boolean isTie;
	
	@Column(name = "exec_timestamp")
	private LocalDateTime execTimestamp;
}
