package gift.controller.itemController;


import gift.dto.itemDto.ItemCreateDto;
import gift.dto.itemDto.ItemDto;
import gift.dto.itemDto.ItemResponseDto;
import gift.dto.itemDto.ItemUpdateDto;
import gift.service.itemService.ItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminItemController {

    private final ItemService itemService;

    public AdminItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public String viewItemList(
            Model model,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer price) {

        List<ItemResponseDto> items = findItems(name, price);
        model.addAttribute("items", items);
        return "admin/list";
    }

    private List<ItemResponseDto> findItems(String name, Integer price) {
        if (name == null && price == null) {
            return itemService.getAllItems();
        }
        return itemService.getItems(name, price);
    }

    @PostMapping
    public String saveItem(@ModelAttribute @Valid ItemCreateDto itemDTO) {

        itemService.saveItem(itemDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("itemDTO", new ItemCreateDto("", 0, "", false));
        return "admin/createForm";
    }

    @PostMapping("/delete")
    public String deleteItem(@RequestParam Long id) {
        ItemDto item = itemService.findById(id);
        if (item != null) {
            itemService.deleteById(id);
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/edit")
    public String updateItem(@PathVariable Long id, @ModelAttribute @Valid ItemUpdateDto dto) {
        itemService.updateItem(id, dto);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        ItemDto item = itemService.findById(id);
        ItemUpdateDto dto = new ItemUpdateDto(item);
        model.addAttribute("itemDTO", dto);
        return "admin/editForm";
    }


}
