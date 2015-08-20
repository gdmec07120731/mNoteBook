package me.july.bean;

/**
 * Created by Rc3 on 2015/8/12.
 */
public class Image {
    /*
     *图片文件路径
     */
    private String dir;
    /*
    *第一张图片路径
     */
    private String firstImagePath;
    /*
    *文件夹名称
     */
    private String name;
    /**
     * 图片数量
     */
    private int count;


    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf=this.dir.lastIndexOf("/");
        this.name=this.dir.substring(lastIndexOf);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
