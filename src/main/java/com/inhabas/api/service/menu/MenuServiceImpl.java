package com.inhabas.api.service.menu;

import com.inhabas.api.controller.BoardController;
import com.inhabas.api.controller.ContestBoardController;
import com.inhabas.api.domain.menu.MenuRepository;
import com.inhabas.api.domain.menu.MenuType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final ApplicationContext context;

    private final MenuRepository menuRepository;

    @Override
    public Optional<BoardController> findControllerByMenuId(Integer menuId){
        MenuType menuType = menuRepository.findMenuTypeByMenuId(menuId).get();

        switch(menuType){
            case contest:
                return Optional.of(context.getBean(ContestBoardController.class));
            default:
                return Optional.empty(); // NormalBoardController
        }
    }
}
