package com.buck.vsplay.domain.notice.mapper;

import com.buck.vsplay.domain.notice.dto.NoticeDto;
import com.buck.vsplay.domain.notice.entity.Notice;
import com.buck.vsplay.global.util.DateTimeUtil;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoticeMapper {

    default NoticeDto.Notice toDtoFromEntity(Notice notice){
        return NoticeDto.Notice.builder()
                .id(notice.getId())
                .noticeType(notice.getNoticeType().getDescription())
                .title(notice.getTitle())
                .build();
    }

    default NoticeDto.NoticeDetail toNoticeDetailFromEntity(Notice notice) {
        return NoticeDto.NoticeDetail.builder()
                .noticeType(notice.getNoticeType().getDescription())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(DateTimeUtil.formatDateToSting(notice.getCreatedAt()))
                .build();
    }

    default List<NoticeDto.Notice> toNoticeDtoListFromEntityList(List<Notice> noticeEntityList){
        return noticeEntityList.stream()
                .map(this::toDtoFromEntity)
                .toList();
    }
}
