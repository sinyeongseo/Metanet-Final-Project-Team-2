package com.example.myapp.lecture.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.lecture.dao.ILectureRepository;
import com.example.myapp.lecture.model.Lecture;
import com.example.myapp.lecture.model.LectureFile;
import com.example.myapp.lecture.model.LectureId;

@Service
public class LectureService implements ILectureService {

    @Autowired
    ILectureRepository lectureDao;

    @Override
    public List<Lecture> getAllLectures() {
        return lectureDao.getAllLectures();
    }

    @Override
    public int lectureFileUpload(LectureFile lectureFile) {
        return lectureDao.lectureFileUpload(lectureFile);
    }

    @Override
    public void setRefundStatus(LectureId lectureId) {
        lectureDao.setRefundStatus(lectureId);
    }

    @Override
    public void registerLectures(Lecture lecture) {
        lectureDao.registerLectures(lecture);

    }

    @Override
    public void updateLectures(Lecture lecture) {
        lectureDao.updateLectures(lecture);
    }

    @Override
    public void deleteLectures(Long lectureId, Long memberId) {
        lectureDao.deleteLectures(lectureId, memberId);
    }

    @Override
    public Long getMemberIdById(String memberId) {
        return lectureDao.getMemberIdById(memberId);
    }

    @Override
    public Lecture getLectureDetail(Long lectureId) {
        return lectureDao.getLectureDetail(lectureId);
    }

    @Override
    public boolean checkLikeLectures(Long memberId, Long lectureId) {
        return lectureDao.checkLikeLectures(memberId, lectureId);
    }

    @Override
    public void insertLikeLectures(Long memberId, Long lectureId) {
        lectureDao.insertLikeLectures(memberId, lectureId);
    }

    @Override
    public void deleteLikeLectures(Long memberId, Long lectureId) {
        lectureDao.deleteLikeLectures(memberId, lectureId);
    }

    @Override
    public void updateLikeLectures(Long memberId, Long lectureId, boolean exist) {
        lectureDao.updateLikeLectures(memberId, lectureId, exist);
    }

}
