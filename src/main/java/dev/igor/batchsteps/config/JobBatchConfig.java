package dev.igor.batchsteps.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableBatchProcessing
public class JobBatchConfig {

    private static final Logger log = LoggerFactory.getLogger(JobBatchConfig.class);

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public JobBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job imprimeOlaJob() {
        return jobBuilderFactory.get("imprimeOlaJob")
                .start(imprimeOlaStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    public Step imprimeOlaStep() {
        return stepBuilderFactory.get("imprimeOlaStep")
                .tasklet(imprimeOlaTasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public Tasklet imprimeOlaTasklet(@Value("#{jobParameters['nome']}") String nome) {
        return (stepContribution, chunkContext) -> {
            log.info("Olá, {}", nome);
            return RepeatStatus.FINISHED;
        };
    }
}
