package com.example.myapp.lecture.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.example.myapp.lecture.model.Lecture;
import com.example.myapp.lecture.model.LectureFile;
import com.example.myapp.lecture.model.LectureId;

@Repository
@Mapper
public interface ILectureRepository {
    List<Lecture> getAllLectures();

    Lecture getLectureDetail(Long lectureId);

    boolean checkLikeLectures(Long memberId, Long lectureId);

    void insertLikeLectures(Long memberId, Long lectureId);

    void deleteLikeLectures(Long memberId, Long lectureId);

    void updateLikeLectures(Long memberId, Long lectureId, boolean exist);

    int lectureFileUpload(LectureFile lectureFile);

    void setRefundStatus(LectureId lectureId);

    void registerLectures(Lecture lecture);

    void updateLectures(Lecture lecture);

    void deleteLectures(Long lectureId, Long memberId);

    Long getMemberIdById(String memberId);
}
