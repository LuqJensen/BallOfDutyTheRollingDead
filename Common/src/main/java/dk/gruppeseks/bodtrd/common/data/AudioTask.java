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
public class AudioTask
{

    private final String _audioFileName;
    private final AudioAction _audioAction;
    private float _duration;
    private final AudioType _audioType;

    public AudioTask(String audioFileName, AudioAction audioAction, AudioType audioType)
    {
        _duration = 0;
        _audioFileName = audioFileName;
        _audioAction = audioAction;
        _audioType = audioType;
    }

    public AudioTask(String audioFileName, AudioAction audioAction, AudioType audioType, float duration)
    {
        this(audioFileName, audioAction, audioType);
        this._duration = duration;
    }

    public String getAudioFileName()
    {
        return _audioFileName;
    }

    public AudioAction getAudioAction()
    {
        return _audioAction;
    }

    public float getDuration()
    {
        return _duration;
    }

    public AudioType getAudioType()
    {
        return _audioType;
    }
}
