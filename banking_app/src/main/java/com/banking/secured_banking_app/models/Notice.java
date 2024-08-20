package com.banking.secured_banking_app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notice_details")
public class Notice
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notice_id")
	private long noticeId;
	@Column(name = "notice_summary")
	private String noticeSummary;
	@Column(name = "notice_details")
	private String noticeDetails;
	@Column(name = "notic_beg_dt")
	private Date noticBegDt;
	@Column(name = "notic_end_dt")
	private Date noticEndDt;
	@JsonIgnore
	@Column(name = "create_dt")
	private Date createDt;
	@JsonIgnore
	@Column(name = "update_dt")
	private Date updateDt;
}
