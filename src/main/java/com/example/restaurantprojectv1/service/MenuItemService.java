package com.example.restaurantprojectv1.service;

import com.example.restaurantprojectv1.domain.entity.MenuItem;
import com.example.restaurantprojectv1.domain.dto.MenuItemDto;
import com.example.restaurantprojectv1.domain.entity.MenuItemFile;
import com.example.restaurantprojectv1.domain.entity.Review;
import com.example.restaurantprojectv1.domain.entity.ReviewFile;
import com.example.restaurantprojectv1.exception.DataNotFoundException;
import com.example.restaurantprojectv1.repository.MenuItemFileRepository;
import com.example.restaurantprojectv1.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemFileRepository menuItemFileRepository;

    String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\menuItem";

    public Long create(MultipartFile file, MenuItemDto.Request requestDto) throws IOException {
        MenuItem menuItem = MenuItem.builder()
                .food(requestDto.getFood())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .build();

        menuItemRepository.save(menuItem);

        if (file != null){
            saveFile(menuItem, file);
        }

        return menuItem.getId();

    }

    public MenuItemDto.Response read(Long id) {
        return menuItemRepository.findById(id)
                .map(m -> new MenuItemDto.Response(m))
                .orElseThrow(() -> new DataNotFoundException("메뉴를 찾을 수 없습니다."));
    }

    public List<MenuItemDto.Response> readAll(){
        return menuItemRepository.findAll()
                .stream()
                .map(m -> new MenuItemDto.Response(m))
                .collect(Collectors.toList());
    }

    public Long update(Long id, MultipartFile file, MenuItemDto.Request menuItemDto) throws IOException {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("메뉴를 찾을 수 없습니다."));

        menuItem.set(menuItemDto);

        menuItemRepository.save(menuItem);

        if (!file.isEmpty()){
            saveFile(menuItem, file);
        }

        return menuItem.getId();
    }

    public void delete(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("메뉴를 찾을 수 없습니다."));

        menuItemRepository.delete(menuItem);

        removeFile(menuItem.getMenuItemFileList().get(0).getFileName());
    }





    private void saveFile(MenuItem menuItem, MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

        File savedFile = new File(projectPath, fileName);
        file.transferTo(savedFile);

        MenuItemFile menuItemFile = MenuItemFile.builder()
                .fileName(fileName)
                .filePath("/images/menuItem/" + fileName)
                .menuItem(menuItem).build();

        menuItemFileRepository.save(menuItemFile);

        menuItem.getMenuItemFileList().add(menuItemFile);
    }

    public void removeFile(String fileName){
        File deleteFile;

        try {
            deleteFile = new File(projectPath + "\\" + URLDecoder.decode(fileName, "UTF-8"));
            deleteFile.delete();

            menuItemFileRepository.deleteByFileName(fileName);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
