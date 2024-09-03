package com.banking.secured_banking_app.repositories;

import com.banking.secured_banking_app.models.Notice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoticeRepository extends CrudRepository<Notice, Long>
{
	@Query(value = "from Notice n where CURDATE() BETWEEN n.noticeBegDt AND n.noticeEndDt")
	List<Notice> findAllActiveNotices();
}
