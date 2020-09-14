package net.guides.springboot2.crud.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class SocietateSelectata {
    private long selected;

    public SocietateSelectata() { this.selected = 0; }

    public long getSelected() {
        return this.selected;
    }
    public void setSelected(long selected) {
        this.selected = selected;
    }
}
