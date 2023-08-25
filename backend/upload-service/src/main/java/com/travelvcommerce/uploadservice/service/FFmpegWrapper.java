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
@RequiredArgsConstructor
public class FFmpegWrapper {
    @Value("${ffmpeg.path}")
    private String FFMPEG_PATH;
    @Value("${ffprobe.path}")
    private String FFPROBE_PATH;
    private final SimpMessagingTemplate messagingTemplate;

    public void encodeToHls(String userId, String inputPath, String encodingPath, int idx, int resolution) throws IOException {
        FFmpeg ffmpeg = new FFmpeg(FFMPEG_PATH);
        FFprobe ffprobe = new FFprobe(FFPROBE_PATH);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        int height = resolution;
        int width = resolution * 16 / 9;
        if (width % 2 != 0) {
            width--;
        }

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputPath)
                .addOutput(encodingPath + "/" + resolution + "p.m3u8")
                .setFormat("hls")
                .setAudioCodec("aac")
                .setVideoCodec("libx264")
                .setVideoResolution(width, height)
                .setVideoFrameRate(30)
                .done();

        FFmpegProbeResult probeResult = ffprobe.probe(inputPath);
        long time_ns = (long) (probeResult.getFormat().duration * 1000000000L);
        long total_time_ns = time_ns * 3;

        executor.createJob(builder, new ProgressListener() {
            @Override
            public void progress(Progress progress) {
                float percentage = (progress.out_time_ns + time_ns * idx) / (float) total_time_ns * 100;
                EncodingProgressDto encodingProgressDto = EncodingProgressDto.builder()
                        .encodedPercentage(percentage)
                        .build();
                messagingTemplate.convertAndSend("/topic/encoding/" + userId, encodingProgressDto);
            }
        }).run();
    }
}
