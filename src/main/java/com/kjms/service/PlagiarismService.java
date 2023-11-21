package com.kjms.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlagiarismService {

    public List<Map<String, String>> searchWordInGoogle(MultipartFile file) {
        List<Map<String, String>> results = new ArrayList<>();

        Map<String,String> googleResult = new HashMap<>();

        googleResult.put("hhh","kkk");

        results.add(googleResult);

        return results;
    }

}
