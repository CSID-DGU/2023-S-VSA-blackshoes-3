package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.EncodingProgressDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.JsonbMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class FFmpegWrapper {
    @Value("${ffmpeg.path}")
    private String FFMPEG_PATH;
    @Value("${ffprobe.path}")
    private String FFPROBE_PATH;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void encodeToHls(String userId, String inputPath, String outputPath) throws IOException {
        FFmpeg ffmpeg = new FFmpeg(FFMPEG_PATH);
        FFprobe ffprobe = new FFprobe(FFPROBE_PATH);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .addOutput(outputPath)
                .setFormat("hls")
                .setAudioCodec("aac")
                .setVideoCodec("libx264")
                .setVideoResolution(1280, 720)
                .setVideoFrameRate(30)
                .done();

        FFmpegProbeResult probeResult = ffprobe.probe(inputPath);
        long total_time_ns = (long) (probeResult.getFormat().duration * 1000000000L);

        executor.createJob(builder, new ProgressListener() {
            @Override
            public void progress(Progress progress) {
                float percentage = progress.out_time_ns / (float) total_time_ns * 100;
                EncodingProgressDto encodingProgressDto = EncodingProgressDto.builder()
                        .percentage(percentage)
                        .build();
                messagingTemplate.convertAndSend("/topic/encoding/" + userId, encodingProgressDto);
            }
        }).run();
    }
}
