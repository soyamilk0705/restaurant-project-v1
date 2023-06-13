package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.entity.MenuItem;
import com.example.restaurantprojectv1.domain.entity.Restaurant;
import com.example.restaurantprojectv1.domain.entity.Review;
import com.example.restaurantprojectv1.domain.entity.User;
import com.example.restaurantprojectv1.domain.dto.ReviewDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.MenuItemRepository;
import com.example.restaurantprojectv1.repository.RestaurantRepository;
import com.example.restaurantprojectv1.repository.ReviewRepository;
import com.example.restaurantprojectv1.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import javax.transaction.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewServiceTests {

    @Autowired private ReviewService reviewService;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private MenuItemRepository menuItemRepository;


    private ReviewDto.Request reviewDto = new ReviewDto.Request();
    private Restaurant restaurant;
    private User user;
    private MenuItem menuItem;

    @BeforeEach
    void before(){
        restaurant = restaurantRepository.save(new Restaurant("test", 10));
        user = userRepository.save(new User("test@gmail.com", "test"));
        menuItem = menuItemRepository.save(MenuItem.builder().food("reviewTest").build());;

        reviewDto.setFood("reviewTest");
        reviewDto.setScore(5);
        reviewDto.setDescription("test");

    }

    @Test
    @DisplayName("리뷰 생성 성공 - 파일 포함된 경우")
    void create_with_file() throws IOException {
        // given
        MockMultipartFile file = createMockMultipartFile();

        // when
        Long reviewId = reviewService.create(restaurant.getId(), user.getId(), reviewDto, file);

        // then
        Review savedReview = reviewRepository.findById(reviewId).orElse(null);

        assertEquals(reviewDto.getScore(), savedReview.getScore());
        assertEquals(reviewDto.getDescription(), savedReview.getDescription());
        assertNotNull(savedReview.getReviewFileList());

    }

    @Test
    @DisplayName("리뷰 생성 성공 - 파일 포함안된 경우")
    void create_without_file() throws IOException {
        // given
        MockMultipartFile file = new MockMultipartFile("file", InputStream.nullInputStream());
        // when
        Long reviewId = reviewService.create(restaurant.getId(), user.getId(), reviewDto, file);

        // then
        Review savedReview = reviewRepository.findById(reviewId).orElse(null);

        assertEquals(reviewDto.getScore(), savedReview.getScore());
        assertEquals(reviewDto.getDescription(), savedReview.getDescription());
        assertNotNull(savedReview.getReviewFileList());

    }

    @Test
    @DisplayName("리뷰 읽기 실패")
    void read_fail() {
        // given
        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () ->  reviewService.read(1004L));

        // then
        assertEquals("리뷰를 찾을 수 없습니다.", fail.getMessage());
    }


    @Test
    @DisplayName("리뷰 수정 성공 - 파일 포함안된 경우")
    void update_without_file() throws IOException {
        // given
        MockMultipartFile file = new MockMultipartFile("file", InputStream.nullInputStream());
        Long savedId = reviewService.create(restaurant.getId(), user.getId(), reviewDto, file);
        reviewDto.setScore(1);
        reviewDto.setDescription("수정");

        // when
        reviewService.update(savedId, file, reviewDto);

        // then
        Review savedReview = reviewRepository.findById(savedId).orElse(null);

        assertEquals(reviewDto.getScore(), savedReview.getScore());
        assertEquals(reviewDto.getDescription(), savedReview.getDescription());
        assertNotNull(savedReview.getReviewFileList());

    }

    @Test
    @DisplayName("리뷰 수정 성공 - 파일 포함된 경우")
    void update_with_file1() throws IOException {
        // given
        MockMultipartFile file = createMockMultipartFile();
        Long savedId = reviewService.create(restaurant.getId(), user.getId(), reviewDto, file);

        reviewDto.setScore(1);
        reviewDto.setDescription("수정");

        // when
        reviewService.update(savedId, file, reviewDto);

        // then
        Review savedReview = reviewRepository.findById(savedId).orElse(null);

        assertEquals(reviewDto.getScore(), savedReview.getScore());
        assertEquals(reviewDto.getDescription(), savedReview.getDescription());
        assertNotNull(savedReview.getReviewFileList());

    }

    @Test
    @DisplayName("리뷰 수정 - 수정 시점에서 파일 포함된 경우")
    void update_with_file2() throws IOException {
        // given
        MockMultipartFile emptyFile = new MockMultipartFile("file", InputStream.nullInputStream());
        MockMultipartFile file = createMockMultipartFile();
        Long savedId = reviewService.create(restaurant.getId(), user.getId(), reviewDto, file);

        reviewDto.setScore(1);
        reviewDto.setDescription("수정");

        // when
        reviewService.update(savedId, emptyFile, reviewDto);

        // then
        Review savedReview = reviewRepository.findById(savedId).orElse(null);

        assertEquals(reviewDto.getScore(), savedReview.getScore());
        assertEquals(reviewDto.getDescription(), savedReview.getDescription());
        assertNotNull(savedReview.getReviewFileList());

    }

    @Test
    @DisplayName("리뷰 삭제 실패")
    void delete_fail() {
        // given
        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () ->  reviewService.delete(1004L));

        // then
        assertEquals("리뷰를 찾을 수 없습니다.", fail.getMessage());
    }

    private MockMultipartFile createMockMultipartFile(){
        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_test.jpg";

        MockMultipartFile file = new MockMultipartFile("images", filename, "image/jpg",  "test file".getBytes(StandardCharsets.UTF_8));

        return file;
    }

}