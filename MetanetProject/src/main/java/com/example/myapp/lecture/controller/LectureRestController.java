package com.example.myapp.lecture.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.myapp.common.response.ResponseCode;
import com.example.myapp.common.response.ResponseDto;
import com.example.myapp.common.response.ResponseMessage;
import com.example.myapp.lecture.model.Lecture;
import com.example.myapp.lecture.model.LectureFile;
import com.example.myapp.lecture.model.LectureId;
import com.example.myapp.lecture.service.ILectureService;
import com.example.myapp.util.RegexUtil;
import com.example.myapp.util.S3FileUploader;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/lecture")
public class LectureRestController {

    @Autowired
    ILectureService lectureService;

    @Autowired
    S3FileUploader s3FileUploader;

    // 전체 조회 -- 고범준
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @GetMapping("/all")
    public ResponseEntity<ResponseDto> getAllLectures() {

        List<Lecture> lecture = new ArrayList<Lecture>();
        try {
            lecture = lectureService.getAllLectures();
        } catch (Exception e) {
            return ResponseDto.databaseError();
        }
        ResponseDto responseBody = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, lecture);
        return ResponseEntity.ok(responseBody);
    }

    // 특정 강의의 조회 -- 고범준
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @GetMapping("/{lecture_id}")
    public ResponseEntity<ResponseDto> getLectureDetail(@PathVariable("lecture_id") Long lectureId) {

        Lecture lecture = new Lecture();
        try {
            lecture = lectureService.getLectureDetail(lectureId);
        } catch (Exception e) {
            return ResponseDto.databaseError();
        }
        ResponseDto responseBody = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, lecture);
        return ResponseEntity.ok(responseBody);
    }

    // 좋아요 누른 강의 목록 보기 -- 고범준
    @SuppressWarnings({ "rawtypes" })
    @PostMapping("/like/{lecture_id}")
    public ResponseEntity<ResponseDto> likeLectures(@PathVariable("lecture_id") Long lectureId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String member_id = "";

        if (authentication != null) {
            // 현재 인증된 사용자 정보
            member_id = authentication.getName();
            log.info(member_id);
        }
        if (member_id == null)
            return ResponseDto.noAuthentication();

        Long memberId = lectureService.getMemberIdById(member_id);

        boolean exist = lectureService.checkLikeLectures(memberId, lectureId);
        try {
            lectureService.updateLikeLectures(memberId, lectureId, exist);
            if (exist) {
                lectureService.deleteLikeLectures(memberId, lectureId);
            } else {
                lectureService.insertLikeLectures(memberId, lectureId);
            }
        } catch (Exception e) {
            return ResponseDto.databaseError();
        }
        ResponseDto responseBody = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.ok(responseBody);
    }

    // 이미지 업로드 (List) form-Data -- 고범준
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping("/upload/{lecture_id}")
    public ResponseEntity<ResponseDto> lectureFileUpload(
            @PathVariable("lecture_id") Long lectureId,
            @RequestParam("files") List<MultipartFile> files) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String memberId = "";

        if (authentication != null) {
            // 현재 인증된 사용자 정보
            memberId = authentication.getName();
            log.info(memberId);
        }
        if (memberId == null)
            return ResponseDto.noAuthentication();

        Long member_id = lectureService.getMemberIdById(memberId);

        try {
            List<String> urls = s3FileUploader.uploadFiles(files, "lectures", "classFile", member_id);
            for (String url : urls) {
                LectureFile lectureFile = new LectureFile();
                lectureFile.setLectureId(lectureId);
                lectureFile.setMemberId(member_id);
                lectureFile.setFileUrl(url);
                lectureService.lectureFileUpload(lectureFile);
            }
            System.out.println(urls);
            ResponseDto responseBody = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, urls);
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return ResponseDto.databaseError();
        }
    }

    // 환불 불가로 변경 기능 Json -- 고범준
    @SuppressWarnings({ "rawtypes" })
    @PutMapping("/{lecture_id}/refund-status")
    public ResponseEntity<ResponseDto> setRefundStatus(@RequestBody Map<String, Long> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String memberId = "";
        Long lectureId = request.get("lectureId");

        if (authentication != null) {
            // 현재 인증된 사용자 정보
            memberId = authentication.getName();
            log.info(memberId);
        }
        if (memberId == null)
            return ResponseDto.noAuthentication();

        Long member_id = lectureService.getMemberIdById(memberId);

        LectureId ids = new LectureId(member_id, lectureId);
        try {
            lectureService.setRefundStatus(ids);
        } catch (Exception e) {
            return ResponseDto.databaseError();
        }
        ResponseDto responseBody = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.ok(responseBody);
    }

    // 강의 추가 form-Data -- 고범준
    @SuppressWarnings({ "rawtypes" })
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> lecturelikeLectures(@ModelAttribute Lecture lecture,
            @RequestParam(value = "profile", required = false) MultipartFile profile,
            @RequestParam(value = "descriptionPic", required = false) MultipartFile descriptionPic) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String memberId = "";

        if (authentication != null) {
            // 현재 인증된 사용자 정보
            memberId = authentication.getName();
            log.info(memberId);
        }
        if (memberId == null)
            return ResponseDto.noAuthentication();

        Long member_id = lectureService.getMemberIdById(memberId);
        try {
            lecture.setMemberId(member_id);
            if (lecture.getTitle() == null || lecture.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseDto("REGEX_ERROR", "Title value is required"));
            }
            if (lecture.getDescription() == null || lecture.getDescription().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto("REGEX_ERROR", "Description value is required"));
            }
            if (lecture.getCategory() == null || lecture.getCategory().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseDto("REGEX_ERROR", "Category value is required"));
            }
            if (lecture.getDeadlineTime() == null) {
                return ResponseEntity.badRequest().body(new ResponseDto("REGEX_ERROR", "Deadline time is required"));
            }
            if (lecture.getStartDate() == null) {
                return ResponseEntity.badRequest().body(new ResponseDto("REGEX_ERROR", "Start date is required"));
            }
            if (lecture.getEndDate() == null) {
                return ResponseEntity.badRequest().body(new ResponseDto("REGEX_ERROR", "End date is required"));
            }
            if (profile != null) {
                String url = new String();
                try {
                    url = s3FileUploader.uploadFile(profile, "lectures", "profile", member_id);
                    lecture.setProfileUrl(url);
                } catch (Exception e) {
                    return ResponseDto.serverError();
                }
                lecture.setProfileUrl(url);
            }
            if (descriptionPic != null) {
                String url = new String();
                try {
                    url = s3FileUploader.uploadFile(descriptionPic, "lectures", "description", member_id);
                    lecture.setDescriptionPicUrl(url);
                } catch (Exception e) {
                    return ResponseDto.serverError();
                }
                lecture.setDescriptionPicUrl(url);
            }

            lectureService.registerLectures(lecture);
        } catch (Exception e) {
            System.out.println(lecture.toString());
            return ResponseDto.databaseError();
        }
        ResponseDto responseBody = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.ok(responseBody);
    }

    // 강의 내용 수정 form-Data -- 고범준
    @SuppressWarnings({ "rawtypes" })
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> updateLectures(
            @ModelAttribute Lecture lecture,
            @RequestParam(value = "profile", required = false) MultipartFile profile,
            @RequestParam(value = "descriptionPic", required = false) MultipartFile descriptionPic) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = "";
        if (authentication != null) {
            // 현재 인증된 사용자 정보
            memberId = authentication.getName();
            log.info(memberId);
        }
        if (memberId == null) {
            return ResponseDto.noAuthentication();
        }

        Long member_id = lectureService.getMemberIdById(memberId);

        try {
            RegexUtil regexUtil = new RegexUtil();
            lecture.setMemberId(member_id);

            if (lecture.getTitle() == null || lecture.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto("REGEX_ERROR", "Title value is required"));
            }
            if (lecture.getDescription() == null || lecture.getDescription().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto("REGEX_ERROR", "Description value is required"));
            }
            if (lecture.getCategory() == null || lecture.getCategory().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto("REGEX_ERROR", "Category value is required"));
            }
            if (lecture.getDeadlineTime() == null || !regexUtil.checkDate(lecture.getDeadlineTime())) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto("REGEX_ERROR", "Deadline time is required"));
            }
            if (lecture.getStartDate() == null || !regexUtil.checkDate(lecture.getStartDate())) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto("REGEX_ERROR", "Start date is required"));
            }
            if (lecture.getEndDate() == null || !regexUtil.checkDate(lecture.getEndDate())) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto("REGEX_ERROR", "End date is required"));
            }

            if (profile != null) {
                String url;
                try {
                    url = s3FileUploader.uploadFile(profile, "lectures", "profile", member_id);
                } catch (Exception e) {
                    return ResponseDto.serverError();
                }
                lecture.setProfileUrl(url);
            }
            if (descriptionPic != null) {
                String url;
                try {
                    url = s3FileUploader.uploadFile(descriptionPic, "lectures", "description", member_id);
                } catch (Exception e) {
                    return ResponseDto.serverError();
                }
                lecture.setDescriptionPicUrl(url);
            }
            lectureService.updateLectures(lecture);

        } catch (Exception e) {
            System.out.println(lecture.toString());
            return ResponseDto.databaseError();
        }

        ResponseDto responseBody = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.ok(responseBody);
    }

    // 강의 삭제 Json -- 고범준
    @SuppressWarnings({ "rawtypes" })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLectures(@RequestBody Map<String, Long> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String memberId = "";
        Long lecture_id = request.get("lectureId");

        if (authentication != null) {
            // 현재 인증된 사용자 정보
            memberId = authentication.getName();
            log.info(memberId);
        }
        if (memberId == null)
            return ResponseDto.noAuthentication();

        Long member_id = lectureService.getMemberIdById(memberId);
        try {
            lectureService.deleteLectures(lecture_id, member_id);
        } catch (Exception e) {
            return ResponseDto.databaseError();
        }
        ResponseDto responseBody = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.ok(responseBody);
    }

}
