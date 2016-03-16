/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.common.data.entityelements;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author frede
 */
public class View
{
    private String _imageFile;

    public View(File file)
    {
        try
        {
            _imageFile = file.getCanonicalPath().replace("\\", "/");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();

        }
    }

    public String getImageFilePath()
    {

        return _imageFile;

    }

    public void setImageFile(File file)
    {
        try
        {
            _imageFile = file.getCanonicalPath().replace("\\", "/");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();

        }
    }

}
