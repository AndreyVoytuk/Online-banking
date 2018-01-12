package com.userfront.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.userfront.domain.Appointment;
import com.userfront.service.AppointmentService;
import com.userfront.service.UserService;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	AppointmentService appointmentService;

	@Autowired
	UserService userService;

	@GetMapping("/create")
	public String createAppointment(Model model) {
		Appointment appointment = new Appointment();
		model.addAttribute("appointment", appointment);
		model.addAttribute("dateString", "");

		return "appointment";
	}

	@PostMapping("/create")
	public String createAppointmentPost(@ModelAttribute("appointment") Appointment appointment, @ModelAttribute("dateString") String date,
			Model model, Principal principal) throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		appointment.setDate(format.parse(date));
		appointment.setUser(userService.findByUsername(principal.getName()));

		appointmentService.createAppointment(appointment);

		return "redirect:/userFront";
	}
}
