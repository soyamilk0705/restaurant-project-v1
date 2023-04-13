package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.dao.MenuItem;
import com.example.restaurantprojectv1.domain.dto.MenuItemRequestDto;
import com.example.restaurantprojectv1.domain.dto.MenuItemResponseDto;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public Long create(MultipartFile file, MenuItemRequestDto requestDto) throws IOException {
        MenuItem menuItem = MenuItem.builder()
                .food(requestDto.getFood())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .build();

        if (!file.isEmpty()){
            String filename = fileSetting(file);

            menuItem.setFilename(filename)
                    .setFilepath("/images/menuItem/" + filename);
        }

        menuItemRepository.save(menuItem);

        return menuItem.getId();

    }

    public MenuItemResponseDto read(Long id) {
        return menuItemRepository.findById(id)
                .map(this::entityToDto)
                .orElseThrow(() -> new DataNotFoundException("메뉴를 찾을 수 없습니다."));
    }

    public List<MenuItemResponseDto> readAll(){
        return menuItemRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }


    public Long update(Long id, MultipartFile file, MenuItemRequestDto requestDto) throws IOException {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("메뉴를 찾을 수 없습니다."));

        menuItem.setFood(requestDto.getFood())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription());

        if (!file.isEmpty()){
            String filename = fileSetting(file);

            menuItem.setFilename(filename)
                    .setFilepath("/images/menuItem/" + filename);
        }

        menuItemRepository.save(menuItem);

        return menuItem.getId();
    }

    public void delete(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("메뉴를 찾을 수 없습니다."));

        menuItemRepository.delete(menuItem);
    }




    private MenuItemResponseDto entityToDto(MenuItem menuItem) {
        return MenuItemResponseDto.builder()
                .id(menuItem.getId())
                .food(menuItem.getFood())
                .price(menuItem.getPrice())
                .description(menuItem.getDescription())
                .filename(menuItem.getFilename())
                .filepath(menuItem.getFilepath()).build();
    }

    public String fileSetting(MultipartFile file) throws IOException {
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\menuItem";

        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_" + file.getOriginalFilename();

        File savedFile = new File(projectPath, filename);
        file.transferTo(savedFile);

        return filename;
    }


}
