package net.guides.springboot2.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.guides.springboot2.crud.model.SocietateSelectata;

@Controller
@Scope("request")
@CrossOrigin
@RequestMapping("/selected")
public class SocietateSelectataController {
    @Autowired
    private SocietateSelectata societateSelectata;

    @GetMapping
    @ResponseBody
    public long getSelected() {
        return societateSelectata.getSelected();
    }

    @PutMapping("{id}")
    @ResponseBody
    public long updateSelected(@PathVariable(value = "id") Long id) {
        societateSelectata.setSelected(id);
        return id;
    }
}
