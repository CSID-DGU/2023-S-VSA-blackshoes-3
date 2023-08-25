package com.travelvcommerce.uploadservice.config;

import com.travelvcommerce.uploadservice.service.TemporaryVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class TemporaryVideoSchedulerConfig {
    @Configuration
    @EnableScheduling
    public class VideoExpirationScheduler {

        @Autowired
        private TemporaryVideoService temporaryVideoService;

        @Scheduled(fixedRateString = "${video.check-interval}")
        public void checkAndDeleteExpiredVideosTask() {
            temporaryVideoService.findAllExpiredVideoAndDelete();
        }
    }
}
