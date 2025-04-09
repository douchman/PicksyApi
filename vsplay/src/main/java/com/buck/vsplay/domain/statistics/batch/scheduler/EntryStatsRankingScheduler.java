package com.buck.vsplay.domain.statistics.batch.scheduler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class EntryStatsRankingScheduler {

    private final JobLauncher jobLauncher;
    private final Job entryStatsRankingJob;

    //@Scheduled(cron = " */10 * * * * *") // every 10 seconds
    @Scheduled(cron = "0 */30 * * * *") // every 30 minutes
    public void runRankingJob(){
        try {
            jobLauncher.run(
                    entryStatsRankingJob,
                    new JobParametersBuilder()
                            .addLong("time", System.currentTimeMillis())
                            .toJobParameters()
            );
        }catch ( Exception e) {
            log.error( "runRankingJob Exception  -> {}" , e.getMessage());
        }
    }
}
