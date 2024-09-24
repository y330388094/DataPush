package org.example;

// 数据库连接信息
public class DBConnectVo {

    private String url;
    private String username;
    private String password;

    public DBConnectVo(String url, String username, String password)
    {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUrl(){
        return url;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
