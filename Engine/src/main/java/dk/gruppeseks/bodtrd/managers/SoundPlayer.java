/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import dk.gruppeseks.bodtrd.common.data.SoundManager;
import dk.gruppeseks.bodtrd.common.data.SoundTask;
import java.util.HashMap;

/**
 *
 * @author S
 */
public class SoundPlayer
{

    private static final HashMap<String, Sound> SOUNDS;

    static
    {
        SOUNDS = new HashMap<>();
    }

    public static void HandleSoundTasks()
    {
        int size = SoundManager.getQuerySize();
        SoundTask st = null;
        boolean isOptionalParam;
        for (int i = 0; i < size; i++)
        {
            st = SoundManager.pollSoundTask();
            isOptionalParam = st.getOptionalParam() != 0;
            SoundPlayer.load(st);
            if (isOptionalParam)
            {
                SoundPlayer.performAction(st, st.getOptionalParam());
            }
            else
            {
                SoundPlayer.performAction(st);
            }
        }
    }

    private static void load(SoundTask st)
    {
        if (!SOUNDS.containsKey(st.getSoundFilePath()))
        {
            String path = st.getSoundFilePath();
            Sound sound = Gdx.audio.newSound(Gdx.files.absolute(path));
            SOUNDS.put(st.getSoundFilePath(), sound);
        }
    }

    private static void unload(SoundTask st)
    {
        SOUNDS.remove(st.getSoundFilePath());
    }

    private static void performAction(SoundTask st)
    {
        switch (st.getAction())
        {
            case PLAY:
                SOUNDS.get(st.getSoundFilePath()).play();
                break;
            case LOOP:
                SOUNDS.get(st.getSoundFilePath()).loop();
                break;
            case STOP:
                SOUNDS.get(st.getSoundFilePath()).stop();
                break;
            case RESUME:
                SOUNDS.get(st.getSoundFilePath()).resume();
                break;
            case PAUSE:
                SOUNDS.get(st.getSoundFilePath()).pause();
                break;
            case DISPOSE:
                SOUNDS.get(st.getSoundFilePath()).dispose();
                break;
            default:
                break;
        }
    }

    private static void performAction(SoundTask st, float optionalParam)
    {
        switch (st.getAction())
        {
            case PLAY:
                SOUNDS.get(st.getSoundFilePath()).play(optionalParam);
                break;
            case LOOP:
                SOUNDS.get(st.getSoundFilePath()).loop(optionalParam);
                break;
            case STOP:
                SOUNDS.get(st.getSoundFilePath()).stop((long)optionalParam);
                break;
            case RESUME:
                SOUNDS.get(st.getSoundFilePath()).resume((long)optionalParam);
                break;
            case PAUSE:
                SOUNDS.get(st.getSoundFilePath()).pause((long)optionalParam);
                break;
            default:
                break;
        }
    }

}
