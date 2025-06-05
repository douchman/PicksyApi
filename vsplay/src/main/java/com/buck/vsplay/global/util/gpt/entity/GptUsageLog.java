package com.buck.vsplay.global.util.gpt.entity;


import com.buck.vsplay.global.entity.Timestamp;
import com.buck.vsplay.global.util.gpt.prompt.GptPromptType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "GPT_USAGE_LOG")
@SequenceGenerator(name = "GPT_USAGE_SEQ_LOG_GENERATOR", sequenceName = "GPT_USAGE_LOG_SEQ")
@Comment("GPT API 호출 로그")
public class GptUsageLog extends Timestamp {

    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GPT_USAGE_SEQ_LOG_GENERATOR")
    private Long id;

    @Column(name = "prompt_type")
    @Comment("호출 용도")
    private GptPromptType promptType;

    @Column(name = "model")
    @Comment("GPT 모델")
    private String model;

    @Column(name = "prompt_tokens")
    @Comment("요청 프롬프트 토큰 수")
    private Integer promptTokens;

    @Column(name = "completion_tokens")
    @Comment("응답 생성 토큰 수")
    private Integer completionTokens;

    @Column(name = "total_tokens")
    @Comment("전체 사용 토큰 수")
    private Integer totalTokens;

    @Column(name = "estimated_cost")
    @Comment("예상 비용 ( 단위 : 달러 )")
    private Double estimatedCost;

    @Column(name = "response_time_mills")
    @Comment("응답 소요 시간")
    private Long responseTimeMills;

    @Column(name = "input_preview")
    @Comment("입력 데이터")
    private String inputPreview;

    @Column(name = "success")
    @Comment("성공 여부")
    private boolean success;

    @Column(name = "error_code")
    @Comment("오류 코드")
    private String errorCode;
}
