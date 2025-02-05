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
    public List<Lecture> likeLectures(Long lectureId) {
        return lectureDao.likeLectures(lectureId);
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

}
