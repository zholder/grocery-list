package com.zachholder.todo.Controllers;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zachholder.todo.Models.Item;
import com.zachholder.todo.Models.data.AisleDao;
import com.zachholder.todo.Models.data.GroceryItemDao;
import com.zachholder.todo.Models.data.GroceryTypeDao;
import com.zachholder.todo.Models.data.ItemData;
import com.zachholder.todo.comparators.AisleComparator;
import com.zachholder.todo.comparators.CompoundComparator;
import com.zachholder.todo.comparators.NameComparator;
import com.zachholder.todo.comparators.TypeComparator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

@Controller
public class MainController {
	
	@Autowired
	private GroceryTypeDao groceryTypeDao;
	
	@Autowired
	private GroceryItemDao groceryItemDao;

	@Autowired
	private AisleDao aisleDao;
	
    @RequestMapping(value = "")
    public String index(Model model) {  
    	
        CompoundComparator comparator = new CompoundComparator();
        comparator.add(new AisleComparator());
        comparator.add(new NameComparator());
        ItemData.getItems().sort(comparator);

    	model.addAttribute("items", ItemData.getItems());
    	model.addAttribute("title", "Grocery List");
    	model.addAttribute("item", new Item());
        return "index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String addItems(Model model, @ModelAttribute @Valid Item item, Errors errors, int[] itemIds) { 
    	
    	if (errors.hasErrors() || (item.getName() == null && itemIds == null)){
    		model.addAttribute("items", ItemData.getItems());
        	model.addAttribute("title", "Grocery List");
        	model.addAttribute("itemTypes", groceryTypeDao.findAll());
    		return "index";
    	}
    	
    	if (itemIds != null) {
	    	for (int itemId : itemIds) {
	            ItemData.remove(itemId);
	        }
    	}
    	
    	if (itemIds == null && item.getName().length() > 0) {
    		
    		if (groceryItemDao.findByName(item.getName()) == null ) {
    			item.setType(groceryTypeDao.findById(1).get());
    			item.setAisle(aisleDao.findById(1).get());
            	ItemData.add(item);
    		}
    		else {
        	item.setType(groceryItemDao.findByName(item.getName()).getItemType());
        	item.setAisle(groceryItemDao.findByName(item.getName()).getAisle());
        	ItemData.add(item);
    	}
    	}
    	
    	return "redirect:";
    }

   
}
