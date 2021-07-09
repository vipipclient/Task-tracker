package com.berdibekov.menu;

import java.util.Objects;

public class MenuAction {

    String description;
    Runnable action;

    public MenuAction(String description, Runnable action) {
        this.description = description;
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuAction that = (MenuAction) o;
        return description.equals(that.description) &&
               action.equals(that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, action);
    }

    @Override
    public String toString() {
        return "MenuOption{" +
               "description='" + description + '\'' +
               ", handler=" + action +
               '}';
    }
}
