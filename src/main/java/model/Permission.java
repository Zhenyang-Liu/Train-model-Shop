package model;

public class Permission {
    private final int permissionID;
    private final String permissionName;

    public Permission(int permissionID, String permissionName) {
        this.permissionID = permissionID;
        this.permissionName = permissionName;
    }
    
    // Getter
    public int getPermissionID() {
        return permissionID;
    }

    public String getPermissionName() {
        return permissionName;
    }
}
