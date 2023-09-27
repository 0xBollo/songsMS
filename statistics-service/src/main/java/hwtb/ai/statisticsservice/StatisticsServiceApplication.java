package hwtb.ai.statisticsservice;

import hwtb.ai.statisticsservice.repository.PersonalSongStatisticsRepository;
import hwtb.ai.statisticsservice.repository.SongStatisticsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StatisticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner deleteAllSongStatistics(SongStatisticsRepository songStatisticsRepository,
                                                     PersonalSongStatisticsRepository personalSongStatisticsRepository)
    {
        return args -> {
            songStatisticsRepository.deleteAll();
            personalSongStatisticsRepository.deleteAll();
        };
    }
}
