package com.iyad.sultan.kooraalyawm.Model;

import java.util.Map;

public class Group {

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Object> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Object> members) {
        this.members = members;
    }

    public Map<String, Object> getCurrentgames() {
        return currentgames;
    }

    public void setCurrentgames(Map<String, Object> currentgames) {
        this.currentgames = currentgames;
    }

    public Map<String, Object> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Map<String, Object> privilege) {
        this.privilege = privilege;
    }

    private String id;
    private String name;
    private String logo;
    private boolean secure;
    private String password;
    private Map<String,Object> members;
    private Map<String,Object> currentgames;
    private Map<String,Object> privilege;

}
