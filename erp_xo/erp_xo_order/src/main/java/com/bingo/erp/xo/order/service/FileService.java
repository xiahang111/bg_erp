package com.bingo.erp.xo.order.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<String> fileAnalyze(String adminUid, MultipartFile file) throws Exception;
}
