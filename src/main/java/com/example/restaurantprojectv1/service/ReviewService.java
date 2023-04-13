package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dao.MenuItem;
import com.example.restaurantprojectv1.domain.dao.Restaurant;
import com.example.restaurantprojectv1.domain.dao.Review;
import com.example.restaurantprojectv1.domain.dao.User;
import com.example.restaurantprojectv1.domain.dto.ReviewRequestDto;
import com.example.restaurantprojectv1.domain.dto.ReviewResponseDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.MenuItemRepository;
import com.example.restaurantprojectv1.repository.RestaurantRepository;
import com.example.restaurantprojectv1.repository.ReviewRepository;
import com.example.restaurantprojectv1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;


    public Long create(Long restaurantId, Long userId, ReviewRequestDto reviewRequestDto, MultipartFile file) throws IOException {
        Restaurant restaurant = getRestaurantData(restaurantId);
        User user = getUserData(userId);
        MenuItem menuItem = getMenuItemData(reviewRequestDto.getFood());

        Review review = Review.builder()
                .score(reviewRequestDto.getScore())
                .description(reviewRequestDto.getDescription())
                .restaurant(restaurant)
                .menuItem(menuItem)
                .user(user).build();


        if (!file.isEmpty()){
            String filename = fileSetting(file);

            review.setFilename(filename)
                    .setFilepath("/images/review/" + filename);
        }

        reviewRepository.save(review);

        return review.getId();
    }

    public ReviewResponseDto read(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(this::entityToDto)
                .orElseThrow(() -> new DataNotFoundException("리뷰를 찾을 수 없습니다."));
    }

    public Page<ReviewResponseDto> readAll(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(this::entityToDto);
    }

    public Page<ReviewResponseDto> readAllSearch(String keyword, Pageable pageable){
        return reviewRepository.findAllByDescriptionContaining(keyword, pageable)
                .map(this::entityToDto);
    }

    public Page<ReviewResponseDto> readAllByUserId(Long userId, Pageable pageable){
        return reviewRepository.findAllByUserId(userId, pageable)
                .map(this::entityToDto);
    }

    public Long countByUserId(Long userId) {
        return reviewRepository.countByUserId(userId);
    }


    public Long update(Long reviewId, MultipartFile file, ReviewRequestDto request) throws IOException  {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new DataNotFoundException("리뷰를 찾을 수 없습니다."));

        MenuItem menuItem = getMenuItemData(request.getFood());

        review.setScore(request.getScore())
                .setMenuItem(menuItem)
                .setDescription(request.getDescription());


        if (!file.isEmpty()) {
            String filename = fileSetting(file);

            review.setFilename(filename)
                    .setFilepath("/images/review/" + filename);
        }

        reviewRepository.save(review);

        return review.getId();
    }

    public void delete(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new DataNotFoundException("리뷰를 찾을 수 없습니다."));

        reviewRepository.delete(review);
    }




    private ReviewResponseDto entityToDto(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .restaurantName(review.getRestaurant().getRestaurantName())
                .nickname(review.getUser().getNickname())
                .food(review.getMenuItem().getFood())
                .score(review.getScore())
                .filename(review.getFilename())
                .filepath(review.getFilepath())
                .description(review.getDescription())
                .updatedAt(review.getUpdatedAt().toLocalDate()).build();
    }

    private Restaurant getRestaurantData(Long restaurantId){
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DataNotFoundException("음식점을 찾을 수 없습니다."));
    }

    private User getUserData(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private MenuItem getMenuItemData(String food){
        return menuItemRepository.findByFood(food)
                .orElseThrow(() ->  new DataNotFoundException("메뉴를 찾을 수 없습니다."));
    }

    private String fileSetting(MultipartFile file) throws IOException {
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\review";

        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_" + file.getOriginalFilename();

        File savedFile = new File(projectPath, filename);
        file.transferTo(savedFile);

        return filename;
    }


}
