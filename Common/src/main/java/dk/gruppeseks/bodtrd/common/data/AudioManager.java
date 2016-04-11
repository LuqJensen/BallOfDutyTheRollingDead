/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author S
 */
public class AudioManager
{
    private static final ConcurrentLinkedQueue<AudioTask> SOUNDTASKS = new ConcurrentLinkedQueue<>();
    public final static String MUSIC_DIRECTORY = "../../../Common/src/main/resources/musics";
    public final static String SOUND_DIRECTORY = "../../../Common/src/main/resources/sounds";

    public static int getQuerySize()
    {
        return SOUNDTASKS.size();
    }

    public static AudioTask pollSoundTask()
    {
        return SOUNDTASKS.poll();
    }

    public static void addSoundTask(AudioTask audioTask)
    {
        SOUNDTASKS.add(audioTask);
    }

    public static void createSoundTask(String audioFileName, AudioAction audioAction, AudioType audioType)
    {
        AudioTask audioTask = new AudioTask(audioFileName, audioAction, audioType);
        SOUNDTASKS.add(audioTask);
    }

    public static void createSoundTask(String audioFileName, AudioAction audioAction, AudioType audioType, float duration)
    {

        AudioTask audioTask = new AudioTask(audioFileName, audioAction, audioType, duration);
        SOUNDTASKS.add(audioTask);
    }
}
