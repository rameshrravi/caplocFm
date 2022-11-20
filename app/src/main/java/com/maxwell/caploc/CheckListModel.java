package com.maxwell.caploc;

import java.io.Serializable;

public class CheckListModel implements Serializable {

    String id;
    String room;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
