package com.wth.service;

import com.wth.mapper.SenderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SenderService {
    @Autowired
    private SenderMapper senderMapper;
}
