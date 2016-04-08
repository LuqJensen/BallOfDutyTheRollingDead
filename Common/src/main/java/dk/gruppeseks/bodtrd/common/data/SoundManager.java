/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author S
 */
public class SoundManager
{
    private static final ConcurrentLinkedQueue<SoundTask> SOUNDTASKS = new ConcurrentLinkedQueue<>();

    public static int getQuerySize()
    {
        return SOUNDTASKS.size();
    }

    public static SoundTask pollSoundTask()
    {
        return SOUNDTASKS.poll();
    }

    public static void addSoundTask(SoundTask st)
    {
        SOUNDTASKS.add(st);
    }

    public static void createSoundTask(String path, SoundAction soundAction)
    {
        try
        {
            String nPath = new File(path).getCanonicalPath().replace("\\", "/");
            SoundTask st = new SoundTask(nPath, soundAction);
            SOUNDTASKS.add(st);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static void createSoundTask(String path, SoundAction soundAction, float duration)
    {
        try
        {
            String nPath = new File(path).getCanonicalPath().replace("\\", "/");
            SoundTask st = new SoundTask(nPath, soundAction, duration);
            SOUNDTASKS.add(st);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
