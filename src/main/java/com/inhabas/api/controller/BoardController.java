package com.inhabas.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface BoardController <DetailDto, ListDto> {
     DetailDto getBoard(Integer menuId, Integer id);

     Page<ListDto> getBoardList(Integer menuId, Pageable pageable);

     Integer addBoard(Integer menuId, Map<String, Object> saveDto);

     Integer updateBoard(Integer menuId, Map<String, Object> updateDto);

     void deleteBoard(Integer id);
}