package com;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main extends PApplet
{
    public static final String ACCESS_TOKEN = "ATLJzXE40oAAAAAAAAAAE21bu74W66UG8Afm1FMCKxH6-RMpSkoWw8uaqTVOY2aW";
    public DbxClientV2 client;
    public List<String> data;
    public SimpleDateFormat format;
    int offsetMain = 0;
    int delta = 1;


    public static void main(String[] args)
    {
        PApplet.main("com.Main");
    }

    @Override
    public void settings()
    {
        format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DbxRequestConfig config = new DbxRequestConfig("dropbox/VkData");
        client = new DbxClientV2(config, ACCESS_TOKEN);
        try(FileOutputStream fos = new FileOutputStream("data.txt"))
        {
            client.files().download("/VkData.txt").download(fos);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        File f = new File("data.txt");
        data = new ArrayList<>();
        try(FileInputStream fis = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis)))
        {
            while(br.ready())
            {
                String inp = br.readLine();
                Date t = format.parse(inp.substring(0, 19));
                Calendar cal = Calendar.getInstance();
                cal.setTime(t);
                cal.add(Calendar.HOUR_OF_DAY, 3);
                t = cal.getTime();
                data.add(format.format(t) + inp.substring(19));
                //System.out.println(data.get(data.size()-1));
            }
        }
        catch(Exception e)
        {}
        size(data.size(), 250);
        frameRate = 120;
        offsetMain = 1280 - data.size();
    }

    @Override
    public void draw()
    {
        background(0);
        for(int i = 0; i < data.size(); i++)
        {
            if(data.get(i).substring(20, 23).equals("0 0"))
            {
                stroke(255, 0 , 0);
                line(i + offsetMain , height-1, i + offsetMain, height);
                //point(i, 199);
            }
            else if(data.get(i).substring(20, 23).equals("1 1"))
            {
                stroke(255, 255 , 0);
                line(i + offsetMain , 120, i + offsetMain, height);
                //point(i, 100);
            }
            else
            {
                stroke(0, 255 , 0);
                line(i + offsetMain , 40, i + offsetMain, height);
                //point(i, 0);
            }
        }
        for(int i = 0; i < data.size(); i++)
        {
            if(i > 0 && !data.get(i).substring(11, 13).equals(data.get(i - 1).substring(11, 13)))
            {
                stroke(87, 87, 87, 87);
                line(i + offsetMain, 25, i + offsetMain, height);
                text(data.get(i).substring(11, 13), i + offsetMain - textWidth(data.get(0).substring(11, 13))/2, 20);
            }
            if(i > 0 && !data.get(i).substring(0, 10).equals(data.get(i - 1).substring(0, 10)))
            {
                strokeWeight(3);
                stroke(87, 87, 87, 87);
                line(i + offsetMain, 25, i + offsetMain, height);
                text(data.get(i).substring(0, 10), i  + offsetMain- textWidth(data.get(0).substring(0, 10))/2, 40);
                strokeWeight(1);
            }
        }
        stroke(255);
        line(offsetMain, 0, offsetMain, height);
        line(offsetMain + data.size(), 0, offsetMain + data.size(), height);
        textFont(createFont("Comic Sans MS", 20));
        if(mousePressed && mouseX - offsetMain >= 0 && mouseX - offsetMain < data.size())
        {
            float offset = textWidth(data.get(0).substring(0, 16))/2;
            if(mouseX + offset > width - 3)
            {
                offset = 2 * offset - width + mouseX + 3;
            }
            if(mouseX - offset < 3)
            {
                offset = mouseX - 3;
            }
            text(data.get(mouseX - offsetMain).substring(0, 16), mouseX - offset, mouseY);
            stroke(255,0 ,0, 87);
            line(mouseX, 0, mouseX, height);

        }
            //stroke(250);
        //point(10, 10);
    }

    @Override
    public void mouseWheel(MouseEvent event)
    {
        if(key == CODED && keyCode == SHIFT)
            delta += event.getCount() * 2;

            offsetMain += event.getCount() * delta;


    }
    //    @Override
//    public void mouseMoved()
//    {
//        if(mouseX >= 0 && mouseX < data.size())
//        text(data.get(mouseX), mouseX, mouseY);
//    }
}
