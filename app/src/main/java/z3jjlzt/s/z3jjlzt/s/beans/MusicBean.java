package z3jjlzt.s.z3jjlzt.s.beans;

/**
 * Created by s on 2016/3/6.
 */
public class MusicBean{
    String name;
    String singer;
    String location;

    public MusicBean() {
    }

    public MusicBean(String name, String singer, String location) {

        this.name = name;
        this.singer = singer;
        this.location = location;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
