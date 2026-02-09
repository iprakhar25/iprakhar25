package com.portfolio.app.scheduler;

import com.portfolio.app.service.MusicService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@EnableScheduling
public class MusicScheduler {

    private final MusicService musicService;

    public MusicScheduler(MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * Fetch music every 12 hours (43200000 milliseconds)
     * First run at application startup, then every 12 hours
     */
    @Scheduled(initialDelay = 0, fixedDelay = 43200000) // 12 hours in milliseconds
    public void fetchMusicEvery12Hours() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("[MUSIC SCHEDULER] Starting music fetch at " + now.format(formatter));
        System.out.println("═══════════════════════════════════════════════════════");

        try {
            musicService.updateMusicFromYouTube();
            
            System.out.println("[MUSIC SCHEDULER] Music fetch completed successfully");
            System.out.println("Next sync in 12 hours at " + now.plusHours(12).format(formatter));
            System.out.println("═══════════════════════════════════════════════════════");
            
        } catch (Exception e) {
            System.err.println("[MUSIC SCHEDULER] Error during scheduled fetch: " + e.getMessage());
            e.printStackTrace();
            System.out.println("═══════════════════════════════════════════════════════");
        }
    }

    /**
     * Alternative: Manual trigger for immediate update (for testing)
     * Call this endpoint to manually trigger music update
     */
    public void manualRefresh() {
        System.out.println("[MUSIC SCHEDULER] Manual refresh triggered");
        musicService.updateMusicFromYouTube();
    }
}
