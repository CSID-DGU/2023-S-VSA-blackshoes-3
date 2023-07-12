package com.travelvcommerce.uploadservice.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class FFmpegWrapper {
    @Value("${ffmpeg.path}")
    private static String FFMPEG_PATH;

    public void encodeToHls(String inputPath, String outputPath) throws IOException {
        FFmpeg ffmpeg = new FFmpeg(FFMPEG_PATH);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .addOutput(outputPath)
                .setFormat("hls").done();

        executor.createJob(builder).run();
    }
}
