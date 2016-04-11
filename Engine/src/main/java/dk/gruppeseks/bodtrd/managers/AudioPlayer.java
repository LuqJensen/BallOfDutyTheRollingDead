/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import dk.gruppeseks.bodtrd.common.data.AudioAction;
import dk.gruppeseks.bodtrd.common.data.AudioManager;
import dk.gruppeseks.bodtrd.common.data.AudioTask;
import dk.gruppeseks.bodtrd.common.data.AudioType;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author S
 */
public class AudioPlayer
{
    private AssetManager _aam;
    private String _soundPath;
    private String _musicPath;

    public AudioPlayer()
    {
        this._aam = new AssetManager();
    }

    public void loadMusics()
    {
        try
        {
            _musicPath = new File(AudioManager.MUSIC_DIRECTORY).getCanonicalPath().replace("\\", "/");
            FileHandle[] files = Gdx.files.internal(_musicPath).list();
            AssetDescriptor musicDesc;
            for (FileHandle fh : files)
            {
                musicDesc = new AssetDescriptor(fh, Music.class);
                _aam.load(musicDesc);
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex.toString());
        }
        _aam.finishLoading();
    }

    public void loadSounds()
    {
        try
        {

            _soundPath = new File(AudioManager.SOUND_DIRECTORY).getCanonicalPath().replace("\\", "/");
            FileHandle[] files = Gdx.files.internal(_soundPath).list();
            AssetDescriptor soundDesc;
            for (FileHandle fh : files)
            {
                soundDesc = new AssetDescriptor(fh, Sound.class);
                _aam.load(soundDesc);
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex.toString());
        }
        _aam.finishLoading();
    }

    public void loadAudio()
    {
        loadMusics();
        loadSounds();
    }

    public void updateAudio()
    {
        _aam.update();
    }

    public void unloadAudio()
    {
        _aam.clear();
    }

    public void disposeAudio()
    {
        _aam.dispose();
    }

    public void handleAudioTaskQuery()
    {
        int taskNum = AudioManager.getQuerySize();
        AudioTask audioTask = null;
        for (int i = 0; i < taskNum; i++)
        {
            audioTask = AudioManager.pollSoundTask();
            handleAudioTask(audioTask);
        }
    }

    private void handleAudioTask(AudioTask audioTask)
    {
        AudioAction audioAction = audioTask.getAudioAction();
        if (audioTask.getAudioType() == AudioType.SOUND)
        {

            Sound sound = _aam.get(_soundPath + "/" + audioTask.getAudioFileName(), Sound.class);
            switch (audioAction)
            {
                case PLAY:
                    sound.play();
                    break;
                case RESUME:
                    sound.resume();
                    break;
                case PAUSE:
                    sound.pause();
                    break;
                case STOP:
                    sound.stop();
                    break;
                case LOOP:
                    sound.loop();
                    break;
                case DISPOSE:
                    sound.dispose();
                    break;
                default:
                    break;
            }
        }
        else
        {
            Music music = _aam.get(_musicPath + "/" + audioTask.getAudioFileName(), Music.class);
            switch (audioAction)
            {
                case PLAY:
                    music.play();
                    break;
                case PAUSE:
                    music.pause();
                    break;
                default:
                    break;
            }
        }
    }
}
