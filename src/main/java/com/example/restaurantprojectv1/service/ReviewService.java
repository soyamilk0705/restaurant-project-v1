package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dto.ReviewDto;
import com.example.restaurantprojectv1.domain.entity.*;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final ReviewFileRepository reviewFileRepository;

    String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\review";


    /**
     * 리뷰 작성
     */
    public Long create(Long restaurantId, Long userId, ReviewDto.Request reviewDto, MultipartFile file) throws IOException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DataNotFoundException("음식점을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));

        MenuItem menuItem = getMenuItemData(reviewDto.getFood());

        Review review = Review.builder()
                .score(reviewDto.getScore())
                .description(reviewDto.getDescription())
                .restaurant(restaurant)
                .menuItem(menuItem)
                .user(user).build();

        reviewRepository.save(review);

        if (file != null){
            saveFile(review, file);
        }


        return review.getId();
    }

    /**
     * 리뷰 보기
     */
    public ReviewDto.Response read(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(r -> new ReviewDto.Response(r))
                .orElseThrow(() -> new DataNotFoundException("리뷰를 찾을 수 없습니다."));
    }

    /**
     * 리뷰 전체 보기
     */
    public Page<ReviewDto.Response> readAll(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(r -> new ReviewDto.Response(r));
    }

    /**
     * 리뷰 전체 보기 - 검색
     */
    public Page<ReviewDto.Response> readAllSearch(String keyword, Pageable pageable){
        return reviewRepository.findAllByDescriptionContaining(keyword, pageable)
                .map(r -> new ReviewDto.Response(r));
    }

    /**
     * 내가 쓴 리뷰 보기
     */
    public Page<ReviewDto.Response> readAllByUserId(Long userId, Pageable pageable){
        return reviewRepository.findAllByUserId(userId, pageable)
                .map(r -> new ReviewDto.Response(r));
    }

    /**
     *  내가 쓴 리뷰 갯수
     */
    public Long countByUserId(Long userId) {
        return reviewRepository.countByUserId(userId);
    }


    /**
     * 리뷰 수정
     */
    public void update(Long reviewId, MultipartFile file, ReviewDto.Request reviewDto) throws IOException  {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new DataNotFoundException("리뷰를 찾을 수 없습니다."));

        if (!review.getMenuItem().equals(reviewDto.getFood())){
            MenuItem menuItem = getMenuItemData(reviewDto.getFood());
            review.setMenuItem(menuItem);
        }

        review.set(reviewDto);

        if (file != null) {
            saveFile(review, file);
        }
    }

    /**
     * 리뷰 삭제
     */
    public void delete(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new DataNotFoundException("리뷰를 찾을 수 없습니다."));

        reviewRepository.delete(review);

        removeFile(review.getReviewFileList().get(0).getFileName());
    }




    /**
     * 메뉴 데이터 가져오기
     */
    private MenuItem getMenuItemData(String food){
        return menuItemRepository.findByFood(food)
                .orElseThrow(() ->  new DataNotFoundException("메뉴를 찾을 수 없습니다."));
    }

    /**
     *  파일 저장
     */
    private void saveFile(Review review, MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

        File savedFile = new File(projectPath, fileName);
        file.transferTo(savedFile);

        ReviewFile reviewFile = ReviewFile.builder()
                .fileName(fileName)
                .filePath("/images/review/" + fileName)
                .review(review).build();

        reviewFileRepository.save(reviewFile);

        review.getReviewFileList().add(reviewFile);
    }

    /**
     * 파일 삭제
     */
    public void removeFile(String fileName){
        File deleteFile;

        try {
            deleteFile = new File(projectPath + "\\" + URLDecoder.decode(fileName, "UTF-8"));
            deleteFile.delete();

            reviewFileRepository.deleteByFileName(fileName);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
