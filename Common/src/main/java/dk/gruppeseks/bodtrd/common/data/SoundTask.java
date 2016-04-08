/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data;

/**
 *
 * @author S
 */
public class SoundTask
{

    private final String _soundFile;
    private final SoundAction _soundAction;
    private float _duration;

    public SoundTask(String path, SoundAction soundAction)
    {
        _duration = 0;
        _soundFile = path;
        _soundAction = soundAction;
    }

    public SoundTask(String path, SoundAction soundAction, float duration)
    {
        this(path, soundAction);
        this._duration = duration;
    }

    public String getSoundFilePath()
    {
        return _soundFile;
    }

    public SoundAction getAction()
    {
        return _soundAction;
    }

    public float getOptionalParam()
    {
        return _duration;
    }
}
