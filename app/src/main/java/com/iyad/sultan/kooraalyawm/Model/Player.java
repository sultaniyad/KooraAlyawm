package com.iyad.sultan.kooraalyawm.Model;


import java.util.Map;

public class Player {

    private String id;
    private String name;
    private String icon;
    private Map<String,Object> privilege;
    private Map<String,Object> groups;
    private String pushToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Map<String, Object> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Map<String, Object> privilege) {
        this.privilege = privilege;
    }

    public Map<String, Object> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Object> groups) {
        this.groups = groups;
    }

    public String getPushToken() {
        return pushToken;
    }
    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
}
