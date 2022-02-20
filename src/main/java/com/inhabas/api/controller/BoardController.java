package com.inhabas.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardController <DetailDto, ListDto, SaveDto, UpdateDto> {
     DetailDto getBoard(Integer menuId, Integer id);

     Page<ListDto> getBoardList(Integer menuId, Pageable pageable);

     Integer addBoard(Integer menuId, SaveDto saveDto);

     Integer updateBoard(Integer menuId, UpdateDto updateDto);

     void deleteBoard(Integer id);

}