package com.lanzdev.vk.wall;

public class Photo {

    String src;
    String srcBig;
    String srcSmall;
    String text;
    Long created;

    public Photo( ) {
    }

    public String getSrc( ) {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrcBig( ) {
        return srcBig;
    }

    public void setSrcBig(String srcBig) {
        this.srcBig = srcBig;
    }

    public String getSrcSmall( ) {
        return srcSmall;
    }

    public void setSrcSmall(String srcSmall) {
        this.srcSmall = srcSmall;
    }

    public String getText( ) {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCreated( ) {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @Override
    public String toString( ) {

        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("\n");
        sb.append(String.format("%8s%s: %s,", "", "src", "\"" + src + "\""));
        sb.append("\n");
        sb.append(String.format("%8s%s: %s,", "", "src_big", "\"" + srcBig + "\""));
        sb.append("\n");
        sb.append(String.format("%8s%s: %s,", "", "src_small", "\"" + srcSmall + "\""));
        sb.append("\n");
        sb.append(String.format("%8s%s: %s,", "", "text", "\"" + text + "\""));
        sb.append("\n");
        sb.append(String.format("%8s%s: %d", "", "created", created));
        sb.append("\n");

        return sb.toString();
    }
}
