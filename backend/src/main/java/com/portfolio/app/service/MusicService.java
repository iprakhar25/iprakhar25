package com.portfolio.app.service;

import com.portfolio.app.dto.MusicDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class MusicService {

    @Value("${music.python.script.path:scripts/fetch_youtube_music.py}")
    private String pythonScriptPath;

    // In-memory cache
    private MusicDTO cachedMusic;
    private LocalDateTime lastFetchTime;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Get current cached music (no external API call)
     */
    public MusicDTO getCurrentMusic() {
        if (cachedMusic == null) {
            // Return default if not yet fetched
            return new MusicDTO(
                    "Waiting for music...",
                    "First sync in progress",
                    null,
                    "IDLE",
                    LocalDateTime.now()
            );
        }
        return cachedMusic;
    }

    /**
     * Update music from YouTube Music (called by scheduler every 12 hours)
     * This method runs the Python script to fetch from YouTube Music
     */
    public void updateMusicFromYouTube() {
        try {
            System.out.println("[Music] Starting YouTube Music fetch...");

            // Run Python script
            String musicJson = fetchFromPythonScript();

            if (musicJson != null && !musicJson.isEmpty()) {
                // Parse JSON response
                MusicDTO music = objectMapper.readValue(musicJson, MusicDTO.class);
                
                // Update cache
                cachedMusic = music;
                lastFetchTime = LocalDateTime.now();
                cachedMusic.setLastUpdated(lastFetchTime);

                System.out.println("[Music] Successfully updated: " + music.getSongName() + " by " + music.getArtist());
            } else {
                System.err.println("[Music] Python script returned empty response");
            }

        } catch (Exception e) {
            System.err.println("[Music] Error updating music from YouTube: " + e.getMessage());
            e.printStackTrace();
            
            // Keep previous cache if update fails
            if (cachedMusic == null) {
                cachedMusic = new MusicDTO(
                        "Error loading music",
                        "Please check YouTube Music connection",
                        null,
                        "ERROR",
                        LocalDateTime.now()
                );
            }
        }
    }

    /**
     * Execute Python script and get JSON response
     */
    private String fetchFromPythonScript() throws Exception {
        try {
            // Build command
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python",
                    pythonScriptPath
            );

            // Set working directory to backend root
            processBuilder.directory(new java.io.File("backend/"));

            // Start process
            Process process = processBuilder.start();

            // Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            // Wait for process to complete (max 30 seconds)
            boolean completed = process.waitFor(30, TimeUnit.SECONDS);

            if (!completed) {
                process.destroy();
                throw new Exception("Python script execution timeout");
            }

            // Check for errors
            if (process.exitValue() != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errors = new StringBuilder();
                while ((line = errorReader.readLine()) != null) {
                    errors.append(line);
                }
                throw new Exception("Python script error: " + errors.toString());
            }

            reader.close();
            return output.toString().trim();

        } catch (Exception e) {
            System.err.println("[Music] Error executing Python script: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get last fetch time
     */
    public LocalDateTime getLastFetchTime() {
        return lastFetchTime;
    }
}
