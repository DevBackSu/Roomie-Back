package com.example.roomie.Service;

import com.example.roomie.Entity.Notice;

import java.util.Map;
import java.util.List;

public interface NoticeService {

    List<Notice> getNoticeList(int page, int size);
}
