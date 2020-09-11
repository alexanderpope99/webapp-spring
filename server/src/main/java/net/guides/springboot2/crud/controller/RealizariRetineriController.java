package net.guides.springboot2.crud.controller;


import java.time.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.crud.model.RealizariRetineri;


@RestController
@RequestMapping("/realizariretineri")
public class RealizariRetineriController {
    public static void main(String[] args) {
		LocalDate today = LocalDate.now();
        int wd = getWorkingDaysInMonthOfYear(today.getMonthValue(), today.getYear());
        System.out.println(wd);
    }

	// @Autowired
    // private RealizariRetineriRepository realizariRetineriRepository;

    @GetMapping("{id}&{luna}&{an}")
    public RealizariRetineri getRealizariRetineriById(@PathVariable(value = "id") Integer idcontract, @PathVariable(value="luna") Integer luna, @PathVariable(value="an") Integer an){
        int nrtichete = getWorkingDaysInMonthOfYear(luna, an);
        return new RealizariRetineri(nrtichete);
    }

    private static int getWorkingDaysInMonthOfYear(int month, int year) {
        int weekdayNr;
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        int workingDays = 0;
        for(int i=1; i <= daysInMonth; ++i) {
            weekdayNr = LocalDate.of(year, month, i).getDayOfWeek().getValue();
            if(weekdayNr != 6 && weekdayNr != 7)
                workingDays++;
        }
        return workingDays;
    }
}
