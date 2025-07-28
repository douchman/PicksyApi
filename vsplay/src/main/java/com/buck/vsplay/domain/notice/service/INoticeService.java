package com.buck.vsplay.domain.notice.service;

import com.buck.vsplay.domain.notice.dto.NoticeDto;

public interface INoticeService {
    NoticeDto.SearchNoticeResponse searchNoticeList(NoticeDto.SearchNoticeRequest noticeSearchRequest);
    NoticeDto.NoticeDetailResponse getNoticeDetail(Long noticeID);
    void createNotice(); // TODO : 추후 삭제 필요
}
