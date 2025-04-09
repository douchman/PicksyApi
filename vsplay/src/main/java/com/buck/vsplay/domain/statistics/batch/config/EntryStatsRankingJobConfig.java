package com.buck.vsplay.domain.statistics.batch.config;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class EntryStatsRankingJobConfig {

    @Bean
    public Job entryStatsRankingJob(JobRepository jobRepository,
                                    Step entryStatsRankingStep) {
        return new JobBuilder("entryStatsRankingJob", jobRepository)
                .start(entryStatsRankingStep)
                .build();
    }

    @Bean
    public Step entryStatsRankingStep(JobRepository jobRepository,
                                      Tasklet entryStatsRankingTasklet,
                                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("entryRankingStep", jobRepository)
                .tasklet(entryStatsRankingTasklet, transactionManager)
                .build();
    }
}
