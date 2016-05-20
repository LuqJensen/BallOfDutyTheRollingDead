/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.services;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.Path;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.exceptions.NoPathException;

/**
 *
 * @author frede
 */
public interface AISPI
{
    public Path getPath(Entity entity, Entity target, World world) throws NoPathException;

}
