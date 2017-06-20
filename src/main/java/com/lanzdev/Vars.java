package com.lanzdev;

public class Vars {

    public static final String linkDB = "jdbc:mysql://localhost:3306/memology?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
//    public static final String linkDB = "jdbc:mysql://localhost:3306/memology_test?useUnicode=true&characterEncoding=UTF-8";
    public static final String driverDB = "com.mysql.jdbc.Driver";
    public static final String userDB = "root";
    public static final String passwordDB = "Equilibrium20!4";

    private static final String ACCESS_TOKEN = "access_token=fc047acefc047acefc047ace68fc5faa60ffc04fc047acea50fe06ac7acecade36c502e";
    public static final String VK_WALL_GET = "https://api.vk.com/method/wall.get?" + ACCESS_TOKEN + "&domain=";
    public static final String VK_GROUP_CHECKER = "https://api.vk.com/method/groups.getById?" + ACCESS_TOKEN + "&group_ids=";
    public static final String VK_GROUP_GET = "https://api.vk.com/method/groups.getById?" + ACCESS_TOKEN + "&group_ids=";
}
