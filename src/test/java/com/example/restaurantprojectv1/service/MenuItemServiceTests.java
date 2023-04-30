package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.entity.MenuItem;
import com.example.restaurantprojectv1.domain.dto.MenuItemDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.MenuItemRepository;
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
public class MenuItemServiceTests {

    @Autowired private MenuItemService menuItemService;
    @Autowired private MenuItemRepository menuItemRepository;

    private MenuItemDto.Request menuItem;
    private MockMultipartFile file;

    @BeforeEach
    void before(){
        menuItem = MenuItemDto.Request.builder()
                .food("test")
                .price(3000)
                .description("설명").build();

        file = new MockMultipartFile("images", "test.jpg", "image/jpg",  "test file".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("메뉴 생성 성공 - 파일 포함된 경우")
    void create_with_file() throws IOException {
        // given
        // when
        Long menuItemId = menuItemService.create(file, menuItem);

        // then
        MenuItem savedMenuItem = menuItemRepository.findById(menuItemId).orElse(null);

        assertEquals(menuItem.getFood(), savedMenuItem.getFood());
        assertEquals(menuItem.getPrice(), savedMenuItem.getPrice());
    }

    @Test
    @DisplayName("메뉴 생성 성공 - 파일 포함안된 경우")
    void create_without_file() throws IOException {
        // given
        MockMultipartFile file = new MockMultipartFile("file", InputStream.nullInputStream());

        // when
        Long menuItemId = menuItemService.create(file, menuItem);

        // then
        MenuItem savedMenuItem = menuItemRepository.findById(menuItemId).orElse(null);

        assertEquals(menuItem.getFood(), savedMenuItem.getFood());
        assertEquals(menuItem.getPrice(), savedMenuItem.getPrice());
    }

    @Test
    @DisplayName("메뉴 읽기 실패")
    void read_fail(){
        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () -> menuItemService.read(1004L));

        // then
        assertEquals("메뉴를 찾을 수 없습니다.", fail.getMessage());
    }


    @Test
    @DisplayName("메뉴 수정 성공 - 파일 포함된")
    void update_with_file() throws IOException {
        // given
        Long savedId = menuItemService.create(file, menuItem);

        menuItem.setFood("수정");
        menuItem.setDescription("수정");

        // when
        Long menuItemId = menuItemService.update(savedId, file, menuItem);

        // then
        MenuItem savedMenuItem = menuItemRepository.findById(menuItemId).orElse(null);

        assertEquals(menuItem.getFood(), savedMenuItem.getFood());
        assertEquals(menuItem.getDescription(), savedMenuItem.getDescription());
    }

    @Test
    @DisplayName("메뉴 수정 성공 - 파일 포함안된")
    void update_without_file1() throws IOException {
        // given
        MockMultipartFile file = new MockMultipartFile("file", InputStream.nullInputStream());
        Long savedId = menuItemService.create(file, menuItem);

        menuItem.setFood("수정");
        menuItem.setDescription("수정");

        // when
        Long menuItemId = menuItemService.update(savedId, file, menuItem);

        // then
        MenuItem savedMenuItem = menuItemRepository.findById(menuItemId).orElse(null);

        assertEquals(menuItem.getFood(), savedMenuItem.getFood());
        assertEquals(menuItem.getDescription(), savedMenuItem.getDescription());

    }

    @Test
    @DisplayName("메뉴 수정 성공 - 수정 시점에 파일 추가된")
    void update_with_file2() throws IOException {
        // given
        MockMultipartFile emptyFile = new MockMultipartFile("file", InputStream.nullInputStream());
        Long savedId = menuItemService.create(emptyFile, menuItem);

        menuItem.setFood("수정");
        menuItem.setDescription("수정");

        // when
        Long menuItemId = menuItemService.update(savedId, file, menuItem);

        // then
        MenuItem savedMenuItem = menuItemRepository.findById(menuItemId).orElse(null);

        assertEquals(menuItem.getFood(), savedMenuItem.getFood());
        assertEquals(menuItem.getDescription(), savedMenuItem.getDescription());
    }

    @Test
    @DisplayName("메뉴 삭제 실패")
    void delete_fail() {
        // when
        DataNotFoundException fail = assertThrows(DataNotFoundException.class, () -> menuItemService.delete(1004L));

        // then
        assertEquals("메뉴를 찾을 수 없습니다.", fail.getMessage());
    }


}