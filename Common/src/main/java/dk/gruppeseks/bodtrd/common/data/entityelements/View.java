/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data.entityelements;

/**
 *
 * @author frede
 */
public class View
{
    private final String _imageFile;
    private final boolean _repeatImage;

    public View(String path, boolean repeatImage)
    {
        _imageFile = path;
        _repeatImage = repeatImage;
    }

    public String getImageFilePath()
    {
        return _imageFile;
    }

    public boolean isRepeat() {
        return _repeatImage;
    }
}
