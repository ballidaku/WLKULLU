package ballidaku.wlkullu.dataModels;

import java.util.List;

/**
 * Created by sharanpalsingh on 21/07/17.
 */

public class DetailModel
{
    String detailId;
    String titleId;
    String childId;
    String heading;
    String text;
    List<String> imagePathList=null;
    String videoPath;

    public String getChildId()
    {
        return childId;
    }

    public void setChildId(String childId)
    {
        this.childId = childId;
    }

    public String getHeading()
    {
        return heading;
    }

    public void setHeading(String heading)
    {
        this.heading = heading;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public List<String>  getImagePathList()
    {
        return imagePathList;
    }

    public void setImagePathList(List<String>  imagePathList)
    {
        this.imagePathList = imagePathList;
    }

    public String getVideoPath()
    {
        return videoPath;
    }

    public void setVideoPath(String videoPath)
    {
        this.videoPath = videoPath;
    }

    public String getDetailId()
    {
        return detailId;
    }

    public void setDetailId(String detailId)
    {
        this.detailId = detailId;
    }

    public String getTitleId()
    {
        return titleId;
    }

    public void setTitleId(String titleId)
    {
        this.titleId = titleId;
    }
}