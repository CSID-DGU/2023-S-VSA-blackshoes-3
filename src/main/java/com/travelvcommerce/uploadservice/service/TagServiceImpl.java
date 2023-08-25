package com.travelvcommerce.uploadservice.service;

import com.travelvcommerce.uploadservice.dto.TagDto;
import com.travelvcommerce.uploadservice.entity.Tag;
import com.travelvcommerce.uploadservice.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;

    @Override
    public List<TagDto> getAllTags() {
        List<Tag> tagList = tagRepository.findAll();

        List<TagDto> tagDtoList = new ArrayList<>();

        tagList.forEach(tag -> {
            TagDto tagDto = modelMapper.map(tag, TagDto.class);
            tagDtoList.add(tagDto);
        });

        return tagDtoList;
    }

    @Override
    public List<TagDto> getTagsByType(String type) {
        List<Tag> tagList = tagRepository.findByType(type);

        List<TagDto> tagDtoList = new ArrayList<>();

        tagList.forEach(tag -> {
            TagDto tagDto = modelMapper.map(tag, TagDto.class);
            tagDtoList.add(tagDto);
        });

        return tagDtoList;
    }
}
