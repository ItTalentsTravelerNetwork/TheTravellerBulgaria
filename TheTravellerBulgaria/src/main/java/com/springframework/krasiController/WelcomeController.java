package com.springframework.krasiController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springframework.model.Papagal;

@Controller
@RequestMapping(value="/greeting")
public class WelcomeController {


	@RequestMapping(value="/hi", method = RequestMethod.GET)
	public String zdrastiBace(Model model, HttpSession ses){
		//load data from DAO
		//init conneciton to other services
		//do some other magic stuff
		//prepare objects for visualization
		model.addAttribute("greeting", "Ko staa? :)");
		return "hello";
	}

	@RequestMapping(value="/bye", method = RequestMethod.GET)
	public String chaoBace(Model model){
		//load data from DAO
		//init conneciton to other services
		//do some other magic stuff
		//prepare objects for visualization
		model.addAttribute("greeting", "Ai chao");
		return "hello";
	}

	@RequestMapping(value="/papagal/{papagalNumber}/room/{roomNumber}/etaj/{floorNumber}", method = RequestMethod.GET)
	public String etoTiPapagal(Model model, @PathVariable("papagalNumber") Integer papagalNumber
										 , @PathVariable("roomNumber") Integer roomNumber
										 , @PathVariable("floorNumber") Integer floorNumber){
		Papagal poli = new Papagal("Poli", 3);
		model.addAttribute(poli);
		model.addAttribute("papagalNumber", papagalNumber);
		model.addAttribute("roomNumber", roomNumber);
		model.addAttribute("floorNumber", floorNumber);
		return "papagal";
	}
	
}
