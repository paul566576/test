package com.banking.secured_banking_app.controller;

import com.banking.secured_banking_app.models.Notice;
import com.banking.secured_banking_app.repositories.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequiredArgsConstructor
public class NoticeController
{
	private final NoticeRepository noticeRepository;

	@GetMapping("/notices")
	public ResponseEntity<List<Notice>> getMyNotices()
	{
		final List<Notice> notices = noticeRepository.findAllActiveNotices();

		if (!notices.isEmpty())
		{
			return ResponseEntity.ok().cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)).body(notices);
		}
		return ResponseEntity.noContent().build();
	}
}
