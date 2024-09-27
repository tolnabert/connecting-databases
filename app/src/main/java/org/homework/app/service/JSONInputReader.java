package org.homework.app.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.homework.app.model.dto.ExamDto;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class JSONInputReader {

    public List<ExamDto> readJsonData(InputStream inputStream) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(inputStream, new TypeReference<>() {
        });
    }
}