package com.pat.utils;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

public class FontLoader {
    // resources/fonts/BorderWall-OG55o.otf
    // resources/fonts/CallOfOpsDutyIi-7Bgm4.ttf

    public static Font TITLE_FONT;
    public static Font SIDE_TITLE_FONT;
    public static Font MAIN_FONT;
    public static Font TEXT_FONT;

    public static void loadFonts(){
        try{
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        
            Font titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("pesotracker/src/main/resources/fonts/CallOfOpsDutyIi-7Bgm4.ttf"));
            Font mainFont = Font.createFont(Font.TRUETYPE_FONT, new File("pesotracker/src/main/resources/fonts/BorderWall-OG55o.otf"));
        
            ge.registerFont(titleFont);
            ge.registerFont(mainFont);
            TITLE_FONT = titleFont.deriveFont(64f);
            SIDE_TITLE_FONT = titleFont.deriveFont(48f);
            MAIN_FONT = mainFont.deriveFont(24f);
            TEXT_FONT = MAIN_FONT.deriveFont(18f);
            System.out.println(mainFont.getFamily());
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
