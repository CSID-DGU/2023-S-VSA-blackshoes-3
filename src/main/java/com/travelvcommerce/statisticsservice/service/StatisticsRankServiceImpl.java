package com.travelvcommerce.statisticsservice.service;

import com.travelvcommerce.statisticsservice.dto.RankDto;
import com.travelvcommerce.statisticsservice.entity.AdClickCount;
import com.travelvcommerce.statisticsservice.entity.TagViewCount;
import com.travelvcommerce.statisticsservice.entity.VideoLikeCount;
import com.travelvcommerce.statisticsservice.entity.VideoViewCount;
import com.travelvcommerce.statisticsservice.repository.AdClickCountRepository;
import com.travelvcommerce.statisticsservice.repository.TagViewCountRepository;
import com.travelvcommerce.statisticsservice.repository.VideoLikeCountRepository;
import com.travelvcommerce.statisticsservice.repository.VideoViewCountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class StatisticsRankServiceImpl implements StatisticsRankService {
    @Autowired
    private VideoViewCountRepository videoViewCountRepository;
    @Autowired
    private TagViewCountRepository tagViewCountRepository;
    @Autowired
    private VideoLikeCountRepository videoLikeCountRepository;
    @Autowired
    private AdClickCountRepository adClickCountRepository;

    @Override
    public List<RankDto.VideoViewRankDto> getVideoViewTop10(String sellerId) {
        List<VideoViewCount> videoViewCountTop10 = videoViewCountRepository.findTop10BySellerIdOrderByCountDesc(sellerId);

        List<RankDto.VideoViewRankDto> videoViewRankDtoList = new ArrayList<>();

        videoViewCountTop10.stream().forEach(videoViewCount -> {
            videoViewRankDtoList.add(RankDto.VideoViewRankDto.builder()
                    .videoId(videoViewCount.getVideoId())
                    .videoName(videoViewCount.getVideoName())
                    .views(videoViewCount.getViewCount())
                    .build());
        });

        return videoViewRankDtoList;
    }

    @Override
    public List<RankDto.TagViewRankDto> getTagViewTop10(String sellerId) {
        List<TagViewCount> tagViewCountTop10 = tagViewCountRepository.findTop10BySellerIdOrderByCountDesc(sellerId);

        List<RankDto.TagViewRankDto> tagViewRankDtoList = new ArrayList<>();

        tagViewCountTop10.stream().forEach(tagViewCount -> {
            tagViewRankDtoList.add(RankDto.TagViewRankDto.builder()
                    .tagId(tagViewCount.getTagId())
                    .tagName(tagViewCount.getTagName())
                    .views(tagViewCount.getViewCount())
                    .build());
        });

        return tagViewRankDtoList;
    }

    @Override
    public List<RankDto.VideoLikeRankDto> getVideoLikeTop10(String sellerId) {
        List<VideoLikeCount> videoLikeCountTop10 = videoLikeCountRepository.findTop10BySellerIdOrderByCountDesc(sellerId);

        List<RankDto.VideoLikeRankDto> videoLikeRankDtoList = new ArrayList<>();

        videoLikeCountTop10.stream().forEach(videoLikeCount -> {
            videoLikeRankDtoList.add(RankDto.VideoLikeRankDto.builder()
                    .videoId(videoLikeCount.getVideoId())
                    .videoName(videoLikeCount.getVideoName())
                    .likes(videoLikeCount.getLikeCount())
                    .build());
        });

        return videoLikeRankDtoList;
    }

    @Override
    public List<RankDto.VideoAdClickRankDto> getAdClickTop10(String sellerId) {
        List<AdClickCount> adClickCountTop10 = adClickCountRepository.findTop10BySellerIdOrderByCountDesc(sellerId);

        List<RankDto.VideoAdClickRankDto> videoAdClickRankDtoList = new ArrayList<>();

        adClickCountTop10.stream().forEach(adClickCount -> {
            videoAdClickRankDtoList.add(RankDto.VideoAdClickRankDto.builder()
                    .adId(adClickCount.getAdId())
                    .videoId(adClickCount.getVideoId())
                    .videoName(adClickCount.getVideoName())
                    .adClicks(adClickCount.getClickCount())
                    .build());
        });
        return videoAdClickRankDtoList;
    }
}
