package com.buck.vsplay.domain.notice.dto;


import com.buck.vsplay.domain.notice.constants.NoticeType;
import com.buck.vsplay.global.dto.Pagination;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeDto {

    @Builder
    @Getter
    public static class Notice{
        Long id;
        NoticeType noticeType;
        String title;
        String summary;
        String createdAt;
    }

    @Builder
    @Getter
    public static class NoticeDetail{
        NoticeType noticeType;
        String title;
        String content;
    }

    @Getter
    @Setter
    public static class SearchNoticeRequest{
        NoticeType noticeType;
        String keyword;
        Integer page = 1;
        Integer size = 15;
    }

    @Builder
    @Getter
    public static class SearchNoticeResponse{
        List<Notice> noticeList;
        Pagination pagination;
    }

    @Builder
    @Getter
    public static class NoticeDetailResponse{
        NoticeDetail noticeDetail;
    }

}
