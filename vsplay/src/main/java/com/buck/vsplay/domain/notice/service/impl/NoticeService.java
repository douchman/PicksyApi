package com.buck.vsplay.domain.notice.service.impl;

import com.buck.vsplay.domain.notice.constants.NoticeType;
import com.buck.vsplay.domain.notice.dto.NoticeDto;
import com.buck.vsplay.domain.notice.entity.Notice;
import com.buck.vsplay.domain.notice.exception.NoticeException;
import com.buck.vsplay.domain.notice.exception.NoticeExceptionCode;
import com.buck.vsplay.domain.notice.mapper.NoticeMapper;
import com.buck.vsplay.domain.notice.repository.NoticeRepository;
import com.buck.vsplay.domain.notice.service.INoticeService;
import com.buck.vsplay.global.dto.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService implements INoticeService {

    private final NoticeMapper noticeMapper;
    private final NoticeRepository noticeRepository;

    @Override
    public NoticeDto.SearchNoticeResponse searchNoticeList(NoticeDto.SearchNoticeRequest noticeSearchRequest) {
        int page = Math.max(noticeSearchRequest.getPage() - 1, 0); // index 보정

        Page<Notice> noticeWithpage = noticeRepository.searchByTitleAndType(
                noticeSearchRequest.getKeyword(),
                noticeSearchRequest.getNoticeType(),
                PageRequest.of(page, noticeSearchRequest.getSize()));

        return NoticeDto.SearchNoticeResponse.builder()
                .noticeList(noticeMapper.toNoticeDtoListFromEntityList(noticeWithpage.getContent()))
                .pagination(Pagination.builder()
                        .totalPages(noticeWithpage.getTotalPages())
                        .totalItems(noticeWithpage.getTotalElements())
                        .currentPage(noticeWithpage.getNumber() + 1 ) // 인덱스 보정
                        .pageSize(noticeWithpage.getSize())
                        .build())
                .build();
    }

    @Override
    public NoticeDto.NoticeDetailResponse getNoticeDetail(Long noticeId) {

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                () -> new NoticeException(NoticeExceptionCode.NOTICE_NOT_EXISTS)
        );

        return NoticeDto.NoticeDetailResponse.builder()
                .noticeDetail(noticeMapper.toNoticeDetailFromEntity(notice))
                .build();
    }

    @Override // TODO : 추후 삭제 필요
    public void createNotice() {
        noticeRepository.save(
                Notice.builder()
                        .noticeType(NoticeType.DEFAULT)
                        .title("공지사항 테스트")
                        .content("공지사항 테스트 내용입니다.")
                        .build()
        );
    }
}
